package it.polimi.ingsw.control;

import it.polimi.ingsw.model.Database;
import it.polimi.ingsw.model.User;
import it.polimi.ingsw.view.RemoteBaseView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Controller extends UnicastRemoteObject implements RemoteController {

    private transient final Database database;
    private final Map<User, RemoteBaseView> views = new HashMap<>();

    public Controller() throws RemoteException{
        super();
        database= Database.get();
    }

    @Override
    public synchronized String login(String username, RemoteBaseView view) throws RemoteException {
        User user=database.login(username);

        views.put(user, view);
        view.ack("Logged in as @" + user.getUsername());
        return user.getUsername();//ritorna username alla view
    }
}
