package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.control.ResponseHandler;

public class MyWindowResponse implements Response {

    private String[][] window;

    public MyWindowResponse(String[][] window) {
        this.window = window;
    }

    public String[][] getWindow() {
        return window;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
