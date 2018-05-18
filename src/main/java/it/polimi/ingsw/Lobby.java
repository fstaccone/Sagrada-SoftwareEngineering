package it.polimi.ingsw;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.MatchSingleplayer;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.*;

public class Lobby {

    private int matchCounter;
    private int waitingTime;
    private int turnTime;

    // to ensure that a username is unique
    private List<String> takenUsernames;
    // waiting list before the  beginning of a new multiplayer match
    private final List<String> waitingPlayers;

    // to store relation between client and match
    private Map<String, MatchSingleplayer> singleplayerMatches;
    private Map<String, MatchMultiplayer> multiplayerMatches;

    // to store observers
    private Map<String, LobbyObserver> remoteObservers;
    private List<LobbyObserver> socketObservers;
    private List<ObjectOutputStream> SocketsOut;// uno dei due va eliminato

    // to simulate the timer before creating a match
    private Timer timer;
    private MatchStarter task;


    public Lobby(int waitingTime, int turnTime) {

        this.matchCounter = 0;

        this.takenUsernames = new ArrayList<>();

        this.waitingPlayers = new ArrayList<>();

        this.multiplayerMatches = new HashMap<>();
        this.singleplayerMatches = new HashMap<>();

        this.remoteObservers = new HashMap<>();
        this.socketObservers = new LinkedList<>();
        this.SocketsOut = new ArrayList<>(); // uno dei due va eliminato

        this.waitingTime = waitingTime;
        this.turnTime = turnTime;
    }

    public List<String> getTakenUsernames() {
        return new ArrayList<>(takenUsernames);
    }

    // to add a new username to the list
    public synchronized void addUsername(String name) {
        this.takenUsernames.add(name);
    }

    // to remove usernames at the end of a match or when a player leave a match before its creation
    public synchronized void removeUsername(String name) {
        this.takenUsernames.remove(name);
    }

    public synchronized void createSingleplayerMatch(String name) {

        singleplayerMatches.put(name, new MatchSingleplayer(matchCounter, name));
        matchCounter++;

        // debug
        System.out.println("By lobby: Match number: " + matchCounter + " type: singleplayer");
        System.out.println("By lobby: Player: " + name);
    }

    private synchronized void createMultiplayerMatch(List<String> clients) {

        MatchMultiplayer match = new MatchMultiplayer(matchCounter, clients, turnTime);
        for(String s : clients) {
            multiplayerMatches.put(s, match);
        }

        System.out.println("By lobby: Match number: " + matchCounter + " type: multiplayer");
        System.out.println("By lobby: Players: ");

        clients.forEach(c -> System.out.print(c + "\t"));
        System.out.println("\n");
        // da rivedere la chiamata
        //multiplayerMatches.get(multiplayerMatches.size()-1).waitForViews();
    }


    // todo: metodo da semplificare, anche splittandolo se serve
    public void removeFromWaitingPlayers(String name) {
        synchronized (waitingPlayers) {
            if(isLogged(name)) {
                // copre il caso di giocatore che aspetta di entrare in partita multiplayer
                if(isWaiting(name)) {
                    // Ha senso il try-catch?
                    try {
                        if (waitingPlayers.size() == 2) {
                            timer.cancel();
                            waitingPlayers.remove(name);
                            remoteObservers.remove(name); // elimina observer rmi

                            removeUsername(name); // todo: ha senso questo metodo?
                            System.out.println("Player " + name + " has left the room!");
                            // todo: convertire in invio agli observer
                            System.out.println("Timer has been canceled, only one waiting player left!");
                        } else {
                            waitingPlayers.remove(name);
                            // debug
                            System.out.println("Player " + name + " has left the room!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("From lobby: Something wants to delete a name that doesn't exist!");
                    }
                }
                // copre i casi singleplayer e partita multiplayer iniziata
                else{
                    boolean wasSingle = false;

                    // se single player termina la partita
                    for(MatchSingleplayer m : singleplayerMatches.values()){
                        if(m.getPlayer().getName().equals(name)){
                            m.terminateMatch();
                            removeUsername(name); // todo: ha senso questo metodo?
                            wasSingle = true;
                            break;
                        }
                    }
                    // altrimenti disconnette il player, nella mappa ci sarà il riferimento
                    if(!wasSingle){
                        try {
                            for (MatchMultiplayer m : multiplayerMatches.values()) {
                                if (m == multiplayerMatches.get(name)) {
                                    for (PlayerMultiplayer p : m.getPlayers()) {
                                        if (p.getName().equals(name)) {
                                            p.setStatus(ConnectionStatus.DISCONNECTED);
                                            // check per capire se il match va chiuso
                                            if(m.checkConnection() < 2 ) {
                                                m.terminateMatch();
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            // debug
                            System.out.println("From lobby: il match cercato non esiste.");
                        }
                    }
                }
            }else {
                System.out.println("From lobby: the name has not been registered as taken yet");
            }
            for (LobbyObserver observer : remoteObservers.values()) {
                try {
                    observer.onWaitingPlayers(waitingPlayers);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private boolean isLogged(String name){ return takenUsernames.contains(name); }
    private boolean isWaiting(String name){ return waitingPlayers.contains(name); }

    public void addToWaitingPlayers(String name) {
        synchronized (waitingPlayers) {

            waitingPlayers.add(name);
            System.out.println("Lobby: waitingplayers size: "+waitingPlayers.size()+"");
            System.out.println("Lobby: lobby rmi observers size: "+remoteObservers.size()+"");
            System.out.println("Lobby: lobby socket observers size: "+socketObservers.size()+"");

            // ANY TIME A WAITING PLAYER IS ADDED, THE NOTIFICATION IS SENT TO THE WAITINGSCREENHANDLER BOTH FOR RMI AND SOCKETS
            //RMI
            for (LobbyObserver observer : remoteObservers.values()) {
                try {
                    observer.onWaitingPlayers(waitingPlayers);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            //SOCKET
            WaitingPlayersResponse response= new WaitingPlayersResponse(waitingPlayers);
            for (ObjectOutputStream out: SocketsOut){
                try {
                    out.writeObject(response);
                    out.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //DEBUG SERVER SIDE
            if (waitingPlayers.size()==1) System.out.println("Lobby: There is 1 player waiting for a match."+"\n");
            else System.out.println("Lobby: There are " + waitingPlayers.size() + " players waiting for a match."+"\n");


            // IF THERE ARE 2 PLAYERS WAITING FOR THE MATCH BEGINNING, THE TIMER IS SET
            if (waitingPlayers.size() == 2) {
                System.out.println("Lobby :Timer started: 30 seconds from now!");
                this.timer = new Timer();
                task = new MatchStarter(this);
                timer.schedule(task, waitingTime);
            }

            if (waitingPlayers.size() == 4) {
                timer.cancel();
                startMatch();
            }
        }
    }

    public void startMatch() {

        synchronized (waitingPlayers) {

            createMultiplayerMatch(waitingPlayers);
            //NOTIFIES TO ALL THE LOBBY OBSERVERS THE CREATION OF THE MATCH, SO FROM THEN THE CLIENTS CAN START THE GUI/CLI AND OBSERVE THE MATCH

            //RMI
            for (LobbyObserver observer : remoteObservers.values()) {
                try {
                    observer.onMatchStarted();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            //SOCKET



            matchCounter++;
            remoteObservers.clear();
            waitingPlayers.clear();
        }
    }

    public List<String> getWaitingPlayers() {
        return waitingPlayers;
    }

    public void observeLobbyRemote(String name, LobbyObserver lobbyObserver){
        remoteObservers.put(name, lobbyObserver);
    }
    public void observeLobbySocket(LobbyObserver lobbyObserver){
        socketObservers.add(lobbyObserver);
    }

    public void observeMatchRemote(String username, MatchObserver observer){
        for (MatchMultiplayer match : multiplayerMatches.values()) {
            if (match == multiplayerMatches.get(username)){
                match.observeMatchRemote(observer, username);
                break;
            }
        }
    }

    public void observeMatchSocket(String username, MatchObserver observer){
        for (MatchMultiplayer match : multiplayerMatches.values()) {
            if (match == multiplayerMatches.get(username)) {
                match.observeMatchSocket(observer, username);
                break;
            }
        }
    }


    public void addSocketOut(ObjectOutputStream out) {
        this.SocketsOut.add(out);
    }
    public void removeObserver(String name) {
        boolean isRemote = false;
        // todo: da completare
    }
}