package it.polimi.ingsw;

import it.polimi.ingsw.control.Controller;
import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.socket.ClientController;
import it.polimi.ingsw.socket.requests.*;

import java.io.PrintWriter;
import java.rmi.RemoteException;

public class ToolCommand {
    int i;
    PrintWriter printer;
    String parametersNeeded;
    RemoteController controller;
    String name;
    boolean single;
    ClientController clientController;

    public ToolCommand(int i, PrintWriter printer, RemoteController controller, ClientController clientController, String name, boolean isSingle) { //DA SOSTITUIRE PRINTER CON CONTROLLER
        this.i = i;
        this.controller=controller;
        this.clientController=clientController;
        this.printer=printer;
        this.name=name;
        this.single=isSingle;
        switch (i){
            case 1:{
               this.parametersNeeded="Pinza Sgrossatrice: \n- It allows you to Increment('+1') or Decrement('-1') the value of a the dice you choose from the Reserve;\n- You cannot Increment a 6 or Decrement a 1;\n- To use the toolcard you want, insert the following command :\n\n                                      ' usetool [number of the toolcard] [number of the dice in the reserve] [+]or[-]'\n";
            }break;

            case 2:{
                this.parametersNeeded="Descrizione azione e parametri da passare 2";
            }break;

            case 3:{
                this.parametersNeeded="Descrizione azione e parametri da passare 3";
            }break;

            case 4:{
                this.parametersNeeded="Descrizione azione e parametri da passare 4";
            }break;

            case 5:{
                this.parametersNeeded="Descrizione azione e parametri da passare 5";
            }break;

            case 6:{
                this.parametersNeeded="Descrizione azione e parametri da passare 6";
            }break;

            case 7:{
                this.parametersNeeded="Descrizione azione e parametri da passare 7";
            }break;

            case 8:{
                this.parametersNeeded="Descrizione azione e parametri da passare 8";
            }break;

            case 9:{
                this.parametersNeeded="Descrizione azione e parametri da passare 9";
            }break;

            case 10:{
                this.parametersNeeded="Descrizione azione e parametri da passare 10";
            }break;

            case 11:{
                this.parametersNeeded="Descrizione azione e parametri da passare 11";
            }break;

            case 12:{
                this.parametersNeeded="Descrizione azione e parametri da passare 12";
            }break;


        }
    }

    public int getI() {
        return i;
    }

    public boolean command1(int diceFromReserve, String incrOrDecr){

        //RMI
        if(controller!=null) {
            try {
                return controller.useToolCard1(diceFromReserve, incrOrDecr, name, single);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //SOCKET
        else {
            clientController.request(new UseToolCard1Request(diceFromReserve, incrOrDecr, name, single));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (clientController.isEffectApplied()) {
                clientController.setEffectApplied(false);//to reset the value
                return true;
            } else {
                return false;
            }
        }
        return false;

    }

    public boolean command2or3(int n,int startX, int startY, int finalX, int finalY){
        //RMI
        if(controller!=null) {
            try {
                return controller.useToolCard2or3(n,startX, startY, finalX, finalY, name, single);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //SOCKET
        else {
            clientController.request(new UseToolCard2or3Request(n,startX, startY, finalX, finalY, name, single));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (clientController.isEffectApplied()) {
                clientController.setEffectApplied(false);//to reset the value
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean command4(int startX1, int startY1, int finalX1, int finalY1,int startX2, int startY2, int finalX2, int finalY2){
        if(controller!=null) {
            try {
                return controller.useToolCard4(startX1, startY1, finalX1, finalY1,startX2, startY2, finalX2, finalY2, name, single);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //SOCKET
        else {
            clientController.request(new UseToolCard4Request(startX1, startY1, finalX1, finalY1,startX2, startY2, finalX2, finalY2, name, single));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (clientController.isEffectApplied()) {
                clientController.setEffectApplied(false);//to reset the value
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    public void command5(){
        printer.println("Comando 5, togliere il passaggio di printer da costrutture e inserire controller ");
        printer.flush();
    }
    public void command6(){
        printer.println("Comando 6, togliere il passaggio di printer da costrutture e inserire controller ");
        printer.flush();
    }
    public boolean command7(){
        if(controller!=null) {
            try {
                return controller.useToolCard7( name, single);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //SOCKET
        else {
            clientController.request(new UseToolCard7Request( name, single));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (clientController.isEffectApplied()) {
                clientController.setEffectApplied(false);//to reset the value
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    public void command8(){
        printer.println("Comando 8, togliere il passaggio di printer da costrutture e inserire controller ");
        printer.flush();
    }
    public void command9(){
        printer.println("Comando 9, togliere il passaggio di printer da costrutture e inserire controller ");
        printer.flush();
    }
    public boolean command10(int diceFromReserve){
        if(controller!=null) {
            try {
                return controller.useToolCard10(diceFromReserve, name, single);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //SOCKET
        else {
            clientController.request(new UseToolCard10Request(diceFromReserve, name, single));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (clientController.isEffectApplied()) {
                clientController.setEffectApplied(false);//to reset the value
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    public void command11(){
        printer.println("Comando 11, togliere il passaggio di printer da costrutture e inserire controller ");
        printer.flush();
    }
    public void command12(){
        printer.println("Comando 12, togliere il passaggio di printer da costrutture e inserire controller ");
        printer.flush();
    }
}
