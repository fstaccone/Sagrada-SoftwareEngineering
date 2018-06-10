package it.polimi.ingsw.model.gameobjects.effects;

import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gamelogic.MatchMultiplayer;
import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.socket.responses.Response;
import it.polimi.ingsw.socket.responses.ToolCardUsedByOthersResponse;

import java.rmi.RemoteException;

public class ChooseAnotherDiceEffect implements Effect {

    private int price;

    public ChooseAnotherDiceEffect() {
        price = 1;
    }

    @Override
    public boolean applyEffect(Player player, Match match) {
        PlayerMultiplayer p = (PlayerMultiplayer) player;
        MatchMultiplayer m=(MatchMultiplayer) match;

        if(p.getNumFavorTokens() >= price){
            match.setDiceAction(false);
            p.setTurnsLeft(0);
            p.setNumFavorTokens(p.getNumFavorTokens() - price);
            price = 2;

            //NOTIFY TO OTHERS
            Response response = new ToolCardUsedByOthersResponse( p.getName(),8);
            for (PlayerMultiplayer otherPlayer : (m.getPlayers())) {
                if (!otherPlayer.getName().equals(p.getName())) {
                    if (m.getRemoteObservers().get(otherPlayer) != null) {
                        try {
                            m.getRemoteObservers().get(otherPlayer).onToolCardUsedByOthers( p.getName(),8);
                        } catch (RemoteException e) {
                            m.getLobby().disconnect(otherPlayer.getName());
                            System.out.println("Player " + p.getName() + " disconnected!");
                        }
                    }
                    m.notifyToSocketClient(otherPlayer, response);
                }
            }
            return true;
        }
        return false;
    }
}
