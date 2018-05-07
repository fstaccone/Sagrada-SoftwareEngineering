package it.polimi.ingsw;

import it.polimi.ingsw.control.Controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class LaunchServer {

    private static String serverConfig = "./src/main/java/it/polimi/ingsw/server.config";
    private static int rmiPort;
    private static int socketPort;
    private static String lobbyName;
    private static int waitingTime;

    public static void main(String[] args) throws IOException {

        //read configuration file
        readServerConfig(serverConfig);

        System.out.println("waitingTime = " + waitingTime);
        Controller controller = new Controller(waitingTime);


        //start RMI registry
        try {
           Registry registry = LocateRegistry.createRegistry(rmiPort);
           registry.rebind(lobbyName,controller);
           System.out.println("RMI server online on port " + rmiPort);

        } catch (RemoteException e) {
            System.out.println("Cannot start RMI registry");
        }

        //start Socket connection
        SocketServer server=new SocketServer(socketPort);
        try{
            server.run();
        } finally {
            server.close();
        }




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
                    LaunchServer.socketPort = Integer.parseInt(value.getValue());
                    break;

                case "rmiPort":
                    LaunchServer.rmiPort = Integer.parseInt(value.getValue());
                    break;

                case "rmiLobbyName":
                    LaunchServer.lobbyName = value.getValue();
                    break;

                case "waitingTime":
                    Server.waitingTime = Integer.parseInt(value.getValue());
                    break;

            }
        }

    }
}
