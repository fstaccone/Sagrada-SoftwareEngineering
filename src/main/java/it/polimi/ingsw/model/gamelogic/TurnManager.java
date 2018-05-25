package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.*;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.responses.Response;
import it.polimi.ingsw.socket.responses.OtherTurnResponse;
import it.polimi.ingsw.socket.responses.ProposeWindowResponse;
import it.polimi.ingsw.socket.responses.ToolCardsResponse;
import it.polimi.ingsw.socket.responses.YourTurnResponse;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

public class TurnManager implements Runnable {

    private Timer turnTimer;
    private TurnTimer task;
    private int turnTime;
    private MatchMultiplayer match;
    private boolean expired; // it's used to avoid double canceling of timer

    public TurnManager(MatchMultiplayer match, int turnTime) {
        this.turnTime = turnTime;
        this.match = match;
    }

    @Override
    public void run() {
        try {
            // drawWindowPatternCards();
            turnManager();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Remote exception from TurnManager");
        }
    }

    /**
     * Toolcard e carte obiettivo privato e pubblico possono essere mostrate all'inizio del gioco insieme al WELCOME
     * così allegeriamo questo metodo
     */


    private void drawWindowPatternCard(PlayerMultiplayer player) throws RemoteException, InterruptedException {
        match.setWindowChosen(false);

        // initialize windows
        match.windowsToBeProposed();
        List<String> windows;
        windows = match.getWindowsProposed().stream().map(WindowPatternCard::toString).collect(Collectors.toList());

        //starting notification
        MatchObserver rmiObserver = rmiObserverNotify(player);
        if (rmiObserver != null) {
            rmiObserver.onToolCards(match.getDecksContainer().getToolCardDeck().getPickedCards().toString());
            rmiObserver.onWindowChoise(windows);
        }

        if (match.getSocketObservers().get(player) != null) {
            socketObserverNotify(player, new ToolCardsResponse(match.getDecksContainer().getToolCardDeck().getPickedCards().toString()));
            socketObserverNotify(player, new ProposeWindowResponse(windows));
        }

        waitForSchemeChoise();

    }


    private void waitForSchemeChoise() throws InterruptedException {
        while (!(match.isWindowChosen() || match.isEndsTurn())) {
            synchronized (match.getLock()) {
                match.getLock().wait();
            }
        }
    }


    private void initializeRound() {
        match.getPlayers().forEach(player -> player.setTurnsLeft(2));
        match.getBoard().getReserve().throwDices(match.getBag().pickDices(match.getPlayers().size()));
    }

    // initialisation of flags to control the turn's flow
    private void initializeTurn(PlayerMultiplayer player) {
        match.setDiceAction(false);
        match.setToolAction(false);
        match.setEndsTurn(false);
        match.setSecondDiceAction(true); // this is useful only when a player can play two dices in the same turn
        player.setMyTurn(true);
    }

    private void setTimer(PlayerMultiplayer player) {
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

    private void notifyTurnBeginning(PlayerMultiplayer player) throws RemoteException {
        MatchObserver rmiObserver = rmiObserverNotify(player);

        if (rmiObserver != null) {
            rmiObserver.onYourTurn(true, match.getBoard().getReserve().getDices().toString());
        }
        if (match.getSocketObservers().get(player) != null) {
            socketObserverNotify(player, new YourTurnResponse(true, match.getBoard().getReserve().getDices().toString()));
        }
        notifyOthers(player);
    }

    private void notifyTurnEnd(PlayerMultiplayer player) throws RemoteException {
        MatchObserver rmiObserver = rmiObserverNotify(player);

        if (rmiObserver != null) {
            rmiObserver.onYourTurn(false, match.getBoard().getReserve().getDices().toString());
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
            match.getSocketObservers().get(player).reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notifyOthers(PlayerMultiplayer player) throws RemoteException {
        Response response = new OtherTurnResponse(player.getName());
        for (PlayerMultiplayer playerNotInTurn : match.getPlayers())
            if (playerNotInTurn != player) {
                if (rmiObserverNotify(playerNotInTurn) != null)
                    rmiObserverNotify(playerNotInTurn).onOtherTurn(player.getName());
                if (match.getSocketObservers().get(playerNotInTurn) != null) {
                    socketObserverNotify(playerNotInTurn, response);
                }
            }
    }

    private boolean checkCondition() {
        return !((match.isToolAction() && match.isDiceAction() && match.isSecondDiceAction()) || match.isEndsTurn());
    }

    private void nextRound() throws InterruptedException, RemoteException {
        match.pushLeftDicesToRoundTrack();
        match.incrementRoundCounter();

        if (match.getCurrentRound() >= 10) {
            //match.calculateFinalScore(); // può stare anche in match
        } else {
            this.turnManager();
        }
    }

    public void setExpiredTrue() {
        this.expired = true;
    }
}