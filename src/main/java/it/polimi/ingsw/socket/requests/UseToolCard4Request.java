package it.polimi.ingsw.socket.requests;

import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.responses.Response;

public class UseToolCard4Request implements Request {
    private int startX1;
    private int startY1;
    private int finalX1;
    private int finalY1;
    private int startX2;
    private int startY2;
    private int finalX2;
    private int finalY2;
    private String username;
    private boolean isSingle;
    private int diceToBeSacrificed;

    public UseToolCard4Request(int diceToBeSacrificed, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String name, boolean isSingle) {
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
        this.diceToBeSacrificed = diceToBeSacrificed;
    }

    public int getStartX1() {
        return startX1;
    }

    public int getStartY1() {
        return startY1;
    }

    public int getFinalX1() {
        return finalX1;
    }

    public int getFinalY1() {
        return finalY1;
    }

    public int getStartX2() {
        return startX2;
    }

    public int getStartY2() {
        return startY2;
    }

    public int getFinalX2() {
        return finalX2;
    }

    public int getFinalY2() {
        return finalY2;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public int getDiceToBeSacrificed() {
        return diceToBeSacrificed;
    }

    @Override
    public Response handleRequest(RequestHandler handler) {
        return handler.handle(this);
    }
}