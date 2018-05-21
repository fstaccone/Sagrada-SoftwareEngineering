package it.polimi.ingsw.control;

import it.polimi.ingsw.*;
import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.view.ViewInterface;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Controller extends UnicastRemoteObject implements RemoteController, RequestHandler {

    private Lobby lobby;
    private List<SocketHandler> socketHandlers; //Intermediate variable, useful between View and Model


    public Controller(Lobby lobby) throws RemoteException {
        super();
        this.lobby = lobby;
        this.socketHandlers=new ArrayList<>();
        // ...
    }

    @Override
    public String login(String username, ViewInterface view){
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
    public void removePlayer(String name) {
        lobby.getRemoteObservers().remove(name);
        lobby.removeFromWaitingPlayers(name);
    }

    @Override
    public void goThrough(String name, boolean isSingle){
        if(isSingle){
            lobby.getSingleplayerMatches().get(name).goThrough(name);
        }else {
            lobby.getMultiplayerMatches().get(name).goThrough(name);
        }
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
        lobby.getSocketObservers().put(request.username,socketHandlers.remove(0).getOut());
        addPlayer(request.username);
        return null;
    }

    @Override
    public Response handle(RemoveFromWaitingPlayersRequest request) {
        lobby.getSocketObservers().remove(request.name);
        lobby.removeFromWaitingPlayers(request.name);
        return null;
    }

    @Override
    public Response handle(GoThroughRequest request) {
        goThrough(request.username,request.singlePlayer);
        return null;
    }

    @Override
    public Response handle(ChooseWindowRequest request) {
        chooseWindow(request.username,request.value,request.single);
        return null;
    }


    public void observeLobby(String name, LobbyObserver lobbyObserver) {
        lobby.observeLobbyRemote(name, lobbyObserver);
    }

    public void observeMatch(String username, MatchObserver observer) {
        lobby.observeMatchRemote(username, observer);
    }

    public void addSocketHandler(SocketHandler socketHandler) {
        this.socketHandlers.add(socketHandler);
    }


    @Override
    public boolean placeDice(int index, int x, int y, String name, boolean isSingle) {
        if (isSingle) {
            lobby.getMultiplayerMatches().get(name).setDiceAction(true);
            // todo: gestire la chiamata all'interno del match singleplayer con TurnManagerSingleplayer
        }else {
            System.out.println("PppppppppppPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
            return lobby.getMultiplayerMatches().get(name).placeDice(name, index, x, y);
        }
        return false;
    }

    // non sono convinto sia corretto ma credo che non abbia senso fare altri giri per notificare i nomi dei giocatori
    // (il nome serve per chiedere di visualizzare la carta schema degli avversari)
    @Override
    public void showPlayers(String name) throws RemoteException {
        lobby.getMultiplayerMatches().get(name).showPlayers(name);
    }

    // ha senso chiedere di ristampare la propria carta schema?
    // Se si allora occorre gestire il caso del single player (si potrebbe scrivere un altro metodo)
    @Override
    public void showWindow(String name, String owner) throws RemoteException {
        lobby.getMultiplayerMatches().get(name).showWindow(name, owner);
    }


    @Override
    public void chooseWindow(String name, int index, boolean isSingle) {
        if(isSingle){
            lobby.getSingleplayerMatches().get(name).setWindowPatternCard(name, index);
        }else{
            lobby.getMultiplayerMatches().get(name).setWindowPatternCard(name, index);
        }
    }
}
