package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.socket.responses.Response;
import it.polimi.ingsw.socket.responses.ToolCardUsedByOthersResponse;

import java.rmi.RemoteException;
import java.util.Scanner;

public class MoveTwoDicesColorRoundTrackEffect implements Effect { //todo

    private int price;

    public MoveTwoDicesColorRoundTrackEffect() {
        price = 1;
    }

    @Override
    public boolean applyEffect(Player player, Match match) {

        PlayerMultiplayer p = (PlayerMultiplayer) player; //USEFUL JUST FOR TOKENS, TO BE FIXED
        MatchMultiplayer m=(MatchMultiplayer) match;

        Colors color = match.getBoard().getRoundTrack().getColorOfAChosenDice(player.getRound(),player.getDiceChosenFromRound());

        WindowPatternCard schema = player.getSchemeCard();
        Dice dice2=null;

        int row1= player.getStartX1();
        int column1 = player.getStartY1();
        int row2 = player.getStartX2();
        int column2 = player.getStartY2();
        Dice dice1 = schema.getDice(row1, column1);
        if(row2!=-1&&column2!=-1) {
             dice2 = schema.getDice(row2, column2);
        }

        if(p.getNumFavorTokens() >= price) {
            if (dice1 != null && dice2!=null && dice1.getColor().equals(color) && dice2.getColor().equals(color)) {
                int newRow1 = player.getFinalX1();
                int newColumn1 = player.getFinalY1();
                int newRow2 = player.getFinalX2();
                int newColumn2 = player.getFinalY2();
                schema.removeDice(row1, column1);
                schema.removeDice(row2, column2);
                if (schema.putDice(dice1, newRow1, newColumn1) && schema.putDice(dice2, newRow2, newColumn2)) {
                    p.setNumFavorTokens(p.getNumFavorTokens() - price);
                    price = 2;
                    //NOTIFY TO OTHERS
                    Response response = new ToolCardUsedByOthersResponse( p.getName(),12);
                    for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
                        if (!otherPlayer.getName().equals(p.getName())) {
                            if (m.getRemoteObservers().get(otherPlayer) != null) {
                                try {
                                    m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers( p.getName(),12);
                                } catch (RemoteException e) {
                                    m.getLobby().disconnect(otherPlayer.getName());
                                    System.out.println("Player " + p.getName() + " disconnected!");
                                }
                            }
                            m.notifyToSocketClient(otherPlayer, response);
                        }
                    }
                    return true;
                } else {
                    if(dice1.equals(schema.getDice(newRow1, newColumn1))) {
                        schema.removeDice(newRow1, newColumn1);
                    }
                    if(dice2.equals(schema.getDice(newRow2, newColumn2))) {
                        schema.removeDice(newRow2, newColumn2);
                    }
                    schema.putDice(dice1, row1, column1);
                    schema.putDice(dice2, row2, column2);
                    return false;
                }
            }
            else if (dice1 != null && dice2==null && dice1.getColor().equals(color)) {
                int newRow1 = player.getFinalX1();
                int newColumn1 = player.getFinalY1();
                schema.removeDice(row1, column1);
                if (schema.putDice(dice1, newRow1, newColumn1) ) {
                    p.setNumFavorTokens(p.getNumFavorTokens() - price);
                    price = 2;

                    //NOTIFY TO OTHERS
                    Response response = new ToolCardUsedByOthersResponse( p.getName(),12);
                    for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
                        if (!otherPlayer.getName().equals(p.getName())) {
                            if (m.getRemoteObservers().get(otherPlayer) != null) {
                                try {
                                    m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers( p.getName(),12);
                                } catch (RemoteException e) {
                                    m.getLobby().disconnect(otherPlayer.getName());
                                    System.out.println("Player " + p.getName() + " disconnected!");
                                }
                            }
                            m.notifyToSocketClient(otherPlayer, response);
                        }
                    }
                    return true;
                } else {
                    schema.putDice(dice1, row1, column1);
                    return false;
                }
            } else
                return false;
        } else
            return false;
    }
}
