package it.polimi.ingsw;

public interface RequestHandler {
    Response handle(CheckUsernameRequest request);
    Response handle(CreateMatchRequest request);
    Response handle(AddPlayerRequest request);
    Response handle(ObserveLobbyRequest request);
    Response handle(ObserveMatchRequest request);
}
