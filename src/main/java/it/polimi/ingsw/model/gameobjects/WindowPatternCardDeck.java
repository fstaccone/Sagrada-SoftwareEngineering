package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gameobjects.windowpatterncards.*;

import java.util.HashSet;
import java.util.Set;

public class WindowPatternCardDeck extends Deck<WindowPatternCard>{

    public WindowPatternCardDeck() {
        cards = new HashSet<>();
        cards.add(new ChromaticSplendor());
        cards.add(new Comitas());
        cards.add(new Firelight());
        cards.add(new FractalDrops());
        cards.add(new FulgorDelCielo());
        cards.add(new Gravitas());
        cards.add(new LuxAstram());
        cards.add(new LuxMundi());
        cards.add(new LuzCelestial());
        cards.add(new RipplesOfLight());
        cards.add(new SunSGlory());
        cards.add(new WaterOfLife());
    }

    @Override
    public Card pickOneCard() {
        return null;
    }

    @Override
    public Set<Card> pickNCards(int num) {
        return null;
    }
}
