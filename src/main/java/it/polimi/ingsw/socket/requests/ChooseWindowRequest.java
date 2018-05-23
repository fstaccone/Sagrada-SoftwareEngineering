package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class ChooseWindowRequest implements Request {

    public String username;
    public int value;
    public boolean single;

    public ChooseWindowRequest(String username, int value, boolean single) {
        this.username = username;
        this.value=value;
        this.single=single;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
