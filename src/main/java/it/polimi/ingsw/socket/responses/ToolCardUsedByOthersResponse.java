package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.control.ResponseHandler;

public class ToolCardUsedByOthersResponse implements Response {

    private String name;
    private int toolCardNumber;

    public ToolCardUsedByOthersResponse(String name, int toolCardNumber) {
        this.name = name;
        this.toolCardNumber = toolCardNumber;
    }

    public String getName() {
        return name;
    }

    public int getToolCardNumber() {
        return toolCardNumber;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}

