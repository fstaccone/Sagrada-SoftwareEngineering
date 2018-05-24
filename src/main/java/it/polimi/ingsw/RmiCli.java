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

    private int diceChosen;
    private int coordinateX;
    private int coordinateY;

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

    private static final String HELP = ("\n 'h'                                         to show game commands" +
            "\n 'r'                                         to show game rules" +
            "\n 'q'                                         to quit the game" +
            "\n 'sp'                                        to show all opponents' names" +
            "\n 'st'                                        to show tool cards" +
            "\n 'sw' + 'name'                               to show the window pattern card of player [name]" +
            "\n 'cw' + 'number'                             to choose tour window pattern card (available once only, at the beginning of the match)" +
            "\n 'cd' + 'number'                             to choose the dice from the Reserve (available only if it's your turn)" +
            "\n 'pd' + 'coordinate x' + 'coordinate y'      to place the chosen dice in your Scheme Card (available only if it's your turn)" +
            "\n 'pass'                                      to pass the turn to the next player (available only if it's your turn)" +
            "\n 'reserve'                                   to show current state of reserve, (available only from the beginning of the first turn)\n");

    public RmiCli(String username, RemoteController controller, boolean single) throws RemoteException {
        super();
        this.username = username;
        this.controller = controller;
        this.printer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(FileDescriptor.out)));
        this.myTurn = false;
        new KeyboardHandler().start();
        this.single = single;
        this.toolCommands=new ArrayList<>();
    }

    public void launch() {
        printer.println(WELCOME + username.toUpperCase() + "!\n");
        printer.flush();
        try {
            controller.observeMatch(username, this);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void reconnect() throws RemoteException {
        controller.reconnect(username);
        launch();
        // todo : definire cosa fare dopo aver ristabilito la connessione
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
        //printer.println("Wait for your turn..."); Commentato per poter riutilizzare il metodo in caso di richiesta di visione della griglia di un certo giocatore
        //printer.flush();                          si potrebbe voler vedere la lista di nomi dei giocatori
        printer.println();
        printer.flush();
    }


    @Override
    public void onYourTurn(boolean yourTurn, String string) {
        if (string!=null)
            onReserve(string);
        this.myTurn = yourTurn;
        if (myTurn)
            printer.println("\nNow it's your turn! Please insert a command:                                   ~ ['h' for help]\n");
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
        for(String s : windows){
            printer.println(i++ + ") " + s + "\n");
            printer.flush();
        }
    }

    @Override
    public void onShowWindow(String window) {
        printer.println(window);
        printer.flush();
    }

    @Override
    public void onOtherTurn(String name)  {
        printer.println("Now it's "+name +"'s turn");
        printer.flush();
    }

    @Override
    public void onShowToolCards(List<String> cards) {

        printer.println("Tool cards:");
        printer.flush();

        for(String s : cards){
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
        for(String card: toolCardsList){
            String[] strings= card.split(" ");
            int i = Integer.parseInt(strings[0].replaceAll("tool", ""));
            this.toolCommands.add(new ToolCommand(i,this.printer,this.controller,null,this.username, this.single));
        }
    }

    @Override
    public void onPlayerExit(String name) {
        printer.println("Player " + name + " has left the game!");
        printer.flush();
    }

    @Override
    public void onPlayerReconnection(String name) throws RemoteException {
        printer.println("Player " + name + " is now in game!");
        printer.flush();
    }

    private class KeyboardHandler extends Thread {
        String parts[];

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
                                if (Integer.parseInt(parts[1])<dicesList.size()){
                                    diceChosen = Integer.parseInt(parts[1]);
                                    printer.println("You have chosen the dice: " + dicesList.toArray()[diceChosen].toString() + "\n");
                                    printer.flush();
                                }
                                else {
                                    printer.println("The dice you are trying to use does not exist, please retry ");
                                    printer.flush();
                                }
                            }
                            break;

                            case "cw": {
                                if (Integer.parseInt(parts[1])<4){
                                    printer.println("You have chosen:");
                                    printer.flush();
                                    controller.chooseWindow(username, Integer.parseInt(parts[1]), single);
                                }
                                //DA SETTARE BOOLEANO COSì DA NON CONSENTIRGLI DI FARE LA cd o pt PRIMA DI AVER SCELTO LA SCHEME CARD
                                else {
                                    printer.println("The scheme you are trying to choose does not exist, please retry ");
                                    printer.flush();
                                }

                            } break;

                            case "h": {
                                printer.println("Insert a new valid option between: ('+' means SPACE)" + HELP);
                                printer.flush();
                            }
                            break;

                            case "pass": {
                                controller.goThrough(username, single);
                            }
                            break;

                            case "pd": {
                                if (Integer.parseInt(parts[1])<4 && Integer.parseInt(parts[2])<5) {
                                    coordinateX = Integer.parseInt(parts[1]);
                                    coordinateY = Integer.parseInt(parts[2]);
                                    printer.println("You have chosen to place the dice in the [" + coordinateX + "][" + coordinateY + "] square of your Scheme Card");
                                    printer.flush();
                                    if (controller.placeDice(diceChosen, coordinateX, coordinateY, username, single)) {
                                        printer.println("Well done! The chosen dice has been placed correctly.\n");
                                        printer.flush();
                                    } else {
                                        printer.println("You tried to place a dice when you shouldn't!");
                                        printer.flush();
                                    }
                                }
                                else {
                                    printer.println("The coordinates of your scheme card are out of bounds, please retry ");
                                    printer.flush();
                                }
                            }break;

                            case "q": {
                                controller.quitGame(username, single);
                                System.exit(0);
                            }break;

                            case "r": {
                                printer.println("Regole");
                                printer.flush();
                            }
                            break;

                            case "reserve":{
                                printer.println("\nHere follows the current RESERVE state:           ~ ['cd number' to choose the dice you want]\n");
                                printer.flush();
                                int i = 0;
                                for (String dice : dicesList) {
                                    printer.println(i++ + ") " + dice);
                                    printer.flush();
                                }
                                printer.println();
                                printer.flush();
                            }break;

                            case "sw": {
                                printer.println("Here is the window pattern card of the player " + parts[1]);
                                printer.flush();
                                controller.showWindow(username, parts[1]);
                            }break;

                            case "tool":{
                                printer.println("\nHere follows the ToolCard description:\n");
                                printer.flush();
                                boolean found = false;
                                for(ToolCommand toolCommand:toolCommands){
                                    if (toolCommand.getI()==Integer.parseInt(parts[1])) {
                                        found=true;
                                        printer.println(toolCommand.parametersNeeded);
                                        printer.flush();

                                    }
                                }
                                if (!found){
                                        printer.println("toolcard not in the ToolCard List");
                                        printer.flush();
                                }
                            } break;

                            case "toolcards": {
                                printer.println("\nHere follows the ToolCards List:          ~ ['tool number' to understand how to play the toolcard you want to use]\n");
                                printer.flush();
                                onShowToolCards(toolCardsList);
                            }break;

                            case "usetool":{
                                useRightCommand(Integer.parseInt(parts[1]));
                            }break;

                            default: {
                                printer.println("Wrong choise. Insert a new valid option between: ('+' means SPACE)" + HELP);
                                printer.flush();
                            }
                        }
                    } else {
                        switch (parts[0]) {

                            case "h": {
                                printer.println("Insert a new valid option between: ('+' means SPACE)" + HELP);
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
                                printer.println("Here is the window pattern card of the player " + parts[1]);
                                printer.flush();
                                controller.showWindow(username, parts[1]);
                            }
                            break;

                            default: {
                                printer.println("Wrong choise. Insert a new valid option between:" + HELP);
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

            boolean found=false;
            for (ToolCommand toolCommand : toolCommands) {
                if (toolCommand.getI() == i) {
                    found=true;
                    switch (i) {
                        case 1: {

                               if( toolCommand.command1(Integer.parseInt(parts[2]), parts[3])){
                                   printer.println("\nWell done! The chosen dice has been modified correctly.\n");
                                   printer.flush();
                               }
                               else {
                                   printer.println("Invalid request.\n");
                                   printer.flush();
                               }
                        }
                        break;
                        case 2: {
                            if( toolCommand.command2or3(2,Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]))){
                                printer.println("\nWell done! The chosen dice has been modified correctly.\n");
                                printer.flush();
                            }
                            else {
                                printer.println("Invalid request.\n");
                                printer.flush();
                            }

                        }
                        break;
                        case 3: {
                            if( toolCommand.command2or3(3,Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]))){
                                printer.println("\nWell done! The chosen dice has been modified correctly.\n");
                                printer.flush();
                            }
                            else {
                                printer.println("Invalid request.\n");
                                printer.flush();
                            }

                        }
                        break;
                        case 4: {
                            toolCommand.command4();

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
            if (found==false){
                printer.println("toolcard not in the ToolCard List");
                printer.flush();
            }
        }
    }
}
