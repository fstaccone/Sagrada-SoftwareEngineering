package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.control.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

import java.io.Serializable;

public interface Request extends Serializable {
    Response handleRequest(RequestHandler handler);
}