package it.polimi.ingsw.control;

import it.polimi.ingsw.*;
import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.ToolCard;
import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.SocketHandler;
import it.polimi.ingsw.socket.requests.*;
import it.polimi.ingsw.socket.responses.DicePlacedResponse;
import it.polimi.ingsw.socket.responses.NameAlreadyTakenResponse;
import it.polimi.ingsw.socket.responses.Response;
import it.polimi.ingsw.socket.responses.ToolCardEffectAppliedResponse;

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
/*
    @Override
    public String login(String username, ViewInterface view){
        return null;
    }*/


    // da gestire il caso di riconnessione
    @Override
    public ConnectionStatus checkName(String name) {
        return lobby.checkName(name);
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
            lobby.getSingleplayerMatches().get(name).goThrough();
        }else {
            lobby.getMultiplayerMatches().get(name).goThrough();
        }
    }

    @Override
    public boolean placeDice(int index, int x, int y, String name, boolean isSingle)  {
        if (isSingle) {
            lobby.getSingleplayerMatches().get(name).setDiceAction(true);
            // todo: gestire la chiamata all'interno del match singleplayer con TurnManagerSingleplayer
        }else {
            return lobby.getMultiplayerMatches().get(name).placeDice(name, index, x, y);
        }
        return false;
    }

    @Override
    public void showPlayers(String name){
        lobby.getMultiplayerMatches().get(name).showPlayers(name);
    }

    @Override
    public void showWindow(String name, String owner){
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
    public void quitGame(String name, boolean isSingle) {
        if(isSingle){
            lobby.getSingleplayerMatches().get(name).terminateMatch();
        }else{
            lobby.disconnect(name);
        }
    }

    @Override
    public void reconnect(String name) throws RemoteException, InterruptedException {
        lobby.reconnect(name);
    }

    @Override
    public boolean useToolCard1(int diceChosen, String IncrOrDecr, String name, boolean isSingle) {
        if(isSingle){
            //DA FARE
        }else{
            return lobby.getMultiplayerMatches().get(name).useToolCard1(diceChosen, IncrOrDecr, name, isSingle);
        }
        return false;
    }

    @Override
    public boolean useToolCard2or3(int n,int startX, int startY, int finalX, int finalY, String name, boolean isSingle) {
        if(isSingle){
            //DA FARE
        }else{
            return lobby.getMultiplayerMatches().get(name).useToolCard2or3(n,startX, startY, finalX, finalY, name, isSingle);
        }
        return false;
    }


    @Override
    public Response handle(CheckUsernameRequest request) {
        return new NameAlreadyTakenResponse(checkName(request.username));
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

    @Override
    public Response handle(PlaceDiceRequest request) {
        boolean placed= placeDice(request.diceChosen,request.coordinateX,request.coordinateY,request.username,request.single);
        return new DicePlacedResponse(placed);
    }

    @Override
    public Response handle(ShowWindowRequest request) {
         showWindow(request.myName,request.ownerName);
         return null;
    }

    @Override
    public Response handle(UseToolCard1Request request) {
        boolean effectApplied=useToolCard1(request.diceChosen,request.IncrOrDecr,request.username,request.isSingle);
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard2or3Request request) {
        boolean effectApplied=useToolCard2or3(request.n,request.startX,request.startY,request.finalX,request.finalY,request.username,request.isSingle);
        return new ToolCardEffectAppliedResponse(effectApplied);
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

}
