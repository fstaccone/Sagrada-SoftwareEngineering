package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.control.ResponseHandler;

import java.util.List;

public class GameStartedResponse implements Response {

    private boolean windowChosen;
    private List<String> names;

    public GameStartedResponse(boolean windowChosen, List<String> names) {
        System.out.println("13 GameStartedResponse");
        this.windowChosen = windowChosen;
        this.names = names;
    }

    public boolean isWindowChosen() {
        return windowChosen;
    }

    public List<String> getNames() {
        return names;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
