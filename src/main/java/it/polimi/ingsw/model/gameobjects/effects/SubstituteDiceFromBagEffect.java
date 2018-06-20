package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.socket.responses.Response;
import it.polimi.ingsw.socket.responses.ToolCardUsedByOthersResponse;

import java.rmi.RemoteException;

public class SubstituteDiceFromBagEffect implements Effect {

    private Integer price;
    private boolean used;

    public SubstituteDiceFromBagEffect() {
        price = 1;
        used=false;
    }

    @Override
    public boolean applyEffect(Player player, Match match) {
        //SINGLEPLAYER
        if (player.getDiceToBeSacrificed() != 9) {
            if(!used) {
                Dice sacrificeDice = match.getBoard().getReserve().getDices().get(player.getDiceToBeSacrificed());
                if (sacrificeDice.getColor().equals(Colors.VIOLET)) {
                    Dice dice = match.getBoard().getReserve().getDices().remove(player.getDice());
                    if (dice != null) {
                        match.getBag().putDiceInBag(dice);
                        Dice diceFromBag = match.getBag().pickSingleDice();
                        player.setDiceFromBag(diceFromBag);
                        System.out.println(diceFromBag.toString());
                        match.getBoard().getReserve().getDices().remove(sacrificeDice);
                        used = true;
                        return true;
                    } else
                        return false;
                } else return false;
            }else return false;
        }
        //MULTIPLAYER
        else {
            PlayerMultiplayer p = (PlayerMultiplayer) player;
            MatchMultiplayer m = (MatchMultiplayer) match;

            if (p.getNumFavorTokens() >= price) {
                Dice dice = match.getBoard().getReserve().getDices().remove(player.getDice());
                if (dice != null) {
                    match.getBag().putDiceInBag(dice);
                    Dice diceFromBag = match.getBag().pickSingleDice();
                    player.setDiceFromBag(diceFromBag);
                    p.setNumFavorTokens(p.getNumFavorTokens() - price);
                    if (price.equals(1)) {
                        //NOTIFY TO OTHERS
                        Response response = new ToolCardUsedByOthersResponse(p.getName(), 11);
                        for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
                            if (!otherPlayer.getName().equals(p.getName())) {
                                if (m.getRemoteObservers().get(otherPlayer) != null) {
                                    try {
                                        m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers(p.getName(), 11);
                                    } catch (RemoteException e) {
                                        m.getLobby().disconnect(otherPlayer.getName());
                                        System.out.println("Player " + p.getName() + " disconnected!");
                                    }
                                }
                                m.notifyToSocketClient(otherPlayer, response);
                            }
                        }
                        price = 2;
                        m.getToolCardsPrices().put("Carta utensile 11: ", price);
                    }
                    return true;
                } else
                    return false;
            }
            return false;
        }
    }
}
