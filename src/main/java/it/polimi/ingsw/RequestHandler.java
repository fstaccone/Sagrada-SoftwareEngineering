package it.polimi.ingsw;

public interface RequestHandler {
    Response handle(CheckUsernameRequest request);
    Response handle(CreateMatchRequest request);
    void handle(AddPlayerRequest request);
}
