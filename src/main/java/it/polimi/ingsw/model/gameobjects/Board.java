package it.polimi.ingsw.model.gameobjects;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
    private List<PublicObjectiveCard> pickedPublicObjectiveCards;
    private Set<Player> players;
    private Reserve reserve;
    private List<ToolCard> pickedToolCards;
    private RoundTrack roundTrack;

    public Board(Match match, Set<Player> players, List <ToolCard> pickedToolCards, List <PublicObjectiveCard> pickedPublicObjectiveCards) {
        // To be managed later, even in Match
        this.players = players;
        this.pickedToolCards = pickedToolCards;
        this.pickedPublicObjectiveCards = pickedPublicObjectiveCards;

        this.reserve = new Reserve();
        this.roundTrack = new RoundTrack();
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

