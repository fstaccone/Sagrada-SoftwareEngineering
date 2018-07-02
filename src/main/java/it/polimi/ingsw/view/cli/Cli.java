package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.control.RemoteController;
import it.polimi.ingsw.control.SocketController;
import it.polimi.ingsw.model.gameobjects.Colors;
import it.polimi.ingsw.socket.requests.*;

import java.io.*;
import java.rmi.RemoteException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Cli {

    private String username;
    private SocketController controllerSocket;
    private RemoteController controllerRmi;
    private boolean myTurn;
    private final PrintWriter printer;
    private boolean stillPlaying;
    private List<String> dicesList;
    private List<String> toolCardsList;
    private List<String> publicCardsList;
    private List<ToolCommand> toolCommands;
    private List<String> privateCard;
    private String[][] mySchemeCard;
    private int myFavorTokens;
    private Map<String, Integer> otherFavorTokensMap;
    private Map<String, String[][]> otherSchemeCardsMap;
    private String roundTrack;
    private int diceChosenToBeSacrificed = 9;
    private int diceChosen = 9;
    private int coordinateX;
    private int coordinateY;
    private boolean diceValueToBeSet;
    private boolean tool11DiceToBePlaced;
    private List<String> playersNames;
    private boolean windowChosen;

    private boolean single;
    private boolean enablePrivateCardChoice;

    private static final String WELCOME = "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@./@@@@@/*@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@./@@@@@/,@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@..@@@@@..@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@.@@&..@@@@@..&@@.@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@.@@(..@@@@@..(@@.@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%.*@,..&@ @&..,@*.%@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@,. @...%&.@%...@ .,@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ..@.. ....  ..@.. @@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@... .#@% . %@&.....@@@@@@@@@@\n" +
            "@@/ ____|@@/\\@@@/ ____|  __ \\@@@@@/\\@@@|  __ \\@@@/\\@@@@@@@@@@@@@@@ .. .(*.....*@. ...@@@@@@@@@@\n" +
            "@| (___@@@/  \\@| |@@__| |__) |@@@/  \\@@| |@@| |@/  \\@@@@@@@@@@@@@@. , . ../@ .... , .@@@@@@@@@@\n" +
            "@@\\___ \\@/ /\\ \\| |@|_ |  _  /@@@/ /\\ \\@| |@@| |/ /\\ \\@@@@@@@@@@@@@, ,....@ .#....., ,@@@@@@@@@@\n" +
            "@@____) / ____ \\ |__| | |@\\ \\@@/ ____ \\| |__| / ____ \\@@@@@@@@@@@(. *...( ...% ...* .(@@@@@@@@@\n" +
            "@|_____/_/@@@@\\_\\_____|_|@@\\_\\/_/@@@@\\_\\_____/_/@@@@\\_\\@@@@@@@@@@..@.@  &.... #..@.@..@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ , .&.*.%&.@ @ .&.., @@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@.&..#.%.@@,@/&..#..&.@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "\nBenvenuto in questo fantastico gioco, ";

    private static final String RULES = ("\nSiete artisti in competizione tra loro per creare la\n" +
            "vetrata più spettacolare della Sagrada Familia. I\n" +
            "vostri frammenti di vetro sono rappresentati da dadi,\n" +
            "caratterizzati da un colore e da una sfumaturache\n" +
            "è indicata dal loro valore numerico (più basso è il\n" +
            "valore più la sfumatura è chiara).\n" +
            "Ad ogni round i giocatori, a turno, scelgono dadi da una\n" +
            "riserva comune e li piazzando sulle proprie finestre. I\n" +
            "giocatori devono piazzare seguendo le restrizioni della\n" +
            "propria Carta Schema e non possono piazzare dadi\n" +
            "adiacenti con lo stesso colore o valore.\n" +
            "Dopo 10 round i giocatori ottengono punti in funzione\n" +
            "degli obiettivi pubblici e privati che hanno raggiunto e il\n" +
            "giocatore con più punti è il vincitore!");

    private static final String HELP_IN_TURN_MULTI = (
            "\n 'passa'                                     per passare il turno al prossimo giocatore " +
                    "\n 'pd' + 'coordinata x' + 'coordinata y'      per piazzare il dado nella tua carta schema nella posizione [x][y] " +
                    "\n 'pd11' + 'coordinata x' + 'coordinata y'    per piazzare il dado scelto con la carta utensile 11 nella tua carta schema nella posizione [x][y] " +
                    "\n 'scs' + 'numero'                            per scegliere la tua carta schema 'numero)' dall'elenco delle carte schema(disponibile una volta soltanto, all'inizio della partita)" +
                    "\n 'sd' + 'numero'                             per scegliere il dado 'numero)' dall'elenco della riserva" +
                    "\n 'usautensile' + 'numero'                    per usare l'effetto della carta utensile [numero]" +
                    "\n 'valore' + 'numero'                         per assegnare al dado proposto dalla carta utensile 11 il valore [numero] ");

    private static final String HELP_GENERAL_MULTI = (
            "\n 'aiuto'                                     per mostrare i comandi di gioco" +
                    "\n 'avversari'                                 per mostrare i nomi degli avversari" +
                    "\n 'cartaschema'                               per mostrare tutte la tua carta schema" +
                    "\n 'carteschema'                               per mostrare tutte le carte schema degli avversari" +
                    "\n 'esci'                                      per uscire dal gioco" +
                    "\n 'mcs' + 'nome'                              per mostrare la carta schema del tuo avversario il cui nome è [nome]" +
                    "\n 'priv'                                      per mostrare la tua carta obiettivo privato" +
                    "\n 'pub'                                       per mostrare le tue carte obiettivo pubblico" +
                    "\n 'regole'                                    per mostrare le regole del gioco" +
                    "\n 'riserva'                                   per mostrare lo stato corrente della riserva, (disponibile solo dall'inizio del primo turno)" +
                    "\n 'segnalini'                                 per mostrare i tuoi segnalini " +
                    "\n 'segnalinialtrui'                           per mostrare i segnalini degli avversari" +
                    "\n 'tracciato'                                 per mostrare lo stato attuale del tracciato dei round" +
                    "\n 'utensile' + 'numero'                       per mostrare la descrizione d'uso della carta utensile [numero] " +
                    "\n 'utensili'                                  per mostrare tutte le carte utensili disponibili \n");

    private static final String HELP_SINGLE = (
            "\n 'aiuto'                                     per mostrare i comandi di gioco" +
                    "\n 'cartaschema'                               per mostrare tutte la tua carta schema" +
                    "\n 'esci'                                      per uscire dal gioco" +
                    "\n 'passa'                                     per passare il turno" +
                    "\n 'pd' + 'coordinata x' + 'coordinata y'      per piazzare il dado nella tua carta schema nella posizione [x][y] " +
                    "\n 'priv'                                      per mostrare le tue carte obiettivo privato" +
                    "\n 'pub'                                       per mostrare le tue carte obiettivo pubblico" +
                    "\n 'regole'                                    per mostrare le regole del gioco" +
                    "\n 'riserva'                                   per mostrare lo stato corrente della riserva, (disponibile solo dall'inizio del primo turno)" +
                    "\n 'sacrifico' + 'numero'                      per sacrificare il dado 'numero)' dall'elenco della riserva per l'utilizzo di una carta utensile" +
                    "\n 'scs' + 'numero'                            per scegliere la tua carta schema 'numero)' dall'elenco delle carte schema(disponibile una volta soltanto, all'inizio della partita)" +
                    "\n 'sd' + 'numero'                             per scegliere il dado 'numero)' dall'elenco della riserva" +
                    "\n 'tracciato'                                 per mostrare lo stato attuale del tracciato dei round" +
                    "\n 'usautensile' + 'number'                    per usare l'effetto della carta utensile [numero]" +
                    "\n 'utensile' + 'number'                       per mostrare la descrizione d'uso della carta utensile [numero] [numero] " +
                    "\n 'utensili'                                  per mostrare tutte le carte utensili disponibili  \n" +
                    "\n 'valore' + 'numero'                         per assegnare al dado proposto dalla carta utensile 11 il valore [numero] ");


    private static final String SYNTAX_ERROR = (
            "\nATTENZIONE: Richiesta non valida a livello di sintassi!\n");

    private static final String GAME_ERROR = (
            "\nATTENZIONE: Richiesta di gioco non valida. Non stai rispettando le regole della carta utensile!\n"); //STAMPATO ANCHE SE PROVI A MODIFICARE DADO NELLA RISERVA OUT OF BOUND , si può migliorare


    /**
     * Initializes the Cli
     *
     * @param username         is the name of the player who is the owner of this view
     * @param controllerRmi    is the server side controller used by this view (if it uses Rmi connection) to contact the model
     * @param controllerSocket is the client side controller used by this view (if it uses Socket connection) to contact the model
     * @param single           is a boolean used to let Cli understand if it is singleplayer or not
     */
    public Cli(String username, RemoteController controllerRmi, SocketController controllerSocket, boolean single) {
        this.username = username;
        this.controllerRmi = controllerRmi;
        this.controllerSocket = controllerSocket;
        printer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(FileDescriptor.out)));
        windowChosen = false;
        otherFavorTokensMap = new HashMap<>();
        otherSchemeCardsMap = new HashMap<>();
        myTurn = false;
        new KeyboardHandler().start();
        this.single = single;
        privateCard = new ArrayList<>();
        toolCommands = new ArrayList<>();
        toolCardsList = new ArrayList<>();
        publicCardsList = new ArrayList<>();
        playersNames = new ArrayList<>();
        diceValueToBeSet = false;
        tool11DiceToBePlaced = false;
        stillPlaying = true;
        enablePrivateCardChoice = false;
    }

    public boolean isSingle() {
        return single;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Prints the welcome string
     */
    public void printWelcome() {
        printer.println(WELCOME + username + "!\n");
    }

    /**
     * Notify used to update this Cli's owner favor tokens; it is propagated from RmiCli and SocketController
     *
     * @param value is the current number of favor tokens
     */
    public void onMyFavorTokens(int value) {
        this.myFavorTokens = value;
    }

    /**
     * Notify used to update this Cli's owner map about other players and their favor tokens; it is propagated from RmiCli and SocketController
     *
     * @param value is the current number of tokens that player
     * @param name  is the name of that player
     */
    public void onOtherFavorTokens(int value, String name) {
        this.otherFavorTokensMap.put(name, value);
    }

    /**
     * Notify used to update this Cli's owner map about other players and their scheme cards; it is propagated from RmiCli and SocketController
     *
     * @param scheme is the current scheme card of that  player
     * @param name   is the name of that player
     */
    public void onOtherSchemeCards(String[][] scheme, String name) {
        this.otherSchemeCardsMap.put(name, scheme);
    }

    /**
     * Notify used to update this Cli's owner about the beginning of the match; it is propagated from RmiCli and SocketController
     *
     * @param names is the names of the opponents
     */
    public void onGameStarted(List<String> names, int turnTime) {
        if (names != null) {
            playersNames = names;
            printNames();
            printer.println("Hai a disposizione " + turnTime / 1000 + " secondi ad ogni turno per giocare! \n");
            printer.flush();
        }
    }

    /**
     * Notify used to update this Cli's owner map about the current state of the round track; it is propagated from RmiCli and SocketController
     *
     * @param roundTrack is the string representing the current round track
     */
    public void onRoundTrack(String roundTrack) {
        this.roundTrack = roundTrack;
    }


    /**
     * Notify used to update this Cli's owner about the turn management; it is propagated from RmiCli and SocketController
     *
     * @param yourTurn is true if it is the turn of this Cli's owner
     * @param reserve  is the string representing the current reserve state
     * @param round    is the current number of round
     * @param turn     is the current number of turn
     */
    public void onYourTurn(boolean yourTurn, String reserve, int round, int turn) {
        // ping response to prove that connection is on
        respondToPing();

        if (reserve != null)
            onReserve(reserve);
        this.myTurn = yourTurn;
        if (myTurn) {
            if (single) {
                printer.println("\nRound: " + round + "\tTurno: " + turn + "\tInserisci un comando:     ~ ['a' per ricevere Aiuto]\n");
            } else {
                printer.println("\nOra è il tuo turno! Round: " + round + "\tTurno: " + turn + "\tInserisci un comando:     ~ ['a' per ricevere Aiuto]\n");
            }
            tool11DiceToBePlaced = false;
            diceValueToBeSet = false;
        } else
            printer.println("\nNon è più il tuo turno! (h for help)");
        printer.flush();
    }

    /**
     * respond to ping in order to prove that connection is ok
     */
    private void respondToPing() {
        if (controllerRmi != null) {
            try {
                controllerRmi.ping(username, single);
            } catch (RemoteException e) {
                closingForDisconnection();
            }
        } else if (controllerSocket != null) {
            controllerSocket.request(new PingRequest(username, single));
        }
    }

    /**
     * Notify used to update this Cli's owner about the reserve state; it is propagated from RmiCli and SocketController
     *
     * @param reserve is the string representing the current reserve state
     */
    public void onReserve(String reserve) {
        String dicesString = reserve.substring(1, reserve.length() - 1);
        dicesList = Pattern.compile(", ")
                .splitAsStream(dicesString)
                .collect(Collectors.toList());
    }

    /**
     * Notify used to update this Cli's owner about the scheme cards proposed to him; it is propagated from RmiCli and SocketController
     *
     * @param schemeCards is the list of strings representing the proposed scheme cards
     */
    public void onWindowChoice(List<String> schemeCards) {
        int i = 0;
        printer.println("\nScegli la tua carta tra le disponibili:                                        ~ [scs] + [numero]\n");
        printer.flush();
        for (String s : schemeCards) {
            printer.println(i++ + ") " + s + "\n");
            printer.flush();
        }
    }

    /**
     * Notify used to update this Cli's owner about the scheme card choice; it is propagated from RmiCli and SocketController
     */
    public void onAfterWindowChoice() {
        printer.println("\nAdesso puoi utilizzare la tua carta schema                              ~ [riserva] per vedere i dadi disponibili\n");
        printer.flush();
    }

    /**
     * Notify used to update this Cli's owner about his scheme card current state; it is propagated from RmiCli and SocketController
     *
     * @param schemeCard is the string representing this Cli's owner scheme card
     */
    public void onMyWindow(String[][] schemeCard) {
        this.mySchemeCard = schemeCard;
    }

    /**
     * Notify used to update this Cli's owner about the turn management; it is propagated from RmiCli and SocketController
     *
     * @param name is the name of the current owner of the turn
     */
    public void onOtherTurn(String name) {
        printer.println("\nOra è il turno di " + name + "!");
        printer.flush();
    }

    /**
     * Notify used to update this Cli's owner about the game initialization; it is propagated from RmiCli and SocketController
     *
     * @param toolcards   is the string representing this tool cards available in the match
     * @param publicCards is the string representing this public cards available in the match
     * @param privateCard is the string representing this this Cli's owner private card in the match
     * @param players     is the list of strings each one representing one opponent
     */
    public void onInitialization(String toolcards, String publicCards, List<String> privateCard, List<String> players) {
        parseToolcards(toolcards);
        parsePublicCards(publicCards);
        this.privateCard = privateCard;
        this.playersNames = players;
    }

    /**
     * Notify used to update this Cli's owner about the exit of an opponent; it is propagated from RmiCli and SocketController
     *
     * @param name is the name of the opponent who has left the game
     */
    public void onPlayerExit(String name) {
        printer.println("\nIl giocatore " + name + " è uscito dal gioco!\n");
        printer.flush();
    }

    /**
     * Notify used to update this Cli's owner about the reconnection of an opponent; it is propagated from RmiCli and SocketController
     *
     * @param name is the name of the opponent who has joined the game again
     */
    public void onPlayerReconnection(String name) {
        printer.println("\nIl giocatore " + name + " è ora in gioco!\n");
        printer.flush();
    }


    /**
     * Notify used to tell this Cli's owner if he becomes the only player in game due to disconnection of other players, in this case he is the winner
     */
    public void onGameClosing() {
        if (stillPlaying) {
            printer.println("\nCongratulazioni! Sei il vincitore. Sei rimasto da solo.\n");
            printer.flush();
        }
        stillPlaying = false;
    }


    /**
     * Notify used to tell this Cli's owner about the state of the match in case of his reconnection; it is propagated from RmiCli and SocketController
     *
     * @param toolcards        are the drawn tool cards
     * @param publicCards      are the drawn public cards
     * @param privateCard      is (or are in single player mode) the drawn private card (cards)
     * @param reserve          is the list of dices available for the current round
     * @param roundTrack       is the list of left dices for all round played till the reconnection
     * @param myTokens         is the number of left tokens for you
     * @param mySchemeCard     is your window pattern card at the current state of the match
     * @param otherTokens      are tokens of other players
     * @param otherSchemeCards are the window pattern cards of the other players
     * @param schemeCardChosen is true if you've already chosen your scheme card
     * @param toolcardsPrices  is the price of tool cards at this state of the match
     */
    public void onAfterReconnection(String toolcards, String publicCards, List<String> privateCard, String reserve, String roundTrack, int myTokens,
                                    String[][] mySchemeCard, Map<String, Integer> otherTokens, Map<String, String[][]> otherSchemeCards,
                                    boolean schemeCardChosen, Map<String, Integer> toolcardsPrices) {
        parseToolcards(toolcards);
        parsePublicCards(publicCards);
        this.privateCard = privateCard;
        String dicesString = reserve.substring(1, reserve.length() - 1);
        this.dicesList = Pattern.compile(", ")
                .splitAsStream(dicesString)
                .collect(Collectors.toList());
        this.roundTrack = roundTrack;
        this.mySchemeCard = mySchemeCard;
        this.myFavorTokens = myTokens;
        this.otherSchemeCardsMap = otherSchemeCards;
        this.otherFavorTokensMap = otherTokens;
        this.windowChosen = schemeCardChosen;
        printer.println("\nAggiornamento prezzi carte utensili:        (se vuoto prezzi=1)");
        for (String toolcard : toolcardsPrices.keySet()) {
            printer.println("-" + toolcard + " " + toolcardsPrices.get(toolcard));
        }
        printer.flush();
    }

    /**
     * Notify used to tell this Cli's owner that the match is finished; it is propagated from RmiCli and SocketController
     *
     * @param winner        is the name of the winner
     * @param rankingNames  is the list of strings representing the names of the opponents
     * @param rankingValues is the list of integers representing the rankings of the opponents
     */
    public void onGameEnd(String winner, List<String> rankingNames, List<Integer> rankingValues) {
        printer.println("\nPunteggio finale:");
        for (int i = 0; i < rankingNames.size(); i++) {
            printer.println("- " + rankingNames.get(i) + "\t" + rankingValues.get(i));
        }
        printer.println();

        if (winner.equals(username)) {
            printer.println("Complimenti! Sei tu il vincitore.");
        } else {
            printer.println(winner.toUpperCase() + " è il vincitore!");
        }
        printer.flush();

        stillPlaying = false;
    }

    /**
     * Notify used to tell this Cli's owner that a tool card has been used for the first time; it is propagated from RmiCli and SocketController
     *
     * @param name           is the name of the player who has used the tool card
     * @param toolCardNumber is the number of the tool card used
     */
    public void onToolCardUsedByOthers(String name, int toolCardNumber) {
        printer.println("\nIl giocatore '" + name + "' è stato il primo ad utilizzare la carta utensile " + toolCardNumber + ", pertanto il suo prezzo di utilizzo diventa di 2 segnalini.");
        printer.flush();
    }

    /**
     * Notify used to tell this Cli's owner that the match is finished; it is propagated from RmiCli and SocketController
     *
     * @param goal   is the value that the player has to achieve to win
     * @param points is the actual ranking of this Cli's owner
     */
    public void onGameEndSingle(int goal, int points) {
        printer.println("\nObiettivo da battere: \t" + goal);
        printer.println("Punteggio ottenuto: \t" + points);

        if (points > goal) {
            printer.println("Complimenti, hai vinto!\nDigita 'esci' per uscire.");
        } else {
            printer.println("Non hai vinto!\nDigita 'esci' per uscire.");
        }

        printer.flush();

        stillPlaying = false;
    }

    /**
     * Notify used to tell this Cli's owner that he has to choose a private card in case of singleplayer match; it is propagated from RmiCli and SocketController
     */
    public void onChoosePrivateCard() {
        enablePrivateCardChoice = true;
        printer.println("\nScegli la carta obiettivo privato da utilizzare per il calcolo del punteggio: digita il comando 'scp' seguito da 'sinistra' o 'destra' per scegliere la carta corrispondente");
        printer.flush();
    }

    private void printNames() {
        printer.println("\nI tuoi avversari sono:\n");
        for (String name : playersNames) {
            if (!name.equals(username)) {
                printer.println("-" + name);
            }
        }
        printer.println();
        printer.flush();
    }

    private void parsePublicCards(String publicCards) {
        String cards = publicCards.substring(1, publicCards.length() - 1);
        publicCardsList = Pattern.compile(", ").splitAsStream(cards).collect(Collectors.toList());
    }

    private void parseToolcards(String toolcards) {
        String cards = toolcards.substring(1, toolcards.length() - 1);
        toolCardsList = Pattern.compile(", ")
                .splitAsStream(cards)
                .collect(Collectors.toList());

        for (String card : toolCardsList) {
            String[] strings = card.split(":");
            int i = Integer.parseInt(strings[0].replaceAll("tool", ""));
            this.toolCommands.add(new ToolCommand(i, this.printer, this.controllerRmi, this.controllerSocket, this.username, this.single));
        }
    }

    private class KeyboardHandler extends Thread {
        String[] parts;
        Integer toolNumber1;
        Integer toolNumber2;
        Integer toolNumber3;
        Integer toolNumber4;
        Integer toolNumber5;
        Integer toolNumber6;
        Integer toolNumber7;
        Integer toolNumber8;
        Integer toolNumber9;
        Integer toolNumber10;
        String toolString1;

        @Override
        public void run() {
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            while (stillPlaying) {

                try {
                    String command = keyboard.readLine();
                    parts = command.split(" +");
                    if (myTurn || single) {
                        switch (parts[0]) {

                            case "aiuto":
                                printGeneralHelp();
                                break;

                            case "avversari":
                                printNames();
                                break;

                            case "cartaschema":
                                showMySchemeCard();
                                break;

                            case "carteschema":
                                showSchemeCards();
                                break;

                            case "esci":
                                quit();
                                break;

                            case "mcs":
                                showWindow();
                                break;

                            case "passa":
                                goThrough();
                                break;

                            case "pd":
                                placeDice();
                                break;

                            case "pd11":
                                placeDice11();
                                break;

                            case "priv":
                                showPrivateCard();
                                break;

                            case "pub":
                                showPublicCards();
                                break;

                            case "regole":
                                printRules();
                                break;

                            case "riserva":
                                showReserve();
                                break;

                            case "sacrifico":
                                sacrifice();
                                break;

                            case "scs":
                                chooseSchemeCard();
                                break;

                            case "sd":
                                chooseDice();
                                break;

                            case "segnalini":
                                showFavorTokens();
                                break;

                            case "segnalinialtrui":
                                showOtherTokens();
                                break;

                            case "tracciato":
                                showRoundTrack();
                                break;

                            case "utensile":
                                howToUseTool();
                                break;

                            case "utensili":
                                showToolCards();

                                break;

                            case "usautensile":
                                useTool();
                                break;

                            case "valore":
                                setDice11Value();
                                break;

                            case "scp":
                                choosePrivateCard();
                                break;

                            default:
                                defaultManagement(command);
                        }
                    } else {
                        switch (parts[0]) {

                            case "aiuto":
                                printSpecificHelp();
                                break;

                            case "avversari":
                                printNames();

                                break;

                            case "cartaschema":
                                showMySchemeCard();

                                break;

                            case "carteschema":
                                showSchemeCards();

                                break;

                            case "esci":
                                quit();

                                break;

                            case "mcs":
                                showWindow();

                                break;

                            case "priv":
                                showPrivateCard();

                                break;

                            case "pub":
                                showPublicCards();

                                break;

                            case "regole":
                                printRules();

                                break;

                            case "riserva":
                                showReserve();

                                break;

                            case "segnalini":
                                showFavorTokens();

                                break;

                            case "segnalinialtrui":
                                showOtherTokens();

                                break;

                            case "utensili":
                                showToolCards();

                                break;

                            case "tracciato":
                                showRoundTrack();
                                break;

                            default: {
                                printer.println("\nATTENZIONE: Comando errato. Digita 'aiuto'!");
                                printer.flush();
                            }
                        }
                    }
                } catch (
                        IOException e) {
                    e.printStackTrace();
                }
            }

            String s = "";
            while (!s.equals("esci")) {
                try {
                    s = keyboard.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                printer.println("\nLa partita è terminata, scrivi 'esci' per uscire");
                printer.flush();
            }
            System.exit(0);
        }

        private void printGeneralHelp() {
            printer.println("\nInserisci un comando valido tra i seguenti:          ('+' sta per SPAZIO)");
            if (single) {
                printer.println(HELP_SINGLE);
            } else {
                printer.println(HELP_IN_TURN_MULTI + HELP_GENERAL_MULTI);
            }
            printer.flush();
        }

        private void printSpecificHelp() {
            printer.println("\nInserisci un comando valido tra i seguenti               ('+' means SPACE)" + HELP_GENERAL_MULTI);
            printer.flush();
        }

        private void showFavorTokens() {
            printer.println("\nAl momento hai " + myFavorTokens + " segnalini.\n");
            printer.flush();
        }

        private void showMySchemeCard() {
            printer.println("\nLa tua carta schema è: \n");
            for (String row[] : mySchemeCard) {
                printer.print("[");
                for (String content : row) {
                    printer.print(content + ",\t");
                }
                printer.print("]");
                printer.println();
            }
            printer.println("");
            printer.flush();
        }

        private void showPrivateCard() {
            printer.println("\nI tuoi obiettivi privati:");
            printer.println(privateCard);
            printer.flush();
        }

        private void showPublicCards() {
            printer.println("\nObiettivi privati:");
            for (String s : publicCardsList) {
                printer.println(s);
            }
            printer.flush();
        }

        private void showToolCards() {
            printer.println("\nDi seguito trovi tutte le carte utensili:          ~ ['utensile' + 'numero' per capire come usare la carta utensile che vuoi utilizzare]\n");
            for (String s : toolCardsList) {
                printer.println("- " + s);
            }
            printer.flush();
        }

        private void goThrough() {
            if (windowChosenCheck(windowChosen) && diceValueToBeSetCheck(diceValueToBeSet) && tool11DiceToBePlacedCheck(tool11DiceToBePlaced) && (!enablePrivateCardChoice)) {
                //RMI
                if (controllerRmi != null) {
                    try {
                        controllerRmi.goThrough(username, single);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                //SOCKET
                else
                    controllerSocket.request(new GoThroughRequest(username, single));
            }
        }

        private void placeDice() {
            if (windowChosenCheck(windowChosen) && diceValueToBeSetCheck(diceValueToBeSet) && tool11DiceToBePlacedCheck(tool11DiceToBePlaced) && (!enablePrivateCardChoice)) {
                if (diceChosen != 9) {
                    if (placeDiceParametersCheck(parts[1], parts[2])) {
                        //RMI
                        if (controllerRmi != null) {
                            try {
                                if (controllerRmi.placeDice(diceChosen, coordinateX, coordinateY, username, single)) {
                                    printer.println("\nBen fatto! Il dado è stato piazzato correttamente!\n");
                                    printer.flush();
                                    diceChosen = 9; //to reset the value
                                } else {
                                    printer.println("\nATTENZIONE: Stai provando a piazzare un dado dove non dovresti, o stai provando a piazzare un altro dado nel turno!");
                                    printer.flush();

                                }
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }

                        //SOCKET
                        else {
                            controllerSocket.request(new PlaceDiceRequest(diceChosen, coordinateX, coordinateY, username, single));
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Thread.currentThread().interrupt();
                            }
                            if (controllerSocket.isDicePlaced()) {
                                controllerSocket.setDicePlaced(false);//to reset the value
                                diceChosen = 9;//to reset the value
                                printer.println("\nBen fatto! il dado è stato piazzato correttamente!\n");
                                printer.flush();
                            } else {
                                printer.println("\nATTENZIONE: Stai provando a piazzare un dado dove non dovresti, o stai provando a piazzare un altro dado nel turno!");
                                printer.flush();
                            }
                        }

                    }
                    toolNumber1 = null;
                    toolNumber2 = null;
                }

            } else {
                printer.println("\nATTENZIONE: Devi scegliere un dado prima di piazzarlo!");
                printer.flush();
            }
        }

        private void placeDice11() {
            if (windowChosenCheck(windowChosen) && !diceValueToBeSet && tool11DiceToBePlaced) {
                if (placeDiceParametersCheck(parts[1], parts[2])) {
                    if (controllerRmi != null) {
                        try {
                            if (controllerRmi.placeDiceTool11(coordinateX, coordinateY, username, single)) {
                                printer.println("\nBen fatto!! Il dado è stato piazzato correttamente!\n");
                                printer.flush();
                            } else {
                                printer.println("\nATTENZIONE! Hai provato a piazzare un dado dove non puoi!, o stai provando mettere un secondo dado nel turno!");
                                printer.flush();
                            }
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                    }

                    //SOCKET
                    else {
                        controllerSocket.request(new PlaceDiceTool11Request(coordinateX, coordinateY, username, single));
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                        if (controllerSocket.isDicePlaced()) {
                            controllerSocket.setDicePlaced(false);//to reset the value
                            printer.println("\nBen fatto! Il dado è stato piazzato correttamente!\n");
                            printer.flush();
                        } else {
                            printer.println("\nATTENZIONE! Hai provato a piazzare un dado dove non puoi, o stai provando mettere un secondo dado nel turno!");
                            printer.flush();
                        }
                    }
                    toolNumber1 = null;
                    toolNumber2 = null;
                }
            }
        }

        private void showWindow() {
            if (parametersCardinalityCheck(2)) {
                if (playersNames.contains(parts[1])) {
                    if (otherSchemeCardsMap.containsKey(parts[1])) {
                        printer.println("\nDi seguito la carta schema del tuo avversario " + parts[1].toUpperCase() + ":");
                        printer.println(Arrays.toString(otherSchemeCardsMap.get(parts[1])));
                        printer.flush();
                    } else {
                        printer.println("\nATTENZIONE:Il giocatore " + parts[1].toUpperCase() + " non è un tuo avversario!");
                        printer.flush();
                    }

                } else {
                    printer.println("\nATTENZIONE: Il giocatore " + parts[1].toUpperCase() + " non esiste!");
                    printer.flush();
                }
            }
        }

        private void showReserve() {
            if (windowChosenCheck(windowChosen)) {
                printer.println("\nDi seguito lo stato corrente della riserva:           ~ ['sd'+ 'numero' per scegliere il dado 'numero)' dall'elenco della riserva]\n");
                printer.flush();
                int i = 0;
                for (String dice : dicesList) {
                    printer.println(i++ + ") " + dice);
                    printer.flush();
                }
                printer.println();
                printer.flush();
            }
        }

        private void sacrifice() {

            if (windowChosenCheck(windowChosen) && single && !diceValueToBeSet && !tool11DiceToBePlaced && (!enablePrivateCardChoice)) {
                if (parametersCardinalityCheck(2)) {
                    toolNumber1 = tryParse(parts[1]);
                    if (toolNumber1 != null) {
                        if (toolNumber1 >= 0 && toolNumber1 < dicesList.size()) {
                            diceChosenToBeSacrificed = toolNumber1;
                            printer.println("\nHai scelto di sacrificare il dado: " + dicesList.toArray()[diceChosenToBeSacrificed].toString() + "\n");
                            printer.flush();
                        } else {
                            printer.println("\nATTENZIONE: Il dado che stai provando a usare non esiste, per favore riprova! ");
                            printer.flush();
                        }
                    } else {
                        syntaxErrorPrint();
                    }
                    toolNumber1 = null;
                }
            }
        }

        private void chooseSchemeCard() {
            if (!windowChosen && !diceValueToBeSet && !tool11DiceToBePlaced) {
                if (parametersCardinalityCheck(2)) {
                    toolNumber1 = tryParse(parts[1]);
                    if (toolNumber1 != null) {
                        if (Integer.parseInt(parts[1]) < 4) {
                            //RMI
                            if (controllerRmi != null) {
                                try {
                                    controllerRmi.chooseWindow(username, toolNumber1, single);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                            //SOCKET
                            else
                                controllerSocket.request(new ChooseWindowRequest(username, toolNumber1, single));
                            printer.println("\nCarta scelta correttamente!");
                            printer.flush();
                            windowChosen = true;
                        } else {
                            printer.println("\nATTENZIONE: La carta schema che stai cercando di scegliere non esiste!");
                            printer.flush();
                        }
                    } else {
                        syntaxErrorPrint();
                    }
                    toolNumber1 = null;
                }
            } else {
                printer.println("\nATTENZIONE: Hai già scelto la tua carta schema!");
                printer.flush();
            }

        }

        private void useTool() {
            if (windowChosenCheck(windowChosen) && !diceValueToBeSet && !tool11DiceToBePlaced && (!enablePrivateCardChoice)) {
                if (parts.length >= 2) {
                    toolNumber1 = tryParse(parts[1]);
                    if (toolNumber1 != null)
                        useRightCommand(toolNumber1);
                    else {
                        syntaxErrorPrint();
                    }
                    toolNumber1 = null;
                } else {
                    syntaxErrorPrint();
                }
            }
        }

        private void setDice11Value() {
            if (windowChosenCheck(windowChosen) && diceValueToBeSet) {

                if (parts.length == 2) {
                    toolNumber1 = tryParse(parts[1]);
                    if (toolNumber1 != null && toolNumber1 > 0 && toolNumber1 < 7) {
                        //RMI
                        if (controllerRmi != null) {
                            try {
                                controllerRmi.setDiceValue(toolNumber1, username, single);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        //SOCKET
                        else
                            controllerSocket.request(new SetDiceValueRequest(toolNumber1, username, single));
                        printer.println("Valore selezionato correttamente! Ora usa il comando 'pd11' seguito dalla posizione in cui vuoi piazzare il dado! \n");
                        printer.flush();
                        diceValueToBeSet = false;
                        tool11DiceToBePlaced = true;
                    } else {
                        syntaxErrorPrint();
                    }
                    toolNumber1 = null;
                } else {
                    syntaxErrorPrint();
                }
            }
        }

        private void defaultManagement(String command) {
            if (!stillPlaying) {

                if (command.equals("esci")) {
                    System.exit(0);
                } else {
                    printer.println("\nLa partita è terminata, scrivi 'esci' per uscire");
                    printer.flush();
                }

            } else {
                printer.println("\nATTENZIONE: Comando errato. Digita 'aiuto'!\n");
                printer.flush();
            }
        }

        private void choosePrivateCard() {
            if (enablePrivateCardChoice && single) {
                if (parts.length == 2) {
                    switch (parts[1]) {
                        case "sinistra":
                            if (controllerRmi != null) {
                                try {
                                    controllerRmi.choosePrivateCard(username, 0); // left position is equal to 0 inside the arrayList that contains private cards
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                controllerSocket.request(new PrivateCardChosenRequest(username, 0));
                            }
                            break;
                        case "destra":
                            if (controllerRmi != null) {
                                try {
                                    controllerRmi.choosePrivateCard(username, 1); // right position is equal to 1 inside the arrayList that contains private cards
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                controllerSocket.request(new PrivateCardChosenRequest(username, 1));
                            }
                            break;
                        default:
                            syntaxErrorPrint();
                            break;
                    }
                } else {
                    syntaxErrorPrint();
                }
            } else {
                if (single) {
                    printer.println("\nNon è ancora il momento di scegliere la carta obiettivo privato!");
                } else {
                    printer.println("\nComando valido solo per le partite a giocatore singolo.");
                }
                printer.flush();
            }
        }

        private void chooseDice() {

            if (windowChosenCheck(windowChosen) && !diceValueToBeSet && !tool11DiceToBePlaced && (!enablePrivateCardChoice)) {
                if (parametersCardinalityCheck(2)) {
                    toolNumber1 = tryParse(parts[1]);
                    if (toolNumber1 != null) {
                        if (toolNumber1 >= 0 && toolNumber1 < dicesList.size()) {
                            diceChosen = toolNumber1;
                            printer.println("\nHai scelto il dado: " + dicesList.toArray()[diceChosen].toString() + "\n");
                            printer.flush();
                        } else {
                            printer.println("\nATTENZIONE: Il dado che stai provando a usare non esiste, per favore riprova!  ");
                            printer.flush();
                        }
                    } else {
                        syntaxErrorPrint();
                    }
                    toolNumber1 = null;
                }
            }
        }

        private void showSchemeCards() {

            for (String name : otherSchemeCardsMap.keySet()) {
                printer.println("\nGiocatore: " + name + "\n");
                for (String row[] : otherSchemeCardsMap.get(name)) {
                    for (String content : row) {
                        printer.print(content + ",\t");
                    }
                    printer.print("]");
                    printer.println();
                }
            }
            printer.println("");
            printer.flush();
        }

        private void showOtherTokens() {
            printer.println(otherFavorTokensMap.toString());
            printer.flush();
        }

        private void printRules() {
            printer.println(RULES);
            printer.flush();
        }

        private void showRoundTrack() {
            printer.println("\nDi seguito il tracciato dei round: (vuoto se primo round) \n" + roundTrack);
            printer.flush();
        }

        private void howToUseTool() {

            boolean found = false;
            if (parametersCardinalityCheck(2)) {
                toolNumber1 = tryParse(parts[1]);
                if (toolNumber1 != null) {
                    for (ToolCommand toolCommand : toolCommands) {
                        if (toolCommand.getI() == Integer.parseInt(parts[1])) {
                            found = true;
                            printer.println("\nDi seguito puoi trovare la descrizione d'uso della carta utensile:\n");
                            printer.println(toolCommand.parametersNeeded);
                            printer.flush();
                        }
                    }
                    if (!found) {
                        printer.println("\nATENZIONE: Carta utensile non presente!");
                        printer.flush();
                    }
                } else {
                    syntaxErrorPrint();
                }
                toolNumber1 = null;
            }
        }

        private void quit() {
            //RMI
            if (controllerRmi != null) {
                try {
                    if (!stillPlaying && !single) {
                        controllerRmi.removeMatch(username);
                    } else {
                        controllerRmi.quitGame(username, single);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
            //SOCKET
            else {
                if (!stillPlaying && !single) {
                    controllerSocket.request(new TerminateMatchRequest(username));
                } else {
                    controllerSocket.request(new QuitGameRequest(username, single));
                }
                System.exit(0);
            }
        }

        private void useRightCommand(int i) {

            boolean found = false;
            for (ToolCommand toolCommand : toolCommands) {
                if (toolCommand.getI() == i) {
                    found = true;
                    switch (i) {
                        case 1:
                            command1(toolCommand);
                            break;

                        case 2:
                            command2(toolCommand);
                            break;

                        case 3:
                            command3(toolCommand);
                            break;

                        case 4:
                            command4(toolCommand);
                            break;

                        case 5:
                            command5(toolCommand);
                            break;

                        case 6:
                            command6(toolCommand);
                            break;

                        case 7:
                            command7(toolCommand);
                            break;
                        case 8:
                            command8(toolCommand);
                            break;
                        case 9:
                            command9(toolCommand);
                            break;

                        case 10:
                            command10(toolCommand);
                            break;

                        case 11:
                            command11(toolCommand);
                            break;

                        case 12:
                            command12(toolCommand);
                            break;
                    }
                }
            }
            if (!found) {
                printer.println("\nATTENZIONE: Carta utensile non presente nella lista delle carte utensili!\n");
                printer.flush();
            }
        }

        private void command1(ToolCommand toolCommand) {
            if (parametersCardinalityCheck(4)) {
                toolNumber1 = tryParse(parts[2]);
                toolString1 = parts[3];
                boolean done = false;
                if (toolNumber1 != null && toolNumber1 >= 0 && toolNumber1 < dicesList.size() && (toolString1.equals("+") || toolString1.equals("-"))) {
                    if (single) {
                        if (diceChosenToBeSacrificed != 9 && diceChosenToBeSacrificed != toolNumber1) {
                            if (toolCommand.command1(diceChosenToBeSacrificed, toolNumber1, toolString1)) {
                                done = true;
                            }
                        } else {
                            printer.println("\nATTENZIONE:Non hai scelto un dado da sacrificare o non lo hai scelto correttamente!\n");
                            printer.flush();
                        }
                    } else {
                        if (toolCommand.command1(-1, toolNumber1, toolString1)) {
                            done = true;
                        } else {
                            gameErrorPrint();
                        }
                    }
                    checkIfItsDone(done);
                } else {
                    syntaxErrorPrint();
                }
                toolNumber1 = null;
                toolString1 = null;
            }
        }

        private void command2(ToolCommand toolCommand) {
            if (parametersCardinalityCheck(6)) {
                toolNumber1 = tryParse(parts[2]);
                toolNumber2 = tryParse(parts[3]);
                toolNumber3 = tryParse(parts[4]);
                toolNumber4 = tryParse(parts[5]);
                boolean done = false;
                if (toolNumber1 != null && toolNumber1 >= 0 && toolNumber1 < 4 && toolNumber2 != null && toolNumber2 >= 0 && toolNumber2 < 5 && toolNumber3 != null && toolNumber3 >= 0 && toolNumber3 < 4 && toolNumber4 != null && toolNumber4 >= 0 && toolNumber4 < 5) {
                    if (single) {
                        if (diceChosenToBeSacrificed != 9) {
                            if (toolCommand.command2or3(diceChosenToBeSacrificed, 2, toolNumber1, toolNumber2, toolNumber3, toolNumber4)) {
                                done = true;
                            }
                        } else {
                            printer.println("\nATTENZIONE:Non hai scelto un dado da sacrificare!\n");
                            printer.flush();
                        }
                    } else {
                        if (toolCommand.command2or3(-1, 2, toolNumber1, toolNumber2, toolNumber3, toolNumber4)) {
                            done = true;
                        } else {
                            gameErrorPrint();
                        }
                    }
                    checkIfItsDone(done);
                } else {
                    syntaxErrorPrint();
                }
                toolNumber1 = null;
                toolNumber2 = null;
                toolNumber3 = null;
                toolNumber4 = null;
            }
        }

        private void command3(ToolCommand toolCommand) {
            if (parametersCardinalityCheck(6)) {
                toolNumber1 = tryParse(parts[2]);
                toolNumber2 = tryParse(parts[3]);
                toolNumber3 = tryParse(parts[4]);
                toolNumber4 = tryParse(parts[5]);
                boolean done = false;
                if (toolNumber1 != null && toolNumber1 >= 0 && toolNumber1 < 4 && toolNumber2 != null && toolNumber2 >= 0 && toolNumber2 < 5 && toolNumber3 != null && toolNumber3 >= 0 && toolNumber3 < 4 && toolNumber4 != null && toolNumber4 >= 0 && toolNumber4 < 5) {
                    if (single) {
                        if (diceChosenToBeSacrificed != 9) {
                            if (toolCommand.command2or3(diceChosenToBeSacrificed, 3, toolNumber1, toolNumber2, toolNumber3, toolNumber4)) {
                                done = true;
                            }
                        } else {
                            printer.println("\nATTENZIONE:Non hai scelto un dado da sacrificare!\n");
                            printer.flush();
                        }
                    } else {
                        if (toolCommand.command2or3(-1, 3, toolNumber1, toolNumber2, toolNumber3, toolNumber4)) {
                            done = true;
                        } else {
                            gameErrorPrint();
                        }
                    }
                    checkIfItsDone(done);
                } else {
                    syntaxErrorPrint();
                }
                toolNumber1 = null;
                toolNumber2 = null;
                toolNumber3 = null;
                toolNumber4 = null;
            }
        }

        private void command4(ToolCommand toolCommand) {
            if (parametersCardinalityCheck(10)) {
                toolNumber1 = tryParse(parts[2]);
                toolNumber2 = tryParse(parts[3]);
                toolNumber3 = tryParse(parts[4]);
                toolNumber4 = tryParse(parts[5]);
                toolNumber5 = tryParse(parts[6]);
                toolNumber6 = tryParse(parts[7]);
                toolNumber7 = tryParse(parts[8]);
                toolNumber8 = tryParse(parts[9]);
                boolean done = false;
                if (toolNumber1 != null && toolNumber1 >= 0 && toolNumber1 < 4 && toolNumber2 != null && toolNumber2 >= 0 && toolNumber2 < 5 && toolNumber3 != null && toolNumber3 >= 0 && toolNumber3 < 4 && toolNumber4 != null && toolNumber4 >= 0 && toolNumber4 < 5 && toolNumber5 != null && toolNumber5 >= 0 && toolNumber5 < 4 && toolNumber6 != null && toolNumber6 >= 0 && toolNumber6 < 5 && toolNumber7 != null && toolNumber7 >= 0 && toolNumber7 < 4 && toolNumber8 != null && toolNumber8 >= 0 && toolNumber8 < 5) {
                    if (single) {
                        if (diceChosenToBeSacrificed != 9) {
                            if (toolCommand.command4(diceChosenToBeSacrificed, toolNumber1, toolNumber2, toolNumber3, toolNumber4, toolNumber5, toolNumber6, toolNumber7, toolNumber8)) {
                                done = true;
                            }
                        } else {
                            printer.println("\nATTENZIONE:Non hai scelto un dado da sacrificare!\n");
                            printer.flush();
                        }
                    } else {
                        if (toolCommand.command4(-1, toolNumber1, toolNumber2, toolNumber3, toolNumber4, toolNumber5, toolNumber6, toolNumber7, toolNumber8)) {
                            done = true;
                        } else {
                            gameErrorPrint();
                        }
                    }
                    checkIfItsDone(done);

                } else {
                    syntaxErrorPrint();
                }
                toolNumber1 = null;
                toolNumber2 = null;
                toolNumber3 = null;
                toolNumber4 = null;
                toolNumber5 = null;
                toolNumber6 = null;
                toolNumber7 = null;
                toolNumber8 = null;
            }
        }

        private void command5(ToolCommand toolCommand) {
            if (parametersCardinalityCheck(5)) {
                toolNumber1 = tryParse(parts[2]);
                toolNumber2 = tryParse(parts[3]);
                toolNumber3 = tryParse(parts[4]);
                boolean done = false;
                if (toolNumber1 != null && toolNumber1 >= 0 && toolNumber1 < dicesList.size() && toolNumber2 != null && toolNumber2 > 0 && toolNumber3 != null && toolNumber3 >= 0) {
                    if (single) {
                        if (diceChosenToBeSacrificed != 9 && diceChosenToBeSacrificed != toolNumber1) {
                            if (toolCommand.command5(diceChosenToBeSacrificed, toolNumber1, toolNumber2, toolNumber3)) {
                                done = true;
                            }
                        } else {
                            printer.println("\nATTENZIONE: Non hai scelto un dado da sacrificare o non lo hai scelto correttamente!\n");
                            printer.flush();
                        }
                    } else {
                        if (toolCommand.command5(-1, toolNumber1, toolNumber2, toolNumber3)) {
                            done = true;
                        } else {
                            gameErrorPrint();
                        }
                    }
                    checkIfItsDone(done);
                } else {
                    syntaxErrorPrint();
                }
                toolNumber1 = null;
                toolNumber2 = null;
                toolNumber3 = null;
            }
        }

        private void command6(ToolCommand toolCommand) {
            if (parametersCardinalityCheck(3)) {
                toolNumber1 = tryParse(parts[2]);
                boolean done = false;
                if (toolNumber1 != null && toolNumber1 >= 0 && toolNumber1 < dicesList.size()) {
                    if (single) {
                        if (diceChosenToBeSacrificed != 9 && diceChosenToBeSacrificed != toolNumber1) {
                            if (toolCommand.command6(diceChosenToBeSacrificed, toolNumber1)) {
                                done = true;
                            }
                        } else {
                            printer.println("\nATTENZIONE:Non hai scelto un dado da sacrificare o non lo hai scelto correttamente!\n");
                            printer.flush();
                        }
                    } else {
                        if (toolCommand.command6(-1, toolNumber1)) {
                            done = true;
                        } else {
                            gameErrorPrint();
                        }
                    }
                    checkIfItsDone(done);
                } else {
                    syntaxErrorPrint();
                }
                toolNumber1 = null;
            }
        }

        private void command7(ToolCommand toolCommand) {
            if (diceChosen == 9) { //dado non ancora scelto
                boolean done = false;
                if (single) {
                    if (diceChosenToBeSacrificed != 9) {
                        if (toolCommand.command7(diceChosenToBeSacrificed)) {
                            done = true;
                        }
                    } else {
                        printer.println("\nATTENZIONE:Non hai scelto un dado da sacrificare!\n");
                        printer.flush();
                    }
                } else {
                    if (toolCommand.command7(-1)) {
                        done = true;
                    } else {
                        gameErrorPrint();
                    }
                }
                checkIfItsDone(done);
            } else {
                printer.println("\nNon puoi scegliere un dado e poi rimescolare la riserva!\n");
                printer.flush();
            }
        }

        private void command8(ToolCommand toolCommand) {
            boolean done = false;
            if (single) {
                if (diceChosenToBeSacrificed != 9) {
                    if (toolCommand.command8(diceChosenToBeSacrificed)) {
                        done = true;
                    }
                } else {
                    printer.println("\nATTENZIONE:Non hai scelto un dado da sacrificare!\n");
                    printer.flush();
                }
            } else {
                if (toolCommand.command8(-1)) {
                    done = true;
                } else {
                    gameErrorPrint();
                }
            }
            checkIfItsDone(done);
        }

        private void command9(ToolCommand toolCommand) {
            if (parametersCardinalityCheck(5)) {
                toolNumber1 = tryParse(parts[2]);
                toolNumber2 = tryParse(parts[3]);
                toolNumber3 = tryParse(parts[4]);
                boolean done = false;
                if (toolNumber1 != null && toolNumber1 >= 0 && toolNumber1 < dicesList.size() && diceChosenToBeSacrificed != toolNumber1 && toolNumber2 != null && toolNumber2 >= 0 && toolNumber2 < 4 && toolNumber3 != null && toolNumber3 >= 0 && toolNumber3 < 5) {
                    if (single) {
                        if (diceChosenToBeSacrificed != 9) {
                            if (toolCommand.command9(diceChosenToBeSacrificed, toolNumber1, toolNumber2, toolNumber3)) {
                                done = true;
                            }
                        } else {
                            printer.println("\nATTENZIONE:Non hai scelto un dado da sacrificareo non lo hai scelto correttamente!\n");
                            printer.flush();
                        }
                    } else {
                        if (toolCommand.command9(-1, toolNumber1, toolNumber2, toolNumber3)) {
                            done = true;
                        } else {
                            gameErrorPrint();
                        }
                    }
                    checkIfItsDone(done);
                } else {
                    syntaxErrorPrint();
                }
                toolNumber1 = null;
                toolNumber2 = null;
                toolNumber3 = null;
            }
        }

        private void command10(ToolCommand toolCommand) {
            if (parametersCardinalityCheck(3)) {
                toolNumber1 = tryParse(parts[2]);
                boolean done = false;
                if (toolNumber1 != null && toolNumber1 >= 0 && toolNumber1 < dicesList.size() && diceChosenToBeSacrificed != toolNumber1) {
                    if (single) {
                        if (diceChosenToBeSacrificed != 9) {
                            if (toolCommand.command10(diceChosenToBeSacrificed, toolNumber1)) {
                                done = true;
                            }
                        } else {
                            printer.println("\nATTENZIONE:Non hai scelto un dado da sacrificare o non lo hai scelto correttamente!\n");
                            printer.flush();
                        }
                    } else {
                        if (toolCommand.command10(-1, toolNumber1)) {
                            done = true;
                        } else {
                            gameErrorPrint();
                        }
                    }
                    checkIfItsDone(done);
                } else {
                    syntaxErrorPrint();
                }
                toolNumber1 = null;
            }
        }

        private void command11(ToolCommand toolCommand) {
            if (parametersCardinalityCheck(3)) {
                toolNumber1 = tryParse(parts[2]);
                boolean done = false;
                Colors color = null;
                if (toolNumber1 != null && toolNumber1 >= 0 && toolNumber1 < dicesList.size() && diceChosenToBeSacrificed != toolNumber1) {
                    if (single) {
                        if (diceChosenToBeSacrificed != 9) {
                            if (toolCommand.command11(diceChosenToBeSacrificed, toolNumber1)) {
                                if (controllerRmi != null) {
                                    try {
                                        color = controllerRmi.askForDiceColor(username, single);
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                }
                                //SOCKET
                                else {
                                    controllerSocket.request(new DiceColorRequest(username, single));
                                    color = controllerSocket.getDiceColor();
                                }
                                diceValueToBeSet = true;
                                done = true;
                            }
                        } else {
                            printer.println("\nATTENZIONE:Non hai scelto un dado da sacrificare o non lo hai scelto correttamente!\n");
                            printer.flush();
                        }
                    } else {
                        if (toolCommand.command11(-1, toolNumber1)) {
                            if (controllerRmi != null) {
                                try {
                                    color = controllerRmi.askForDiceColor(username, single);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                            //SOCKET
                            else {
                                controllerSocket.request(new DiceColorRequest(username, single));
                                color = controllerSocket.getDiceColor();
                            }
                            diceValueToBeSet = true;
                            done = true;
                        } else {
                            gameErrorPrint();
                        }
                    }
                    if (done) {
                        diceChosenToBeSacrificed = 9;
                        printer.println("\nBen fatto! Il dado da te selezionato è stato inserito correttamente nel sacchetto! Ora puoi scegliere il valore del nuovo dado del colore  " + color + " e piazzarlo!\n Per effettuare questa operazione digita il comando 'valore' accompagnato da uno spazio e dal valore che vuoi");
                        printer.flush();
                    }
                }
                toolNumber1 = null;
            }
        }

        private void command12(ToolCommand toolCommand) {
            if (parametersCardinalityCheck(8)) {
                command12A(toolCommand);
            } else if (parametersCardinalityCheck(12)) {
                command12B(toolCommand);
            }
        }

        private void command12A(ToolCommand toolCommand) {
            toolNumber1 = tryParse(parts[2]);
            toolNumber2 = tryParse(parts[3]);
            toolNumber3 = tryParse(parts[4]);
            toolNumber4 = tryParse(parts[5]);
            toolNumber5 = tryParse(parts[6]);
            toolNumber6 = tryParse(parts[7]);
            boolean done = false;
            if (toolNumber1 != null && toolNumber1 > 0 && toolNumber2 != null && toolNumber2 >= 0 && toolNumber3 != null && toolNumber3 >= 0 && toolNumber3 < 4 && toolNumber4 != null && toolNumber4 >= 0 && toolNumber4 < 5 && toolNumber5 != null && toolNumber5 >= 0 && toolNumber5 < 4 && toolNumber6 != null && toolNumber6 >= 0 && toolNumber6 < 5) {
                if (single) {
                    if (diceChosenToBeSacrificed != 9) {
                        if (toolCommand.command12(diceChosenToBeSacrificed, toolNumber1, toolNumber2, toolNumber3, toolNumber4, toolNumber5, toolNumber6, -1, -1, -1, -1)) {
                            done = true;
                        }
                    } else {
                        printer.println("\nATTENZIONE:Non hai scelto un dado da sacrificare o non lo hai scelto correttamente!\n");
                        printer.flush();
                    }
                } else {
                    if (toolCommand.command12(-1, toolNumber1, toolNumber2, toolNumber3, toolNumber4, toolNumber5, toolNumber6, -1, -1, -1, -1)) {
                        done = true;
                    } else {
                        gameErrorPrint();
                    }
                }
                checkIfItsDone(done);
            } else {
                syntaxErrorPrint();
            }
            toolNumber1 = null;
            toolNumber2 = null;
            toolNumber3 = null;
            toolNumber4 = null;
            toolNumber5 = null;
            toolNumber6 = null;
        }

        private void command12B(ToolCommand toolCommand) {
            toolNumber1 = tryParse(parts[2]);
            toolNumber2 = tryParse(parts[3]);
            toolNumber3 = tryParse(parts[4]);
            toolNumber4 = tryParse(parts[5]);
            toolNumber5 = tryParse(parts[6]);
            toolNumber6 = tryParse(parts[7]);
            toolNumber7 = tryParse(parts[8]);
            toolNumber8 = tryParse(parts[9]);
            toolNumber9 = tryParse(parts[10]);
            toolNumber10 = tryParse(parts[11]);
            boolean done = false;
            if (toolNumber1 != null && toolNumber1 > 0 && toolNumber2 != null && toolNumber2 >= 0 && toolNumber3 != null && toolNumber3 >= 0 && toolNumber3 < 4 && toolNumber4 != null && toolNumber4 >= 0 && toolNumber4 < 5 && toolNumber5 != null && toolNumber5 >= 0 && toolNumber5 < 4 && toolNumber6 != null && toolNumber6 >= 0 && toolNumber6 < 5 && toolNumber7 != null && toolNumber7 >= 0 && toolNumber7 < 4 && toolNumber8 != null && toolNumber8 >= 0 && toolNumber8 < 5 && toolNumber9 != null && toolNumber9 >= 0 && toolNumber9 < 4 && toolNumber10 != null && toolNumber10 >= 0 && toolNumber10 < 5) {
                if (single) {
                    if (diceChosenToBeSacrificed != 9) {
                        if (toolCommand.command12(diceChosenToBeSacrificed, toolNumber1, toolNumber2, toolNumber3, toolNumber4, toolNumber5, toolNumber6, toolNumber7, toolNumber8, toolNumber9, toolNumber10)) {
                            done = true;
                        }
                    } else {
                        printer.println("\nATTENZIONE:Non hai scelto un dado da sacrificare o non lo hai scelto correttamente!\n");
                        printer.flush();
                    }
                } else {
                    if (toolCommand.command12(-1, toolNumber1, toolNumber2, toolNumber3, toolNumber4, toolNumber5, toolNumber6, toolNumber7, toolNumber8, toolNumber9, toolNumber10)) {
                        done = true;
                    } else {
                        gameErrorPrint();
                    }
                }
                checkIfItsDone(done);

            } else {
                syntaxErrorPrint();
            }
            toolNumber1 = null;
            toolNumber2 = null;
            toolNumber3 = null;
            toolNumber4 = null;
            toolNumber5 = null;
            toolNumber6 = null;
            toolNumber7 = null;
            toolNumber8 = null;
            toolNumber9 = null;
            toolNumber10 = null;

        }

        private boolean windowChosenCheck(boolean windowChosen) {
            if (windowChosen)
                return true;
            else {
                printer.println("\nATTENZIONE: devi scegliere la tua carta schema prima di fare altre mosse!");
                printer.flush();
                return false;
            }
        }

        private boolean tool11DiceToBePlacedCheck(boolean tool11DiceToBePlaced) {
            if (!tool11DiceToBePlaced)
                return true;
            else {
                printer.println("\nATTENZIONE: devi piazzare il dado di cui hai appena settato il valore prima di fare altre mosse! Usa il comando 'pd11' seguito dalle coordinate.\n");
                printer.flush();
                return false;
            }
        }

        private boolean diceValueToBeSetCheck(boolean diceValueToBeSet) {
            if (!diceValueToBeSet)
                return true;
            else {
                printer.println("\nATTENZIONE: devi scegliere il valore del dado che hai ricevuto in seguito all'utilizzo dalla toolcard 11! Usa il comando 'valore' seguito dal numero che vuoi settare.\n");
                printer.flush();
                return false;
            }
        }

        private Integer tryParse(String text) {
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        private boolean parametersCardinalityCheck(int n) {
            if (parts.length == n)
                return true;
            else {
                syntaxErrorPrint();
                return false;
            }
        }

        private void checkIfItsDone(boolean done) {
            if (done) {
                diceChosenToBeSacrificed = 9;
                printer.println("\nBen fatto! Il dado da te selezionato è stato modificato correttamente!\n");
                printer.flush();
            }
        }

        private void syntaxErrorPrint() {
            printer.println(SYNTAX_ERROR);
            printer.flush();
        }

        private boolean placeDiceParametersCheck(String parameter1, String parameter2) {
            if (parametersCardinalityCheck(3)) {
                toolNumber1 = tryParse(parameter1);
                toolNumber2 = tryParse(parameter2);
                if (toolNumber1 != null && toolNumber2 != null) {
                    if (toolNumber1 >= 0 && toolNumber1 < 4 && toolNumber2 >= 0 && toolNumber2 < 5) {
                        coordinateX = toolNumber1;
                        coordinateY = toolNumber2;
                        printer.println("\nHai scelto di posizionare il dado nella posizione [" + coordinateX + "][" + coordinateY + "] della tua carta schema");
                        printer.flush();
                        return true;
                    } else {
                        printer.println("\nATTENZIONE: Le coordinate da te inserite sono fuori dai limiti!");
                        printer.flush();
                        return false;
                    }
                } else {
                    syntaxErrorPrint();
                    return false;
                }
            } else {
                return false;
            }
        }

        private void gameErrorPrint() {
            printer.println(GAME_ERROR);
            printer.flush();
        }
    }

    /**
     * close the client if a in connection with server occurs
     */
    private void closingForDisconnection() {
        if (single) {
            printer.println("La tua connessione è caduta. Se vuoi continuare a giocare inizia una nuova partita.");
        } else {
            printer.println("La tua connessione è caduta, esegui nuovamente il login con lo stesso username per rientrare in partita.");
        }
        printer.flush();
        System.exit(0);
    }
}