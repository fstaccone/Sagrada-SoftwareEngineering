package it.polimi.ingsw.model.gameobjects;


public class PrivateObjectiveCard extends ObjectiveCard{
    private Colors color;

    public PrivateObjectiveCard(Colors color) {
        super("PrivateObjectiveCard " + color.toString());
        this.color = color;
    }

    @Override
    public void useCard(Player player) {
        int temp = player.getPoints();
        Square[][] window = player.getSchemeCard().getWindow();
        //calculation algorithm
        for(Square[] row : window){
            for(Square spot : row){
                Colors diceColor = Colors.NONE;
                if(spot.getDice()!=null)
                    diceColor = spot.getDice().getColor();
                if(diceColor==color)
                    temp = temp + spot.getDice().getValue();
            }
        }

        player.setPoints(temp);
    }

    @Override
    public String toString() {
        return "PrivateObjectiveCard{" +
                "color=" + color +
                '}';
    }

}
