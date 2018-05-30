package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class RoundTrackResponse implements Response {

    public String roundTrack;

    public RoundTrackResponse(String roundTrack) {
        this.roundTrack = roundTrack;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
