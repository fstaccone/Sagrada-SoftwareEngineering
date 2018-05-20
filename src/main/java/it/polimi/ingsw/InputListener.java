/*package it.polimi.ingsw;

import java.rmi.RemoteException;
import java.util.Scanner;

public class InputListener implements Runnable {

    private RmiCli cli;

    public InputListener(RmiCli rmiCli) {
        this.cli = rmiCli;
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);

        while(cli != null) {
            if (!cli.isMyTurn()) {
                switch (in.nextLine()) {
                    case "-h":
                    case "-help":
                    case "help":
                    case "-H":
                    case "-Help":
                    case "Help": System.out.println("Aiuto");//request for help's info
                        break;
                    case "-r":
                    case "-rules":
                    case "rules":
                    case "-R":
                    case "-Rules":
                    case "Rules": System.out.println("Regole");//request for rules
                        break;
                    case "-q":
                    case "-quit":
                    case "quit":
                    case "-Q":
                    case "-Quit":
                    case "Quit":System.out.println("Esci");//quit connection and close
                        break;

                    default:
                        System.out.println("Wrong choise. Insert a new valid option between:" +
                                "\n -h to show game's commands accepted" +
                                "\n -r to show game's rules" +
                                "\n -q to quit the game");
                }
            }
            else{
                // ha senso inserire un ciclo while per ripetere le operazioni se non vanno a buon fine?
                switch (in.nextLine()) {
                    case "-cd":
                    case "cd":
                    case "-chooseDice":{
                        // Stampa i dadi disponibili da scegliere
                        System.out.println("Scegli il dado:");
                        // manda richiesta con l'input preso da tastiera
                        }break;
                    case "-pd":
                    case "pd":
                    case "-placeDice":{
                        System.out.println("Inserisci la posizione:");
                        // manda richiesta con l'input preso da tastiera
                        }break;
                    case "-q":
                    case "-quit":
                    case "quit":
                    case "-Q":
                    case "-Quit":
                    case "Quit":{
                        System.out.println("Esci");
                        //quit connection and close
                        }break;
                    case "passa":
                        try {
                            cli.goThrough();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }break;
                    default:
                        System.out.println("Wrong choise. Insert a new valid option !");
                }

            }
        }
    }
}
*/