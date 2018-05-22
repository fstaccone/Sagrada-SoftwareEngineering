package it.polimi.ingsw;

import java.io.PrintWriter;

public class ToolCommand {
    int i;
    PrintWriter printer;
    String parametersNeeded;

    public ToolCommand(int i, PrintWriter printer) { //DA SOSTITUIRE PRINTER CON CONTROLLER
        this.i = i;
        this.printer=printer;
        switch (this.i){
            case 1:{
               this.parametersNeeded="Descrizione azione e parametri da passare 1";
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

    public String getParametersNeeded() {
        return parametersNeeded;
    }

    public int getI() {
        return i;
    }

    public void Command1(int diceFromReserve, String incrOrDecr){
        printer.println("Comando 1, togliere il passaggio di printer da costrutture e inserire controller "+ diceFromReserve );
        printer.flush();
    }

    public void Command2(int startX, int startY, int finalX, int finalY){
        printer.println("Comando 2, togliere il passaggio di printer da costrutture e inserire controller ");
        printer.flush();
    }
    public void Command3(){
        printer.println("Comando 3, togliere il passaggio di printer da costrutture e inserire controller");
        printer.flush();
    }
    public void Command4(){
        printer.println("Comando 4, togliere il passaggio di printer da costrutture e inserire controller ");
        printer.flush();
    }
    public void Command5(){
        printer.println("Comando 5, togliere il passaggio di printer da costrutture e inserire controller ");
        printer.flush();
    }
    public void Command6(){
        printer.println("Comando 6, togliere il passaggio di printer da costrutture e inserire controller ");
        printer.flush();
    }
    public void Command7(){
        printer.println("Comando 7, togliere il passaggio di printer da costrutture e inserire controller ");
        printer.flush();
    }
    public void Command8(){
        printer.println("Comando 8, togliere il passaggio di printer da costrutture e inserire controller ");
        printer.flush();
    }
    public void Command9(){
        printer.println("Comando 9, togliere il passaggio di printer da costrutture e inserire controller ");
        printer.flush();
    }
    public void Command10(){
        printer.println("Comando 10, togliere il passaggio di printer da costrutture e inserire controller ");
        printer.flush();
    }
    public void Command11(){
        printer.println("Comando 11, togliere il passaggio di printer da costrutture e inserire controller ");
        printer.flush();
    }
    public void Command12(){
        printer.println("Comando 12, togliere il passaggio di printer da costrutture e inserire controller ");
        printer.flush();
    }
}
