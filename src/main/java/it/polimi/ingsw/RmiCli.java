package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RmiCli extends UnicastRemoteObject implements MatchObserver {

    private String username;
    private RemoteController controller;
    private boolean myTurn;
    private final PrintWriter printer;

    private List<String> dicesList;

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
            "\n 'sw' + 'name'                               to show the window pattern card of player [name]" +
            "\n 'cw' + 'number'                             to choose tour window pattern card (available once only, at the beginning of the match)" +
            "\n 'cd' + 'number'                             to choose the dice from the Reserve (available only if it's your turn)" +
            "\n 'pd' + 'coordinate x' + 'coordinate y'       to place the chosen dice in your Scheme Card (available only if it's your turn)" +
            "\n 'pass'                                      to pass the turn to the next player (available only if it's your turn)");

    public RmiCli(String username, RemoteController controller, boolean single) throws RemoteException {
        super();
        this.username = username;
        this.controller = controller;
        this.printer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(FileDescriptor.out)));
        this.myTurn = false;
        new KeyboardHandler().start();
        this.single = single;
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
    public void onYourTurn(boolean yourTurn) {
        this.myTurn = yourTurn;
        if (myTurn)
            printer.println("\nNow it's your turn! Please insert a command:                                  ~ ['h' for help]\n");
        else
            printer.println("\nIt's no more your turn! (h for help)");
        printer.flush();
    }

    @Override
    public void onReserve(String string) {
        String dicesString = string.substring(1, string.length() - 1);
        printer.println("\nHere follows the current RESERVE state:            ~ ['cd number' to choose the dice you want]\n");
        printer.flush();
        dicesList = Pattern.compile(", ")
                .splitAsStream(dicesString)
                .collect(Collectors.toList());
        int i = 0;
        for (String dice : dicesList) {
            printer.println(i++ + ") " + dice);
            printer.flush();
        }
        printer.println();
        printer.flush();
    }

    @Override
    public void onWindowChoise(List<String> windows) {
        int i = 0;
        printer.println("Choose your window among the following by typing [cw] + [number]!\n");
        printer.flush();
        for(String s : windows){
            s=s.replaceAll("null"," ");
            printer.println(i++ + ") " + s + "\n");
            printer.flush();
        }
    }

    @Override
    public void onShowWindow(String window) {
        printer.println("Window pattern card:");
        printer.flush();
        printer.println(window);
        printer.flush();
    }

    @Override
    public void onOtherTurn(String name)  {
        printer.println("Now it's "+name +"'s turn");
        printer.flush();
    }

    private class KeyboardHandler extends Thread {

        @Override
        public void run() {
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            while (true) {

                try {
                    String command = keyboard.readLine();
                    String[] parts = command.split(" ");
                    if (myTurn) {
                        switch (parts[0]) {

                            case "h": {
                                printer.println("Insert a new valid option between: ('+' means SPACE)" + HELP);
                                printer.flush();
                            }
                            break;

                            case "r": {
                                printer.println("Regole");
                                printer.flush();
                            }
                            break;

                            case "q": {
                                printer.println("Esci");
                                printer.flush();
                            }
                            break;

                            case "cd": {
                                diceChosen = Integer.parseInt(parts[1]);
                                printer.println("You have chosen the dice: " + dicesList.toArray()[diceChosen].toString());
                                printer.flush();
                            }
                            break;

                            case "cw": {
                                controller.chooseWindow(username, Integer.parseInt(parts[1]), false);
                            } break;

                            case "pd": {
                                coordinateX = Integer.parseInt(parts[1]);
                                coordinateY = Integer.parseInt(parts[2]);
                                printer.println("You have chosen to place the dice in the [" + coordinateX + "][" + coordinateY + "] square of your Scheme Card");
                                printer.flush();
                                if (controller.placeDice(diceChosen, coordinateX, coordinateY, username, single)) {
                                    printer.println("Well done! The chosen dice has been placed correctly.");
                                    printer.flush();
                                } else {
                                    printer.println("You tried to place a dice when you shouldn't!");
                                    printer.flush();
                                }
                            }
                            break;

                            case "sw": {
                                printer.println("Here is the window pattern card of the player " + parts[1]);
                                printer.flush();
                                controller.showWindow(username, parts[1]);
                            }break;

                            case "pass": {
                                controller.goThrough(username, single);
                            }
                            break;

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

                            case "q": {
                                printer.println("Esci");
                                printer.flush();
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
    }
}
