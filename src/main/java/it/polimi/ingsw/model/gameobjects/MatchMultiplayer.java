package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gamelogic.TurnTimer;

import java.net.PortUnreachableException;
import java.util.*;

public class MatchMultiplayer extends Match implements Runnable{

    private int matchCounter;
    private List<PlayerMultiplayer> players;
    private int positionOfFirstPlayerInRound;
    private Timer timer;
    private TurnTimer task;

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
        //this.proposeWindowPatternCards();

        // Viene lanciata la fase a turni
        this.turnManager(positionOfFirstPlayerInRound);
    }

    // Assegna il colore ai giocatori in modo casuale
    private void assignColors() {

        // Creation of a list of colors (without the special value NONE) to be assigned randomly to players
        Random rand = new Random();
        int val;
        List colors = new ArrayList();

        // This block creates an ArrayList of colors. A color, once assigned, must be removed from the ArrayList
        for (Colors c : Colors.values()) {
            if (!c.equals(Colors.NONE)) {
                colors.add(c);
            }
        }

        for (Player p : players) {
            val = rand.nextInt(colors.size());
            p.setColor((Colors) colors.get(val));  // Da testare, non ne sono convinto
            colors.remove(val);
        }
    }

    // Gestore del singolo turno
    private void turnManager (int positionOfFirstPlayerInRound){


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
        // points assigned by the private objective card
        for (PlayerMultiplayer p: players) {
           p.getPrivateObjectiveCard().useCard(p);
        }
        // points assigned by public objective cards
        for(int i = 0; i < board.getPickedPublicObjectiveCards().size(); i++){
            for (PlayerMultiplayer p: players) {
                board.getPickedPublicObjectiveCards().get(i).useCard(p, this);
            }
        }
        // points due to free cells
        for (PlayerMultiplayer p: players) {
            p.setPoints(p.getPoints() - p.getSchemeCard().countFreeSquares());
        }
        // points due to remaining favor tokens
        for (PlayerMultiplayer p: players) {
            p.setPoints(p.getPoints() + p.getNumFavorTokens());
        }

        // just for now todo: implements and test
        System.out.println("The winner is: " + players.stream().max(Comparator.comparing(p -> p.getPoints() > p.getPoints())));
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
    public void run() {
        System.out.println("Players before game init:");
        players.stream().map(Player::getName).forEach(System.out::println);
        gameInit();
        System.out.println("Players after game init:");
        players.stream().map(Player::getName).forEach(System.out::println);
    }
}
