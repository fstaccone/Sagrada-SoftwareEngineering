package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.socket.ResponseHandler;

public class NameAlreadyTakenResponse implements Response {

    private ConnectionStatus status;

    public NameAlreadyTakenResponse(ConnectionStatus status) {
        this.status = status;
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
