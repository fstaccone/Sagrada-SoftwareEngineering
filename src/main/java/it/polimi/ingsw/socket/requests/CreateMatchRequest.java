package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.control.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class CreateMatchRequest implements Request {

    private final String username;
    private final int difficulty;

    public CreateMatchRequest(String username, int difficulty) {
        this.username = username;
        this.difficulty = difficulty;
    }

    public String getUsername() {
        return username;
    }

    public int getDifficulty() {
        return difficulty;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
