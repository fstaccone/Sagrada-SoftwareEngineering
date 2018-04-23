package it.polimi.ingsw.model.gameobjects;

import java.util.*;

// commentato perchè non ho il jar
//import static org.mockito.Mockito.mock;


public class Match {

    private int matchId;
    private List<Player> players;
    private DecksContainer decksContainer;
    private Bag bag;
    private Board board;
    private static final int numberOfRounds = 10;
    private int roundCounter;
    private Player firstPlayerInRound;

    //LA ROOM ISTANZIA IL MATCH PASSANDO I GIOCATORI? SE Sì ALLORA IL CONTROLLER DOVREBBE RIFARSI ALLA ROOM E NON AL MATCH(RIENTRA NELLE MODIFICHE DA FARE)
    public Match(List<Player> players) {
        // this.players deve diventare una mappa con i player contenuti in players a cui viene associata una posizione in modo da tenerne traccia durante il gioco
        // la posizione potrebbe anche essere determinata dal colore, stabiliamo una gerarchia ed assegnamo il colore in modo casuale
        this.players = players;
        //this.players = players.toMap(); // con posizione associata

        this.decksContainer = new DecksContainer(players.size());
        this.bag = new Bag(18);
        this.board = new Board(this, players, decksContainer.getToolCardDeck().getPickedCards(), decksContainer.getPublicObjectiveCardDeck().getPickedCards());
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

    public static int getNumberOfRounds() { return numberOfRounds; }

    public int getCurrentRound() { return roundCounter; }

    public void incrementRoundCounter() {
        this.roundCounter++;
    }

    public Player getFirstPlayerInRound() { return firstPlayerInRound; }

    public void setFirstPlayerInRound(Player firstPlayerInRound) {
        this.firstPlayerInRound = firstPlayerInRound;
    }

    // Game's initialisation
    public void gameInit() {

        // Fase in cui vengono svolte le operazioni da fare una sola volta
        this.roundCounter = 0;
        this.assignColors();
        //this.setFirstPlayerInRound(this.chooseFirstPlayer());
        this.drawPrivateObjectiveCards();
        this.proposeWindowPatternCards();
        this.drawPublicObjectiveCards();
        this.drawToolCards();

        // Viene lanciata la fase a turni
        this.turnManager(firstPlayerInRound);
    }

    // Nuovi metodi utili allo svolgimento della partita

    // Assegna il colore ai giocatori in modo casuale
    private void assignColors(){

        // Creation of a list of colors (without the special value NONE) to be assigned randomly to players
        Random rand = new Random();
        int val;
        List colors = new ArrayList(Colors.values().length -1);

        for (Colors c : Colors.values()) {
            if(!c.equals(Colors.NONE)){
                colors.add(c);
            }
        }

        for (Player p : players){
            val = rand.nextInt(colors.size()) + 1;
           // p.
        }
/*
        for(Dice dice : init){
            Random rand = new Random();
            int val = rand.nextInt(6)+1;
            dice.setValue(val);
            dices.add(dice);
        }
*/    }

    // Sceglie il primo giocatore in modo casuale
    // Potrebbe essere scelto in base alla posizione o al colore
    /*private Player chooseFirstPlayer() {
        Player p;
        return p;
    }*/

    // Assegna le carte obiettivo privato
    private void drawPrivateObjectiveCards() {

    }

    // Propone le carte schema
    private void proposeWindowPatternCards() {

    }

    // Distribuisce le carte obiettivo pubblico
    private void drawPublicObjectiveCards(){

    }

    // Distribuisce le tool cards
    private void drawToolCards(){

    }

    // Gestore del singolo turno
    private void turnManager(Player firstPlayer){
        // Esecuzione del turno
        this.nextRound();
    }

    private void nextRound(){

        // mette i dadi rimanenti nella riserva in roundtrack
        //...
        this.incrementRoundCounter();

        // Determina il prossimo giocatore primo a giocare
        //this.setFirstPlayerInRound(this.nextFirtsPlayer());

        if(this.roundCounter > 10){
            // calcola punteggio
        }
        else {
            this.turnManager(firstPlayerInRound);
        }

    }

   /* private Player nextFirstPlayer(){
        Player p;
        // ...
        return p;
    }*/
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

