package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class OtherFavorTokensResponse implements Response {

    public int value;
    public String name;

    public OtherFavorTokensResponse(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
