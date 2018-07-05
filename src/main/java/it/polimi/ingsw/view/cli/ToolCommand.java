package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.control.SocketController;
import it.polimi.ingsw.socket.requests.*;

import java.rmi.RemoteException;

public class ToolCommand {
    private int i;
    String parametersNeeded;
    private RemoteController controller;
    private String name;
    private boolean single;
    private SocketController socketController;

    ToolCommand(int i, RemoteController controller, SocketController socketController, String name, boolean isSingle) { //DA SOSTITUIRE PRINTER CON CONTROLLER
        this.i = i;
        this.controller = controller;
        this.socketController = socketController;
        this.name = name;
        this.single = isSingle;
        switch (i) {
            case 1: {
                this.parametersNeeded = "Pinza Sgrossatrice: \nPer usare questa carta, digita :                         ' usautensile 1 [numero dado riserva] [+]o[-] '\nN.B.: In caso di partita singola ricordati di aver scelto il dado sacrificale prima di utilizzare la carta utensile!";
            }
            break;

            case 2: {
                this.parametersNeeded = "Pennello per Eglomise \nPer usare questa carta, digita :                       ' usautensile 2 [coord X partenza carta schema] [coord Y partenza carta schema] [coord X dest carta schema] [coord Y dest carta schema] '\nN.B.: In caso di partita singola ricordati di aver scelto il dado sacrificale prima di utilizzare la carta utensile! ";
            }
            break;

            case 3: {
                this.parametersNeeded = "Alesatore per Lamina di Rame \nPer usare questa carta, digita :                ' usautensile 3 [coord X partenza carta schema] [coord Y partenza carta schema] [coord X dest carta schema] [coord Y dest carta schema] '\nN.B.: In caso di partita singola ricordati di aver scelto il dado sacrificale prima di utilizzare la carta utensile!";
            }
            break;

            case 4: {
                this.parametersNeeded = "Lathekin \nPer usare questa carta, digita :                                    ' usautensile 4 [coord X 1 partenza carta schema] [coord Y 1 partenza carta schema] [coord X 1 dest carta schema] [coord Y 1 dest carta schema] [coord X 2 partenza carta schema] [coord Y 2 partenza carta schema] [coord X 2 dest carta schema] [coord Y 2 dest carta schema]'\nN.B.: In caso di partita singola ricordati di aver scelto il dado sacrificale prima di utilizzare la carta utensile!";
            }
            break;

            case 5: {
                this.parametersNeeded = "Taglierina Circolare \nPer usare questa carta, digita :                        ' usautensile 5 [numero dado riserva] [numero round] [numero dado nel round] '\nN.B.: In caso di partita singola ricordati di aver scelto il dado sacrificale prima di utilizzare la carta utensile!";
            }
            break;

            case 6: {
                this.parametersNeeded = "Pennello per Pasta Salda \nPer usare questa carta, digita :                    ' usautensile 6 [numero dado riserva] '\nN.B.: In caso di partita singola ricordati di aver scelto il dado sacrificale prima di utilizzare la carta utensile!";
            }
            break;

            case 7: {
                this.parametersNeeded = "Martelletto\nPer usare questa carta, digita :                                  ' usautensile 7] '\nN.B.: In caso di partita singola ricordati di aver scelto il dado sacrificale prima di utilizzare la carta utensile!";
            }
            break;

            case 8: {
                this.parametersNeeded = "Tenaglia a Rotelle\nPer usare questa carta, digita :                           ' usautensile 8 '\nN.B.: In caso di partita singola ricordati di aver scelto il dado sacrificale prima di utilizzare la carta utensile!";
            }
            break;

            case 9: {
                this.parametersNeeded = "Riga in sughero\nPer usare questa carta, digita :                              ' usautensile 9 [numero dado riserva] [coord X dest carta schema] [coord Y dest carta schema] '\nN.B.: In caso di partita singola ricordati di aver scelto il dado sacrificale prima di utilizzare la carta utensile!";
            }
            break;

            case 10: {
                this.parametersNeeded = "Tampone Diamantato\nPer usare questa carta, digita :                           ' usautensile 10 [numero dado riserva] '\nN.B.: In caso di partita singola ricordati di aver scelto il dado sacrificale prima di utilizzare la carta utensile!";
            }
            break;

            case 11: {
                this.parametersNeeded = "Diluente per Pasta Salda\nPer usare questa carta, digita :                     ' usautensile 11 [numero dado riserva] '\nN.B.: In caso di partita singola ricordati di aver scelto il dado sacrificale prima di utilizzare la carta utensile!";
            }
            break;

            case 12: {
                this.parametersNeeded = "Taglierina Manuale\nPer usare questa carta, digita :                           ' usautensile 12 [numero round] [numero dado round] [coord X 1 partenza carta schema] [coord Y 1 partenza carta schema] [coord X 1 dest carta schema] [coord Y 1 dest carta schema] [coord X 2 partenza carta schema] [coord Y 2 partenza carta schema] [coord X 2 dest carta schema] [coord Y 2 dest carta schema]'\nN.B.: In caso di partita singola ricordati di aver scelto il dado sacrificale prima di utilizzare la carta utensile!";
            }
            break;


        }
    }

    public int getI() {
        return i;
    }

    boolean command1(int diceToBeSacrificed, int diceFromReserve, String incrOrDecr) {

        //RMI
        if (controller != null) {
            try {
                return controller.useToolCard1(diceToBeSacrificed, diceFromReserve, incrOrDecr, name, single);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //SOCKET
        else {
            socketController.request(new UseToolCard1Request(diceToBeSacrificed, diceFromReserve, incrOrDecr, name, single));
            return waitForToolEffectAppliedResponse();
        }
        return false;

    }

    boolean command2or3(int diceToBeSacrificed, int n, int startX, int startY, int finalX, int finalY) {
        //RMI
        if (controller != null) {
            try {
                return controller.useToolCard2or3(diceToBeSacrificed, n, startX, startY, finalX, finalY, name, single);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //SOCKET
        else {
            socketController.request(new UseToolCard2or3Request(diceToBeSacrificed, n, startX, startY, finalX, finalY, name, single));
            return waitForToolEffectAppliedResponse();
        }
        return false;
    }

    boolean command4(int diceToBeSacrificed, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2) {
        if (controller != null) {
            try {
                return controller.useToolCard4(diceToBeSacrificed, startX1, startY1, finalX1, finalY1, startX2, startY2, finalX2, finalY2, name, single);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //SOCKET
        else {
            socketController.request(new UseToolCard4Request(diceToBeSacrificed, startX1, startY1, finalX1, finalY1, startX2, startY2, finalX2, finalY2, name, single));
            return waitForToolEffectAppliedResponse();
        }
        return false;
    }

    boolean command5(int diceToBeSacrificed, int diceFromReserve, int roundFromTrack, int diceInRound) {
        if (controller != null) {
            try {
                return controller.useToolCard5(diceToBeSacrificed, diceFromReserve, roundFromTrack, diceInRound, name, single);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //SOCKET
        else {
            socketController.request(new UseToolCard5Request(diceToBeSacrificed, diceFromReserve, roundFromTrack, diceInRound, name, single));
            return waitForToolEffectAppliedResponse();
        }
        return false;
    }

    boolean command6(int diceToBeSacrificed, int diceChosen) {
        if (controller != null) {
            try {
                return controller.useToolCard6(diceToBeSacrificed, diceChosen, name, single);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //SOCKET
        else {
            socketController.request(new UseToolCard6Request(diceToBeSacrificed, diceChosen, name, single));
            return waitForToolEffectAppliedResponse();
        }
        return false;
    }

    boolean command7(int diceToBeSacrificed) {
        if (controller != null) {
            try {
                return controller.useToolCard7(diceToBeSacrificed, name, single);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //SOCKET
        else {
            socketController.request(new UseToolCard7Request(diceToBeSacrificed, name, single));
            return waitForToolEffectAppliedResponse();
        }
        return false;
    }

    boolean command8(int diceToBeSacrificed) {
        if (controller != null) {
            try {
                return controller.useToolCard8(diceToBeSacrificed, name, single);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //SOCKET
        else {
            socketController.request(new UseToolCard8Request(diceToBeSacrificed, name, single));
            return waitForToolEffectAppliedResponse();
        }
        return false;

    }

    boolean command9(int diceToBeSacrificed, int diceFromReserve, int finalX, int finalY) {
        if (controller != null) {
            try {
                return controller.useToolCard9(diceToBeSacrificed, diceFromReserve, finalX, finalY, name, single);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //SOCKET
        else {
            socketController.request(new UseToolCard9Request(diceToBeSacrificed, diceFromReserve, finalX, finalY, name, single));
            return waitForToolEffectAppliedResponse();
        }
        return false;

    }

    boolean command10(int diceToBeSacrificed, int diceFromReserve) {
        if (controller != null) {
            try {
                return controller.useToolCard10(diceToBeSacrificed, diceFromReserve, name, single);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //SOCKET
        else {
            socketController.request(new UseToolCard10Request(diceToBeSacrificed, diceFromReserve, name, single));
            return waitForToolEffectAppliedResponse();
        }
        return false;
    }

    boolean command11(int diceToBeSacrificed, int diceFromReserve) {
        //RMI
        if (controller != null) {
            try {
                return controller.useToolCard11(diceToBeSacrificed, diceFromReserve, name, single);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //SOCKET
        else {
            socketController.request(new UseToolCard11Request(diceToBeSacrificed, diceFromReserve, name, single));
            return waitForToolEffectAppliedResponse();
        }
        return false;
    }


    boolean command12(int diceToBeSacrificed, int roundFromTrack, int diceInRound, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2) {
        if (controller != null) {
            try {
                return controller.useToolCard12(diceToBeSacrificed, roundFromTrack, diceInRound, startX1, startY1, finalX1, finalY1, startX2, startY2, finalX2, finalY2, name, single);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //SOCKET
        else {
            socketController.request(new UseToolCard12Request(diceToBeSacrificed, roundFromTrack, diceInRound, startX1, startY1, finalX1, finalY1, startX2, startY2, finalX2, finalY2, name, single));
            return waitForToolEffectAppliedResponse();
        }
        return false;
    }

    private boolean waitForToolEffectAppliedResponse() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        if (socketController.isEffectApplied()) {
            socketController.setEffectApplied(false);//to reset the value
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "command" + i;
    }
}
