package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.socket.responses.Response;
import it.polimi.ingsw.socket.responses.ToolCardUsedByOthersResponse;

import java.rmi.RemoteException;

public class PlaceDiceNotAdjacentToAnotherEffect implements Effect {

    private Integer price;

    /**
     * When initialized, the price of the tool card is set to 1
     */
    public PlaceDiceNotAdjacentToAnotherEffect() {
        price = 1;
    }

    /**
     * This tool card allows the player to place one chosen dice from the reserve anywhere in his scheme card.
     * The new dice must not have any adjacent dice and the player has to consider all constraints.
     * @param player is the player that uses this tool card
     * @param match is the player's current match
     * @return true if the tool card prerequisites are satisfied (for single player: correct color of the dice to
     * sacrifice, for multi player: enough favor tokens) and the chosen dice is placed correctly in the new position.
     */
    @Override
    public boolean applyEffect(Player player, Match match) {
        WindowPatternCard schema = player.getSchemeCard();
        int newRow = player.getFinalX1();
        int newColumn = player.getFinalY1();
        Dice dice = match.getBoard().getReserve().getDices().get(player.getDice());
        //SINGLEPLAYER
        if (player.getDiceToBeSacrificed() != 9) {
            Dice sacrificeDice = match.getBoard().getReserve().getDices().get(player.getDiceToBeSacrificed());
            if (sacrificeDice.getColor().equals(Colors.YELLOW) && player.getDice() < match.getBoard().getReserve().getDices().size()) {
                if (!(schema.existsAdjacentDice(newRow, newColumn))) {
                    schema.putDiceWithoutCheckPos(dice, newRow, newColumn);
                    if (dice.equals(schema.getWindow()[newRow][newColumn].getDice())) {
                        match.getBoard().getReserve().getDices().remove(player.getDice());
                        match.getBoard().getReserve().getDices().remove(sacrificeDice);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            //MULTIPLAYER
            PlayerMultiplayer p = (PlayerMultiplayer) player;
            MatchMultiplayer m = (MatchMultiplayer) match;
            if (p.getNumFavorTokens() >= price) {
                if (player.getDice() < match.getBoard().getReserve().getDices().size()) {
                    if (!(schema.existsAdjacentDice(newRow, newColumn))) {
                        schema.putDiceWithoutCheckPos(dice, newRow, newColumn);
                        if (dice.equals(schema.getWindow()[newRow][newColumn].getDice())) {
                            match.getBoard().getReserve().getDices().remove(player.getDice());
                            p.setNumFavorTokens(p.getNumFavorTokens() - price);
                            if (price.equals(1)) {
                                //NOTIFY TO OTHERS
                                Response response = new ToolCardUsedByOthersResponse(p.getName(), 9);
                                for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
                                    if (!otherPlayer.getName().equals(p.getName())) {
                                        if (m.getRemoteObservers().get(otherPlayer) != null) {
                                            try {
                                                m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers(p.getName(), 9);
                                            } catch (RemoteException e) {
                                                m.getLobby().disconnect(otherPlayer.getName());
                                                System.out.println("Player " + p.getName() + " disconnected!");
                                            }
                                        }
                                        m.notifyToSocketClient(otherPlayer, response);
                                    }
                                }
                                price = 2;
                                m.getToolCardsPrices().put("Carta utensile 9: ", price);

                            }
                            return true;
                        } else return false;
                    } else
                        return false;
                } else
                    return false;
            } else
                return false;
        }
    }
}
