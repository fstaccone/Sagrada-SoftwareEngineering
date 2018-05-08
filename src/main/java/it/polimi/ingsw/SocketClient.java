package it.polimi.ingsw;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient implements ResponseHandler{

    private Socket socket;

    public void setSocket(Socket socket) {
        this.socket=socket;
    }

    private ObjectInputStream in;
    private ObjectOutputStream out;



    public void request(Request request) {
        try {
            out.writeObject(request);
        } catch (IOException e) {
            System.err.println("Exception on network: " + e.getMessage());
        }
    }


    @Override
    public void handle(NameAlreadyTakenResponse response) { //DA TOGLIERE
       // nameTaken=response.nameAlreadyTaken;
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
}
