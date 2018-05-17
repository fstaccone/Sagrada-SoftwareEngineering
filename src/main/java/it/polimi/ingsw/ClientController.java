package it.polimi.ingsw;

import it.polimi.ingsw.control.Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class ClientController implements ResponseHandler {

    private List<String> waitingPlayers;
    private boolean nameAlreadyTaken=false;
    private ObjectInputStream in;
    private ObjectOutputStream out;


    public ClientController(ObjectInputStream in, ObjectOutputStream out) {
        this.in = in;
        this.out = out;
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

    public List<String> getWaitingPlayers() {
        return waitingPlayers;
    }

    @Override
    public void handle(NameAlreadyTakenResponse response) {
        this.nameAlreadyTaken=response.nameAlreadyTaken;
    }
    @Override
    public void handle(WaitingPlayersResponse response){
        this.waitingPlayers=response.waitingPlayers;
        System.out.println("lista di waiting players: "+waitingPlayers);
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

}
