package it.polimi.ingsw.socket;

import it.polimi.ingsw.control.Controller;
import it.polimi.ingsw.socket.requests.Request;
import it.polimi.ingsw.socket.responses.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketHandler implements Runnable {
    private Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final Controller controller;

    /**
     * Constructor for SocketHandler.
     * @param socket is the used socket to guarantee connection.
     * @param controller is a Controller.
     */
    public SocketHandler(Socket socket, Controller controller) throws IOException {

        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());

        this.controller = controller;
        controller.addSocketHandler(this);

    }

    @Override
    public void run() {
        try {
            do {
                Response response = ((Request) in.readObject()).handleRequest(controller);
                if (response != null) {
                    respond(response);
                }
            } while (socket != null);
        } catch (Exception e) {
            System.out.println("Connection down");
        }
    }

    /**
     * Sends a response.
     *
     * @param response is the response to send.
     */
    public void respond(Response response) {
        try {
            out.writeObject(response);
        } catch (IOException e) {
            System.out.println("Cannot write the response");
        }
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

}
