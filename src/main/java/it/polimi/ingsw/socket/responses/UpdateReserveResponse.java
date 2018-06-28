package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.control.ResponseHandler;

public class UpdateReserveResponse implements Response {

    private String string;

    public UpdateReserveResponse(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
