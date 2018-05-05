package it.polimi.ingsw.control;

import it.polimi.ingsw.model.gameobjects.Lobby;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.Room;
import it.polimi.ingsw.view.RemoteBaseView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Controller extends UnicastRemoteObject implements RemoteController  {

    private transient Lobby lobby;

    public Controller() throws RemoteException{
        super();
        // ...
    }

    @Override
    public String login(String username, RemoteBaseView view) throws RemoteException {
        return null;
    }

    public boolean checkName(String name){
        for ( String n: lobby.getTakenUsernames()) {
            if (name.equals(n))
                return false;
        }
        return true;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    // private transient final Room room;
   // private final Map<Player, RemoteBaseView> views = new HashMap<>();

    //public Controller() throws RemoteException{
    //    super();
      //  room= Room.get();
    //}

    //@Override
    //public synchronized String login(String playername, RemoteBaseView view) throws RemoteException {
        //Player player=room.login(playername);

        //views.put(player, view);
        //view.ack("Logged in as @" + player.getName());
        //return player.getName();//ritorna playername alla view
    //}


    //dopo il login il controller ha il riferimento a ciascun player quindi può chiamarne i metodi
}
