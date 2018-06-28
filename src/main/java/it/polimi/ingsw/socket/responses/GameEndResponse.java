package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.control.ResponseHandler;

import java.util.List;

public class GameEndResponse implements Response {

    private String winner;
    private List<String> names;
    private List<Integer> values;

    public GameEndResponse(String winner, List<String> names, List<Integer> values) {
        this.winner = winner;
        this.names = names;
        this.values = values;
    }

    public String getWinner() {
        return winner;
    }

    public List<String> getNames() {
        return names;
    }

    public List<Integer> getValues() {
        return values;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
