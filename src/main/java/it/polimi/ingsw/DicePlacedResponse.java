package it.polimi.ingsw;

public class DicePlacedResponse implements Response {

    boolean done;

    public DicePlacedResponse(boolean done) {
        this.done = done;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
