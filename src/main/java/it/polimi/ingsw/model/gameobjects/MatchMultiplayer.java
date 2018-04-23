package it.polimi.ingsw.model.gameobjects;

import java.util.*;

public class MatchMultiplayer extends Match {

    private List<Player> players;
    private int positionOfFirstPlayerInRound;

    public MatchMultiplayer(int matchId, List<Player> players) {
        super(matchId);
        this.decksContainer = new DecksContainer(players.size());
        this.board = new Board(this, players, decksContainer.getToolCardDeck().getPickedToolCards(), decksContainer.getPublicObjectiveCardDeck().getPickedPublicObjectiveCards());
    }

    // getters
    public int getPositionOfFirstPlayerInRound() { return positionOfFirstPlayerInRound; }
    // end of getters

    // setters
    public void setPositionOfFirstPlayerInRound(int positionOfFirstPlayerInRound) { this.positionOfFirstPlayerInRound = positionOfFirstPlayerInRound; }
    // end of setters

    // game's initialisation
    @Override
    public void gameInit(){

        // actions to be performed once only
        this.roundCounter = 0;
        this.assignColors();
        Collections.shuffle(this.players); // shuffles players to determine the sequence flow of rounds
        this.setPositionOfFirstPlayerInRound(0); // the first player is always in the first position due to the shuffle
        this.drawPrivateObjectiveCards();
        this.proposeWindowPatternCards();
        this.drawPublicObjectiveCards();
        this.drawToolCards();

        // Viene lanciata la fase a turni
        this.turnManager(positionOfFirstPlayerInRound);
    }

    // Assegna il colore ai giocatori in modo casuale
    private void assignColors() {

        // Creation of a list of colors (without the special value NONE) to be assigned randomly to players
        Random rand = new Random();
        int val;
        List colors = new ArrayList(Colors.values().length - 1);

        // This block creates an ArrayList of colors. A color, once assigned, must be removed from the ArrayList
        for (Colors c : Colors.values()) {
            if (!c.equals(Colors.NONE)) {
                colors.add(c);
            }
        }

        for (Player p : players) {
            val = rand.nextInt(colors.size()) + 1;
            p.setColor((Colors) colors.get(val));  // Da testare, non ne sono convinto
            colors.remove(val);
        }
    }

    // Gestore del singolo turno
    private void turnManager (int positionOfFirstPlayerInRound){

        // Esecuzione del turno

        this.nextRound();
    }

    public int positionOfNextFirstPlayer(){
        if(this.positionOfFirstPlayerInRound >= players.size()-1)
            return 0;
        return this.positionOfFirstPlayerInRound + 1;
    }

    public void nextRound () {
        this.pushLeftDicesToRoundTrack();
        this.incrementRoundCounter();
        this.setPositionOfFirstPlayerInRound(this.positionOfNextFirstPlayer());

        if (this.roundCounter > 10) {
            this.calculateFinalScore();
        } else {
            this.turnManager(positionOfFirstPlayerInRound);
        }
    }

    @Override
    public void calculateFinalScore() {
        for (Player p: players) {

        }
    }

    @Override
    public void drawPrivateObjectiveCards() {

    }

    @Override
    public void proposeWindowPatternCards() {

    }

    @Override
    public void drawPublicObjectiveCards() {

    }

    @Override
    public void drawToolCards() {

    }
}
