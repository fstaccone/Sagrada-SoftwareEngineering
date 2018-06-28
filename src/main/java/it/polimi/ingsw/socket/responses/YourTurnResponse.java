package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.control.ResponseHandler;

public class YourTurnResponse implements Response {

    private boolean myTurn;
    private String name;
    private int round;
    private int turn;

    public YourTurnResponse(boolean myTurn, String name, int round, int turn) {
        this.myTurn = myTurn;
        this.name = name;
        this.round = round;
        this.turn = turn;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public String getName() {
        return name;
    }

    public int getRound() {
        return round;
    }

    public int getTurn() {
        return turn;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
