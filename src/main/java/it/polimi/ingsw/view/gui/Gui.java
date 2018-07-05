package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.control.SocketController;
import it.polimi.ingsw.socket.requests.PingRequest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Gui {

    private String username;
    private RemoteController controllerRmi;
    private SocketController controllerSocket;
    private Stage windowStage;
    private boolean stillPlaying;

    private boolean myTurn;
    private boolean reconnection;

    private ChooseCardHandlerMultiplayer chooseCardHandlerMultiplayer;
    private ChooseCardHandlerSingle chooseCardHandlerSingle;
    private GameBoardHandlerMulti gameBoardHandlerMulti;
    private GameBoardHandlerSingle gameBoardHandlerSingle;
    private String track;
    private List<String> toolCardsList;
    private List<String> dicesList;
    private List<String> privateCards;
    private List<String> publicCardsList;
    private Map<String, Integer> otherFavorTokensMap;
    private Map<String, String[][]> otherSchemeCardsMap;
    private Map<String, String> otherSchemeCardNamesMap;
    private List<String> players;
    private String[][] mySchemeCard;
    private int myTokens;
    private Map<String, Integer> toolcardsPrices;
    private boolean single;

    private String playerSchemeCardImageURL;
    private AudioClip turnClip = Applet.newAudioClip(getClass().getResource("/sounds/turn.au"));

    /**
     * GUI constructor.
     *
     * @param fromLogin        is the Stage where the login scene was showed.
     * @param username         is the name of the GUI owner.
     * @param controllerRmi    is the controller in case of rmi connection.
     * @param controllerSocket is the controller in case of socket connection.
     * @param single           is true if the GUI refers to a single player match, false otherwise.
     */
    Gui(Stage fromLogin, String username, RemoteController controllerRmi, SocketController controllerSocket, boolean single) {
        this.username = username;
        this.controllerRmi = controllerRmi;
        this.controllerSocket = controllerSocket;
        privateCards = new ArrayList<>();
        myTurn = false;
        this.single = single;
        stillPlaying = true;
        windowStage = fromLogin;

        otherFavorTokensMap = new HashMap<>();
        otherSchemeCardsMap = new HashMap<>();
        otherSchemeCardNamesMap = new HashMap<>();
        toolCardsList = new ArrayList<>();
        publicCardsList = new ArrayList<>();
        players = new ArrayList<>();
    }

    // getters
    public boolean isSingle() {
        return single;
    }

    List<String> getToolCardsList() {
        return toolCardsList;
    }

    RemoteController getControllerRmi() {
        return controllerRmi;
    }

    SocketController getControllerSocket() {
        return controllerSocket;
    }

    Stage getWindowStage() {
        return windowStage;
    }

    Boolean isMyTurn() {
        return myTurn;
    }

    List<String> getDicesList() {
        return dicesList;
    }

    public List<String> getPlayers() {
        return players;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Called on the player's turn.
     *
     * @param isMyTurn true if it's the player's turn, false otherwise.
     * @param string   represents the reserve. If not null, the reserve is updated.
     * @param round    indicates the current round.
     * @param turn     indicates the current turn.
     */
    public void onYourTurn(boolean isMyTurn, String string, int round, int turn) {

        if (string != null) {
            onReserve(string);
        }
        this.myTurn = isMyTurn;
        if (myTurn) {
            // ping response to be considered connected
            respondToPing();
            turnClip.play();
            String multi = "Ora è il tuo turno!\nRound: " + round + "\tTurno: " + turn;
            String single = "Round: " + round + "\tTurno: " + turn;
            if (gameBoardHandlerMulti != null) {
                gameBoardHandlerMulti.setDiceChosenOutOfRange();
                gameBoardHandlerMulti.resetToolValues();
                gameBoardHandlerMulti.appendToTextArea(multi);
                //gameBoardHandlerMulti.initializeActions();
            } else if (chooseCardHandlerMultiplayer != null) {
                chooseCardHandlerMultiplayer.appendToTextArea(multi);
                chooseCardHandlerMultiplayer.setTurn(true);
            } else if (gameBoardHandlerSingle != null) {
                gameBoardHandlerSingle.setDiceChosenOutOfRange();
                gameBoardHandlerSingle.resetToolValues();
                gameBoardHandlerSingle.appendToTextArea(single);
            }
        } else {
            if (chooseCardHandlerMultiplayer != null) {
                chooseCardHandlerMultiplayer.setTurn(false);
            }
        }
    }

    /**
     * respond to ping in order to prove that connection is ok
     */
    private void respondToPing() {
        /*if (controllerRmi != null) {
            try {
                controllerRmi.ping(username, single);
            } catch (RemoteException e) {
                closingForDisconnection();
            }
        } else */
            if (controllerSocket != null) {
            try {
                controllerSocket.request(new PingRequest(username, single));
            } catch (Exception e) {
                closingForDisconnection();
            }
        }
    }

    /**
     * Updates the reserve.
     *
     * @param string is the string representing the reserve.
     */
    public void onReserve(String string) {
        // ping response to be considered connected
        //respondToPing();

        dicesList = new ArrayList<>();
        String dicesString = string.substring(1, string.length() - 1);
        List<String> temp = Pattern.compile(", ")
                .splitAsStream(dicesString)
                .collect(Collectors.toList());
        for (String s : temp) {
            s = s.substring(1, s.length() - 1).toLowerCase().replaceAll(" ", "_");
            dicesList.add(s);
        }
        if (single) {
            if (gameBoardHandlerSingle != null) {
                gameBoardHandlerSingle.setReserve(dicesList);
            }
        } else {
            if (gameBoardHandlerMulti != null) {
                gameBoardHandlerMulti.setReserve(dicesList);
            }
        }
    }

    /**
     * Shows a message in the texArea when a player is reconnected to the match.
     *
     * @param name is the name of the player reconnected to the match.
     */
    public void onPlayerReconnection(String name) {
        if (gameBoardHandlerMulti != null) {
            gameBoardHandlerMulti.appendToTextArea("Il giocatore " + name + " è entrato nella partita!");
        } else if (chooseCardHandlerMultiplayer != null) {
            chooseCardHandlerMultiplayer.appendToTextArea("Il giocatore " + name + " è entrato nella partita!");
        }
    }

    /**
     * Called whenever the game starts.
     *
     * @param windowChosen true if the GUI owner has already chosen his window pattern card.
     * @param names        is the list of players in the match.
     * @param turnTime     is the time limit for each turn.
     */
    public void onGameStarted(Boolean windowChosen, List<String> names, int turnTime) {
        // ping response to be considered connected
        //respondToPing();

        players = names;

        if (windowChosen) {
            if (single) {
                onAfterWindowChoiceSingleplayer();
                notifyTimerSingle(turnTime);
            } else {
                onAfterWindowChoiceMultiplayer();
                notifyTimerMulti(turnTime);
            }
        } else {
            if (single) {
                initializeChooseCardSingle(turnTime);
            } else {
                initializeChooseCardMulti(turnTime);
            }
        }
    }

    /**
     * The player in a single player match is redirected to the window pattern card choice scene.
     *
     * @param turnTime is the time limit for each turn.
     */
    private void initializeChooseCardSingle(int turnTime) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = null;
        fxmlLoader.setLocation(getClass().getResource("/choose-card-single.fxml"));
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chooseCardHandlerSingle = fxmlLoader.getController();
        assert root != null;
        Scene scene = new Scene(root);
        chooseCardHandlerSingle.init(windowStage, scene, controllerRmi, controllerSocket, username);
        chooseCardHandlerSingle.welcome(turnTime);
    }

    /**
     * The player in a multi player match is redirected to the window pattern card choice scene.
     *
     * @param turnTime is the time limit for each turn.
     */
    private void initializeChooseCardMulti(int turnTime) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = null;
        fxmlLoader.setLocation(getClass().getResource("/choose-card-multiplayer.fxml"));
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chooseCardHandlerMultiplayer = fxmlLoader.getController();
        assert root != null;
        Scene scene = new Scene(root);
        chooseCardHandlerMultiplayer.init(windowStage, scene, controllerRmi, controllerSocket, username);
        chooseCardHandlerMultiplayer.welcome(turnTime);
        chooseCardHandlerMultiplayer.showOpponents(players);
        chooseCardHandlerMultiplayer.appendToTextArea("Hai a disposizione " + turnTime / 1000 + " secondi ad ogni turno per giocare!");
    }

    /**
     * Called when the game is being closed.
     */
    public void onGameClosing() {
        // ping response to be considered connected
        //respondToPing();

        if (stillPlaying) {
            if (gameBoardHandlerMulti != null) {
                gameBoardHandlerMulti.onGameClosing();
            } else if (chooseCardHandlerMultiplayer != null) {
                chooseCardHandlerMultiplayer.onGameClosing();
            }
        }
        stillPlaying = false;
    }

    /**
     * Called when a multi player match ends.
     *
     * @param winner        is the player with the highest score.
     * @param rankingNames  is the ordered list of players by their scores.
     * @param rankingValues is the ordered list of the players'scores.
     */
    public void onGameEndMulti(String winner, List<String> rankingNames, List<Integer> rankingValues) {
        // ping response to be considered connected
        //respondToPing();

        if (gameBoardHandlerMulti != null) {
            gameBoardHandlerMulti.showRanking(winner, rankingNames, rankingValues);
        }
        stillPlaying = false;
    }

    /**
     * Shows the player's score at the end of the game and the goal to beat, telling him if he won.
     *
     * @param goal   is the score the player has to beat.
     * @param points is the player's actual score.
     */
    public void onGameEndSingle(int goal, int points) {
        // ping response to be considered connected
        //respondToPing();

        if (gameBoardHandlerSingle != null) {
            gameBoardHandlerSingle.showResultForSingle(goal, points);
        }
        stillPlaying = false;
    }

    /**
     * Called when a player is reconnected to a match. All his information about the game is updated.
     *
     * @param toolcards               of the match.
     * @param publicCards             of the match.
     * @param privateCards            of the player.
     * @param reserve                 of the match at the current state.
     * @param roundTrack              of the match at the current state.
     * @param myTokens                are the token of the player.
     * @param schemeCard              player's window pattern card.
     * @param schemeCardName          name of the player's window pattern card.
     * @param otherTokens             maps with opponents' names as key and their number of tokens as values.
     * @param otherSchemeCards        maps with opponents' names as key and theur window pattern cards as values.
     * @param otherSchemeCardNamesMap maps with opponents' names as key and window pattern cards names as values.
     * @param schemeCardChosen        is true if the reconnected player has already chosen his window pattern card, false otherwise.
     * @param toolcardsPrices         map with tool card names as keys and their price as value.
     */
    public void onAfterReconnection(String toolcards, String publicCards, List<String> privateCards, String reserve, String roundTrack, int myTokens, String[][] schemeCard, String schemeCardName, Map<String, Integer> otherTokens, Map<String, String[][]> otherSchemeCards, Map<String, String> otherSchemeCardNamesMap, boolean schemeCardChosen, Map<String, Integer> toolcardsPrices) {
        reconnection = true;
        this.myTokens = myTokens;
        parseToolcards(toolcards);
        parsePublicCards(publicCards);

        for (String c : privateCards) {
            this.privateCards.add(c.substring(7, c.length() - 1).toLowerCase());
        }
        track = roundTrack;
        onReserve(reserve);
        otherFavorTokensMap = otherTokens;
        otherSchemeCardsMap = otherSchemeCards;
        this.otherSchemeCardNamesMap = otherSchemeCardNamesMap;
        this.toolcardsPrices = toolcardsPrices;
        if (schemeCardChosen) {
            mySchemeCard = schemeCard;
            String s = schemeCardName.toLowerCase().replaceAll(" ", "_").replaceAll("'", "");
            playerSchemeCardImageURL = "/images/cards/window_pattern_cards/" + s + ".png";
        }
    }

    /**
     * Updates the round track
     *
     * @param track is a string representing the round track.
     */
    public void onRoundTrack(String track) {
        // ping response to prove that connection is on
        //respondToPing();

        if (single) {
            if (gameBoardHandlerSingle != null) {
                gameBoardHandlerSingle.onRoundTrack(track);
            }
        } else {
            if (gameBoardHandlerMulti != null) {
                gameBoardHandlerMulti.onRoundTrack(track);
            }
        }
    }

    /**
     * Updates the GUI owner's window pattern card.
     *
     * @param window is a bi-dimensional array of strings representing the player's window pattern card.
     */
    public void onMyWindow(String[][] window) {
        // ping response to prove that connection is on
        //respondToPing();

        if (single) {
            if (gameBoardHandlerSingle != null) {
                gameBoardHandlerSingle.setMyWindow(window);
            }
        } else {
            if (gameBoardHandlerMulti != null) {
                gameBoardHandlerMulti.setMyWindow(window);
            }
        }
    }

    /**
     * Updates the number of favor tokens of the GUI owner.
     *
     * @param value is the new number of favor tokens.
     */
    public void onMyFavorTokens(int value) {
        // ping response to prove that connection is on
        //respondToPing();

        if (gameBoardHandlerMulti != null) {
            gameBoardHandlerMulti.setMyFavourTokens(value);
        }
    }

    /**
     * Updates the favor tokens of a player's opponent.
     *
     * @param value is the new number of favor tokens.
     * @param name  is the opponent's name.
     */
    public void onOtherFavorTokens(int value, String name) {
        // ping response to prove that connection is on
        //respondToPing();

        otherFavorTokensMap.put(name, value);
        if (gameBoardHandlerMulti != null) {
            gameBoardHandlerMulti.onOtherFavorTokens(value, name);
        }
    }

    /**
     * Called when one of the player's opponent modifies his window pattern card.
     *
     * @param window   is the modified window pattern card.
     * @param name     is the name of the opponent who modified his window pattern card.
     * @param cardName is the window pattern card name.
     */
    public void onOtherSchemeCards(String[][] window, String name, String cardName) {
        // ping response to prove that connection is on
        //respondToPing();

        otherSchemeCardNamesMap.put(name, cardName);
        otherSchemeCardsMap.put(name, window);
        if (gameBoardHandlerMulti != null) gameBoardHandlerMulti.onOtherSchemeCards(window, name, cardName);
    }

    /**
     * Shows a message in the textArea of the game telling that is another's player's turn.
     *
     * @param name is the name of the player who is now playing.
     */
    public void onOtherTurn(String name) {
        // ping response to prove that connection is on
        //respondToPing();

        String s = "Ora è il turno di " + name + "!";
        if (gameBoardHandlerMulti != null) {
            gameBoardHandlerMulti.appendToTextArea(s);
        } else if (chooseCardHandlerMultiplayer != null) {
            chooseCardHandlerMultiplayer.appendToTextArea(s);
        }
    }

    /**
     * Initializes some game elements values.
     *
     * @param toolcards    is a string representing the tool cards available for the match.
     * @param publicCards  is a string representing the public objective cards available for the match.
     * @param privateCards is a list of the private objective cards available for the match.
     * @param players      is the list of players in te match.
     */
    public void onInitialization(String toolcards, String publicCards, List<String> privateCards, List<String> players) {
        // ping response to prove that connection is on
        //respondToPing();

        parseToolcards(toolcards);

        for (String c : privateCards) {
            if (c != null) {
                this.privateCards.add(c.substring(7, c.length() - 1).toLowerCase());
            }

        }

        parsePublicCards(publicCards);
        this.players = players;
    }

    /**
     * Parses the string passed as a parameter to get a list of the tool cards available for the match.
     *
     * @param toolcards is a string representing the tool cards available for the match.
     */
    private void parseToolcards(String toolcards) {
        String dicesString = toolcards.substring(1, toolcards.length() - 1);
        List<String> temp = Pattern.compile(", ")
                .splitAsStream(dicesString)
                .collect(Collectors.toList());
        for (String s : temp) {
            s = s.split(":", 2)[0];
            toolCardsList.add(s);
        }
    }

    /**
     * Parses the string passed as a parameter to get a list of the public objective cards available for the match.
     *
     * @param publicCards is a string representing the public objective cards available for the match.
     */
    private void parsePublicCards(String publicCards) {
        publicCardsList = new ArrayList<>();
        String cards = publicCards.substring(1, publicCards.length() - 1);
        List<String> temp = Pattern.compile(", ").splitAsStream(cards).collect(Collectors.toList());
        for (String s : temp) {
            s = s.substring(8).toLowerCase();
            s = s.split("\n", 2)[0];
            s = s.replaceAll(" - ", "_").replaceAll(" ", "_");
            publicCardsList.add(s);
        }
    }

    /**
     * Shows a message in the game board text area informing of the exit of a player.
     *
     * @param name of the player who left the match.
     */
    public void onPlayerExit(String name) {
        if (gameBoardHandlerMulti != null) {
            gameBoardHandlerMulti.appendToTextArea("Il giocatore " + name + " è uscito dalla partita!");
        } else {
            chooseCardHandlerMultiplayer.appendToTextArea("Il giocatore " + name + " è uscito dalla partita!");
        }
    }

    /**
     * Called when a player has to choose his window pattern card.
     * If it's a single player match, both private objective cards available are shown.
     * If it's a multi player match, only the first private objective card available is shown.
     *
     * @param windows is a list of the proposed window pattern cards.
     */
    public void onWindowChoice(List<String> windows) {
        // ping response to be considered connected
        //respondToPing();

        AudioClip cardsClip = Applet.newAudioClip(getClass().getResource("/sounds/cards.au"));
        cardsClip.play();

        if (single) {
            chooseCardHandlerSingle.setPrivateCard(privateCards.get(0), privateCards.get(1));
            chooseCardHandlerSingle.setWindows(windows);

        } else {
            chooseCardHandlerMultiplayer.setPrivateCard(privateCards.get(0));
            chooseCardHandlerMultiplayer.setWindows(windows);
        }
    }

    /**
     * Shows a message in the game board text area to inform that an opponent has used a tool card for the first time.
     *
     * @param name       of the opponent who used the tool card for the first time.
     * @param toolNumber is the number of the used tool card.
     */
    public void onToolCardUsedByOthers(String name, int toolNumber) {
        // ping response to be considered connected
        //respondToPing();

        if (gameBoardHandlerMulti != null) {
            gameBoardHandlerMulti.appendToTextArea("Il giocatore '" + name + "' è stato il primo ad utilizzare la carta utensile " + toolNumber + ", pertanto il suo prezzo di utilizzo diventa di 2 segnalini.");
        } else
            chooseCardHandlerMultiplayer.appendToTextArea("Il giocatore '" + name + "' è stato il primo ad utilizzare la carta utensile " + toolNumber + ", pertanto il suo prezzo di utilizzo diventa di 2 segnalini.");
    }

    /**
     * Called after the GUI owner has chosen his window pattern card in a multi player match.
     * He is redirected to the game board scene and all the scene elements are initialized.
     */
    public void onAfterWindowChoiceMultiplayer() {
        // ping response to be considered connected
        //respondToPing();

        AudioClip dicesClip = Applet.newAudioClip(getClass().getResource("/sounds/dices.au"));
        dicesClip.play();
        FXMLLoader fx = new FXMLLoader();
        fx.setLocation(getClass().getResource("/game-board-multi.fxml"));
        Parent root = null;
        try {
            root = fx.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (chooseCardHandlerMultiplayer != null) {
            playerSchemeCardImageURL = chooseCardHandlerMultiplayer.getImageUrl();
        }
        assert root != null;
        Scene scene = new Scene(root);
        gameBoardHandlerMulti = fx.getController();
        gameBoardHandlerMulti.init(scene, this);
        gameBoardHandlerMulti.setWindowPatternCardImg(playerSchemeCardImageURL);
        if (mySchemeCard != null) {
            gameBoardHandlerMulti.setMyWindow(mySchemeCard);
            gameBoardHandlerMulti.setMyFavourTokens(myTokens);
        }

        gameBoardHandlerMulti.setToolCards(toolCardsList);
        gameBoardHandlerMulti.setPrivateCard(privateCards.get(0));
        gameBoardHandlerMulti.setPublicCards(publicCardsList);
        gameBoardHandlerMulti.setReserve(dicesList);
        gameBoardHandlerMulti.onRoundTrack(track);
        if (!reconnection) {
            gameBoardHandlerMulti.appendToTextArea("Fai la tua prima mossa!");
        } else {
            gameBoardHandlerMulti.appendToTextArea("Aggiornamento prezzi carte utensili:        (se vuoto prezzi=1)");
            for (String toolcard : toolcardsPrices.keySet()) {
                gameBoardHandlerMulti.appendToTextArea("-" + toolcard + " " + toolcardsPrices.get(toolcard));
            }
        }
        gameBoardHandlerMulti.createLabelsMap();
        gameBoardHandlerMulti.createOtherLabelsList();
        gameBoardHandlerMulti.initializeLabels(players);

        //FOR SOCKET CONNECTION
        if (controllerSocket != null) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        gameBoardHandlerMulti.initializeFavorTokens(otherFavorTokensMap);
        gameBoardHandlerMulti.initializeSchemeCards(otherSchemeCardsMap, otherSchemeCardNamesMap);
    }

    /**
     * Shows a message in the text area of a multi player match game board, informing the player about the turn time.
     *
     * @param turnTime is the time limit for each turn.
     */
    private void notifyTimerMulti(int turnTime) {
        gameBoardHandlerMulti.appendToTextArea("Hai a disposizione " + turnTime / 1000 + " secondi ad ogni turno per giocare!");
    }

    /**
     * Called after the GUI owner has chosen his window pattern card in a single player match.
     * He is redirected to the game board scene and all the scene elements are initialized.
     */
    public void onAfterWindowChoiceSingleplayer() {
        // ping response to be considered connected
        //respondToPing();

        AudioClip dicesClip = Applet.newAudioClip(getClass().getResource("/sounds/dices.au"));
        dicesClip.play();
        FXMLLoader fx = new FXMLLoader();
        fx.setLocation(getClass().getResource("/game-board-single.fxml"));
        Parent root = null;
        try {
            root = fx.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (chooseCardHandlerSingle != null) {
            playerSchemeCardImageURL = chooseCardHandlerSingle.getImageUrl();
        }
        assert root != null;
        Scene scene = new Scene(root);
        gameBoardHandlerSingle = fx.getController();
        gameBoardHandlerSingle.init(scene, this);
        gameBoardHandlerSingle.setWindowPatternCardImg(playerSchemeCardImageURL);
        if (mySchemeCard != null) {
            gameBoardHandlerSingle.setMyWindow(mySchemeCard);
        }
        gameBoardHandlerSingle.setToolCards(toolCardsList);
        gameBoardHandlerSingle.setPrivateCards(privateCards.get(0), privateCards.get(1));
        gameBoardHandlerSingle.setPublicCards(publicCardsList);
        gameBoardHandlerSingle.setReserve(dicesList);
        gameBoardHandlerSingle.appendToTextArea("Fai la tua prima mossa!");
    }

    /**
     * Shows a message in the text area of a single player match game board, informing the player about the turn time.
     *
     * @param turnTime is the time limit for each turn.
     */
    private void notifyTimerSingle(int turnTime) {
        gameBoardHandlerSingle.appendToTextArea("Hai a disposizione " + turnTime / 1000 + " secondi ad ogni turno per giocare!");
    }

    /**
     * Called at the end of a single player match, when the player has to choose which of the two private objective cards
     * available he wants to use to calculate his final score.
     */
    public void onChoosePrivateCard() {
        // ping response to be considered connected
        //respondToPing();

        gameBoardHandlerSingle.choosePrivateCard();
    }

    /**
     * close the client if a in connection with server occurs
     */
    private void closingForDisconnection() {
        if (single) {
            if (chooseCardHandlerSingle != null) {
                chooseCardHandlerSingle.afterDisconnection();
            } else if (gameBoardHandlerSingle != null) {
                gameBoardHandlerSingle.afterDisconnection();
            }
        } else {
            if (chooseCardHandlerMultiplayer != null) {
                chooseCardHandlerMultiplayer.afterDisconnection();
            } else if (gameBoardHandlerMulti != null) {
                gameBoardHandlerMulti.afterDisconnection();
            }
        }
    }
}
