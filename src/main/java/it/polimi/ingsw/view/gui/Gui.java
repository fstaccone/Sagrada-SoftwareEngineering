package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.control.SocketController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
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
    private boolean dicePlaced;
    private Map<String, Integer> toolcardsPrices;

    private boolean windowChosen;
    private boolean single;

    private String playerSchemeCardImageURL;
    private AudioClip turnClip = Applet.newAudioClip(getClass().getResource("/sounds/turn.mp3"));

    public Gui(Stage fromLogin, String username, RemoteController controllerRmi, SocketController controllerSocket, boolean single) {
        this.username = username;
        this.controllerRmi = controllerRmi;
        this.controllerSocket = controllerSocket;
        privateCards = new ArrayList<>();
        myTurn = false;
        windowChosen = false;
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

    public List<String> getToolCardsList() {
        return toolCardsList;
    }

    public RemoteController getControllerRmi() {
        return controllerRmi;
    }

    public SocketController getControllerSocket() {
        return controllerSocket;
    }

    public Stage getWindowStage() {
        return windowStage;
    }

    public Boolean isMyTurn() {
        return myTurn;
    }

    public List<String> getDicesList() {
        return dicesList;
    }

    public Map<String, String[][]> getOtherSchemeCardsMap() {
        return otherSchemeCardsMap;
    }

    public List<String> getPlayers() {
        return players;
    }

    public String getUsername() {
        return username;
    }

    // setters
    public void setDicePlaced(boolean dicePlaced) {
        this.dicePlaced = dicePlaced;
    }

    public void onYourTurn(boolean isMyTurn, String string, int round, int turn) {
        if (string != null) {
            onReserve(string);
        }
        this.myTurn = isMyTurn;
        if (myTurn) {
            //Solo per verifica
            turnClip.play();
            String multi = "Ora è il tuo turno!\nRound: " + round + "\tTurno: " + turn;
            String single = "Round: " + round + "\tTurno: " + turn;
            if (gameBoardHandlerMulti != null) {
                gameBoardHandlerMulti.resetToolValues();
                gameBoardHandlerMulti.appendToTextArea(multi);
                gameBoardHandlerMulti.initializeActions();
            } else if (chooseCardHandlerMultiplayer != null) {
                chooseCardHandlerMultiplayer.appendToTextArea(multi);
                chooseCardHandlerMultiplayer.setTurn(true);
            } else if (gameBoardHandlerSingle != null) {
                gameBoardHandlerSingle.resetToolValues();
                gameBoardHandlerSingle.appendToTextArea(single);
            }
        } else {
            if (chooseCardHandlerMultiplayer != null) {
                chooseCardHandlerMultiplayer.setTurn(false);
            }
        }
    }

    public void onReserve(String string) {
        dicesList = new ArrayList<>();
        System.out.println("On reserve");
        String dicesString = string.substring(1, string.length() - 1);
        List<String> temp = Pattern.compile(", ")
                .splitAsStream(dicesString)
                .collect(Collectors.toList());
        for (String s : temp) {
            s = s.substring(1, s.length() - 1).toLowerCase().replaceAll(" ", "_");
            dicesList.add(s);
        }
        for (String dice : dicesList) System.out.println(dice);
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

    public void onPlayerReconnection(String name) {
        if (gameBoardHandlerMulti != null) {
            gameBoardHandlerMulti.appendToTextArea("Il giocatore " + name + " è entrato nella partita!");
        } else if (chooseCardHandlerMultiplayer != null) {
            chooseCardHandlerMultiplayer.appendToTextArea("Il giocatore " + name + " è entrato nella partita!");
        }
    }

    public void onGameStarted(Boolean windowChosen, List<String> names, int turnTime) {
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
        chooseCardHandlerMultiplayer.appendToTextArea("Hai a disposizione " + turnTime/1000 + " secondi ad ogni turno per giocare!");
    }

    public void onGameClosing() {
        if (stillPlaying) {
            if (gameBoardHandlerMulti != null) {
                gameBoardHandlerMulti.onGameClosing();
            } else if (chooseCardHandlerMultiplayer != null) {
                chooseCardHandlerMultiplayer.onGameClosing();
            }
        }
        stillPlaying = false;
    }

    public void onGameEndMulti(String winner, List<String> rankingNames, List<Integer> rankingValues) {
        if (gameBoardHandlerMulti != null) {
            gameBoardHandlerMulti.showRanking(winner, rankingNames, rankingValues);
        }
        stillPlaying = false;
    }

    public void onGameEndSingle(int goal, int points) {
        if (gameBoardHandlerSingle != null) {
            gameBoardHandlerSingle.showResultForSingle(goal, points);
        }
        stillPlaying = false;
    }

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
            playerSchemeCardImageURL = "File:./src/main/java/it/polimi/ingsw/resources/images/cards/window_pattern_cards/" + s + ".png";
        }
    }

    public void onRoundTrack(String track) {
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

    public void onMyWindow(String[][] window) {
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

    /* to update the owner's tokens*/
    public void onMyFavorTokens(int value) {
        if (gameBoardHandlerMulti != null) {
            gameBoardHandlerMulti.setFavourTokens(value);
        }
    }

    /* to update the others' tokens*/
    public void onOtherFavorTokens(int value, String name) {
        otherFavorTokensMap.put(name, value);
        if (gameBoardHandlerMulti != null) {
            gameBoardHandlerMulti.onOtherFavorTokens(value, name);
        }
    }

    public void onOtherSchemeCards(String[][] window, String name, String cardName) {
        otherSchemeCardNamesMap.put(name, cardName);
        otherSchemeCardsMap.put(name, window);
        if (gameBoardHandlerMulti != null) gameBoardHandlerMulti.onOtherSchemeCards(window, name, cardName);
    }

    public void onOtherTurn(String name) {
        String s = "Ora è il turno di " + name + "!";
        if (gameBoardHandlerMulti != null) {
            gameBoardHandlerMulti.appendToTextArea(s);
        } else if (chooseCardHandlerMultiplayer != null) {
            chooseCardHandlerMultiplayer.appendToTextArea(s);
        }
    }

    public void onInitialization(String toolcards, String publicCards, List<String> privateCards, List<String> players) {
        parseToolcards(toolcards);

        for (String c : privateCards) {
            if (c != null) {
                this.privateCards.add(c.substring(7, c.length() - 1).toLowerCase());
            }

        }

        parsePublicCards(publicCards);
        this.players = players;
    }

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

    public void onPlayerExit(String name) {
        System.out.println("On player exit");
        if (gameBoardHandlerMulti != null) {
            gameBoardHandlerMulti.appendToTextArea("Il giocatore " + name + " è uscito dalla partita!");
        } else {
            chooseCardHandlerMultiplayer.appendToTextArea("Il giocatore " + name + " è uscito dalla partita!");
        }
    }

    public void onWindowChoice(List<String> windows) {
        AudioClip cardsClip = Applet.newAudioClip(getClass().getResource("/sounds/cards.mp3"));
        cardsClip.play();

        if (single) {
            chooseCardHandlerSingle.setPrivateCard(privateCards.get(0), privateCards.get(1));
            chooseCardHandlerSingle.setWindows(windows);

        } else {
            chooseCardHandlerMultiplayer.setPrivateCard(privateCards.get(0));
            chooseCardHandlerMultiplayer.setWindows(windows);
        }
    }

    public void onToolCardUsedByOthers(String name, int toolNumber) {
        if (gameBoardHandlerMulti != null) {
            gameBoardHandlerMulti.appendToTextArea("Il giocatore '" + name + "' è stato il primo ad utilizzare la carta utensile " + toolNumber + ", pertanto il suo prezzo di utilizzo diventa di 2 segnalini.");
        } else
            chooseCardHandlerMultiplayer.appendToTextArea("Il giocatore '" + name + "' è stato il primo ad utilizzare la carta utensile " + toolNumber + ", pertanto il suo prezzo di utilizzo diventa di 2 segnalini.");
    }

    public void onAfterWindowChoiceMultiplayer() {

        AudioClip dicesClip = Applet.newAudioClip(getClass().getResource("/sounds/dices.mp3"));
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
            gameBoardHandlerMulti.setFavourTokens(myTokens);
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

    private void notifyTimerMulti(int turnTime){
        gameBoardHandlerMulti.appendToTextArea("Hai a disposizione " + turnTime/1000 + " secondi ad ogni turno per giocare!");
    }

    public void onAfterWindowChoiceSingleplayer() {

        AudioClip dicesClip = Applet.newAudioClip(getClass().getResource("/sounds/dices.mp3"));
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

    private void notifyTimerSingle(int turnTime){
        gameBoardHandlerSingle.appendToTextArea("Hai a disposizione " + turnTime/1000 + " secondi ad ogni turno per giocare!");

    }

    public void onChoosePrivateCard() {
        gameBoardHandlerSingle.choosePrivateCard();
    }
}