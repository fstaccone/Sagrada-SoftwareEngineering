package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

import java.util.List;

public class ProposeWindowResponse implements Response {

    public List<String> list;
    public ProposeWindowResponse(List<String> list) {
        this.list = list;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
