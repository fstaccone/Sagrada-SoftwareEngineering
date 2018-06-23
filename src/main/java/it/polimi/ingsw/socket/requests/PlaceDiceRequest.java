package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class PlaceDiceRequest implements Request {

    private int diceChosen;
    private int coordinateX;
    private int coordinateY;
    private String username;
    private boolean single;

    public PlaceDiceRequest(int diceChosen, int coordinateX, int coordinateY, String username, boolean single) {
        this.diceChosen = diceChosen;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.username = username;
        this.single = single;
    }

    public int getDiceChosen() {
        return diceChosen;
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
