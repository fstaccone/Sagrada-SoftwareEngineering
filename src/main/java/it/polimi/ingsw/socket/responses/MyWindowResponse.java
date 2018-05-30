package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.ResponseHandler;

public class MyWindowResponse implements Response {

    public WindowPatternCard window;

    public MyWindowResponse(WindowPatternCard window) {
        this.window = window;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
