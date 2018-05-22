package it.polimi.ingsw;

public interface RequestHandler {
    Response handle(CheckUsernameRequest request);
    Response handle(CreateMatchRequest request);
    Response handle(AddPlayerRequest request);
    Response handle (RemoveFromWaitingPlayersRequest request);
    Response handle(GoThroughRequest request);
    Response handle(ChooseWindowRequest request);
    Response handle(PlaceDiceRequest request);
}
