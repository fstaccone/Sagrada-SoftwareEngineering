package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.socket.ResponseHandler;

import java.util.List;

public class InitializationResponse implements Response {

    private String toolcards;
    private String publicCards;
    private List<String> privateCard;
    private List<String> players;

    public InitializationResponse(String toolcards, String publicCards, List<String> privateCard, List<String> players) {
        this.toolcards = toolcards;
        this.publicCards = publicCards;
        this.privateCard = privateCard;
        this.players = players;
    }

    public String getToolcards() {
        return toolcards;
    }

    public String getPublicCards() {
        return publicCards;
    }

    public List<String> getPrivateCard() {
        return privateCard;
    }

    public List<String> getPlayers() { return players; }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
