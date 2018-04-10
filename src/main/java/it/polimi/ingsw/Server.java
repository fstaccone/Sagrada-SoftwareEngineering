package it.polimi.ingsw;

import it.polimi.ingsw.control.Controller;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) throws RemoteException {
        Controller controller= new Controller();
        System.out.println("Controller exported.");
        Registry registry= LocateRegistry.getRegistry();
        registry.rebind("lobby", controller);//COME GESTIRE PIù PARTITE?
    }
}
