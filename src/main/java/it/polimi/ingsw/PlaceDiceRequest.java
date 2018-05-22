package it.polimi.ingsw;

public class PlaceDiceRequest implements Request {

    public int diceChosen;
    public int coordinateX;
    public int coordinateY;
    public String username;
    public boolean single;

    public PlaceDiceRequest(int diceChosen, int coordinateX, int coordinateY, String username, boolean single) {
        this.diceChosen = diceChosen;
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
