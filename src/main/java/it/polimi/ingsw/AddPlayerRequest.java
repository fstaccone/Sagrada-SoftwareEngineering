package it.polimi.ingsw;

public class AddPlayerRequest implements Request {
    public final String username;

    public AddPlayerRequest(String username) {
        this.username = username;
    }

    @Override
    public Response handle(RequestHandler handler) {
         return handler.handle(this);
    }
}