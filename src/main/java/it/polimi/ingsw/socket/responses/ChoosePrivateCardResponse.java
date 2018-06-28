package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.control.ResponseHandler;

public class ChoosePrivateCardResponse implements Response {

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
