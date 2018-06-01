package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gamelogic.Match;

import java.util.List;
import java.util.Map;

public class Board {
    private List<PublicObjectiveCard> pickedPublicObjectiveCards;
    //private int numOfPlayers // potrebbe essere utile per la GUI
    private Reserve reserve;
    private List<ToolCard> pickedToolCards;
    private RoundTrack roundTrack;
    private Map<Player, WindowPatternCard> windowPatternCardsMap; // Viene creata una mappa che associa la carta al giocatore

    public Board(Match match, List<ToolCard> pickedToolCards, List<PublicObjectiveCard> pickedPublicObjectiveCards) {
        // To be managed later, even in Match
        this.pickedToolCards = pickedToolCards;
        this.pickedPublicObjectiveCards = pickedPublicObjectiveCards;

        this.reserve = new Reserve();
        this.roundTrack = new RoundTrack();
    }

    public boolean findAndUseToolCard(int i, Player player, Match match) {
        for (ToolCard card : pickedToolCards) {
            if (Integer.parseInt(card.getToolID().replaceAll("tool", "")) == i) {
                System.out.println("BOARD28");
                return card.useCard(player, match);
            }
        }
        return false;
    }

    public Reserve getReserve() {
        return reserve;
    }


    public List<ToolCard> getPickedToolCards() {
        return pickedToolCards;
    }

    public List<PublicObjectiveCard> getPickedPublicObjectiveCards() {
        return pickedPublicObjectiveCards;
    }

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

}

