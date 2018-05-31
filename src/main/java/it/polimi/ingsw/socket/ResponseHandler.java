package it.polimi.ingsw.socket;

import it.polimi.ingsw.socket.responses.*;

public interface ResponseHandler {
    void handle(NameAlreadyTakenResponse response);

    void handle(DiceColorResponse response);

    void handle(WaitingPlayersResponse response);

    void handle(MatchStartedResponse response);

    void handle(ActualPlayersResponse response);

    void handle(YourTurnResponse response);

    void handle(ReserveResponse response);

    void handle(ProposeWindowResponse response);

    void handle(MyWindowResponse response);

    void handle(AfterWindowChoiseResponse response);

    void handle(InitializationResponse response);

    void handle(OtherTurnResponse response);

    void handle(UpdateReserveResponse response);

    void handle(DicePlacedResponse response);

    void handle(ToolCardEffectAppliedResponse response);

    void handle(ClosingGameResponse response);

    void handle(PlayerExitGameResponse response);

    void handle(PlayerReconnectionResponse response);

    void handle(MyFavorTokensResponse response);

    void handle(OtherFavorTokensResponse response);

    void handle(OtherSchemeCardsResponse response);

    void handle(GameEndResponse response);

    void handle(AfterReconnectionResponse response);

    void handle(CheckConnectionResponse response);

    void handle(PlayerExitRoomResponse response);

    void handle(LastPlayerRoomResponse response);

    void handle(RoundTrackResponse response);
}
