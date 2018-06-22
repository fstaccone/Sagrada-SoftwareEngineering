package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.ResponseHandler;

import java.util.List;
import java.util.Map;

public class AfterReconnectionResponse implements Response {


    public String toolcards;
    public String publicCards;
    public List<String> privateCard;
    public String reserve;
    public String roundTrack;
    public int myTokens;
    public String[][] schemeCard;
    public String schemeCardName;
    public Map<String, Integer> otherTokens;
    public Map<String, WindowPatternCard> otherSchemeCards;
    public boolean schemeCardChosen;
    public Map<String, Integer> toolcardsPrices;

    public AfterReconnectionResponse(String toolcards, String publicCards, List<String> privateCard, String reserve, String roundTrack, int myTokens, String[][] schemeCard,String schemeCardName, Map<String, Integer> otherTokens, Map<String, WindowPatternCard> otherSchemeCards, boolean schemeCardChosen, Map<String, Integer> toolcardsPrices) {
        this.toolcards = toolcards;
        this.publicCards = publicCards;
        this.privateCard = privateCard;
        this.reserve = reserve;
        this.roundTrack = roundTrack;
        this.myTokens = myTokens;
        this.schemeCard = schemeCard;
        this.otherTokens = otherTokens;
        this.otherSchemeCards = otherSchemeCards;
        this.schemeCardChosen = schemeCardChosen;
        this.toolcardsPrices = toolcardsPrices;
        this.schemeCardName=schemeCardName;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
