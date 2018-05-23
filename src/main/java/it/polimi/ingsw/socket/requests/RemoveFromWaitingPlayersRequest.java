package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class RemoveFromWaitingPlayersRequest implements Request {
    public final String name;

    public RemoveFromWaitingPlayersRequest(String name) {
        this.name = name;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
