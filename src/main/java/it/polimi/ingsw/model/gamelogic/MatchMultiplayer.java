package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.LobbyObserver;
import it.polimi.ingsw.MatchObserver;
import it.polimi.ingsw.model.gameobjects.*;

import java.rmi.RemoteException;
import java.util.*;

public class MatchMultiplayer extends Match implements Runnable{

    private List<MatchObserver> observers;
    private int matchCounter;
    private List<PlayerMultiplayer> players;
    private int positionOfFirstPlayerInRound;
    private int turnTime;
    private Timer timer;
    private TurnTimer task;

    public MatchMultiplayer(int matchCounter, List<String> clients, int turnTime) {
        super();
        this.observers=new LinkedList<>();
        this.matchCounter=matchCounter;
        System.out.println("New multiplayer matchId: " + matchCounter);
        // trovare un modo per fare il cast da Player a PlayerMultiplayer
        this.turnTime = turnTime;
        this.decksContainer = new DecksContainer(clients.size());
        this.board = new Board(this, decksContainer.getToolCardDeck().getPickedCards(), decksContainer.getPublicObjectiveCardDeck().getPickedCards());
        this.players = new ArrayList<>();
        clients.forEach(p -> this.players.add(new PlayerMultiplayer(p, this)));
    }

    // getters
    public int getPositionOfFirstPlayerInRound() { return positionOfFirstPlayerInRound; }

    public Timer getTimer() { return timer; }
    // end of getters

    // setters
    public void setPositionOfFirstPlayerInRound(int positionOfFirstPlayerInRound) { this.positionOfFirstPlayerInRound = positionOfFirstPlayerInRound; }
    // end of setters

    // game's initialisation
    @Override
    public void gameInit(){
        List<String> playersNames= new ArrayList<>();
        players.forEach(p->playersNames.add(p.getName()));

        for (MatchObserver observer : observers) {
            try {
                observer.onPlayers(playersNames);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        // actions to be performed once only
        this.roundCounter = 0;
        this.assignColors();
        Collections.shuffle(this.players); // shuffles players to determine the sequence flow of rounds
        this.setPositionOfFirstPlayerInRound(0); // the first player is always in the first position due to the shuffle
        this.drawPrivateObjectiveCards();
        //this.proposeWindowPatternCards();
        timer = new Timer();
        // Viene lanciata la fase a turni
        this.turnManager(positionOfFirstPlayerInRound);
    }

    // Assegna il colore ai giocatori in modo casuale
    private void assignColors() {

        // Creation of a list of colors (without the special value NONE) to be assigned randomly to players
        Random rand = new Random();
        int index;
        List colors = new ArrayList();

        // This block creates an ArrayList of colors. A color, once assigned, must be removed from the ArrayList
        for (Colors c : Colors.values()) {
            if (!c.equals(Colors.NONE)) {
                colors.add(c);
            }
        }

        for (Player p : players) {
            index = rand.nextInt(colors.size());
            p.setColor((Colors) colors.get(index));  // Da testare, non ne sono convinto
            colors.remove(index);
        }
    }

    // Gestore del singolo round
    private void turnManager (int positionOfFirstPlayerInRound){
        // capire come gestire il primo giocatore
        List<PlayerMultiplayer> rightOrder = new ArrayList<>();

        // da controllare, troppo contorto

        // prepara ordine diretto
        for(int i = positionOfFirstPlayerInRound; i < players.size(); i++){
            rightOrder.add(players.get(i));
        }
        for(int i = 0; i < positionOfFirstPlayerInRound; i++){
            rightOrder.add(players.get(i));
        }

        // prepara ordine inverso
        List<PlayerMultiplayer> reverseOrder = new ArrayList<>(rightOrder);
        Collections.reverse(reverseOrder);

        for(PlayerMultiplayer p : rightOrder){
            p.playTurn(this);
            task = new TurnTimer(p);
            timer.schedule(task, turnTime);
        }

        for(PlayerMultiplayer p : reverseOrder){
            p.playTurn(this);
            task = new TurnTimer(p);
            timer.schedule(task, turnTime);
        }

        this.nextRound();
    }

    private void nextTurn(){

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
            p.setPrivateObjectiveCard(this.decksContainer.getPrivateObjectiveCardDeck().getPickedCards().get(0));
            this.decksContainer.getPrivateObjectiveCardDeck().getPickedCards().remove(this.decksContainer.getPrivateObjectiveCardDeck().getPickedCards().get(0));
        }
    }

    @Override
    public void proposeWindowPatternCards() {

    }

    @Override
    public void run() {
        gameInit();
    }


    public int getMatchCounter(){
        return matchCounter;
    }

    public void observeMatch(MatchObserver observer){

        this.observers.add(observer);
        System.out.println("Gli observers del match"+this.matchCounter +" al momento sono: "+observers.size());
        System.out.println("Il numero dei players nel match"+this.matchCounter + " è: "+ players.size());
        if (this.players.size()==this.observers.size()){
            run();}
    }
}
