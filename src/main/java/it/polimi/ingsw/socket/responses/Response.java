package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.control.ResponseHandler;

import java.io.Serializable;

/**
 * Every response is already described in the controller thought to handle it.
 * Check SocketController.
 */
public interface Response extends Serializable {
    void handleResponse(ResponseHandler handler);
}
