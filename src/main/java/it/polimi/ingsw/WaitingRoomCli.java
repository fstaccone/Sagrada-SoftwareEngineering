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
        printer.println("BENVENUTO NELLA SALA DI ATTESA DI SAGRADA!                Per uscire dalla sala di attesa puoi interrompere il processo in qualsiasi momento!\nDi seguito troverai la lista aggiornata dei giocatori che entrano o escono dalla sala di attesa.\n");
    }

    @Override
    public void onLastPlayer(String name) {
        printer.println(name + " è uscito dalla sala di attesa, sei l'unico rimasto. Il timer è stato cancellato!\n");
        printer.flush();
    }

    @Override
    public void onPlayerExit(String name) {
        printer.println("Il giocatore \" + name + \" è uscito dalla sala di attesa prima dell'inizio della partita!\n");
        printer.flush();
    }

    @Override
    public void onWaitingPlayers(List<String> waitingPlayers) {
        String wPlayers = waitingPlayers.toString().toUpperCase().replaceAll("\\[", "");
        wPlayers = wPlayers.replaceAll("\\]", "");
        printer.println("Lista ordinata dei giocatori nella sala di attesa (tu incluso):\n" + wPlayers);
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
