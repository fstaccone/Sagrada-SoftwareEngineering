package it.polimi.ingsw;

import it.polimi.ingsw.control.Controller;
import it.polimi.ingsw.model.gameobjects.Lobby;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class Server{

    private static String serverConfig = "./src/main/java/it/polimi/ingsw/server.config";
    private static int rmiPort;
    private static int socketPort;
    private static String lobbyName;


    public static void main(String[] args) throws RemoteException {

        Controller lobby = new Controller();


        //read configuration file
        readServerConfig(serverConfig);

        //start RMI registry
        try {
           Registry registry = LocateRegistry.createRegistry(rmiPort);
           registry.rebind(lobbyName,lobby);

        } catch (RemoteException e) {
            System.out.println("Cannot start RMI registry");
        }
        System.out.println("RMI server online on port " + rmiPort);





    }


    private static void readServerConfig(String serverConfig){

        FileReader fileReader=null;
        Scanner scanner = null;
        Scanner lineParser=null;
        String line;
        HashMap<String, String> valuesMap = new HashMap<>();

        //try-catch-finally to guarantee rightness of fileReader's closure after use
        try {
             fileReader = new FileReader(serverConfig);

             //try-finally to guarantee rightness of scanner's closure after use
            try {
                scanner = new Scanner(fileReader);

                //lecture of values added to the valuesMap
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    if (line.isEmpty()) {
                        continue;
                    }
                    //try-finally to guarantee rightness of lineParser's closure
                    try{
                        lineParser = new Scanner(line);
                        valuesMap.put(lineParser.next(), lineParser.next());
                    }
                    finally {
                        if (lineParser != null) {
                            lineParser.close();
                        }
                    }

                }

            }
            finally {
                if (scanner != null) {
                    scanner.close();
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //values assignment from map to server's attributes
        configValues(valuesMap);
    }

    private static void configValues(HashMap<String, String> valuesMap) {

        for (Map.Entry<String, String> value : valuesMap.entrySet()) {
            switch (value.getKey()) {

                case "socketPort":
                    Server.socketPort = Integer.parseInt(value.getValue());
                    break;

                case "rmiPort":
                    Server.rmiPort = Integer.parseInt(value.getValue());
                    break;

                case "rmiLobbyName":
                    Server.lobbyName = value.getValue();
                    break;

            }
        }

    }
}
