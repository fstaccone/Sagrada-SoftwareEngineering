package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.control.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard5Request implements Request {

    private int diceChosen;
    private int roundChosen;
    private int diceChosenFromRound;
    private String name;
    private boolean isSingle;
    private int diceToBeSacrificed;

    public UseToolCard5Request(int diceToBeSacrificed, int diceChosen, int roundChosen, int diceChosenFromRound, String name, boolean isSingle) {
        this.diceChosen = diceChosen;
        this.roundChosen = roundChosen;
        this.diceChosenFromRound = diceChosenFromRound;
        this.name = name;
        this.isSingle = isSingle;
        this.diceToBeSacrificed = diceToBeSacrificed;
    }

    public int getDiceChosen() {
        return diceChosen;
    }

    public int getRoundChosen() {
        return roundChosen;
    }

    public int getDiceChosenFromRound() {
        return diceChosenFromRound;
    }

    public String getName() {
        return name;
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
