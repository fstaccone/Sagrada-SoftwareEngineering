package it.polimi.ingsw;

import it.polimi.ingsw.model.gameobjects.Player;
import it.polimi.ingsw.view.RemoteBaseView;


public class Client {
    private String name;
    private Player player;
    private ClientState state;
    private RemoteBaseView view;

    private Client(String name, RemoteBaseView view, ClientState state) {
        this.name = name;
        this.view = view;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    public ClientState getState() {
        return state;
    }

    public RemoteBaseView getView() {
        return view;
    }

    // the Player attribute will be set in the Model when tha match will be started todo: è in inglese?
    public void setPlayer(Player player) {
        this.player = player;
    }


}
