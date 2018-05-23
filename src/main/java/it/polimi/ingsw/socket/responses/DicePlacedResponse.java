package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class DicePlacedResponse implements Response {

    public boolean done;

    public DicePlacedResponse(boolean done) {
        this.done = done;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
