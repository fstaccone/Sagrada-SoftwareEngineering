package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.control.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class PingRequest implements Request {

    private String username;
    private boolean single;

    public PingRequest(String username, boolean single) {
        this.username = username;
        this.single = single;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSingle() {
        return single;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}