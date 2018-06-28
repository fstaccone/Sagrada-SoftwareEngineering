package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.view.LobbyObserver;
import it.polimi.ingsw.view.LoginHandler;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class WaitingRoomCli extends UnicastRemoteObject implements LobbyObserver {
    private PrintWriter printer;
    private LoginHandler loginHandler;
    private Stage window;

    /**
     * Initializes the Cli waiting room
     * @param loginHandler     is the controller of the previous stage
     * @param window          is the stage representing the previous step
     */
    public WaitingRoomCli(LoginHandler loginHandler, Stage window) throws RemoteException {
        super();
        this.window = window;
        this.loginHandler = loginHandler;
        this.printer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(FileDescriptor.out)));
        printer.println("BENVENUTO NELLA SALA DI ATTESA DI SAGRADA!                Per uscire dalla sala di attesa puoi interrompere il processo in qualsiasi momento!\nDi seguito troverai la lista aggiornata dei giocatori che entrano o escono dalla sala di attesa.\n");
    }

    /**
     * Notify used to update this Cli's owner about every opponent's leaving
     *
     * @param name is the name of the last opponent who left the waiting room
     */
    @Override
    public void onLastPlayer(String name) {
        printer.println(name + " è uscito dalla sala di attesa, sei l'unico rimasto. Il timer è stato cancellato!\n");
        printer.flush();
    }

    /**
     * Notify used to update this Cli's owner about opponents' leaving
     *
     * @param name is the name of the opponent who left the waiting room
     */
    @Override
    public void onPlayerExit(String name) {
        printer.println("Il giocatore " + name + " è uscito dalla sala di attesa prima dell'inizio della partita!\n");
        printer.flush();
    }

    /**
     * Notify used to update this Cli's owner about his opponents
     *
     * @param waitingPlayers is the list of names of the opponent who are currently in the waiting room
     */
    @Override
    public void onWaitingPlayers(List<String> waitingPlayers) {
        String wPlayers = waitingPlayers.toString().replaceAll("\\[", "");
        wPlayers = wPlayers.replaceAll("\\]", "");
        printer.println("Lista ordinata dei giocatori nella sala di attesa (tu incluso):\n" + wPlayers);
        printer.flush();
        Platform.runLater(() -> window.close());

    }

    /**
     * Notify used to update this Cli's owner the match beginning
     */
    @Override
    public void onMatchStarted() {
        loginHandler.onMatchStartedRmi();
    }

    /**
     * Notify used to check this Cli connection before the starting of the match
     */
    @Override
    public void onCheckConnection() {
        //just to check connection before the starting of the match in case other notifies didn't happen
    }

}
