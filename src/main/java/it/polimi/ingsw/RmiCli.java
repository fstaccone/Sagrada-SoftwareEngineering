package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RmiCli extends UnicastRemoteObject implements MatchObserver {

    private String username;
    private transient RemoteController controller;
    private boolean myTurn;
    private final transient PrintWriter printer;

    private List<String> dicesList;
    private List<String> toolCardsList;
    private List<String> publicCardsList;
    private List<ToolCommand> toolCommands;
    private String privateCard;

    private int diceChosen = 9;
    private int coordinateX;
    private int coordinateY;

    private int turnNumber = 0;
    private List<String> playersNames;

    private boolean windowChosen = false;

    private boolean single; //NON SONO CONVINTO SIA LA SOLUZIONE MIGLIORE

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

    public RmiCli(String username, RemoteController controller, boolean single) throws RemoteException {
        super();
        this.username = username;
        this.controller = controller;
        printer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(FileDescriptor.out)));
        myTurn = false;
        new KeyboardHandler().start();
        this.single = single;
        toolCommands = new ArrayList<>();
        toolCardsList = new ArrayList<>();
        publicCardsList = new ArrayList<>();
    }

    public void launch() throws RemoteException {
        printer.println(WELCOME + username.toUpperCase() + "!\n");
        printer.flush();
        controller.observeMatch(username, this);
    }

    public void reconnect() throws RemoteException, InterruptedException {
        controller.reconnect(username);
        launch();
    }

    @Override
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


    @Override
    public void onYourTurn(boolean yourTurn, String string) {
        turnNumber++;
        if (string != null)
            onReserve(string);
        this.myTurn = yourTurn;
        if (myTurn)
            printer.println("\nNow it's your turn! Please insert a command:                            ~ ['h' for help]\n");
        else
            printer.println("\nIt's no more your turn! (h for help)");
        printer.flush();
    }

    @Override
    public void onReserve(String string) {
        String dicesString = string.substring(1, string.length() - 1);
        dicesList = Pattern.compile(", ")
                .splitAsStream(dicesString)
                .collect(Collectors.toList());
    }

    @Override
    public void onWindowChoise(List<String> windows) {
        int i = 0;
        printer.println("Choose your window among the following                                        ~ [cw] + [number]\n");
        printer.flush();
        for (String s : windows) {
            printer.println(i++ + ") " + s + "\n");
            printer.flush();
        }
    }

    @Override
    public void onAfterWindowChoise() {
        printer.println("You can now perform actions on your scheme card                               ~ [reserve] to check available dices\n");
        printer.flush();
    }

    @Override
    public void onShowWindow(String window) {
        printer.println(window);
        printer.flush();
    }

    @Override
    public void onOtherTurn(String name) {
        printer.println("Now it's " + name + "'s turn");
        printer.flush();
    }

    @Override
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

    @Override
    public void onPlayerExit(String name) {
        printer.println("Player " + name + " has left the game!");
        printer.flush();
    }

    @Override
    public void onPlayerReconnection(String name) {
        printer.println("Player " + name + " is now in game!");
        printer.flush();
    }

    @Override
    public void onShowTrack(String track) {
        printer.println("This is the roundtrack:");
        printer.println(track);
        printer.flush();
    }

    @Override
    public void onShowPrivateCard() {
        printer.println("Your private objective card:");
        printer.println(privateCard);
        printer.flush();
    }

    @Override
    public void onShowPublicCards() {
        printer.println("Public objective cards:");

        for (String s : publicCardsList) {
            printer.println(s);
        }
        printer.flush();
    }

    @Override
    public void onGameClosing() {
        printer.println("Congratulations! You are the winner. You were the only one still in game.");
        printer.flush();
        System.exit(0);
    }

    @Override
    public void onShowToolCards() {
        printer.println("Tool cards:");

        for (String s : toolCardsList) {
            printer.println("- " + s);
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
                                                printer.println("\nYou have chosen:");
                                                printer.flush();
                                                controller.chooseWindow(username, Integer.parseInt(parts[1]), single);
                                                windowChosen = true;
                                            } else {
                                                printer.println("\nWARNING: The scheme you are trying to choose does not exist, please retry ");
                                                printer.flush();
                                            }
                                        } else {
                                            syntaxErrorPrint();
                                        }
                                        toolNumber1 = null;
                                    }
                                } else {
                                    printer.println("\nWARNING: You have already chosen your scheme card!");
                                    printer.flush();
                                }
                            }
                            break;

                            case "h": {
                                printer.println("\nInsert a new valid option between: ('+' means SPACE)" + HELP_IN_TURN + HELP_GENERAL);
                                printer.flush();
                            }
                            break;

                            case "pass": {
                                if (windowChosenCheck(windowChosen)) {
                                    controller.goThrough(username, single);
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
                                                    printer.println("\nYou have chosen to place the dice in the [" + coordinateX + "][" + coordinateY + "] square of your Scheme Card");
                                                    printer.flush();
                                                    if (controller.placeDice(diceChosen, coordinateX, coordinateY, username, single)) {
                                                        printer.println("\nWell done! The chosen dice has been placed correctly.\n");
                                                        printer.flush();
                                                        diceChosen = 9; //FIRST VALUE NEVER PRESENT IN THE RESERVE
                                                    } else {
                                                        printer.println("\nWARNING: You tried to place a dice where you shouldn't, or you are trying to place a second dice in your turn!");
                                                        printer.flush();

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

                            case "q": {
                                controller.quitGame(username, single);
                                System.exit(0);
                            }
                            break;

                            case "r": {
                                printer.println("\nRegole: da scrivere");
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

                            case "sp": {
                                controller.showPlayers(username);
                            }
                            break;

                            case "sw": {
                                if (parametersCardinalityCheck(2)) {
                                    if (turnNumber > 1) {
                                        if (playersNames.contains(parts[1])) {
                                            printer.println("\nHere is the window pattern card of the player " + parts[1].toUpperCase());
                                            printer.flush();
                                            controller.showWindow(username, parts[1]);
                                        } else {
                                            printer.println("\nWARNING: Player " + parts[1].toUpperCase() + " does not exist!");
                                            printer.flush();
                                        }
                                    } else {
                                        printer.println("\nWARNING: You have to wait your second turn to ask for other players' scheme cards! " + parts[1]);
                                        printer.flush();
                                    }
                                }
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

                            case "toolcards": {
                                printer.println("\nHere follows the ToolCards List:          ~ ['tool number' to understand how to play the toolcard you want to use]\n");
                                printer.flush();
                                onShowToolCards();
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

                            case "track": {
                                controller.showTrack(username, single);
                            }
                            break;

                            case "private": {
                                onShowPrivateCard();
                            }
                            break;

                            case "public": {
                                onShowPublicCards();
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

                            case "q": {
                                controller.quitGame(username, single);
                                System.exit(0);
                            }
                            break;

                            case "r": {
                                printer.println(RULES);
                                printer.flush();
                            }
                            break;

                            case "sp": {
                                controller.showPlayers(username);
                            }
                            break;

                            case "sw": {
                                if (parametersCardinalityCheck(2)) {
                                    //DA INSERIRE CONTROLLO CHE NOME SIA VERAMENTE INCLUSO NELLA LISTA DEI GIOCATORI, IO LA FAREI DIRETTAMENTE IN LOCALE
                                    printer.println("\nHere is the window pattern card of the player " + parts[1]);
                                    printer.flush();
                                    controller.showWindow(username, parts[1]);
                                }
                            }
                            break;

                            case "toolcards": {
                                printer.println("\nHere follows the ToolCards list:          ~ ['tool number' to understand how to play the toolcard you want to use]\n");
                                printer.flush();
                                onShowToolCards();
                            }
                            break;

                            case "track": {
                                controller.showTrack(username, single);
                            }
                            break;

                            case "private": {
                                onShowPrivateCard();
                            }
                            break;

                            case "public": {
                                onShowPublicCards();
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
                            if (turnNumber == 2 && diceChosen == 9) {// turno corretto  e dado non ancora scelto
                                if (toolCommand.command7()) {
                                    printer.println("\nWell done! The reserve has been rerolled correctly.\n");
                                    printer.flush();
                                }
                            } else {
                                gameErrorPrint();
                            }

                        }
                        break;
                        case 8: {
                            toolCommand.command8();

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
