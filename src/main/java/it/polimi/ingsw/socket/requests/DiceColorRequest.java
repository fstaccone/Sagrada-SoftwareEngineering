package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;


public class DiceColorRequest implements Request {

    private String name;
    private boolean isSingle;

    public DiceColorRequest(String name, boolean isSingle) {
        this.name = name;
        this.isSingle = isSingle;
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
