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

public class IncrDecrDiceValueEffect implements Effect {

    private Integer price;
    private MatchMultiplayer m;
    private PlayerMultiplayer p;

    public IncrDecrDiceValueEffect() {
        price = 1;
    }

    @Override
    public boolean applyEffect(Player player, Match match) {

        String plusOrMinus = player.getChoice();
        Dice dice = match.getBoard().getReserve().getDices().get(player.getDice());
        int value = dice.getValue();
        //SINGLEPLAYER
        if (player.getDiceToBeSacrificed() != 9) {
            Dice sacrificeDice = match.getBoard().getReserve().getDices().get(player.getDiceToBeSacrificed());
            if (sacrificeDice.getColor().equals(Colors.VIOLET) && player.getDice() < match.getBoard().getReserve().getDices().size()) {
                switch (plusOrMinus) {
                    case "+":
                        if (value != 6) {
                            value = value + 1;
                            match.getBoard().getReserve().getDices().get(player.getDice()).setValue(value); //player.getDice() è l'indice
                            match.getBoard().getReserve().getDices().remove(sacrificeDice);
                            player.setDice(9);
                            player.setChoice(null);
                            return true;
                        } else return false;
                    case "-":
                        if (value != 1) {
                            value = value - 1;
                            match.getBoard().getReserve().getDices().get(player.getDice()).setValue(value);
                            match.getBoard().getReserve().getDices().remove(sacrificeDice);
                            return true;
                        } else return false;
                    default:
                        return false;
                }
            }else return false;
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
                                    match.getBoard().getReserve().getDices().get(player.getDice()).setValue(value); //player.getDice() è l'indice
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

    private void notifyToOthers() {
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
    }
}
