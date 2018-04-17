package it.polimi.ingsw.model.GameObjects;

public enum DiceValue {
    UNO(1), DUE(2), TRE(3), QUATTRO(4), CINQUE(5), SEI(6);
    int value;
    DiceValue(int i) {
        this.value=i;
    }
}
