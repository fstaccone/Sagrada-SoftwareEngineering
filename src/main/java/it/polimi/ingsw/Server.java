package it.polimi.ingsw;

//import it.polimi.ingsw.control.Controller;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    private ThreadPoolExecutor threads;

    public static void main(String[] args) throws RemoteException {
      //  Controller controller= new Controller();
        System.out.println("Controller exported.");
        Registry registry= LocateRegistry.getRegistry();
      //  registry.rebind("lobby", controller);//COME GESTIRE PIù PARTITE?
    }


    // Fa partire una nuova room
    public void createNewRoom(){
        Thread newThread = new Thread();
    }
}
