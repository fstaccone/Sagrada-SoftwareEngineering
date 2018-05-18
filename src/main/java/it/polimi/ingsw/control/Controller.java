package it.polimi.ingsw.control;

import it.polimi.ingsw.*;
import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.view.ViewInterface;

import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Controller extends UnicastRemoteObject implements RemoteController, RequestHandler {

    private Lobby lobby;

    public Controller(Lobby lobby) throws RemoteException {
        super();
        this.lobby = lobby;
        // ...
    }

    @Override
    public String login(String username, ViewInterface view) throws RemoteException {
        return null;
    }


    // da gestire il caso di riconnessione
    @Override
    public boolean checkName(String name) {

        for (String n : lobby.getTakenUsernames()) {
            if (name.equals(n)) {
                System.out.println("checkName found a double name");
                return false;
            }
        }
        return true;
    }


    @Override
    public void createMatch(String name) {
        this.lobby.addUsername(name);
        this.lobby.createSingleplayerMatch(name);
    }

    @Override
    public void addPlayer(String name) {
        this.lobby.addUsername(name);

        // check if there is a still alive match in which the player must be put
        if (lobby.getMultiplayerMatches().get(name) != null) {
            for (PlayerMultiplayer p : lobby.getMultiplayerMatches().get(name).getPlayers()) {
                if (p.getName().equals(name)) {
                    p.setStatus(ConnectionStatus.READY);
                }
            }
        } else {
            this.lobby.addToWaitingPlayers(name);
        }
    }

    @Override
    public void removePlayer(String name) throws RemoteException {
        lobby.removeFromWaitingPlayers(name);
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

    @Override
    public Response handle(ObserveLobbyRequest request) {
        lobby.observeLobbySocket(request.lobbyObserver);
        return null;
    }

    @Override
    public Response handle(ObserveMatchRequest request) {
        lobby.observeMatchSocket(request.username, request.matchObserver);
        return null;
    }


    public void observeLobby(String name, LobbyObserver lobbyObserver) {
        lobby.observeLobbyRemote(name, lobbyObserver);
    }

    public void observeMatch(String username, MatchObserver observer) {
        lobby.observeMatchRemote(username, observer);
    }

    public void addSocketOut(ObjectOutputStream out) {
        lobby.addSocketOut(out);
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
