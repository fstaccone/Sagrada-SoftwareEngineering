package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.responses.InitializationResponse;
import it.polimi.ingsw.socket.responses.ProposeWindowResponse;
import it.polimi.ingsw.socket.responses.ReserveResponse;
import it.polimi.ingsw.socket.responses.RoundTrackResponse;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

public class TurnManagerSingleplayer implements Runnable {

    private final static int NUM_ROUNDS = 10;
    private final static int DICES_NUM = 4;
    private int turnTime;
    private MatchSingleplayer match;
    private Timer turnTimer;
    private boolean expired; // it's used to avoid double canceling of timer

    TurnManagerSingleplayer(MatchSingleplayer match, int turnTime) {
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
        inizializeClient();
        try {
            turnManager();
        } catch (InterruptedException e) {
            match.terminateMatch();
            System.out.println("Match singleplayer interrotto");
        }
    }

    private void inizializeClient() {

        String toolCards = match.getDecksContainer().getToolCardDeck().getPickedCards().toString();
        String publicCards = match.getDecksContainer().getPublicObjectiveCardDeck().getPickedCards().toString();

        if (match.getObserverRmi() != null) {
            try { // todo: può andare così? board può contenere le private cards
                match.getObserverRmi().onInitialization(toolCards, publicCards, null, null);
            } catch (RemoteException e) {
                match.terminateMatch();
                System.out.println("Match singleplayer interrotto");
            }
        } else if (match.getObserverSocket() != null) {
            try {
                match.getObserverSocket().writeObject(new InitializationResponse(toolCards, publicCards, null, null));
                match.getObserverSocket().reset();
            } catch (IOException e) {
                match.terminateMatch();
                System.out.println("Match singleplayer interrotto");
            }
        }
    }

    private void turnManager() throws InterruptedException {

        initializeRound();

        // turn one
        playTurn();

        // turn two
        playTurn();

        nextRound();

    }

    private void initializeRound() {
        match.getPlayer().setTurnsLeft(2);
        match.getBoard().getReserve().throwDices(match.getBag().pickDices(DICES_NUM));
    }

    /**
     * manages the core actions of the turn, useful to maintain the right flow during the turn of the player.
     *
     * @throws InterruptedException due to waitForSchemeChoise
     */
    private void playTurn() throws InterruptedException {
        initializeTurn();

        if (!match.getPlayer().isSchemeCardSet()) {
            drawWindowPatternCard();
        }

        waitForUserActions();

        if (!expired) {
            turnTimer.cancel();
        }
        expired = false;

        match.getPlayer().setTurnsLeft(match.getPlayer().getTurnsLeft() - 1);
    }

    private void drawWindowPatternCard() throws InterruptedException {
        match.setWindowChosen(false);

        // initialize windows
        match.windowsToBeProposed();
        List<String> windows;
        windows = match.getWindowsProposed().stream().map(WindowPatternCard::toString).collect(Collectors.toList());

        //starting notification
        if (match.getObserverRmi() != null) {
            try {
                match.getObserverRmi().onWindowChoise(windows);
            } catch (RemoteException e) {
                match.terminateMatch();
                System.out.println("Match singleplayer interrotto");
            }
        } else if (match.getObserverSocket() != null) {
            try {
                match.getObserverSocket().writeObject(new ProposeWindowResponse(windows));
                match.getObserverSocket().reset();
            } catch (IOException e) {
                match.terminateMatch();
                System.out.println("Match singleplayer interrotto");
            }
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

    /**
     * initialization of flags to control the turn's flow
     */
    private void initializeTurn() {
        match.setDiceAction(false);
        match.setToolAction(false);
        match.setEndsTurn(false);
        setTimer();
    }

    /**
     * sets the timer to force the passing of the turn for the player if timer expires
     */
    private void setTimer() {
        TurnTimerSingle task;

        turnTimer = new Timer();
        task = new TurnTimerSingle(match);
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

    private void nextRound() throws InterruptedException {
        match.pushLeftDicesToRoundTrack();
        match.incrementRoundCounter();

        if (match.getObserverRmi() != null) {
            try {
                match.getObserverRmi().onRoundTrack(match.getBoard().getRoundTrack().toString());
            } catch (RemoteException e) {
                match.terminateMatch();
                System.out.println("Match singleplayer interrotto");
            }
        } else if (match.getObserverSocket() != null) {
            try {
                match.getObserverSocket().writeObject(new RoundTrackResponse(match.getBoard().getRoundTrack().toString()));
                match.getObserverSocket().reset();
            } catch (IOException e) {
                match.terminateMatch();
                System.out.println("Match singleplayer interrotto");
            }
        }

        if (match.getObserverRmi() != null) {
            try {
                match.getObserverRmi().onReserve(match.getBoard().getReserve().toString());
            } catch (RemoteException e) {
                match.terminateMatch();
                System.out.println("Match singleplayer interrotto");
            }
        } else if (match.getObserverSocket() != null) {
            try {
                match.getObserverSocket().writeObject(new ReserveResponse(match.getBoard().getReserve().toString()));
                match.getObserverSocket().reset();
            } catch (IOException e) {
                match.terminateMatch();
                System.out.println("Match singleplayer interrotto");
            }
        }

        if (match.getCurrentRound() >= NUM_ROUNDS) {
            match.calculateFinalScore();
        } else {
            turnManager();
        }
    }
}
