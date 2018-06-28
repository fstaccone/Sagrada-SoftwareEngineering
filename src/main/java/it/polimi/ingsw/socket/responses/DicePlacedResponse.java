package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.control.ResponseHandler;

public class DicePlacedResponse implements Response {

    private boolean done;

    public DicePlacedResponse(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
