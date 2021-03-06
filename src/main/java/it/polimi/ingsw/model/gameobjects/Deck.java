package it.polimi.ingsw.model.gameobjects;

import java.util.ArrayList;
import java.util.List;

public abstract class Deck<T> {
    protected List<String> deck;
    protected List<T> pickedCards;

    /**
     * Constructor for Deck.
     *
     */
    public Deck() {
        this.deck = new ArrayList<>();
        this.pickedCards = new ArrayList<>();
    }

    public List<T> getPickedCards() {
        return this.pickedCards;
    }

}
