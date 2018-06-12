package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.ResponseHandler;

public class OtherSchemeCardsResponse implements Response {

    public WindowPatternCard scheme;
    public String name;

    public OtherSchemeCardsResponse(WindowPatternCard scheme, String name) {
        this.scheme = scheme;
        this.name = name;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
