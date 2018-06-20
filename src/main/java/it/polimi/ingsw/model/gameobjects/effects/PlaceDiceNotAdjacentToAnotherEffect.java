package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.*;
import it.polimi.ingsw.socket.responses.Response;
import it.polimi.ingsw.socket.responses.ToolCardUsedByOthersResponse;

import java.rmi.RemoteException;

public class PlaceDiceNotAdjacentToAnotherEffect implements Effect {

    private Integer price;
    private boolean used;

    public PlaceDiceNotAdjacentToAnotherEffect() {
        price = 1;
        used=false;
    }

    @Override
    public boolean applyEffect(Player player, Match match) {
        WindowPatternCard schema = player.getSchemeCard();
        int newRow = player.getFinalX1();
        int newColumn = player.getFinalY1();
        Dice dice = match.getBoard().getReserve().getDices().get(player.getDice());
        //SINGLEPLAYER
        if (player.getDiceToBeSacrificed() != 9) {
            if(!used) {
                Dice sacrificeDice = match.getBoard().getReserve().getDices().get(player.getDiceToBeSacrificed());
                if (sacrificeDice.getColor().equals(Colors.YELLOW) && player.getDice() < match.getBoard().getReserve().getDices().size()) {
                    if (!(schema.existsAdjacentDice(newRow, newColumn))) {
                        schema.putDiceWithoutCheckPos(dice, newRow, newColumn);
                        if (dice.equals(schema.getWindow()[newRow][newColumn].getDice())) {
                            match.getBoard().getReserve().getDices().remove(player.getDice());
                            match.getBoard().getReserve().getDices().remove(sacrificeDice);
                            used=true;
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
            }else return false;
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
