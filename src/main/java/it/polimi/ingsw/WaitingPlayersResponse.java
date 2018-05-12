package it.polimi.ingsw;

import java.util.List;

public class WaitingPlayersResponse implements Response {

    List<String> waitingPlayers;
    LobbyObserver observer;

    public WaitingPlayersResponse(List<String> waitingPlayers, LobbyObserver observer) {
        this.waitingPlayers = waitingPlayers;
        this.observer=observer;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
