package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.ResponseHandler;

public class OtherSchemeCardsResponse implements Response {

    private String[][] scheme;
    private String name;
    private String cardName;

    public OtherSchemeCardsResponse(String[][] scheme, String name, String cardName) {
        this.scheme = scheme;
        this.name = name;
        this.cardName=cardName;
    }

    public String[][] getScheme() {
        return scheme;
    }

    public String getName() {
        return name;
    }

    public String getCardName() {
        return cardName;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
