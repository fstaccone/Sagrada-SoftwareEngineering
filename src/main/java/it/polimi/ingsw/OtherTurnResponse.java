package it.polimi.ingsw;

public class OtherTurnResponse implements Response {

    public String name;

    public OtherTurnResponse(String name) {
        this.name = name;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
