package it.polimi.ingsw;

public interface ResponseHandler {
    void handle(NameAlreadyTakenResponse response);
    void handle(WaitingPlayersResponse response);
    void handle(MatchStartedResponse response);
    void handle(ActualPlayersResponse response);
    void handle(YourTurnResponse response);
    void handle(ReserveResponse response);
}
