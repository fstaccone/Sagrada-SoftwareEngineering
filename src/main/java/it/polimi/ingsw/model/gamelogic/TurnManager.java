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
        }
    }

    private void drawWindowPatternCard(PlayerMultiplayer player) throws RemoteException, InterruptedException {
        match.setWindowChosen(false);

        // initialize windows
        match.windowsToBeProposed();
        List<String> windows;
        windows = match.getWindowsProposed().stream().map(WindowPatternCard::toString).collect(Collectors.toList());

        //starting notification
        MatchObserver rmiObserver = rmiObserverNotify(player);
        if (rmiObserver != null) {
            rmiObserver.onWindowChoise(windows);
        } else if (match.getSocketObservers().get(player) != null) {
            socketObserverNotify(player, new ProposeWindowResponse(windows));
        }

        waitForSchemeChoise();
    }

    private void initializeRound() {
        match.getPlayers().forEach(player -> player.setTurnsLeft(2));
        match.getBoard().getReserve().throwDices(match.getBag().pickDices(match.getPlayers().size()));
    }

    private void initializeClients() throws RemoteException {

        String toolCards = match.getDecksContainer().getToolCardDeck().getPickedCards().toString();
        String publicCards = match.getDecksContainer().getPublicObjectiveCardDeck().getPickedCards().toString();
        List<String> names = match.getPlayers().stream().map(PlayerMultiplayer::getName).collect(Collectors.toList());


        // Notification RMI and Socket
        for (PlayerMultiplayer p : match.getPlayers()) {
            if (rmiObserverNotify(p) != null) {
                rmiObserverNotify(p).onInitialization(toolCards, publicCards, p.getPrivateObjectiveCard().toString(), names);
            } else if (match.getSocketObservers().get(p) != null) {
                socketObserverNotify(p, new InitializationResponse(toolCards, publicCards, p.getPrivateObjectiveCard().toString(), names));
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

    // initialisation of flags to control the turn's flow
    private void initializeTurn(PlayerMultiplayer player) {
        match.setDiceAction(false);
        match.setToolAction(false);
        match.setEndsTurn(false);
        player.setMyTurn(true);
    }

    private void setTimer(PlayerMultiplayer player) {
        TurnTimer task;

        turnTimer = new Timer();
        task = new TurnTimer(match, player);
        turnTimer.schedule(task, turnTime);
    }

    /**
     * diceAction e toolAction vengono settati inizialmente a false, se l'azione corrispondente viene
     * completata con successo viene settato a true il rispettivo flag. Quando saranno entrambi veri
     * la condizione del ciclo sarà falsa
     * I metodi utilizzati ed i flag appartengono a match, in modo che possano essere settati a true senza risvegliare
     * TurnManager, sarà risvegliato solo dopo che una azione è stata completata con successo
     */
    private void waitForUserActions() throws InterruptedException {
        while (checkCondition()) {
            synchronized (match.getLock()) {
                match.getLock().wait();
            }
        }
    }

    private void terminateTurn(PlayerMultiplayer player) {
        player.setTurnsLeft(player.getTurnsLeft() - 1);
        player.setMyTurn(false);
    }

    /**
     * Rearrange match.getPlayers() to keep the right order in next round
     * following the idea that the first player in this round will be the last in the next round
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
        MatchObserver rmiObserver = rmiObserverNotify(player);

        if (rmiObserver != null) {
            try {
                rmiObserver.onYourTurn(true, match.getBoard().getReserve().getDices().toString());
            } catch (RemoteException e) {
                match.getLobby().disconnect(player.getName());
            }
        }

        if (match.getSocketObservers().get(player) != null) {

            socketObserverNotify(player, new YourTurnResponse(true, match.getBoard().getReserve().getDices().toString()));
        }
        notifyOthers(player);
    }

    private void notifyTurnEnd(PlayerMultiplayer player) {
        MatchObserver rmiObserver = rmiObserverNotify(player);
        if (rmiObserver != null) {
            try {
                rmiObserver.onYourTurn(false, match.getBoard().getReserve().getDices().toString());
            } catch (RemoteException e) {
                match.getLobby().disconnect(player.getName());
            }
        }
        if (match.getSocketObservers().get(player) != null) {
            socketObserverNotify(player, new YourTurnResponse(false, null));
        }

    }

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

    private void play(PlayerMultiplayer player) throws RemoteException, InterruptedException {

        if (!player.isSchemeCardSet()) {
            drawWindowPatternCard(player);
        }
        waitForUserActions();
    }

    private void turnManager() throws InterruptedException, RemoteException {
        initializeRound();

        playFirstTurn();

        playSecondTurn();

        terminateRound();
    }

    private MatchObserver rmiObserverNotify(PlayerMultiplayer player) {
        return match.getRemoteObservers().get(player);
    }

    private void socketObserverNotify(PlayerMultiplayer player, Response response) {
        try {
            match.getSocketObservers().get(player).writeObject(response);
           // match.getSocketObservers().get(player).reset();
        } catch (IOException e) {
            match.getLobby().disconnect(player.getName());
        }
    }

    private void notifyOthers(PlayerMultiplayer player) {
        Response response = new OtherTurnResponse(player.getName());
        for (PlayerMultiplayer playerNotInTurn : match.getPlayers()) {
            if (playerNotInTurn != player) {
                if (rmiObserverNotify(playerNotInTurn) != null)
                    try {
                        rmiObserverNotify(playerNotInTurn).onOtherTurn(player.getName());
                    } catch (RemoteException e) {
                        match.getLobby().disconnect(player.getName());
                    }
                if (match.getSocketObservers().get(playerNotInTurn) != null) {
                    socketObserverNotify(playerNotInTurn, response);
                }
            }
        }
    }

    private boolean checkCondition() {
        return !((match.isToolAction() && match.isDiceAction()) || match.isEndsTurn());
    }

    private void nextRound() throws RemoteException, InterruptedException {
        match.pushLeftDicesToRoundTrack();
        match.incrementRoundCounter();
        
        Response response1 = new RoundTrackResponse(match.getBoard().getRoundTrack().toString());
        for (PlayerMultiplayer player : match.getPlayers()) {
            if (rmiObserverNotify(player) != null)
                try {
                    rmiObserverNotify(player).onRoundTrack(match.getBoard().getRoundTrack().toString());
                } catch (RemoteException e) {
                    match.getLobby().disconnect(player.getName());
                }
            if (match.getSocketObservers().get(player) != null) {
                socketObserverNotify(player, response1);
            }
        }

        Response response2 = new ReserveResponse(match.getBoard().getReserve().toString());
        for (PlayerMultiplayer player : match.getPlayers()) {
            if (rmiObserverNotify(player) != null)
                try {
                    rmiObserverNotify(player).onReserve(match.getBoard().getReserve().toString());
                } catch (RemoteException e) {
                    match.getLobby().disconnect(player.getName());
                }
            if (match.getSocketObservers().get(player) != null) {
                socketObserverNotify(player, response2);
            }
        }

        if (match.getCurrentRound() >= NUM_ROUNDS) {
            match.calculateFinalScore();
            // todo: Notifica a tutti
        } else {
            this.turnManager();
        }
    }

    void setExpiredTrue() { this.expired = true; }
}