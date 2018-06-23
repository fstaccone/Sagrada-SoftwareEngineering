package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class RoundTrackResponse implements Response {

    private String roundTrack;

    public RoundTrackResponse(String roundTrack) {
        this.roundTrack = roundTrack;
    }

    public String getRoundTrack() {
        return roundTrack;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
