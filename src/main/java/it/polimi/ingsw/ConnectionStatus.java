package it.polimi.ingsw;

public enum ConnectionStatus {
    /**
     * A player is created as CONNECTED and is set to DISCONECTED if he quits the connection during an
     * active match in which he's involved.
     * The value ABSENT is used to show that a player is not involved in any match and it's not logged in.
     */
    CONNECTED, DISCONNECTED, ABSENT
}
