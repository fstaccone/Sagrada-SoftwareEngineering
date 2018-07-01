package it.polimi.ingsw.model.gameobjects;

import it.polimi.ingsw.model.gameobjects.windowpatterncards.*;

import java.util.Random;

public class WindowPatternCardDeck extends Deck<WindowPatternCard> {

    /**
     * Initializes the deck with all the scheme cards and then chooses randomly 4 scheme cards to show to each player
     *
     * @param numOfPlayers is the number of players
     */
    public WindowPatternCardDeck(int numOfPlayers) {
        super();
        for (int i = 1; i < 25; i++) {
            this.deck.add("window" + i);
        }

        Random randomGenerator;

        for (int j = 0; j < 4 * numOfPlayers; j++) {
            randomGenerator = new Random();
            int windowIndex = randomGenerator.nextInt(deck.size());
            String windowName = deck.get(windowIndex);
            switch (windowName) {

                case "window1":
                    this.pickedCards.add(new AuroraeMagnificus());
                    this.deck.remove("window1");
                    break;
                case "window2":
                    this.pickedCards.add(new AuroraSagradis());
                    this.deck.remove("window2");
                    break;
                case "window3":
                    this.pickedCards.add(new Batllo());
                    this.deck.remove("window3");
                    break;
                case "window4":
                    this.pickedCards.add(new Bellesguard());
                    this.deck.remove("window4");
                    break;
                case "window5":
                    this.pickedCards.add(new ChromaticSplendor());
                    this.deck.remove("window5");
                    break;
                case "window6":
                    this.pickedCards.add(new Comitas());
                    this.deck.remove("window6");
                    break;
                case "window7":
                    this.pickedCards.add(new Firelight());
                    this.deck.remove("window7");
                    break;
                case "window8":
                    this.pickedCards.add(new Firmitas());
                    this.deck.remove("window8");
                    break;
                case "window9":
                    this.pickedCards.add(new FractalDrops());
                    this.deck.remove("window9");
                    break;
                case "window10":
                    this.pickedCards.add(new FulgorDelCielo());
                    this.deck.remove("window10");
                    break;
                case "window11":
                    this.pickedCards.add(new Gravitas());
                    this.deck.remove("window11");
                    break;
                case "window12":
                    this.pickedCards.add(new Industria());
                    this.deck.remove("window12");
                    break;
                case "window13":
                    this.pickedCards.add(new KaleidoscopicDream());
                    this.deck.remove("window13");
                    break;
                case "window14":
                    this.pickedCards.add(new LuxAstram());
                    this.deck.remove("window14");
                    break;
                case "window15":
                    this.pickedCards.add(new LuxMundi());
                    this.deck.remove("window15");
                    break;
                case "window16":
                    this.pickedCards.add(new LuzCelestial());
                    this.deck.remove("window16");
                    break;
                case "window17":
                    this.pickedCards.add(new RipplesOfLight());
                    this.deck.remove("window17");
                    break;
                case "window18":
                    this.pickedCards.add(new ShadowThief());
                    this.deck.remove("window18");
                    break;
                case "window19":
                    this.pickedCards.add(new SunCatcher());
                    this.deck.remove("window19");
                    break;
                case "window20":
                    this.pickedCards.add(new SunSGlory());
                    this.deck.remove("window20");
                    break;
                case "window21":
                    this.pickedCards.add(new SymphonyOfLight());
                    this.deck.remove("window21");
                    break;
                case "window22":
                    this.pickedCards.add(new ViaLux());
                    this.deck.remove("window22");
                    break;
                case "window23":
                    this.pickedCards.add(new Virtus());
                    this.deck.remove("window23");
                    break;
                case "window24":
                    this.pickedCards.add(new WaterOfLife());
                    this.deck.remove("window24");
                    break;

                default:
                    break;
            }
        }
    }
}
