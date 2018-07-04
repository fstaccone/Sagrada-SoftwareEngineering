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

public class MoveDiceIgnoringValueRestrEffect implements Effect {

    private Integer price;
    private boolean used;

    /**
     * When initialized, the price of the tool card is set to 1
     */
    public MoveDiceIgnoringValueRestrEffect() {
        price = 1;
        used = false;
    }

    /**
     * This tool card allows the player to move a dice in his scheme card ignoring value restriction.
     * The player has to consider all other placement rules.
     *
     * @param player is the player that uses this tool card
     * @param match  is the player's current match
     * @return true if the tool card prerequisites are satisfied (for single player: correct color of the dice to
     * sacrifice, for multi player: enough favor tokens) and the chosen dice is placed correctly in the new position.
     */
    @Override
    public boolean applyEffect(Player player, Match match) {

        WindowPatternCard schema = player.getSchemeCard();
        int row = player.getStartX1();
        int column = player.getStartY1();
        Dice dice = schema.getDice(row, column);
        //SINGLE
        if (player.getDiceToBeSacrificed() != 9) {
            if (!used) {
                Dice sacrificeDice = match.getBoard().getReserve().getDices().get(player.getDiceToBeSacrificed());
                if (sacrificeDice.getColor().equals(Colors.RED) && dice != null) {
                    putDice(dice, schema, player, row, column);
                    if (dice.equals(schema.getWindow()[player.getFinalX1()][player.getFinalY1()].getDice())) {
                        match.getBoard().getReserve().getDices().remove(sacrificeDice);
                        used = true;
                        return true;
                    } else {
                        schema.putDiceBack(dice, row, column);
                        return false;
                    }
                } else {
                    return false;
                }
            } else return false;

        } //MULTIPLAYER
        else {
            PlayerMultiplayer p = (PlayerMultiplayer) player;
            MatchMultiplayer m = (MatchMultiplayer) match;
            if (p.getNumFavorTokens() >= price) {
                if (dice != null) {
                    putDice(dice, schema, player, row, column);
                    if (dice.equals(schema.getWindow()[p.getFinalX1()][p.getFinalY1()].getDice())) {
                        p.setNumFavorTokens(p.getNumFavorTokens() - price);
                        if (price.equals(1)) {
                            //NOTIFY TO OTHERS
                            Response response = new ToolCardUsedByOthersResponse(p.getName(), 3);
                            for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
                                if (!otherPlayer.getName().equals(p.getName())) {
                                    if (m.getRemoteObservers().get(otherPlayer) != null) {
                                        try {
                                            //m.initializePingTimer(otherPlayer.getName());
                                            m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers(p.getName(), 3);
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
                            price = 2;
                            m.getToolCardsPrices().put("Carta utensile 3: ", price);
                        }
                        return true;
                    } else {
                        schema.putDiceBack(dice, row, column);
                        return false;
                    }
                } else {
                    return false;
                }

            } else {
                return false;
            }
        }

    }

    /**
     * Places the dice in the player's scheme card.
     *
     * @param dice   is the dice to move
     * @param schema is the player's scheme card
     * @param player is the player that uses this tool card
     * @param row    is the row index of the dice new position in the scheme card
     * @param column is the column index of the dice new position in the scheme card
     */
    private void putDice(Dice dice, WindowPatternCard schema, Player player, int row, int column) {
        int newRow = player.getFinalX1();
        int newColumn = player.getFinalY1();
        schema.removeDice(row, column);
        schema.putDiceIgnoringValueConstraint(dice, newRow, newColumn);
    }
}
