package it.polimi.ingsw.model.gameobjects;


import it.polimi.ingsw.model.gamelogic.Match;
import it.polimi.ingsw.model.gameobjects.effects.*;

public class ToolCard {

    private String name;
    private String toolID;
    private Effect effect;
    private String description;
    private Colors color;

    public ToolCard(String name, String toolID) {
        this.name = name;
        switch (this.name) {
            case "Pinza Sgrossatrice":
                effect = new IncrDecrDiceValueEffect();
                description = "Dopo aver scelto un dado aumenta o dominuisci il valore del dado scelto di 1\n" +
                        "Non puoi cambiare un 6 in 1 o un 1 in 6";
                color = Colors.VIOLET;
                break;
            case "Pennello per Eglomise":
                effect = new MoveDiceIgnoringColorRestrEffect();
                description = "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di colore\n" +
                        "Devi rispettare tutte le altre restrizioni di piazzamento";
                color = Colors.BLUE;
                break;
            case "Alesatore per Lamina di Rame":
                effect = new MoveDiceIgnoringValueRestrEffect();
                description = "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di valore\n" +
                        "Devi rispettare tutte le altre restrizioni di piazzamento";
                color = Colors.RED;
                break;
            case "Lathekin":
                effect = new MoveTwoDicesEffect();
                description = "Muovi esattamente due dadi rispettando tutte le restrizioni di piazzamento";
                color = Colors.YELLOW;
                break;
            case "Taglierina Circolare":
                effect = new ExchangeDiceRoundTrackEffect();
                description = "Dopo aver scelto un dado scambia quel dado con un dado sul Tracciato dei Round";
                color = Colors.GREEN;
                break;
            case "Pennello per Pasta Salda":
                effect = new ReRollDiceEffect();
                description = "Dopo aver scelto un dado tira nuovamente quel dado\n" +
                        "Se non puoi piazzarlo riponilo nella Riserva";
                color = Colors.VIOLET;
                break;
            case "Martelletto":
                effect = new ReRollAllReserveDicesEffect();
                description = "Tira nuovamente tutti i dadi della Riserva\n" +
                        "Questa carta può essera usata solo durante il tuo secondo turno prima di scegliere il secondo dado";
                color = Colors.BLUE;
                break;
            case "Tenaglia a Rotelle":
                effect = new ChooseAnotherDiceEffect();
                description = "Dopo il tuo primo turno scegli immediatamente un altro dado\n" +
                        "Salta il tuo secondo turno in questo round";
                color = Colors.RED;
                break;
            case "Riga in Sughero":
                effect = new PlaceDiceNotAdjacentToAnotherEffect();
                description = "Dopo aver scelto un dado piazzalo in una casella che non sia adiacente a un altro dado\n" +
                        "Devi rispettare tutte le restrizioni di piazzamento";
                color = Colors.YELLOW;
                break;
            case "Tampone Diamantato":
                effect = new UpsideDownDiceEffect();
                description = "Dopo aver scelto un dado giralo sulla faccia opposta\n" +
                        "6 diventa 1 - 5 diventa 2 - 4 diventa 3 ecc.";
                color = Colors.GREEN;
                break;
            case "Diluente per Pasta Salda":
                effect = new SubstituteDiceFromBagEffect();
                description = "Dopo aver scelto un dado riponilo nel Sacchetto poi pescane uno dal Sacchetto\n" +
                        "Scegli il valore del nuovo dado e piazzalo rispettando tutte le restrizioni di piazzamento";
                color = Colors.VIOLET;
                break;
            case "Taglierina Manuale":
                effect = new MoveTwoDicesColorRoundTrackEffect();
                description = "Muovi fino a due dadi dello stesso colore di un solo dado sul Tracciato dei Round\n" +
                        "Devi rispettare tutte le restrizioni di piazzamento";
                color = Colors.BLUE;
                break;
            default:
                System.out.println("Invalid card from ToolCard");
                break;
        }
        this.toolID = toolID;
    }

    public boolean useCard(Player caller, Match match) {
        return effect.applyEffect(caller, match);
    }

    public String getName() {
        return name;
    }

    public String getToolID() {
        return toolID;
    }

    @Override
    public String toString() {
        return toolID + ": " + name + "\ncolore: " + color + "\ndescrizione: " + description + "\n";
    }
}
