package it.polimi.ingsw;

public class ToolCardsResponse implements Response {

    public String string;

    public ToolCardsResponse(String string) {
        this.string = string;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
