package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class RemoveFromWaitingPlayersRequest implements Request {

    private final String name;

    public RemoveFromWaitingPlayersRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
