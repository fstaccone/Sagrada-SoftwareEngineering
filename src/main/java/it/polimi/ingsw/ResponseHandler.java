package it.polimi.ingsw;

public interface ResponseHandler {
    void handle(NameAlreadyTakenResponse response);
    void handle(WaitingPlayersResponse response);
}
