package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class CheckUsernameRequest implements Request {
    private final String username;

    public CheckUsernameRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }


    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}