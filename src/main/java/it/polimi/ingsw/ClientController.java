package it.polimi.ingsw;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientController implements ResponseHandler {


    private boolean nameAlreadyTaken=false;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private LoginHandler loginHandler;
    private SocketCli socketCli;

    public ClientController(ObjectInputStream in, ObjectOutputStream out, LoginHandler loginHandler) {
        this.in = in;
        this.out = out;
        this.loginHandler=loginHandler;
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

    public boolean isNameAlreadyTaken() {
        return nameAlreadyTaken;
    }

    public void setSocketCli(SocketCli socketCli) {
        this.socketCli = socketCli;
    }

    @Override
    public void handle(NameAlreadyTakenResponse response) {
        this.nameAlreadyTaken=response.nameAlreadyTaken;
    }

    @Override
    public void handle(WaitingPlayersResponse response){
        loginHandler.getWaitingScreenHandler().onWaitingPlayers(response.waitingPlayers);
        if (response.name!=null){
            if(response.unique==false)
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
        if (socketCli!=null){
            socketCli.onPlayers(response.playersNames);
        }
    }

    @Override
    public void handle(YourTurnResponse response) {
        if (socketCli!=null){
            socketCli.onYourTurn(response.myTurn, response.string);
        }
    }

    @Override
    public void handle(ReserveResponse response) {
        if (socketCli!=null){
            socketCli.onReserve(response.reserve);
        }
    }

    @Override
    public void handle(ShowWindowResponse response) {
        if (socketCli!=null){
            socketCli.onShowWindow(response.string);
        }
    }

    @Override
    public void handle(ToolCardsResponse response) {
        if (socketCli!=null){
            socketCli.onToolCards(response.string);
        }
    }

    @Override
    public void handle(OtherTurnResponse response) {
        if (socketCli!=null){
            socketCli.onOtherTurn(response.name);
        }
    }

    @Override
    public void handle(ProposeWindowResponse response) {
        if (socketCli!=null){
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
