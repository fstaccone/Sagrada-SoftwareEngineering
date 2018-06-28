package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.socket.responses.CheckConnectionResponse;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.TimerTask;

public class MatchStarter extends TimerTask{

    Lobby lobby;

    public MatchStarter(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void run() {
        //RMI CHECK FOR REAL CONNECTION
        for (String observerName : lobby.getRemoteObservers().keySet()) {
            try {
                lobby.getRemoteObservers().get(observerName).onCheckConnection();
            } catch (RemoteException e) {
                lobby.getRemoteObservers().remove(observerName);
                lobby.removeFromWaitingPlayers(observerName);
            }
        }
        CheckConnectionResponse response = new CheckConnectionResponse();
        for (String name : lobby.getSocketObservers().keySet()) {
            try {
                lobby.getSocketObservers().get(name).writeObject(response);
                lobby.getSocketObservers().get(name).reset();
            } catch (IOException e) {
                lobby.getSocketObservers().remove(name);
                lobby.removeFromWaitingPlayers(name);
            }
        }

        if(lobby.getWaitingPlayers().size() >= 2) {
            lobby.startMatch();
        }
    }
}
