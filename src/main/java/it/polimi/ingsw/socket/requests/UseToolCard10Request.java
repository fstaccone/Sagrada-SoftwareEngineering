package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard10Request implements Request {

    public int diceChosen;

    public UseToolCard10Request(int diceChosen, String username, boolean isSingle) {
        this.diceChosen = diceChosen;
        this.username = username;
        this.isSingle = isSingle;
    }

    public String username;
    public boolean isSingle;

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
