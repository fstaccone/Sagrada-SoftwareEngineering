package it.polimi.ingsw.control;

import it.polimi.ingsw.model.gameobjects.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.view.RemoteBaseView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Controller extends UnicastRemoteObject implements RemoteController {

    private transient final Match match;
    private final Map<Player, RemoteBaseView> views = new HashMap<>();

    public Controller() throws RemoteException{
        super();
        match= Match.get();
    }

    @Override
    public synchronized String login(String playername, RemoteBaseView view) throws RemoteException {
        Player player=match.login(playername);

        views.put(player, view);
        view.ack("Logged in as @" + player.getName());
        return player.getName();//ritorna playername alla view
    }


    //dopo il login il controller ha il riferimento a ciascun player quindi può chiamarne i metodi
}
