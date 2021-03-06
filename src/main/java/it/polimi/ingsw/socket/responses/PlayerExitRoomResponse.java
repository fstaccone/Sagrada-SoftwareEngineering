package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.control.ResponseHandler;

public class PlayerExitRoomResponse implements Response {

    private String name;

    public PlayerExitRoomResponse(String name) {
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