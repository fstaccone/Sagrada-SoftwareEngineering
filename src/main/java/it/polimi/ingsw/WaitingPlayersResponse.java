package it.polimi.ingsw;

import java.util.List;

public class WaitingPlayersResponse implements Response {

    List<String> waitingPlayers;

    public WaitingPlayersResponse(List<String> waitingPlayers) {
        this.waitingPlayers = waitingPlayers;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
