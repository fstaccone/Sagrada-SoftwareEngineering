package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class ChooseWindowRequest implements Request {

    private String username;
    private int value;
    private boolean single;

    public ChooseWindowRequest(String username, int value, boolean single) {
        this.username = username;
        this.value = value;
        this.single = single;
    }

    public String getUsername() {
        return username;
    }

    public int getValue() {
        return value;
    }

    public boolean isSingle() {
        return single;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
