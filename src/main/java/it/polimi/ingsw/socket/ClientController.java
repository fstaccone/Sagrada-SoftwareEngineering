package it.polimi.ingsw.socket;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.LoginHandler;
import it.polimi.ingsw.socket.responses.Response;
import it.polimi.ingsw.socket.requests.Request;
import it.polimi.ingsw.socket.responses.*;
import it.polimi.ingsw.SocketCli;

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

    public ConnectionStatus isNameAlreadyTaken() { return nameStatus; }

    public void setSocketCli(SocketCli socketCli) {
        this.socketCli = socketCli;
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
    public void handle(WaitingPlayersResponse response) {
        loginHandler.getWaitingScreenHandler().onWaitingPlayers(response.waitingPlayers);
        if (response.name != null) {
            if (!response.unique)
                loginHandler.getWaitingScreenHandler().onPlayerExit(response.name);
            else
                loginHandler.getWaitingScreenHandler().onLastPlayer(response.name);
        }
    }

    @Override
    public void handle(MatchStartedResponse response) {
        loginHandler.onMatchStartedSocket();
    }

    @Override
    public void handle(ActualPlayersResponse response) {
        if (socketCli != null) {
            socketCli.onPlayers(response.playersNames);
        }
    }

    @Override
    public void handle(YourTurnResponse response) {
        if (socketCli != null) {
            socketCli.onYourTurn(response.myTurn, response.string);
        }
    }

    @Override
    public void handle(ReserveResponse response) {
        if (socketCli != null) {
            socketCli.onReserve(response.reserve);
        }
    }

    @Override
    public void handle(MyWindowResponse response) {
        if (socketCli != null) {
            socketCli.onMyWindow(response.string);
        }
    }

    @Override
    public void handle(AfterWindowChoiseResponse response) {
        if (socketCli != null) {
            socketCli.onAfterWindowChoise();
        }
    }

    @Override
    public void handle(ToolCardsResponse response) {
        if (socketCli != null) {
          //  socketCli.onInitialization(response.string, , );
        }
    }

    @Override
    public void handle(OtherTurnResponse response) {
        if (socketCli != null) {
            socketCli.onOtherTurn(response.name);
        }
    }

    @Override
    public void handle(UpdateReserveResponse response) {
        if (socketCli != null) {
            socketCli.onReserve(response.string);
        }
    }

    @Override
    public void handle(DicePlacedResponse response) {
        if (socketCli != null) {
            dicePlaced = response.done;
        }
    }

    @Override
    public void handle(ToolCardEffectAppliedResponse response) {
        if (socketCli != null) {
            effectApplied = response.effectApplied;
        }
    }

    @Override
    public void handle(ClosingGameResponse response) {
        if (socketCli != null){
            socketCli.onGameClosing();
        }
    }

    @Override
    public void handle(PlayerExitResponse response) {
        if(socketCli != null){
            socketCli.onPlayerExit(response.name);
        }
    }

    @Override
    public void handle(PlayerReconnectionResponse response) {
        if(socketCli != null) {
            socketCli.onPlayerReconnection(response.getName());
        }
    }

    @Override
    public void handle(MyFavorTokensResponse response) {
        if(socketCli != null){
            socketCli.onMyFavorTokens(response.value);
        }
    }

    @Override
    public void handle(OtherFavorTokensResponse response) {
        if(socketCli != null){
            socketCli.onOtherFavorTokens(response.value,response.name);
        }
    }

    @Override
    public void handle(OtherSchemeCardsResponse response) {
        if(socketCli != null){
            socketCli.onOtherSchemeCards(response.scheme,response.name);
        }
    }


    @Override
    public void handle(GameEndResponse response) {
        if(socketCli != null) {
            socketCli.onGameEnd(response.getWinner(), response.getNames(), response.getValues());
        }
    }

    @Override
    public void handle(ProposeWindowResponse response) {
        if (socketCli != null) {
            socketCli.onWindowChoise(response.list);
        }
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

}
