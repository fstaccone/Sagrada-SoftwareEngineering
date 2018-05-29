package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class MyFavorTokensResponse implements Response {

    public int value;


    public MyFavorTokensResponse(int value) {
        this.value = value;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
         handler.handle(this);
    }
}
