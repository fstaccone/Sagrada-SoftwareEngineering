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
    private List<ToolCommand> toolCommands;

    private int diceChosen=9;
    private int coordinateX;
    private int coordinateY;

    private boolean windowChosen=false;

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

    private static final String HELP_IN_TURN = (
            "\n 'st'                                        to show tool cards" +
            "\n 'cw' + 'number'                             to choose tour window pattern card (available once only, at the beginning of the match)" +
            "\n 'cd' + 'number'                             to choose the dice from the Reserve " +
            "\n 'pd' + 'coordinate x' + 'coordinate y'      to place the chosen dice in your Scheme Card " +
            "\n 'pass'                                      to pass the turn to the next player " +
            "\n 'reserve'                                   to show current state of reserve, (available only from the beginning of the first turn)\n");

    private static final String HELP_GENERAL = (
            "\n 'h'                                         to show game commands" +
            "\n 'r'                                         to show game rules" +
            "\n 'q'                                         to quit the game" +
            "\n 'sp'                                        to show all opponents' names" +
            "\n 'sw' + 'name'                               to show the window pattern card of player [name]" +
            "\n 'tool' + 'name'                             to show the description of a toolcard " +
            "\n 'toolcards'                                 to show the list of available toolcards ");


    public RmiCli(String username, RemoteController controller, boolean single) throws RemoteException {
        super();
        this.username = username;
        this.controller = controller;
        this.printer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(FileDescriptor.out)));
        this.myTurn = false;
        new KeyboardHandler().start();
        this.single = single;
        this.toolCommands = new ArrayList<>();
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
    public void onShowToolCards(List<String> cards) {

        printer.println("Tool cards:");
        printer.flush();

        for (String s : cards) {
            printer.println("- " + s);
            printer.println("\n");
            printer.flush();
        }
    }

    @Override
    public void onToolCards(String string) {
        String dicesString = string.substring(1, string.length() - 1);
        toolCardsList = Pattern.compile(", ")
                .splitAsStream(dicesString)
                .collect(Collectors.toList());
        printer.println("ToolCards available for this match:");
        printer.println(toolCardsList.toString());
        printer.flush();
        for (String card : toolCardsList) {
            String[] strings = card.split(" ");
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

    private boolean windowChosenCheck(boolean windowChosen){
        if(windowChosen)
            return true;
        else{
            printer.println("WARNING: You have to choose your window card before asking for commands that necessitate the match to actually be started!");
            printer.flush();
            return false;
        }
    }

    private static Integer tryParse(String text){
        try{
            return Integer.parseInt(text);
        }catch (NumberFormatException e){
            return null;
        }
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

                                if(windowChosenCheck(windowChosen)) {
                                    if (parts.length==2) {
                                        toolNumber1 = tryParse(parts[1]);
                                        if (toolNumber1 != null) {
                                            if (Integer.parseInt(parts[1]) < dicesList.size()) {
                                                diceChosen = Integer.parseInt(parts[1]);
                                                printer.println("You have chosen the dice: " + dicesList.toArray()[diceChosen].toString() + "\n");
                                                printer.flush();
                                            } else {
                                                printer.println("The dice you are trying to use does not exist, please retry ");
                                                printer.flush();
                                            }
                                        } else {
                                            printer.println("WARNING: Invalid request.\n");
                                            printer.flush();
                                        }
                                        toolNumber1 = null;
                                    }else {
                                        printer.println("WARNING: Invalid request.\n");
                                        printer.flush();
                                    }
                                }
                            }
                            break;

                            case "cw": {
                                if (parts.length==2) {
                                    toolNumber1 = tryParse(parts[1]);
                                    if (toolNumber1 != null) {
                                        if (Integer.parseInt(parts[1]) < 4) {
                                            printer.println("You have chosen:");
                                            printer.flush();
                                            controller.chooseWindow(username, Integer.parseInt(parts[1]), single);
                                            windowChosen = true;
                                        }
                                        //DA SETTARE BOOLEANO COSì DA NON CONSENTIRGLI DI FARE LA cd o pt PRIMA DI AVER SCELTO LA SCHEME CARD
                                        else {
                                            printer.println("WARNING: The scheme you are trying to choose does not exist, please retry ");
                                            printer.flush();
                                        }
                                    } else {
                                        printer.println("WARNING: Invalid request.\n");
                                        printer.flush();
                                    }
                                    toolNumber1 = null;
                                } else {
                                    printer.println("WARNING: Invalid request.\n");
                                    printer.flush();
                                }
                            }
                            break;

                            case "h": {
                                printer.println("Insert a new valid option between: ('+' means SPACE)" + HELP_IN_TURN + HELP_GENERAL);
                                printer.flush();
                            }
                            break;

                            case "pass": {
                                if(windowChosenCheck(windowChosen)) {
                                    controller.goThrough(username, single);
                                }
                            }
                            break;

                            case "pd": {
                                if(windowChosenCheck(windowChosen)) {
                                    if (diceChosen != 9) {
                                        if (parts.length==3) {
                                            toolNumber1 = tryParse(parts[1]);
                                            toolNumber2 = tryParse(parts[2]);
                                            if (toolNumber1 != null && toolNumber2 != null) {
                                                if (Integer.parseInt(parts[1]) < 4 && Integer.parseInt(parts[2]) < 5) {
                                                    coordinateX = Integer.parseInt(parts[1]);
                                                    coordinateY = Integer.parseInt(parts[2]);
                                                    printer.println("You have chosen to place the dice in the [" + coordinateX + "][" + coordinateY + "] square of your Scheme Card");
                                                    printer.flush();
                                                    if (controller.placeDice(diceChosen, coordinateX, coordinateY, username, single)) {
                                                        printer.println("Well done! The chosen dice has been placed correctly.\n");
                                                        printer.flush();
                                                        diceChosen = 9; //FIRST VALUE NEVER PRESENT IN THE RESERVE
                                                    } else {
                                                        printer.println("WARNING: You tried to place a dice where you shouldn't, or you are trying to place a second dice in your turn!");
                                                        printer.flush();

                                                    }
                                                } else {
                                                    printer.println("WARNING: The coordinates of your scheme card are out of bounds, please retry ");
                                                    printer.flush();
                                                }
                                            }else {
                                                printer.println("WARNING: Invalid request.\n");
                                                printer.flush();
                                            }
                                        }else {
                                            printer.println("WARNING: Invalid request.\n");
                                            printer.flush();
                                        }
                                        toolNumber1 = null;
                                        toolNumber2 = null;
                                    } else {
                                        printer.println("WARNING: You have to choose a dice before trying to place one! ");
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
                                printer.println("Regole: da scrivere");
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

                            case "sw": {
                                if (parts.length==2) {
                                    //DA INSERIRE CONTROLLO CHE NOME SIA VERAMENTE INCLUSO NELLA LISTA DEI GIOCATORI, IO LA FAREI DIRETTAMENTE IN LOCALE
                                    printer.println("Here is the window pattern card of the player " + parts[1]);
                                    printer.flush();
                                    controller.showWindow(username, parts[1]);
                                }
                            }
                            break;

                            case "sp": {
                                controller.showPlayers(username);
                            }
                            break;

                            case "tool": {

                                boolean found = false;
                                if (parts.length==2) {
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
                                            printer.println("WARNING: Toolcard not in the ToolCard List");
                                            printer.flush();
                                        }
                                    }else {
                                        printer.println("WARNING: Invalid request.\n");
                                        printer.flush();
                                    }
                                    toolNumber1=null;
                                }
                            }
                            break;

                            case "toolcards": {
                                printer.println("\nHere follows the ToolCards List:          ~ ['tool number' to understand how to play the toolcard you want to use]\n");
                                printer.flush();
                                onShowToolCards(toolCardsList);
                            }
                            break;

                            case "usetool": {
                                if (windowChosenCheck(windowChosen)) {
                                    if (parts.length>=2){
                                        toolNumber1 = tryParse(parts[1]);
                                        if (toolNumber1 !=null)
                                            useRightCommand(toolNumber1);
                                        else{
                                            printer.println("WARNING: Invalid request.\n");
                                            printer.flush();
                                        }
                                        toolNumber1=null;
                                    }else {
                                        printer.println("WARNING: Invalid request.\n");
                                        printer.flush();
                                    }
                                }
                            }
                            break;

                            default: {
                                printer.println("Wrong choise. Insert a new valid option between: ('+' means SPACE)" + HELP_IN_TURN + HELP_GENERAL);
                                printer.flush();
                            }
                        }
                    } else {
                        switch (parts[0]) {

                            case "h": {
                                printer.println("Insert a new valid option between: ('+' means SPACE)" + HELP_GENERAL);
                                printer.flush();
                            }
                            break;

                            case "q": {
                                controller.quitGame(username, single);
                                System.exit(0);
                            }
                            break;

                            case "r": {
                                printer.println("Regole");
                                printer.flush();
                            }
                            break;

                            case "sp": {
                                controller.showPlayers(username);
                            }
                            break;

                            case "sw": {
                                if (parts.length==2) {
                                    //DA INSERIRE CONTROLLO CHE NOME SIA VERAMENTE INCLUSO NELLA LISTA DEI GIOCATORI, IO LA FAREI DIRETTAMENTE IN LOCALE
                                    printer.println("Here is the window pattern card of the player " + parts[1]);
                                    printer.flush();
                                    controller.showWindow(username, parts[1]);
                                }
                            }
                            break;

                            case "toolcards": {
                                printer.println("\nHere follows the ToolCards List:          ~ ['tool number' to understand how to play the toolcard you want to use]\n");
                                printer.flush();
                                onShowToolCards(toolCardsList);
                            }
                            break;

                            default: {
                                printer.println("Wrong choise. Insert a new valid option between:" + HELP_IN_TURN);
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
                            if(parts.length==4) {
                                toolNumber1 = tryParse(parts[1]);
                                toolString1=parts[3];
                                if(toolNumber1!=null && (toolString1.equals("+")||toolString1.equals("-"))) {
                                    if (toolCommand.command1(Integer.parseInt(parts[2]), parts[3])) {
                                        printer.println("\nWell done! The chosen dice has been modified correctly.\n");
                                        printer.flush();
                                    } else {
                                        printer.println("WARNING: Invalid request.\n");
                                        printer.flush();
                                    }
                                }else {
                                    printer.println("WARNING: Invalid request.\n");
                                    printer.flush();
                                }
                            }else {
                                printer.println("WARNING: Invalid request.\n");
                                printer.flush();
                            }
                            toolNumber1=null;
                        }
                        break;

                        case 2: {
                            if(parts.length==6) {
                                toolNumber1 = tryParse(parts[2]);
                                toolNumber2 = tryParse(parts[3]);
                                toolNumber3 = tryParse(parts[4]);
                                toolNumber4 = tryParse(parts[5]);

                                if (toolNumber1 != null && toolNumber2 != null && toolNumber3 != null && toolNumber4 != null) {
                                    if (toolCommand.command2or3(2,toolNumber1,toolNumber2,toolNumber3,toolNumber4)) {
                                        printer.println("\nWell done! The chosen dice has been modified correctly.\n");
                                        printer.flush();
                                    } else {
                                        printer.println("Invalid request.\n");
                                        printer.flush();
                                    }
                                }else {
                                    printer.println("WARNING: Invalid request.\n");
                                    printer.flush();
                                }
                            }
                            else {
                                printer.println("WARNING: Invalid request.\n");
                                printer.flush();
                            }
                            toolNumber1=null;
                            toolNumber2=null;
                            toolNumber3=null;
                            toolNumber4=null;
                        }
                        break;

                        case 3: {
                            if(parts.length==6) {
                                toolNumber1 = tryParse(parts[2]);
                                toolNumber2 = tryParse(parts[3]);
                                toolNumber3 = tryParse(parts[4]);
                                toolNumber4 = tryParse(parts[5]);

                                if (toolNumber1 != null && toolNumber2 != null && toolNumber3 != null && toolNumber4 != null) {
                                    if (toolCommand.command2or3(3, toolNumber1,toolNumber2,toolNumber3,toolNumber4)) {
                                        printer.println("\nWell done! The chosen dice has been modified correctly.\n");
                                        printer.flush();
                                    } else {
                                        printer.println("Invalid game request.\n");
                                        printer.flush();
                                    }
                                }else {
                                    printer.println("WARNING: Invalid syntax request.\n");
                                    printer.flush();
                                }
                            }
                            else {
                                printer.println("WARNING: Invalid syntax request.\n");
                                printer.flush();
                            }
                            toolNumber1=null;
                            toolNumber2=null;
                            toolNumber3=null;
                            toolNumber4=null;
                        }
                        break;

                        case 4: {
                            if(parts.length==10) {
                                toolNumber1 = tryParse(parts[2]);
                                toolNumber2 = tryParse(parts[3]);
                                toolNumber3 = tryParse(parts[4]);
                                toolNumber4 = tryParse(parts[5]);
                                toolNumber5 = tryParse(parts[6]);
                                toolNumber6 = tryParse(parts[7]);
                                toolNumber7 = tryParse(parts[8]);
                                toolNumber8 = tryParse(parts[9]);

                                if (toolNumber1 != null && toolNumber2 != null && toolNumber3 != null && toolNumber4 != null && toolNumber5 != null && toolNumber6 != null && toolNumber7 != null && toolNumber8 != null) {
                                    if (toolCommand.command4( toolNumber1,toolNumber2,toolNumber3,toolNumber4,toolNumber5,toolNumber6,toolNumber7,toolNumber8)) {
                                        printer.println("\nWell done! The chosen dice has been modified correctly.\n");
                                        printer.flush();
                                    } else {
                                        printer.println("Invalid game request.\n");
                                        printer.flush();
                                    }
                                }else {
                                    printer.println("WARNING: Invalid syntax request.\n");
                                    printer.flush();
                                }
                            }
                            else {
                                printer.println("WARNING: Invalid syntax request.\n");
                                printer.flush();
                            }
                            toolNumber1=null;
                            toolNumber2=null;
                            toolNumber3=null;
                            toolNumber4=null;
                            toolNumber5=null;
                            toolNumber6=null;
                            toolNumber7=null;
                            toolNumber8=null;
                        }
                        break;

                        case 5: {
                            toolCommand.command5();

                        }
                        break;
                        case 6: {
                            toolCommand.command6();

                        }
                        break;
                        case 7: {
                            toolCommand.command7();

                        }
                        break;
                        case 8: {
                            toolCommand.command8();

                        }
                        break;
                        case 9: {
                            toolCommand.command9();

                        }
                        break;
                        case 10: {
                            toolCommand.command10();

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
                printer.println("WARNING: Toolcard not in the ToolCard List");
                printer.flush();
            }
        }
    }
}
