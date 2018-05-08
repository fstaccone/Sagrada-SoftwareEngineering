package it.polimi.ingsw;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientController implements ResponseHandler {
    public boolean isMatchCreated() {
        return matchCreated;
    }

    private boolean matchCreated=false;
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

    @Override
    public void handle(NameAlreadyTakenResponse response) {
        this.nameAlreadyTaken=response.nameAlreadyTaken;
    }

    @Override
    public void handle(CreatedMatchResponse response) {
        this.matchCreated=response.matchCreated;
    }
}
