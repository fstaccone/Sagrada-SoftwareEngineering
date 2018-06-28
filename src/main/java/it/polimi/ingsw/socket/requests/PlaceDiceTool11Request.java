package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.control.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class PlaceDiceTool11Request implements Request {

    private int coordinateX;
    private int coordinateY;
    private String username;
    private boolean single;

    public PlaceDiceTool11Request(int coordinateX, int coordinateY, String username, boolean single) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.username = username;
        this.single = single;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSingle() {
        return single;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}