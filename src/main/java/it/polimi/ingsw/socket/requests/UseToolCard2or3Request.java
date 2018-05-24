package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard2or3Request implements Request{
    public int n;
    public int startX;
    public int startY;
    public int finalX;
    public int finalY;
    public String username;
    public boolean isSingle;

    public UseToolCard2or3Request(int n,int startX, int startY, int finalX, int finalY, String name, boolean isSingle) {
        this.startX = startX;
        this.startY = startY;
        this.finalX = finalX;
        this.finalY = finalY;
        this.username = name;
        this.isSingle = isSingle;
    }


    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}
