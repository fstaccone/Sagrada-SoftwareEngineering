package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

public class AfterReconnectionResponse implements Response {

    private String toolcards;
    private String publicCards;
    private String privateCard;

    public AfterReconnectionResponse(String toolcards, String publicCards, String privateCard) {
        this.toolcards = toolcards;
        this.publicCards = publicCards;
        this.privateCard = privateCard;
    }

    public String getToolcards() {
        return toolcards;
    }

    public String getPublicCards() {
        return publicCards;
    }

    public String getPrivateCard() {
        return privateCard;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
