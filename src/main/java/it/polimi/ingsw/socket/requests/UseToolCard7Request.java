package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard7Request implements Request {

    public String username;
    public boolean isSingle;
    public int diceToBeSacrificed;

    public UseToolCard7Request(int diceToBeSacrificed,String username, boolean isSingle) {
        this.username = username;
        this.isSingle = isSingle;
        this.diceToBeSacrificed=diceToBeSacrificed;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
