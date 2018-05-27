package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.MatchObserver;
import it.polimi.ingsw.model.gameobjects.Board;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.DecksContainer;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.responses.ActualPlayersResponse;
import it.polimi.ingsw.socket.responses.AfterWindowChoiseResponse;
import it.polimi.ingsw.socket.responses.ShowWindowResponse;
import it.polimi.ingsw.socket.responses.UpdateReserveResponse;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class MatchMultiplayer extends Match implements Runnable {

    private Map<PlayerMultiplayer, MatchObserver> remoteObservers;
    private Map<PlayerMultiplayer, ObjectOutputStream> socketObservers;

    private Thread localThread;
    private boolean started;
    private int matchId;
    private TurnManager turnManager;
    private List<PlayerMultiplayer> players;
    private List<WindowPatternCard> windowsProposed;


    public MatchMultiplayer(int matchId, List<String> clients, int turnTime, Map<String, ObjectOutputStream> socketsOut) {

        super();
        this.matchId = matchId;
        started = false;

        players = new ArrayList<>();
        remoteObservers = new HashMap<>();
        socketObservers = new HashMap<>();
        turnManager = new TurnManager(this, turnTime);
        decksContainer = new DecksContainer(clients.size());
        board = new Board(this, decksContainer.getToolCardDeck().getPickedCards(), decksContainer.getPublicObjectiveCardDeck().getPickedCards());

        initializePlayers(clients, socketsOut);

        if (this.players.size() == this.socketObservers.size()) {
            localThread = new Thread(this);
            localThread.start();
            started = true;
        }

        // debug
        System.out.println("New multiplayer matchId: " + matchId);
    }

    private void initializePlayers(List<String> clients, Map<String, ObjectOutputStream> socketsOut) {
        clients.forEach(client -> {
            PlayerMultiplayer player = new PlayerMultiplayer(client, this);
            this.players.add(player);
            if (socketsOut.size() != 0) { // ha senso questo controllo?
                for (String name : socketsOut.keySet()) {
                    if (name.equals(client)) {
                        this.socketObservers.put(player, socketsOut.get(name));
                    }
                }
            }
        });
    }

    // getters
    public List<WindowPatternCard> getWindowsProposed() {
        return windowsProposed;
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }

    public Map<PlayerMultiplayer, MatchObserver> getRemoteObservers() {
        return remoteObservers;
    }

    public Map<PlayerMultiplayer, ObjectOutputStream> getSocketObservers() {
        return socketObservers;
    }

    public List<PlayerMultiplayer> getPlayers() {
        return players;
    }

    public void windowsToBeProposed() {
        windowsProposed = decksContainer.getWindowPatternCardDeck().getPickedCards().subList(0, 4);
    }


    /**
     * It checks if a player is CONNECTED and
     *
     * @return the number of CONNECTED players
     */
    public int checkConnection() {
        return (int) players.stream().map(p -> p.getStatus().equals(ConnectionStatus.CONNECTED)).count();
    }


    // game's initialisation
    @Override
    public void gameInit() {

        // todo: revision of the creation of this arraylist
        List<String> playersNames = new ArrayList<>();
        players.forEach(p -> playersNames.add(p.getName()));

        // notification to remote observers
        for (PlayerMultiplayer p : remoteObservers.keySet()) {
            try {
                remoteObservers.get(p).onPlayers(playersNames);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        //notification to sockets
        ActualPlayersResponse response = new ActualPlayersResponse(playersNames);
        for (PlayerMultiplayer p : socketObservers.keySet()) {
            try {
                socketObservers.get(p).writeObject(response);
                socketObservers.get(p).reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // actions to be performed once only
        this.roundCounter = 0;
        this.assignColors();

        // it shuffles players to determine the sequence flow of rounds. Furthermore the first player is always in the first position.
        Collections.shuffle(this.players);

        this.drawPrivateObjectiveCards();

        this.turnManager.run();
    }

    // Assegna il colore ai giocatori in modo casuale
    private void assignColors() {

        // Creation of a list of colors (without the special value NONE) to be assigned randomly to players
        Random rand = new Random();
        int index;
        List<Colors> colors = new ArrayList<>();

        // This block creates an ArrayList of colors. A color, once assigned, must be removed from the ArrayList
        for (Colors c : Colors.values()) {
            if (!c.equals(Colors.NONE)) {
                colors.add(c);
            }
        }

        for (Player p : players) {
            index = rand.nextInt(colors.size());
            p.setColor(colors.get(index));  // Da testare, non ne sono convinto
            colors.remove(index);
        }
    }


    @Override
    public void calculateFinalScore() {
        // points assigned by the private objective card
        for (PlayerMultiplayer p : players) {
            p.getPrivateObjectiveCard().useCard(p);
        }
        // points assigned by public objective cards
        for (int i = 0; i < board.getPickedPublicObjectiveCards().size(); i++) {
            for (PlayerMultiplayer p : players) {
                board.getPickedPublicObjectiveCards().get(i).useCard(p, this);
            }
        }
        // points due to free cells
        for (PlayerMultiplayer p : players) {
            p.setPoints(p.getPoints() - p.getSchemeCard().countFreeSquares());
        }
        // points due to remaining favor tokens
        for (PlayerMultiplayer p : players) {
            p.setPoints(p.getPoints() + p.getNumFavorTokens());
        }

    }

    @Override
    public void showTrack(String name) {
        if (remoteObservers.get(getPlayer(name)) != null) {
            try {
                remoteObservers.get(getPlayer(name)).onShowTrack(board.getRoundTrack().toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            if (socketObservers.get(getPlayer(name)) != null) {
                try {
                    //socketObservers.get(getPlayer(name)).writeObject(new ShowTrackResponse(board.getRoundTrack().toString());
                    socketObservers.get(getPlayer(name)).reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void theWinnerIs() {
        // occorre il metodo del controller che lo notifichi a tutti gli observer del match
        // se non abbiamo intenzione di implementare la funzionalità avanzata "persistenza" non ci serve salvare il vincitore

        //il metodo prenderà come parametro il player restituito dal seguente metodo todo: test del metodo
        //players.stream().max(Comparator.comparing(p -> p.getPoints() > p.getPoints()));
    }

    @Override
    public void drawPrivateObjectiveCards() {
        for (PlayerMultiplayer p : players) {
            p.setPrivateObjectiveCard(this.decksContainer.getPrivateObjectiveCardDeck().getPickedCards().remove(0));
        }
    }

    @Override
    public void terminateMatch() {
        for (PlayerMultiplayer p : players) {
            // chiude le connessioni
            //...

            // rimuove i nomi da takenUsernames
            // ha bisogno il riferimento a lobby, al momento sono cancellati prima di chiamare questa funzione

        }
        System.out.println("Match multiplayer " + matchId + " has been closed.");
        System.exit(0);
    }

    @Override
    public void run() {
        gameInit();
    }

    public void observeMatchRemote(MatchObserver observer, String username) {

        remoteObservers.put(getPlayer(username), observer);

        System.out.println("Gli observers remoti del match" + this.matchId + " al momento sono: " + remoteObservers.size());
        System.out.println("Il numero dei players nel match" + this.matchId + " è: " + players.size());

        if (!started) {
            if (this.players.size() == this.remoteObservers.size() + this.socketObservers.size()) {
                localThread = new Thread(this);
                localThread.start();
                started = true;
            }
        }
    }

    public void showWindow(String name, String owner) {
        //RMI
        if (remoteObservers.get(getPlayer(name)) != null) {
            try {
                remoteObservers.get(getPlayer(name)).onShowWindow(getPlayer(owner).getSchemeCard().toString());// dimostra l'utilità del metodo sottostante
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if (socketObservers.get(getPlayer(name)) != null) {
            try {
                socketObservers.get(getPlayer(name)).writeObject(new ShowWindowResponse(getPlayer(owner).getSchemeCard().toString()));
                socketObservers.get(getPlayer(name)).reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showPlayers(String name) {
        // RMI
        if (remoteObservers.get(getPlayer(name)) != null) {
            try {
                remoteObservers.get(getPlayer(name)).onPlayers(players.stream().map(Player::getName).collect(Collectors.toList()));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if (socketObservers.get(getPlayer(name)) != null) {
            try {
                socketObservers.get(getPlayer(name)).writeObject(new ActualPlayersResponse(players.stream().map(Player::getName).collect(Collectors.toList())));
                socketObservers.get(getPlayer(name)).reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public PlayerMultiplayer getPlayer(String name) {

        for (PlayerMultiplayer p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void setWindowPatternCard(String name, int index) {
        getPlayer(name).setSchemeCard(windowsProposed.get(index));
        decksContainer.getWindowPatternCardDeck().getPickedCards().removeAll(windowsProposed);
        getPlayer(name).setSchemeCardSet(true);
        setWindowChosen(true);

        if (remoteObservers.get(getPlayer(name)) != null) {
            try {
                remoteObservers.get(getPlayer(name)).onShowWindow(getPlayer(name).getSchemeCard().toString());
                remoteObservers.get(getPlayer(name)).onAfterWindowChoise();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        if (socketObservers.get(getPlayer(name)) != null) {
            try {
                socketObservers.get(getPlayer(name)).writeObject(new ShowWindowResponse(getPlayer(name).getSchemeCard().toString()));
                socketObservers.get(getPlayer(name)).writeObject(new AfterWindowChoiseResponse());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        synchronized (getLock()) { // è più giusto mettere lock protected?
            getLock().notifyAll();
        }
    }

    @Override
    public boolean placeDice(String name, int index, int x, int y) {
        if (!isDiceAction()) {
            boolean result;
            result = getPlayer(name).getSchemeCard().putDice(board.getReserve().getDices().get(index), x, y);
            setDiceAction(result);

            if (result) {
                board.getReserve().getDices().remove(index);
                if (remoteObservers.get(getPlayer(name)) != null) {
                    try {
                        remoteObservers.get(getPlayer(name)).onReserve(board.getReserve().getDices().toString());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                if (socketObservers.get(getPlayer(name)) != null) {
                    try {
                        socketObservers.get(getPlayer(name)).writeObject(new UpdateReserveResponse(board.getReserve().getDices().toString()));
                        socketObservers.get(getPlayer(name)).reset();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            synchronized (getLock()) {
                getLock().notifyAll();
            }
            return result;
        }
        return false;
    }

    public boolean useToolCard1(int diceChosen, String incrOrDecr, String name) {
        getPlayer(name).setDice(diceChosen);
        getPlayer(name).setChoise(incrOrDecr);
        boolean reserveToBeUpdated = getBoard().findAndUseToolCard(1, getPlayer(name), this);
        reserveToBeUpdatedCheck(reserveToBeUpdated, name);
        return reserveToBeUpdated;
    }

    public boolean useToolCard2or3(int n, int startX, int startY, int finalX, int finalY, String name) {
        getPlayer(name).setStartX1(startX);
        getPlayer(name).setStartY1(startY);
        getPlayer(name).setFinalX1(finalX);
        getPlayer(name).setFinalY1(finalY);
        return getBoard().findAndUseToolCard(n, getPlayer(name), this);

    }

    public boolean useToolCard4(int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String name) {
        getPlayer(name).setStartX1(startX1);
        getPlayer(name).setStartY1(startY1);
        getPlayer(name).setFinalX1(finalX1);
        getPlayer(name).setFinalY1(finalY1);
        getPlayer(name).setStartX2(startX2);
        getPlayer(name).setStartY2(startY2);
        getPlayer(name).setFinalX2(finalX2);
        getPlayer(name).setFinalY2(finalY2);
        return getBoard().findAndUseToolCard(4, getPlayer(name), this);
    }

    public boolean useToolCard6(int diceChosen, String name) {
        getPlayer(name).setDice(diceChosen);
        boolean reserveToBeUpdated = getBoard().findAndUseToolCard(6, getPlayer(name), this);
        reserveToBeUpdatedCheck(reserveToBeUpdated, name);
        return reserveToBeUpdated;
    }

    public boolean useToolCard7(String name) {
        boolean reserveToBeUpdated = getBoard().findAndUseToolCard(7, getPlayer(name), this);
        reserveToBeUpdatedCheck(reserveToBeUpdated, name);
        return reserveToBeUpdated;
    }

    public boolean useToolCard9(int diceChosen, int finalX1, int finalY1, String name) {
        getPlayer(name).setDice(diceChosen);
        getPlayer(name).setFinalX1(finalX1);
        getPlayer(name).setFinalY1(finalY1);
        boolean reserveToBeUpdated = getBoard().findAndUseToolCard(9, getPlayer(name), this);
        reserveToBeUpdatedCheck(reserveToBeUpdated, name);
        return reserveToBeUpdated;
    }

    public boolean useToolCard10(int diceChosen, String name) {
        getPlayer(name).setDice(diceChosen);
        boolean reserveToBeUpdated = getBoard().findAndUseToolCard(10, getPlayer(name), this);
        reserveToBeUpdatedCheck(reserveToBeUpdated, name);
        return reserveToBeUpdated;
    }

    private void reserveToBeUpdatedCheck(boolean reserveToBeUpdated, String name) {
        if (reserveToBeUpdated) {
            if (remoteObservers.get(getPlayer(name)) != null) {
                try {
                    remoteObservers.get(getPlayer(name)).onReserve(board.getReserve().getDices().toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            if (socketObservers.get(getPlayer(name)) != null) {
                try {
                    socketObservers.get(getPlayer(name)).writeObject(new UpdateReserveResponse(board.getReserve().getDices().toString()));
                    socketObservers.get(getPlayer(name)).reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
