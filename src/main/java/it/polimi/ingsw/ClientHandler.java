package it.polimi.ingsw;

import it.polimi.ingsw.control.Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final Controller controller;


    public ClientHandler(Socket s, Controller controller) throws IOException {

        this.socket = s;
        this.out = new ObjectOutputStream(s.getOutputStream());
        this.in = new ObjectInputStream(s.getInputStream());
        this.controller=controller;
    }

    @Override
    public void run() {
        System.out.println("arrivo qui");

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
