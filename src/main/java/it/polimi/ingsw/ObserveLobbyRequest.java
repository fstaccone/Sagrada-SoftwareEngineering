package it.polimi.ingsw;

public class ObserveLobbyRequest implements Request {
    public final LobbyObserver lobbyObserver;

    public ObserveLobbyRequest(LobbyObserver lobbyObserver) {
        this.lobbyObserver = lobbyObserver;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}