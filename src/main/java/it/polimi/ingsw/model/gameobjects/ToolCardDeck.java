package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gameobjects.effects.Effect;
import it.polimi.ingsw.model.gameobjects.effects.ReRollDiceEffect;
import it.polimi.ingsw.model.gameobjects.effects.UpsideDownDiceEffect;

import java.util.HashSet;

public class ToolCardDeck extends Deck<ToolCard>{



    public ToolCardDeck() {
        cards = new HashSet<>();

        // First card
        cards.add(new ToolCard( "pennelloPerPastaSalda", new ReRollDiceEffect());

        // Second card
        cards.add(new ToolCard( "tamponeDiamantato", new UpsideDownDiceEffect()));

    }

    // methods
}
