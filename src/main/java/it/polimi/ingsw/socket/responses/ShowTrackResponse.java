package it.polimi.ingsw.socket.responses;


import it.polimi.ingsw.socket.ResponseHandler;

public class ShowTrackResponse implements Response {

    private String track;

    public ShowTrackResponse(String track) {
        this.track = track;
    }

    public String getTrack() {
        return track;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
