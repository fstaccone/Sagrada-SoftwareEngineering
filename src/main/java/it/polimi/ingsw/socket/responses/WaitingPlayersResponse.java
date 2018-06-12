package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

import java.util.List;

public class WaitingPlayersResponse implements Response {

    private List<String> waitingPlayers;
    private String name;
    private boolean unique;

    public WaitingPlayersResponse(List<String> waitingPlayers, String name, Boolean unique) {
        this.waitingPlayers = waitingPlayers;
        this.name = name;
        this.unique = unique;
    }

    public List<String> getWaitingPlayers() {
        return waitingPlayers;
    }

    public String getName() {
        return name;
    }

    public boolean isUnique() {
        return unique;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
