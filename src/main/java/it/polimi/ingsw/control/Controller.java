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

    private transient Lobby lobby;
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
        return !lobby.checkName(name);
    }



    @Override
    public void createMatch(String name) {
        this.lobby.addUsername(name);
        this.lobby.createSingleplayerMatch(name);
    }

    @Override
    public void addPlayer(String name) {
        this.lobby.addUsername(name);
        this.lobby.addPlayer(name);
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
    public boolean placeDice(int index, int x, int y, String name, boolean isSingle) throws RemoteException {
        if (isSingle) {
            lobby.getSingleplayerMatches().get(name).setDiceAction(true);
            // todo: gestire la chiamata all'interno del match singleplayer con TurnManagerSingleplayer
        }else {
            return lobby.getMultiplayerMatches().get(name).placeDice(name, index, x, y);
        }
        return false;
    }

    @Override
    public void showPlayers(String name) throws RemoteException {
        lobby.getMultiplayerMatches().get(name).showPlayers(name);
    }

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

    @Override
    public void showToolcards(String name, boolean isSingle) throws RemoteException {
        if(isSingle){
            lobby.getSingleplayerMatches().get(name).showToolCards();
        }else{
            lobby.getMultiplayerMatches().get(name).showToolCards(name);
        }
    }

    @Override
    public void quitGame(String name, boolean isSingle) throws RemoteException {
        if(isSingle){
            lobby.getSingleplayerMatches().get(name).terminateMatch();
        }else{
            lobby.disconnect(name);
        }
    }
}
