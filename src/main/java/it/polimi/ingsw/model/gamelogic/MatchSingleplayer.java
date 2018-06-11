package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.MatchObserver;
import it.polimi.ingsw.model.gameobjects.Board;
import it.polimi.ingsw.model.gameobjects.DecksContainer;
import it.polimi.ingsw.model.gameobjects.PlayerSingleplayer;
import it.polimi.ingsw.socket.responses.GameEndSingleResponse;
import it.polimi.ingsw.socket.responses.GameStartedResponse;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;

public class MatchSingleplayer extends Match implements Runnable {

    private int matchId;
    private String clientIdentifier;
    private PlayerSingleplayer player;
    private TurnManagerSingleplayer turnManager;
    private MatchObserver observerRmi;
    private ObjectOutputStream observerSocket;
    private int selectedPrivateCard; // con questo attributo si seleziona quale carta utilizzare per il calcolo del punteggio
    private static final int MULTIPLIER_FOR_SINGLE = 3;

    public MatchSingleplayer(int matchId, String name, int difficulty, int turnTime, Lobby lobby) {
        super(lobby);
        this.matchId = matchId;
        this.decksContainer = new DecksContainer(1, difficulty);
        this.clientIdentifier = name;
        this.player = new PlayerSingleplayer(name);
        turnManager = new TurnManagerSingleplayer(this, turnTime);
        // todo: occorre creare un mumero di toolcard compreso tra 1 e 5
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

        if (observerRmi != null) {
            try {
                observerRmi.onGameStarted(player.isSchemeCardSet(), null);
            } catch (RemoteException e) {
                terminateMatch();
                System.out.println("Match singleplayer interrotto");
            }
        } else if (observerSocket != null) {
            try {
                observerSocket.writeObject(new GameStartedResponse(player.isSchemeCardSet(), null));
                observerSocket.reset();
            } catch (IOException e) {
                terminateMatch();
                System.out.println("Match singleplayer interrotto");
            }
        }

        // actions to be performed once only
        roundCounter = 0;
        drawPrivateObjectiveCards();

        turnManager.run();
    }

    @Override
    public void drawPrivateObjectiveCards() {
        player.setPrivateObjectiveCards(decksContainer.getPrivateObjectiveCardDeck().getPickedCards());
        decksContainer.getPrivateObjectiveCardDeck().getPickedCards().clear();
    }

    @Override
    public void calculateFinalScore() {
        int targetPoints;

        // points from roundtrack, score to beat
        targetPoints = board.getRoundTrack().sumForSinglePlayer();

        // points assigned by the private objective card
        player.getPrivateObjectiveCards().get(selectedPrivateCard).useCard(player);

        // points assigned by public objective cards
        for (int i = 0; i < board.getPickedPublicObjectiveCards().size(); i++) {
            board.getPickedPublicObjectiveCards().get(i).useCard(player, this);
        }

        // points due to free cells
        player.setPoints(player.getPoints() - MULTIPLIER_FOR_SINGLE * player.getSchemeCard().countFreeSquares());

        if (observerRmi != null) {
            try {
                observerRmi.onGameEndSingle(targetPoints, player.getPoints());
            } catch (RemoteException e) {
                terminateMatch();
                System.out.println("Match singleplayer interrotto");
            }
        } else if (observerSocket != null) {
            try {
                observerSocket.writeObject(new GameEndSingleResponse(targetPoints, player.getPoints()));
            } catch (IOException e) {
                terminateMatch();
                System.out.println("Match singleplayer interrotto");
            }
        }
    }

    @Override
    public void run() {
        gameInit();
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
        lobby.removeMatchSingleplayer(player.getName());
    }

    public void observeMatchRemote(MatchObserver observer) {
        observerRmi = observer;
        localThread = new Thread(this);
        localThread.start();
    }
}
