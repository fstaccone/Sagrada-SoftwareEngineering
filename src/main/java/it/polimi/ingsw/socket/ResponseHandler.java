package it.polimi.ingsw.socket;

import it.polimi.ingsw.socket.responses.*;

public interface ResponseHandler {
    void handle(NameAlreadyTakenResponse response);
    void handle(WaitingPlayersResponse response);
    void handle(MatchStartedResponse response);
    void handle(ActualPlayersResponse response);
    void handle(YourTurnResponse response);
    void handle(ReserveResponse response);
    void handle(ProposeWindowResponse response);
    void handle(ShowWindowResponse response);
    void handle(AfterWindowChoiseResponse response);
    void handle(ToolCardsResponse response);
    void handle(OtherTurnResponse response);
    void handle(UpdateReserveResponse response);
    void handle(DicePlacedResponse response);
    void handle(ToolCardEffectAppliedResponse response);
    void handle(ClosingGameResponse response);
    void handle(PlayerExitResponse response);
    void handle(PlayerReconnectionResponse response);
    void handle(GameEndResponse response);
}
