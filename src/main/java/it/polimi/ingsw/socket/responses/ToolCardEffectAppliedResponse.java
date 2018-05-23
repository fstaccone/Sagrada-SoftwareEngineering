package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class ToolCardEffectAppliedResponse implements Response {

    public boolean effectApplied;

    public ToolCardEffectAppliedResponse(boolean effectApplied) {
        this.effectApplied = effectApplied;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
