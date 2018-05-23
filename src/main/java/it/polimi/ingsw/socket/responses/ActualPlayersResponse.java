package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

import java.util.List;

public class ActualPlayersResponse implements Response {

    public List<String> playersNames;

    public ActualPlayersResponse(List<String> playersNames) {
        this.playersNames = playersNames;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
