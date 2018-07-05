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

    @Override
    public void handle(GameStartedResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onGameStarted(response.getNames(), response.getTurnTime());
        } else {
            socketGui.getGui().onGameStarted(response.isWindowChosen(), response.getNames(), response.getTurnTime());
        }
    }

    @Override
    public void handle(YourTurnResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onYourTurn(response.isMyTurn(), response.getName(), response.getRound(), response.getTurn());
        } else
            socketGui.getGui().onYourTurn(response.isMyTurn(), response.getName(), response.getRound(), response.getTurn());
    }

    @Override
    public void handle(ReserveResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onReserve(response.getReserve());
        } else socketGui.getGui().onReserve(response.getReserve());
    }

    @Override
    public void handle(MyWindowResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onMyWindow(response.getWindow());
        } else {
            socketGui.getGui().onMyWindow(response.getWindow());
        }
    }

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

    @Override
    public void handle(InitializationResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onInitialization(response.getToolcards(), response.getPublicCards(), response.getPrivateCard(), response.getPlayers());
        } else {
            socketGui.getGui().onInitialization(response.getToolcards(), response.getPublicCards(), response.getPrivateCard(), response.getPlayers());
        }
    }

    @Override
    public void handle(OtherTurnResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onOtherTurn(response.getName());
        } else {
            socketGui.getGui().onOtherTurn(response.getName());
        }
    }


    @Override
    public void handle(DicePlacedResponse response) {
        dicePlaced = response.isDone();
    }

    @Override
    public void handle(ToolCardEffectAppliedResponse response) {
        effectApplied = response.isEffectApplied();
    }

    @Override
    public void handle(ClosingGameResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onGameClosing();
        } else {
            socketGui.getGui().onGameClosing();
        }
    }

    @Override
    public void handle(PlayerExitGameResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onPlayerExit(response.getName());
        } else {
            socketGui.getGui().onPlayerExit(response.getName());
        }
    }

    @Override
    public void handle(PlayerReconnectionResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onPlayerReconnection(response.getName());
        } else {
            socketGui.getGui().onPlayerReconnection(response.getName());
        }
    }

    @Override
    public void handle(MyFavorTokensResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onMyFavorTokens(response.getValue());
        } else {
            socketGui.getGui().onMyFavorTokens(response.getValue());
        }
    }

    @Override
    public void handle(OtherFavorTokensResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onOtherFavorTokens(response.getValue(), response.getName());
        } else {
            socketGui.getGui().onOtherFavorTokens(response.getValue(), response.getName());
        }
    }

    @Override
    public void handle(OtherSchemeCardsResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onOtherSchemeCards(response.getScheme(), response.getName());
        } else {
            socketGui.getGui().onOtherSchemeCards(response.getScheme(), response.getName(), response.getCardName());
        }
    }


    @Override
    public void handle(GameEndResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onGameEnd(response.getWinner(), response.getNames(), response.getValues());
        } else {
            socketGui.getGui().onGameEndMulti(response.getWinner(), response.getNames(), response.getValues());
        }
    }

    @Override
    public void handle(AfterReconnectionResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onAfterReconnection(response.getToolcards(), response.getPublicCards(), response.getPrivateCard(), response.getReserve(), response.getRoundTrack(), response.getMyTokens(), response.getSchemeCard(), response.getOtherTokens(), response.getOtherSchemeCards(), response.isSchemeCardChosen(), response.getToolcardsPrices());
        } else
            socketGui.getGui().onAfterReconnection(response.getToolcards(), response.getPublicCards(), response.getPrivateCard(), response.getReserve(), response.getRoundTrack(), response.getMyTokens(), response.getSchemeCard(), response.getSchemeCardName(), response.getOtherTokens(), response.getOtherSchemeCards(), response.getOtherSchemeCardNamesMap(), response.isSchemeCardChosen(), response.getToolcardsPrices());
    }

    @Override
    public void handle(ProposeWindowResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onWindowChoice(response.getList());
        } else {
            socketGui.getGui().onWindowChoice(response.getList());
        }
    }

    @Override
    public void handle(CheckConnectionResponse response) {
        if (loginHandler.isCli())
            loginHandler.getWaitingRoomCli().onCheckConnection();
        else {
            loginHandler.getWaitingScreenHandler().onCheckConnection();
        }
    }

    @Override
    public void handle(ToolCardUsedByOthersResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onToolCardUsedByOthers(response.getName(), response.getToolCardNumber());
        } else {
            socketGui.getGui().onToolCardUsedByOthers(response.getName(), response.getToolCardNumber());
        }
    }

    @Override
    public void handle(GameEndSingleResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onGameEndSingle(response.getTarget(), response.getPoints());
        } else {
            socketGui.getGui().onGameEndSingle(response.getTarget(), response.getPoints());
        }
    }

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
