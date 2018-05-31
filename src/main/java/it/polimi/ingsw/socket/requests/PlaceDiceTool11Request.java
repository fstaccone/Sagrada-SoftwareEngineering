package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class PlaceDiceTool11Request implements Request {

    public int coordinateX;
    public int coordinateY;
    public String username;
    public boolean single;

    public PlaceDiceTool11Request( int coordinateX, int coordinateY, String username, boolean single) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.username = username;
        this.single = single;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}