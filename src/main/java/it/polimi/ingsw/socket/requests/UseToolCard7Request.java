package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.control.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard7Request implements Request {

    private String username;
    private boolean isSingle;
    private int diceToBeSacrificed;

    public UseToolCard7Request(int diceToBeSacrificed, String username, boolean isSingle) {
        this.username = username;
        this.isSingle = isSingle;
        this.diceToBeSacrificed = diceToBeSacrificed;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public int getDiceToBeSacrificed() {
        return diceToBeSacrificed;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
