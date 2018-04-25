package it.polimi.ingsw.model.gameobjects;

import java.util.*;

public class Reserve {

    private List<Dice> dices;

    public Reserve(){
        dices = new ArrayList<>();
    }

    public void throwDices(List<Dice> init){
        Random rand = new Random();

        for(Dice dice : init){
            int val = rand.nextInt(6)+1;
            dice.setValue(val);
            dices.add(dice);
        }
    }

    public boolean isEmpty(){
        return dices.isEmpty();
    }

    public void showReserve(){
        if(!(dices.isEmpty())){
            System.out.println("Dices in reserve:");
            int i=0;
            for(Dice nav : dices){
                System.out.println(nav.toString()+" id="+i);
                i++;
            }
        }
        else System.out.println("The reserve is empty.");
    }

    public Dice chooseDice() {
        if(!(dices.isEmpty())){
            System.out.println("Please choose a dice from the reserve by writing the corresponding id: ");
            showReserve();
            Scanner scan = new Scanner(System.in);
            int i = scan.nextInt();
            if(i<0 || i>dices.size()-1){
                while(i<0 || i>dices.size()-1) {
                    System.out.println("Please insert a correct value.");
                    i = scan.nextInt();
                }
            }
            Dice choice = dices.get(i);
            dices.remove(i);
            System.out.println("Chosen dice: "+choice.toString());
            return choice;
        }
        else {
            System.out.println("The reserve is empty."); //andrebbe fatto lanciando ExceptionEmptyReserve
            return null;
        }
    }
    public List<Dice> endRound(){
        System.out.println("End of the round.");
        ArrayList<Dice> dicesLeft = new ArrayList<>();
        while(!dices.isEmpty()){
            System.out.println("Dice removed from reserve: "+dices.get(0));
            dicesLeft.add(dices.get(0));
            dices.remove(0);
            //i dadi restanti nella riserva vanno rimossi e posti nella corrispondente casella del roundtrack
            this.showReserve();
        }
        //aggiungere dicesLeft alla RoundTrack
        return dicesLeft;
        //dicesLeft è ArrayList di dadi che verrà usato come parametro per il metodo putDices di RoundTrack
    }

    public List<Dice> removeAllDices(){
        ArrayList<Dice> dicesRemoved = new ArrayList<>();
        while(!dices.isEmpty()){
            dicesRemoved.add(dices.get(0));
            dices.remove(0);
        }
        return dicesRemoved;
    }
/*
    public static void main(String args[]){
        Bag bag = new Bag(2);
        List<Dice> pescata = bag.pickDices(3);
        Reserve riserva = new Reserve();
        riserva.throwDices(pescata);
        riserva.showReserve();
        Dice dado = riserva.chooseDice();
        Square place = new Square(Colors.BLUE);
        place.putDice(dado);
        List<Dice> leftDices = riserva.endRound();
        RoundTrack track = new RoundTrack();
        track.putDices(leftDices);
        track.showRoundTrack();
        pescata = bag.pickDices(2);
        riserva.throwDices(pescata);
        riserva.showReserve();
        leftDices = riserva.endRound();
        track.putDices(leftDices);
        track.showRoundTrack();
    }
*/
}