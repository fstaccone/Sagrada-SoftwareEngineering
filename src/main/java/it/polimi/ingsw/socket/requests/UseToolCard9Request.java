package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.control.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard9Request implements Request {

    private int diceChosen;
    private int finalX;
    private int finalY;
    private String username;
    private boolean isSingle;
    private int diceToBeSacrificed;

    public UseToolCard9Request(int diceToBeSacrificed, int diceChosen, int finalX, int finalY, String username, boolean isSingle) {
        this.diceChosen = diceChosen;
        this.finalX = finalX;
        this.finalY = finalY;
        this.username = username;
        this.isSingle = isSingle;
        this.diceToBeSacrificed = diceToBeSacrificed;
    }

    public int getDiceChosen() {
        return diceChosen;
    }

    public int getFinalX() {
        return finalX;
    }

    public int getFinalY() {
        return finalY;
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
