package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class PlayerExitRoomResponse implements Response {

    public String name;

    public PlayerExitRoomResponse(String name) {
        this.name = name;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}