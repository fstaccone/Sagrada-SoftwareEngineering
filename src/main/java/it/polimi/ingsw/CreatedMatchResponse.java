package it.polimi.ingsw;

public class CreatedMatchResponse implements Response {

    public boolean matchCreated;

    public CreatedMatchResponse(boolean matchCreated) {
        this.matchCreated = matchCreated;
    }

    @Override
    public void handle(ResponseHandler handler) {
        handler.handle(this);
    }
}
