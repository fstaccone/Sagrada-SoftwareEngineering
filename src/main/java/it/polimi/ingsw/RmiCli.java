package it.polimi.ingsw;

import it.polimi.ingsw.control.RemoteController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RmiCli extends UnicastRemoteObject implements MatchObserver{

    private String username;
    private RemoteController controller;
    private boolean myTurn;
    private boolean single; // per distinguere singleplayer da muiltiplayer

    public RmiCli(String username, RemoteController controller, boolean single) throws RemoteException {
        super();
        this.username = username;
        this.controller = controller;
        this.myTurn = false;
        this.single = single;
    }

    public boolean isMyTurn() { return myTurn; }

    public void launch() {
        System.out.println(
                "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
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
                        "\nWelcome to this fantastic game," + username.toUpperCase()+" :)\n");

        try {
            controller.observeMatch(username,this);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        new Thread( new InputListener(this)).start();

    }

    @Override
    public void onPlayers(List<String> playersNames)  {
        System.out.println("\nYour match starts now! You are playing SAGRADA against:");
        for(String name:playersNames){
            if (!name.equals(username)){
                System.out.println("-" + name.toUpperCase());
            }
        }
    }

    @Override
    public void onYourTurn(String name, boolean isMyTurn){
        myTurn = isMyTurn;

        if(isMyTurn) {
            System.out.println(this.username.toUpperCase() + " is your turn!");
        }
    }

    // per definire i metodi di gioco non è meglio creare un'altra interfaccia?
    public void goThrough() throws RemoteException {
        controller.goThrough(username, single); // todo: passare il booleano corretto
    }

}
