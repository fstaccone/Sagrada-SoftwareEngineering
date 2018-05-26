package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard9Request implements Request {

    public int diceChosen;
    public int finalX;
    public int finalY;
    public String username;
    public boolean isSingle;

    public UseToolCard9Request(int diceChosen, int finalX, int finalY, String username, boolean isSingle) {
        this.diceChosen = diceChosen;
        this.finalX = finalX;
        this.finalY = finalY;
        this.username=username;
        this.isSingle=isSingle;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
