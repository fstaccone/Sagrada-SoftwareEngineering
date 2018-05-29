package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class OtherSchemeCardsResponse implements Response{

    public String scheme;
    public String name;

    public OtherSchemeCardsResponse(String scheme, String name) {
        this.scheme = scheme;
        this.name = name;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
