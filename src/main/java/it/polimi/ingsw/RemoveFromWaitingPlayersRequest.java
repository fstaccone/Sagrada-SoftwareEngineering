package it.polimi.ingsw;

public class RemoveFromWaitingPlayersRequest implements Request{
    public final String name;

    public RemoveFromWaitingPlayersRequest(String name) {
        this.name = name;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
