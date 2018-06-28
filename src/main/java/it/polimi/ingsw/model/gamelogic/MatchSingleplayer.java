package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.view.MatchObserver;
import it.polimi.ingsw.model.gameobjects.Board;
import it.polimi.ingsw.model.gameobjects.DecksContainer;
import it.polimi.ingsw.socket.responses.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;

public class MatchSingleplayer extends Match implements Runnable {

    private int matchId;
    private PlayerSingleplayer player;
    private TurnManagerSingleplayer turnManager;
    private MatchObserver observerRmi;
    private ObjectOutputStream observerSocket;
    private int selectedPrivateCard;
    private static final int MULTIPLIER_FOR_SINGLE = 3;
    private boolean privateCardChosen;

    /**
     *
     * @param matchId id of the current match
     * @param name name of the player in the match
     * @param difficulty difficulty chosen by the player
     * @param turnTime duration of every turn in milliseconds
     * @param lobby lobby that creates this match
     * @param socketOut OutputObjectStream of the client (only if the connection is socket)
     */
    public MatchSingleplayer(int matchId, String name, int difficulty, int turnTime, Lobby lobby, ObjectOutputStream socketOut) {
        super(lobby);
        this.matchId = matchId;
        this.decksContainer = new DecksContainer(1, difficulty);
        this.player = new PlayerSingleplayer(name);
        turnManager = new TurnManagerSingleplayer(this, turnTime);
        board = new Board(this, decksContainer.getToolCardDeck().getPickedCards(), decksContainer.getPublicObjectiveCardDeck().getPickedCards());
        System.out.println("New singleplayer matchId: " + this.matchId);
        observerSocket = socketOut;
        privateCardChosen = false;
        if (observerSocket != null) {
            startMatch();
        }
    }

    /**
     *
     * @return the rmi observer for the match
     */
    public MatchObserver getObserverRmi() {
        return observerRmi;
    }

    /**
     *
     * @return the socket observer for the match
     */
    public ObjectOutputStream getObserverSocket() {
        return observerSocket;
    }

    /**
     *
     * @return the match id
     */
    public int getMatchId() {
        return matchId;
    }

    /**
     *
     * @return the match turn manager
     */
    public TurnManagerSingleplayer getTurnManager() {
        return turnManager;
    }

    /**
     *
     * @return the player of the match
     */
    public PlayerSingleplayer getPlayer() {
        return player;
    }

    public boolean isPrivateCardChosen() {
        return privateCardChosen;
    }
    /**
     *Assigns to the player two private objective cards randomly chosen from the corresponding deck
     */
    @Override
    public void drawPrivateObjectiveCards() {
        player.setPrivateObjectiveCards(decksContainer.getPrivateObjectiveCardDeck().getPickedCards());
    }

    /**
     * Calculates the final score and checks if the player won the game.
     */
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

    /**
     * Beginning of the match: round counter is set to 0.
     * Checking if the player has chosen his window pattern card
     */
    @Override
    public void run() {
        roundCounter = 0;
        drawPrivateObjectiveCards();

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

        turnManager.run();
    }

    /**
     *
     * @param name player's name
     * @param index index of the window pattern card chosen among the four proposed
     */
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
                observerSocket.writeObject(new AfterWindowChoiceResponse());
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

    /**
     * If result == true the player's scheme card has to be updated.
     * @param result boolean giving information about the scheme card status.
     */
    private void schemeCardToBeUpdated(boolean result) {

        String [][] schemeCard= new String[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++)
                schemeCard[i][j] = player.getSchemeCard().getWindow()[i][j].toString();
        }

        if (result) {
            if (observerRmi != null) {
                try {
                    observerRmi.onMyWindow(schemeCard);
                } catch (RemoteException e) {
                    terminateMatch();
                    System.out.println("Match ended due to disconnection!");
                }
            } else if (observerSocket != null) {
                try {
                    observerSocket.writeObject(new MyWindowResponse(schemeCard));
                    observerSocket.reset();
                } catch (IOException e) {
                    terminateMatch();
                    System.out.println("Match ended due to disconnection!");
                }
            }
        }
    }

    /**
     * If reserveToBeUpdated == True, the reserve has to be updated.
     * @param reserveToBeUpdated boolean giving information about the reserve status.
     */
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

    /**
     *
     * @param name Player's name
     * @param index index of the dice in the reserve that the player wants to place
     * @param x row index of the chosen dice new position in the player's scheme card
     * @param y column index of the chosen dice new position in the player's scheme card
     * @return true if the player is allowed to place a dice (if isDiceAction()==true the player has already
     * placed a dice in this turn) and the dice is placed correctly.
     */
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

    /**
     * Places the dice taken from the bag after using tool card 11.
     * @param name Player's name
     * @param x row index of the chosen dice new position in the player's scheme card
     * @param y column index of the chosen dice new position in the player's scheme card
     * @return true if the player is allowed to place a dice (if isDiceAction()==true the player has already
     * placed a dice in this turn) and the dice is placed correctly.
     */
    @Override
    public boolean placeDiceTool11(String name, int x, int y){
        if (!isDiceAction()) {
            boolean result;
            result = getPlayer().getSchemeCard().putDice(getPlayer().getDiceFromBag(), x, y);
            setDiceAction(result);
            schemeCardToBeUpdated(result);
            synchronized (getLock()) {
                getLock().notifyAll();
            }
            return result;
        }
        return false;
    }

    /**
     * Terminates the match and removes it from the single player matches map.
     */
    @Override
    public void terminateMatch() {
        lobby.removeMatchSingleplayer(player.getName());
        localThread.interrupt();
    }

    /**
     * Method used to set the parameters needed by the effect of tool 1.
     * This tool card allows the player to increment or decrement by 1 the value of a chosen dice in the reserve.
     * The player can't increment the value of a dice with value 6 and can't decrement the value of a dice with value 1.
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param diceChosen Dice from the reserve whose value is modified.
     * @param incrOrDecr Choice made by the player.
     * @param name Player's name.
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard1(int diceToBeSacrificed, int diceChosen, String incrOrDecr, String name) {
        if (!isToolAction()) {
            getPlayer().setDice(diceChosen);
            getPlayer().setChoice(incrOrDecr);
            getPlayer().setDiceToBeSacrificed(diceToBeSacrificed);
            boolean result = getBoard().findAndUseToolCard(1, getPlayer(), this);
            if (result) getPlayer().setDiceToBeSacrificed(9);
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

    /**
     * Method used to set the parameters needed by the effects of tool cards 2 and 3.
     * Both tool cards allow the player to move a dice in his scheme card.
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param n index of the tool card selected (it can be 2 or 3).
     * @param startX Row index of the chosen dice to move.
     * @param startY Column index of the chosen dice to move.
     * @param finalX Row index of the chosen dice new position.
     * @param finalY Column index of the chosen dice new position.
     * @param name Player's name.
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard2or3(int diceToBeSacrificed, int n, int startX, int startY, int finalX, int finalY, String name) {
        if (!isToolAction()) {
            getPlayer().setStartX1(startX);
            getPlayer().setStartY1(startY);
            getPlayer().setFinalX1(finalX);
            getPlayer().setFinalY1(finalY);
            getPlayer().setDiceToBeSacrificed(diceToBeSacrificed);
            boolean result = getBoard().findAndUseToolCard(n, getPlayer(), this);
            if (result) getPlayer().setDiceToBeSacrificed(9);
            reserveToBeUpdated(result);
            schemeCardToBeUpdated(result);
            setToolAction(result);
            synchronized (getLock()) {
                getLock().notifyAll();
            }
            return result;
        } else {
            return false;
        }
    }

    /**
     * Method used to set the parameters needed by the effect of tool card 4.
     * This tool card allows the player to move exactly 2 dices in his scheme card.
     * The player has to consider all placement rules.
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param startX1 Row index of the first chosen dice to move.
     * @param startY1 Column index of the first chosen dice to move.
     * @param finalX1 Row index of the first chosen dice new position.
     * @param finalY1 Column index of the first chosen dice new position.
     * @param startX2 Row index of the second chosen dice to move.
     * @param startY2 Column index of the second chosen dice to move.
     * @param finalX2 Row index of the second chosen dice new position.
     * @param finalY2 Column index of the second chosen dice new position.
     * @param name Player's name.
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
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
            if (result) getPlayer().setDiceToBeSacrificed(9);
            reserveToBeUpdated(result);
            schemeCardToBeUpdated(result);
            synchronized (getLock()) {
                getLock().notifyAll();
            }
            return result;
        } else {
            return false;
        }
    }

    /**
     * Method used to set the parameters needed by the effect of tool card 5.
     * This tool card allows the player to switch a dice chosen from the reserve with a dice in the round track.
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param diceChosen Dice from the reserve to swap with the dice from the round track.
     * @param roundChosen Round slot in the round track from which the player wants to take a dice.
     * @param diceChosenFromRound Position index of the chosen dice in the round slot.
     * @param name Player's name.
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard5(int diceToBeSacrificed, int diceChosen, int roundChosen, int diceChosenFromRound, String name) {
        if (!isToolAction()) {
            getPlayer().setDice(diceChosen);
            getPlayer().setRound(roundChosen);
            getPlayer().setDiceChosenFromRound(diceChosenFromRound);
            getPlayer().setDiceToBeSacrificed(diceToBeSacrificed);
            boolean result = getBoard().findAndUseToolCard(5, getPlayer(), this);
            if (result) getPlayer().setDiceToBeSacrificed(9);
            reserveToBeUpdated(result);
            setToolAction(result);
            if (observerRmi != null) {
                try {
                    observerRmi.onRoundTrack(board.getRoundTrack().toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
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

    /**
     * Method used to set the parameters needed by the effect of tool card 6.
     * This tool card allows the player to re roll a chosen dice in the reserve.
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param diceChosen Dice from the reserve to re roll.
     * @param name Player's name.
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard6(int diceToBeSacrificed, int diceChosen, String name) {
        if (!isToolAction()) {
            getPlayer().setDice(diceChosen);
            getPlayer().setDiceToBeSacrificed(diceToBeSacrificed);
            boolean result = getBoard().findAndUseToolCard(6, getPlayer(), this);
            if (result) getPlayer().setDiceToBeSacrificed(9);
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

    /**
     * Method used to set the parameters needed by the effect of tool card 7.
     * This tool card allows the player to re roll all the dices in the reserve.
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param name Player's name.
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard7(int diceToBeSacrificed, String name) {
        if (!isToolAction()) {
            getPlayer().setDiceToBeSacrificed(diceToBeSacrificed);
            boolean result = getBoard().findAndUseToolCard(7, getPlayer(), this);
            reserveToBeUpdated(result);
            setToolAction(result);
            if (result) getPlayer().setDiceToBeSacrificed(9);
            synchronized (getLock()) {
                getLock().notifyAll();
            }
            return result;
        } else {
            return false;
        }
    }

    /**
     * Method used to set the parameters needed by the effect of tool card 8.
     * This tool card allows the player to choose and place a second dice in his first turn.
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param name Player's name.
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard8(int diceToBeSacrificed, String name) {
        if (!isToolAction()) {
            boolean result;
            getPlayer().setDiceToBeSacrificed(diceToBeSacrificed);
            result = getBoard().findAndUseToolCard(8, getPlayer(), this);
            reserveToBeUpdated(result);
            setToolAction(result);
            if (result) getPlayer().setDiceToBeSacrificed(9);
            synchronized (getLock()) {
                getLock().notifyAll();
            }
            return result;
        } else {
            return false;
        }
    }

    /**
     * Method used to set the parameters needed by the effect of tool card 9.
     * This tool card allows the player to place one chosen dice from the reserve anywhere in his scheme card.
     * The new dice must not have any adjacent dice and the player has to consider all constraints.
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param diceChosen Dice from the reserve to place in the player's scheme card.
     * @param finalX1 Row index of the chosen dice new position.
     * @param finalY1 Column index of the chosen dice new position.
     * @param name Player's name.
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard9(int diceToBeSacrificed, int diceChosen, int finalX1, int finalY1, String name) {
        if (!isToolAction() && !isDiceAction()) {
            getPlayer().setDice(diceChosen);
            getPlayer().setFinalX1(finalX1);
            getPlayer().setFinalY1(finalY1);
            getPlayer().setDiceToBeSacrificed(diceToBeSacrificed);
            boolean result = getBoard().findAndUseToolCard(9, getPlayer(), this);
            if (result) getPlayer().setDiceToBeSacrificed(9);
            reserveToBeUpdated(result);
            schemeCardToBeUpdated(result);
            setDiceAction(result);
            setToolAction(result);
            synchronized (getLock()) {
                getLock().notifyAll();
            }
            return result;
        } else {
            return false;
        }
    }

    /**
     * Method used to set the parameters needed by the effect of tool card 10.
     * This tool card allows the player to put a chosen dice from the reserve upside down.
     * It means that if the dice value is 6, after using the tool card it'll be 1, if it's 5, it'll be 2 and so on.
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param diceChosen Dice from the reserve to put upside down.
     * @param name Player's name.
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard10(int diceToBeSacrificed, int diceChosen, String name) {
        if (!isToolAction()) {
            getPlayer().setDice(diceChosen);
            getPlayer().setDiceToBeSacrificed(diceToBeSacrificed);
            boolean result = getBoard().findAndUseToolCard(10, getPlayer(), this);
            if (result) getPlayer().setDiceToBeSacrificed(9);
            reserveToBeUpdated(result);
            setToolAction(result);
            synchronized (getLock()) {
                getLock().notifyAll();
            }
            return result;
        } else return false;
    }

    /**
     * Method used to set the parameters needed by the effect of tool card 11.
     * This tool card allows the player to put a dice of the reserve back in the dices bag, and get a new dice from the
     * bag.
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param diceChosen Dice from the reserve to put back in the dices bag.
     * @param name Player's name.
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard11(int diceToBeSacrificed, int diceChosen, String name) {
        if (!(isToolAction() || isDiceAction())) {
            getPlayer().setDice(diceChosen);
            getPlayer().setDiceToBeSacrificed(diceToBeSacrificed);
            boolean result = getBoard().findAndUseToolCard(11, getPlayer(), this);
            reserveToBeUpdated(result);
            if (result) getPlayer().setDiceToBeSacrificed(9);
            setToolAction(result);
            return result;
        } else return false;
    }

    /**
     * Method used to set the parameters needed by the effect of tool card 12.
     * This tool card allows the player to move up to 2 dices in his scheme card. These dices must have the same color
     * of a dice in the round track.
     * The player has to consider all placement rules.
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param roundFromTrack Round slot in round track from which the player wants to choose a dice.
     * @param diceInRound Index of the chosen dice from the round slot.
     * @param startX1 Row index of the first chosen dice to move.
     * @param startY1 Column index of the first chosen dice to move.
     * @param finalX1 Row index of the first chosen dice new position.
     * @param finalY1 Column index of the first chosen dice new position.
     * @param startX2 Row index of the second chosen dice to move.
     * @param startY2 Column index of the second chosen dice to move.
     * @param finalX2 Row index of the second chosen dice new position.
     * @param finalY2 Column index of the second chosen dice new position.
     * @param name Player's name.
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard12(int diceToBeSacrificed, int roundFromTrack, int diceInRound, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String name) {
        if (!isToolAction()) {
            getPlayer().setRound(roundFromTrack);
            getPlayer().setDiceChosenFromRound(diceInRound);
            getPlayer().setStartX1(startX1);
            getPlayer().setStartY1(startY1);
            getPlayer().setFinalX1(finalX1);
            getPlayer().setFinalY1(finalY1);
            getPlayer().setStartX2(startX2);
            getPlayer().setStartY2(startY2);
            getPlayer().setFinalX2(finalX2);
            getPlayer().setFinalY2(finalY2);
            getPlayer().setDiceToBeSacrificed(diceToBeSacrificed);
            boolean result = getBoard().findAndUseToolCard(12, getPlayer(), this);
            reserveToBeUpdated(result);
            if (result) getPlayer().setDiceToBeSacrificed(9);
            schemeCardToBeUpdated(result);
            setToolAction(result);
            synchronized (getLock()) {
                getLock().notifyAll();
            }

            return result;
        } else return false;
    }

    /**
     * Sets the Rmi observer for the match.
     * @param observer Is the rmi observer.
     */
    public void observeMatchRemote(MatchObserver observer) {
        observerRmi = observer;
        startMatch();
    }

    /**
     * Starts the match.
     */
    private void startMatch() {
        localThread = new Thread(this);
        localThread.start();
    }

    public void setPrivateCardChosen(int position){
        privateCardChosen = true;
        selectedPrivateCard = position;
        synchronized (getLock()) {
            getLock().notifyAll();
        }
    }
}
