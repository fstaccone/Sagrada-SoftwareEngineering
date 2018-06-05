package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.ClientController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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


    private boolean myTurn;
    private boolean reconnection;

    private ChooseCardHandler chooseCardHandler;
    private GameBoardHandler gameBoardHandler;

    private List<String> toolCardsList;
    private List<String> dicesList;
    private String privateCard;
    private List<String> publicCardsList;
    private Map<String, Integer> otherFavorTokensMap;
    private Map<String, WindowPatternCard> otherSchemeCardsMap;
    private List<String> players;
    private boolean dicePlaced;

    private boolean windowChosen;
    private boolean single;

    public Gui(Stage fromLogin, String username, RemoteController controllerRmi, ClientController controllerSocket, boolean single) {
        this.username = username;
        this.controllerRmi = controllerRmi;
        this.controllerSocket = controllerSocket;

        myTurn = false;
        windowChosen = false;
        this.single = single;

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

    public void onPlayers(List<String> playersNames) {
        players = playersNames;
    }

    public void onYourTurn(boolean isMyTurn, String string) {
        if (string != null) {
            onReserve(string);
        }
        this.myTurn = isMyTurn;
        if (myTurn) {
            //Solo per verifica
            String s = "Ora è il tuo turno!";
            if (gameBoardHandler != null) {
                gameBoardHandler.setTextArea(s);
                gameBoardHandler.initializeActions();
            } else if (chooseCardHandler != null) {
                chooseCardHandler.setTextArea(s);
            }
        } else
            //Solo per verifica
            gameBoardHandler.setTextArea("Non è il tuo turno!");
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
            gameBoardHandler.setTextArea("Il giocatore " + name + " è entrato nella partita!");
        } else {
            chooseCardHandler.setTextArea("Il giocatore " + name + " è entrato nella partita!");
        }
    }

    public void onGameStarted(Boolean windowChosen){
        if(windowChosen){
            // direttamente alla schermata di gioco
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                fxmlLoader.setLocation(new URL("File:./src/main/java/it/polimi/ingsw/resources/choose-card.fxml"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            chooseCardHandler = fxmlLoader.getController();
            Scene scene = new Scene(root);
            chooseCardHandler.init(windowStage, scene, controllerRmi, controllerSocket, username);
            chooseCardHandler.setOpponents(players);
            //chooseCardHandler.setPrivateCard(privateCard);
        }
    }

    public void onGameClosing() {
        gameBoardHandler.onGameClosing();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        windowStage.close();
        System.exit(0);
    }

    public void onGameEnd(String winner, List<String> rankingNames, List<Integer> rankingValues) {
        if (gameBoardHandler != null) {
            gameBoardHandler.showRanking(winner, rankingNames, rankingValues);
        }
    }

    public void onAfterReconnection(String toolcards, String publicCards, String privateCard, String reserve, String roundTrack, int myTokens, WindowPatternCard schemeCard, Map<String, Integer> otherTokens, Map<String, WindowPatternCard> otherSchemeCards, boolean schemeCardChosen) {
        parseToolcards(toolcards);
        privateCard = privateCard.substring(7, privateCard.length() - 1).toLowerCase();
        this.privateCard = privateCard;
        parsePublicCards(publicCards);
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
        String s = "Ora è il turno di " + name;
        if (gameBoardHandler != null) {
            gameBoardHandler.setTextArea(s);
        } else if (chooseCardHandler != null) {
            chooseCardHandler.setTextArea(s);
        }
    }


    public void onInitialization(String toolcards, String publicCards, String privateCard, List<String> players) {
        parseToolcards(toolcards);
        privateCard = privateCard.substring(7, privateCard.length() - 1).toLowerCase();
        this.privateCard = privateCard;
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
            gameBoardHandler.setTextArea("Player " + name + " has left the game!");
        } else {
            chooseCardHandler.setTextArea("Player " + name + " has left the game!");
        }
    }

    public void onWindowChoise(List<String> windows) {
        chooseCardHandler.setWindows(windows);
    }

    public void onAfterWindowChoise() {

        String imgUrl = chooseCardHandler.getImageUrl();
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

        Scene scene = new Scene(root);
        gameBoardHandler = fx.getController();
        gameBoardHandler.init(scene, this);
        gameBoardHandler.setWindowPatternCardImg(imgUrl);
        gameBoardHandler.setToolCards(toolCardsList);
        gameBoardHandler.setReserve(dicesList);
        gameBoardHandler.setTextArea("Now it's your turn!");
        gameBoardHandler.setPrivateCard(privateCard);
        gameBoardHandler.setPublicCards(publicCardsList);
        gameBoardHandler.createLabelsMap();
        gameBoardHandler.createOtherLabelsList();
        gameBoardHandler.initializeLabels(players);
        //FOR SOCKET CONNECTION
        if (controllerRmi == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        gameBoardHandler.initializeFavorTokens(otherFavorTokensMap);
        gameBoardHandler.initializeSchemeCards(otherSchemeCardsMap);
    }
}
