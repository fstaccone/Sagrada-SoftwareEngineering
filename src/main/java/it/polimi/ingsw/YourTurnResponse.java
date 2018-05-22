package it.polimi.ingsw;

public class YourTurnResponse implements Response {

    public boolean myTurn;
    public String string;

    public YourTurnResponse( boolean yourTurn, String string) {
        this.myTurn=yourTurn;
        this.string=string;
    }


    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
