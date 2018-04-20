package it.polimi.ingsw.view;

import it.polimi.ingsw.control.RemoteController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class TextView extends UnicastRemoteObject implements RemoteBaseView {

    private final Scanner in;
    private final RemoteController controller;
    private String receivedUsername;

    public TextView(RemoteController controller) throws RemoteException {
        this.controller = controller;
        this.in = new Scanner(System.in);
    }


    public void run() throws RemoteException {
        System.out.println("\t\tWelcome to Sagrada");

        String username;
        do{
            System.out.println(">>> Please provide username:");
            username = in.nextLine();
            if (!username.isEmpty()) {
                try {
                    receivedUsername = controller.login(username, this);
                }
                catch(RemoteException e) {
                    System.err.println("Playername already in use, choose a different one");
                    username = "";
                }
            }
        } while (username.isEmpty());
    }

    @Override
    public void ack (String message) throws RemoteException {
        System.out.println(">>> " + message);
    }
}
