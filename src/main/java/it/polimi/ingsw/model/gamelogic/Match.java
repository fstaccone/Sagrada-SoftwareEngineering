package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.gameobjects.Bag;
import it.polimi.ingsw.model.gameobjects.Board;
import it.polimi.ingsw.model.gameobjects.DecksContainer;

import java.rmi.RemoteException;
import java.util.*;

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
    private boolean secondDiceAction;

    public Match() {
        this.bag = new Bag(18);
    }

    // getters
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
    public boolean isSecondDiceAction() { return secondDiceAction; }

    // end of getters

    // to limit player action
    public void setDiceAction(boolean diceAction) { this.diceAction = diceAction; }
    public void setToolAction(boolean toolAction) { this.toolAction = toolAction; }
    public void setEndsTurn(boolean endsTurn) { this.endsTurn = endsTurn; }
    public void setSecondDiceAction(boolean secondDiceAction) { this.secondDiceAction = secondDiceAction; }


    public void incrementRoundCounter() { this.roundCounter++; }

    public void pushLeftDicesToRoundTrack(){
        this.getBoard().getRoundTrack().putDices(this.getBoard().getReserve().endRound(), this.roundCounter);
    }

    // Game's initialisation
    public abstract void gameInit() throws InterruptedException, RemoteException;

    // Assegna le carte obiettivo privato
    public abstract void drawPrivateObjectiveCards();

    // Propone le carte schema
    public abstract void proposeWindowPatternCards();

    public abstract void calculateFinalScore();

    public abstract void terminateMatch();

/* //to check if board attributes get the right values
    public static void main (String[] args) {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Set players = new HashSet();
        players.add(player1);
        players.add(player2);
        Match match = new Match(players);
        System.out.println(match.board.getPickedToolCards().toString());
        System.out.println(match.board.getPickedPublicObjectiveCards().toString());

    }*/
}

