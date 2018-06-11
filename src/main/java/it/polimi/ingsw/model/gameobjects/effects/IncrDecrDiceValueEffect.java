package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.socket.responses.Response;
import it.polimi.ingsw.socket.responses.ToolCardUsedByOthersResponse;

import java.rmi.RemoteException;

public class IncrDecrDiceValueEffect implements Effect {

    private Integer price;

    public IncrDecrDiceValueEffect() {
        price = 1;
    }

    @Override
    public boolean applyEffect(Player player, Match match) {
        PlayerMultiplayer p = (PlayerMultiplayer) player;
        MatchMultiplayer m = (MatchMultiplayer) match;

        String plusOrMinus = player.getChoise();
        if (p.getNumFavorTokens() >= price) {
            if (player.getDice() < match.getBoard().getReserve().getDices().size()) {
                Dice dice = match.getBoard().getReserve().getDices().get(player.getDice());
                if (dice != null) {//PROBABILMENTE INUTILE
                    int value = dice.getValue();
                    switch (plusOrMinus) {
                        case "+":
                            if (value != 6) {
                                value = value + 1;
                                match.getBoard().getReserve().getDices().get(player.getDice()).setValue(value); //player.getDice() è l'indice
                                p.setNumFavorTokens(p.getNumFavorTokens() - price);
                                if(price.equals(1)) {
                                    //NOTIFY TO OTHERS
                                    Response response = new ToolCardUsedByOthersResponse(p.getName(), 1);
                                    for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
                                        if (!otherPlayer.getName().equals(p.getName())) {
                                            if (m.getRemoteObservers().get(otherPlayer) != null) {
                                                try {
                                                    m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers(p.getName(), 1);
                                                } catch (RemoteException e) {
                                                    m.getLobby().disconnect(otherPlayer.getName());
                                                    System.out.println("Player " + p.getName() + " disconnected!");
                                                }
                                            }
                                            m.notifyToSocketClient(otherPlayer, response);
                                        }
                                    }
                                    price = 2;
                                    m.getToolCardsPrices().put("Carta utensile 1: ",price);
                                }
                                return true;
                            } else return false;

                        case "-":
                            if (value != 1) {
                                value = value - 1;
                                match.getBoard().getReserve().getDices().get(player.getDice()).setValue(value);
                                p.setNumFavorTokens(p.getNumFavorTokens() - price);
                                if(price==1) {
                                    //NOTIFY TO OTHERS
                                    Response response = new ToolCardUsedByOthersResponse(p.getName(), 1);
                                    for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
                                        if (!otherPlayer.getName().equals(p.getName())) {
                                            if (m.getRemoteObservers().get(otherPlayer) != null) {
                                                try {
                                                    m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers(p.getName(), 1);
                                                } catch (RemoteException e) {
                                                    m.getLobby().disconnect(otherPlayer.getName());
                                                    System.out.println("Player " + p.getName() + " disconnected!");
                                                }
                                            }
                                            m.notifyToSocketClient(otherPlayer, response);
                                        }
                                    }
                                    price = 2;
                                }
                                return true;
                            } else return false;

                        default:
                            return false;
                    }
                } else return false;
            } else return false;
        } else return false;
    }
}
