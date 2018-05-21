package it.polimi.ingsw;



public class MatchStartedResponse implements Response {
    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
