package it.polimi.ingsw;

public class YourTurnResponse implements Response {

    public boolean myTurn;

    public YourTurnResponse( boolean yourTurn) {
        this.myTurn=yourTurn;
    }


    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
