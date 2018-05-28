package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class PlayerReconnectionResponse implements Response {

    private String name;

    public PlayerReconnectionResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
