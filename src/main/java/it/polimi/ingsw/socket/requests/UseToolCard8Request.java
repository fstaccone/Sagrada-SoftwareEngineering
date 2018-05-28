package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard8Request implements Request {

    public String name;
    public boolean single;

    public UseToolCard8Request(String name, boolean single) {
    this.name = name;
    this.single = single;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
