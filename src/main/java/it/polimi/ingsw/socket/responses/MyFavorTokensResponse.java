package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class MyFavorTokensResponse implements Response {

    private int value;


    public MyFavorTokensResponse(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
