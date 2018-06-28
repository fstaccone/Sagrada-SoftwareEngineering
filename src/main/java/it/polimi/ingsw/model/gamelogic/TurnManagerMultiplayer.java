package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.view.MatchObserver;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.responses.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

public class TurnManagerMultiplayer implements Runnable {

    private static final int NUM_ROUNDS = 10;
    private Timer turnTimer;
    private int turnTime;
    private MatchMultiplayer match;
    private boolean timerExpired; // it's used to avoid double canceling of timer
    private int currentTurn;

    TurnManagerMultiplayer(MatchMultiplayer match, int turnTime) {
        this.turnTime = turnTime;
        this.match = match;
        timerExpired = false;
    }

    public boolean isTimerExpired() { return timerExpired; }

    /**
     * sets the boolean to true in order to avoid double canceling of the timer when the timer expires
     */
    public void setTimerExpiredTrue() {
        this.timerExpired = true;
    }

    @Override
    public void run() {
        try {
            initializeClients();
            turnManager();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Remote exception from TurnManagerMultiplayer");
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
                rmiObserver.onWindowChoice(windows);
            } catch (RemoteException e) {
                match.getLobby().disconnect(player.getName());
            }
        } else if (match.getSocketObservers().get(player) != null) {
            getObserverSocket(player, new ProposeWindowResponse(windows));
        }

        waitForSchemeChoise();
    }

    private void initializeRound() {
        match.getPlayers().forEach(player -> player.setTurnsLeft(2));
        match.getBoard().getReserve().throwDices(match.getBag().pickDices(match.getPlayers().size() * 2 + 1));
    }


    private void initializeClients() {

        String toolCards = match.getDecksContainer().getToolCardDeck().getPickedCards().toString();
        String publicCards = match.getDecksContainer().getPublicObjectiveCardDeck().getPickedCards().toString();
        List<String> names = match.getPlayers().stream().map(PlayerMultiplayer::getName).collect(Collectors.toList());
        // Notification RMI and Socket
        for (PlayerMultiplayer p : match.getPlayers()) {
            List<String> privateCard = new ArrayList<>();
            privateCard.add(p.getPrivateObjectiveCard().toString());
            if (getObserverRmi(p) != null) {
                try {
                    getObserverRmi(p).onInitialization(toolCards, publicCards, privateCard, names);
                } catch (RemoteException e) {
                    match.getLobby().disconnect(p.getName());
                }
            } else if (match.getSocketObservers().get(p) != null) {
                getObserverSocket(p, new InitializationResponse(toolCards, publicCards, privateCard, names));
            }
        }
    }

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
        while (match.checkCondition()) {
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

    private void playFirstTurn() throws InterruptedException {
        for (int i = 0; i < match.getPlayers().size(); i++) {

            PlayerMultiplayer player = match.getPlayers().get(i);
            initializeTurn(player);

            if (player.getStatus() == ConnectionStatus.CONNECTED) {
                playTurnCore(player);
            }
            terminateTurn(player);
        }
    }

    private void playSecondTurn() throws InterruptedException {

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
                rmiObserver.onYourTurn(true, match.getBoard().getReserve().getDices().toString(), match.getCurrentRound() + 1, currentTurn);
            } catch (RemoteException e) {
                match.getLobby().disconnect(player.getName());
            }
        }

        if (match.getSocketObservers().get(player) != null) {

            getObserverSocket(player, new YourTurnResponse(true, match.getBoard().getReserve().getDices().toString(), match.getCurrentRound() + 1, currentTurn));
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
                rmiObserver.onYourTurn(false, null, match.getCurrentRound() + 1, currentTurn);
            } catch (RemoteException e) {
                match.getLobby().disconnect(player.getName());
            }
        }
        if (match.getSocketObservers().get(player) != null) {
            getObserverSocket(player, new YourTurnResponse(false, null, match.getCurrentRound() + 1, currentTurn));
        }

    }

    /**
     * manages the core actions of the turn, useful to maintain the right flow during the turn of the player.
     *
     * @param player is the player who is playing this turn
     * @throws InterruptedException
     */
    private void playTurnCore(PlayerMultiplayer player) throws InterruptedException {
        notifyTurnBeginning(player);
        setTimer(player);
        play(player);
        notifyTurnEnd(player);
        if (!timerExpired) {
            turnTimer.cancel();
        }
        timerExpired = false;
    }

    /**
     * checks if the player has the scheme card and waits for his actions
     *
     * @param player is the player who is playing this turn
     * @throws InterruptedException
     */
    private void play(PlayerMultiplayer player) throws InterruptedException {

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
        if (match.isStillPlaying()) {
            initializeRound();
        }

        currentTurn = 1;
        if (match.isStillPlaying()) {
            playFirstTurn();
        }

        currentTurn = 2;
        if (match.isStillPlaying()) {
            playSecondTurn();
        }

        if (match.isStillPlaying()) {
            terminateRound();
        }
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

    private void nextRound() throws RemoteException, InterruptedException {
        match.pushLeftDicesToRoundTrack();
        match.incrementRoundCounter();

        Response roundTrackResponse = new RoundTrackResponse(match.getBoard().getRoundTrack().toString());
        for (PlayerMultiplayer player : match.getPlayers()) {
            if (getObserverRmi(player) != null)
                try {
                    getObserverRmi(player).onRoundTrack(match.getBoard().getRoundTrack().toString());
                } catch (RemoteException e) {
                    match.getLobby().disconnect(player.getName());
                }
            if (match.getSocketObservers().get(player) != null) {
                getObserverSocket(player, roundTrackResponse);
            }
        }

        Response reserveResponse = new ReserveResponse(match.getBoard().getReserve().toString());

        for (PlayerMultiplayer player : match.getPlayers()) {
            if (getObserverRmi(player) != null)
                try {
                    getObserverRmi(player).onReserve(match.getBoard().getReserve().toString());
                } catch (RemoteException e) {
                    match.getLobby().disconnect(player.getName());
                }
            if (match.getSocketObservers().get(player) != null) {
                getObserverSocket(player, reserveResponse);
            }
        }

        if (match.getCurrentRound() >= NUM_ROUNDS) {
            match.calculateFinalScore();
            match.setStillPlaying(false);
            match.deleteDisconnectedClients();
        } else {
            this.turnManager();
        }
    }
}