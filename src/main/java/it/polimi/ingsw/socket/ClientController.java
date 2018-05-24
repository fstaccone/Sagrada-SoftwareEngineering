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


    private ConnectionStatus nameSatus = ConnectionStatus.ABSENT;
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
        return nameSatus;
    }

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
        this.nameSatus = response.status;
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
    public void handle(ShowWindowResponse response) {
        if (socketCli != null) {
            socketCli.onShowWindow(response.string);
        }
    }

    @Override
    public void handle(ToolCardsResponse response) {
        if (socketCli != null) {
            socketCli.onToolCards(response.string);
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
