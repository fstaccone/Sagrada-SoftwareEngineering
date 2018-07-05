package it.polimi.ingsw.model.gamelogic;

import java.util.TimerTask;

public class PingTimer extends TimerTask {

    private String username;
    private Lobby lobby;

    /**
     * @param username is the name of the player
     * @param lobby    is the lobby
     */
    PingTimer(String username, Lobby lobby) {
        this.username = username;
        this.lobby = lobby;
    }

    /**
     * called to disconnect the player whose name is username if he doesn't respond after a notification after a certain time
     */
    @Override
    public void run() {
        System.out.println("disconnessione giocatore " + username);
        lobby.disconnect(username);
    }
}