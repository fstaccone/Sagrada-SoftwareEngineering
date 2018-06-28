package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.control.ResponseHandler;

public class OtherFavorTokensResponse implements Response {

    private int value;
    private String name;

    public OtherFavorTokensResponse(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
