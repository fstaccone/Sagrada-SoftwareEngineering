package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.model.gameobjects.Bag;
import it.polimi.ingsw.model.gameobjects.Board;
import it.polimi.ingsw.model.gameobjects.DecksContainer;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.rmi.RemoteException;
import java.util.List;


public abstract class Match {


    protected DecksContainer decksContainer;
    private Bag bag;
    protected Board board;
    private static final int numberOfRounds = 10;
    protected int roundCounter;
    protected Thread localThread;

    private boolean diceAction;
    private boolean toolAction;
    private boolean endsTurn;
    private boolean windowChosen;
    List<WindowPatternCard> windowsProposed;
    Lobby lobby;

    private final Object lock;

    /**
     * When the match is initialized the bag is created (with 18 dices for each of the 5 available colors)
     */
    public Match(Lobby lobby) {
        this.lobby = lobby;
        lock = new Object();
        bag = new Bag(18);
    }

    // getters
    public Object getLock() {
        return lock;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public DecksContainer getDecksContainer() {
        return decksContainer;
    }

    public Bag getBag() {
        return bag;
    }

    public Board getBoard() {
        return board;
    }

    public static int getNumberOfRounds() {
        return numberOfRounds;
    }

    public List<WindowPatternCard> getWindowsProposed() {
        return windowsProposed;
    }


    public int getCurrentRound() {
        return roundCounter;
    }

    public boolean isDiceAction() {
        return diceAction;
    }

    public boolean isToolAction() {
        return toolAction;
    }

    public boolean isEndsTurn() {
        return endsTurn;
    }

    public boolean isWindowChosen() {
        return windowChosen;
    }
    // end of getters

    // to limit player action
    public void setDiceAction(boolean diceAction) {
        this.diceAction = diceAction;
    }

    public void setToolAction(boolean toolAction) {
        this.toolAction = toolAction;
    }

    public void setEndsTurn(boolean endsTurn) {
        this.endsTurn = endsTurn;
    }

    public void setWindowChosen(boolean windowChosen) {
        this.windowChosen = windowChosen;
    }

    public void incrementRoundCounter() {
        this.roundCounter++;
    }

    /**
     * At the end of each round, all the dices left on the reserve are taken and placed on the round track
     */
    public void pushLeftDicesToRoundTrack() {
        this.getBoard().getRoundTrack().putDices(this.getBoard().getReserve().removeAllDices(), this.roundCounter);
    }

    /**
     * game's initialization
     *
     * @throws InterruptedException
     * @throws RemoteException
     */
    public abstract void gameInit() throws InterruptedException, RemoteException;

    /**
     * to assign the private objective cards
     */
    public abstract void drawPrivateObjectiveCards();

    public abstract void calculateFinalScore();

    public abstract void setWindowPatternCard(String name, int index) throws RemoteException;

    public abstract boolean placeDice(String name, int index, int x, int y) throws RemoteException;

    /**
     * Sets the end of the turn
     */
    public void goThrough() {
        setEndsTurn(true);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void windowsToBeProposed() {
        windowsProposed = decksContainer.getWindowPatternCardDeck().getPickedCards().subList(0, 4);
    }

    /**
     * checks if the player has performed all actions during his turn or has decided to pass.
     *
     * @return true if the player can perform other actions
     */
    public boolean checkCondition() {
        return !((toolAction && diceAction) || endsTurn);
    }


    public abstract void terminateMatch();

    public abstract boolean useToolCard1(int diceToBeSacrificed, int diceChosen, String incrOrDecr, String name);
}

