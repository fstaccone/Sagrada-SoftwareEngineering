package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.gameobjects.Bag;
import it.polimi.ingsw.model.gameobjects.Board;
import it.polimi.ingsw.model.gameobjects.DecksContainer;

import java.rmi.RemoteException;

import static org.mockito.Mockito.mock;


public abstract class Match {

    protected DecksContainer decksContainer;
    private Bag bag;
    protected Board board;
    private static final int numberOfRounds = 10;
    protected int roundCounter;

    private boolean diceAction;
    private boolean toolAction;
    private boolean endsTurn;
    private boolean windowChosen;

    private final Object lock;


    public Match() {
        lock = new Object();
        bag = new Bag(18);
    }

    // getters
    public Object getLock() { return lock; }
    public DecksContainer getDecksContainer() {
        return decksContainer;
    }
    public Bag getBag() {
        return bag;
    }
    public Board getBoard() {
        return board;
    }
    public static int getNumberOfRounds() { return numberOfRounds; }
    public int getCurrentRound() { return roundCounter; }
    public boolean isDiceAction() { return diceAction; }
    public boolean isToolAction() { return toolAction; }
    public boolean isEndsTurn() { return endsTurn; }
    public boolean isWindowChosen() { return windowChosen; }
    // end of getters

    // to limit player action
    public void setDiceAction(boolean diceAction) { this.diceAction = diceAction; }
    public void setToolAction(boolean toolAction) { this.toolAction = toolAction; }
    public void setEndsTurn(boolean endsTurn) { this.endsTurn = endsTurn; }
    public void setWindowChosen(boolean windowChosen) { this.windowChosen = windowChosen; }

    public void incrementRoundCounter() { this.roundCounter++; }

    public void pushLeftDicesToRoundTrack(){
        this.getBoard().getRoundTrack().putDices(this.getBoard().getReserve().removeAllDices(), this.roundCounter);
    }

    // Game's initialisation
    public abstract void gameInit() throws InterruptedException, RemoteException;

    // Assegna le carte obiettivo privato
    public abstract void drawPrivateObjectiveCards();

    public abstract void calculateFinalScore();

    public abstract void showTrack(String name);

    public abstract void setWindowPatternCard(String name, int index) throws RemoteException;

    public abstract boolean placeDice(String name, int index, int x, int y) throws RemoteException;

    public void goThrough(){
        setEndsTurn(true);
        synchronized (lock) {
            lock.notify();
        }
    }
}

