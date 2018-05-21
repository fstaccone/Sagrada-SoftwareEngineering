package it.polimi.ingsw;

public class GoThroughRequest implements Request {

    public String username;
    public boolean singlePlayer;

    public GoThroughRequest(String username, boolean singlePlayer) {
        this.username=username;
        this.singlePlayer=singlePlayer;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
