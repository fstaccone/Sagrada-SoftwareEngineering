package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.socket.ResponseHandler;

public class DiceColorResponse implements Response {

    public Colors diceColor;

    public DiceColorResponse(Colors color) {
        this.diceColor = color;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
