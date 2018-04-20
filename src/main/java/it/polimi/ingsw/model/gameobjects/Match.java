package it.polimi.ingsw.model.gameobjects;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;


public class Match {
    //added attributes to try connectivity from view to model(MVC) - Later on with room introduction have to be modified/deleted
    private static Match match;
    private Set<Player> loggedPlayers;

    //DA QUI INIZIA PRECEDENTE IMPLEMENTAZIONE A MENO DI COMMENTI SIMILI(MVC) A QUELLO POSTO ALL'INIZIO DELLA CLASSE
    // Match doesn't contain numPlayers because that information is in Room (that's the entity that manage connections to the match)
    private  int matchId;
    private Set<Player> players;
    private DecksContainer decksContainer;
    private Bag bag;
    private Board board;

    //useful for MVC - Later on with room introduction has to be modified/deleted
    private Match(){
        loggedPlayers= new HashSet<>();
    }

    //Singleton(useful for MVC) Later on with room introduction has to be modified/deleted
    public synchronized static Match get(){
        if (match==null){
            match=new Match();
        }
        return match;
    }

    //LA ROOM ISTANZIA IL MATCH PASSANDO I GIOCATORI? SE Sì ALLORA IL CONTROLLER DOVREBBE RIFARSI ALLA ROOM E NON AL MATCH(RIENTRA NELLE MODIFICHE DA FARE)
    public Match(Set<Player> players) {
        this.players = players;
        this.decksContainer = new DecksContainer();
        this.bag = new Bag(18);
        this.board = new Board(this, players, decksContainer.getPickedToolCards());
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
    // TODO: understand how to manage match's flow
    public void gameInit() { }

    public void proposeWindowPatternCards(){}

    public void assignPrivateObjectiveCardToPlayer(Player player){
    }

    //added to try connectivity from view to model(MVC) - Later on with room introduction has to be modified/deleted
    public synchronized Player login(String username) throws RemoteException {
        if (loggedPlayers.stream().map(Player::getName).anyMatch(u -> u.equals(username))) {
            throw new RemoteException("Another player is already using the username you choose: " + username);
        }
        Player player = new Player(username);
        loggedPlayers.add(player);
        return player; //ritorna player al controller
    }
}

