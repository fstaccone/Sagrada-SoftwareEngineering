package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.control.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

import java.io.Serializable;

/**
 * Every request is already described in the controller thought to handle it.
 * Check Controller.
 */
public interface Request extends Serializable {
    Response handleRequest(RequestHandler handler);
}