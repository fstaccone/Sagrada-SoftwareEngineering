package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class ShowPlayersRequest implements Request {

    String usrname;

    public ShowPlayersRequest(String username) {
        this.usrname = username;
    }

    public String getUsrname() {
        return usrname;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
