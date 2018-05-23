package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class ReserveResponse implements Response {

    public String reserve;

    public ReserveResponse(String reserve) {
        this.reserve = reserve;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
