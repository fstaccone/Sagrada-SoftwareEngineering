package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class QuitGameRequest implements Request {

    public QuitGameRequest(String name, boolean single) {
        this.name = name;
        this.single = single;
    }

    public String name;
    public boolean single;

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
