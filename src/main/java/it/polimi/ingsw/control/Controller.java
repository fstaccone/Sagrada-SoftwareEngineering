package it.polimi.ingsw.control;

import it.polimi.ingsw.model.gamelogic.ConnectionStatus;
import it.polimi.ingsw.model.gamelogic.Lobby;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.socket.SocketHandler;
import it.polimi.ingsw.socket.requests.*;
import it.polimi.ingsw.socket.responses.*;
import it.polimi.ingsw.view.LobbyObserver;
import it.polimi.ingsw.view.MatchObserver;

import java.io.ObjectOutputStream;
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
        this.lobby.createSingleplayerMatch(name, difficulty, outputStream);
    }

    @Override
    public void addPlayer(String name) {
        this.lobby.addUsername(name);
        this.lobby.addToWaitingPlayers(name);
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
            return lobby.getSingleplayerMatches().get(name).placeDiceTool11(name, x, y);
        } else {
            return lobby.getMultiplayerMatches().get(name).placeDiceTool11(name, x, y);
        }
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
            return lobby.getSingleplayerMatches().get(name).getPlayer().getDiceFromBag().getColor();
        } else {
            return lobby.getMultiplayerMatches().get(name).getPlayer(name).getDiceFromBag().getColor();
        }
    }

    @Override
    public void setDiceValue(int value, String name, boolean isSingle) {
        if (isSingle) {
            lobby.getSingleplayerMatches().get(name).getPlayer().getDiceFromBag().setValue(value);
        } else {
            lobby.getMultiplayerMatches().get(name).getPlayer(name).getDiceFromBag().setValue(value);
        }
    }

    @Override
    public boolean useToolCard1(int diceToBeSacrificed, int diceChosen, String IncrOrDecr, String name, boolean isSingle) {
        if (isSingle) {
            return lobby.getSingleplayerMatches().get(name).useToolCard1(diceToBeSacrificed, diceChosen, IncrOrDecr, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard1(diceToBeSacrificed, diceChosen, IncrOrDecr, name);
        }
    }

    @Override
    public boolean useToolCard2or3(int diceToBeSacrificed, int n, int startX, int startY, int finalX, int finalY, String name, boolean isSingle) {
        if (isSingle) {
            return lobby.getSingleplayerMatches().get(name).useToolCard2or3(diceToBeSacrificed, n, startX, startY, finalX, finalY, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard2or3(diceToBeSacrificed, n, startX, startY, finalX, finalY, name);
        }
    }

    @Override
    public boolean useToolCard4(int diceToBeSacrificed, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String name, boolean isSingle) {
        if (isSingle) {
            return lobby.getSingleplayerMatches().get(name).useToolCard4(diceToBeSacrificed, startX1, startY1, finalX1, finalY1, startX2, startY2, finalX2, finalY2, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard4(diceToBeSacrificed, startX1, startY1, finalX1, finalY1, startX2, startY2, finalX2, finalY2, name);
        }
    }

    @Override
    public boolean useToolCard5(int diceToBeSacrificed, int diceChosen, int roundChosen, int diceChosenFromRound, String name, boolean isSingle) {
        if (isSingle) {
            return lobby.getSingleplayerMatches().get(name).useToolCard5(diceToBeSacrificed, diceChosen, roundChosen, diceChosenFromRound, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard5(diceToBeSacrificed, diceChosen, roundChosen, diceChosenFromRound, name);
        }
    }


    @Override
    public boolean useToolCard6(int diceToBeSacrificed, int diceChosen, String name, boolean isSingle) {
        if (isSingle) {
            return lobby.getSingleplayerMatches().get(name).useToolCard6(diceToBeSacrificed, diceChosen, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard6(diceToBeSacrificed, diceChosen, name);
        }
    }

    @Override
    public boolean useToolCard7(int diceToBeSacrificed, String name, boolean isSingle) {
        if (isSingle) {
            return lobby.getSingleplayerMatches().get(name).useToolCard7(diceToBeSacrificed, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard7(diceToBeSacrificed, name);
        }
    }

    @Override
    public boolean useToolCard8(int diceToBeSacrificed, String name, boolean isSingle) {
        if (isSingle) {
            return lobby.getSingleplayerMatches().get(name).useToolCard8(diceToBeSacrificed, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard8(diceToBeSacrificed, name);
        }
    }

    @Override
    public boolean useToolCard9(int diceToBeSacrificed, int diceChosen, int finalX1, int finalY1, String name, boolean isSingle) {
        if (isSingle) {
            return lobby.getSingleplayerMatches().get(name).useToolCard9(diceToBeSacrificed, diceChosen, finalX1, finalY1, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard9(diceToBeSacrificed, diceChosen, finalX1, finalY1, name);
        }
    }

    @Override
    public boolean useToolCard10(int diceToBeSacrificed, int diceChosen, String name, boolean isSingle) {
        if (isSingle) {
            return lobby.getSingleplayerMatches().get(name).useToolCard10(diceToBeSacrificed, diceChosen, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard10(diceToBeSacrificed, diceChosen, name);
        }
    }

    @Override
    public boolean useToolCard11(int diceToBeSacrificed, int diceChosen, String name, boolean isSingle) {
        if (isSingle) {
            return lobby.getSingleplayerMatches().get(name).useToolCard11(diceToBeSacrificed, diceChosen, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard11(diceToBeSacrificed, diceChosen, name);
        }
    }

    @Override
    public boolean useToolCard12(int diceToBeSacrificed, int roundFromTrack, int diceInRound, int startX1, int startY1, int finalX1, int finalY1, int startX2, int startY2, int finalX2, int finalY2, String name, boolean isSingle) {
        if (isSingle) {
            return lobby.getSingleplayerMatches().get(name).useToolCard12(diceToBeSacrificed, roundFromTrack, diceInRound, startX1, startY1, finalX1, finalY1, startX2, startY2, finalX2, finalY2, name);
        } else {
            return lobby.getMultiplayerMatches().get(name).useToolCard12(diceToBeSacrificed, roundFromTrack, diceInRound, startX1, startY1, finalX1, finalY1, startX2, startY2, finalX2, finalY2, name);
        }
    }

    @Override
    public void removeMatch(String name) {
        lobby.removeFromMatchMulti(name);
    }


    @Override
    public Response handle(CheckUsernameRequest request) {
        return new NameAlreadyTakenResponse(checkName(request.getUsername()));
    }

    @Override
    public Response handle(CreateMatchRequest request) {
        createMatch(request.getUsername(), request.getDifficulty(), socketHandlers.remove(0).getOut());
        return null;
    }

    @Override
    public Response handle(AddPlayerRequest request) {
        if (socketHandlers.size() > 1) {
            SocketHandler toBeSaved = socketHandlers.get(socketHandlers.size() - 1);
            socketHandlers.clear();
            socketHandlers.add(toBeSaved);
        }
        lobby.getSocketObservers().put(request.getUsername(), socketHandlers.remove(0).getOut());
        addPlayer(request.getUsername());
        return null;
    }

    @Override
    public Response handle(RemoveFromWaitingPlayersRequest request) {
        lobby.getSocketObservers().remove(request.getName());
        lobby.removeFromWaitingPlayers(request.getName());
        return null;
    }

    @Override
    public Response handle(GoThroughRequest request) {
        goThrough(request.getUsername(), request.isSinglePlayer());
        return null;
    }

    @Override
    public Response handle(ChooseWindowRequest request) {
        chooseWindow(request.getUsername(), request.getValue(), request.isSingle());
        return null;
    }

    @Override
    public Response handle(PlaceDiceRequest request) {
        placeDice(request.getDiceChosen(), request.getCoordinateX(), request.getCoordinateY(), request.getUsername(), request.isSingle());
        return null;
    }

    @Override
    public Response handle(PlaceDiceTool11Request request) {
        boolean placed = placeDiceTool11(request.getCoordinateX(), request.getCoordinateY(), request.getUsername(), request.isSingle());
        return new DicePlacedResponse(placed);
    }

    @Override
    public Response handle(UseToolCard1Request request) {
        boolean effectApplied = useToolCard1(request.getDiceToBeSacrificed(), request.getDiceChosen(), request.getIncrOrDecr(), request.getUsername(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard2or3Request request) {
        boolean effectApplied = useToolCard2or3(request.getDiceToBeSacrificed(), request.getN(), request.getStartX(), request.getStartY(), request.getFinalX(), request.getFinalY(), request.getUsername(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard4Request request) {
        boolean effectApplied = useToolCard4(request.getDiceToBeSacrificed(), request.getStartX1(), request.getStartY1(), request.getFinalX1(), request.getFinalY1(), request.getStartX2(), request.getStartY2(), request.getFinalX2(), request.getFinalY2(), request.getUsername(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard5Request request) {
        boolean effectApplied = useToolCard5(request.getDiceToBeSacrificed(), request.getDiceChosen(), request.getRoundChosen(), request.getDiceChosenFromRound(), request.getName(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    public Response handle(UseToolCard6Request request) {
        boolean effectApplied = useToolCard6(request.getDiceToBeSacrificed(), request.getDiceChosen(), request.getUsername(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard7Request request) {
        boolean effectApplied = useToolCard7(request.getDiceToBeSacrificed(), request.getUsername(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard8Request request) {
        boolean effectApplied = useToolCard8(request.getDiceToBeSacrificed(), request.getName(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard9Request request) {
        boolean effectApplied = useToolCard9(request.getDiceToBeSacrificed(), request.getDiceChosen(), request.getFinalX(), request.getFinalY(), request.getUsername(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard10Request request) {
        boolean effectApplied = useToolCard10(request.getDiceToBeSacrificed(), request.getDiceChosen(), request.getUsername(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard11Request request) {
        boolean effectApplied = useToolCard11(request.getDiceToBeSacrificed(), request.getDiceChosen(), request.getUsername(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(UseToolCard12Request request) {
        boolean effectApplied = useToolCard12(request.getDiceToBeSacrificed(), request.getRoundFromTrack(), request.getDiceInRound(), request.getStartX1(), request.getStartY1(), request.getFinalX1(), request.getFinalY1(), request.getStartX2(), request.getStartY2(), request.getFinalX2(), request.getFinalY2(), request.getName(), request.isSingle());
        return new ToolCardEffectAppliedResponse(effectApplied);
    }

    @Override
    public Response handle(QuitGameRequest request) {
        quitGame(request.getName(), request.isSingle());
        return null;
    }

    @Override
    public Response handle(ReconnectionRequest request) {
        lobby.getSocketObservers().put(request.getUsername(), socketHandlers.remove(0).getOut());
        reconnect(request.getUsername());
        return null;
    }

    @Override
    public Response handle(DiceColorRequest request) {
        Colors color = askForDiceColor(request.getName(), request.isSingle());
        return new DiceColorResponse(color);
    }

    @Override
    public Response handle(SetDiceValueRequest request) {
        setDiceValue(request.getValue(), request.getName(), request.isSingle());
        return null;
    }

    @Override
    public Response handle(PrivateCardChosenRequest request) {
        choosePrivateCard(request.getUsername(), request.getCardPosition());
        return null;
    }

    @Override
    public Response handle(TerminateMatchRequest request) {
        lobby.removeFromMatchMulti(request.getName());
        return null;
    }

    public void choosePrivateCard(String username, int cardPosition) {
        lobby.getSingleplayerMatches().get(username).setPrivateCardChosen(cardPosition);
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
