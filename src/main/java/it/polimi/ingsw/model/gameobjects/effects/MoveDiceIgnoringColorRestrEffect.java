package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.socket.responses.Response;
import it.polimi.ingsw.socket.responses.ToolCardUsedByOthersResponse;

import java.rmi.RemoteException;

public class MoveDiceIgnoringColorRestrEffect implements Effect {

    private Integer price;

    public MoveDiceIgnoringColorRestrEffect() {
        price = 1;
    }

    @Override
    public boolean applyEffect(Player player, Match match) {
        WindowPatternCard schema = player.getSchemeCard();
        int row = player.getStartX1();
        int column = player.getStartY1();
        Dice dice = schema.getDice(row, column);
        //SINGLEPLAYER
        if (player.getDiceToBeSacrificed() != 9) {
            Dice sacrificeDice = match.getBoard().getReserve().getDices().get(player.getDiceToBeSacrificed());
            if (sacrificeDice.getColor().equals(Colors.BLUE)&&dice != null) {
                putDice(dice,schema,player,row,column);
                if (dice.equals(schema.getWindow()[player.getFinalX1()][player.getFinalY1()].getDice())) {
                    match.getBoard().getReserve().getDices().remove(sacrificeDice);
                    return true;
                } else {
                    schema.putDiceBack(dice, row, column);

                    return false;
                }
            } else {
                return false;
            }
        }
        //MULTIPLAYER
        else {
            PlayerMultiplayer p = (PlayerMultiplayer) player;
            MatchMultiplayer m = (MatchMultiplayer) match;
            if (p.getNumFavorTokens() >= price) {
                if (dice != null) {
                    putDice(dice,schema,player,row,column);
                    if (dice.equals(schema.getWindow()[p.getFinalX1()][p.getFinalY1()].getDice())) {
                        p.setNumFavorTokens(p.getNumFavorTokens() - price);
                        if (price.equals(1)) {
                            //NOTIFY TO OTHERS
                            Response response = new ToolCardUsedByOthersResponse(p.getName(), 2);
                            for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
                                if (!otherPlayer.getName().equals(p.getName())) {
                                    if (m.getRemoteObservers().get(otherPlayer) != null) {
                                        try {
                                            m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers(p.getName(), 2);
                                        } catch (RemoteException e) {
                                            m.getLobby().disconnect(otherPlayer.getName());
                                            System.out.println("Player " + p.getName() + " disconnected!");
                                        }
                                    }
                                    m.notifyToSocketClient(otherPlayer, response);
                                }
                            }
                            price = 2;
                            m.getToolCardsPrices().put("Carta utensile 2: ", price);
                        }
                        return true;
                    } else {
                        schema.putDiceBack(dice, row, column);
                        return false;
                    }
                } else {
                    return false;
                }
            } else
                return false;

        }
    }

    private void putDice(Dice dice, WindowPatternCard schema, Player player, int row, int column){
        int newRow = player.getFinalX1();
        int newColumn = player.getFinalY1();
        schema.removeDice(row, column);
        schema.putDiceIgnoringColorConstraint(dice, newRow, newColumn);
    }

}
