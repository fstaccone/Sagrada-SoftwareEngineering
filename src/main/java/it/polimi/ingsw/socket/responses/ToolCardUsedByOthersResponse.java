package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class ToolCardUsedByOthersResponse implements Response {

    public String name;
    public int toolCardNumber;

    public ToolCardUsedByOthersResponse(String name, int toolCardNumber) {
        this.name = name;
        this.toolCardNumber=toolCardNumber;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}

