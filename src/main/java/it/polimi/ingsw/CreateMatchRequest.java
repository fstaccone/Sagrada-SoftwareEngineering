package it.polimi.ingsw;

public class CreateMatchRequest implements Request {
    public final String username;

    public CreateMatchRequest(String username) {
        this.username = username;
    }

    @Override
    public Response handle(RequestHandler handler) {
            return null;
    }
}
