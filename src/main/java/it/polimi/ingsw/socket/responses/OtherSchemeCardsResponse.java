package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.ResponseHandler;

public class OtherSchemeCardsResponse implements Response {

    public String[][] scheme;
    public String name;
    public String cardName;

    public OtherSchemeCardsResponse(String[][] scheme, String name, String cardName) {
        this.scheme = scheme;
        this.name = name;
        this.cardName=cardName;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
