package it.polimi.ingsw;

public enum ConnectionStatus {
    // a player is created as READY, he'll be set to CONNECTED if the timeout
    // during his turn goes out of time. Finally a player is set to DISCONECTED if
    // he quits the connection during an active match in which he's involved
    READY, CONNECTED, DISCONNECTED;
}
