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

public class IncrDecrDiceValueEffect implements Effect {

    private Integer price;
    private boolean used;
    private MatchMultiplayer m;
    private PlayerMultiplayer p;

    /**
     * When initialized, the price of the tool card is set to 1
     */
    public IncrDecrDiceValueEffect() {
        price = 1;
        used = false;
    }

    /**
     * This tool card allows the player to increment or decrement by 1 the value of a chosen dice in the reserve.
     * The player can't increment the value of a dice with value 6 and can't decrement the value of a dice with value 1.
     *
     * @param player is the player that uses this tool card
     * @param match  is the player's current match
     * @return true if the tool card prerequisites are satisfied (for single player: correct color of the dice to
     * sacrifice, for multi player: enough favor tokens) and the player uses the tool card in the correct way.
     */
    @Override
    public boolean applyEffect(Player player, Match match) {

        String plusOrMinus = player.getChoice();
        Dice dice = match.getBoard().getReserve().getDices().get(player.getDice());
        int value = dice.getValue();
        //SINGLEPLAYER
        if (player.getDiceToBeSacrificed() != 9) {
            if (!used) {
                Dice sacrificeDice = match.getBoard().getReserve().getDices().get(player.getDiceToBeSacrificed());
                if (sacrificeDice.getColor().equals(Colors.VIOLET) && player.getDice() < match.getBoard().getReserve().getDices().size()) {
                    switch (plusOrMinus) {
                        case "+":
                            if (value != 6) {
                                value = value + 1;
                                match.getBoard().getReserve().getDices().get(player.getDice()).setValue(value); //player.getDice() is the index
                                match.getBoard().getReserve().getDices().remove(sacrificeDice);
                                player.setDice(9);
                                player.setChoice(null);
                                used = true;
                                return true;
                            } else return false;
                        case "-":
                            if (value != 1) {
                                value = value - 1;
                                match.getBoard().getReserve().getDices().get(player.getDice()).setValue(value);
                                match.getBoard().getReserve().getDices().remove(sacrificeDice);
                                used = true;
                                return true;
                            } else return false;
                        default:
                            return false;
                    }
                } else return false;
            } else return false;
        }
        //MULTIPLAYER
        else {
            p = (PlayerMultiplayer) player;
            m = (MatchMultiplayer) match;
            if (p.getNumFavorTokens() >= price) {
                if (player.getDice() < match.getBoard().getReserve().getDices().size()) {
                    switch (plusOrMinus) {
                        case "+":
                            if (value != 6) {
                                value = value + 1;
                                match.getBoard().getReserve().getDices().get(player.getDice()).setValue(value); //player.getDice() is the index
                                p.setNumFavorTokens(p.getNumFavorTokens() - price);
                                if (price.equals(1)) {
                                    notifyToOthers();
                                    price = 2;
                                    m.getToolCardsPrices().put("Carta utensile 1: ", price);
                                }
                                return true;
                            } else return false;

                        case "-":
                            if (value != 1) {
                                value = value - 1;
                                match.getBoard().getReserve().getDices().get(player.getDice()).setValue(value);
                                p.setNumFavorTokens(p.getNumFavorTokens() - price);
                                if (price == 1) {
                                    notifyToOthers();
                                    price = 2;
                                }
                                return true;
                            } else return false;

                        default:
                            return false;
                    }
                } else return false;
            } else return false;
        }
    }

    /**
     * Tells to the other players in the match that this tool card has been used
     */
    private void notifyToOthers() {
        //NOTIFY TO OTHERS
        Response response = new ToolCardUsedByOthersResponse(p.getName(), 1);
        for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
            if (!otherPlayer.getName().equals(p.getName())) {
                if (m.getRemoteObservers().get(otherPlayer) != null) {
                    try {
                        //m.initializePingTimer(otherPlayer.getName());
                        m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers(p.getName(), 1);
                    } catch (RemoteException e) {
                        m.getLobby().disconnect(otherPlayer.getName());
                        System.out.println("Player " + p.getName() + " disconnected!");
                    }
                } else {
                    //m.initializePingTimer(otherPlayer.getName());
                    m.notifyToSocketClient(otherPlayer, response);
                }
            }
        }
    }
}
