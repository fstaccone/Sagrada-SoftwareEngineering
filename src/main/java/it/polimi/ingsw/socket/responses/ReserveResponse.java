package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class ReserveResponse implements Response {

    private String reserve;

    public ReserveResponse(String reserve) {
        this.reserve = reserve;
    }

    public String getReserve() {
        return reserve;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
