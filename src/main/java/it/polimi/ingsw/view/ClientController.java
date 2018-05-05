package it.polimi.ingsw.view;

import it.polimi.ingsw.control.Controller;


public class ClientController {

    // Affo usa l'interfaccia per la dichiarazione, perchè?
    private Controller remoteController;

    public ClientController() {

    }


    public boolean checkName(){
        // manda un messaggio con il nome al controller del modello che verifica se è unico
        return remoteController.checkName();
    }

}
