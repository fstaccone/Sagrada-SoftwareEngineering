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
    private List<SocketHandler> socketHandlers;


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
    public Response handle(ObserveMatchRequest request) {
        lobby.observeMatchSocket(request.username, request.matchObserver);
        return null;
    }

    @Override
    public Response handle(RemoveFromWaitingPlayersRequest request) {
        lobby.getSocketObservers().remove(request.name);
        lobby.removeFromWaitingPlayers(request.name);
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
    public void goThrough(String name, boolean isSingle){
        if(isSingle){
            lobby.getSingleplayerMatches().get(name).setEndsTurn(true);
            // todo: gestire la chiamata all'interno del match singleplayer
        }else {
            lobby.getMultiplayerMatches().get(name).setEndsTurn(true);
            System.out.println("Da controller: metodo passa funziona!");
            synchronized (lobby.getMultiplayerMatches().get(name).getLock()) {
                lobby.getMultiplayerMatches().get(name).getLock().notify();
            }
        }
    }

    @Override
    public boolean placeDice(int index, int x, int y, String name, boolean isSingle) {
        if (isSingle) {
            lobby.getMultiplayerMatches().get(name).setDiceAction(true);
            // todo: gestire la chiamata all'interno del match singleplayer con TurnManager
        }else{
            if (lobby.getMultiplayerMatches().get(name).isDiceAction()) {

                for (PlayerMultiplayer p : lobby.getMultiplayerMatches().get(name).getPlayers()) {
                    if (p.getName().equals(name)) {
                        p.getSchemeCard().putDice(p.chooseDice(index), x, y);

                        lobby.getMultiplayerMatches().get(name).setDiceAction(true);

                        synchronized (lobby.getMultiplayerMatches().get(name).getLock()) {
                            lobby.getMultiplayerMatches().get(name).getLock().notify();
                        }

                        return true;
                    }
                }
            }else{
                return false;
            }
        }
        return false; // non si dovrebbe mai arrivare qui
    }

    // non sono convinto sia corretto ma credo che non abbia senso fare altri giri per notificare i nomi dei giocatori
    // (il nome serve per chiedere di visualizzare la carta schema degli avversari)
    @Override
    public void showPlayers(String name) throws RemoteException {

        List<String> playersNames = new ArrayList<>();
        MatchMultiplayer match;
        match = lobby.getMultiplayerMatches().get(name);

        match.getPlayers().forEach(p -> playersNames.add(p.getName()));
        for(PlayerMultiplayer p : match.getPlayers()){
            if(p.getName().equals(name)){
                match.getRemoteObservers().get(p).onPlayers(playersNames);
                break;
            }
        }

    }

    // ha senso chiedere di ristampare la propria carta schema? Se si allora occorre gestire il caso del single player (si potrebbe scrivere un altro metodo)
    @Override
    public void showWindow(String name) throws RemoteException {
        getObserver(name).onShowWindow(getPlayer(name).getSchemeCard().toString()); // dimostra l'utilità dei metodi sottostanti
    }


    /**
     * Ho creato questi metodi perchè la ricerca dell'obesrver e del player mi sembrano un costrutto ricorrente
     */
    private MatchObserver getObserver(String name){
        MatchMultiplayer match;
        match = lobby.getMultiplayerMatches().get(name);

        for(PlayerMultiplayer p : match.getPlayers()){
            if(p.getName().equals(name)) {
                return match.getRemoteObservers().get(p);
            }
        }
        return null;
    }

    private PlayerMultiplayer getPlayer(String name){
        MatchMultiplayer match;
        match = lobby.getMultiplayerMatches().get(name);

        for(PlayerMultiplayer p : match.getPlayers()){
            if(p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void chooseWindow(String name, int index) {

        getPlayer(name).setSchemeCard();
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
