package it.polimi.ingsw;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient implements ResponseHandler{

    private boolean nameTaken;
    private final String host;
    private final int port;
    private Socket connection;

    public Socket getConnection() {
        return connection;
    }

    private ObjectInputStream in;
    private ObjectOutputStream out;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void init() throws IOException {
        connection = new Socket(host, port);
        out = new ObjectOutputStream(connection.getOutputStream());
        in = new ObjectInputStream(connection.getInputStream());
    }

    public void request(Request request) {
        try {
            out.writeObject(request);
        } catch (IOException e) {
            System.err.println("Exception on network: " + e.getMessage());
        }
    }

    public boolean isNameTaken() {
        return nameTaken;
    }

    @Override
    public void handle(NameAlreadyTakenResponse response) {
        nameTaken=response.nameAlreadyTaken;

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
