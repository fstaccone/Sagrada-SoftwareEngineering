package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;

public class PlayerMultiplayer extends Player {
    private int numFavorTokens;
    private PrivateObjectiveCard privateObjectiveCard;
    private ConnectionStatus status;
    private int turnsLeft;
    private boolean myTurn;
    private MatchMultiplayer match;

    public PlayerMultiplayer(String name, MatchMultiplayer match) {
        super(name);
        status = ConnectionStatus.READY;
        myTurn = false;
        this.match = match;
    }

    // setter
    public void setNumFavorTokens(int numFavorTokens) {
        this.numFavorTokens = numFavorTokens;
    }


    public void setPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) {
        this.privateObjectiveCard = privateObjectiveCard;
    }

    public void setStatus(ConnectionStatus status) { this.status = status; }

    public void setTurnsLeft(int turnsLeft) { this.turnsLeft = turnsLeft; }

    public void setMyTurn(boolean myTurn) { this.myTurn = myTurn; }

    // getters
    public int getNumFavorTokens() {
        return numFavorTokens;
    }

    public PrivateObjectiveCard getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }

    public ConnectionStatus getStatus() { return status; }

    public int getTurnsLeft() { return turnsLeft; }

    public boolean isMyTurn() { return myTurn; }

    @Override
    public void setSchemeCard(WindowPatternCard schemeCard) {
        this.schemeCard = schemeCard;
        this.setNumFavorTokens(schemeCard.getDifficulty());
    }

    @Override
    public void useToolCard(ToolCard chosenToolCardToUse){//il controller fa player1.useToolCard(): può passare la carta scelta perchè il controller ha riferimento alla board e pertanto alle pickedToolCards(attributo di board) tra le quali fa scegliere al client quale usare(ammesso che possa-->va fatto un check)(si tratta di un'azione precedente)
        // non ha più modo di avere il riferimento a match
        //chosenToolCardToUse.useCard(this,this.room.getMatch());
    }

    public Dice chooseDice(int index){
        return match.getBoard().getReserve().chooseDice(index);
    }

}
