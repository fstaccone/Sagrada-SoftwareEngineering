package it.polimi.ingsw.model;

import java.rmi.RemoteException;
import java.util.*;

public class Database {
    private static Database database;
    private Set<User> loggedUsers;

    private Database(){
        loggedUsers= new HashSet<>();
    }
    //Singleton
    public synchronized static Database get(){
        if (database==null){
            database=new Database();
        }
        return database;
    }

    public synchronized User login(String username) throws RemoteException{
        if (loggedUsers.stream().map(User::getUsername).anyMatch(u -> u.equals(username))) {
            throw new RemoteException("The user is already logged: " + username);
        }
        User user = new User(username);
        loggedUsers.add(user);
        return user; //ritorna user al controller
    }
}
