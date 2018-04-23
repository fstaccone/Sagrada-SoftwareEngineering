package it.polimi.ingsw.model.gameobjects;

import java.util.*;

public  class ToolCardDeck extends Deck{

    private Random randomGenerator;
    private List<String> toolDeck;
    private List<ToolCard> pickedToolCards;

    public ToolCardDeck() {
        this.toolDeck=new ArrayList<>();
        this.pickedToolCards = new ArrayList<>();

        for(int i=1;i<13;i++) {
            this.toolDeck.add("tool"+i);
        }

        for(int j=0;j<3;j++) {
            randomGenerator = new Random();
            int toolIndex = randomGenerator.nextInt(toolDeck.size() -1);
            String toolName = toolDeck.get(toolIndex);
            switch (toolName) {

                case "tool1":  toolName = "Pinza Sgrossatrice";
                    ToolCard card1 = new ToolCard(toolName);
                    this.pickedToolCards.add(card1);
                    this.toolDeck.remove("tool1");
                    break;
                case "tool2":  toolName= "Pennello per Eglomise";
                    ToolCard card2= new ToolCard(toolName);
                    this.pickedToolCards.add(card2);
                    this.toolDeck.remove("tool2");
                    break;
                case "tool3":  toolName = "Alesatore per Lamina di Rame";
                    ToolCard card3= new ToolCard(toolName);
                    this.pickedToolCards.add(card3);
                    this.toolDeck.remove("tool3");
                    break;
                case "tool4":  toolName = "Lathekin";
                    ToolCard card4= new ToolCard(toolName);
                    this.pickedToolCards.add(card4);
                    this.toolDeck.remove("tool4");
                    break;
                case "tool5":  toolName = "Taglierina Circolare";
                    ToolCard card5= new ToolCard(toolName);
                    this.pickedToolCards.add(card5);
                    this.toolDeck.remove("tool5");
                    break;
                case "tool6":  toolName = "Pennello per Pasta Salda";
                    ToolCard card6= new ToolCard(toolName);
                    this.pickedToolCards.add(card6);
                    this.toolDeck.remove("tool6");
                    break;
                case "tool7":  toolName = "Martelletto";
                    ToolCard card7= new ToolCard(toolName);
                    this.pickedToolCards.add(card7);
                    this.toolDeck.remove("tool7");
                    break;
                case "tool8":  toolName = "Tenaglia a Rotelle";
                    ToolCard card8= new ToolCard(toolName);
                    this.pickedToolCards.add(card8);
                    this.toolDeck.remove("tool8");
                    break;
                case "tool9":  toolName = "Riga in Sughero";
                    ToolCard card9= new ToolCard(toolName);
                    this.pickedToolCards.add(card9);
                    this.toolDeck.remove("tool9");
                    break;
                case "tool10": toolName = "Tampone Diamantato";
                    ToolCard card10= new ToolCard(toolName);
                    this.pickedToolCards.add(card10);
                    this.toolDeck.remove("tool10");
                    break;
                case "tool11": toolName = "Diluente per Pasta Salda";
                    ToolCard card11= new ToolCard(toolName);
                    this.pickedToolCards.add(card11);
                    this.toolDeck.remove("tool11");
                    break;
                case "tool12": toolName = "Taglierina Manuale";
                    ToolCard card12= new ToolCard(toolName);
                    this.pickedToolCards.add(card12);
                    this.toolDeck.remove("tool12");
                    break;
                default: toolName = "Invalid card";
                    break;
            }

        }


    }


    public List<ToolCard> getPickedToolCards() {
        return pickedToolCards;
    }

    @Override
    public Card pickOneCard() {
        return null;
    }

    @Override
    public Set<Card> pickNCards(int num) {
        return null;
    }


}