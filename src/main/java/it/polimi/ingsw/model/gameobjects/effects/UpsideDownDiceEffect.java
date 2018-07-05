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

public class UpsideDownDiceEffect implements Effect {

    private Integer price;
    private boolean used;
    private MatchMultiplayer m;
    private PlayerMultiplayer p;

    /**
     * When initialized, the price of the tool card is set to 1
     */
    public UpsideDownDiceEffect() {
        price = 1;
        used = false;
    }

    /**
     * This tool card allows the player to put a chosen dice from the reserve upside down.
     * It means that if the dice value is 6, after using the tool card it'll be 1, if it's 5, it'll be 2 and so on.
     *
     * @param player is the player that uses this tool card
     * @param match  is the player's current match
     * @return true if the tool card prerequisites are satisfied (for single player: correct color of the dice to
     * sacrifice, for multi player: enough favor tokens) and the chosen dice from the reserve has a value between
     * 1 and 6.
     */
    @Override
    public boolean applyEffect(Player player, Match match) {
        int value = match.getBoard().getReserve().getDices().get(player.getDice()).getValue();
        //SINGLEPLAYER
        if (player.getDiceToBeSacrificed() != 9) {
            if (!used) {
                Dice sacrificeDice = match.getBoard().getReserve().getDices().get(player.getDiceToBeSacrificed());
                if (sacrificeDice.getColor().equals(Colors.GREEN) && player.getDice() < match.getBoard().getReserve().getDices().size()) {
                    switch (value) {
                        case 1:
                            match.getBoard().getReserve().getDices().get(player.getDice()).setValue(6);
                            match.getBoard().getReserve().getDices().remove(sacrificeDice);
                            used = true;
                            return true;
                        case 2:
                            match.getBoard().getReserve().getDices().get(player.getDice()).setValue(5);
                            match.getBoard().getReserve().getDices().remove(sacrificeDice);
                            used = true;
                            return true;
                        case 3:
                            match.getBoard().getReserve().getDices().get(player.getDice()).setValue(4);
                            match.getBoard().getReserve().getDices().remove(sacrificeDice);
                            used = true;
                            return true;
                        case 4:
                            match.getBoard().getReserve().getDices().get(player.getDice()).setValue(3);
                            match.getBoard().getReserve().getDices().remove(sacrificeDice);
                            used = true;
                            return true;
                        case 5:
                            match.getBoard().getReserve().getDices().get(player.getDice()).setValue(2);
                            match.getBoard().getReserve().getDices().remove(sacrificeDice);
                            used = true;
                            return true;
                        case 6:
                            match.getBoard().getReserve().getDices().get(player.getDice()).setValue(1);
                            match.getBoard().getReserve().getDices().remove(sacrificeDice);
                            used = true;
                            return true;
                        default:
                            return false;
                    }
                } else
                    return false;
            } else
                return false;
        }
        //MULTIPLAYER
        else {

            PlayerMultiplayer p = (PlayerMultiplayer) player;
            MatchMultiplayer m = (MatchMultiplayer) match;
            this.m = m;
            this.p = p;

            if (p.getNumFavorTokens() >= price) {
                if (player.getDice() < match.getBoard().getReserve().getDices().size()) {
                    switch (value) {
                        case 1:
                            match.getBoard().getReserve().getDices().get(player.getDice()).setValue(6);
                            p.setNumFavorTokens(p.getNumFavorTokens() - price);
                            notifytoOthersAndUpdatePrice();
                            return true;
                        case 2:
                            match.getBoard().getReserve().getDices().get(player.getDice()).setValue(5);
                            p.setNumFavorTokens(p.getNumFavorTokens() - price);
                            notifytoOthersAndUpdatePrice();
                            return true;
                        case 3:
                            match.getBoard().getReserve().getDices().get(player.getDice()).setValue(4);
                            p.setNumFavorTokens(p.getNumFavorTokens() - price);
                            notifytoOthersAndUpdatePrice();
                            return true;
                        case 4:
                            match.getBoard().getReserve().getDices().get(player.getDice()).setValue(3);
                            p.setNumFavorTokens(p.getNumFavorTokens() - price);
                            notifytoOthersAndUpdatePrice();
                            return true;
                        case 5:
                            match.getBoard().getReserve().getDices().get(player.getDice()).setValue(2);
                            p.setNumFavorTokens(p.getNumFavorTokens() - price);
                            notifytoOthersAndUpdatePrice();
                            return true;
                        case 6:
                            match.getBoard().getReserve().getDices().get(player.getDice()).setValue(1);
                            p.setNumFavorTokens(p.getNumFavorTokens() - price);
                            notifytoOthersAndUpdatePrice();
                            return true;
                        default:
                            return false;
                    }
                } else return false;
            } else return false;
        }
    }

    /**
     * If the price is 1, which means that it's the first time this tool card is used in the current match, all players
     * are advised that the tool card has been used and its price is now 2.
     */
    private void notifytoOthersAndUpdatePrice() {
        if (price.equals(1)) {
            //NOTIFY TO OTHERS
            Response response = new ToolCardUsedByOthersResponse(p.getName(), 10);
            for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
                if (!otherPlayer.getName().equals(p.getName())) {
                    if (m.getRemoteObservers().get(otherPlayer) != null) {
                        try {
                            m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers(p.getName(), 10);
                        } catch (RemoteException e) {
                            m.getLobby().disconnect(otherPlayer.getName());
                        }
                    }else {
                        m.notifyToSocketClient(otherPlayer, response);
                    }
                }
            }
            price = 2;
            m.getToolCardsPrices().put("Carta utensile 10: ", price);
        }
    }
}
