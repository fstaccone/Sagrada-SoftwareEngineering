package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class PlayerExitResponse implements Response {

    public String name;

    public PlayerExitResponse(String name) {
        this.name = name;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
