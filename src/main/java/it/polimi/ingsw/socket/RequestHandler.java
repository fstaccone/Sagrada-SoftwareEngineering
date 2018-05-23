package it.polimi.ingsw.socket;

import it.polimi.ingsw.socket.responses.Response;
import it.polimi.ingsw.socket.requests.*;

public interface RequestHandler {
    Response handle(CheckUsernameRequest request);
    Response handle(CreateMatchRequest request);
    Response handle(AddPlayerRequest request);
    Response handle (RemoveFromWaitingPlayersRequest request);
    Response handle(GoThroughRequest request);
    Response handle(ChooseWindowRequest request);
    Response handle(PlaceDiceRequest request);
}
