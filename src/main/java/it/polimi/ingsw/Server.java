package it.polimi.ingsw;

import it.polimi.ingsw.control.Controller;
import it.polimi.ingsw.model.gamelogic.Lobby;
import it.polimi.ingsw.socket.SocketHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static final String SERVER_CONFIG = "/server.config";
    private static int socketPort;
    private static String lobbyName;
    private static int waitingTime;
    private static int turnTime;

    /**
     * Reads the configuration file and starts RMI registry and socket server.
     */
    public static void main(String[] args) throws IOException {

        //read configuration file
        readServerConfig(SERVER_CONFIG);

        Lobby lobby = new Lobby(waitingTime, turnTime);
        Controller controller = new Controller(lobby);

        //start RMI registry
        try {
            LocateRegistry.createRegistry(1099);
            Naming.rebind("//localhost/" + lobbyName, controller);
            LOGGER.log(Level.INFO,"RMI server online on port 1099");
        } catch (RemoteException e) {
            LOGGER.log(Level.INFO,"Cannot start RMI registry");
        }

        //start Socket connection
        ExecutorService threadPool;
        try (ServerSocket serverSocket = new ServerSocket(socketPort)) {
            threadPool = Executors.newCachedThreadPool();
            LOGGER.log(Level.INFO,"Socket server online on port " + socketPort);
            while (serverSocket != null) {
                Socket Socket = serverSocket.accept();
                LOGGER.log(Level.INFO,"New socket connection: " + Socket.getRemoteSocketAddress());
                threadPool.submit(new SocketHandler(Socket, controller));
            }
            serverSocket.close();
        }
        threadPool.shutdown();
    }


    private static void readServerConfig(String serverConfig) {

        Scanner scanner = null;
        Scanner lineParser = null;
        String line;
        HashMap<String, String> valuesMap = new HashMap<>();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Server.class.getResourceAsStream(serverConfig)));

        //try-finally to guarantee rightness of scanner's closure after use
        try {
            scanner = new Scanner(bufferedReader);

            //lecture of values added to the valuesMap
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.isEmpty()) {
                    continue;
                }
                //try-finally to guarantee rightness of lineParser's closure
                try {
                    lineParser = new Scanner(line);
                    valuesMap.put(lineParser.next(), lineParser.next());
                } finally {
                    if (lineParser != null) {
                        lineParser.close();
                    }
                }
            }

        } finally {
            if (scanner != null) {
                scanner.close();
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

                case "rmiLobbyName":
                    Server.lobbyName = value.getValue();
                    break;

                case "waitingTime":
                    Server.waitingTime = Integer.parseInt(value.getValue());
                    break;

                case "turnTime":
                    Server.turnTime = Integer.parseInt(value.getValue());
                    break;

                default:
                    LOGGER.log(Level.INFO,"Unknown parameter in config file");
            }
        }
    }
}