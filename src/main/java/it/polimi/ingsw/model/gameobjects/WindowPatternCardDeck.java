package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gameobjects.windowpatterncards.*;

import java.util.*;

public class WindowPatternCardDeck extends Deck<WindowPatternCard>{

    private Random randomGenerator;
    private List<String> windowPatternDeck;
    private List<WindowPatternCard> pickedWindowPatternCards;

    public WindowPatternCardDeck(int numOfPlayers) {

        this.windowPatternDeck=new ArrayList<>();
        this.pickedWindowPatternCards = new ArrayList<>();

        for(int i=1;i<25;i++) {
            this.windowPatternDeck.add("window"+i);
        }

        for(int j=0;j<4*numOfPlayers;j++) {
            randomGenerator = new Random();
            int windowIndex = randomGenerator.nextInt(windowPatternDeck.size() -1);
            String windowName = windowPatternDeck.get(windowIndex);
            switch (windowName) {

                case "window1":
                    this.pickedWindowPatternCards.add(new AuroraeMagnificus());
                    this.pickedWindowPatternCards.remove("window1");
                    break;
                case "window2":
                    this.pickedWindowPatternCards.add(new AuroraSagradis());
                    this.pickedWindowPatternCards.remove("window2");
                    break;
                case "window3":
                    this.pickedWindowPatternCards.add(new Batllo());
                    this.pickedWindowPatternCards.remove("window3");
                    break;
                case "window4":
                    this.pickedWindowPatternCards.add(new Bellesguard());
                    this.pickedWindowPatternCards.remove("window4");
                    break;
                case "window5":
                    this.pickedWindowPatternCards.add(new ChromaticSplendor());
                    this.pickedWindowPatternCards.remove("window5");
                    break;
                case "window6":
                    this.pickedWindowPatternCards.add(new Comitas());
                    this.pickedWindowPatternCards.remove("window6");
                    break;
                case "window7":
                    this.pickedWindowPatternCards.add(new Firelight());
                    this.pickedWindowPatternCards.remove("window7");
                    break;
                case "window8":
                    this.pickedWindowPatternCards.add(new Firmitas());
                    this.pickedWindowPatternCards.remove("window8");
                    break;
                case "window9":
                    this.pickedWindowPatternCards.add(new FractalDrops());
                    this.pickedWindowPatternCards.remove("window9");
                    break;
                case "window10":
                    this.pickedWindowPatternCards.add(new FulgorDelCielo());
                    this.pickedWindowPatternCards.remove("window10");
                    break;
                case "window11":
                    this.pickedWindowPatternCards.add(new Gravitas());
                    this.pickedWindowPatternCards.remove("window11");
                    break;
                case "window12":
                    this.pickedWindowPatternCards.add(new Industria());
                    this.pickedWindowPatternCards.remove("window12");
                    break;
                case "window13":
                    this.pickedWindowPatternCards.add(new KaleidoscopicDream());
                    this.pickedWindowPatternCards.remove("window13");
                    break;
                case "window14":
                    this.pickedWindowPatternCards.add(new LuxAstram());
                    this.pickedWindowPatternCards.remove("window14");
                    break;
                case "window15":
                    this.pickedWindowPatternCards.add(new LuxMundi());
                    this.pickedWindowPatternCards.remove("window15");
                    break;
                case "window16":
                    this.pickedWindowPatternCards.add(new LuzCelestial());
                    this.pickedWindowPatternCards.remove("window16");
                    break;
                case "window17":
                    this.pickedWindowPatternCards.add(new RipplesOfLight());
                    this.pickedWindowPatternCards.remove("window17");
                    break;
                case "window18":
                    this.pickedWindowPatternCards.add(new ShadowThief());
                    this.pickedWindowPatternCards.remove("window18");
                    break;
                case "window19":
                    this.pickedWindowPatternCards.add(new SunCatcher());
                    this.pickedWindowPatternCards.remove("window19");
                    break;
                case "window20":
                    this.pickedWindowPatternCards.add(new SunSGlory());
                    this.pickedWindowPatternCards.remove("window20");
                    break;
                case "window21":
                    this.pickedWindowPatternCards.add(new SymphonyOfLight());
                    this.pickedWindowPatternCards.remove("window21");
                    break;
                case "window22":
                    this.pickedWindowPatternCards.add(new ViaLux());
                    this.pickedWindowPatternCards.remove("window22");
                    break;
                case "window23":
                    this.pickedWindowPatternCards.add(new Virtus());
                    this.pickedWindowPatternCards.remove("window23");
                    break;
                case "window24":
                    this.pickedWindowPatternCards.add(new WaterOfLife());
                    this.pickedWindowPatternCards.remove("window24");
                    break;

                default: windowName = "Invalid card";
                    break;
            }

        }

    }

    public List<WindowPatternCard> getPickedWindowPatternCards() {
        return pickedWindowPatternCards;
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
