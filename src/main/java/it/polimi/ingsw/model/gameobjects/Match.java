package it.polimi.ingsw.model.gameobjects;

import java.util.*;

// commentato perchè non ho il jar
//import static org.mockito.Mockito.mock;


public abstract class Match {

    protected DecksContainer decksContainer;
    private Bag bag;
    protected Board board;
    private static final int numberOfRounds = 10;
    protected int roundCounter;

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
    // end of getters

    public void incrementRoundCounter() {
        this.roundCounter++;
    }

    public void pushLeftDicesToRoundTrack(){
        this.getBoard().getRoundTrack().putDices(this.getBoard().getReserve().endRound(), this.roundCounter);
    }

    // Game's initialisation
    public abstract void gameInit();

    // Assegna le carte obiettivo privato
    public abstract void drawPrivateObjectiveCards();

    // Propone le carte schema
    public abstract void proposeWindowPatternCards();

    // Distribuisce le carte obiettivo pubblico
    public abstract void drawPublicObjectiveCards();

    // Distribuisce le tool cards
    public abstract void drawToolCards();


    public abstract void nextRound();

    public abstract void calculateFinalScore();

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

