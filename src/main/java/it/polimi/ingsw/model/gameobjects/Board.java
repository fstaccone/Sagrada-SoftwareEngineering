package it.polimi.ingsw.model.gameobjects;

import java.util.HashSet;
import java.util.Set;

public class Board {
    private Set<PublicObjectiveCard> pickedPublicObjectiveCards;
    private Set<Player> players;
    private Reserve reserve;
    private Set<ToolCard> pickedToolCards;

    public Board(Match match, Set<Player> players) {
        this.pickedPublicObjectiveCards = new HashSet<>();

        // To be managed later, even in Match
        this.players = players;

        this.reserve = new Reserve();
        this.pickedToolCards = new HashSet<>();
    }

    public Reserve getReserve() {
        return reserve;
    }
}

