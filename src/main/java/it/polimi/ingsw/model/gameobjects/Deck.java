package it.polimi.ingsw.model.gameobjects;

import java.util.Set;

public abstract class Deck<T> {
    protected Set<T> cards;

    public abstract Card pickOneCard();

    public abstract Set<Card> pickNCards(int num);

    public Set<T> getCards() {
        return cards;
    }
}
