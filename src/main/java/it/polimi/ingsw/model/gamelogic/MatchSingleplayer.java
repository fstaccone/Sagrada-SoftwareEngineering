package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.MatchObserver;
import it.polimi.ingsw.model.gameobjects.Board;
import it.polimi.ingsw.model.gameobjects.DecksContainer;
import it.polimi.ingsw.model.gameobjects.PlayerSingleplayer;

import java.io.ObjectOutputStream;

public class MatchSingleplayer extends Match implements Runnable {

    private int matchId;
    private int difficulty;
    String clientIdentifier;
    PlayerSingleplayer player;
    TurnManagerSingleplayer turnManager;
    MatchObserver observerRmi;
    ObjectOutputStream observerSocket;

    public MatchSingleplayer(int matchId, String name, int turnTime, Lobby lobby) {
        super(lobby);
        this.matchId = matchId;
        this.decksContainer = new DecksContainer(1);
        this.clientIdentifier = name;
        this.player = new PlayerSingleplayer(name);
        turnManager = new TurnManagerSingleplayer(this, turnTime);
        // todo: occorre creare un mumero di toolcard compreso tra 1 e 5
        decksContainer = new DecksContainer(1);
        board = new Board(this, decksContainer.getToolCardDeck().getPickedCards(), decksContainer.getPublicObjectiveCardDeck().getPickedCards()); // todo: controllare, servono le private


        System.out.println("New singleplayer matchId: " + this.matchId);

    }

    public MatchObserver getObserverRmi() {
        return observerRmi;
    }


    public ObjectOutputStream getObserverSocket() {
        return observerSocket;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public TurnManagerSingleplayer getTurnManager() {
        return turnManager;
    }

    public PlayerSingleplayer getPlayer() {
        return player;
    }

    public void setObserverSocket(ObjectOutputStream observerSocket) {
        this.observerSocket = observerSocket;
    }


    public void setObserverRmi(MatchObserver observerRmi) {
        this.observerRmi = observerRmi;
    }

    @Override
    public void gameInit() {

    }

    @Override
    public void drawPrivateObjectiveCards() {

    }

    @Override
    public void calculateFinalScore() {

    }

    @Override
    public void run() {

    }

    @Override
    public void setWindowPatternCard(String name, int index) {

    }

    @Override
    public boolean placeDice(String name, int index, int x, int y) {
        return false;
    }

    @Override
    public void terminateMatch() {
        // todo: complatare
        lobby.removeUsername(player.getName());
        lobby.removeMatchSingleplayer(player.getName());
    }
}
