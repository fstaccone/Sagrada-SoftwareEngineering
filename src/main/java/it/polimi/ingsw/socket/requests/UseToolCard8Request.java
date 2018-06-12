package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard8Request implements Request {

    public String name;
    public boolean single;
    public int diceToBeSacrificed;
    public UseToolCard8Request(int diceToBeSacrificed, String name, boolean single) {
        this.name = name;
        this.single = single;
        this.diceToBeSacrificed=diceToBeSacrificed;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
