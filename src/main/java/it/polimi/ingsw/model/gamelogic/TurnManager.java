package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.MatchObserver;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.responses.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

public class TurnManager implements Runnable {

    private final static int NUM_ROUNDS = 10;
    private Timer turnTimer;
    private int turnTime;
    private MatchMultiplayer match;
    private boolean expired; // it's used to avoid double canceling of timer

    TurnManager(MatchMultiplayer match, int turnTime) {
        this.turnTime = turnTime;
        this.match = match;
        expired = false;
    }

    /**
     * sets the boolean to true in order to avoid double canceling of the timer when the timer expires
     */
    void setExpiredTrue() {
        this.expired = true;
    }


    @Override
    public void run() {
        try {
            initializeClients();
            turnManager();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Remote exception from TurnManager");
            Thread.currentThread().interrupt();
        }
    }

    private void drawWindowPatternCard(PlayerMultiplayer player) throws InterruptedException {
        match.setWindowChosen(false);

        // initialize windows
        match.windowsToBeProposed();
        List<String> windows;
        windows = match.getWindowsProposed().stream().map(WindowPatternCard::toString).collect(Collectors.toList());

        //starting notification
        MatchObserver rmiObserver = getObserverRmi(player);
        if (rmiObserver != null) {
            try {
                rmiObserver.onWindowChoise(windows);
            } catch (RemoteException e) {
                match.getLobby().disconnect(player.getName());
            }
        } else if (match.getSocketObservers().get(player) != null) {
            getObserverSocket(player, new ProposeWindowResponse(windows));
        }

        waitForSchemeChoise();
    }

    /**
     *
     */
    private void initializeRound() {
        match.getPlayers().forEach(player -> player.setTurnsLeft(2));
        match.getBoard().getReserve().throwDices(match.getBag().pickDices(match.getPlayers().size()));
    }


    private void initializeClients() {

        String toolCards = match.getDecksContainer().getToolCardDeck().getPickedCards().toString();
        String publicCards = match.getDecksContainer().getPublicObjectiveCardDeck().getPickedCards().toString();
        List<String> names = match.getPlayers().stream().map(PlayerMultiplayer::getName).collect(Collectors.toList());


        // Notification RMI and Socket
        for (PlayerMultiplayer p : match.getPlayers()) {
            if (getObserverRmi(p) != null) {
                try {
                    getObserverRmi(p).onInitialization(toolCards, publicCards, p.getPrivateObjectiveCard().toString(), names);
                } catch (RemoteException e) {
                    match.getLobby().disconnect(p.getName());
                }
            } else if (match.getSocketObservers().get(p) != null) {
                getObserverSocket(p, new InitializationResponse(toolCards, publicCards, p.getPrivateObjectiveCard().toString(), names));
            }
        }
    }

    /**
     * @throws InterruptedException
     */
    private void waitForSchemeChoise() throws InterruptedException {
        while (!(match.isWindowChosen() || match.isEndsTurn())) {
            synchronized (match.getLock()) {
                match.getLock().wait();
            }
        }
    }

    /**
     * initialization of flags to control the turn's flow
     *
     * @param player is the representation of the player in the model
     */
    private void initializeTurn(PlayerMultiplayer player) {
        match.setDiceAction(false);
        match.setToolAction(false);
        match.setEndsTurn(false);
        player.setMyTurn(true);
    }

    /**
     * sets the timer to force the passing of the turn for the player if timer expires
     *
     * @param player is the representation of the player in the model
     */
    private void setTimer(PlayerMultiplayer player) {
        TurnTimer task;

        turnTimer = new Timer();
        task = new TurnTimer(match, player);
        turnTimer.schedule(task, turnTime);
    }

    /**
     * wait for the user actions until he can perform something else.
     */
    private void waitForUserActions() throws InterruptedException {
        while (checkCondition()) {
            synchronized (match.getLock()) {
                match.getLock().wait();
            }
        }
    }

    /**
     * update the number of turns left for the player and set its myTurn to false
     *
     * @param player is the player who is playing the current turn
     */
    private void terminateTurn(PlayerMultiplayer player) {
        player.setTurnsLeft(player.getTurnsLeft() - 1);
        player.setMyTurn(false);
    }

    /**
     * Rearrange match.getPlayers() to keep the right order in next round
     * following the idea that the first player in this round will be the last in the next round
     *
     * @throws RemoteException
     * @throws InterruptedException
     */
    private void terminateRound() throws RemoteException, InterruptedException {
        match.getPlayers().add(match.getPlayers().get(0));
        match.getPlayers().remove(0);
        nextRound();
    }

    private void playFirstTurn() throws RemoteException, InterruptedException {
        for (int i = 0; i < match.getPlayers().size(); i++) {

            PlayerMultiplayer player = match.getPlayers().get(i);
            initializeTurn(player);

            if (player.getStatus() == ConnectionStatus.CONNECTED) {
                playTurnCore(player);
            }
            terminateTurn(player);
        }
    }

    private void playSecondTurn() throws RemoteException, InterruptedException {

        for (int i = match.getPlayers().size() - 1; i >= 0; i--) {

            PlayerMultiplayer player = match.getPlayers().get(i);
            initializeTurn(player);

            if (player.getTurnsLeft() > 0 && player.getStatus() == ConnectionStatus.CONNECTED) {
                playTurnCore(player);
            }

            terminateTurn(player);
        }
    }

    private void notifyTurnBeginning(PlayerMultiplayer player) {
        MatchObserver rmiObserver = getObserverRmi(player);

        if (rmiObserver != null) {
            try {
                rmiObserver.onYourTurn(true, match.getBoard().getReserve().getDices().toString());
            } catch (RemoteException e) {
                match.getLobby().disconnect(player.getName());
            }
        }

        if (match.getSocketObservers().get(player) != null) {

            getObserverSocket(player, new YourTurnResponse(true, match.getBoard().getReserve().getDices().toString()));
        }
        notifyOthers(player);
    }

    /**
     * notifies that the player has ended his turn
     *
     * @param player is the player is playing this turn
     */
    private void notifyTurnEnd(PlayerMultiplayer player) {
        MatchObserver rmiObserver = getObserverRmi(player);
        if (rmiObserver != null) {
            try {
                rmiObserver.onYourTurn(false, null);
            } catch (RemoteException e) {
                match.getLobby().disconnect(player.getName());
            }
        }
        if (match.getSocketObservers().get(player) != null) {
            getObserverSocket(player, new YourTurnResponse(false, null));
        }

    }

    /**
     * manages the core actions of the turn, useful to maintain the right flow during the turn of the player.
     *
     * @param player is the player who is playing this turn
     * @throws RemoteException
     * @throws InterruptedException
     */
    private void playTurnCore(PlayerMultiplayer player) throws RemoteException, InterruptedException {
        notifyTurnBeginning(player);
        setTimer(player);
        play(player);
        notifyTurnEnd(player);
        if (!expired) {
            turnTimer.cancel();
            expired = false;
        }
    }

    /**
     * checks if the player has the scheme card and waits for his actions
     *
     * @param player is the player who is playing this turn
     * @throws RemoteException
     * @throws InterruptedException
     */
    private void play(PlayerMultiplayer player) throws RemoteException, InterruptedException {

        if (!player.isSchemeCardSet()) {
            drawWindowPatternCard(player);
        }
        waitForUserActions();
    }

    /**
     * manages the rounds' and turns' flow
     *
     * @throws InterruptedException
     * @throws RemoteException
     */
    private void turnManager() throws InterruptedException, RemoteException {
        initializeRound();

        playFirstTurn();

        playSecondTurn();

        terminateRound();
    }

    /**
     * selects the right observer rmi in the map of rmi clients
     *
     * @param player is the player who will receive the notification
     * @return the observer linked to the player
     */
    private MatchObserver getObserverRmi(PlayerMultiplayer player) {
        return match.getRemoteObservers().get(player);
    }

    /**
     * selects the right OoutputObjectStream in the map of socket clients
     *
     * @param player is the player who will receive the notification
     */
    private void getObserverSocket(PlayerMultiplayer player, Response response) {
        try {
            match.getSocketObservers().get(player).writeObject(response);
        } catch (IOException e) {
            match.getLobby().disconnect(player.getName());
        }
    }


    private void notifyOthers(PlayerMultiplayer player) {
        Response response = new OtherTurnResponse(player.getName());
        for (PlayerMultiplayer playerNotInTurn : match.getPlayers()) {
            if (playerNotInTurn != player) {
                if (getObserverRmi(playerNotInTurn) != null)
                    try {
                        getObserverRmi(playerNotInTurn).onOtherTurn(player.getName());
                    } catch (RemoteException e) {
                        match.getLobby().disconnect(playerNotInTurn.getName());
                    }
                if (match.getSocketObservers().get(playerNotInTurn) != null) {
                    getObserverSocket(playerNotInTurn, response);
                }
            }
        }
    }

    /**
     * checks if the player has performed all actions during his turn or has decided to pass.
     *
     * @return true if the player can perform other actions
     */
    private boolean checkCondition() {
        return !((match.isToolAction() && match.isDiceAction()) || match.isEndsTurn());
    }

    private void nextRound() throws RemoteException, InterruptedException {
        match.pushLeftDicesToRoundTrack();
        match.incrementRoundCounter();

        Response response1 = new RoundTrackResponse(match.getBoard().getRoundTrack().toString());
        for (PlayerMultiplayer player : match.getPlayers()) {
            if (getObserverRmi(player) != null)
                try {
                    getObserverRmi(player).onRoundTrack(match.getBoard().getRoundTrack().toString());
                } catch (RemoteException e) {
                    match.getLobby().disconnect(player.getName());
                }
            if (match.getSocketObservers().get(player) != null) {
                getObserverSocket(player, response1);
            }
        }

        Response response2 = new ReserveResponse(match.getBoard().getReserve().toString());

        for (PlayerMultiplayer player : match.getPlayers()) {
            if (getObserverRmi(player) != null)
                try {
                    getObserverRmi(player).onReserve(match.getBoard().getReserve().toString());
                } catch (RemoteException e) {
                    match.getLobby().disconnect(player.getName());
                }
            if (match.getSocketObservers().get(player) != null) {
                getObserverSocket(player, response2);
            }
        }

        if (match.getCurrentRound() >= NUM_ROUNDS) {
            match.calculateFinalScore();
        } else {
            this.turnManager();
        }
    }
}