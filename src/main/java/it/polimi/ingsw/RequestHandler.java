package it.polimi.ingsw;

public interface RequestHandler {
    Response handle(CheckUsernameRequest request);
}
