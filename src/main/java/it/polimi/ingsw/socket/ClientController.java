package it.polimi.ingsw.socket;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.LoginHandler;
import it.polimi.ingsw.SocketCli;
import it.polimi.ingsw.SocketGui;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.socket.requests.Request;
import it.polimi.ingsw.socket.responses.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientController implements ResponseHandler {

    private ConnectionStatus nameStatus = ConnectionStatus.ABSENT;
    private boolean dicePlaced = false;
    private boolean effectApplied = false;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private LoginHandler loginHandler;
    private SocketCli socketCli;
    private SocketGui socketGui;
    private Colors diceColor;


    public ClientController(ObjectInputStream in, ObjectOutputStream out, LoginHandler loginHandler) {
        this.in = in;
        this.out = out;
        this.loginHandler = loginHandler;
    }

    public void request(Request request) {
        try {
            out.writeObject(request);
            out.reset();
        } catch (IOException e) {
            System.err.println("Exception on network: " + e.getMessage());
        }
    }

    public Response nextResponse() {
        try {
            return ((Response) in.readObject());
        } catch (IOException e) {
            System.err.println("Exception on network: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Wrong deserialization: " + e.getMessage());
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

    @Override
    public void handle(NameAlreadyTakenResponse response) {
        this.nameStatus = response.status;
    }

    @Override
    public void handle(DiceColorResponse response) {
        this.diceColor = response.diceColor;
    }

    @Override
    public void handle(WaitingPlayersResponse response) {
        if (loginHandler.isCli())
            loginHandler.getWaitingRoomCli().onWaitingPlayers(response.waitingPlayers);
        else
            loginHandler.getWaitingScreenHandler().onWaitingPlayers(response.waitingPlayers);
        if (response.name != null) {
            if (!response.unique) {
                if (loginHandler.isCli())
                    loginHandler.getWaitingRoomCli().onWaitingPlayers(response.waitingPlayers);
                else
                    loginHandler.getWaitingScreenHandler().onWaitingPlayers(response.waitingPlayers);
            } else {
                if (loginHandler.isCli())
                    loginHandler.getWaitingRoomCli().onWaitingPlayers(response.waitingPlayers);
                else
                    loginHandler.getWaitingScreenHandler().onWaitingPlayers(response.waitingPlayers);
            }
        }
    }

    @Override
    public void handle(PlayerExitRoomResponse response) {
        if (loginHandler.isCli())
            loginHandler.getWaitingRoomCli().onPlayerExit(response.name);
        else
            loginHandler.getWaitingScreenHandler().onPlayerExit(response.name);
    }

    @Override
    public void handle(LastPlayerRoomResponse response) {
        if (loginHandler.isCli())
            loginHandler.getWaitingRoomCli().onLastPlayer(response.name);
        else
            loginHandler.getWaitingScreenHandler().onLastPlayer(response.name);
    }


    @Override//DA RIVEDERE
    public void handle(MatchStartedResponse response) {
        loginHandler.onMatchStartedSocket();
    }

    @Override
    public void handle(RoundTrackResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onRoundTrack(response.roundTrack);
        } else socketGui.getGui().onRoundTrack(response.roundTrack);
    }

    @Override
    public void handle(GameStartedResponse response) {
        if(socketCli != null){
            socketCli.getCli().onGameStarted(response.getNames());
        } else {
            socketGui.getGui().onGameStarted(response.isWindowChosen(), response.getNames());
        }
    }

    @Override
    public void handle(YourTurnResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onYourTurn(response.myTurn, response.string);
        } else socketGui.getGui().onYourTurn(response.myTurn, response.string);
    }

    @Override
    public void handle(ReserveResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onReserve(response.reserve);
        } else socketGui.getGui().onReserve(response.reserve);
    }

    @Override
    public void handle(MyWindowResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onMyWindow(response.window);
        } else {
            socketGui.getGui().onMyWindow(response.window);
        }
    }

    @Override
    public void handle(AfterWindowChoiseResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onAfterWindowChoise();
        } else
            socketGui.getGui().onAfterWindowChoice();
    }

    @Override
    public void handle(InitializationResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onInitialization(response.getToolcards(), response.getPublicCards(), response.getPrivateCard(), response.getPlayers());
        } else
            socketGui.getGui().onInitialization(response.getToolcards(), response.getPublicCards(), response.getPrivateCard(), response.getPlayers());
    }

    @Override
    public void handle(OtherTurnResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onOtherTurn(response.name);
        } else {
            socketGui.getGui().onOtherTurn(response.name);
        }
    }

    @Override
    public void handle(UpdateReserveResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onReserve(response.string);
        } else socketGui.getGui().onReserve(response.string);
    }

    @Override
    public void handle(DicePlacedResponse response) {
        //if (socketCli != null) {
        dicePlaced = response.done;
        //}
        //DA RIVEDERE PER GUI
    }

    @Override
    public void handle(ToolCardEffectAppliedResponse response) {
        //if (socketCli != null) {
        effectApplied = response.effectApplied;
        //}

    }

    @Override
    public void handle(ClosingGameResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onGameClosing();
        } else socketGui.getGui().onGameClosing();
    }

    @Override
    public void handle(PlayerExitGameResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onPlayerExit(response.name);
        } else socketGui.getGui().onPlayerExit(response.name);
    }

    @Override
    public void handle(PlayerReconnectionResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onPlayerReconnection(response.getName());
        } else socketGui.getGui().onPlayerReconnection(response.getName());
    }

    @Override
    public void handle(MyFavorTokensResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onMyFavorTokens(response.value);
        } else {
            socketGui.getGui().onMyFavorTokens(response.value);
        }
    }

    @Override
    public void handle(OtherFavorTokensResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onOtherFavorTokens(response.value, response.name);
        } else {
            socketGui.getGui().onOtherFavorTokens(response.value, response.name);
        }
    }

    @Override
    public void handle(OtherSchemeCardsResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onOtherSchemeCards(response.scheme, response.name);
        } else {
            socketGui.getGui().onOtherSchemeCards(response.scheme, response.name);
        }
    }


    @Override
    public void handle(GameEndResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onGameEnd(response.getWinner(), response.getNames(), response.getValues());
        } else {
            socketGui.getGui().onGameEnd(response.getWinner(), response.getNames(), response.getValues());
        }
    }

    @Override
    public void handle(AfterReconnectionResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onAfterReconnection(response.toolcards, response.publicCards, response.privateCard, response.reserve, response.roundTrack, response.myTokens, response.schemeCard, response.otherTokens, response.otherSchemeCards, response.schemeCardChosen);
        } else
            socketGui.getGui().onAfterReconnection(response.toolcards, response.publicCards, response.privateCard, response.reserve, response.roundTrack, response.myTokens, response.schemeCard, response.otherTokens, response.otherSchemeCards, response.schemeCardChosen);
    }

    @Override
    public void handle(ProposeWindowResponse response) {
        if (socketCli != null) {
            socketCli.getCli().onWindowChoise(response.list);
        } else socketGui.getGui().onWindowChoice(response.list);
    }

    public void handle(CheckConnectionResponse response) { //DA SISTEMARE ANCHE CON GUI
        if (loginHandler.isCli())
            loginHandler.getWaitingRoomCli().onCheckConnection();
        else
            loginHandler.getWaitingScreenHandler().onCheckConnection();
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
