package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

import java.io.Serializable;

public interface Response extends Serializable {
    void handleResponse(ResponseHandler handler);
}
