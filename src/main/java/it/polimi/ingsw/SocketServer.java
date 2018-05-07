package it.polimi.ingsw;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    private final ServerSocket serverSocket;
    private final ExecutorService threadPool;

    public SocketServer(int port) throws IOException {

        serverSocket = new ServerSocket(port);
        threadPool = Executors.newCachedThreadPool();
        System.out.println(">>>Socket server online on port" + port);
    }

    public void run() throws IOException {
        while (serverSocket!=null) {
            Socket clientSocket = serverSocket.accept();
            System.out.println(">>> New connection " + clientSocket.getRemoteSocketAddress());
            threadPool.submit( new ClientHandler(clientSocket));
        }
    }

    public void close() throws IOException {
        serverSocket.close();
        threadPool.shutdown();
    }
}
