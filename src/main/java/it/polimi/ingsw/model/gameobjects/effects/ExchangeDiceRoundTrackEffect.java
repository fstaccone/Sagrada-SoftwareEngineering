package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.Dice;
import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.RoundTrack;
import it.polimi.ingsw.socket.responses.Response;
import it.polimi.ingsw.socket.responses.ToolCardUsedByOthersResponse;

import java.rmi.RemoteException;

public class ExchangeDiceRoundTrackEffect implements Effect{

    private int price;

    public ExchangeDiceRoundTrackEffect() {
        price = 1;
    }

    @Override
    public boolean applyEffect(Player player, Match match) {
        PlayerMultiplayer p = (PlayerMultiplayer) player;
        MatchMultiplayer m=(MatchMultiplayer) match;

        if(p.getNumFavorTokens() >= price) {
            if (player.getDice() >= 0 && player.getDice() < match.getBoard().getReserve().getDices().size()) {
                Dice dice = match.getBoard().getReserve().getDices().remove(player.getDice());
                RoundTrack track = match.getBoard().getRoundTrack();
                dice = track.switchDice(dice, player.getRound(), player.getDiceChosenFromRound());
                if (dice != null) {
                    match.getBoard().getReserve().getDices().add(player.getDice(), dice);
                    p.setNumFavorTokens(p.getNumFavorTokens() - price);
                    price = 2;

                    //NOTIFY TO OTHERS
                    Response response = new ToolCardUsedByOthersResponse( p.getName(),5);
                    for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
                        if (!otherPlayer.getName().equals(p.getName())) {
                            if (m.getRemoteObservers().get(otherPlayer) != null) {
                                try {
                                    m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers( p.getName(),5);
                                } catch (RemoteException e) {
                                    m.getLobby().disconnect(otherPlayer.getName());
                                    System.out.println("Player " + p.getName() + " disconnected!");
                                }
                            }
                            m.notifyToSocketClient(otherPlayer, response);
                        }
                    }
                    return true;
                } else
                    return false;
            }
            return false;
        }else
            return false;
    }
}
