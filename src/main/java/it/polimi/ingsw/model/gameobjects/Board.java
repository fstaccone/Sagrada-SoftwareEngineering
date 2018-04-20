package it.polimi.ingsw.model.gameobjects;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
    private Set<PublicObjectiveCard> pickedPublicObjectiveCards;
    private Set<Player> players;
    private Reserve reserve;
    private List<ToolCard> pickedToolCards;

    public Board(Match match, Set<Player> players, List <ToolCard> pickedToolCards) {
        this.pickedPublicObjectiveCards = new HashSet<>();

        // To be managed later, even in Match
        this.players = players;

        this.reserve = new Reserve();
        this.pickedToolCards = pickedToolCards;
    }

    public Reserve getReserve() {
        return reserve;
    }


}

