package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class ShowWindowRequest implements Request {

    public String myName;
    public String ownerName;

    public ShowWindowRequest(String myName, String ownerName) {
        this.myName = myName;
        this.ownerName = ownerName;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
