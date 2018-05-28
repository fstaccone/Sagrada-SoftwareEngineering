package it.polimi.ingsw;

public abstract class Cli {

    /**
     *  il problema è la doppia ereditarietà della classe RmiCli, potrebbe essere risolto con la export...
     *
     *  si possono ereditare quasi tutti i metodi onQualcosa che non fanno chiamate al modello
     */



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
            "\nWelcome to this fantastic game, ";

    private static final String HELP_IN_TURN = (
            "\n 'cd' + 'number'                             to choose the dice from the Reserve" +
                    "\n 'cw' + 'number'                             to choose tour window pattern card (available once only, at the beginning of the match)" +
                    "\n 'pd' + 'coordinate x' + 'coordinate y'      to place the chosen dice in your Scheme Card " +
                    "\n 'pass'                                      to pass the turn to the next player " +
                    "\n 'reserve'                                   to show current state of reserve, (available only from the beginning of the first turn)" +
                    "\n 'usetool' + 'number'                        to use the effect of the tool card [number]");

    private static final String HELP_GENERAL = (
            "\n 'h'                                         to show game available commands" +
                    "\n 'q'                                         to quit the game" +
                    "\n 'r'                                         to show game rules" +
                    "\n 'sp'                                        to show all opponents' names" +
                    "\n 'track'                                     to show current state of the round track" +
                    "\n 'private'                                   to show your private objective card" +
                    "\n 'public'                                    to show public objective cards" +
                    "\n 'sw' + 'name'                               to show the window pattern card of player [name]" +
                    "\n 'tool' + 'number'                           to show the description of the tool card [number] " +
                    "\n 'toolcards'                                 to show the list of available tool cards \n");

    private static final String SYNTAX_ERROR = (
            "\nWARNING: Invalid syntax request.\n");

    private static final String GAME_ERROR = (
            "\nWARNING: Invalid game request. You did not respect the tool card's rules!\n"); //STAMPATO ANCHE SE PROVI A MODIFICARE DADO NELLA RISERVA OUT OF BOUND , si può migliorare


    public abstract void launch();
}
