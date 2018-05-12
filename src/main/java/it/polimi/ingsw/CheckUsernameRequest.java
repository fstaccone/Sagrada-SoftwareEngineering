package it.polimi.ingsw;

public class CheckUsernameRequest implements Request {
    public final String username;

    public CheckUsernameRequest(String username) {
        this.username = username;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}