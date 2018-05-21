package it.polimi.ingsw;

import java.util.List;

public class ProposeWindowResponse implements Response {

    List<String> list;
    public ProposeWindowResponse(List<String> list) {
        this.list = list;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
