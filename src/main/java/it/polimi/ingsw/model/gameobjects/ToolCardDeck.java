package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gameobjects.effects.Effect;
import it.polimi.ingsw.model.gameobjects.effects.ReRollDiceEffect;
import it.polimi.ingsw.model.gameobjects.effects.UpsideDownDiceEffect;

import java.util.HashSet;
import java.util.Set;

public class ToolCardDeck extends Deck<ToolCard>{



    public ToolCardDeck() {
        cards = new HashSet<>();

        // First card
        cards.add(new ToolCard( "pennelloPerPastaSalda"));

        // Second card
        cards.add(new ToolCard( "tamponeDiamantato"));

    }

    @Override
    public Card pickOneCard() {
        return null;
    }

    @Override
    public Set<Card> pickNCards(int num) {
        return null;
    }

    // methods
}
