package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.control.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard8Request implements Request {

    private String name;
    private boolean single;
    private int diceToBeSacrificed;

    public UseToolCard8Request(int diceToBeSacrificed, String name, boolean single) {
        this.name = name;
        this.single = single;
        this.diceToBeSacrificed=diceToBeSacrificed;
    }

    public String getName() {
        return name;
    }

    public boolean isSingle() {
        return single;
    }

    public int getDiceToBeSacrificed() {
        return diceToBeSacrificed;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
