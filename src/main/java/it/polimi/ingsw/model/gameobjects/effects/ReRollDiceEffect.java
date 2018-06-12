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
import java.util.Random;

public class ReRollDiceEffect implements Effect {

    private Integer price;

    public ReRollDiceEffect() {
        price = 1;
    }

    @Override
    public boolean applyEffect(Player player, Match match) {
        Random rand = new Random();
        int newValue = rand.nextInt(6) + 1;
        //SINGLEPLAYER
        if (player.getDiceToBeSacrificed() != 9) {
            Dice sacrificeDice = match.getBoard().getReserve().getDices().get(player.getDiceToBeSacrificed());
            if (sacrificeDice.getColor().equals(Colors.VIOLET)&&player.getDice() < match.getBoard().getReserve().getDices().size()) {
                Dice dice = match.getBoard().getReserve().getDices().get(player.getDice());
                dice.setValue(newValue);
                return true;
            } else return false;
        }
        //MULTIPLAYER
        else {
            PlayerMultiplayer p = (PlayerMultiplayer) player;
            MatchMultiplayer m = (MatchMultiplayer) match;


            if (p.getNumFavorTokens() >= price) {
                if (player.getDice() < match.getBoard().getReserve().getDices().size()) {
                    Dice dice = match.getBoard().getReserve().getDices().get(player.getDice());
                    dice.setValue(newValue);
                    p.setNumFavorTokens(p.getNumFavorTokens() - price);
                    if (price.equals(1)) {
                        //NOTIFY TO OTHERS
                        Response response = new ToolCardUsedByOthersResponse(p.getName(), 6);
                        for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
                            if (!otherPlayer.getName().equals(p.getName())) {
                                if (m.getRemoteObservers().get(otherPlayer) != null) {
                                    try {
                                        m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers(p.getName(), 6);
                                    } catch (RemoteException e) {
                                        m.getLobby().disconnect(otherPlayer.getName());
                                        System.out.println("Player " + p.getName() + " disconnected!");
                                    }
                                }
                                m.notifyToSocketClient(otherPlayer, response);
                            }
                        }
                        price = 2;
                        m.getToolCardsPrices().put("Carta utensile 6: ", price);
                    }
                    return true;
                } else return false;
            } else
                return false;
        }
    }
}
