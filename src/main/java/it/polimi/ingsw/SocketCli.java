package it.polimi.ingsw;

import java.io.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SocketCli implements Serializable, MatchObserver {

    private String username;
    private transient ClientController controller;
    private boolean myTurn;
    private final PrintWriter printer;

    private List<String> dicesList;

    private int diceChosen;
    private int coordinateX;
    private int coordinateY;

    private boolean single; //NON SONO CONVINTO SIA LA SOLUZIONE MIGLIORE

    private static final String WELCOME="@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
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
            "\nWelcome to this fantastic game,";

    private static final String HELP=   ("\n 'h'                                         to show game commands" +
            "\n 'r'                                         to show game rules" +
            "\n 'q'                                         to quit the game" +
            "\n 'sp'                                        to show all opponents' names" +
            "\n 'sw' + 'name'                               to show the window pattern card of player [name]" +
            "\n 'cw' + 'number'                             to choose tour window pattern card (available once only, at the beginning of the match)" +
            "\n 'cd' + 'number'                             to choose the dice from the Reserve (available only if it's your turn)" +
            "\n 'pd' + 'coordinate x' + 'coordinate y'       to place the chosen dice in your Scheme Card (available only if it's your turn)" +
            "\n 'pass'                                      to pass the turn to the next player (available only if it's your turn)");

    public SocketCli(String username, ClientController controller){
        this.username=username;
        this.controller=controller;
        this.printer=new PrintWriter(new OutputStreamWriter(new FileOutputStream(FileDescriptor.out)));
        this.myTurn=false;
        new KeyboardHandler().start();
        controller.setSocketCli(this);
        this.single=false; //PARALLELIZZARE CON RmiCli
    }

    public void launch() {
        printer.println(
                WELCOME+ username.toUpperCase()+" :)\n");
        printer.flush();

        //controller.request(new ObserveMatchRequest(username,this));
    }



    //DA CAPIRE COME CONVIENE FARE, UN'ALTRA INTERFACCIA MATCHOBSERVERSOCKET? discorso != da loginhandler, qui si doppia socket cli e rmi cli
    @Override
    public void onPlayers(List<String> playersNames) {
        printer.println("Your match starts now! You are playing SAGRADA against:");
        printer.flush();
        for(String name:playersNames){
            if (!name.equals(username)){
                printer.println("-" + name.toUpperCase());
                printer.flush();
            }
        }
        //printer.println("Wait for your turn...");
        //printer.flush();
        printer.println();
        printer.flush();
    }


    @Override
    public void onYourTurn(boolean isMyTurn, String string){
        if (string!=null)
            onReserve(string);
        this.myTurn=isMyTurn;
        if (myTurn)
            printer.println("\nNow it's your turn! Please insert a command:                                  ~ ['h' for help]\n");
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
    public void onWindowChoise(List<String> windows)  {
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
        printer.println("Your window pattern card:");
        printer.flush();
        printer.println(window);
        printer.flush();
    }

    @Override
    public void onOtherTurn(String name)  {
    }
    @Override
    public void onShowToolCards(List<String> cards) {
    }

    private class KeyboardHandler extends Thread {

        @Override
        public void run() {
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            while (true) {

                try {
                    String command = keyboard.readLine();
                    String[] parts = command.split(" ");
                    if(myTurn) {
                        switch (parts[0]) {

                            case "h": {
                                printer.println( "Insert a new valid option between: ('+' means SPACE)" +HELP);
                                printer.flush();
                            }
                            break;

                            case "r": {
                                printer.println("Regole");
                                printer.flush();
                            }
                            break;

                            case "q":{
                                printer.println("Esci");
                                printer.flush();
                            }
                            break;

                            case "cd":{
                                diceChosen=Integer.parseInt(parts[1]);
                                printer.println("You have chosen the dice: "+dicesList.toArray()[diceChosen].toString());
                                printer.flush();
                            }
                            break;

                            case "cw": {
                                controller.request(new ChooseWindowRequest(username, Integer.parseInt(parts[1]), false));
                                //controller.chooseWindow(username, Integer.parseInt(parts[1]), false);
                            } break;

                            case "pd":{
                                coordinateX= Integer.parseInt(parts[1]);
                                coordinateY= Integer.parseInt(parts[2]);
                                printer.println("You have chosen to place the dice in the ["+coordinateX+"]["+coordinateY+"] square of your Scheme Card");
                                printer.flush();
                                //controller.placeDice(diceChosen,coordinateX,coordinateY,username);
                            }break;

                            case "pass":{
                                controller.request(new GoThroughRequest(username, single));

                            }break;

                            case "reserve":{
                                printer.println("\nHere follows the current RESERVE state:            ~ ['cd number' to choose the dice you want]\n");
                                printer.flush();
                                int i = 0;
                                for (String dice : dicesList) {
                                    printer.println(i++ + ") " + dice);
                                    printer.flush();
                                }
                                printer.println();
                                printer.flush();
                            }break;

                            default: {
                                printer.println("Wrong choise. Insert a new valid option between: ('+' means SPACE)" + HELP);
                                printer.flush();
                            }
                        }
                    }
                    else {
                        switch (parts[0]) {
                            case "h": {
                                printer.println("Insert a new valid option between: ('+' means SPACE)" +HELP);
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




