package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class PrivateCardChosenRequest implements Request {

    private String username;
    private int cardPosition;

    public PrivateCardChosenRequest(String username, int cardPosition) {
        this.username = username;
        this.cardPosition = cardPosition;
    }

    public String getUsername() {
        return username;
    }

    public int getCardPosition() {
        return cardPosition;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }

}
