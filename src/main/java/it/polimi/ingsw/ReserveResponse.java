package it.polimi.ingsw;

public class ReserveResponse implements Response {

    String reserve;

    public ReserveResponse(String reserve) {
        this.reserve = reserve;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
