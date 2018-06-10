package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.responses.Response;
import it.polimi.ingsw.socket.responses.ToolCardUsedByOthersResponse;

import java.rmi.RemoteException;
import java.util.Scanner;

public class MoveDiceIgnoringColorRestrEffect implements Effect {

    private int price;

    public MoveDiceIgnoringColorRestrEffect() {

        price = 1;
    }

    @Override
    public boolean applyEffect(Player player, Match match) {
        PlayerMultiplayer p = (PlayerMultiplayer) player;
        MatchMultiplayer m =(MatchMultiplayer) match; 
        WindowPatternCard schema = player.getSchemeCard();

        int row = player.getStartX1();
        int column = player.getStartY1();
        Dice dice = schema.getDice(row, column);
        if(p.getNumFavorTokens() >= price) {
            if (dice != null) {
                int newRow = player.getFinalX1();
                int newColumn = player.getFinalY1();
                System.out.println(newRow);
                System.out.println(newColumn);
                System.out.println(dice.toString());
                schema.removeDice(row, column);
                schema.putDiceIgnoringColorConstraint(dice, newRow, newColumn); //DA RIVEDERE
                System.out.println(newRow);
                System.out.println(newColumn);
                System.out.println(row);
                System.out.println(column);
                if (dice.equals(schema.getWindow()[newRow][newColumn].getDice())) {
                    player.setStartX1(5);//UNREACHABLE VALUE, USED TO RESET
                    player.setFinalX1(5);
                    player.setStartY1(4);//UNREACHABLE VALUE, USED TO RESET
                    player.setFinalY1(4);
                    p.setNumFavorTokens(p.getNumFavorTokens() - price);
                    price = 2;

                    //NOTIFY TO OTHERS
                    Response response = new ToolCardUsedByOthersResponse( p.getName(),2);
                    for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
                        if (!otherPlayer.getName().equals(p.getName())) {
                            if (m.getRemoteObservers().get(otherPlayer) != null) {
                                try {
                                    m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers( p.getName(),2);
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
                    schema.putDice(dice, row, column);
                    player.setStartX1(5);//UNREACHABLE VALUE, USED TO RESET
                    player.setFinalX1(5);
                    player.setStartY1(4);//UNREACHABLE VALUE, USED TO RESET
                    player.setFinalY1(4);
                    return false;
                }
            } else {
                player.setStartX1(5);//UNREACHABLE VALUE, USED TO RESET
                player.setFinalX1(5);
                player.setStartY1(4);//UNREACHABLE VALUE, USED TO RESET
                player.setFinalY1(4);
                return false;
            }
        }else
            return false;

    }

}
