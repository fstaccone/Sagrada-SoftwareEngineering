package it.polimi.ingsw.control;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.LobbyObserver;
import it.polimi.ingsw.MatchObserver;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.socket.RequestHandler;
import it.polimi.ingsw.socket.SocketHandler;
import it.polimi.ingsw.socket.requests.*;
import it.polimi.ingsw.socket.responses.*;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Controller extends UnicastRemoteObject implements RemoteController, RequestHandler {

    private transient Lobby lobby;
    private List<SocketHandler> socketHandlers; //Intermediate variable, useful between View and Model


    public Controller(Lobby lobby) throws RemoteException {
        super();
        this.lobby = lobby;
        this.socketHandlers = new ArrayList<>();
    }

    @Override
    public ConnectionStatus checkName(String name) {
        return lobby.checkName(name);
    }


    @Override
    public void createMatch(String name, int difficulty, ObjectOutputStream outputStream) {
        this.lobby.addUsername(name);
        this.lobby.createSingleplayerMatch(name,difficulty,outputStream);
    }

    @Override
    public void addPlayer(String name) {
        this.lobby.addUsername(name);
        this.lobby.addPlayer(name);
    }

    @Override
    public void removePlayer(String name) {
        lobby.getRemoteObservers().remove(name);
        lobby.removeFromWaitingPlayers(name);
    }

    @Override
    public void goThrough(String name, boolean isSingle) {
        if (isSingle) {
            lobby.getSingleplayerMatches().get(name).goThrough();
        } else {
            lobby.getMultiplayerMatches().get(name).goThrough();
        }
    }

    @Override
    public boolean placeDice(int index, int x, int y, String name, boolean isSingle) {
        if (isSingle) {
            return lobby.getSingleplayerMatches().get(name).placeDice(name, index, x, y);
        } else {
            return lobby.getMultiplayerMatches().get(name).placeDice(name, index, x, y);
        }
    }

    @Override
    public boolean placeDiceTool11(int x, int y, String name, boolean isSingle) {
        if (isSingle) {
            lobby.getSingleplayerMatches().get(name).setDiceAction(true); // perchè?
        } else {
            return lobby.getMultiplayerMatches().get(name).placeDiceTool11(name, x, y);
        }
        return false;
    }

    @Override
    public void chooseWindow(String name, int index, boolean isSingle) {
        if (isSingle) {
            lobby.getSingleplayerMatches().get(name).setWindowPatternCard(name, index);
        } else {
            lobby.getMultiplayerMatches().get(name).setWindowPatternCard(name, index);
        }
    }

    @Override
    public void quitGame(String name, boolean isSingle) {
        if (isSingle) {
            lobby.getSingleplayerMatches().get(name).terminateMatch();
        } else {
            lobby.disconnect(name);
        }
    }

    @Override
    public void reconnect(String name) {
        lobby.reconnect(name);
    }

    @Override
    public Colors askForDiceColor(String name, boolean isSingle) {
        if (isSingle) {
            //DA FARE
        } else {
            return lobby.getMultiplayerMatches().get(name).getPlayer(name).getDiceFromBag().getColor();
        }
        return null;
    }

    @Override
    public void setDiceValue(int value, String name, boolean isSingle) {
        if (isSingle) {
            //DA FARE
        } else {
            System.out.println("OH");
            System.out.println(lobby.getMultiplayerMatches().get(name).getPlayer(name).getDiceFromBag());
            lobby.getMultiplayerMatches().get(name).getPlayer(name).getDiceFromBag().setValue(value);
            System.out.println(lobby.getMultiplayerMatches().get(name).getPlayer(name).getDiceFromBag());
        }
    }

    @Override
    public boolean useToolCard1(int diceChosen, String IncrOrDecr, String name, boolean isSingle) {
        if (isSingle) {
            //return lobby.getSingleplayerMatches().get(name).useToolCard
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard1(diceChosen, IncrOrDecr, name);
        }
        return false;
    }

    @Override
    public boolean useToolCard2or3(int n, int startX, int startY, int finalX, int finalY, String name, boolean isSingle) {
        if (isSingle) {
            //return lobby.getSingleplayerMatches().get(name).useToolCard
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard2or3(n, startX, startY, finalX, finalY, name);
        }
        return false;
    }

    @Override
    public boolean useToolCard4(int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String name, boolean isSingle) {
        if (isSingle) {
            //return lobby.getSingleplayerMatches().get(name).useToolCard
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard4(startX1, startY1, finalX1, finalY1, startX2, startY2, finalX2, finalY2, name);
        }
        return false;
    }

    @Override
    public boolean useToolCard5(int diceChosen, int roundChosen, int diceChosenFromRound, String name, boolean isSingle) {
        if (isSingle) {
            //return lobby.getSingleplayerMatches().get(name).useToolCard
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard5(diceChosen, roundChosen, diceChosenFromRound, name);
        }
        return false;
    }


    @Override
    public boolean useToolCard6(int diceChosen, String name, boolean isSingle) {
        if (isSingle) {
            //return lobby.getSingleplayerMatches().get(name).useToolCard
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard6(diceChosen, name);
        }
        return false;
    }

    @Override
    public boolean useToolCard7(String name, boolean isSingle) {
        if (isSingle) {
            //return lobby.getSingleplayerMatches().get(name).useToolCard
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard7(name);
        }
        return false;
    }

    @Override
    public boolean useToolCard8(String name, boolean isSingle) {
        if (isSingle) {
            //return lobby.getSingleplayerMatches().get(name).useToolCard
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard8(name);
        }
        return false;
    }

    @Override
    public boolean useToolCard9(int diceChosen, int finalX1, int finalY1, String name, boolean isSingle) {
        if (isSingle) {
            //return lobby.getSingleplayerMatches().get(name).useToolCard
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard9(diceChosen, finalX1, finalY1, name);
        }
        return false;
    }

    @Override
    public boolean useToolCard10(int diceChosen, String name, boolean isSingle) {
        if (isSingle) {
            //return lobby.getSingleplayerMatches().get(name).useToolCard
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard10(diceChosen, name);
        }
        return false;
    }

    @Override
    public boolean useToolCard11(int diceChosen, String name, boolean isSingle) {
        if (isSingle) {
            //return lobby.getSingleplayerMatches().get(name).useToolCard
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard11(diceChosen, name);
        }
        return false;
    }

    @Override
    public boolean useToolCard12(int roundFromTrack, int diceInRound, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String name, boolean isSingle) {
        if (isSingle) {
            //return lobby.getSingleplayerMatches().get(name).useToolCard
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard12(roundFromTrack, diceInRound, startX1, startY1, finalX1, finalY1, startX2, startY2, finalX2, finalY2, name);
        }
        return false;
    }


    @Override
    public Response handle(CheckUsernameRequest request) {
        return new NameAlreadyTakenResponse(checkName(request.username));
    }

    @Override
    public Response handle(CreateMatchRequest request) {
        createMatch(request.username,request.difficulty,socketHandlers.remove(0).getOut());
        return null;
    }

    @Override
    public Response handle(AddPlayerRequest request) {
        lobby.getSocketObservers().put(request.username, socketHandlers.remove(0).getOut());
        addPlayer(request.username);
        return null;
    }

    @Override
    public Response handle(RemoveFromWaitingPlayersRequest request) {
        lobby.getSocketObservers().remove(request.name);
        lobby.removeFromWaitingPlayers(request.name);
        return null;
    }

    @Override
    public Response handle(GoThroughRequest request) {
        goThrough(request.username, request.singlePlayer);
        return null;
    }

    @Override
    public Response handle(ChooseWindowRequest request) {
        chooseWindow(request.username, request.value, request.single);
        return null;
    }

    @Override
    public Response handle(PlaceDiceRequest request) {
        placeDice(request.diceChosen, request.coordinateX, request.coordinateY, request.username, request.single);
        return null;
    }

    @Override
    public Response handle(PlaceDiceTool11Request request) {
        boolean placed = placeDiceTool11(request.coordinateX, request.coordinateY, request.username, request.single);
        return new DicePlacedResponse(placed);
    }

    @Override
    public Response handle(UseToolCard1Request request) {
        boolean effectApplied = useToolCard1(request.diceChosen, request.IncrOrDecr, request.username, request.isSingle);
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard2or3Request request) {
        boolean effectApplied = useToolCard2or3(request.n, request.startX, request.startY, request.finalX, request.finalY, request.username, request.isSingle);
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard4Request request) {
        boolean effectApplied = useToolCard4(request.startX1, request.startY1, request.finalX1, request.finalY1, request.startX2, request.startY2, request.finalX2, request.finalY2, request.username, request.isSingle);
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard5Request request) {
        boolean effectApplied = useToolCard5(request.diceChosen, request.roundChosen, request.diceChosenFromRound, request.name, request.isSingle);
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    public Response handle(UseToolCard6Request request) {
        boolean effectApplied = useToolCard6(request.diceChosen, request.username, request.isSingle);
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard7Request request) {
        boolean effectApplied = useToolCard7(request.username, request.isSingle);
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard8Request request) {
        boolean effectApplied = useToolCard8(request.name, request.single);
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard9Request request) {
        boolean effectApplied = useToolCard9(request.diceChosen, request.finalX, request.finalY, request.username, request.isSingle);
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard10Request request) {
        boolean effectApplied = useToolCard10(request.diceChosen, request.username, request.isSingle);
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard11Request request) {
        boolean effectApplied = useToolCard11(request.diceChosen, request.username, request.isSingle);
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard12Request request) {
        boolean effectApplied = useToolCard12(request.roundFromTrack, request.diceInRound, request.startX1, request.startY1, request.finalX1, request.finalY1, request.startX2, request.startY2, request.finalX2, request.finalY2, request.name, request.isSingle);
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(QuitGameRequest request) {
        quitGame(request.name, request.single);
        return null;
    }

    @Override
    public Response handle(ReconnectionRequest request) {
        lobby.getSocketObservers().put(request.username, socketHandlers.remove(0).getOut());
        reconnect(request.username);
        return null;
    }

    @Override
    public Response handle(DiceColorRequest request) {
        Colors color = askForDiceColor(request.name, request.isSingle);
        return new DiceColorResponse(color);
    }

    @Override
    public Response handle(SetDiceValueRequest request) {
        setDiceValue(request.value, request.name, request.isSingle);
        return null;
    }


    public void observeLobby(String name, LobbyObserver lobbyObserver) {
        lobby.observeLobbyRemote(name, lobbyObserver);
    }

    public void observeMatch(String username, MatchObserver observer, boolean single, boolean reconnection) {
        lobby.observeMatchRemote(username, observer, single);

        if (reconnection) {
            lobby.transferAllData(username);
        }
    }

    public void addSocketHandler(SocketHandler socketHandler) {
        this.socketHandlers.add(socketHandler);
    }

}
