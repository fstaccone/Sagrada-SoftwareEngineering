package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.Client;

import java.util.List;

public class MatchSingleplayer extends Match implements Runnable{

    private int matchId;
    private int difficulty;
    String clientIdentifier;
    PlayerSingleplayer player;

    public MatchSingleplayer(int matchId, String name) {
        super();
        this.matchId = matchId;
        this.decksContainer = new DecksContainer(1);
        this.clientIdentifier = name;
        this.player = new PlayerSingleplayer(name);
        System.out.println("New singleplayer matchId: " + this.matchId);
    }

    public int getMatchId() { return matchId; }

    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

    @Override
    public void gameInit() {

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

    @Override
    public void nextRound() {

    }

    @Override
    public void calculateFinalScore() {

    }

    @Override
    public void run() {

    }
}
