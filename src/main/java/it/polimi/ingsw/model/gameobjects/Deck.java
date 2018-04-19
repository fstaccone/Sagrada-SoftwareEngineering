package it.polimi.ingsw.model.gameobjects;

import java.util.Set;

public abstract class Deck<T> {
    Set<T> cards;

    public abstract Card pickOneCard();

    public abstract Set<Card> pickNCards(int num);

}
