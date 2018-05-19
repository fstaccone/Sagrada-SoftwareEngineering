package it.polimi.ingsw;

import java.util.List;

public class WaitingPlayersResponse implements Response {

    List<String> waitingPlayers;
    String name;
    Boolean unique;

    public WaitingPlayersResponse(List<String> waitingPlayers, String name, Boolean unique) {
        this.waitingPlayers = waitingPlayers;
        this.name=name;
        this.unique=unique;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
