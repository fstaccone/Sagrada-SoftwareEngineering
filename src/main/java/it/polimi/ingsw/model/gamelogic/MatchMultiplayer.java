package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.MatchObserver;
import it.polimi.ingsw.model.gameobjects.*;

import javax.sound.midi.SysexMessage;
import java.rmi.RemoteException;
import java.util.*;

public class MatchMultiplayer extends Match implements Runnable {

    private Map<PlayerMultiplayer, MatchObserver> remoteObservers;
    private Map<PlayerMultiplayer, MatchObserver> socketObservers;

    private Thread localThread;

    private int matchId;
    private TurnManager turnManager;

    List<WindowPatternCard> windowsToBeProposed;

    /**
     *
     */
    private Object lock;

    public Object getLock() { return lock; }

    /**
     *
     */

    private List<PlayerMultiplayer> players;

    public MatchMultiplayer(int matchId, List<String> clients, int turnTime) {

        super();
        this.matchId = matchId;

        this.lock = new Object();
        turnManager = new TurnManager(this, turnTime);

        System.out.println("New multiplayer matchId: " + matchId);
        this.remoteObservers = new HashMap<>();
        this.socketObservers = new HashMap<>();

        System.out.println("New multiplayer matchId: " + matchId);

        this.decksContainer = new DecksContainer(clients.size());
        this.board = new Board(this, decksContainer.getToolCardDeck().getPickedCards(), decksContainer.getPublicObjectiveCardDeck().getPickedCards());

        this.players = new ArrayList<>();
        clients.forEach(p -> this.players.add(new PlayerMultiplayer(p, this)));
    }

    /**
     * è il posto giusto?
     *
     */
    public List<WindowPatternCard> getWindowsToBeProposed() { return windowsToBeProposed; }

    public void initializeWindowsToBeProposed(List<WindowPatternCard> windowsToBeProposed, int n) {
        this.windowsToBeProposed = decksContainer.getWindowPatternCardDeck().getPickedCards().subList(4*n , 4*(n+1)); // todo: sarebbe più sensato eliminare le carte
    }

    /**
     *
     *
     */

    public TurnManager getTurnManager() { return turnManager; }

    public int getMatchId() {
        return matchId;
    }

    public Map<PlayerMultiplayer, MatchObserver> getRemoteObservers() {
        return remoteObservers;
    }

    public List<PlayerMultiplayer> getPlayers() {
        return players;
    }


    // todo: controllare, come gestiamo i player CONNECTED?
    // it returns the number of READY players
    public int checkConnection() {
        return (int) players.stream().map(p -> p.getStatus().equals(ConnectionStatus.READY)).count();
    }


    // game's initialisation
    @Override
    public void gameInit() throws InterruptedException, RemoteException {

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

        /*
            DA FARE LA STESSA COSA DI QUI SOPRA MA CON I SOCKET
        */
        // actions to be performed once only
        this.roundCounter = 0;
        this.assignColors();

        // it shuffles players to determine the sequence flow of rounds. Furthermore the first player is always in the first position.
        Collections.shuffle(this.players);

        this.drawPrivateObjectiveCards();
        //this.proposeWindowPatternCards();

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

    public void theWinnerIs() {
        // occorre il metodo del controller che lo notifichi a tutti gli observer del match
        // se non abbiamo intenzione di implementare la funzionalità avanzata "persistenza" non ci serve salvare il vincitore

        //il metodo prenderà come parametro il player restituito dal seguente metodo todo: test del metodo
        //players.stream().max(Comparator.comparing(p -> p.getPoints() > p.getPoints()));
    }

    @Override
    public void drawPrivateObjectiveCards() {
        for (PlayerMultiplayer p : players) {
            p.setPrivateObjectiveCard(this.decksContainer.getPrivateObjectiveCardDeck().getPickedCards().get(0));
            this.decksContainer.getPrivateObjectiveCardDeck().getPickedCards().remove(this.decksContainer.getPrivateObjectiveCardDeck().getPickedCards().get(0));
        }
    }

    @Override
    public void proposeWindowPatternCards() {
        // todo: implement
    }

    @Override
    public void terminateMatch() {
        for (PlayerMultiplayer p : players) {
            // chiudi le connessioni
            //...

            // rimuove i nomi da takenUsernames
            // ha bisogno il riferimento a lobby, al momento sono cancellati prima di chiamare questa funzione

        }
        System.out.println("Match multiplayer " + matchId + " has been closed.");
        System.exit(0);
    }

    @Override
    public void run() {
        try {
            gameInit();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Exception in MatchMultiplayer, run()");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void observeMatchRemote(MatchObserver observer, String username) {

        for (PlayerMultiplayer p : players) {
            if (p.getName().equals(username)) {
                this.remoteObservers.put(p, observer);
                break;
            }
        }

        System.out.println("Gli observers remoti del match" + this.matchId + " al momento sono: " + remoteObservers.size());
        System.out.println("Il numero dei players nel match" + this.matchId + " è: " + players.size());
        if (this.players.size() == this.remoteObservers.size() + this.socketObservers.size()) {
            localThread.start();
        }
    }

    public void observeMatchSocket(MatchObserver observer, String username) {

        for (PlayerMultiplayer p : players) {
            if (p.getName().equals(username)) {
                this.socketObservers.put(p, observer);
                break;
            }
        }

        System.out.println("Gli observers socket del match" + this.matchId + " al momento sono: " + socketObservers.size());
        System.out.println("Il numero dei players nel match" + this.matchId + " è: " + players.size());
        if (this.players.size() == this.remoteObservers.size() + this.socketObservers.size()) {
            localThread.start();
        }
    }

    public void start(){
        localThread=new Thread(this);
    }
}
