package it.polimi.ingsw;

import it.polimi.ingsw.control.Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketHandler implements Runnable{
    private Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final Controller controller;


    public SocketHandler(Socket socket, Controller controller) throws IOException {

        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.controller=controller;
    }

    @Override
    public void run() {
        try {
            do {
                Response response = ((Request) in.readObject()).handle(controller);
                if (response != null) {
                    respond(response);
                }
            } while (socket!=null);
        } catch (Exception e) {
            System.out.println("non prende la risposta");
        }

        //close();

    }

    public void respond(Response response) {
        try {
            out.writeObject(response);
        } catch (IOException e) {
           System.out.println("non scrive la risposta");
        }
    }

}
