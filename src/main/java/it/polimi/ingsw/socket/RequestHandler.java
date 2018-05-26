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
    Response handle(ShowWindowRequest request);
    Response handle(UseToolCard1Request request);
    Response handle(UseToolCard2or3Request request);
    Response handle(UseToolCard4Request request);
    Response handle(UseToolCard7Request request);
    Response handle(UseToolCard9Request request);
    Response handle(UseToolCard10Request request);

}
