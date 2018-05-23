package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class ShowWindowResponse implements Response {

    public String string;
    public ShowWindowResponse(String string) {
        this.string=string;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
