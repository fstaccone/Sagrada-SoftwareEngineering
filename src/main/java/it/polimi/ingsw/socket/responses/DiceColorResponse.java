package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.control.ResponseHandler;
import it.polimi.ingsw.model.gameobjects.Colors;

public class DiceColorResponse implements Response {

    private Colors diceColor;

    public DiceColorResponse(Colors color) {
        this.diceColor = color;
    }

    public Colors getDiceColor() {
        return diceColor;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
