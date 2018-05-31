package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;


public class SetDiceValueRequest implements Request {

    public int value;
    public String name;
    public boolean isSingle;

    public SetDiceValueRequest(int value, String name, boolean isSingle) {
        this.value = value;
        this.name = name;
        this.isSingle = isSingle;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
