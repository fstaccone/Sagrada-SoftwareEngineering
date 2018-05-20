package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.ConnectionStatus;
import it.polimi.ingsw.MatchObserver;
import it.polimi.ingsw.model.gameobjects.PlayerMultiplayer;

import java.rmi.RemoteException;
import java.util.Timer;

public class TurnManager implements Runnable {

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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("Remote exception from TurnManager");
        }
    }


    private void turnManager() throws InterruptedException, RemoteException {

        TurnTimer task;
        int number = (2 * match.getPlayers().size()) + 1; // used to know the number of dices to be picked

        System.out.println("Round " + (match.getCurrentRound() + 1));
        System.out.println("First player: " + match.getPlayers().get(0).getName());

        for (PlayerMultiplayer player : match.getPlayers()) {
            player.setTurnsLeft(2);
        }

        match.getBoard().getReserve().throwDices(match.getBag().pickDices(number));

        /**
         * Per la scelta della carta schema uso un booleano (come per il gioco normale)
         * e lo faccio solo per il primo turno del primo round
         *
         * Ha senso farlo fuori dal for, in ogni caso questo metodo va splittato
         *
         * lo faccio io (PAOLO), lo ho in mente
         *
         */

        
        // first turn
        for (PlayerMultiplayer player : match.getPlayers()) {

            // debug
            System.out.println("From match : Turn 1 - round " + (match.getCurrentRound() + 1) + " player: " + player.getName());

            if (player.getStatus() == ConnectionStatus.READY) {


                // solo RMI per ora
                match.getRemoteObservers().get(player).onYourTurn( true);
                match.getRemoteObservers().get(player).onReserve( match.getBoard().getReserve().getDices().toString());


                match.setDiceAction(false);
                match.setToolAction(false);
                match.setEndsTurn(false);
                match.setSecondDiceAction(true); // this is useful only when a player can play two dices in the same turn

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

                match.getRemoteObservers().get(player).onYourTurn( false);// INSERITA DA ME(F.S.), da verificare con PAOLO
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
                match.getRemoteObservers().get(match.getPlayers().get(i)).onYourTurn( true);
                match.getRemoteObservers().get(match.getPlayers().get(i)).onReserve( match.getBoard().getReserve().getDices().toString());

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
                    System.out.println("TurnManager correttamente risvegliato");
                }

                match.getRemoteObservers().get(match.getPlayers().get(i)).onYourTurn( false);

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
        this.nextRound();

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
