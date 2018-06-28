package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.control.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard1Request implements Request {

    private int diceChosen;
    private String IncrOrDecr;
    private String username;
    private boolean isSingle;
    private int diceToBeSacrificed;

    public UseToolCard1Request(int diceToBeSacrificed, int diceChosen, String incrOrDecr, String username, boolean isSingle) {
        this.diceChosen = diceChosen;
        IncrOrDecr = incrOrDecr;
        this.username = username;
        this.isSingle = isSingle;
        this.diceToBeSacrificed = diceToBeSacrificed;
    }

    public int getDiceChosen() {
        return diceChosen;
    }

    public String getIncrOrDecr() {
        return IncrOrDecr;
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
