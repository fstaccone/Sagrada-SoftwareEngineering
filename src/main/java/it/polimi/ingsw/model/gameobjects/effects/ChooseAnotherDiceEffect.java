package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.socket.responses.Response;
import it.polimi.ingsw.socket.responses.ToolCardUsedByOthersResponse;

import java.rmi.RemoteException;

public class ChooseAnotherDiceEffect implements Effect {

    private Integer price;
    private boolean used;

    /**
     * When the match starts the price is 1
     */
    public ChooseAnotherDiceEffect() {
        price = 1;
        used = false;
    }

    /**
     * If the preliminary conditions are satisfied (enough favor tokens for multi player, correct dice to sacrifice
     * for single player), the player is allowed to choose and place a second dice in his first turn
     *
     * @param player is the player that uses the tool card
     * @param match  is the player's current match
     * @return true if the preliminary conditions are satisfied, false otherwise
     */
    @Override
    public boolean applyEffect(Player player, Match match) {
        //SINGLEPLAYER
        if (player.getDiceToBeSacrificed() != 9) {
            if (!used) {
                Dice sacrificeDice = match.getBoard().getReserve().getDices().get(player.getDiceToBeSacrificed());
                if (sacrificeDice.getColor().equals(Colors.RED)) {
                    match.setDiceAction(false);
                    player.setTurnsLeft(0);
                    match.getBoard().getReserve().getDices().remove(sacrificeDice);
                    used = true;
                    return true;
                } else {
                    return false;
                }
            } else return false;
        }
        //MULTIPLAYER
        else {
            PlayerMultiplayer p = (PlayerMultiplayer) player;
            MatchMultiplayer m = (MatchMultiplayer) match;

            if (p.getNumFavorTokens() >= price) {
                match.setDiceAction(false);
                p.setTurnsLeft(0);
                p.setNumFavorTokens(p.getNumFavorTokens() - price);
                if (price.equals(1)) {
                    //NOTIFY TO OTHERS
                    Response response = new ToolCardUsedByOthersResponse(p.getName(), 8);
                    for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
                        if (!otherPlayer.getName().equals(p.getName())) {
                            if (m.getRemoteObservers().get(otherPlayer) != null) {
                                try {
                                    m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers(p.getName(), 8);
                                } catch (RemoteException e) {
                                    m.getLobby().disconnect(otherPlayer.getName());
                                }
                            }else {
                                m.notifyToSocketClient(otherPlayer, response);
                            }
                        }
                    }
                    price = 2;
                    m.getToolCardsPrices().put("Carta utensile 8: ", price);
                }
                return true;
            }
            return false;
        }
    }
}
