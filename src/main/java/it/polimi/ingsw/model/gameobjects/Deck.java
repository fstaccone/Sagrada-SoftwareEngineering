package it.polimi.ingsw.model.gameobjects;

import java.util.ArrayList;
import java.util.List;

public abstract class Deck{
    protected List<String> deck;
    protected List<Card> pickedCards;


    public  Deck() {
        this.deck=new ArrayList<>();
        this.pickedCards=new ArrayList<>();
    }
     public List getPickedCards(){
        return this.pickedCards;
    }

}
