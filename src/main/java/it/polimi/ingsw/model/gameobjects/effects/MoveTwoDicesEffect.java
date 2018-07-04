package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;
import it.polimi.ingsw.socket.responses.Response;
import it.polimi.ingsw.socket.responses.ToolCardUsedByOthersResponse;

import java.rmi.RemoteException;

public class MoveTwoDicesEffect implements Effect {

    private Integer price;
    private boolean used;

    /**
     * When initialized, the price of the tool card is set to 1
     */
    public MoveTwoDicesEffect() {
        price = 1;
        used = false;
    }

    /**
     * This tool card allows the player to move exactly 2 dices in his scheme card.
     * The player has to consider all placement rules.
     *
     * @param player is the player that uses this tool card
     * @param match  is the player's current match
     * @return true if the tool card prerequisites are satisfied (for single player: correct color of the dice to
     * sacrifice, for multi player: enough favor tokens) and the chosen dices are placed correctly in their new
     * positions.
     */
    @Override
    public boolean applyEffect(Player player, Match match) {
        WindowPatternCard schema = player.getSchemeCard();
        int row1 = player.getStartX1();
        int column1 = player.getStartY1();
        int row2 = player.getStartX2();
        int column2 = player.getStartY2();
        Dice dice1 = schema.removeDice(row1, column1);
        Dice dice2 = schema.removeDice(row2, column2);
        //SINGLEPLAYER
        if (player.getDiceToBeSacrificed() != 9) {
            if (!used) {
                Dice sacrificeDice = match.getBoard().getReserve().getDices().get(player.getDiceToBeSacrificed());
                if (sacrificeDice.getColor().equals(Colors.YELLOW) && dice1 != null && dice2 != null) {
                    int newRow1 = player.getFinalX1();
                    int newColumn1 = player.getFinalY1();
                    int newRow2 = player.getFinalX2();
                    int newColumn2 = player.getFinalY2();
                    if (schema.putDice(dice1, newRow1, newColumn1) && schema.putDice(dice2, newRow2, newColumn2)) {
                        match.getBoard().getReserve().getDices().remove(sacrificeDice);
                        used = true;
                        return true;
                    } else {
                        if (dice1.equals(schema.getDice(newRow1, newColumn1))) {
                            schema.removeDice(newRow1, newColumn1);
                        }
                        if (dice2.equals(schema.getDice(newRow2, newColumn2))) {
                            schema.removeDice(newRow2, newColumn2);
                        }
                        schema.putDiceBack(dice1, row1, column1);
                        schema.putDiceBack(dice2, row2, column2);
                        return false;
                    }
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
                if (dice1 != null && dice2 != null) {
                    int newRow1 = player.getFinalX1();
                    int newColumn1 = player.getFinalY1();
                    int newRow2 = player.getFinalX2();
                    int newColumn2 = player.getFinalY2();
                    if (schema.putDice(dice1, newRow1, newColumn1) && schema.putDice(dice2, newRow2, newColumn2)) {
                        p.setNumFavorTokens(p.getNumFavorTokens() - price);

                        if (price.equals(1)) {
                            //NOTIFY TO OTHERS
                            Response response = new ToolCardUsedByOthersResponse(p.getName(), 4);
                            for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
                                if (!otherPlayer.getName().equals(p.getName())) {
                                    if (m.getRemoteObservers().get(otherPlayer) != null) {
                                        try {
                                            m.initializePingTimer(otherPlayer.getName());
                                            m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers(p.getName(), 4);
                                        } catch (RemoteException e) {
                                            m.getLobby().disconnect(otherPlayer.getName());
                                            System.out.println("Player " + p.getName() + " disconnected!");
                                        }
                                    } else {
                                        m.initializePingTimer(otherPlayer.getName());
                                        m.notifyToSocketClient(otherPlayer, response);
                                    }
                                }
                            }
                            price = 2;
                            m.getToolCardsPrices().put("Carta utensile 4: ", price);
                        }
                        return true;
                    } else {
                        if (dice1.equals(schema.getDice(newRow1, newColumn1))) {
                            schema.removeDice(newRow1, newColumn1);
                        }
                        if (dice2.equals(schema.getDice(newRow2, newColumn2))) {
                            schema.removeDice(newRow2, newColumn2);
                        }
                        schema.putDiceBack(dice1, row1, column1);
                        schema.putDiceBack(dice2, row2, column2);
                        return false;
                    }
                } else
                    return false;
            } else
                return false;
        }
    }
}
