package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.gameobjects.Board;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.DecksContainer;
import it.polimi.ingsw.socket.responses.*;
import it.polimi.ingsw.view.MatchObserver;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MatchMultiplayer extends Match implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(MatchMultiplayer.class.getName());

    private Map<PlayerMultiplayer, MatchObserver> remoteObservers;
    private Map<PlayerMultiplayer, ObjectOutputStream> socketObservers;

    private List<PlayerMultiplayer> ranking;
    private boolean started;
    private int turnTime;
    private int matchId;
    private TurnManagerMultiplayer turnManager;
    private List<PlayerMultiplayer> players;
    private Map<String, Integer> toolCardsPrices;
    private Map<String, Timer> pingTimers;


    /**
     * Initializes the multiplayer match
     *
     * @param matchId    is the match id
     * @param clients    is the list of players
     * @param turnTime   is the time for each player turn
     * @param socketsOut is a map playerName-ObjectOutputStream
     * @param lobby      is the match lobby
     */
    public MatchMultiplayer(int matchId, List<String> clients, int turnTime, Map<String, ObjectOutputStream> socketsOut, Lobby lobby) {

        super(lobby);
        this.matchId = matchId;
        this.turnTime = turnTime;
        started = false;
        toolCardsPrices = new HashMap<>();
        players = new ArrayList<>();
        remoteObservers = new HashMap<>();
        socketObservers = new HashMap<>();
        turnManager = new TurnManagerMultiplayer(this, turnTime);
        decksContainer = new DecksContainer(clients.size(), -1);
        board = new Board(decksContainer.getToolCardDeck().getPickedCards(), decksContainer.getPublicObjectiveCardDeck().getPickedCards());
        pingTimers = new HashMap<>();


        initializePlayers(clients, socketsOut);

        if (this.players.size() == this.socketObservers.size()) {
            localThread = new Thread(this);
            localThread.start();
            started = true;
        }

        LOGGER.log(Level.INFO, "New multiplayer matchId: " + this.matchId);
    }

    // getters
    TurnManagerMultiplayer getTurnManagerMultiplayer() {
        return turnManager;
    }

    public Map<PlayerMultiplayer, MatchObserver> getRemoteObservers() {
        return remoteObservers;
    }

    Map<PlayerMultiplayer, ObjectOutputStream> getSocketObservers() {
        return socketObservers;
    }

    public List<PlayerMultiplayer> getPlayers() {
        return players;
    }

    public Map<String, Integer> getToolCardsPrices() {
        return toolCardsPrices;
    }

    /**
     * Initializes the match players and links every socketsOut with the player name in a new map
     *
     * @param clients    is the list of players names
     * @param socketsOut is a map with players names as keys as socketsOut as values
     */
    private void initializePlayers(List<String> clients, Map<String, ObjectOutputStream> socketsOut) {
        clients.forEach(client -> {
            PlayerMultiplayer player = new PlayerMultiplayer(client);
            this.players.add(player);
            if (socketsOut.size() != 0) {
                for (String name : socketsOut.keySet()) {
                    if (name.equals(client)) {
                        this.socketObservers.put(player, socketsOut.get(name));
                    }
                }
            }
        });
    }

    /**
     * check if a player is CONNECTED and
     *
     * @return the number of CONNECTED players
     */
    int checkConnection() {
        return (int) players.stream().filter(p -> p.getStatus().equals(ConnectionStatus.CONNECTED)).count();
    }

    /**
     * assigns colors to players randomly
     */
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
            p.setColor(colors.get(index));
            colors.remove(index);
        }
    }

    /**
     * Calculates the final score at the end of the game.
     */
    @Override
    public void calculateFinalScore() {

        PlayerMultiplayer winner;
        List<String> rankingNames;
        List<Integer> rankingValues;

        ranking = new ArrayList<>();
        rankingNames = new ArrayList<>();
        rankingValues = new ArrayList<>();

        for (PlayerMultiplayer p : players) {

            // points assigned by the private objective card
            p.getPrivateObjectiveCard().useCard(p);
            // points assigned by public objective cards
            for (int i = 0; i < board.getPickedPublicObjectiveCards().size(); i++) {
                board.getPickedPublicObjectiveCards().get(i).useCard(p, this);
            }
            // points due to free cells
            p.setPoints(p.getPoints() - p.getSchemeCard().countFreeSquares());
            // points due to remaining favor tokens
            p.setPoints(p.getPoints() + p.getNumFavorTokens());

            addInOrder(p);
        }

        winner = theWinnerIs();

        for (int i = 0; i < ranking.size(); i++) {
            rankingNames.add(i, ranking.get(i).getName());
            rankingValues.add(i, ranking.get(i).getPoints());
        }

        ranking.clear();

        notifyWinner(winner, rankingNames, rankingValues);
    }

    /**
     * notifies to all clients who is the winner and the list of all players with their points. The String in position x
     * inside ranckingNames is linked to the points contained in the position x of the list rankingvalues
     *
     * @param winner        is the player whit most points
     * @param rankingNames  is the list of players' names
     * @param rankingValues is the list of values of points got by all players
     */
    private void notifyWinner(PlayerMultiplayer winner, List<String> rankingNames, List<Integer> rankingValues) {
        for (PlayerMultiplayer p : players) {
            if (remoteObservers.get(p) != null) {
                try {
                    //initializePingTimer(p.getName());
                    remoteObservers.get(p).onGameEnd(winner.getName(), rankingNames, rankingValues);
                } catch (RemoteException e) {
                    lobby.disconnect(p.getName());
                    LOGGER.log(Level.FINE, "Player " + p.getName() + " disconnected!", e);
                }
            } else if (socketObservers.get(p) != null) {
                try {
                    //initializePingTimer(p.getName());
                    socketObservers.get(p).writeObject(new GameEndResponse(winner.getName(), rankingNames, rankingValues));
                    socketObservers.get(p).reset();
                } catch (IOException e) {
                    lobby.disconnect(p.getName());
                    LOGGER.log(Level.FINE, "Player " + p.getName() + " disconnected!", e);
                }
            }
        }
    }

    private void addInOrder(PlayerMultiplayer p) {
        int i = 0;

        if (ranking.isEmpty()) {
            ranking.add(p);
        } else {
            while (i < ranking.size()) {
                if (p.getPoints() > ranking.get(i).getPoints()) {
                    break;
                }
                i++;
            }
            ranking.add(i, p);
        }
    }

    void afterReconnection(String name) {
        PlayerMultiplayer p = getPlayer(name);
        List<String> names = players.stream().map(Player::getName).collect(Collectors.toList());
        String toolCards = decksContainer.getToolCardDeck().getPickedCards().toString();
        String publicCards = decksContainer.getPublicObjectiveCardDeck().getPickedCards().toString();
        List<String> privateCard = new ArrayList<>();
        privateCard.add(p.getPrivateObjectiveCard().toString());
        String reserve = board.getReserve().getDices().toString();
        String roundTrack = board.getRoundTrack().toString();
        int myTokens = p.getNumFavorTokens();
        String[][] schemeCard = null;
        String schemeCardName = null;
        if (p.getSchemeCard() != null) {
            schemeCard = new String[4][5];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++)
                    schemeCard[i][j] = p.getSchemeCard().getWindow()[i][j].toString();
            }
            schemeCardName = p.getSchemeCard().getName();
        }
        Map<String, Integer> otherTokens = new HashMap<>();
        Map<String, String[][]> otherSchemeCards = new HashMap<>();
        Map<String, String> otherSchemeCardNamesMap = new HashMap<>();
        boolean schemeCardChosen = p.isSchemeCardSet();
        for (PlayerMultiplayer player : players) {
            if (!player.getName().equals(name)) {
                otherTokens.put(player.getName(), player.getNumFavorTokens());
            }
        }
        for (PlayerMultiplayer player : players) {
            String[][] otherSchemeCard = new String[4][5];
            if (!player.getName().equals(name)) {
                if (player.getSchemeCard() != null) {
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 5; j++)
                            otherSchemeCard[i][j] = player.getSchemeCard().getWindow()[i][j].toString();
                    }
                    otherSchemeCards.put(player.getName(), otherSchemeCard);
                    otherSchemeCardNamesMap.put(player.getName(), player.getSchemeCard().getName());
                }
            }
        }


        if (remoteObservers.get(p) != null) {
            try {
                //initializePingTimer(p.getName());
                remoteObservers.get(p).onAfterReconnection(toolCards, publicCards, privateCard, reserve, roundTrack, myTokens, schemeCard, schemeCardName, otherTokens, otherSchemeCards, otherSchemeCardNamesMap, schemeCardChosen, toolCardsPrices);
                remoteObservers.get(p).onGameStarted(p.isSchemeCardSet(), names, turnTime);
            } catch (RemoteException e) {
                lobby.disconnect(p.getName());
                LOGGER.log(Level.FINE, "Player " + p.getName() + " disconnected!", e);
            }
        } else if (socketObservers.get(p) != null) {
            try {
                //initializePingTimer(p.getName());
                socketObservers.get(p).writeObject(new AfterReconnectionResponse(toolCards, publicCards, privateCard, reserve, roundTrack, myTokens, schemeCard, schemeCardName, otherTokens, otherSchemeCards, otherSchemeCardNamesMap, schemeCardChosen, toolCardsPrices));
                socketObservers.get(p).writeObject(new GameStartedResponse(p.isSchemeCardSet(), names, turnTime));
                socketObservers.get(p).reset();
            } catch (IOException e) {
                lobby.disconnect(p.getName());
                LOGGER.log(Level.FINE, "Player " + p.getName() + " disconnected!", e);
            }
        }
    }

    /**
     * The player with the highest score is the winner.
     * If two or more players have the same score, the winner is the one who got more points from
     * his PrivateObjectiveCard. If a winner is still not found, the winner is the player who has more FavorTokens left
     * at the end of the game. If a winner is still not found, the winner is the player in the lowest position in
     * last round order.
     *
     * @return the winner player
     */
    private PlayerMultiplayer theWinnerIs() {
        List<PlayerMultiplayer> firstPlayoff;
        List<PlayerMultiplayer> secondPlayoff;
        List<PlayerMultiplayer> thirdPlayoff;

        firstPlayoff = new ArrayList<>();

        firstPlayoff.add(ranking.get(0));

        // compare points
        for (int i = 0; i < ranking.size() - 1; i++) {
            if (ranking.get(i).getPoints() == ranking.get(i + 1).getPoints()) {
                firstPlayoff.add(ranking.get(i + 1));
            } else {
                break;
            }
        }

        if (firstPlayoff.size() == 1) {
            return firstPlayoff.get(0);
        } else {
            // compare points by private objective card
            secondPlayoff = new ArrayList<>();

            int maxPrivate;
            maxPrivate = firstPlayoff.get(0).getPointsByPrivateObjective();

            // find the max value
            for (int i = 1; i < firstPlayoff.size(); i++) {
                if (firstPlayoff.get(i).getPointsByPrivateObjective() > maxPrivate) {
                    maxPrivate = firstPlayoff.get(i).getPointsByPrivateObjective();
                }
            }

            // find players admitted to the second playoff
            for (PlayerMultiplayer pla : firstPlayoff) {
                if (pla.getPointsByPrivateObjective() == maxPrivate) {
                    secondPlayoff.add(pla);
                }
            }

            if (secondPlayoff.size() == 1) {
                return secondPlayoff.get(0);
            } else {
                thirdPlayoff = new ArrayList<>();

                int maxTokens;
                maxTokens = secondPlayoff.get(0).getNumFavorTokens();

                // find the max value
                for (int i = 1; i < firstPlayoff.size(); i++) {
                    if (secondPlayoff.get(i).getNumFavorTokens() > maxTokens) {
                        maxTokens = secondPlayoff.get(i).getNumFavorTokens();
                    }
                }

                // find players admitted to the third playoff
                for (PlayerMultiplayer p : secondPlayoff) {
                    if (p.getNumFavorTokens() == maxTokens) {
                        thirdPlayoff.add(p);
                    }
                }

                if (thirdPlayoff.size() == 1) {
                    return thirdPlayoff.get(0);
                } else {
                    return lastPlayoff(thirdPlayoff);
                }
            }
        }
    }

    /**
     * Determines the winner if there are players with the same points at the end of other playoffs
     *
     * @param playoff list of players with the same points by tokens
     * @return the player which is in playoff and played before other players in playoff in the last round
     */
    private PlayerMultiplayer lastPlayoff(List<PlayerMultiplayer> playoff) {
        for (PlayerMultiplayer p : players) {
            if (playoff.contains(p)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void drawPrivateObjectiveCards() {
        for (PlayerMultiplayer p : players) {
            p.setPrivateObjectiveCard(this.decksContainer.getPrivateObjectiveCardDeck().getPickedCards().remove(0));
        }
    }

    @Override
    public void run() {

        List<String> playersNames = new ArrayList<>();
        players.forEach(p -> playersNames.add(p.getName()));

        // notification to remote observers
        for (PlayerMultiplayer p : remoteObservers.keySet()) {
            if (remoteObservers.get(p) != null) {
                try {
                    //initializePingTimer(p.getName());
                    remoteObservers.get(p).onGameStarted(p.isSchemeCardSet(), playersNames, turnTime);
                } catch (RemoteException e) {
                    lobby.disconnect(p.getName());
                    LOGGER.log(Level.FINE, "Player " + p.getName() + " disconnected!", e);
                }
            }
        }

        //notification to sockets
        for (PlayerMultiplayer p : socketObservers.keySet()) {
            if (socketObservers.get(p) != null) {
                try {
                    //initializePingTimer(p.getName());
                    socketObservers.get(p).writeObject(new GameStartedResponse(p.isSchemeCardSet(), playersNames, turnTime));
                    socketObservers.get(p).reset();
                } catch (IOException e) {
                    lobby.disconnect(p.getName());
                    LOGGER.log(Level.FINE, "Player " + p.getName() + " disconnected!", e);
                }
            }
        }

        // actions to be performed once only
        roundCounter = 0;
        assignColors();

        // it shuffles players to determine the sequence flow of rounds. Furthermore the first player is always in the first position.
        Collections.shuffle(this.players);

        drawPrivateObjectiveCards();

        turnManager.run();
    }

    public void observeMatchRemote(MatchObserver observer, String username) {

        remoteObservers.put(getPlayer(username), observer);

        if (!started) {
            if (this.players.size() == this.remoteObservers.size() + this.socketObservers.size()) {
                localThread = new Thread(this);
                localThread.start();
                started = true;
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
        PlayerMultiplayer p = getPlayer(name);

        p.setSchemeCard(windowsProposed.get(index));
        decksContainer.getWindowPatternCardDeck().getPickedCards().removeAll(windowsProposed);
        p.setSchemeCardSet(true);
        setWindowChosen(true);


        schemeCardsToBeUpdated(true, name);

        if (remoteObservers.get(p) != null) {
            try {
                //initializePingTimer(p.getName());
                remoteObservers.get(p).onAfterWindowChoice();
            } catch (RemoteException e) {
                lobby.disconnect(p.getName());
                LOGGER.log(Level.FINE, "Player " + p.getName() + " disconnected!", e);
            }
        }
        if (socketObservers.get(p) != null) {
            try {
                //initializePingTimer(p.getName());
                socketObservers.get(p).writeObject(new AfterWindowChoiceResponse());
                socketObservers.get(p).reset();
            } catch (IOException e) {
                lobby.disconnect(p.getName());
                LOGGER.log(Level.FINE, "Player " + p.getName() + " disconnected!", e);
            }
        }

        tokensToBeUpdated(true, name);

        synchronized (getLock()) {
            getLock().notifyAll();
        }
    }

    @Override
    public boolean placeDice(String name, int index, int x, int y) {
        if (!isDiceAction()) {
            boolean result;
            result = getPlayer(name).getSchemeCard().putDice(board.getReserve().getDices().get(index), x, y);
            setDiceAction(result);

            //SPECIAL NOTIFY FOR SOCKET
            if (socketObservers.get(getPlayer(name)) != null) {
                try {
                    socketObservers.get(getPlayer(name)).writeObject(new DicePlacedResponse(result));
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "problem in placing dice with a socket connection", e);
                }
            }

            if (result) {
                board.getReserve().getDices().remove(index);
            }
            reserveToBeUpdated(result);
            schemeCardsToBeUpdated(result, name);

            synchronized (getLock()) {
                getLock().notifyAll();
            }
            return result;
        }
        return false;
    }

    @Override
    public boolean placeDiceTool11(String name, int x, int y) {
        if (!isDiceAction()) {
            boolean result;
            result = getPlayer(name).getSchemeCard().putDice(getPlayer(name).getDiceFromBag(), x, y);
            setDiceAction(result);
            schemeCardsToBeUpdated(result, name);

            synchronized (getLock()) {
                getLock().notifyAll();
            }
            return result;
        }
        return false;
    }

    @Override
    public boolean useToolCard1(int diceToBeSacrificed, int diceChosen, String incrOrDecr, String name) {
        if (!isToolAction()) {
            getPlayer(name).setDice(diceChosen);
            getPlayer(name).setChoice(incrOrDecr);
            boolean result = getBoard().findAndUseToolCard(1, getPlayer(name), this);
            tokensToBeUpdated(result, name);
            reserveToBeUpdated(result);
            setToolAction(result);

            synchronized (getLock()) {
                getLock().notifyAll();
            }

            return result;
        } else {
            return false;
        }
    }

    @Override
    public boolean useToolCard2or3(int diceToBeSacrificed, int n, int startX, int startY, int finalX, int finalY, String name) {
        if (!isToolAction()) {
            getPlayer(name).setStartX1(startX);
            getPlayer(name).setStartY1(startY);
            getPlayer(name).setFinalX1(finalX);
            getPlayer(name).setFinalY1(finalY);
            boolean result = getBoard().findAndUseToolCard(n, getPlayer(name), this);
            tokensToBeUpdated(result, name);
            schemeCardsToBeUpdated(result, name);
            setToolAction(result);

            synchronized (getLock()) {
                getLock().notifyAll();
            }

            return result;
        } else {
            return false;
        }
    }

    @Override
    public boolean useToolCard4(int diceToBeSacrificedd, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String name) {
        if (!isToolAction()) {
            getPlayer(name).setStartX1(startX1);
            getPlayer(name).setStartY1(startY1);
            getPlayer(name).setFinalX1(finalX1);
            getPlayer(name).setFinalY1(finalY1);
            getPlayer(name).setStartX2(startX2);
            getPlayer(name).setStartY2(startY2);
            getPlayer(name).setFinalX2(finalX2);
            getPlayer(name).setFinalY2(finalY2);
            boolean result = getBoard().findAndUseToolCard(4, getPlayer(name), this);
            tokensToBeUpdated(result, name);
            schemeCardsToBeUpdated(result, name);
            setToolAction(result);

            synchronized (getLock()) {
                getLock().notifyAll();
            }

            return result;
        } else {
            return false;
        }
    }

    @Override
    public boolean useToolCard5(int diceToBeSacrificed, int diceChosen, int roundChosen, int diceChosenFromRound, String name) {
        if (!isToolAction()) {
            getPlayer(name).setDice(diceChosen);
            getPlayer(name).setRound(roundChosen);
            getPlayer(name).setDiceChosenFromRound(diceChosenFromRound);
            boolean result = getBoard().findAndUseToolCard(5, getPlayer(name), this);
            tokensToBeUpdated(result, name);
            reserveToBeUpdated(result);
            setToolAction(result);
            Response response = new RoundTrackResponse(board.getRoundTrack().toString());
            for (PlayerMultiplayer player : players) {
                if (remoteObservers.get(player) != null) {
                    try {
                        //initializePingTimer(player.getName());
                        remoteObservers.get(player).onRoundTrack(board.getRoundTrack().toString());
                    } catch (RemoteException e) {
                        lobby.disconnect(player.getName());
                        LOGGER.log(Level.FINE, "Player " + player.getName() + " disconnected!", e);
                    }
                } else {
                    //initializePingTimer(player.getName());
                    notifyToSocketClient(player, response);
                }
            }

            synchronized (getLock()) {
                getLock().notifyAll();
            }

            return result;
        } else {
            return false;
        }
    }

    @Override
    public boolean useToolCard6(int diceToBeSacrificed, int diceChosen, String name) {
        if (!isToolAction()) {
            getPlayer(name).setDice(diceChosen);
            boolean result = getBoard().findAndUseToolCard(6, getPlayer(name), this);
            tokensToBeUpdated(result, name);
            reserveToBeUpdated(result);
            setToolAction(result);

            synchronized (getLock()) {
                getLock().notifyAll();
            }

            return result;
        } else {
            return false;
        }
    }

    @Override
    public boolean useToolCard7(int diceToBeSacrificed, String name) {
        if (!isToolAction()) {
            boolean result = getBoard().findAndUseToolCard(7, getPlayer(name), this);
            reserveToBeUpdated(result);
            tokensToBeUpdated(result, name);
            setToolAction(result);

            synchronized (getLock()) {
                getLock().notifyAll();
            }

            return result;
        } else {
            return false;
        }
    }

    @Override
    public boolean useToolCard8(int diceToBeSacrificed, String name) {
        if (!isToolAction()) {
            boolean result = getBoard().findAndUseToolCard(8, getPlayer(name), this);
            tokensToBeUpdated(result, name);
            setToolAction(result);
            synchronized (getLock()) {
                getLock().notifyAll();
            }

            return result;
        } else {
            return false;
        }
    }

    @Override
    public boolean useToolCard9(int diceToBeSacrificed, int diceChosen, int finalX1, int finalY1, String name) {
        if (!isToolAction() && !isDiceAction()) {
            getPlayer(name).setDice(diceChosen);
            getPlayer(name).setFinalX1(finalX1);
            getPlayer(name).setFinalY1(finalY1);
            boolean result = getBoard().findAndUseToolCard(9, getPlayer(name), this);
            tokensToBeUpdated(result, name);
            reserveToBeUpdated(result);
            schemeCardsToBeUpdated(result, name);
            setDiceAction(result);
            setToolAction(result);

            synchronized (getLock()) {
                getLock().notifyAll();
            }

            return result;
        } else {
            return false;
        }
    }

    @Override
    public boolean useToolCard10(int diceToBeSacrificed, int diceChosen, String name) {
        if (!isToolAction()) {
            getPlayer(name).setDice(diceChosen);
            boolean result = getBoard().findAndUseToolCard(10, getPlayer(name), this);
            tokensToBeUpdated(result, name);
            reserveToBeUpdated(result);
            setToolAction(result);

            synchronized (getLock()) {
                getLock().notifyAll();
            }

            return result;
        } else return false;
    }

    @Override
    public boolean useToolCard11(int diceToBeSacrificed, int diceChosen, String name) {
        if (!(isToolAction() || isDiceAction())) {
            getPlayer(name).setDice(diceChosen);
            boolean result = getBoard().findAndUseToolCard(11, getPlayer(name), this);
            tokensToBeUpdated(result, name);
            reserveToBeUpdated(result);
            setToolAction(result);
            return result;
        } else return false;
    }

    @Override
    public boolean useToolCard12(int diceToBeSacrificed, int roundFromTrack, int diceInRound, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String name) {
        if (!isToolAction()) {
            getPlayer(name).setRound(roundFromTrack);
            getPlayer(name).setDiceChosenFromRound(diceInRound);
            getPlayer(name).setStartX1(startX1);
            getPlayer(name).setStartY1(startY1);
            getPlayer(name).setFinalX1(finalX1);
            getPlayer(name).setFinalY1(finalY1);
            getPlayer(name).setStartX2(startX2);
            getPlayer(name).setStartY2(startY2);
            getPlayer(name).setFinalX2(finalX2);
            getPlayer(name).setFinalY2(finalY2);
            boolean result = getBoard().findAndUseToolCard(12, getPlayer(name), this);
            tokensToBeUpdated(result, name);
            schemeCardsToBeUpdated(result, name);
            setToolAction(result);

            synchronized (getLock()) {
                getLock().notifyAll();
            }

            return result;
        } else return false;
    }

    /**
     * Cancel timer if the client linked to this match is connected. The client calls this method after receiving a notify
     *
     * @param username is the name of the player
     */
    @Override
    public void ping(String username) {
        if (pingTimers.get(username) != null) {
            LOGGER.log(Level.INFO, "Timer cancellato, giocatore " + username); // todo: si può cancellare
            pingTimers.get(username).cancel();
            pingTimers.put(username, null);
        }
    }

    private void reserveToBeUpdated(boolean reserveToBeUpdated) {
        if (reserveToBeUpdated) {
            Response response = new ReserveResponse(board.getReserve().getDices().toString());
            for (PlayerMultiplayer player : players) {
                if (remoteObservers.get(player) != null) {
                    try {
                        //initializePingTimer(player.getName());
                        remoteObservers.get(player).onReserve(board.getReserve().getDices().toString());
                    } catch (RemoteException e) {
                        lobby.disconnect(player.getName());
                        LOGGER.log(Level.FINE, "Player " + player.getName() + " disconnected!", e);
                    }
                } else {
                    //initializePingTimer(player.getName());
                    notifyToSocketClient(player, response);
                }
            }
        }
    }

    private void tokensToBeUpdated(boolean result, String name) {
        PlayerMultiplayer p = getPlayer(name);
        if (result) {
            if (remoteObservers.get(p) != null) {
                try {
                    //initializePingTimer(p.getName());
                    remoteObservers.get(p).onMyFavorTokens(p.getNumFavorTokens());
                } catch (RemoteException e) {
                    lobby.disconnect(p.getName());
                    LOGGER.log(Level.FINE, "Player " + p.getName() + " disconnected!", e);
                }
            } else if (socketObservers.get(p) != null) {
                try {
                    //initializePingTimer(p.getName());
                    socketObservers.get(p).writeObject(new MyFavorTokensResponse(p.getNumFavorTokens()));
                    socketObservers.get(p).reset();
                } catch (IOException e) {
                    lobby.disconnect(p.getName());
                    LOGGER.log(Level.FINE, "Player " + p.getName() + " disconnected!", e);
                }
            }
            Response response = new OtherFavorTokensResponse(p.getNumFavorTokens(), name);
            for (PlayerMultiplayer otherPlayer : players) {
                if (!otherPlayer.getName().equals(name)) {
                    if (remoteObservers.get(otherPlayer) != null) {
                        try {
                            //initializePingTimer(p.getName());
                            remoteObservers.get(otherPlayer).onOtherFavorTokens(p.getNumFavorTokens(), name);
                        } catch (RemoteException e) {
                            lobby.disconnect(otherPlayer.getName());
                            LOGGER.log(Level.FINE, "Player " + p.getName() + " disconnected!", e);
                        }
                    } else {
                        //initializePingTimer(p.getName());
                        notifyToSocketClient(otherPlayer, response);
                    }
                }
            }
        }
    }

    private void schemeCardsToBeUpdated(boolean result, String name) {
        PlayerMultiplayer p = getPlayer(name);

        String[][] schemeCard = new String[4][5];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                schemeCard[i][j] = p.getSchemeCard().getWindow()[i][j].toString();
            }
        }


        if (result) {
            if (remoteObservers.get(p) != null) {
                try {
                    //initializePingTimer(p.getName());
                    remoteObservers.get(p).onMyWindow(schemeCard);
                } catch (RemoteException e) {
                    lobby.disconnect(p.getName());
                    LOGGER.log(Level.FINE, "Player " + p.getName() + " disconnected!", e);
                }
            } else if (socketObservers.get(p) != null) {
                try {
                    //initializePingTimer(p.getName());
                    socketObservers.get(p).writeObject(new MyWindowResponse(schemeCard));
                } catch (IOException e) {
                    lobby.disconnect(p.getName());
                    LOGGER.log(Level.FINE, "Player " + p.getName() + " disconnected!", e);
                }
            }

            Response response = new OtherSchemeCardsResponse(schemeCard, name, p.getSchemeCard().getName());
            for (PlayerMultiplayer otherPlayer : players) {
                if (!otherPlayer.getName().equals(name)) {
                    if (remoteObservers.get(otherPlayer) != null) {
                        try {
                            //initializePingTimer(p.getName());
                            remoteObservers.get(otherPlayer).onOtherSchemeCards(schemeCard, name, p.getSchemeCard().getName());
                        } catch (RemoteException e) {
                            lobby.disconnect(otherPlayer.getName());
                            LOGGER.log(Level.FINE, "Player " + p.getName() + " disconnected!", e);
                        }
                    } else {
                        //initializePingTimer(p.getName());
                        notifyToSocketClient(otherPlayer, response);
                    }
                }
            }
        }
    }

    public void notifyToSocketClient(PlayerMultiplayer player, Response response) {
        if (socketObservers.get(player) != null) {
            try {
                socketObservers.get(player).writeObject(response);
                socketObservers.get(player).reset();
            } catch (IOException e) {
                lobby.disconnect(player.getName());
                LOGGER.log(Level.FINE, "Player " + player.getName() + " disconnected!", e);
            }
        }
    }

    void deleteDisconnectedClients() {
        lobby.deleteDisconnectedClients(players.stream().map(Player::getName).collect(Collectors.toList()));
    }

    @Override
    public void terminateMatch() {
        lobby.removeFromMatchMulti(players.get(0).getName());
        localThread.interrupt();
    }

    /**
     * initializes the timer for the ping response of player whose name is equal to the parameter name
     *
     * @param name is the name of the player
     */
    public void initializePingTimer(String name) {
        if (pingTimers.get(name) == null) {
            LOGGER.log(Level.INFO, "Timer inizializzato, giocatore " + name); // todo: si può cancellare
            PingTimer task = new PingTimer(name, lobby);
            pingTimers.put(name, new Timer());
            pingTimers.get(name).schedule(task, Match.PING_TIME);
        }
    }
}
