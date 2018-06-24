package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class TerminateMatchRequest implements Request {

    private String name;

    public TerminateMatchRequest(String name) {
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
