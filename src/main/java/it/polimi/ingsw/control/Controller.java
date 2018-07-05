package it.polimi.ingsw.control;

import it.polimi.ingsw.model.gamelogic.ConnectionStatus;
import it.polimi.ingsw.model.gamelogic.Lobby;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.socket.SocketHandler;
import it.polimi.ingsw.socket.requests.*;
import it.polimi.ingsw.socket.responses.*;
import it.polimi.ingsw.view.LobbyObserver;
import it.polimi.ingsw.view.MatchObserver;

import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Controller extends UnicastRemoteObject implements RemoteController, RequestHandler {

    private transient Lobby lobby;
    private transient List<SocketHandler> socketHandlers; //Intermediate variable, useful between View and Model

    /**
     * Controller constructor, server side to manage both rmi and socket connections.
     * In particular the handle methods manage the socket requests, that then call the methods already defined for the rmi methods
     *
     * @param lobby represents the lobby instance who has knowledge of every match in the model status
     */
    public Controller(Lobby lobby) throws RemoteException {
        super();
        this.lobby = lobby;
        this.socketHandlers = new ArrayList<>();
    }

    /**
     * Checks the status of a player
     *
     * @param name represents the name of the player on which the status is checked
     * @return the status of the player who has that name
     */
    @Override
    public ConnectionStatus checkName(String name) {
        return lobby.checkName(name);
    }

    /**
     * Creates a singleplayer match
     *
     * @param name         is the name of the single-player
     * @param difficulty   is the number of toolcards the player has chosen to use
     * @param outputStream is the stream used to communicate with the that player's view
     */
    @Override
    public void createMatch(String name, int difficulty, ObjectOutputStream outputStream) {
        this.lobby.addUsername(name);
        this.lobby.createSingleplayerMatch(name, difficulty, outputStream);
    }

    /**
     * Adds a player to the waiting room and then places him into a match
     *
     * @param name is the name added to the waiting room and then processed and placed in a match
     */
    @Override
    public void addPlayer(String name) {
        this.lobby.addUsername(name);
        this.lobby.addToWaitingPlayers(name);
    }

    /**
     * Removes a player from waiting players
     *
     * @param name is the name of the player who has to be removed
     */
    @Override
    public void removePlayer(String name) {
        lobby.getRemoteObservers().remove(name);
        lobby.removeFromWaitingPlayers(name);
    }

    /**
     * Passes the turn to the next player
     *
     * @param name   is the name of the player who wants to pass the turn
     * @param single is the boolean used to distinguish between single and multiplayer match
     */
    @Override
    public void goThrough(String name, boolean single) {
        if (single) {
            lobby.getSingleplayerMatches().get(name).goThrough();
        } else {
            lobby.getMultiplayerMatches().get(name).goThrough();
        }
    }

    /**
     * Places a dice in the player's scheme card
     *
     * @param index  is the reserve index used to take the right dice
     * @param x      is the x coordinate of the dice destination square
     * @param y      is the y coordinate of the dice destination square
     * @param name   is the name of the player who wants to place the dice
     * @param single is the boolean used to distinguish between single and multiplayer match
     * @return true if the dice is correctly placed
     */
    @Override
    public boolean placeDice(int index, int x, int y, String name, boolean single) {
        if (single) {
            return lobby.getSingleplayerMatches().get(name).placeDice(name, index, x, y);
        } else {
            return lobby.getMultiplayerMatches().get(name).placeDice(name, index, x, y);
        }
    }

    /**
     * Places the dice from the toolcard 11 in the player's scheme card
     *
     * @param x      is the x coordinate of the dice destination square
     * @param y      is the y coordinate of the dice destination square
     * @param name   is the name of the player who wants to place the dice
     * @param single is the boolean used to distinguish between single and multiplayer match
     * @return true if the dice is correctly placed
     */
    @Override
    public boolean placeDiceTool11(int x, int y, String name, boolean single) {
        if (single) {
            return lobby.getSingleplayerMatches().get(name).placeDiceTool11(name, x, y);
        } else {
            return lobby.getMultiplayerMatches().get(name).placeDiceTool11(name, x, y);
        }
    }

    /**
     * Chooses the window pattern card
     *
     * @param name   is the name of the player who wants to choose the window pattern card
     * @param index  is the windows index used to take the right scheme card
     * @param single is the boolean used to distinguish between single and multiplayer match
     */
    @Override
    public void chooseWindow(String name, int index, boolean single) {
        if (single) {
            lobby.getSingleplayerMatches().get(name).setWindowPatternCard(name, index);
        } else {
            lobby.getMultiplayerMatches().get(name).setWindowPatternCard(name, index);
        }
    }

    /**
     * Lets the player quit the game
     *
     * @param name   is the name of the player who wants to quit the game
     * @param single is the boolean used to distinguish between single and multiplayer match
     */
    @Override
    public void quitGame(String name, boolean single) {
        if (single) {
            lobby.getSingleplayerMatches().get(name).terminateMatch();
        } else {
            lobby.disconnect(name);
        }
    }

    /**
     * Lets the player to be set to CONNECTED status again
     *
     * @param name is the name of the player who wants to reconnect to the match previously left
     */
    @Override
    public void reconnect(String name) {
        lobby.reconnect(name);
    }

    /**
     * Asks for dice color from toolcard 11
     *
     * @param name   is the name of the player who has used the toolcard 11 and wants to know the dice color proposed to him
     * @param single is the boolean used to distinguish between single and multiplayer match
     * @return true if the color is proposed correctly
     */
    @Override
    public Colors askForDiceColor(String name, boolean single) {
        if (single) {
            return lobby.getSingleplayerMatches().get(name).getPlayer().getDiceFromBag().getColor();
        } else {
            return lobby.getMultiplayerMatches().get(name).getPlayer(name).getDiceFromBag().getColor();
        }
    }

    /**
     * Sets the dice value chosen by the player while using the toolcard 11
     *
     * @param value  is the value chosen by the player while using the toolcard 11
     * @param name   is the name of the player who has used the toolcard 11
     * @param single is the boolean used to distinguish between single and multiplayer match
     */
    @Override
    public void setDiceValue(int value, String name, boolean single) {
        if (single) {
            lobby.getSingleplayerMatches().get(name).getPlayer().getDiceFromBag().setValue(value);
        } else {
            lobby.getMultiplayerMatches().get(name).getPlayer(name).getDiceFromBag().setValue(value);
        }
    }

    /**
     * Method used to set the parameters needed by the effect of tool 1.
     * This tool card allows the player to increment or decrement by 1 the value of a chosen dice in the reserve.
     * The player can't increment the value of a dice with value 6 and can't decrement the value of a dice with value 1.
     *
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card (used only for singleplayer)
     * @param diceChosen         Dice from the reserve whose value is modified.
     * @param incrOrDecr         Choice made by the player.
     * @param name               Player's name.
     * @param single             True in case of singleplayer
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard1(int diceToBeSacrificed, int diceChosen, String incrOrDecr, String name, boolean single) {
        if (single) {
            return lobby.getSingleplayerMatches().get(name).useToolCard1(diceToBeSacrificed, diceChosen, incrOrDecr, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard1(diceToBeSacrificed, diceChosen, incrOrDecr, name);
        }
    }

    /**
     * Method used to set the parameters needed by the effects of tool cards 2 and 3.
     * Both tool cards allow the player to move a dice in his scheme card.
     *
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param n                  index of the tool card selected (it can be 2 or 3).
     * @param startX             Row index of the chosen dice to move.
     * @param startY             Column index of the chosen dice to move.
     * @param finalX             Row index of the chosen dice new position.
     * @param finalY             Column index of the chosen dice new position.
     * @param name               Player's name.
     * @param single             True in case of singleplayer
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard2or3(int diceToBeSacrificed, int n, int startX, int startY, int finalX, int finalY, String name, boolean single) {
        if (single) {
            return lobby.getSingleplayerMatches().get(name).useToolCard2or3(diceToBeSacrificed, n, startX, startY, finalX, finalY, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard2or3(diceToBeSacrificed, n, startX, startY, finalX, finalY, name);
        }
    }

    /**
     * Method used to set the parameters needed by the effect of tool card 4.
     * This tool card allows the player to move exactly 2 dices in his scheme card.
     * The player has to consider all placement rules.
     *
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param startX1            Row index of the first chosen dice to move.
     * @param startY1            Column index of the first chosen dice to move.
     * @param finalX1            Row index of the first chosen dice new position.
     * @param finalY1            Column index of the first chosen dice new position.
     * @param startX2            Row index of the second chosen dice to move.
     * @param startY2            Column index of the second chosen dice to move.
     * @param finalX2            Row index of the second chosen dice new position.
     * @param finalY2            Column index of the second chosen dice new position.
     * @param name               Player's name.
     * @param single             True in case of singleplayer
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard4(int diceToBeSacrificed, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String name, boolean single) {
        if (single) {
            return lobby.getSingleplayerMatches().get(name).useToolCard4(diceToBeSacrificed, startX1, startY1, finalX1, finalY1, startX2, startY2, finalX2, finalY2, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard4(diceToBeSacrificed, startX1, startY1, finalX1, finalY1, startX2, startY2, finalX2, finalY2, name);
        }
    }

    /**
     * Method used to set the parameters needed by the effect of tool card 5.
     * This tool card allows the player to switch a dice chosen from the reserve with a dice in the round track.
     *
     * @param diceToBeSacrificed  Dice from the reserve to be sacrificed in order to use this tool card.
     * @param diceChosen          Dice from the reserve to swap with the dice from the round track.
     * @param roundChosen         Round slot in the round track from which the player wants to take a dice.
     * @param diceChosenFromRound Position index of the chosen dice in the round slot.
     * @param name                Player's name.
     * @param single              True in case of singleplayer
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard5(int diceToBeSacrificed, int diceChosen, int roundChosen, int diceChosenFromRound, String name, boolean single) {
        if (single) {
            return lobby.getSingleplayerMatches().get(name).useToolCard5(diceToBeSacrificed, diceChosen, roundChosen, diceChosenFromRound, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard5(diceToBeSacrificed, diceChosen, roundChosen, diceChosenFromRound, name);
        }
    }

    /**
     * Method used to set the parameters needed by the effect of tool card 6.
     * This tool card allows the player to re roll a chosen dice in the reserve.
     *
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param diceChosen         Dice from the reserve to re roll.
     * @param name               Player's name.
     * @param single             True in case of singleplayer
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard6(int diceToBeSacrificed, int diceChosen, String name, boolean single) {
        if (single) {
            return lobby.getSingleplayerMatches().get(name).useToolCard6(diceToBeSacrificed, diceChosen, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard6(diceToBeSacrificed, diceChosen, name);
        }
    }

    /**
     * Method used to set the parameters needed by the effect of tool card 7.
     * This tool card allows the player to re roll all the dices in the reserve.
     *
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param name               Player's name.
     * @param single             True in case of singleplayer
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard7(int diceToBeSacrificed, String name, boolean single) {
        if (single) {
            return lobby.getSingleplayerMatches().get(name).useToolCard7(diceToBeSacrificed, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard7(diceToBeSacrificed, name);
        }
    }

    /**
     * Method used to set the parameters needed by the effect of tool card 8.
     * This tool card allows the player to choose and place a second dice in his first turn.
     *
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param name               Player's name.
     * @param single             True in case of singleplayer
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard8(int diceToBeSacrificed, String name, boolean single) {
        if (single) {
            return lobby.getSingleplayerMatches().get(name).useToolCard8(diceToBeSacrificed, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard8(diceToBeSacrificed, name);
        }
    }


    /**
     * Method used to set the parameters needed by the effect of tool card 9.
     * This tool card allows the player to place one chosen dice from the reserve anywhere in his scheme card.
     * The new dice must not have any adjacent dice and the player has to consider all constraints.
     *
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param diceChosen         Dice from the reserve to place in the player's scheme card.
     * @param finalX1            Row index of the chosen dice new position.
     * @param finalY1            Column index of the chosen dice new position.
     * @param name               Player's name.
     * @param single             True in case of singleplayer
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard9(int diceToBeSacrificed, int diceChosen, int finalX1, int finalY1, String name, boolean single) {
        if (single) {
            return lobby.getSingleplayerMatches().get(name).useToolCard9(diceToBeSacrificed, diceChosen, finalX1, finalY1, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard9(diceToBeSacrificed, diceChosen, finalX1, finalY1, name);
        }
    }

    /**
     * Method used to set the parameters needed by the effect of tool card 10.
     * This tool card allows the player to put a chosen dice from the reserve upside down.
     * It means that if the dice value is 6, after using the tool card it'll be 1, if it's 5, it'll be 2 and so on.
     *
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param diceChosen         Dice from the reserve to put upside down.
     * @param name               Player's name.
     * @param single             True in case of singleplayer
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard10(int diceToBeSacrificed, int diceChosen, String name, boolean single) {
        if (single) {
            return lobby.getSingleplayerMatches().get(name).useToolCard10(diceToBeSacrificed, diceChosen, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard10(diceToBeSacrificed, diceChosen, name);
        }
    }

    /**
     * Method used to set the parameters needed by the effect of tool card 11.
     * This tool card allows the player to put a dice of the reserve back in the dices bag, and get a new dice from the
     * bag.
     *
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param diceChosen         Dice from the reserve to put back in the dices bag.
     * @param name               Player's name.
     * @param single             True in case of singleplayer
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard11(int diceToBeSacrificed, int diceChosen, String name, boolean single) {
        if (single) {
            return lobby.getSingleplayerMatches().get(name).useToolCard11(diceToBeSacrificed, diceChosen, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard11(diceToBeSacrificed, diceChosen, name);
        }
    }

    /**
     * Method used to set the parameters needed by the effect of tool card 12.
     * This tool card allows the player to move up to 2 dices in his scheme card. These dices must have the same color
     * of a dice in the round track.
     * The player has to consider all placement rules.
     *
     * @param diceToBeSacrificed Dice from the reserve to be sacrificed in order to use this tool card.
     * @param roundFromTrack     Round slot in round track from which the player wants to choose a dice.
     * @param diceInRound        Index of the chosen dice from the round slot.
     * @param startX1            Row index of the first chosen dice to move.
     * @param startY1            Column index of the first chosen dice to move.
     * @param finalX1            Row index of the first chosen dice new position.
     * @param finalY1            Column index of the first chosen dice new position.
     * @param startX2            Row index of the second chosen dice to move.
     * @param startY2            Column index of the second chosen dice to move.
     * @param finalX2            Row index of the second chosen dice new position.
     * @param finalY2            Column index of the second chosen dice new position.
     * @param name               Player's name.
     * @param single             True in case of singleplayer
     * @return True if the player hasn't used other tool cards in this turn and the card effect is applied correctly.
     */
    @Override
    public boolean useToolCard12(int diceToBeSacrificed, int roundFromTrack, int diceInRound, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String name, boolean single) {
        if (single) {
            return lobby.getSingleplayerMatches().get(name).useToolCard12(diceToBeSacrificed, roundFromTrack, diceInRound, startX1, startY1, finalX1, finalY1, startX2, startY2, finalX2, finalY2, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard12(diceToBeSacrificed, roundFromTrack, diceInRound, startX1, startY1, finalX1, finalY1, startX2, startY2, finalX2, finalY2, name);
        }
    }

    /**
     * Removes singleplayer match from all the matches known by the lobby
     *
     * @param name is the singplayer name corresponding to that match
     */
    @Override
    public void removeMatch(String name) {
        lobby.removeFromMatchMulti(name);
    }

    /**
     * Handles the CheckUsernameRequest sent by the view and directed to the the model
     *
     * @param request is a CheckUsernameRequest, used to check if the name is already taken
     * @return the affermative response in case of request success
     */
    @Override
    public Response handle(CheckUsernameRequest request) {
        return new NameAlreadyTakenResponse(checkName(request.getUsername()));
    }

    /**
     * Handles the CreateMatchRequest sent by the view and directed to the the model
     *
     * @param request is a CreateMatchRequest, used to create a singleplayer match
     * @return a null value; the return notify from server to client is managed directly in the model
     */
    @Override
    public Response handle(CreateMatchRequest request) {
        createMatch(request.getUsername(), request.getDifficulty(), socketHandlers.remove(0).getOut());
        return null;
    }

    /**
     * Handles the AddPlayerRequest sent by the view and directed to the the model
     *
     * @param request is a AddPlayerRequest, used to a player in a multiplayer match
     * @return a null value; the return notify from server to client is managed directly in the model
     */
    @Override
    public Response handle(AddPlayerRequest request) {
        if (socketHandlers.size() > 1) {
            SocketHandler toBeSaved = socketHandlers.get(socketHandlers.size() - 1);
            socketHandlers.clear();
            socketHandlers.add(toBeSaved);
        }
        lobby.getSocketObservers().put(request.getUsername(), socketHandlers.remove(0).getOut());
        addPlayer(request.getUsername());
        return null;
    }

    /**
     * Handles the RemoveFromWaitingPlayersRequest sent by the view and directed to the the model
     *
     * @param request is a RemoveFromWaitingPlayersRequest, used to remove a player from a multiplayer match
     * @return a null value; the return notify from server to client is managed directly in the model
     */
    @Override
    public Response handle(RemoveFromWaitingPlayersRequest request) {
        lobby.getSocketObservers().remove(request.getName());
        lobby.removeFromWaitingPlayers(request.getName());
        return null;
    }

    /**
     * Handles the GoThroughRequest sent by the view and directed to the the model
     *
     * @param request is a GoThroughRequest, used by a player pass the turn to the next player
     * @return a null value; the return notify from server to client is managed directly in the model
     */
    @Override
    public Response handle(GoThroughRequest request) {
        goThrough(request.getUsername(), request.isSinglePlayer());
        return null;
    }

    /**
     * Handles the ChooseWindowRequest sent by the view and directed to the the model
     *
     * @param request is a ChooseWindowRequest, used by a player to choose the window pattern card
     * @return a null value; the return notify from server to client is managed directly in the model
     */
    @Override
    public Response handle(ChooseWindowRequest request) {
        chooseWindow(request.getUsername(), request.getValue(), request.isSingle());
        return null;
    }

    /**
     * Handles the PlaceDiceRequest sent by the view and directed to the the model
     *
     * @param request is a PlaceDiceRequest, used by a player to place a dice
     * @return a null value; the return notify from server to client is managed directly in the model
     */
    @Override
    public Response handle(PlaceDiceRequest request) {
        placeDice(request.getDiceChosen(), request.getCoordinateX(), request.getCoordinateY(), request.getUsername(), request.isSingle());
        return null;
    }

    /**
     * Handles the PlaceDiceTool11Request sent by the view and directed to the the model
     *
     * @param request is a PlaceDiceTool11Request, used by a player to place the dice proposed by toolcard 11
     * @return a DicePlacedReponse that says if the dice has been placed correctly
     */
    @Override
    public Response handle(PlaceDiceTool11Request request) {
        boolean placed = placeDiceTool11(request.getCoordinateX(), request.getCoordinateY(), request.getUsername(), request.isSingle());
        return new DicePlacedResponse(placed);
    }

    /**
     * Handles the UseToolCard1Request sent by the view and directed to the the model
     *
     * @param request is a UseToolCard1Request, used by a player to use that toolcard
     * @return a ToolCardEffectAppliedResponse that says if the effect has been applied correctly
     */
    @Override
    public Response handle(UseToolCard1Request request) {
        boolean effectApplied = useToolCard1(request.getDiceToBeSacrificed(), request.getDiceChosen(), request.getIncrOrDecr(), request.getUsername(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    /**
     * Handles the UseToolCard2or3Request sent by the view and directed to the the model
     *
     * @param request is a UseToolCard2or3Request, used by a player to use that toolcard
     * @return a ToolCardEffectAppliedResponse that says if the effect has been applied correctly
     */
    @Override
    public Response handle(UseToolCard2or3Request request) {
        boolean effectApplied = useToolCard2or3(request.getDiceToBeSacrificed(), request.getN(), request.getStartX(), request.getStartY(), request.getFinalX(), request.getFinalY(), request.getUsername(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    /**
     * Handles the UseToolCard4Request sent by the view and directed to the the model
     *
     * @param request is a UseToolCard4Request, used by a player to use that toolcard
     * @return a ToolCardEffectAppliedResponse that says if the effect has been applied correctly
     */
    @Override
    public Response handle(UseToolCard4Request request) {
        boolean effectApplied = useToolCard4(request.getDiceToBeSacrificed(), request.getStartX1(), request.getStartY1(), request.getFinalX1(), request.getFinalY1(), request.getStartX2(), request.getStartY2(), request.getFinalX2(), request.getFinalY2(), request.getUsername(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    /**
     * Handles the UseToolCard5Request sent by the view and directed to the the model
     *
     * @param request is a UseToolCard5Request, used by a player to use that toolcard
     * @return a ToolCardEffectAppliedResponse that says if the effect has been applied correctly
     */
    @Override
    public Response handle(UseToolCard5Request request) {
        boolean effectApplied = useToolCard5(request.getDiceToBeSacrificed(), request.getDiceChosen(), request.getRoundChosen(), request.getDiceChosenFromRound(), request.getName(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    /**
     * Handles the UseToolCard6Request sent by the view and directed to the the model
     *
     * @param request is a UseToolCard6Request, used by a player to use that toolcard
     * @return a ToolCardEffectAppliedResponse that says if the effect has been applied correctly
     */
    public Response handle(UseToolCard6Request request) {
        boolean effectApplied = useToolCard6(request.getDiceToBeSacrificed(), request.getDiceChosen(), request.getUsername(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    /**
     * Handles the UseToolCard7Request sent by the view and directed to the the model
     *
     * @param request is a UseToolCard7Request, used by a player to use that toolcard
     * @return a ToolCardEffectAppliedResponse that says if the effect has been applied correctly
     */
    @Override
    public Response handle(UseToolCard7Request request) {
        boolean effectApplied = useToolCard7(request.getDiceToBeSacrificed(), request.getUsername(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    /**
     * Handles the UseToolCard8Request sent by the view and directed to the the model
     *
     * @param request is a UseToolCard8Request, used by a player to use that toolcard
     * @return a ToolCardEffectAppliedResponse that says if the effect has been applied correctly
     */
    @Override
    public Response handle(UseToolCard8Request request) {
        boolean effectApplied = useToolCard8(request.getDiceToBeSacrificed(), request.getName(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    /**
     * Handles the UseToolCard9Request sent by the view and directed to the the model
     *
     * @param request is a UseToolCard9Request, used by a player to use that toolcard
     * @return a ToolCardEffectAppliedResponse that says if the effect has been applied correctly
     */
    @Override
    public Response handle(UseToolCard9Request request) {
        boolean effectApplied = useToolCard9(request.getDiceToBeSacrificed(), request.getDiceChosen(), request.getFinalX(), request.getFinalY(), request.getUsername(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    /**
     * Handles the UseToolCard10Request sent by the view and directed to the the model
     *
     * @param request is a UseToolCard10Request, used by a player to use that toolcard
     * @return a ToolCardEffectAppliedResponse that says if the effect has been applied correctly
     */
    @Override
    public Response handle(UseToolCard10Request request) {
        boolean effectApplied = useToolCard10(request.getDiceToBeSacrificed(), request.getDiceChosen(), request.getUsername(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    /**
     * Handles the UseToolCard11Request sent by the view and directed to the the model
     *
     * @param request is a UseToolCard11Request, used by a player to use that toolcard
     * @return a ToolCardEffectAppliedResponse that says if the effect has been applied correctly
     */
    @Override
    public Response handle(UseToolCard11Request request) {
        boolean effectApplied = useToolCard11(request.getDiceToBeSacrificed(), request.getDiceChosen(), request.getUsername(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    /**
     * Handles the UseToolCard12Request sent by the view and directed to the the model
     *
     * @param request is a UseToolCard12Request, used by a player to use that toolcard
     * @return a ToolCardEffectAppliedResponse that says if the effect has been applied correctly
     */
    @Override
    public Response handle(UseToolCard12Request request) {
        boolean effectApplied = useToolCard12(request.getDiceToBeSacrificed(), request.getRoundFromTrack(), request.getDiceInRound(), request.getStartX1(), request.getStartY1(), request.getFinalX1(), request.getFinalY1(), request.getStartX2(), request.getStartY2(), request.getFinalX2(), request.getFinalY2(), request.getName(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    /**
     * Handles the QuitGameRequest sent by the view and directed to the the model
     *
     * @param request is a QuitGameRequest, used by a player to quit the game
     * @return a null value; the return notify from server to client is managed directly in the model
     */
    @Override
    public Response handle(QuitGameRequest request) {
        quitGame(request.getName(), request.isSingle());
        return null;
    }

    /**
     * Handles the ReconnectionRequest sent by the view and directed to the the model
     *
     * @param request is a ReconnectionRequest, used by a player to reconnect to the match
     * @return a null value; the return notify from server to client is managed directly in the model
     */
    @Override
    public Response handle(ReconnectionRequest request) {
        lobby.getSocketObservers().put(request.getUsername(), socketHandlers.remove(0).getOut());
        reconnect(request.getUsername());
        return null;
    }

    /**
     * Handles the DiceColorRequest sent by the view and directed to the the model
     *
     * @param request is a DiceColorRequest, used by a player to asks for the color of the dice proposed by the toolcard 11
     * @return a DiceColorResponse with the color of the dice proposed
     */
    @Override
    public Response handle(DiceColorRequest request) {
        Colors color = askForDiceColor(request.getName(), request.isSingle());
        return new DiceColorResponse(color);
    }

    /**
     * Handles the SetDiceValueRequest sent by the view and directed to the the model
     *
     * @param request is a SetDiceValueRequest, used by a player to set the value of the dice proposed by the toolcard 11
     * @return a null value; the return notify from server to client is managed directly in the model
     */
    @Override
    public Response handle(SetDiceValueRequest request) {
        setDiceValue(request.getValue(), request.getName(), request.isSingle());
        return null;
    }

    /**
     * Handles the PrivateCardChosenRequest sent by the view and directed to the the model
     *
     * @param request is a PrivateCardChosenRequest, used by a player choose the private card in singleplayer mode
     * @return a null value; the return notify from server to client is managed directly in the model
     */
    @Override
    public Response handle(PrivateCardChosenRequest request) {
        choosePrivateCard(request.getUsername(), request.getCardPosition());
        return null;
    }

    /**
     * Handles the TerminateMatchRequest sent by the view and directed to the the model
     *
     * @param request is a TerminateWatchRequest, used by a player to terminate the match
     * @return a null value; the return notify from server to client is managed directly in the model
     */
    @Override
    public Response handle(TerminateMatchRequest request) {
        lobby.removeFromMatchMulti(request.getName());
        return null;
    }

    /**
     * Handles the PingRequest sent by the view and directed to the the model
     *
     * @param request is a PingRequest, used by a player to prove to the model that he is still connected
     * @return a null value; the return notify from server to client is managed directly in the model
     */
    @Override
    public Response handle(PingRequest request) {
        ping(request.getUsername(), request.isSingle());
        return null;
    }

    /**
     * Is used by the player to prove to the model that he is still connected
     *
     * @param username is the name of the player
     * @param single is true in case of singleplayer match
     */
    @Override
    public void ping(String username, boolean single) {
        if (single) {
            lobby.getSingleplayerMatches().get(username).ping(username);
        } else {
            lobby.getMultiplayerMatches().get(username).ping(username);
        }
    }

    /**
     * Is used by the player to chosse the private card in case of singleplayer
     *
     * @param username is the name of the player
     * @param cardPosition is the index used to select the right card
     */
    public void choosePrivateCard(String username, int cardPosition) {
        lobby.getSingleplayerMatches().get(username).setPrivateCardChosen(cardPosition);
    }


    /**
     * Adds a player username in the key values of the lobby remote obsevers map
     *
     * @param name is the player username
     * @param lobbyObserver receives notification from lobby
     */
    public void observeLobby(String name, LobbyObserver lobbyObserver) {
        lobby.observeLobbyRemote(name, lobbyObserver);
    }

    /**
     * Adds a player username in the key values of the match remote obsevers map
     *
     * @param username is the player username
     * @param observer receives notification from match
     * @param single is true in case of singleplayer match
     * @param reconnection is true in case of reconnection
     */
    public void observeMatch(String username, MatchObserver observer, boolean single, boolean reconnection) {
        lobby.observeMatchRemote(username, observer, single);

        if (reconnection) {
            lobby.transferAllData(username);
        }
    }

    /**
     * Adds a socketHandler server side for each socket connection detected
     *
     * @param socketHandler is the SocketHandler instance for the incoming socket connection
     */
    public void addSocketHandler(SocketHandler socketHandler) {
        this.socketHandlers.add(socketHandler);
    }

}
