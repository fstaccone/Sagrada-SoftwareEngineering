package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;

import java.rmi.RemoteException;
import java.util.Timer;

public class TurnManager implements Runnable{

    private Timer turnTimer;
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
            turnManager();
        } catch (InterruptedException e ) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Remote exception from TurnManager");
        }
    }


    private void turnManager() throws InterruptedException, RemoteException {
        TurnTimer task;

        System.out.println("Round " + (match.getCurrentRound() + 1));
        System.out.println("First player: " + match.getPlayers().get(0).getName());

        for (PlayerMultiplayer player : match.getPlayers()) {
            player.setTurnsLeft(2);
        }

        // first turn
        for (PlayerMultiplayer player : match.getPlayers()) {

            // debug
            System.out.println("From match : Turn 1 - round " + (match.getCurrentRound() + 1) + " player: " + player.getName());

            if (player.getStatus() == ConnectionStatus.READY) {

                // solo RMI per ora
                match.getRemoteObservers().get(player).onYourTurn(player.getName());

                turnTimer = new Timer();
                task = new TurnTimer(this, player);
                turnTimer.schedule(task, turnTime);

                // wait for user action or for timer
                this.wait();
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

                // solo RMI per ora
                match.getRemoteObservers().get(match.getPlayers().get(i)).onYourTurn(match.getPlayers().get(i).getName());

                turnTimer = new Timer();
                task = new TurnTimer(this, match.getPlayers().get(i));
                turnTimer.schedule(task, turnTime);

                // wait for user action or for timer
                this.wait();

            } else {
                System.out.println("Player " + match.getPlayers().get(i).getName() + " has no turns left");
            }

            // todo: non ha molto senso, potrebbe averne se controllassimo in fase di testing che dopo questo aggiornamento siano davvero 0
            match.getPlayers().get(i).setTurnsLeft(match.getPlayers().get(i).getTurnsLeft() - 1);

        }

        // rearrange match.getPlayers() to keep the right order in next round
        // following the idea that the first player in this round will be the last in the next round
        match.getPlayers().add(match.getPlayers().get(0));
        match.getPlayers().remove(0);

        this.nextRound();
    }


    private void nextRound() throws InterruptedException, RemoteException {
        match.pushLeftDicesToRoundTrack();
        match.incrementRoundCounter();

        if (match.getCurrentRound() >= 10) {
            //match.calculateFinalScore();
        } else {
            this.turnManager();
        }
    }

    public void setExpiredTrue() {
        this.expired = true;
    }
}
