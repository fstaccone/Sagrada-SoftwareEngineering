package it.polimi.ingsw.control;

import it.polimi.ingsw.model.gamelogic.ConnectionStatus;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.socket.requests.Request;
import it.polimi.ingsw.socket.responses.*;
import it.polimi.ingsw.view.LoginHandler;
import it.polimi.ingsw.view.cli.SocketCli;
import it.polimi.ingsw.view.gui.SocketGui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SocketController implements ResponseHandler {

    private ConnectionStatus nameStatus = ConnectionStatus.ABSENT;
    private boolean dicePlaced = false;
    private boolean effectApplied = false;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private LoginHandler loginHandler;
    private SocketCli socketCli;
    private SocketGui socketGui;
    private Colors diceColor;
    private boolean single;

    /**
     * SocketController constructor, client side to manage socket connections.
     * There is one SocketController for each Client.
     *
     * @param in is the inputStream used to read socket responses from the server
     * @param out is the outputStream used to send socket requests to the server
     * @param loginHandler is the handler of the login stage
     * @param single is true in case of singleplayer match
     */
    public SocketController(ObjectInputStream in, ObjectOutputStream out, LoginHandler loginHandler, boolean single) {
        this.in = in;
        this.out = out;
        this.loginHandler = loginHandler;
        this.single = single;
    }

    /**
     * Sends every socket request to the server through the outputstream
     *
     * @param request is the socket request sent to the server
     */
    public void request(Request request) {
        try {
            out.writeObject(request);
            out.reset();
        } catch (IOException e) {
            System.err.println("Problema di connessione con il server!" );
            System.exit(0);
        }
    }

    /**
     * Reads every socket response received by the server through the inputstream
     *
     * @return the response received from the server
     */
    public Response nextResponse() {
        try {
            return ((Response) in.readObject());
        } catch (IOException e) {
            System.err.println("Problema di connessione con il server!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Problema di deserializzazione!" );
        }

        return null;
    }

    public ConnectionStatus isNameAlreadyTaken() {
        return nameStatus;
    }

    public void setSocketCli(SocketCli socketCli) {
        this.socketCli = socketCli;
    }

    public void setSocketGui(SocketGui socketGui) {
        this.socketGui = socketGui;
    }

    public void setDicePlaced(boolean dicePlaced) {
        this.dicePlaced = dicePlaced;
    }

    public boolean isDicePlaced() {
        return dicePlaced;
    }

    public void setEffectApplied(boolean effectApplied) {
        this.effectApplied = effectApplied;
    }

    public boolean isEffectApplied() {
        return effectApplied;
    }

    /**
     * Handles the NameAlreadyTakenResponse sent by the model and directed to the the view
     *
     * @param response is a NameAlreadyTakenResponse, that says if the name is already taken
     */
    @Override
    public void handle(NameAlreadyTakenResponse response) {
        this.nameStatus = response.getStatus();
    }

    /**
     * Handles the DiceColorResponse sent by the model and directed to the the view
     *
     * @param response is a DiceColorResponse, that says the dice color proposed by the toolcard 11
     */
    @Override
    public void handle(DiceColorResponse response) {
        this.diceColor = response.getDiceColor();
    }

    /**
     * Handles the WaitingPlayersResponse sent by the model and directed to the the view
     *
     * @param response is a WaitingPlayersResponse, that says the current waiting players in the room
     */
    @Override
    public void handle(WaitingPlayersResponse response) {
        if (loginHandler.isCli()) {
            loginHandler.getWaitingRoomCli().onWaitingPlayers(response.getWaitingPlayers());
        } else {
            loginHandler.getWaitingScreenHandler().onWaitingPlayers(response.getWaitingPlayers());
        }
    }

    /**
     * Handles the PlayerExitRoomResponse sent by the model and directed to the the view
     *
     * @param response is a PlayerExitRoomResponse, that says the name of the player who has left the room
     */
    @Override
    public void handle(PlayerExitRoomResponse response) {
        if (loginHandler.isCli())
            loginHandler.getWaitingRoomCli().onPlayerExit(response.getName());
        else
            loginHandler.getWaitingScreenHandler().onPlayerExit(response.getName());
    }

    /**
     * Handles the LastPlayerRoomResponse sent by the model and directed to the the view
     *
     * @param response is a LastPlayerRoomResponse, that says if the player is the only one in the room
     */
    @Override
    public void handle(LastPlayerRoomResponse response) {
        if (loginHandler.isCli())
            loginHandler.getWaitingRoomCli().onLastPlayer(response.getName());
        else
            loginHandler.getWaitingScreenHandler().onLastPlayer(response.getName());
    }


    /**
     * Handles the MatchStartedResponse sent by the model and directed to the the view
     *
     * @param response is a MatchStartedResponse, that says if the match has started
     */
    @Override
    public void handle(MatchStartedResponse response) {
        loginHandler.onMatchStartedSocket();
    }

    /**
     * Handles the RoundTrackResponse sent by the model and directed to the the view
     *
     * @param response is a RoundTrackResponse, that updated the RoundTrack state
     */
    @Override
    public void handle(RoundTrackResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onRoundTrack(response.getRoundTrack());
        } else socketGui.getGui().onRoundTrack(response.getRoundTrack());
    }

    /**
     * Handles the GameStartedResponse sent by the model and directed to the the view
     *
     * @param response is a GameStartedResponse, that says if the game has started
     */
    @Override
    public void handle(GameStartedResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onGameStarted(response.getNames(), response.getTurnTime());
        } else {
            socketGui.getGui().onGameStarted(response.isWindowChosen(), response.getNames(), response.getTurnTime());
        }
    }

    /**
     * Handles the YourTurnResponse sent by the model and directed to the the view
     *
     * @param response is a YourTurnResponse, that tells the player is it is his turn
     */
    @Override
    public void handle(YourTurnResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onYourTurn(response.isMyTurn(), response.getName(), response.getRound(), response.getTurn());
        } else
            socketGui.getGui().onYourTurn(response.isMyTurn(), response.getName(), response.getRound(), response.getTurn());
    }

    /**
     * Handles the ReserveResponse sent by the model and directed to the the view
     *
     * @param response is a ReserveResponse, that updates the Reserve state
     */
    @Override
    public void handle(ReserveResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onReserve(response.getReserve());
        } else socketGui.getGui().onReserve(response.getReserve());
    }

    /**
     * Handles the MyWindowResponse sent by the model and directed to the the view
     *
     * @param response is a MyWindowResponse, that updates the window state of the current player
     */
    @Override
    public void handle(MyWindowResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onMyWindow(response.getWindow());
        } else {
            socketGui.getGui().onMyWindow(response.getWindow());
        }
    }

    /**
     * Handles the AfterWindowChoiceResponse sent by the model and directed to the the view
     *
     * @param response is a AfterWindowChoiceResponse, that sets the after window state of the current player view
     */
    @Override
    public void handle(AfterWindowChoiceResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onAfterWindowChoice();
        } else {
            if (single)
                socketGui.getGui().onAfterWindowChoiceSingleplayer();
            else
                socketGui.getGui().onAfterWindowChoiceMultiplayer();
        }
    }

    /**
     * Handles the InitializationResponse sent by the model and directed to the the view
     *
     * @param response is a InitializationResponse, that sets the Initialization state of the current player view
     */
    @Override
    public void handle(InitializationResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onInitialization(response.getToolcards(), response.getPublicCards(), response.getPrivateCard(), response.getPlayers());
        } else {
            socketGui.getGui().onInitialization(response.getToolcards(), response.getPublicCards(), response.getPrivateCard(), response.getPlayers());
        }
    }

    /**
     * Handles the OtherTurnResponse sent by the model and directed to the the view
     *
     * @param response is a OtherTurnResponse, that says whose is the current turn
     */
    @Override
    public void handle(OtherTurnResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onOtherTurn(response.getName());
        } else {
            socketGui.getGui().onOtherTurn(response.getName());
        }
    }

    /**
     * Handles the DicePlacedResponse sent by the model and directed to the the view
     *
     * @param response is a DicePlacedResponse, that says if the dice has been placed correctly
     */
    @Override
    public void handle(DicePlacedResponse response) {
        dicePlaced = response.isDone();
    }

    /**
     * Handles the ToolCardEffectAppliedResponse sent by the model and directed to the the view
     *
     * @param response is a DicePlacedResponse, that says if the effect has been placed correctly
     */
    @Override
    public void handle(ToolCardEffectAppliedResponse response) {
        effectApplied = response.isEffectApplied();
    }

    /**
     * Handles the ClosingGameResponse sent by the model and directed to the the view
     *
     * @param response is a ClosingGameResponse, that communicates the closing of the game to the player
     */
    @Override
    public void handle(ClosingGameResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onGameClosing();
        } else {
            socketGui.getGui().onGameClosing();
        }
    }

    /**
     * Handles the PlayerExitGameResponse sent by the model and directed to the the view
     *
     * @param response is a PlayerExitGameResponse, that communicates the exit of a player to the current player
     */
    @Override
    public void handle(PlayerExitGameResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onPlayerExit(response.getName());
        } else {
            socketGui.getGui().onPlayerExit(response.getName());
        }
    }

    /**
     * Handles the PlayerReconnectionResponse sent by the model and directed to the the view
     *
     * @param response is a PlayerReconnectionResponse, that communicates the reconnection of a player to the current player
     */
    @Override
    public void handle(PlayerReconnectionResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onPlayerReconnection(response.getName());
        } else {
            socketGui.getGui().onPlayerReconnection(response.getName());
        }
    }

    /**
     * Handles the MyFavorTokensResponse sent by the model and directed to the the view
     *
     * @param response is a MyFavorTokensResponse, that updates the favor tokens number of the current player
     */
    @Override
    public void handle(MyFavorTokensResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onMyFavorTokens(response.getValue());
        } else {
            socketGui.getGui().onMyFavorTokens(response.getValue());
        }
    }

    /**
     * Handles the OtherFavorTokensResponse sent by the model and directed to the the view
     *
     * @param response is a OtherFavorTokensResponse, that updates the favor tokens number of the other players
     */
    @Override
    public void handle(OtherFavorTokensResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onOtherFavorTokens(response.getValue(), response.getName());
        } else {
            socketGui.getGui().onOtherFavorTokens(response.getValue(), response.getName());
        }
    }

    /**
     * Handles the OtherSchemeCardsResponse sent by the model and directed to the the view
     *
     * @param response is a OtherSchemeCardsResponse, that updates the scheme cards of the other players
     */
    @Override
    public void handle(OtherSchemeCardsResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onOtherSchemeCards(response.getScheme(), response.getName());
        } else {
            socketGui.getGui().onOtherSchemeCards(response.getScheme(), response.getName(), response.getCardName());
        }
    }

    /**
     * Handles the GameEndResponse sent by the model and directed to the the view
     *
     * @param response is a GameEndResponse, that communicates the ending of the game to the player
     */
    @Override
    public void handle(GameEndResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onGameEnd(response.getWinner(), response.getNames(), response.getValues());
        } else {
            socketGui.getGui().onGameEndMulti(response.getWinner(), response.getNames(), response.getValues());
        }
    }

    /**
     * Handles the AfterReconnectionResponse sent by the model and directed to the the view
     *
     * @param response is a AfterReconnectionResponse, that recovers the current model state of the current player view
     */
    @Override
    public void handle(AfterReconnectionResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onAfterReconnection(response.getToolcards(), response.getPublicCards(), response.getPrivateCard(), response.getReserve(), response.getRoundTrack(), response.getMyTokens(), response.getSchemeCard(), response.getOtherTokens(), response.getOtherSchemeCards(), response.isSchemeCardChosen(), response.getToolcardsPrices());
        } else
            socketGui.getGui().onAfterReconnection(response.getToolcards(), response.getPublicCards(), response.getPrivateCard(), response.getReserve(), response.getRoundTrack(), response.getMyTokens(), response.getSchemeCard(), response.getSchemeCardName(), response.getOtherTokens(), response.getOtherSchemeCards(), response.getOtherSchemeCardNamesMap(), response.isSchemeCardChosen(), response.getToolcardsPrices());
    }

    /**
     * Handles the ProposeWindowResponse sent by the model and directed to the the view
     *
     * @param response is a ProposeWindowResponse, that proposes the possible window pattern cards to the player
     */
    @Override
    public void handle(ProposeWindowResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onWindowChoice(response.getList());
        } else {
            socketGui.getGui().onWindowChoice(response.getList());
        }
    }

    /**
     * Handles the CheckConnectionResponse sent by the model and directed to the the view
     *
     * @param response is a CheckConnectionResponse, that checks the connection of the player
     */
    @Override
    public void handle(CheckConnectionResponse response) {
        if (loginHandler.isCli())
            loginHandler.getWaitingRoomCli().onCheckConnection();
        else {
            loginHandler.getWaitingScreenHandler().onCheckConnection();
        }
    }

    /**
     * Handles the ToolCardUsedByOthersResponse sent by the model and directed to the the view
     *
     * @param response is a ToolCardUsedByOthersResponse, that updates the current player in case of first use of a toolcard
     */
    @Override
    public void handle(ToolCardUsedByOthersResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onToolCardUsedByOthers(response.getName(), response.getToolCardNumber());
        } else {
            socketGui.getGui().onToolCardUsedByOthers(response.getName(), response.getToolCardNumber());
        }
    }


    /**
     * Handles the GameEndSingleResponse sent by the model and directed to the the view
     *
     * @param response is a GameEndSingleResponse, that communicates the ending of the game to the player
     */
    @Override
    public void handle(GameEndSingleResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onGameEndSingle(response.getTarget(), response.getPoints());
        } else {
            socketGui.getGui().onGameEndSingle(response.getTarget(), response.getPoints());
        }
    }

    /**
     * Handles the ChoosePrivateCardResponse sent by the model and directed to the the view
     *
     * @param response is a ChoosePrivateCardResponse, that proposes the two possible private cards to the single player
     */
    @Override
    public void handle(ChoosePrivateCardResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onChoosePrivateCard();
        } else {
            socketGui.getGui().onChoosePrivateCard();
        }
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public Colors getDiceColor() {
        return diceColor;
    }

}
