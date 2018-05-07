package it.polimi.ingsw;

import java.net.Socket;

public class ClientHandler implements Runnable{
    Socket s;

    public ClientHandler(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {

    }


}
