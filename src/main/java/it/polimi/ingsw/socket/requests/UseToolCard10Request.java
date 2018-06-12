package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard10Request implements Request {

    public int diceChosen;
    public String username;
    public boolean isSingle;
    public int diceToBeSacrificed;

    public UseToolCard10Request(int diceToBeSacrificed,int diceChosen, String username, boolean isSingle) {
        this.diceChosen = diceChosen;
        this.username = username;
        this.isSingle = isSingle;
        this.diceToBeSacrificed=diceToBeSacrificed;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
