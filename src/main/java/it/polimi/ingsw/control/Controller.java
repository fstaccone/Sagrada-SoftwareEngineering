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


    // da gestire il caso di riconnessione
    @Override
    public boolean checkName(String name){

        for ( String n: lobby.getTakenUsernames()) {
            if (name.equals(n)) {
                System.out.println("checkName found a double name");
                return false;
            }
        }
        return true;
    }


    @Override
    public void createMatch(String name){
        this.lobby.addUsername(name);
        this.lobby.createSingleplayerMatch(name);
    }

    @Override
    public void addPlayer(String name){
        this.lobby.addUsername(name);
        this.lobby.addToWaitingPlayers(name);
    }

    @Override
    public Response handle(CheckUsernameRequest request) {

        return new NameAlreadyTakenResponse(!checkName(request.username));
    }

    @Override
    public Response handle(CreateMatchRequest request) {
        createMatch(request.username);
        return null;
    }

    @Override
    public Response handle(AddPlayerRequest request) {
        addPlayer(request.username);
        return null;
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
