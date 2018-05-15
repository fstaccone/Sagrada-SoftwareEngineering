package it.polimi.ingsw;

public class CreateMatchRequest implements Request {
    public final String username;

    public CreateMatchRequest(String username) {
        this.username = username;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
          return handler.handle(this);
    }
}
