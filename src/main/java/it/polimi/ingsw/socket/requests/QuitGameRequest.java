package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.control.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class QuitGameRequest implements Request {

    private String name;
    private boolean single;

    public QuitGameRequest(String name, boolean single) {
        this.name = name;
        this.single = single;
    }

    public String getName() {
        return name;
    }

    public boolean isSingle() {
        return single;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
