package it.polimi.ingsw.control;

import it.polimi.ingsw.*;
import it.polimi.ingsw.view.ViewInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Controller extends UnicastRemoteObject implements RemoteController,RequestHandler {

    private Lobby lobby;

    public Controller(int time) throws RemoteException {
        super();
        this.lobby = new Lobby(time);
        // ...
    }

    @Override
    public String login(String username, ViewInterface view) throws RemoteException {
        return null;
    }

    @Override
    public boolean checkName(String name){
        for ( String n: lobby.getTakenUsernames()) {
            if (name.equals(n)) {
                System.out.println("checkname ha trovato uno username duplicato");
                return false;
            }
        }
        return true;
    }


    @Override
    public void createMatch(String name) throws RemoteException{
        this.lobby.addUsername(name);
        this.lobby.createSingleplayerMatch(name);
    }

    @Override
    public void addPlayer(String name) throws RemoteException {
        this.lobby.addUsername(name);
        this.lobby.addToWaitingPlayers(name);
    }

    @Override
    public Response handle(CheckUsernameRequest request) {
        for ( String n: lobby.getTakenUsernames()) {
            if (request.username.equals(n)) {
                System.out.println("checkname ha trovato uno username duplicato");
                return new NameAlreadyTakenResponse(true);
            }
        }
        return new NameAlreadyTakenResponse(false);
    }

    // private transient final Room room;
   // private final Map<Player, ViewInterface> views = new HashMap<>();

    //public Controller() throws RemoteException{
    //    super();
      //  room= Room.get();
    //}

    //@Override
    //public synchronized String login(String playername, ViewInterface view) throws RemoteException {
        //Player player=room.login(playername);

        //views.put(player, view);
        //view.ack("Logged in as @" + player.getName());
        //return player.getName();//ritorna playername alla view
    //}


    //dopo il login il controller ha il riferimento a ciascun player quindi può chiamarne i metodi
}
