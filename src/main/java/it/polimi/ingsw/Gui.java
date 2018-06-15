package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.ClientController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Gui {

    private String username;
    private RemoteController controllerRmi;
    private ClientController controllerSocket;
    private Stage windowStage;
    private boolean stillPlaying;

    private boolean myTurn;
    private boolean reconnection;

    // todo: come facciamo per il nome? Si potrebbe creare un' interfaccia
    private ChooseCardHandlerMultiplayer chooseCardHandlerMultiplayer;
    private ChooseCardHandlerSingle chooseCardHandlerSingle;
    private GameBoardHandler gameBoardHandler;
    private String track;
    private List<String> toolCardsList;
    private List<String> dicesList;
    private List<String> privateCards;
    private List<String> publicCardsList;
    private Map<String, Integer> otherFavorTokensMap;
    private Map<String, WindowPatternCard> otherSchemeCardsMap;
    private List<String> players;
    private WindowPatternCard mySchemeCard;
    private int myTokens;
    private boolean dicePlaced;
    private Map<String, Integer> toolcardsPrices;

    private boolean windowChosen;
    private boolean single;

    private String playerSchemeCardImageURL;
    private AudioClip turnClip = new AudioClip("File:./src/main/java/it/polimi/ingsw/resources/turn.mp3");

    public Gui(Stage fromLogin, String username, RemoteController controllerRmi, ClientController controllerSocket, boolean single) {
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
        toolCardsList = new ArrayList<>();
        publicCardsList = new ArrayList<>();
        players = new ArrayList<>();
    }

    // getters
    public List<String> getToolCardsList() {
        return toolCardsList;
    }

    public RemoteController getControllerRmi() {
        return controllerRmi;
    }

    public ClientController getControllerSocket() {
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

    public Map<String, WindowPatternCard> getOtherSchemeCardsMap() {
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
            String s = "Ora è il tuo turno!\nRound: " + round + "\tTurno: " + turn;
            if (gameBoardHandler != null) {
                gameBoardHandler.appendToTextArea(s);
                gameBoardHandler.initializeActions();
            } else if (chooseCardHandlerMultiplayer != null) {
                chooseCardHandlerMultiplayer.appendToTextArea(s);
                chooseCardHandlerMultiplayer.setTurn(true);
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
        if (gameBoardHandler != null) {
            gameBoardHandler.setReserve(dicesList);
        }
    }

    public void onPlayerReconnection(String name) {
        if (gameBoardHandler != null) {
            gameBoardHandler.appendToTextArea("Il giocatore " + name + " è entrato nella partita!");
        } else if (chooseCardHandlerMultiplayer != null) {
            chooseCardHandlerMultiplayer.appendToTextArea("Il giocatore " + name + " è entrato nella partita!");
        }
    }

    public void onGameStarted(Boolean windowChosen, List<String> names) {
        players = names;

        if (windowChosen) {
            if (single) {
                onAfterWindowChoiceSingleplayer();
            } else {
                onAfterWindowChoiceMultiplayer(); // todo: controllare
            }
        } else {
            if (single) {
                initializeChooseCardSingle();
            } else {
                initializeChooseCardMulti();
            }
        }
    }

    private void initializeChooseCardSingle() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = null;
        try {
            fxmlLoader.setLocation(new URL("File:./src/main/java/it/polimi/ingsw/resources/choose-card-single.fxml"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chooseCardHandlerSingle = fxmlLoader.getController();
        Scene scene = new Scene(root);
        chooseCardHandlerSingle.init(windowStage, scene, controllerRmi, controllerSocket, username);
        chooseCardHandlerSingle.welcome();
    }

    private void initializeChooseCardMulti() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = null;
        try {
            fxmlLoader.setLocation(new URL("File:./src/main/java/it/polimi/ingsw/resources/choose-card-multiplayer.fxml"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chooseCardHandlerMultiplayer = fxmlLoader.getController();
        Scene scene = new Scene(root);
        chooseCardHandlerMultiplayer.init(windowStage, scene, controllerRmi, controllerSocket, username);
        chooseCardHandlerMultiplayer.welcome();
        chooseCardHandlerMultiplayer.showOpponents(players);
    }

    public void onGameClosing() {
        if (stillPlaying) {
            gameBoardHandler.onGameClosing();//da fare anche su choosecardhandler
        }
        stillPlaying = false;
    }

    public void onGameEnd(String winner, List<String> rankingNames, List<Integer> rankingValues) {
        if (gameBoardHandler != null) {
            gameBoardHandler.showRanking(winner, rankingNames, rankingValues);
        }
        stillPlaying = false;
    }

    // todo: eventualmente cambiare il gameboardhandler se ne creiamo uno per il singleplayer
    public void onGameEndSingle(int goal, int points) {
        if (gameBoardHandler != null) {
            gameBoardHandler.showResultForSingle(goal, points);
        }
        stillPlaying = false;
    }

    public void onAfterReconnection(String toolcards, String publicCards, List<String> privateCards, String reserve, String roundTrack, int myTokens, WindowPatternCard schemeCard, Map<String, Integer> otherTokens, Map<String, WindowPatternCard> otherSchemeCards, boolean schemeCardChosen, Map<String, Integer> toolcardsPrices) {
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
        this.toolcardsPrices = toolcardsPrices;
        if (schemeCardChosen) {
            mySchemeCard = schemeCard;
            String s = schemeCard.getName().toLowerCase().replaceAll(" ", "_").replaceAll("'", "");
            playerSchemeCardImageURL = "File:./src/main/java/it/polimi/ingsw/resources/window_pattern_card/" + s + ".png";
        }
    }

    public void onRoundTrack(String track) {
        if (gameBoardHandler != null) {
            gameBoardHandler.onRoundTrack(track);
        }
    }

    public void onMyWindow(WindowPatternCard window) {
        if (gameBoardHandler != null) {
            gameBoardHandler.setMyWindow(window);
        }
    }

    /* to update the owner's tokens*/
    public void onMyFavorTokens(int value) {
        if (gameBoardHandler != null) {
            gameBoardHandler.setFavourTokens(value);
        }
    }

    /* to update the others' tokens*/
    public void onOtherFavorTokens(int value, String name) {
        otherFavorTokensMap.put(name, value);
        if (gameBoardHandler != null) {
            gameBoardHandler.onOtherFavorTokens(value, name);
        }
    }

    public void onOtherSchemeCards(WindowPatternCard window, String name) {
        otherSchemeCardsMap.put(name, window);
        if (gameBoardHandler != null) gameBoardHandler.onOtherSchemeCards(window, name);
    }

    public void onOtherTurn(String name) {
        String s = "Ora è il turno di " + name + "!";
        if (gameBoardHandler != null) {
            gameBoardHandler.appendToTextArea(s);
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
            System.out.println("---------------------------------" + privateCards);
            System.out.println("---------------------------------" + this.privateCards);

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
        if (gameBoardHandler != null) {
            gameBoardHandler.appendToTextArea("Il giocatore " + name + " è uscito dalla partita!");
        } else {
            chooseCardHandlerMultiplayer.appendToTextArea("Il giocatore " + name + " è uscito dalla partita!");
        }
    }

    public void onWindowChoice(List<String> windows) {
        AudioClip cardsClip = new AudioClip("File:./src/main/java/it/polimi/ingsw/resources/cards.mp3");
        cardsClip.play();

        if (single) {
            chooseCardHandlerSingle.setPrivateCard(privateCards.get(0), privateCards.get(1));
            chooseCardHandlerSingle.setWindows(windows);

        } else {
            chooseCardHandlerMultiplayer.setPrivateCard(privateCards.get(0));
            chooseCardHandlerMultiplayer.setWindows(windows); // todo: ripetuto per lo stesso motivo del nome
        }
    }

    public void onToolCardUsedByOthers(String name, int toolNumber) {
        if (gameBoardHandler != null) {
            gameBoardHandler.appendToTextArea("Il giocatore '" + name + "' è stato il primo ad utilizzare la carta utensile " + toolNumber + ", pertanto il suo prezzo di utilizzo diventa di 2 segnalini.");
        } else
            chooseCardHandlerMultiplayer.appendToTextArea("Il giocatore '" + name + "' è stato il primo ad utilizzare la carta utensile " + toolNumber + ", pertanto il suo prezzo di utilizzo diventa di 2 segnalini.");
    }

    public void onAfterWindowChoiceMultiplayer() {

        AudioClip dicesClip = new AudioClip("File:./src/main/java/it/polimi/ingsw/resources/dices.mp3");
        dicesClip.play();
        FXMLLoader fx = new FXMLLoader();
        try {
            fx.setLocation(new URL("File:./src/main/java/it/polimi/ingsw/resources/game-board.fxml"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Parent root = null;
        try {
            root = fx.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (chooseCardHandlerMultiplayer != null) {
            playerSchemeCardImageURL = chooseCardHandlerMultiplayer.getImageUrl();
        }
        Scene scene = new Scene(root);
        gameBoardHandler = fx.getController();
        gameBoardHandler.init(scene, this);
        gameBoardHandler.setWindowPatternCardImg(playerSchemeCardImageURL);
        if (mySchemeCard != null) {
            gameBoardHandler.setMyWindow(mySchemeCard);
            gameBoardHandler.setFavourTokens(myTokens);
        }

        gameBoardHandler.setToolCards(toolCardsList);
        gameBoardHandler.setPrivateCard(privateCards.get(0));
        gameBoardHandler.setPublicCards(publicCardsList);
        gameBoardHandler.setReserve(dicesList);
        gameBoardHandler.onRoundTrack(track);
        if (!reconnection) {
            gameBoardHandler.appendToTextArea("Fai la tua prima mossa!");
        } else {
            gameBoardHandler.appendToTextArea("Aggiornamento prezzi carte utensili:        (se vuoto prezzi=1)");
            for (String toolcard : toolcardsPrices.keySet()) {
                gameBoardHandler.appendToTextArea("-" + toolcard + " " + toolcardsPrices.get(toolcard));
            }
        }
        gameBoardHandler.createLabelsMap();
        gameBoardHandler.createOtherLabelsList();
        gameBoardHandler.initializeLabels(players);


        //FOR SOCKET CONNECTION
        if (controllerRmi == null) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        gameBoardHandler.initializeFavorTokens(otherFavorTokensMap);
        gameBoardHandler.initializeSchemeCards(otherSchemeCardsMap);
    }

    public void onAfterWindowChoiceSingleplayer() {

    }
}
