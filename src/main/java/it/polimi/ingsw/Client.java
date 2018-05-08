package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.view.ClientControllerRMI;
import it.polimi.ingsw.view.ClientControllerInterface;
import it.polimi.ingsw.view.ViewInterface;

import java.rmi.RemoteException;


public class Client{
    private String name;
    private Player player;
    private RemoteController gameController;
    private ClientControllerInterface clientController;
    private ConnectionStatus state;
    private ViewInterface view;

    public Client(String name, ViewInterface view, ConnectionStatus state, RemoteController gameController) throws RemoteException {
        this.name = name;
        this.view = view;
        this.state = state;
        this.clientController = new ClientControllerRMI();
        this.gameController = gameController;
    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    public ConnectionStatus getState() {
        return state;
    }

    public ViewInterface getView() {
        return view;
    }

    // the Player attribute will be set in the Model when tha match will be started todo: è in inglese?
    public void setPlayer(Player player) {
        this.player = player;
    }



}
