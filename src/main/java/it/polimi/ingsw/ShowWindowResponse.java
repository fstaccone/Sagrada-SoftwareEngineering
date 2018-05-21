package it.polimi.ingsw;

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
