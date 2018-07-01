package it.polimi.ingsw.control;

import it.polimi.ingsw.model.gamelogic.ConnectionStatus;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.socket.responses.DiceColorResponse;
import it.polimi.ingsw.socket.responses.DicePlacedResponse;
import it.polimi.ingsw.socket.responses.NameAlreadyTakenResponse;
import it.polimi.ingsw.socket.responses.ToolCardEffectAppliedResponse;
import it.polimi.ingsw.view.LoginHandler;
import it.polimi.ingsw.view.cli.SocketCli;
import it.polimi.ingsw.view.gui.SocketGui;
import org.junit.Assert;
import org.junit.Test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.mockito.Mockito.mock;

public class SocketControllerTest {

    @Test
    public void SocketController(){
        ObjectInputStream objectInputStream = mock(ObjectInputStream.class);
        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        LoginHandler loginHandler = mock(LoginHandler.class);
        SocketController socketController = new SocketController(objectInputStream, objectOutputStream, loginHandler, true);
        Assert.assertNotNull(socketController);
    }

    @Test
    public void isNameAlreadyTaken(){
        ObjectInputStream objectInputStream = mock(ObjectInputStream.class);
        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        LoginHandler loginHandler = mock(LoginHandler.class);
        SocketController socketController = new SocketController(objectInputStream, objectOutputStream, loginHandler, true);
        ObjectInputStream objectInputStream1 = socketController.getIn();
        ObjectOutputStream objectOutputStream1 = socketController.getOut();
        Assert.assertEquals(ConnectionStatus.ABSENT, socketController.isNameAlreadyTaken());
    }

    @Test
    public void isDicePlaced(){
        ObjectInputStream objectInputStream = mock(ObjectInputStream.class);
        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        LoginHandler loginHandler = mock(LoginHandler.class);
        SocketController socketController = new SocketController(objectInputStream, objectOutputStream, loginHandler, true);
        Assert.assertEquals(false, socketController.isDicePlaced());
    }

    @Test
    public void isEffectApplied(){
        ObjectInputStream objectInputStream = mock(ObjectInputStream.class);
        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        LoginHandler loginHandler = mock(LoginHandler.class);
        SocketController socketController = new SocketController(objectInputStream, objectOutputStream, loginHandler, true);
        Assert.assertEquals(false, socketController.isEffectApplied());
    }

    @Test
    public void handleNameAlreadyTakenResponse(){
        ObjectInputStream objectInputStream = mock(ObjectInputStream.class);
        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        LoginHandler loginHandler = mock(LoginHandler.class);
        SocketController socketController = new SocketController(objectInputStream, objectOutputStream, loginHandler, true);
        socketController.setDicePlaced(true);
        socketController.setEffectApplied(true);
        SocketCli socketCli = mock(SocketCli.class);
        socketController.setSocketCli(socketCli);
        SocketGui socketGui = mock(SocketGui.class);
        socketController.setSocketGui(socketGui);
        NameAlreadyTakenResponse nameAlreadyTakenResponse = new NameAlreadyTakenResponse(ConnectionStatus.CONNECTED);
        socketController.handle(nameAlreadyTakenResponse);
        Assert.assertEquals(ConnectionStatus.CONNECTED, socketController.isNameAlreadyTaken());
    }

    @Test
    public void handleDicePlacedResponse(){
        ObjectInputStream objectInputStream = mock(ObjectInputStream.class);
        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        LoginHandler loginHandler = mock(LoginHandler.class);
        SocketController socketController = new SocketController(objectInputStream, objectOutputStream, loginHandler, true);
        DicePlacedResponse dicePlacedResponse = new DicePlacedResponse(true);
        socketController.handle(dicePlacedResponse);
        Assert.assertEquals(true, socketController.isDicePlaced());
    }

    @Test
    public void toolCardEffectAppliedResponse(){
        ObjectInputStream objectInputStream = mock(ObjectInputStream.class);
        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        LoginHandler loginHandler = mock(LoginHandler.class);
        SocketController socketController = new SocketController(objectInputStream, objectOutputStream, loginHandler, true);
        ToolCardEffectAppliedResponse toolCardEffectAppliedResponse = new ToolCardEffectAppliedResponse(true);
        socketController.handle(toolCardEffectAppliedResponse);
        Assert.assertEquals(true, socketController.isEffectApplied());
    }

    @Test
    public void handleDiceColorResponse(){
        ObjectInputStream objectInputStream = mock(ObjectInputStream.class);
        ObjectOutputStream objectOutputStream = mock(ObjectOutputStream.class);
        LoginHandler loginHandler = mock(LoginHandler.class);
        SocketController socketController = new SocketController(objectInputStream, objectOutputStream, loginHandler, true);
        DiceColorResponse diceColorResponse = new DiceColorResponse(Colors.BLUE);
        socketController.handle(diceColorResponse);
        Assert.assertEquals(Colors.BLUE, socketController.getDiceColor());
    }



}
