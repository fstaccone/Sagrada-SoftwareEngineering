package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard5Request implements Request {

    public int diceChosen;
    public int roundChosen;
    public int diceChosenFromRound;
    public String name;
    public boolean isSingle;

    public UseToolCard5Request(int diceChosen, int roundChosen, int diceChosenFromRound, String name, boolean isSingle) {
        this.diceChosen = diceChosen;
        this.roundChosen = roundChosen;
        this.diceChosenFromRound = diceChosenFromRound;
        this.name=name;
        this.isSingle=isSingle;
    }


    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
