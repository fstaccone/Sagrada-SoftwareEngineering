package it.polimi.ingsw.socket.responses;

import it.polimi.ingsw.control.ResponseHandler;

import java.util.List;
import java.util.Map;

public class AfterReconnectionResponse implements Response {


    private String toolcards;
    private String publicCards;
    private List<String> privateCard;
    private String reserve;
    private String roundTrack;
    private int myTokens;
    private String[][] schemeCard;
    private String schemeCardName;
    private Map<String, Integer> otherTokens;
    private Map<String, String[][]> otherSchemeCards;
    private boolean schemeCardChosen;
    private Map<String, Integer> toolcardsPrices;
    private Map<String, String> otherSchemeCardNamesMap;

    public AfterReconnectionResponse(String toolcards, String publicCards, List<String> privateCard, String reserve, String roundTrack, int myTokens, String[][] schemeCard, String schemeCardName, Map<String, Integer> otherTokens, Map<String, String[][]> otherSchemeCards, Map<String, String> otherSchemeCardNamesMap, boolean schemeCardChosen, Map<String, Integer> toolcardsPrices) {
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
        this.schemeCardName = schemeCardName;
        this.otherSchemeCardNamesMap = otherSchemeCardNamesMap;
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

    public String getReserve() {
        return reserve;
    }

    public String getRoundTrack() {
        return roundTrack;
    }

    public int getMyTokens() {
        return myTokens;
    }

    public String[][] getSchemeCard() {
        return schemeCard;
    }

    public String getSchemeCardName() {
        return schemeCardName;
    }

    public Map<String, Integer> getOtherTokens() {
        return otherTokens;
    }

    public Map<String, String[][]> getOtherSchemeCards() {
        return otherSchemeCards;
    }

    public boolean isSchemeCardChosen() {
        return schemeCardChosen;
    }

    public Map<String, Integer> getToolcardsPrices() {
        return toolcardsPrices;
    }

    public Map<String, String> getOtherSchemeCardNamesMap() {
        return otherSchemeCardNamesMap;
    }

    @Override
    public void handleResponse(ResponseHandler handler) {
        handler.handle(this);
    }
}
