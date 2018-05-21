package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.ReserveResponse;
import it.polimi.ingsw.YourTurnResponse;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;
import it.polimi.ingsw.model.gameobjects.WindowPatternCard;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.stream.Collectors;

public class TurnManager implements Runnable {

    private Timer turnTimer;
    TurnTimer task;
    private int turnTime;
    private MatchMultiplayer match;

    private boolean expired; // it's used to avoid double canceling of timer

    public TurnManager(MatchMultiplayer match, int turnTime) {
        this.turnTime = turnTime;
        this.match = match;
    }

    @Override
    public void run() {
        try {
            drawWindowPatternCards();
            turnManager();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Remote exception from TurnManager");
        }
    }

    private void drawWindowPatternCards() throws InterruptedException, RemoteException {

        for(int i = 0; i < match.getPlayers().size(); i++) {
            match.initializeWindowsToBeProposed(i);
            match.setWindowChosen(false);

            if (match.getPlayers().get(i).getStatus() == ConnectionStatus.READY) {
                match.getRemoteObservers().get(match.getPlayers().get(i)).onYourTurn(true);
                match.getRemoteObservers().get(match.getPlayers().get(i)).onWindowChoise(match.getWindowsProposed()
                        .stream()
                        .map(WindowPatternCard::toString)
                        .collect(Collectors.toList()));


                turnTimer = new Timer();
                task = new TurnTimer(match, match.getPlayers().get(i));
                turnTimer.schedule(task, turnTime);

                while (!match.isWindowChosen()) {
                    synchronized (match.getLock()) {
                        match.getLock().wait();
                    }
                }

                if (!expired) {
                    turnTimer.cancel();
                }

                match.getRemoteObservers().get(match.getPlayers().get(i)).onYourTurn(false);

            }
        }


    }



    private void turnManager() throws InterruptedException, RemoteException {

        TurnTimer task;

        System.out.println("Round " + (match.getCurrentRound() + 1));
        System.out.println("First player: " + match.getPlayers().get(0).getName());

        for (PlayerMultiplayer player : match.getPlayers()) {
            player.setTurnsLeft(2);
        }

        match.getBoard().getReserve().throwDices(match.getBag().pickDices(match.getPlayers().size()));
        
        // first turn
        for (PlayerMultiplayer player : match.getPlayers()) {

            // initialisation of flags to control the turn's flow
            match.setDiceAction(false);
            match.setToolAction(false);
            match.setEndsTurn(false);
            match.setSecondDiceAction(true); // this is useful only when a player can play two dices in the same turn

            // debug
            System.out.println("From match : Turn 1 - round " + (match.getCurrentRound() + 1) + " player: " + player.getName());

            if (player.getStatus() == ConnectionStatus.READY) {


                //RMI
                if(match.getRemoteObservers().size()!=0)
                    if(match.getRemoteObservers().get(player) !=null) {
                        match.getRemoteObservers().get(player).onYourTurn(true);
                        match.getRemoteObservers().get(player).onReserve( match.getBoard().getReserve().getDices().toString());
                    }

                //SOCKET
                if(match.getSocketObservers().size()!=0) {
                    if(match.getSocketObservers().get(player) !=null) {
                        try {
                            match.getSocketObservers().get(player).writeObject(new YourTurnResponse(true));
                            match.getSocketObservers().get(player).reset();
                            match.getSocketObservers().get(player).writeObject(new ReserveResponse(match.getBoard().getReserve().getDices().toString()));
                            match.getSocketObservers().get(player).reset();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                turnTimer = new Timer();
                task = new TurnTimer(match, player);
                turnTimer.schedule(task, turnTime);

                /**
                 * diceAction e toolAction vengono settati inizialmente a false, se l'azione corrispondente viene
                 * completata con successo viene settato a true il rispettivo flag. Quando saranno entrambi veri
                 * la condizione del ciclo sarà falsa
                 * I metodi utilizzati ed i flag appartengono a match, in modo che possano essere settati a true senza risvegliare
                 * TurnManager, sarà risvegliato solo dopo che una azione è stata completata con successo
                 */
                // wait for user action or for timer
                while (checkCondition()) {
                    synchronized (match.getLock()) {
                        match.getLock().wait();
                    }
                    System.out.println("TurnManager correttamente risvegliato");
                }

                //RMI
                if(match.getRemoteObservers().size()!=0)
                    if (match.getRemoteObservers().get(player)!=null)
                        match.getRemoteObservers().get(player).onYourTurn( false);

                //SOCKET
                if(match.getSocketObservers().size()!=0) {
                    if (match.getSocketObservers().get(player)!=null) {
                        try {
                            match.getSocketObservers().get(player).writeObject(new YourTurnResponse(false));
                            match.getSocketObservers().get(player).reset();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            if (!expired) {
                turnTimer.cancel();
            }

            player.setTurnsLeft(player.getTurnsLeft() - 1);

        }

        // second turn todo: controllare dopo aver verificato che funzioni
        for (int i = match.getPlayers().size() - 1; i >= 0; i--) {

            if (match.getPlayers().get(i).getTurnsLeft() > 0 && match.getPlayers().get(i).getStatus() == ConnectionStatus.READY) {
                System.out.println("From match : Turn 2 - round " + (match.getCurrentRound() + 1) + " player: " + match.getPlayers().get(i).getName());

                //RMI
                if(match.getRemoteObservers().size()!=0)
                    if (match.getRemoteObservers().get(match.getPlayers().get(i))!=null) {
                        match.getRemoteObservers().get(match.getPlayers().get(i)).onYourTurn(true);
                        match.getRemoteObservers().get(match.getPlayers().get(i)).onReserve(match.getBoard().getReserve().getDices().toString());
                    }

                //SOCKET
                if(match.getSocketObservers().size()!=0) {
                    if(match.getSocketObservers().get(match.getPlayers().get(i))!=null) {
                        try {
                            match.getSocketObservers().get(match.getPlayers().get(i)).writeObject(new YourTurnResponse(true));
                            match.getSocketObservers().get(match.getPlayers().get(i)).reset();
                            match.getSocketObservers().get(match.getPlayers().get(i)).writeObject(new ReserveResponse(match.getBoard().getReserve().getDices().toString()));
                            match.getSocketObservers().get(match.getPlayers().get(i)).reset();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                match.setDiceAction(false);
                match.setToolAction(false);
                match.setEndsTurn(false);
                match.setSecondDiceAction(true); // this is useful only when a player can play two dices in the same turn

                turnTimer = new Timer();
                task = new TurnTimer(match, match.getPlayers().get(i));
                turnTimer.schedule(task, turnTime);

                // wait for user action or for timer
                while (checkCondition()) {
                    synchronized (match.getLock()) {
                        match.getLock().wait();
                    }
                    // debug
                    System.out.println("TurnManager correttamente risvegliato");
                }

                //RMI
                if(match.getRemoteObservers().size()!=0)
                    if(match.getRemoteObservers().get(match.getPlayers().get(i))!=null)
                        match.getRemoteObservers().get(match.getPlayers().get(i)).onYourTurn( false);

                //SOCKET
                if(match.getSocketObservers().size()!=0) {
                    if (match.getSocketObservers().get(match.getPlayers().get(i)) != null) {
                        try {
                            match.getSocketObservers().get(match.getPlayers().get(i)).writeObject(new YourTurnResponse(false));
                            match.getSocketObservers().get(match.getPlayers().get(i)).reset();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            if (!expired) {
                turnTimer.cancel();
            }

            // todo: non ha molto senso, potrebbe averne se controllassimo in fase di testing che dopo questo aggiornamento siano davvero 0
            match.getPlayers().get(i).setTurnsLeft(match.getPlayers().get(i).getTurnsLeft() - 1);

        }

        // rearrange match.getPlayers() to keep the right order in next round
        // following the idea that the first player in this round will be the last in the next round
        match.getPlayers().add(match.getPlayers().get(0));
        match.getPlayers().remove(0);
        nextRound();

    }

    // todo: da spiegare (se funziona)
    private boolean checkCondition() {
        return !((match.isToolAction() && match.isDiceAction() && match.isSecondDiceAction()) || match.isEndsTurn());
    }

    private void nextRound() throws InterruptedException, RemoteException {
        match.pushLeftDicesToRoundTrack();
        match.incrementRoundCounter();

        if (match.getCurrentRound() >= 10) {
            //match.calculateFinalScore(); // può stare anche in match
        } else {
            this.turnManager();
        }
    }

    public void setExpiredTrue() {
        this.expired = true;
    }
}
