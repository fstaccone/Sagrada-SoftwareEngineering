package it.polimi.ingsw;

public class ObserveMatchRequest implements Request {
    public final String username;
    public final MatchObserver matchObserver;

    public ObserveMatchRequest(String username,MatchObserver matchObserver) {
        this.username=username;
        this.matchObserver = matchObserver;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}