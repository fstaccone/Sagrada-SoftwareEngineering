package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;


public class SetDiceValueRequest implements Request {

    private int value;
    private String name;
    private boolean isSingle;

    public SetDiceValueRequest(int value, String name, boolean isSingle) {
        this.value = value;
        this.name = name;
        this.isSingle = isSingle;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public boolean isSingle() {
        return isSingle;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
