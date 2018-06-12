package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class CreateMatchRequest implements Request {

    public final String username;
    public final int difficulty;

    public CreateMatchRequest(String username, int difficulty) {
        this.username = username;
        this.difficulty = difficulty;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
