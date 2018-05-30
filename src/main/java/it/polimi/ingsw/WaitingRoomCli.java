package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.socket.ClientController;
import it.polimi.ingsw.socket.requests.RemoveFromWaitingPlayersRequest;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class WaitingRoomCli extends UnicastRemoteObject implements LobbyObserver {
    private final String  username;//USEFUL ONLY IF WE DECIDE TO LET A PLAYER PRESS 'q' COMMAND WHILE IN WAITING ROOM
    private PrintWriter printer;
    private LoginHandler loginHandler;
    private Stage window;
    private boolean rmi;
    private RemoteController controller;//USEFUL ONLY IF WE DECIDE TO LET A PLAYER PRESS 'q' COMMAND WHILE IN WAITING ROOM
    private ClientController clientController;//USEFUL ONLY IF WE DECIDE TO LET A PLAYER PRESS 'q' COMMAND WHILE IN WAITING ROOM



    public WaitingRoomCli(LoginHandler loginHandler, Stage window, String username, boolean rmi) throws RemoteException {
        super();
        this.window = window;
        this.username=username;
        this.rmi=rmi;
        this.loginHandler = loginHandler;
        this.printer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(FileDescriptor.out)));
        printer.println("WELCOME TO THE WAITING ROOM OF SAGRADA!                You can interrupt the process in any moment to quit the room!\nHere follows the updated list of players who join/leave the room\n");
    }

    @Override
    public void onLastPlayer(String name) {
        printer.println("\n"+name.toUpperCase() + " has left the room, you are the only one remaining. Timer has been canceled!");
        printer.flush();
    }

    @Override
    public void onPlayerExit(String name) {
        printer.println("\nPlayer " + name.toUpperCase() + " has left the room before the starting of the Match!");
        printer.flush();
    }

    @Override
    public void onWaitingPlayers(List<String> waitingPlayers) {
        String wPlayers = waitingPlayers.toString().toUpperCase().replaceAll("\\[", "");
        wPlayers = wPlayers.replaceAll("\\]", "");
        printer.println("Ordered list of waiting players (including you):\n" + wPlayers);
        printer.flush();
        Platform.runLater(() -> window.close());

    }

    @Override
    public void onMatchStarted() {
        loginHandler.onMatchStartedRmi();
    }

    public void onCheckConnection() {
        //just to check connection before the starting of the match in case other notifies didn't happen
    }

    public void setController(RemoteController controller) {
        this.controller = controller;
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }
}
