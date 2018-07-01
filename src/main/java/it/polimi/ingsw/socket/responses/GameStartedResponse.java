package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.control.ResponseHandler;

import java.util.List;

public class GameStartedResponse implements Response {

    private boolean windowChosen;
    private List<String> names;
    private int turnTime;

    public GameStartedResponse(boolean windowChosen, List<String> names, int turnTime) {
        this.windowChosen = windowChosen;
        this.names = names;
        this.turnTime = turnTime;
    }

    public boolean isWindowChosen() {
        return windowChosen;
    }

    public List<String> getNames() {
        return names;
    }

    public int getTurnTime() {
        return turnTime;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
