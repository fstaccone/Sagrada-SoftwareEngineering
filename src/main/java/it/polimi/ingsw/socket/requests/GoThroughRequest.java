package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class GoThroughRequest implements Request {

    private String username;
    private boolean singlePlayer;

    public GoThroughRequest(String username, boolean singlePlayer) {
        this.username = username;
        this.singlePlayer = singlePlayer;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSinglePlayer() {
        return singlePlayer;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
