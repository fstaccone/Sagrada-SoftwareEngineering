package it.polimi.ingsw.socket.responses;


import it.polimi.ingsw.socket.ResponseHandler;

public class MatchStartedResponse implements Response {
    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
