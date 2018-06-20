package it.polimi.ingsw.model.gameobjects.effects;


import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.socket.responses.Response;
import it.polimi.ingsw.socket.responses.ToolCardUsedByOthersResponse;

import java.rmi.RemoteException;


public class ReRollAllReserveDicesEffect implements Effect {

    private Integer price;
    private boolean used;

    public ReRollAllReserveDicesEffect() {
        price = 1;
        used=false;
    }

    @Override
    public boolean applyEffect(Player player, Match match) {

        Reserve reserve = match.getBoard().getReserve();

        //SINGLEPLAYER
        if (player.getDiceToBeSacrificed() != 9) {
            if(!used) {
                Dice sacrificeDice = match.getBoard().getReserve().getDices().get(player.getDiceToBeSacrificed());
                if (sacrificeDice.getColor().equals(Colors.BLUE) && player.getTurnsLeft() == 1) {
                    reserve.throwDices(reserve.removeAllDices());
                    match.getBoard().getReserve().getDices().remove(sacrificeDice);
                    used=true;
                    return true;
                } else {
                    return false;
                }
            }else return false;
        }
        //MULTIPLAYER
        else {
            PlayerMultiplayer p = (PlayerMultiplayer) player;
            MatchMultiplayer m = (MatchMultiplayer) match;
            if (p.getNumFavorTokens() >= price && player.getTurnsLeft() == 1) {
                reserve.throwDices(reserve.removeAllDices());
                p.setNumFavorTokens(p.getNumFavorTokens() - price);
                if (price.equals(1)) {
                    //NOTIFY TO OTHERS
                    Response response = new ToolCardUsedByOthersResponse(p.getName(), 7);
                    for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
                        if (!otherPlayer.getName().equals(p.getName())) {
                            if (m.getRemoteObservers().get(otherPlayer) != null) {
                                try {
                                    m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers(p.getName(), 7);
                                } catch (RemoteException e) {
                                    m.getLobby().disconnect(otherPlayer.getName());
                                    System.out.println("Player " + p.getName() + " disconnected!");
                                }
                            }
                            m.notifyToSocketClient(otherPlayer, response);
                        }
                    }
                    price = 2;
                    m.getToolCardsPrices().put("Carta utensile 7: ", price);
                }
                return true;
            }
            return false;
        }
    }
}
