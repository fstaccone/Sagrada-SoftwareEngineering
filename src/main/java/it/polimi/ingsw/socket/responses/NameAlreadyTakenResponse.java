package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class NameAlreadyTakenResponse implements Response {
    public boolean nameAlreadyTaken;

    public NameAlreadyTakenResponse(boolean nameAlreadyTaken) {
        this.nameAlreadyTaken = nameAlreadyTaken;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
