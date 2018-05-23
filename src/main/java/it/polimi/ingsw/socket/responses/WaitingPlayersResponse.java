package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

import java.util.List;

public class WaitingPlayersResponse implements Response {

    public List<String> waitingPlayers;
    public String name;
    public boolean unique;

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
