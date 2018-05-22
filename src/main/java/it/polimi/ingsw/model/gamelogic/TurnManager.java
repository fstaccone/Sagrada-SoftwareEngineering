package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.*;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

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
            drawWindowPatternCards();
            turnManager();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Remote exception from TurnManager");
        }
    }

    private void drawWindowPatternCards() throws InterruptedException, RemoteException {

        for (int i = 0; i < match.getPlayers().size(); i++) {
            PlayerMultiplayer player;
            player = match.getPlayers().get(i);

            match.initializeWindowsToBeProposed(i);
            match.setWindowChosen(false);

            if (player.getStatus() == ConnectionStatus.READY) {

                List<String> list = match.getWindowsProposed()
                        .stream()
                        .map(WindowPatternCard::toString)
                        .collect(Collectors.toList());

                //notification
                if (match.getRemoteObservers().size() != 0) {
                    rmiObserverNotify(player).onToolCards(match.getDecksContainer().getToolCardDeck().getPickedCards().toString());
                    rmiObserverNotify(player).onYourTurn(true, null);
                    rmiObserverNotify(player).onWindowChoise(list);
                    for (PlayerMultiplayer playerNotInTurn : match.getPlayers())
                        if (playerNotInTurn != player)
                            rmiObserverNotify(playerNotInTurn).onOtherTurn(player.getName());
                }
                if (match.getSocketObservers().size() != 0) {
                    socketObserverNotify(player, new YourTurnResponse(true, null));
                    socketObserverNotify(player, new ProposeWindowResponse(list));
                }


                setTimer(player);

                while (!match.isWindowChosen()) {
                    synchronized (match.getLock()) {
                        match.getLock().wait();
                    }
                }

                if (!expired) {
                    turnTimer.cancel();
                }

                //notification
                if (match.getRemoteObservers().size() != 0) {
                    rmiObserverNotify(player).onYourTurn(false, null);
                }
                if (match.getSocketObservers().size() != 0) {
                    socketObserverNotify(player, new YourTurnResponse(false, null));
                }
            }
        }
    }

    private void initializeRound() {
        for (PlayerMultiplayer player : match.getPlayers()) {
            player.setTurnsLeft(2);
        }
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

            if (player.getStatus() == ConnectionStatus.READY) {
                playTurnCore(player);
            }
            terminateTurn(player);
        }
    }

    private void playSecondTurn() throws RemoteException, InterruptedException {

        for (int i = match.getPlayers().size() - 1; i >= 0; i--) {

            PlayerMultiplayer player = match.getPlayers().get(i);
            initializeTurn(player);

            if (player.getTurnsLeft() > 0 && player.getStatus() == ConnectionStatus.READY) {
                playTurnCore(player);
            }
            terminateTurn(player);
        }
    }

    private void playTurnCore(PlayerMultiplayer player) throws RemoteException, InterruptedException {
        //notification
        if (match.getRemoteObservers().size() != 0) {
            rmiObserverNotify(player).onYourTurn(true, match.getBoard().getReserve().getDices().toString());
            for (PlayerMultiplayer playerNotInTurn : match.getPlayers())
                if (!playerNotInTurn.equals(player) && playerNotInTurn.getStatus().equals(ConnectionStatus.READY)) {
                    rmiObserverNotify(playerNotInTurn).onOtherTurn(player.getName());
                }
        }
        if (match.getSocketObservers().size() != 0) {
            socketObserverNotify(player, new YourTurnResponse(true, match.getBoard().getReserve().getDices().toString()));
        }

        setTimer(player);

        waitForUserActions();

        //notification
        if (match.getRemoteObservers().size() != 0 && player.getStatus().equals(ConnectionStatus.READY)) {
            rmiObserverNotify(player).onYourTurn(false, match.getBoard().getReserve().getDices().toString());
        }
        if (match.getSocketObservers().size() != 0 && player.getStatus().equals(ConnectionStatus.READY)) {
            socketObserverNotify(player, new YourTurnResponse(false, match.getBoard().getReserve().getDices().toString()));
        }

        if (!expired) {
            turnTimer.cancel();
        }
    }

    private void turnManager() throws InterruptedException, RemoteException {

        initializeRound();

        playFirstTurn();

        playSecondTurn();

        terminateRound();
    }

    private MatchObserver rmiObserverNotify(PlayerMultiplayer player) {
        if (match.getRemoteObservers().get(player) != null) {
            return match.getRemoteObservers().get(player);
        } else return null;
    }

    private void socketObserverNotify(PlayerMultiplayer player, Response response) {
        if (match.getSocketObservers().get(player) != null) {
            try {
                match.getSocketObservers().get(player).writeObject(response);
                match.getSocketObservers().get(player).reset();
            } catch (IOException e) {
                e.printStackTrace();
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
