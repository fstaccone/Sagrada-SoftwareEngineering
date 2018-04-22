package it.polimi.ingsw.model.gameobjects;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

// commentato perchè non ho il jar
//import static org.mockito.Mockito.mock;


public class Match {

    // Match doesn't contain numPlayers because that information is in Room (that's the entity that manage connections to the match)
    private int matchId;
    private Set<Player> players;
    private DecksContainer decksContainer;
    private Bag bag;
    private Board board;
    private RoundTrack roundTrack;
    private final static int numberOfRounds = 10;
    private int currentRound;

    public static int getNumberOfRounds() { return numberOfRounds; }

    public int getCurrentRound() { return currentRound; }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    //LA ROOM ISTANZIA IL MATCH PASSANDO I GIOCATORI? SE Sì ALLORA IL CONTROLLER DOVREBBE RIFARSI ALLA ROOM E NON AL MATCH(RIENTRA NELLE MODIFICHE DA FARE)
    public Match(Set<Player> players) {
        this.players = players;
        this.decksContainer = new DecksContainer();
        this.bag = new Bag(18);
        this.board = new Board(this, players, decksContainer.getPickedToolCards(), decksContainer.getPickedPublicObjectiveCards());
    }

    public int getMatchId() {
        return matchId;
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

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    public void setRoundTrack(RoundTrack roundTrack) {
        this.roundTrack = roundTrack;
    }

    // TODO: understand how to manage match's flow
    public void gameInit() {
    }

    public void proposeWindowPatternCards() {
    }

    public void assignPrivateObjectiveCardToPlayer(Player player) {
    }

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

