package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.ClientController;
import it.polimi.ingsw.socket.requests.*;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Cli {

    private String username;
    private transient ClientController controllerSocket;
    private transient RemoteController controllerRmi;
    private boolean myTurn;
    private final transient PrintWriter printer;

    private List<String> players;
    private List<String> dicesList;
    private List<String> toolCardsList;
    private List<String> publicCardsList;
    private List<ToolCommand> toolCommands;
    private String privateCard;
    private WindowPatternCard mySchemeCard;

    private int myFavorTokens;
    private Map<String, Integer> otherFavorTokensMap;
    private Map<String, WindowPatternCard> otherSchemeCardsMap;
    private String roundTrack;

    private int diceChosen = 9;
    private int coordinateX;
    private int coordinateY;
    private boolean diceValueToBeSet;
    private boolean tool11DiceToBePlaced;

    private List<String> playersNames;

    private boolean windowChosen;

    private boolean single;


    private static final String WELCOME = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@./@@@@@/*@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@./@@@@@/,@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@..@@@@@..@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@.@@&..@@@@@..&@@.@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@.@@(..@@@@@..(@@.@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%.*@,..&@ @&..,@*.%@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@,. @...%&.@%...@ .,@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ..@.. ....  ..@.. @@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@... .#@% . %@&.....@@@@@@@@@@\n" +
            "@@/ ____|@@/\\@@@/ ____|  __ \\@@@@@/\\@@@|  __ \\@@@/\\@@@@@@@@@@@@@@@ .. .(*.....*@. ...@@@@@@@@@@\n" +
            "@| (___@@@/  \\@| |@@__| |__) |@@@/  \\@@| |@@| |@/  \\@@@@@@@@@@@@@@. , . ../@ .... , .@@@@@@@@@@\n" +
            "@@\\___ \\@/ /\\ \\| |@|_ |  _  /@@@/ /\\ \\@| |@@| |/ /\\ \\@@@@@@@@@@@@@, ,....@ .#....., ,@@@@@@@@@@\n" +
            "@@____) / ____ \\ |__| | |@\\ \\@@/ ____ \\| |__| / ____ \\@@@@@@@@@@@(. *...( ...% ...* .(@@@@@@@@@\n" +
            "@|_____/_/@@@@\\_\\_____|_|@@\\_\\/_/@@@@\\_\\_____/_/@@@@\\_\\@@@@@@@@@@..@.@  &.... #..@.@..@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ , .&.*.%&.@ @ .&.., @@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@.&..#.%.@@,@/&..#..&.@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "\nBenvenuto in questo fantastico gioco, ";

    private static final String RULES = ("Da decidere se in italiano o in inglese");

    private static final String HELP_IN_TURN_MULTI = (
            "\n 'cd' + 'number'                             to choose the dice from the Reserve" +
                    "\n 'cw' + 'number'                             to choose tour window pattern card (available once only, at the beginning of the match)" +
                    "\n 'pd' + 'coordinate x' + 'coordinate y'      to place the chosen dice in your Scheme Card " +
                    "\n 'pass'                                      to pass the turn to the next player " +
                    "\n 'reserve'                                   to show current state of reserve, (available only from the beginning of the first turn)" +
                    "\n 'usetool' + 'number'                        to use the effect of the tool card [number]");

    private static final String HELP_GENERAL_MULTI = (
            "\n 'h'                                         to show game available commands" +
                    "\n 'q'                                         to quit the game" +
                    "\n 'r'                                         to show game rules" +
                    "\n 'sp'                                        to show all opponents' names" +
                    "\n 'track'                                     to show current state of the round track" +
                    "\n 'private'                                   to show your private objective card" +
                    "\n 'public'                                    to show public objective cards" +
                    "\n 'sw' + 'name'                               to show the window pattern card of player [name]" +
                    "\n 'tool' + 'number'                           to show the description of the tool card [number] " +
                    "\n 'toolcards'                                 to show the list of available tool cards \n");

    private static final String HELP_SINGLE = (
            "\n 'cd' + 'number'                             to choose the dice from the Reserve" +
                    "\n 'cw' + 'number'                             to choose tour window pattern card (available once only, at the beginning of the match)" +
                    "\n 'pd' + 'coordinate x' + 'coordinate y'      to place the chosen dice in your Scheme Card " +
                    "\n 'pass'                                      to pass the turn" +
                    "\n 'private'                                   to show your private objective cards" +
                    "\n 'public'                                    to show public objective cards" +
                    "\n 'q'                                         to quit the game" +
                    "\n 'r'                                         to show game rules" +
                    "\n 'reserve'                                   to show current state of reserve, (available only from the beginning of the first turn)" +
                    "\n 'track'                                     to show current state of the round track" +
                    "\n 'tool' + 'number'                           to show the description of the tool card [number] " +
                    "\n 'toolcards'                                 to show the list of available tool cards \n" +
                    "\n 'usetool' + 'number'                        to use the effect of the tool card [number]");

    private static final String SYNTAX_ERROR = (
            "\nWARNING: Invalid syntax request.\n");

    private static final String GAME_ERROR = (
            "\nWARNING: Invalid game request. You did not respect the tool card's rules!\n"); //STAMPATO ANCHE SE PROVI A MODIFICARE DADO NELLA RISERVA OUT OF BOUND , si può migliorare


    public Cli(String username, RemoteController controllerRmi, ClientController controllerSocket, boolean single) {
        this.username = username;
        this.controllerRmi = controllerRmi;
        this.controllerSocket = controllerSocket;
        printer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(FileDescriptor.out)));
        windowChosen = false;
        otherFavorTokensMap = new HashMap<>();
        otherSchemeCardsMap = new HashMap<>();
        myTurn = false;
        new KeyboardHandler().start();
        this.single = single;
        toolCommands = new ArrayList<>();
        toolCardsList = new ArrayList<>();
        publicCardsList = new ArrayList<>();
        players = new ArrayList<>();
        diceValueToBeSet = false;
        tool11DiceToBePlaced = false;
    }

    public void printWelcome() {
        printer.println(WELCOME + username + "!\n");
    }

    public String getUsername() {
        return username;
    }

    public void onMyFavorTokens(int value) {
        this.myFavorTokens = value;
    }

    public void onOtherFavorTokens(int value, String name) {
        this.otherFavorTokensMap.put(name, value);
    }

    public void onOtherSchemeCards(WindowPatternCard scheme, String name) {
        this.otherSchemeCardsMap.put(name, scheme);
    }

    public void onGameStarted(List<String> names) {
        playersNames = names;

        printer.println("Stai giocando contro:\n");
        printNames();
    }

    private void printNames() {
        for (String name : playersNames) {
            if (!name.equals(username)) {
                printer.println("-" + name);
            }
        }
        printer.println();
        printer.flush();
    }

    public void onRoundTrack(String roundTrack) {
        this.roundTrack = roundTrack;
    }

    public void onYourTurn(boolean yourTurn, String string) {
        if (string != null)
            onReserve(string);
        this.myTurn = yourTurn;
        if (myTurn) {
            printer.println("\nOra è il tuo turno! Inserisci un comando:                            ~ ['h' for help]\n");
            tool11DiceToBePlaced = false;
            diceValueToBeSet = false;
        } else
            printer.println("\nNon è più il tuo turno! (h for help)");
        printer.flush();
    }

    public void onReserve(String string) {
        String dicesString = string.substring(1, string.length() - 1);
        dicesList = Pattern.compile(", ")
                .splitAsStream(dicesString)
                .collect(Collectors.toList());
    }

    public void onWindowChoice(List<String> windows) {
        int i = 0;
        printer.println("Choose your window among the following                                        ~ [cw] + [number]\n");
        printer.flush();
        for (String s : windows) {
            printer.println(i++ + ") " + s + "\n");
            printer.flush();
        }
    }

    public void onAfterWindowChoice() {
        printer.println("You can now perform actions on your scheme card                               ~ [reserve] to check available dices\n");
        printer.flush();
    }

    public void onMyWindow(WindowPatternCard window) {
        this.mySchemeCard = window;
    }

    public void onOtherTurn(String name) {
        printer.println("Ora è il turno di " + name + "!");
        printer.flush();
    }

    public void onInitialization(String toolcards, String publicCards, String privateCard, List<String> players) {
        parseToolcards(toolcards);
        parsePublicCards(publicCards);
        this.privateCard = privateCard;
        this.players = players;
    }

    private void parsePublicCards(String publicCards) {
        String cards = publicCards.substring(1, publicCards.length() - 1);
        publicCardsList = Pattern.compile(", ").splitAsStream(cards).collect(Collectors.toList());


    }

    private void parseToolcards(String toolcards) {
        String cards = toolcards.substring(1, toolcards.length() - 1);

        toolCardsList = Pattern.compile(", ")
                .splitAsStream(cards)
                .collect(Collectors.toList());

        for (String card : toolCardsList) {
            String[] strings = card.split(":");

            int i = Integer.parseInt(strings[0].replaceAll("tool", ""));
            this.toolCommands.add(new ToolCommand(i, this.printer, this.controllerRmi, this.controllerSocket, this.username, this.single));
        }
    }

    public void onPlayerExit(String name) {
        printer.println("Player " + name + " has left the game!");
        printer.flush();
    }

    public void onPlayerReconnection(String name) {
        printer.println("Player " + name + " is now in game!");
        printer.flush();
    }


    public void onGameClosing() {
        printer.println("Congratulations! You are the winner. You were the only one still in game.");
        printer.flush();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        System.exit(0);
    }

    public void showPrivateCard() {
        printer.println("Your private objective card:");
        printer.println(privateCard);
        printer.flush();
    }

    public void showPublicCards() {
        printer.println("Public objective cards:");

        for (String s : publicCardsList) {
            printer.println(s);
        }
        printer.flush();
    }

    public void showToolCards() {
        printer.println("Tool cards:");

        for (String s : toolCardsList) {
            printer.println("- " + s);
        }

        printer.flush();
    }

    public void onAfterReconnection(String toolcards, String publicCards, String privateCard, String reserve, String roundTrack, int myTokens, WindowPatternCard mySchemeCard, Map<String, Integer> otherTokens, Map<String, WindowPatternCard> otherSchemeCards, boolean schemeCardChosen, Map<String, Integer> toolcardsPrices) {
        parseToolcards(toolcards);
        parsePublicCards(publicCards);
        this.privateCard = privateCard;
        String dicesString = reserve.substring(1, reserve.length() - 1);
        this.dicesList = Pattern.compile(", ")
                .splitAsStream(dicesString)
                .collect(Collectors.toList());
        this.roundTrack = roundTrack;
        this.mySchemeCard = mySchemeCard;
        this.myFavorTokens = myTokens;
        this.otherSchemeCardsMap = otherSchemeCards;
        this.otherFavorTokensMap = otherTokens;
        this.windowChosen = schemeCardChosen;
        printer.println("Aggiornamento prezzi carte utensili:        (se vuoto prezzi=1)");
        for (String toolcard : toolcardsPrices.keySet()) {
            printer.println("-" + toolcard + " " + toolcardsPrices.get(toolcard));
        }
        printer.flush();
    }


    public void showFavorTokens() {
        printer.println("\nAl momento hai " + myFavorTokens + " segnalini.\n");
        printer.flush();
    }

    public void showMySchemeCard() {
        printer.println("\nLa tua carta schema è: " + mySchemeCard + "\n");
        printer.flush();
    }

    public void onGameEnd(String winner, List<String> rankingNames, List<Integer> rankingValues) {
        printer.println("Punteggio finale:");
        for (int i = 0; i < rankingNames.size(); i++) {
            printer.println("- " + rankingNames.get(i) + "\t" + rankingValues.get(i));
        }
        printer.println();

        if (winner.equals(username)) {
            printer.println("Complimenti! Sei tu il vincitore.");
        } else {
            printer.println(winner.toUpperCase() + " è il vincitore!");
        }
        printer.flush();
    }

    public void onToolCardUsedByOthers(String name, int toolCardNumber) {
        printer.println("Il giocatore '" + name + "' è stato il primo ad utilizzare la carta utensile " + toolCardNumber + ", pertanto il suo prezzo di utilizzo diventa di 2 segnalini.");
        printer.flush();
    }

    public void onGameEndSingle(int goal, int points) {
        printer.println("Obiettivo da battere: \t" + goal);
        printer.println("Punteggio ottenuto: \t" + points);

        if (points > goal) {
            printer.println("Complimenti, hai vinto!");
        } else {
            printer.println("Non hai vinto!");
        }

        printer.flush();
    }

    private class KeyboardHandler extends Thread {
        String parts[];
        Integer toolNumber1;
        Integer toolNumber2;
        Integer toolNumber3;
        Integer toolNumber4;
        Integer toolNumber5;
        Integer toolNumber6;
        Integer toolNumber7;
        Integer toolNumber8;
        Integer toolNumber9;
        Integer toolNumber10;
        String toolString1;

        @Override
        public void run() {
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            while (true) {

                try {
                    String command = keyboard.readLine();
                    parts = command.split(" +");
                    if (myTurn) {
                        switch (parts[0]) {

                            case "cd": {

                                if (windowChosenCheck(windowChosen) && !diceValueToBeSet && !tool11DiceToBePlaced) {
                                    if (parametersCardinalityCheck(2)) {
                                        toolNumber1 = tryParse(parts[1]);
                                        if (toolNumber1 != null) {
                                            if (Integer.parseInt(parts[1]) < dicesList.size()) {
                                                diceChosen = Integer.parseInt(parts[1]);
                                                printer.println("\nYou have chosen the dice: " + dicesList.toArray()[diceChosen].toString() + "\n");
                                                printer.flush();
                                            } else {
                                                printer.println("\nThe dice you are trying to use does not exist, please retry ");
                                                printer.flush();
                                            }
                                        } else {
                                            syntaxErrorPrint();
                                        }
                                        toolNumber1 = null;
                                    }
                                }
                            }
                            break;

                            case "cw": {
                                if (!windowChosen && !diceValueToBeSet && !tool11DiceToBePlaced) {
                                    if (parametersCardinalityCheck(2)) {
                                        toolNumber1 = tryParse(parts[1]);
                                        if (toolNumber1 != null) {
                                            if (Integer.parseInt(parts[1]) < 4) {
                                                //RMI
                                                if (controllerRmi != null)
                                                    controllerRmi.chooseWindow(username, toolNumber1, single);
                                                    //SOCKET
                                                else
                                                    controllerSocket.request(new ChooseWindowRequest(username, toolNumber1, false));
                                                printer.println("Carta scelta correttamente!");
                                                printer.flush();
                                                windowChosen = true;
                                            } else {
                                                printer.println("\nATTENZIONE: La carta schema che stai cercando di scegliere non esiste!");
                                                printer.flush();
                                            }
                                        } else {
                                            syntaxErrorPrint();
                                        }
                                        toolNumber1 = null;
                                    }
                                } else {
                                    printer.println("\nATTENZIONE: Hai già scelto la tua carta schema!");
                                    printer.flush();
                                }
                            }
                            break;

                            case "h": {
                                if(single){
                                    printer.println("\nInserisci un comando valido tra i seguenti('+' means SPACE)" + HELP_SINGLE);
                                }else {
                                    printer.println("\nInserisci un comando valido tra i seguenti('+' means SPACE)" + HELP_IN_TURN_MULTI + HELP_GENERAL_MULTI);
                                }
                                printer.flush();
                            }
                            break;

                            case "otherschemecards": {
                                printer.println(otherSchemeCardsMap.toString());
                                printer.flush();
                            }
                            break;

                            case "othertokens": {
                                printer.println(otherFavorTokensMap.toString());
                                printer.flush();
                            }
                            break;

                            case "pass": {
                                if (windowChosenCheck(windowChosen) && diceValueToBeSetCheck(diceValueToBeSet) && tool11DiceToBePlacedCheck(tool11DiceToBePlaced)) {
                                    //RMI
                                    if (controllerRmi != null)
                                        controllerRmi.goThrough(username, single);
                                        //SOCKET
                                    else
                                        controllerSocket.request(new GoThroughRequest(username, single));
                                }
                            }
                            break;

                            case "pd": {
                                if (windowChosenCheck(windowChosen) && diceValueToBeSetCheck(diceValueToBeSet) && tool11DiceToBePlacedCheck(tool11DiceToBePlaced)) {
                                    if (diceChosen != 9) {
                                        if (parametersCardinalityCheck(3)) {
                                            toolNumber1 = tryParse(parts[1]);
                                            toolNumber2 = tryParse(parts[2]);
                                            if (toolNumber1 != null && toolNumber2 != null) {
                                                if (Integer.parseInt(parts[1]) < 4 && Integer.parseInt(parts[2]) < 5) {
                                                    coordinateX = Integer.parseInt(parts[1]);
                                                    coordinateY = Integer.parseInt(parts[2]);
                                                    printer.println("\nHai scelto di posizionare il dado nella posizione [" + coordinateX + "][" + coordinateY + "] della tua carta schema");
                                                    printer.flush();

                                                    //RMI
                                                    if (controllerRmi != null) {
                                                        if (controllerRmi.placeDice(diceChosen, coordinateX, coordinateY, username, single)) {
                                                            printer.println("\nWell done! The chosen dice has been placed correctly.\n");
                                                            printer.flush();
                                                            diceChosen = 9; //FIRST VALUE NEVER PRESENT IN THE RESERVE
                                                        } else {
                                                            printer.println("\nWARNING: You tried to place a dice where you shouldn't, or you are trying to place a second dice in your turn!");
                                                            printer.flush();

                                                        }
                                                    }

                                                    //SOCKET
                                                    else {
                                                        controllerSocket.request(new PlaceDiceRequest(diceChosen, coordinateX, coordinateY, username, single));
                                                        try {
                                                            Thread.sleep(500);
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                            Thread.currentThread().interrupt();
                                                        }
                                                        if (controllerSocket.isDicePlaced()) {
                                                            controllerSocket.setDicePlaced(false);//to reset the value
                                                            printer.println("\nWell done! The chosen dice has been placed correctly.\n");
                                                            printer.flush();
                                                        } else {
                                                            printer.println("\nWARNING:You tried to place a dice when you shouldn't!");
                                                            printer.flush();
                                                        }
                                                    }

                                                } else {
                                                    printer.println("\nWARNING: The coordinates of your scheme card are out of bounds, please retry ");
                                                    printer.flush();
                                                }
                                            } else {
                                                syntaxErrorPrint();
                                            }
                                        }
                                        toolNumber1 = null;
                                        toolNumber2 = null;
                                    } else {
                                        printer.println("\nWARNING: You have to choose a dice before trying to place one! ");
                                        printer.flush();
                                    }
                                }
                            }
                            break;

                            case "pd11": {
                                if (windowChosenCheck(windowChosen) && !diceValueToBeSet && tool11DiceToBePlaced) {
                                    if (parametersCardinalityCheck(3)) {
                                        toolNumber1 = tryParse(parts[1]);
                                        toolNumber2 = tryParse(parts[2]);
                                        if (toolNumber1 != null && toolNumber2 != null) {
                                            if (Integer.parseInt(parts[1]) < 4 && Integer.parseInt(parts[2]) < 5) {
                                                coordinateX = Integer.parseInt(parts[1]);
                                                coordinateY = Integer.parseInt(parts[2]);
                                                printer.println("\nHai scelto di posizionare il dado nella posizione [" + coordinateX + "][" + coordinateY + "] della tua carta schema");
                                                printer.flush();
                                                if (controllerRmi != null) {
                                                    if (controllerRmi.placeDiceTool11(coordinateX, coordinateY, username, single)) {
                                                        printer.println("\nWell done! The chosen dice has been placed correctly.\n");
                                                        printer.flush();
                                                    } else {
                                                        printer.println("\nWARNING: You tried to place a dice where you shouldn't, or you are trying to place a second dice in your turn!");
                                                        printer.flush();

                                                    }
                                                }

                                                //SOCKET
                                                else {
                                                    controllerSocket.request(new PlaceDiceTool11Request(coordinateX, coordinateY, username, single));
                                                    try {
                                                        Thread.sleep(500);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                        Thread.currentThread().interrupt();
                                                    }
                                                    if (controllerSocket.isDicePlaced()) {
                                                        controllerSocket.setDicePlaced(false);//to reset the value
                                                        printer.println("\nBen fatto! Il dado è stato piazzato correttamente!\n");
                                                        printer.flush();
                                                    } else {
                                                        printer.println("\nATTENZIONE! Hai provato a piazzare un dado dove non puoi!");
                                                        printer.flush();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;

                            case "private": {
                                showPrivateCard();//DA SISTEMARE CON SINGLEPLAYER CHE NE HA 2
                            }
                            break;

                            case "public": {
                                showPublicCards();
                            }
                            break;

                            case "q": {
                                quit();
                            }
                            break;

                            case "r": {
                                printer.println("\nRegole: da scrivere");
                                printer.flush();
                            }
                            break;

                            case "reserve": {
                                if (windowChosenCheck(windowChosen)) {
                                    printer.println("\nStato della RISERVA:           ~ ['sd + numero' per scegliere il dado che vuoi]\n");
                                    printer.flush();
                                    int i = 0;
                                    for (String dice : dicesList) {
                                        printer.println(i++ + ") " + dice);
                                        printer.flush();
                                    }
                                    printer.println();
                                    printer.flush();
                                }
                            }
                            break;

                            case "schemecard": {
                                showMySchemeCard();
                            }
                            break;

                            case "sp": {
                                printer.println("Stai giocando contro:");
                                printNames();
                            }
                            break;

                            case "sw": {
                                showWindow();
                            }
                            break;

                            case "tool": {

                                boolean found = false;
                                if (parametersCardinalityCheck(2)) {
                                    toolNumber1 = tryParse(parts[1]);
                                    if (toolNumber1 != null) {
                                        for (ToolCommand toolCommand : toolCommands) {
                                            if (toolCommand.getI() == Integer.parseInt(parts[1])) {
                                                found = true;
                                                printer.println("\nHere follows the ToolCard description:\n");
                                                printer.println(toolCommand.parametersNeeded);
                                                printer.flush();
                                            }
                                        }
                                        if (!found) {
                                            printer.println("\nWARNING: Toolcard not in the ToolCard List!");
                                            printer.flush();
                                        }
                                    } else {
                                        syntaxErrorPrint();
                                    }
                                    toolNumber1 = null;
                                }
                            }
                            break;

                            case "tokens": {
                                showFavorTokens();
                            }
                            break;

                            case "toolcards": {
                                printer.println("\nHere follows the ToolCards List:          ~ ['tool number' to understand how to play the toolcard you want to use]\n");
                                showToolCards();
                            }
                            break;

                            case "track": {
                                printer.println("Di seguito il tracciato dei round: (vuoto se primo round) \n" + roundTrack);
                                printer.flush();
                            }
                            break;

                            case "usetool": {
                                if (windowChosenCheck(windowChosen) && !diceValueToBeSet && !tool11DiceToBePlaced) {
                                    if (parts.length >= 2) {
                                        toolNumber1 = tryParse(parts[1]);
                                        if (toolNumber1 != null)
                                            useRightCommand(toolNumber1);
                                        else {
                                            syntaxErrorPrint();
                                        }
                                        toolNumber1 = null;
                                    } else {
                                        syntaxErrorPrint();
                                    }
                                }
                            }
                            break;

                            case "valore": {
                                if (windowChosenCheck(windowChosen) && diceValueToBeSet) {

                                    if (parts.length == 2) {
                                        toolNumber1 = tryParse(parts[1]);
                                        if (toolNumber1 != null && toolNumber1 > 0 && toolNumber1 < 7) {
                                            //RMI
                                            if (controllerRmi != null)
                                                controllerRmi.setDiceValue(toolNumber1, username, single);
                                                //SOCKET
                                            else
                                                controllerSocket.request(new SetDiceValueRequest(toolNumber1, username, single));
                                            printer.println("Valore selezionato correttamente! Ora usa il comando 'pd11' seguito dalla posizione in cui vuoi piazzare il dado! \n" + roundTrack);
                                            printer.flush();
                                            diceValueToBeSet = false; //todo:VA MESSO A FALSE ANCHE SE SI PASSA IL TURNO SENZA AVER COMPLETATO L'AZIONE
                                            tool11DiceToBePlaced = true;
                                        } else {
                                            syntaxErrorPrint();
                                        }
                                        toolNumber1 = null;
                                    } else {
                                        syntaxErrorPrint();
                                    }
                                }
                            }
                            break;

                            default: {
                                printer.println("\nWARNING: Wrong command. Insert 'h' command for help!");
                                printer.flush();
                            }
                        }
                    } else {
                        switch (parts[0]) {

                            case "h": {
                                printer.println("\nInsert a new valid option between: ('+' means SPACE)" + HELP_GENERAL_MULTI);
                                printer.flush();
                            }
                            break;


                            case "otherschemecards": {
                                printer.println(otherSchemeCardsMap.toString());
                                printer.flush();
                            }
                            break;

                            case "othertokens": {
                                printer.println(otherFavorTokensMap.toString());
                                printer.flush();
                            }
                            break;

                            case "private": {
                                showPrivateCard();
                            }
                            break;

                            case "public": {
                                showPublicCards();
                            }
                            break;

                            case "q": {
                                quit();
                            }
                            break;

                            case "r": {
                                printer.println(RULES);
                                printer.flush();
                            }
                            break;

                            case "reserve": {
                                if (windowChosenCheck(windowChosen)) {
                                    printer.println("\nHere follows the current RESERVE state:           ~ ['cd number' to choose the dice you want]\n");
                                    printer.flush();
                                    int i = 0;
                                    for (String dice : dicesList) {
                                        printer.println(i++ + ") " + dice);
                                        printer.flush();
                                    }
                                    printer.println();
                                    printer.flush();
                                }
                            }
                            break;

                            case "schemecard": {
                                showMySchemeCard();
                            }
                            break;

                            case "sp": {
                                printer.println("Stai giocando contro:");
                                printNames();
                            }
                            break;

                            case "sw": {
                                showWindow();
                            }
                            break;

                            case "toolcards": {
                                printer.println("\nHere follows the ToolCards list:          ~ ['tool number' to understand how to play the toolcard you want to use]\n");
                                showToolCards();
                            }
                            break;


                            case "track": {
                                printer.println("Di seguito il tracciato dei round: (vuoto se primo round) \n" + roundTrack);
                                printer.flush();
                            }
                            break;

                            case "tokens": {
                                showFavorTokens();
                            }
                            break;

                            default: {
                                printer.println("\nWARNING: Wrong command. Insert 'h' command for help!");
                                printer.flush();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }

        private void showWindow() {
            if (parametersCardinalityCheck(2)) {
                if (playersNames.contains(parts[1])) {

                    if (otherSchemeCardsMap.containsKey(parts[1])) {
                        printer.println("\nDi seguito la carta schema del tuo avversario " + parts[1].toUpperCase() + ":");
                        printer.println(otherSchemeCardsMap.get(parts[1]).toString());
                        printer.flush();
                    } else {
                        printer.println("\nATTENZIONE:Il giocatore " + parts[1].toUpperCase() + " non è un tuo avversario!");
                        printer.flush();
                    }


                } else {
                    printer.println("\nATTENZIONE: Il giocatore " + parts[1].toUpperCase() + " non esiste!");
                    printer.flush();
                }
            }
        }

        private void quit() {
            //RMI
            if (controllerRmi != null) {
                try {
                    controllerRmi.quitGame(username, single);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
            //SOCKET
            else {
                controllerSocket.request(new QuitGameRequest(username, single));
                System.exit(0);
            }
        }

        private void useRightCommand(int i) {

            boolean found = false;
            for (ToolCommand toolCommand : toolCommands) {
                if (toolCommand.getI() == i) {
                    found = true;
                    switch (i) {
                        case 1: {
                            if (parametersCardinalityCheck(4)) {
                                toolNumber1 = tryParse(parts[2]);
                                toolString1 = parts[3];
                                if (toolNumber1 != null && (toolString1.equals("+") || toolString1.equals("-"))) {
                                    if (toolCommand.command1(toolNumber1, toolString1)) {
                                        printer.println("\nBen fatto! Il dado da te selezionato è stato modificato correttamente!\n");
                                        printer.flush();
                                    } else {
                                        gameErrorPrint();
                                    }
                                } else {
                                    syntaxErrorPrint();
                                }
                            }
                            toolNumber1 = null;
                            toolString1 = null;
                        }
                        break;

                        case 2: {
                            if (parametersCardinalityCheck(6)) {
                                toolNumber1 = tryParse(parts[2]);
                                toolNumber2 = tryParse(parts[3]);
                                toolNumber3 = tryParse(parts[4]);
                                toolNumber4 = tryParse(parts[5]);

                                if (toolNumber1 != null && toolNumber2 != null && toolNumber3 != null && toolNumber4 != null) {
                                    if (toolCommand.command2or3(2, toolNumber1, toolNumber2, toolNumber3, toolNumber4)) {
                                        printer.println("\nBen fatto! Il dado da te selezionato è stato spostato correttamente!\n");
                                        printer.flush();
                                    } else {
                                        gameErrorPrint();
                                    }
                                } else {
                                    syntaxErrorPrint();
                                }
                            }
                            toolNumber1 = null;
                            toolNumber2 = null;
                            toolNumber3 = null;
                            toolNumber4 = null;
                        }
                        break;

                        case 3: {
                            if (parametersCardinalityCheck(6)) {
                                toolNumber1 = tryParse(parts[2]);
                                toolNumber2 = tryParse(parts[3]);
                                toolNumber3 = tryParse(parts[4]);
                                toolNumber4 = tryParse(parts[5]);

                                if (toolNumber1 != null && toolNumber2 != null && toolNumber3 != null && toolNumber4 != null) {
                                    if (toolCommand.command2or3(3, toolNumber1, toolNumber2, toolNumber3, toolNumber4)) {
                                        printer.println("\nBen fatto! Il dado da te selezionato è stato spostato correttamente!\n");
                                        printer.flush();
                                    } else {
                                        gameErrorPrint();
                                    }
                                } else {
                                    syntaxErrorPrint();
                                }
                            }
                            toolNumber1 = null;
                            toolNumber2 = null;
                            toolNumber3 = null;
                            toolNumber4 = null;
                        }
                        break;

                        case 4: {
                            if (parametersCardinalityCheck(10)) {
                                toolNumber1 = tryParse(parts[2]);
                                toolNumber2 = tryParse(parts[3]);
                                toolNumber3 = tryParse(parts[4]);
                                toolNumber4 = tryParse(parts[5]);
                                toolNumber5 = tryParse(parts[6]);
                                toolNumber6 = tryParse(parts[7]);
                                toolNumber7 = tryParse(parts[8]);
                                toolNumber8 = tryParse(parts[9]);

                                if (toolNumber1 != null && toolNumber2 != null && toolNumber3 != null && toolNumber4 != null && toolNumber5 != null && toolNumber6 != null && toolNumber7 != null && toolNumber8 != null) {
                                    if (toolCommand.command4(toolNumber1, toolNumber2, toolNumber3, toolNumber4, toolNumber5, toolNumber6, toolNumber7, toolNumber8)) {
                                        printer.println("\nBen fatto! I dadi da te selezionati sono stati spostati correttamente!\n");
                                        printer.flush();
                                    } else {
                                        gameErrorPrint();
                                    }
                                } else {
                                    syntaxErrorPrint();
                                }
                            }
                            toolNumber1 = null;
                            toolNumber2 = null;
                            toolNumber3 = null;
                            toolNumber4 = null;
                            toolNumber5 = null;
                            toolNumber6 = null;
                            toolNumber7 = null;
                            toolNumber8 = null;
                        }
                        break;

                        case 5: {
                            if (parametersCardinalityCheck(5)) {
                                toolNumber1 = tryParse(parts[2]);
                                toolNumber2 = tryParse(parts[3]);
                                toolNumber3 = tryParse(parts[4]);

                                if (toolNumber1 != null && toolNumber2 != null && toolNumber3 != null) {
                                    if (toolCommand.command5(toolNumber1, toolNumber2, toolNumber3)) {
                                        printer.println("\nBen fatto! Il dado da te selezionato è stato modificato correttamente!\n");
                                        printer.flush();
                                    } else {
                                        gameErrorPrint();
                                    }
                                } else {
                                    syntaxErrorPrint();
                                }
                            }
                            toolNumber1 = null;
                            toolNumber2 = null;
                            toolNumber3 = null;
                        }
                        break;

                        case 6: {
                            if (parametersCardinalityCheck(3)) {
                                toolNumber1 = tryParse(parts[2]);
                                if (toolNumber1 != null) {
                                    if (toolCommand.command6(toolNumber1)) {
                                        printer.println("\nBen fatto! Il dado da te selezionato è stato modificato correttamente!\n");
                                        printer.flush();
                                    } else {
                                        gameErrorPrint();
                                    }
                                } else {
                                    syntaxErrorPrint();
                                }
                            }
                            toolNumber1 = null;
                        }
                        break;

                        case 7: {
                            if (diceChosen == 9) { //dado non ancora scelto
                                if (toolCommand.command7()) {
                                    printer.println("\nBen fatto! La riserve è stata rimescolata correttamente!\n");
                                    printer.flush();
                                } else {
                                    gameErrorPrint();
                                }
                            } else {
                                syntaxErrorPrint();
                            }

                        }
                        break;
                        case 8: {
                            if (toolCommand.command8()) {
                                printer.println("\nBen fatto! Puoi ora piazzare il tuo secondo dado.\n");
                                printer.flush();
                            } else {
                                gameErrorPrint();
                            }
                        }
                        break;
                        case 9: {
                            if (parametersCardinalityCheck(5)) {
                                toolNumber1 = tryParse(parts[2]);
                                toolNumber2 = tryParse(parts[3]);
                                toolNumber3 = tryParse(parts[4]);
                                if (toolNumber1 != null && toolNumber2 != null && toolNumber3 != null) {
                                    if (toolCommand.command9(toolNumber1, toolNumber2, toolNumber3)) {
                                        printer.println("\nBen fatto! Il dado da te selezionato è stato inserito correttamente!\n");
                                        printer.flush();
                                    } else {
                                        gameErrorPrint();
                                    }
                                } else {
                                    syntaxErrorPrint();
                                }
                            }
                            toolNumber1 = null;
                            toolNumber2 = null;
                            toolNumber3 = null;
                        }
                        break;

                        case 10: {
                            if (parametersCardinalityCheck(3)) {
                                toolNumber1 = tryParse(parts[2]);
                                if (toolNumber1 != null) {
                                    if (toolCommand.command10(toolNumber1)) {
                                        printer.println("\nBen fatto! Il dado da te selezionato è stato modificato correttamente!\n");
                                        printer.flush();
                                    } else {
                                        gameErrorPrint();
                                    }
                                } else {
                                    syntaxErrorPrint();
                                }
                            }
                            toolNumber1 = null;

                        }
                        break;

                        case 11: {
                            if (parametersCardinalityCheck(3)) {
                                toolNumber1 = tryParse(parts[2]);
                                if (toolNumber1 != null) {
                                    if (toolCommand.command11(toolNumber1)) {
                                        Colors color = null;
                                        if (controllerRmi != null) {
                                            try {
                                                color = controllerRmi.askForDiceColor(username, single);
                                            } catch (RemoteException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        //SOCKET
                                        else {
                                            controllerSocket.request(new DiceColorRequest(username, single));
                                            color = controllerSocket.getDiceColor();
                                        }

                                        printer.println("\nBen fatto! Il dado da te selezionato è stato inserito correttamente nel sacchetto! Ora puoi scegliere il valore del nuovo dado del colore  " + color.toString() + " e piazzarlo!\n Per effettuare questa operazione digita il comando 'valore' accompagnato da uno spazio e dal valore che vuoi");
                                        printer.flush();
                                        diceValueToBeSet = true;
                                    } else {
                                        gameErrorPrint();
                                    }
                                } else {
                                    syntaxErrorPrint();
                                }
                            }
                            toolNumber1 = null;
                        }
                        break;

                        case 12: {
                            if (parametersCardinalityCheck(8)) {
                                toolNumber1 = tryParse(parts[2]);
                                toolNumber2 = tryParse(parts[3]);
                                toolNumber3 = tryParse(parts[4]);
                                toolNumber4 = tryParse(parts[5]);
                                toolNumber5 = tryParse(parts[6]);
                                toolNumber6 = tryParse(parts[7]);
                                if (toolNumber1 != null && toolNumber2 != null && toolNumber3 != null && toolNumber4 != null && toolNumber5 != null && toolNumber6 != null) {
                                    if (toolCommand.command12(toolNumber1, toolNumber2, toolNumber3, toolNumber4, toolNumber5, toolNumber6, -1, -1, -1, -1)) {
                                        printer.println("\nBen fatto! Il dado da te scelto è stato spostato correttamente!\n");
                                        printer.flush();
                                    } else {
                                        gameErrorPrint();
                                    }
                                } else {
                                    syntaxErrorPrint();
                                }
                                toolNumber1 = null;
                                toolNumber2 = null;
                                toolNumber3 = null;
                                toolNumber4 = null;
                                toolNumber5 = null;
                                toolNumber6 = null;
                            } else if (parametersCardinalityCheck(12)) {
                                toolNumber1 = tryParse(parts[2]);
                                toolNumber2 = tryParse(parts[3]);
                                toolNumber3 = tryParse(parts[4]);
                                toolNumber4 = tryParse(parts[5]);
                                toolNumber5 = tryParse(parts[6]);
                                toolNumber6 = tryParse(parts[7]);
                                toolNumber7 = tryParse(parts[8]);
                                toolNumber8 = tryParse(parts[9]);
                                toolNumber9 = tryParse(parts[10]);
                                toolNumber10 = tryParse(parts[11]);

                                if (toolNumber1 != null && toolNumber2 != null && toolNumber3 != null && toolNumber4 != null && toolNumber5 != null && toolNumber6 != null && toolNumber7 != null && toolNumber8 != null && toolNumber9 != null && toolNumber10 != null) {
                                    if (toolCommand.command12(toolNumber1, toolNumber2, toolNumber3, toolNumber4, toolNumber5, toolNumber6, toolNumber7, toolNumber8, toolNumber9, toolNumber10)) {
                                        printer.println("\nBen fatto! I dadi da te scelti sono stati spostati correttamente!\n");
                                        printer.flush();
                                    } else {
                                        gameErrorPrint();
                                    }
                                } else {
                                    syntaxErrorPrint();
                                }
                                toolNumber1 = null;
                                toolNumber2 = null;
                                toolNumber3 = null;
                                toolNumber4 = null;
                                toolNumber5 = null;
                                toolNumber6 = null;
                                toolNumber7 = null;
                                toolNumber8 = null;
                                toolNumber9 = null;
                                toolNumber10 = null;
                            }

                        }
                        break;
                    }
                }
            }
            if (!found) {
                printer.println("\nATTENZIONE: Carta utensile non presente nella lista delle carte utensili!\n");
                printer.flush();
            }
        }

        private boolean windowChosenCheck(boolean windowChosen) {
            if (windowChosen)
                return true;
            else {
                printer.println("\nATTENZIONE: devi scegliere la tua carta schema prima di fare altre mosse!");
                printer.flush();
                return false;
            }
        }

        private boolean tool11DiceToBePlacedCheck(boolean tool11DiceToBePlaced) {
            if (!tool11DiceToBePlaced)
                return true;
            else {
                printer.println("\nATTENZIONE: devi piazzare il dado di cui hai appena settato il valore prima di fare altre mosse! Usa il comando 'pd11' seguito dalle coordinate.\n");
                printer.flush();
                return false;
            }
        }

        private boolean diceValueToBeSetCheck(boolean diceValueToBeSet) {
            if (!diceValueToBeSet)
                return true;
            else {
                printer.println("\nATTENZIONE: devi scegliere il valore del dado che hai ricevuto in seguito all'utilizzo dalla toolcard 11! Usa il comando 'valore' seguito dal numero che vuoi settare.\n");
                printer.flush();
                return false;
            }
        }

        private Integer tryParse(String text) {
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        private boolean parametersCardinalityCheck(int n) {
            if (parts.length == n)
                return true;
            else {
                syntaxErrorPrint();
                return false;
            }
        }

        private void syntaxErrorPrint() {
            printer.println(SYNTAX_ERROR);
            printer.flush();
        }

        private void gameErrorPrint() {
            printer.println(GAME_ERROR);
            printer.flush();
        }
    }
}