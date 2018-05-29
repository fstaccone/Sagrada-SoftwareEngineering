package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class MyWindowResponse implements Response {

    public String string;

    public MyWindowResponse(String string) {
        this.string = string;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
