package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard4Request implements Request{
    public int startX1;
    public int startY1;
    public int finalX1;
    public int finalY1;
    public int startX2;
    public int startY2;
    public int finalX2;
    public int finalY2;
    public String username;
    public boolean isSingle;
    public int diceToBeSacrificed;

    public UseToolCard4Request(int diceToBeSacrificed,int startX1, int startY1, int finalX1, int finalY1,int startX2, int startY2, int finalX2, int finalY2, String name, boolean isSingle) {
        this.startX1 = startX1;
        this.startY1 = startY1;
        this.finalX1 = finalX1;
        this.finalY1 = finalY1;
        this.startX2 = startX2;
        this.startY2 = startY2;
        this.finalX2 = finalX2;
        this.finalY2 = finalY2;
        this.username = name;
        this.isSingle = isSingle;
        this.diceToBeSacrificed=diceToBeSacrificed;
    }


    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}