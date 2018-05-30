package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.ClientController;
import it.polimi.ingsw.socket.requests.*;

import java.awt.*;
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
    private transient ClientController clientController;
    private transient RemoteController controller;
    private boolean myTurn;
    private final transient PrintWriter printer;

    private List<String> dicesList;
    private List<String> toolCardsList;
    private List<String> publicCardsList;
    private List<ToolCommand> toolCommands;
    private String privateCard;
    private WindowPatternCard mySchemeCard;

    private int myFavorTokens;
    private Map<String, Integer> otherFavorTokensMap;
    private Map<String, WindowPatternCard> otherSchemeCardsMap;

    private int diceChosen = 9;
    private int coordinateX;
    private int coordinateY;

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
            "\nWelcome to this fantastic game, ";

    private static final String RULES = ("Da decidere se in italiano o in inglese");

    private static final String HELP_IN_TURN = (
            "\n 'cd' + 'number'                             to choose the dice from the Reserve" +
                    "\n 'cw' + 'number'                             to choose tour window pattern card (available once only, at the beginning of the match)" +
                    "\n 'pd' + 'coordinate x' + 'coordinate y'      to place the chosen dice in your Scheme Card " +
                    "\n 'pass'                                      to pass the turn to the next player " +
                    "\n 'reserve'                                   to show current state of reserve, (available only from the beginning of the first turn)" +
                    "\n 'usetool' + 'number'                        to use the effect of the tool card [number]");

    private static final String HELP_GENERAL = (
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

    private static final String SYNTAX_ERROR = (
            "\nWARNING: Invalid syntax request.\n");

    private static final String GAME_ERROR = (
            "\nWARNING: Invalid game request. You did not respect the tool card's rules!\n"); //STAMPATO ANCHE SE PROVI A MODIFICARE DADO NELLA RISERVA OUT OF BOUND , si può migliorare


    public Cli(String username, RemoteController controller, ClientController clientController, boolean single) {
        this.username = username;
        this.controller = controller;
        this.clientController = clientController;
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
    }

    public void printWelcome() {
        printer.println(WELCOME + username.toUpperCase() + "!\n");
        printer.flush();
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

    public void onPlayers(List<String> playersNames) {
        printer.println("Your match starts now! You are playing SAGRADA against:");
        printer.flush();
        this.playersNames = playersNames;
        for (String name : playersNames) {
            if (!name.equals(username)) {
                printer.println("-" + name.toUpperCase());
                printer.flush();
            }
        }
        printer.println();
        printer.flush();
    }


    public void onYourTurn(boolean yourTurn, String string) {
        if (string != null)
            onReserve(string);
        this.myTurn = yourTurn;
        if (myTurn)
            printer.println("\nNow it's your turn! Please insert a command:                            ~ ['h' for help]\n");
        else
            printer.println("\nIt's no more your turn! (h for help)");
        printer.flush();
    }

    public void onReserve(String string) {
        String dicesString = string.substring(1, string.length() - 1);
        dicesList = Pattern.compile(", ")
                .splitAsStream(dicesString)
                .collect(Collectors.toList());
    }

    public void onWindowChoise(List<String> windows) {
        int i = 0;
        printer.println("Choose your window among the following                                        ~ [cw] + [number]\n");
        printer.flush();
        for (String s : windows) {
            printer.println(i++ + ") " + s + "\n");
            printer.flush();
        }
    }

    public void onAfterWindowChoise() {
        printer.println("You can now perform actions on your scheme card                               ~ [reserve] to check available dices\n");
        printer.flush();
    }

    public void onMyWindow(WindowPatternCard window) {
        this.mySchemeCard = window;
    }

    public void onOtherTurn(String name) {
        printer.println("Now it's " + name + "'s turn");
        printer.flush();
    }

    public void onInitialization(String toolcards, String publicCards, String privateCard) {
        parseToolcards(toolcards);
        parsePublicCards(publicCards);
        this.privateCard = privateCard;
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
            this.toolCommands.add(new ToolCommand(i, this.printer, this.controller, null, this.username, this.single));
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

    public void onShowTrack(String track) {
        printer.println("This is the roundtrack:");
        printer.println(track);
        printer.flush();
    }

    public void onGameClosing() {
        printer.println("Congratulations! You are the winner. You were the only one still in game.");
        printer.flush();
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

    // TODO: completare con altri parametri
    public void afterReconnection(String toolcards, String publicCards, String privateCard) {
        parseToolcards(toolcards);
        parsePublicCards(publicCards);
        this.privateCard = privateCard;
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

                                if (windowChosenCheck(windowChosen)) {
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
                                if (!windowChosen) {
                                    if (parametersCardinalityCheck(2)) {
                                        toolNumber1 = tryParse(parts[1]);
                                        if (toolNumber1 != null) {
                                            if (Integer.parseInt(parts[1]) < 4) {
                                                //RMI
                                                if (controller != null)
                                                    controller.chooseWindow(username, toolNumber1, single);
                                                    //SOCKET
                                                else
                                                    clientController.request(new ChooseWindowRequest(username, toolNumber1, false));
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
                                printer.println("\nInserisci un comando valido tra i seguenti('+' means SPACE)" + HELP_IN_TURN + HELP_GENERAL);
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
                                if (windowChosenCheck(windowChosen)) {
                                    //RMI
                                    if (controller != null)
                                        controller.goThrough(username, single);
                                        //SOCKET
                                    else
                                        clientController.request(new GoThroughRequest(username, single));
                                }
                            }
                            break;

                            case "pd": {
                                if (windowChosenCheck(windowChosen)) {
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
                                                    if (controller != null) {
                                                        if (controller.placeDice(diceChosen, coordinateX, coordinateY, username, single)) {
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
                                                        clientController.request(new PlaceDiceRequest(diceChosen, coordinateX, coordinateY, username, single));
                                                        try {
                                                            Thread.sleep(500);
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (clientController.isDicePlaced()) {
                                                            clientController.setDicePlaced(false);//to reset the value
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
                                if (controller != null)
                                    controller.showPlayers(username);
                                else {
                                    clientController.request(new ShowPlayersRequest(username));
                                }
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
                                printer.flush();
                                showToolCards();
                            }
                            break;


                            case "track": {
                                if (controller != null)
                                    controller.showTrack(username, single);
                                else {
                                    clientController.request(new ShowTrackRequest(username, single));
                                }
                            }
                            break;

                            case "usetool": {
                                if (windowChosenCheck(windowChosen)) {
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

                            default: {
                                printer.println("\nWARNING: Wrong command. Insert 'h' command for help!");
                                printer.flush();
                            }
                        }
                    } else {
                        switch (parts[0]) {

                            case "h": {
                                printer.println("\nInsert a new valid option between: ('+' means SPACE)" + HELP_GENERAL);
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
                                if (controller != null)
                                    controller.showPlayers(username);
                                else {
                                    clientController.request(new ShowPlayersRequest(username));
                                }
                            }
                            break;

                            case "sw": {
                                showWindow();
                            }
                            break;

                            case "toolcards": {
                                printer.println("\nHere follows the ToolCards list:          ~ ['tool number' to understand how to play the toolcard you want to use]\n");
                                printer.flush();
                                showToolCards();
                            }
                            break;

                            case "track": {
                                if (controller != null)
                                    controller.showTrack(username, single);
                                else {
                                    clientController.request(new ShowTrackRequest(username, single));
                                }
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
            if (controller != null) {
                try {
                    controller.quitGame(username, single);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
            //SOCKET
            else {
                clientController.request(new QuitGameRequest(username, single));
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
                                        printer.println("\nWell done! The chosen dice has been modified correctly.\n");
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
                                        printer.println("\nWell done! The chosen dice has been modified correctly.\n");
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
                                        printer.println("\nWell done! The chosen dice has been modified correctly.\n");
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
                                        printer.println("\nWell done! The chosen dice has been modified correctly.\n");
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
                                        printer.println("\nWell done! The chosen dice has been modified correctly.\n");
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
                                        printer.println("\nWell done! The chosen dice has been modified correctly.\n");
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
                                    printer.println("\nWell done! The reserve has been rerolled correctly.\n");
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
                                printer.println("\nBen fatto! Puoi piazzare il tuo secondo dado.");
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
                                        printer.println("\nWell done! The chosen dice has been modified correctly.\n");
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
                                        printer.println("\nWell done! The chosen dice has been modified correctly.\n");
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
                            toolCommand.command11();

                        }
                        break;
                        case 12: {
                            toolCommand.command12();

                        }
                        break;
                    }
                }
            }
            if (!found) {
                printer.println("WARNING: Toolcard not in the ToolCard list!");
                printer.flush();
            }
        }

        private boolean windowChosenCheck(boolean windowChosen) {
            if (windowChosen)
                return true;
            else {
                printer.println("WARNING: You have to choose your window card before asking for commands that necessitate the match to actually be started!");
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
