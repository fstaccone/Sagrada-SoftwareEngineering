package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.MatchObserver;
import it.polimi.ingsw.model.gameobjects.Board;
import it.polimi.ingsw.model.gameobjects.DecksContainer;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.PlayerSingleplayer;
import it.polimi.ingsw.socket.responses.GameEndSingleResponse;
import it.polimi.ingsw.socket.responses.*;

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

    public MatchSingleplayer(int matchId, String name, int difficulty, int turnTime, Lobby lobby, ObjectOutputStream socketOut) {
        super(lobby);
        this.matchId = matchId;
        this.decksContainer = new DecksContainer(1, difficulty);
        this.clientIdentifier = name;
        this.player = new PlayerSingleplayer(name);
        turnManager = new TurnManagerSingleplayer(this, turnTime);
        board = new Board(this, decksContainer.getToolCardDeck().getPickedCards(), decksContainer.getPublicObjectiveCardDeck().getPickedCards());
        System.out.println("New singleplayer matchId: " + this.matchId);
        observerSocket = socketOut;
        if (observerSocket != null) {
            startMatch();
        }
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
                System.out.println("Match singleplayer interrupted");
            }
        } else if (observerSocket != null) {
            try {
                observerSocket.writeObject(new GameEndSingleResponse(targetPoints, player.getPoints()));
            } catch (IOException e) {
                terminateMatch();
                System.out.println("Match singleplayer interrupted");
            }
        }
    }

    @Override
    public void run() {
        roundCounter = 0;
        drawPrivateObjectiveCards();

        turnManager.run();
    }

    @Override
    public void setWindowPatternCard(String name, int index) {

        player.setSchemeCard(windowsProposed.get(index));
        decksContainer.getWindowPatternCardDeck().getPickedCards().removeAll(windowsProposed);
        player.setSchemeCardSet(true);
        setWindowChosen(true);

        schemeCardToBeUpdated(true);

        if (observerRmi != null) {
            try {
                observerRmi.onAfterWindowChoice();
            } catch (RemoteException e) {
                terminateMatch();
                System.out.println("Match terminato per disconnessione!");
            }
        } else if (observerSocket != null) {
            try {
                observerSocket.writeObject(new AfterWindowChoiseResponse());
                observerSocket.reset();
            } catch (IOException e) {
                terminateMatch();
                System.out.println("Match ended due to disconnection!");
            }
        }

        synchronized (getLock()) {
            getLock().notifyAll();
        }
    }

    private void schemeCardToBeUpdated(boolean result) {
        if (result) {
            if (observerRmi != null) {
                try {
                    observerRmi.onMyWindow(player.getSchemeCard());
                } catch (RemoteException e) {
                    terminateMatch();
                    System.out.println("Match ended due to disconnection!");
                }
            } else if (observerSocket != null) {
                try {
                    observerSocket.writeObject(new MyWindowResponse(player.getSchemeCard()));
                    observerSocket.reset();
                } catch (IOException e) {
                    terminateMatch();
                    System.out.println("Match ended due to disconnection!");
                }
            }
        }
    }

    private void reserveToBeUpdated(boolean reserveToBeUpdated) {
        if (reserveToBeUpdated) {
            if (observerRmi != null) {
                try {
                    observerRmi.onReserve(board.getReserve().getDices().toString());
                } catch (RemoteException e) {
                    terminateMatch();
                    System.out.println("Match ended due to disconnection!");
                }
            } else if (observerSocket != null) {
                try {
                    observerSocket.writeObject(new UpdateReserveResponse(board.getReserve().getDices().toString()));
                    observerSocket.reset();
                } catch (IOException e) {
                    terminateMatch();
                    System.out.println("Match ended due to disconnection!");
                }
            }
        }
    }

    @Override
    public boolean placeDice(String name, int index, int x, int y) {
        if (!isDiceAction()) {
            boolean result;
            result = player.getSchemeCard().putDice(board.getReserve().getDices().get(index), x, y);
            setDiceAction(result);

            // special notification for socket connection
            if (observerSocket != null) {
                try {
                    observerSocket.writeObject(new DicePlacedResponse(result));
                    observerSocket.reset();
                } catch (IOException e) {
                    terminateMatch();
                    System.out.println("Match ended due to disconnection!");
                }
            }

            if (result) {
                board.getReserve().getDices().remove(index);
            }
            reserveToBeUpdated(result);
            schemeCardToBeUpdated(result);

            synchronized (getLock()) {
                getLock().notifyAll();
            }
            return result;
        }
        return false;
    }

    @Override
    public void terminateMatch() {
        lobby.removeMatchSingleplayer(player.getName());
    }

    @Override
    public boolean useToolCard1(int diceToBeSacrificed, int diceChosen, String incrOrDecr, String name) {
        if (!isToolAction()) {
            getPlayer().setDice(diceChosen);
            getPlayer().setChoice(incrOrDecr);
            getPlayer().setDiceToBeSacrificed(diceToBeSacrificed);
            boolean result = getBoard().findAndUseToolCard(1, getPlayer(), this);
            if(result) getPlayer().setDiceToBeSacrificed(9);
            reserveToBeUpdated(result);
            setToolAction(result);
            synchronized (getLock()) {
                getLock().notifyAll();
            }

            return result;
        } else {
            return false;
        }
    }

    @Override
    public boolean useToolCard2or3(int diceToBeSacrificed, int n, int startX, int startY, int finalX, int finalY, String name) {
        if (!isToolAction()) {
            getPlayer().setStartX1(startX);
            getPlayer().setStartY1(startY);
            getPlayer().setFinalX1(finalX);
            getPlayer().setFinalY1(finalY);
            getPlayer().setDiceToBeSacrificed(diceToBeSacrificed);
            boolean result = getBoard().findAndUseToolCard(n, getPlayer(), this);
            if(result) getPlayer().setDiceToBeSacrificed(9);
            setToolAction(result);
            synchronized (getLock()) {
                getLock().notifyAll();
            }
            return result;
        } else {
            return false;
        }    }

    @Override
    public boolean useToolCard4(int diceToBeSacrificed, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String name) {
        if (!isToolAction()) {
            getPlayer().setStartX1(startX1);
            getPlayer().setStartY1(startY1);
            getPlayer().setFinalX1(finalX1);
            getPlayer().setFinalY1(finalY1);
            getPlayer().setStartX2(startX2);
            getPlayer().setStartY2(startY2);
            getPlayer().setFinalX2(finalX2);
            getPlayer().setFinalY2(finalY2);
            getPlayer().setDiceToBeSacrificed(diceToBeSacrificed);
            boolean result = getBoard().findAndUseToolCard(4, getPlayer(), this);
            setToolAction(result);
            if(result) getPlayer().setDiceToBeSacrificed(9);
            synchronized (getLock()) {
                getLock().notifyAll();
            }
            return result;
        } else {
            return false;
        }
    }

    @Override
    public boolean useToolCard5(int diceToBeSacrificed, int diceChosen, int roundChosen, int diceChosenFromRound, String name) {
        if (!isToolAction()) {
            getPlayer().setDice(diceChosen);
            getPlayer().setRound(roundChosen);
            getPlayer().setDiceChosenFromRound(diceChosenFromRound);
            getPlayer().setDiceToBeSacrificed(diceToBeSacrificed);
            boolean result = getBoard().findAndUseToolCard(5, getPlayer(), this);
            if(result) getPlayer().setDiceToBeSacrificed(9);
            reserveToBeUpdated(result);
            setToolAction(result);
            if(observerRmi!=null){
                try {
                    observerRmi.onRoundTrack(board.getRoundTrack().toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }else{
                Response response = new RoundTrackResponse(board.getRoundTrack().toString());
                try {
                    observerSocket.writeObject(response);
                    observerSocket.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            synchronized (getLock()) {
                getLock().notifyAll();
            }
            return result;
        } else {
            return false;
        }
    }

    @Override
    public boolean useToolCard6(int diceToBeSacrificed, int diceChosen, String name) {
        if (!isToolAction()) {
            getPlayer().setDice(diceChosen);
            getPlayer().setDiceToBeSacrificed(diceToBeSacrificed);
            boolean result = getBoard().findAndUseToolCard(6, getPlayer(), this);
            if(result) getPlayer().setDiceToBeSacrificed(9);
            reserveToBeUpdated(result);
            setToolAction(result);
            synchronized (getLock()) {
                getLock().notifyAll();
            }

            return result;
        } else {
            return false;
        }    }

    public void observeMatchRemote(MatchObserver observer) {
        observerRmi = observer;
        startMatch();
    }

    private void startMatch() {
        localThread = new Thread(this);
        localThread.start();
    }
}
