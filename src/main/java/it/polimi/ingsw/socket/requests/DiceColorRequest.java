package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;



public class DiceColorRequest implements Request {

    public String name;
    public boolean isSingle;

    public DiceColorRequest(String name, boolean isSingle) {
        this.name = name;
        this.isSingle = isSingle;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
