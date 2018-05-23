package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class UpdateReserveResponse implements Response {

    public String string;

    public UpdateReserveResponse(String string) {
        this.string = string;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
