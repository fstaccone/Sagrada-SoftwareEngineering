package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.control.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard6Request implements Request {

    private int diceChosen;
    private String username;
    private boolean isSingle;
    private int diceToBeSacrificed;

    public UseToolCard6Request(int diceToBeSacrificed, int diceChosen, String username, boolean isSingle) {
        this.diceChosen = diceChosen;
        this.username = username;
        this.isSingle = isSingle;
        this.diceToBeSacrificed = diceToBeSacrificed;
    }

    public int getDiceChosen() {
        return diceChosen;
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
