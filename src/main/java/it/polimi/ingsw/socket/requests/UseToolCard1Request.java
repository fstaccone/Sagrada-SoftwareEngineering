package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard1Request implements Request {

    public int diceChosen;
    public String IncrOrDecr;
    public String username;
    public boolean isSingle;
    public int diceToBeSacrificed;

    public UseToolCard1Request(int diceToBeSacrificed, int diceChosen, String incrOrDecr, String username, boolean isSingle) {
        this.diceChosen = diceChosen;
        IncrOrDecr = incrOrDecr;
        this.username = username;
        this.isSingle = isSingle;
        this.diceToBeSacrificed = diceToBeSacrificed;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
