package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.control.ResponseHandler;

import java.util.List;

public class WaitingPlayersResponse implements Response {

    private List<String> waitingPlayers;
    private String name;

    public WaitingPlayersResponse(List<String> waitingPlayers, String name) {
        this.waitingPlayers = waitingPlayers;
        this.name = name;
    }

    public List<String> getWaitingPlayers() {
        return waitingPlayers;
    }

    public String getName() {
        return name;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
