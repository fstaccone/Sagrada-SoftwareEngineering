package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Request extends Serializable {
    Response handleRequest(RequestHandler handler) throws RemoteException, InterruptedException;
}