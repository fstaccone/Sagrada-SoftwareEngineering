package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.control.ResponseHandler;

public class GameEndSingleResponse implements Response {

    private int target;
    private int points;

    public GameEndSingleResponse(int target, int points) {
        this.target = target;
        this.points = points;
    }

    public int getTarget() {
        return target;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
