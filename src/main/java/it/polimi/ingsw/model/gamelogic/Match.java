package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.gameobjects.Bag;
import it.polimi.ingsw.model.gameobjects.Board;
import it.polimi.ingsw.model.gameobjects.DecksContainer;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.rmi.RemoteException;
import java.util.List;


public abstract class Match {

    public static final int PING_TIME = 3000; // max lasting of the waiting for the ping response
    public static final int NUMBER_OF_ROUNDS = 10;

    DecksContainer decksContainer;
    private Bag bag;
    protected Board board;
    int roundCounter;
    Thread localThread;
    private boolean stillPlaying;

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
        stillPlaying = true;
    }

    // getters
    Object getLock() {
        return lock;
    }

    public Lobby getLobby() {
        return lobby;
    }

    DecksContainer getDecksContainer() {
        return decksContainer;
    }

    public Bag getBag() {
        return bag;
    }

    public Board getBoard() {
        return board;
    }

    boolean isStillPlaying() {
        return stillPlaying;
    }

    List<WindowPatternCard> getWindowsProposed() {
        return windowsProposed;
    }

    int getCurrentRound() {
        return roundCounter;
    }

    public boolean isDiceAction() {
        return diceAction;
    }

    boolean isToolAction() {
        return toolAction;
    }

    public boolean isEndsTurn() {
        return endsTurn;
    }

    boolean isWindowChosen() {
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

    void setEndsTurn(boolean endsTurn) {
        this.endsTurn = endsTurn;
    }

    void setWindowChosen(boolean windowChosen) {
        this.windowChosen = windowChosen;
    }

    void incrementRoundCounter() {
        this.roundCounter++;
    }

    void setStillPlayingToFalse() {
        stillPlaying = false;
    }

    /**
     * At the end of each round, all the dices left on the reserve are taken and placed on the round track
     */
    void pushLeftDicesToRoundTrack() {
        this.getBoard().getRoundTrack().putDices(this.getBoard().getReserve().removeAllDices(), this.roundCounter);
    }

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
    boolean checkCondition() {
        return !((toolAction && diceAction) || endsTurn);
    }


    public abstract void terminateMatch();

    public abstract boolean placeDiceTool11(String name, int x, int y);

    public abstract boolean useToolCard1(int diceToBeSacrificed, int diceChosen, String incrOrDecr, String name);

    public abstract boolean useToolCard2or3(int diceToBeSacrificed, int n, int startX, int startY, int finalX, int finalY, String name);

    public abstract boolean useToolCard4(int diceToBeSacrificed, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String name);

    public abstract boolean useToolCard5(int diceToBeSacrificed, int diceChosen, int roundChosen, int diceChosenFromRound, String name);

    public abstract boolean useToolCard6(int diceToBeSacrificed, int diceChosen, String name);

    public abstract boolean useToolCard7(int diceToBeSacrificed, String name);

    public abstract boolean useToolCard8(int diceToBeSacrificed, String name);

    public abstract boolean useToolCard9(int diceToBeSacrificed, int diceChosen, int finalX1, int finalY1, String name);

    public abstract boolean useToolCard10(int diceToBeSacrificed, int diceChosen, String name);

    public abstract boolean useToolCard11(int diceToBeSacrificed, int diceChosen, String name);

    public abstract boolean useToolCard12(int diceToBeSacrificed, int roundFromTrack, int diceInRound, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String name);

    public abstract void ping(String username);
}

