package it.polimi.ingsw;

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
