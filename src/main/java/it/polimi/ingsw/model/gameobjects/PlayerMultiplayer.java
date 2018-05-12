package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.model.gamelogic.Match;
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

    public Colors getColor() { return this.color; }

    public ConnectionStatus getStatus() { return status; }

    public int getTurnsLeft() { return turnsLeft; }

    public boolean isMyTurn() { return myTurn; }

    public String getName(){
        return this.name;
    }

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

    // todo: controllo
    @Override
    public void playTurn() {
        if(status == ConnectionStatus.READY) {
            // attesa di azioni da parte del client
        }
        turnsLeft--;
        // solo per ora, il giocatore potrà decidere quando passare e in quel caso verrà chiamata la cancel
        match.getTimer().cancel();
    }

    @Override
    public void goTrough() {
        // passa il turno

    }

    // it'll be called by the timer if it would go out of time
    public void expiredTimer(){
        // passa ma senza richiamare la cancel sul timer
    }

    // todo: aggiornare metodo chooseDice di Reserve
    public void chooseDice(){
        setPickedDice(match.getBoard().getReserve().chooseDice());
    }
    /* todo: gestire la sequenza di passaggi di parametri
    public void putDiceInWindow(){
        getSchemeCard().putDice();
    }
    */
}
