package it.polimi.ingsw.model.gameobjects;

import java.util.*;

public class MatchMultiplayer extends Match implements Runnable{

    private int matchCounter;
    private List<PlayerMultiplayer> players;
    private int positionOfFirstPlayerInRound;

    public MatchMultiplayer(int matchCounter, List<String> clients) {
        super();
        this.matchCounter=matchCounter;
        System.out.println("New multiplayer matchId: "+ matchCounter);
        // trovare un modo per fare il cast da Player a PlayerMultiplayer
        this.decksContainer = new DecksContainer(clients.size());
        this.board = new Board(this, decksContainer.getToolCardDeck().getPickedCards(), decksContainer.getPublicObjectiveCardDeck().getPickedCards());
        this.players = new ArrayList<>();
        clients.forEach(p -> this.players.add(new PlayerMultiplayer(p)));
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

    private int positionOfNextFirstPlayer(){
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
        for (PlayerMultiplayer p: players) {
           p.getPrivateObjectiveCard().useCard(p); // useCard può (dovrebbe) essere un metodo del player
        }
    }

    @Override
    public void drawPrivateObjectiveCards() {
        for (PlayerMultiplayer p: players) {
            p.setPrivateObjectiveCard(this.decksContainer.getPrivateObjectiveCardDeck().pickedCards.get(0));
            this.decksContainer.getPrivateObjectiveCardDeck().pickedCards.remove(this.decksContainer.getPrivateObjectiveCardDeck().pickedCards.get(0));
        }
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

    @Override
    public void run() {

    }
}
