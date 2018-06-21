package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class ChoosePrivateCardResponse implements Response {

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
