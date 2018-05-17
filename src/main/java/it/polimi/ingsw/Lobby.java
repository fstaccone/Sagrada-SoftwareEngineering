package it.polimi.ingsw;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.MatchSingleplayer;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.*;

public class Lobby {

    private List<LobbyObserver> remoteObservers;
    private List<LobbyObserver> socketObservers;
    private List<String> takenUsernames;
    private int matchCounter;
    private final List<String> waitingPlayers;
    private int waitingTime;
    private int turnTime;

    // map that contains the link between a client(only multiplayer clients) and the matchId useful for reconnection
    private Map<String, Integer> mapClientsToRoom;

    private List<MatchSingleplayer> singleplayerMatches;
    private List<MatchMultiplayer> multiplayerMatches;

    // to simulate the timer before creating a match
    private Timer timer;
    private MatchStarter task;
    private List<ObjectOutputStream> SocketsOut;

    public Lobby(int waitingTime, int turnTime) {
        this.socketObservers=new LinkedList<>();
        this.remoteObservers=new LinkedList<>();
        this.takenUsernames = new ArrayList<>();
        this.matchCounter = 0;
        this.waitingPlayers = new ArrayList<>();
        this.mapClientsToRoom = new HashMap<>();
        this.multiplayerMatches = new ArrayList<>();
        this.singleplayerMatches = new ArrayList<>();
        this.waitingTime = waitingTime;
        this.turnTime = turnTime;
        this.SocketsOut=new ArrayList<>();
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
        singleplayerMatches.add(new MatchSingleplayer(matchCounter, name));
        matchCounter++;

        // debug
        System.out.println("By lobby: Match number: " + matchCounter + " type: singleplayer");
        System.out.println("By lobby: Player: " + name);
    }

    private synchronized void createMultiplayerMatch(List<String> clients) {
        multiplayerMatches.add(new MatchMultiplayer(matchCounter, clients, turnTime));
        System.out.println("By lobby: Match number: " + matchCounter + " type: multiplayer");
        System.out.println("By lobby: Players: ");
        clients.forEach(c -> System.out.print(c + "\t"));
        System.out.println("\n");
        // da rivedere la chiamata
        //multiplayerMatches.get(multiplayerMatches.size()-1).waitForViews();
    }

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
                    for(MatchSingleplayer m : singleplayerMatches){
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
                            for (MatchMultiplayer m : multiplayerMatches) {
                                if (m.getMatchId() == mapClientsToRoom.get(name)) {
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
        }
    }

    public boolean isLogged(String name){ return takenUsernames.contains(name); }
    public boolean isWaiting(String name){ return waitingPlayers.contains(name); }

    public void addToWaitingPlayers(String name) {
        synchronized (waitingPlayers) {

            waitingPlayers.add(name);
            System.out.println("Lobby: lobby rmi observers size: "+remoteObservers.size()+"\n");
            System.out.println("Lobby: waitingplayers size: "+waitingPlayers.size()+"\n");
            System.out.println("Lobby: lobby socket observers size: "+socketObservers.size()+"\n");

            //notifico ai remoteObservers i waitingplayers ogni volta che uno waiting player è aggiunto
            for (LobbyObserver observer : remoteObservers) {
                try {
                    observer.onWaitingPlayers(waitingPlayers); // todo: eliminare observers quando viene eliminato il giocatore
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            //notifico ai socketObservers i waitingplayers ogni volta che uno waiting player è aggiunto
            //NON SO COME NOTIFICARE GLI OBSERVER ATTRAVERSO LE RESPONSES, ma dubbio: sono effettivamente delle responses?
           /* for (LobbyObserver observer:socketObservers){
                new WaitingPlayersResponse(waitingPlayers);
            }*/
            WaitingPlayersResponse response= new WaitingPlayersResponse(waitingPlayers);
            for (ObjectOutputStream out: SocketsOut){
                try {
                    out.writeObject(response);
                    out.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



            //debug
            if (waitingPlayers.size()==1) System.out.println("There is 1 player waiting for a match.");
            else System.out.println("There are " + waitingPlayers.size() + " players waiting for a match.");


            // if there are two players waiting for the match beginning, the timer is set
            if (waitingPlayers.size() == 2) {
                System.out.println("Timer started: 30 seconds from now!");
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
            // links between client and match are registered into the map
            for (String name : waitingPlayers) {
                mapClientsToRoom.put(name, matchCounter);
            }
            createMultiplayerMatch(waitingPlayers);
            matchCounter++;
            waitingPlayers.clear();
        }
    }

    public List<String> getWaitingPlayers() {
        return waitingPlayers;
    }

    public List<MatchMultiplayer> getMultiplayerMatches() {
        return multiplayerMatches;
    }

    public void observeLobbyRemote(LobbyObserver lobbyObserver){
        remoteObservers.add(lobbyObserver);
    }
    public void observeLobbySocket(LobbyObserver lobbyObserver){
        socketObservers.add(lobbyObserver);
    }

    public void observeMatchRemote(String username, MatchObserver observer){
        for (MatchMultiplayer match:multiplayerMatches) {
            if (match.getMatchId()== mapClientsToRoom.get(username)){
                match.observeMatchRemote(observer, username);
                break;
            }
        }
    }

    public void observeMatchSocket(String username, MatchObserver observer){
        for (MatchMultiplayer match:multiplayerMatches) {
            if (match.getMatchId()== mapClientsToRoom.get(username)) {
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