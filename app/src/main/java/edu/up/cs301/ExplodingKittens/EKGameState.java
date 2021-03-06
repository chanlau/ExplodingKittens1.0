package edu.up.cs301.ExplodingKittens;

import java.security.CryptoPrimitive;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import edu.up.cs301.game.GameFramework.infoMessage.GameState;

/**
 *An Exploding Kittens Game State Class
 * Holds all the information about the current game (ex: whose turn, player
 * hand, etc.)
 * Sent to players to inform them about the current state of the game
 *
 * @author Samuel Warrick
 * @author Kaulu Ng
 * @author Chandler Lau
 * @version December 2020
 */

/**
 * Constructor for EKGameState
 * Initializes all instance variables
 */
public class EKGameState extends GameState {
    /**
     * External Citation
     * Date: 20 October 2020
     * Problem: General knowledge about array lists, used in several places
     * in the code but would be
     * redundant so just listed here once
     * <p>
     * Resources:
     * https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
     * https://www.w3schools.com/java/java_arraylist.asp
     * Solution: Utilized both sources when performing actions related to
     * array lists
     */
    //instance variables
    private ArrayList<Card> discardPile;
    private ArrayList<Card> deck;
    private ArrayList<ArrayList<Card>> playerHands;
    private int whoseTurn;
    private int cardsToDraw;
    private int numPlayers;
    private ArrayList<String> playerLog;

    //trying to set up nope
    private ArrayList<Integer> actionsPerformed;
    private ArrayList<Integer> whoPerformed;
    private boolean humanDefusing;


    //initial EKGameState constructor
    public EKGameState(int numOfPlayers) {
        this.discardPile = new ArrayList<Card>();
        this.deck = new ArrayList<Card>();
        this.playerHands = new ArrayList<ArrayList<Card>>();
        this.playerLog = new ArrayList<String>();
        for(int i = 0; i < numOfPlayers; i++){
            this.playerHands.add(new ArrayList<Card>());
        }
        this.whoseTurn = 0;
        this.cardsToDraw = 1;
        this.numPlayers = numOfPlayers;
        this.actionsPerformed = new ArrayList<Integer>();
        this.whoPerformed = new ArrayList<Integer>();
        this.humanDefusing = false;
        populateDeck();
        populateHands();
        populateDefuseExplode();
    }//EKGameState

    public void setHumanDefuse(boolean bool){
        this.humanDefusing = bool;
    }

    public boolean getHumanDefuse(){
        return this.humanDefusing;
    }

    //copy constructor to copy the given gamestate
    public EKGameState(EKGameState gamestate) {
        /**
         * External Citation
         * Date: 8 October 2020
         * Problem: Was not certain on the easiest way to copy an array
         * <p>
         * Resource:
         * https://www.geeksforgeeks.org/array-copy-in-java/
         * Solution: Reaffirmed that the best way is to use a for loop
         */

        this.discardPile = new ArrayList<Card>();
        this.deck = new ArrayList<Card>();
        this.playerHands = new ArrayList<ArrayList<Card>>();
        this.playerLog = new ArrayList<String>();
        this.numPlayers = gamestate.getNumPlayers();
        //copy of whose turn it is
        this.whoseTurn = gamestate.getWhoseTurn();
        //Copy of # of cards to draw
        this.cardsToDraw = gamestate.getCardsToDraw();
        this.actionsPerformed = new ArrayList<Integer>();
        this.whoPerformed = new ArrayList<Integer>();

        //deep copy of the gamestate discardPile
        for (int a = 0; a < gamestate.getDiscardPile().size(); a++) {
            //create a copy of the given card from the discard pile
            Card newCard =
                    new Card(gamestate.getDiscardPile().get(a).getCardType());
            //add that new copy to the current discard pile
            this.discardPile.add(newCard);
        }
        //deep copy of the gamestate deck
        for (int b = 0; b < gamestate.getDeck().size(); b++) {
            //create a copy of the given card from the deck
            Card newCard2 =
                    new Card(gamestate.getDeck().get(b).getCardType());
            //add that new copy to the current deck
            this.deck.add(newCard2);
        }

        //deep copy for array of player hands
        for(int c = 0; c < gamestate.numPlayers; c++){
            ArrayList<Card> temp = new ArrayList<Card>();
            this.playerHands.add(temp);
            for (int d = 0; d < gamestate.playerHands.get(c).size(); d++){
                Card tempCard = new Card(gamestate.getPlayerHand(c).get(d));
                this.playerHands.get(c).add(tempCard);
            }
        }

        //deep copy for player log arraylist
        for(int e = 0; e < gamestate.getPlayerLog().size(); e++){
            this.playerLog.add(gamestate.getPlayerLog().get(e));
        }

        //deep copy for actions performed
        for(int f = 0; f < gamestate.getActionsPerformed().size(); f++){
            this.actionsPerformed.add(gamestate.getActionsPerformed().get(f));
        }
        //deep copy for who performed
        for(int g = 0; g < gamestate.getActionsPerformed().size(); g++){
            this.whoPerformed.add(gamestate.getWhoPerformed().get(g));
        }
    }//EKGameState(copy constructor)

    //getter method to return Arraylist of player actions

    public ArrayList<String> getPlayerLog() {
        return playerLog;
    }

    /**
     * getter method to return ArrayList of PlayerHands(ArrayLists)
     * @return
     *      returns an ArrayList of ArrayLists of cards for the players hands
     */
    public ArrayList<ArrayList<Card>> getPlayerHands(){
        return this.playerHands;
    }//getPlayerHands()

    /**
     * getter method to return current player's hand
     * @return
     *      returns an ArrayList of cards for the current player's hand
     */
    public ArrayList<Card> getCurrentPlayerHand() {return this.playerHands.get(this.whoseTurn);}

    /**
     * getter method to return current discard pile
     * @return
     *      returns an ArrayList of cards or the discard pile
     */
    public ArrayList<Card> getDiscardPile(){
        return this.discardPile;
    }//getDiscardPile

    /**
     * getter method to return current deck
     * @return
     *      returns an ArrayList of cards for the current deck
     */
    public ArrayList<Card> getDeck(){
        return this.deck;
    }//getDeck

    /**
     * getter method to return current turn
     * @return
     *      returns an int with the current player's turn
     */
    public int getWhoseTurn(){
        return this.whoseTurn;
    }//getWhoseTurn

    /**
     * setter method to set current turn
     * @param i
     *      int for the current player's turn
     */
    public void setWhoseTurn(int i){
        this.whoseTurn = i;
    }//setWhoseTurn

    /**
     * getterMethod to get amount of draws required this turn
     * @return
     *      returns an int for the number of cards the player has to draw
     */
    public int getCardsToDraw() {return this.cardsToDraw;}

    /**
     * setter method to set amount of cards to draw this turn
     * @param i
     *      int for the number of cards to draw
     */
    public void setCardsToDraw(int i){ this.cardsToDraw = i;}

    /**
     * getter method to get specified players hand
     * @param playerID
     *      int for the current player
     * @return
     *      returns an ArrayList of cards for the desired player's hand
     */
    public ArrayList<Card> getPlayerHand(int playerID){
        return this.playerHands.get(playerID);
    }//getPlayerHand

    /**
     * getter method to get the amount of players
     * @return
     *      returns the amount of players in the game
     */
    public int getNumPlayers(){
        return this.numPlayers;
    }//getNumPlayers

    /**
     * populates the deck with various cards
     */
    public void populateDeck() {
        int i;
        int j;
        //puts 4 of each cat card, attack, shuffle, favor, skip cards
        for (i = 1; i <= 9; i++) {
            for (j = 0; j < 4; j++) {
                this.getDeck().add(new Card(i));
            }
        }
        // puts 5 See the Future Cards into the deck
        for (j = 0; j < 5; j++) {
            this.getDeck().add(new Card(10));
            this.getDeck().add(new Card(11));
        }
        //shuffles the deck
        Collections.shuffle(this.deck);
    }//populateDeck

    /**
     * adds 7 cards from the deck to each player's hand and adds a defuse
     * card as well
     */
    public void populateHands() {
        int i, j;
        for (i = 0; i < this.getNumPlayers(); i++) {
            for (j = 0; j < 7; j++) {
                this.playerHands.get(i).add(this.getDeck().get(0));
                this.deck.remove(0);
            }
            this.playerHands.get(i).add(new Card(12));
        }
    }//populateHands

    /**
     * adds defuse cards and exploding kitten cards to the deck
     */
    public void populateDefuseExplode() {
        int i;
        int j;
        //puts 3 Exploding Kittens into deck
        for (i = 0; i < (this.numPlayers-1); i++) {
            this.deck.add(new Card(0));
        }

        //Puts 2 defuse cards into deck
        for (i = 0; i < (6-this.numPlayers); i++) {
            this.deck.add(new Card(12));
        }
        //shuffles them into the deck
        Collections.shuffle(this.deck);
    }//populateDefuseExplode

    /**
     * used for testing, populates each player's hand with a variation of
     * cards that allows each player to do the trade actions once
     */
    public void makeTestHand() {
        int i, j;
        for (i = 0; i < this.getNumPlayers(); i++) {
            //puts 3 tacocats in hand
            for (j = 0; j < 3; j++) {
                this.getPlayerHand(i).add(new Card(1));
            }
            //puts 2 beardcats in hand
            for (j = 0; j < 2; j++) {
                this.getPlayerHand(i).add(new Card(2));
            }
            //puts one of every card in hand
            for (j = 1; j <= 12; j++) {
                this.getPlayerHand(i).add(new Card(j));
            }
        }

    }//makeTestHand

    /**
     * checks to see if a player has lost the game
     * @param index
     *      int of the players hand to check
     * @return
     *      returns true if the player has lost and false if not
     */
    public boolean hasPlayerLost(int index){
        //Edge case of the hand of a player completely
        //running out.
        if(this.playerHands.get(index) == null || this.playerHands.get(index).size() == 0){
            return false;
        }
        return this.playerHands.get(index).get(0).getCardType() == 0;

    }//hasPlayerLost

    /**
     * method adds a string to the player actions log to be displayed
     * @param addString
     *      string to add to the player log
     */
    public void addToPlayerLog(String addString){
        this.playerLog.add(addString);
    }//addToPlayerLog

    /**
     * clears the player log
     * @param confirmClearLog
     *      true if the player log should be cleared and false if not
     */
    public void clearPlayerLog(boolean confirmClearLog){
        if(confirmClearLog){
            this.playerLog.clear();
        }
    }//clearPlayerLog

    /**
     * setter method to set the number of player's in a game
     * @param val
     */
    public void setNumPlayers(int val){
        this.numPlayers = val;
    }

    /**
     * getter method to get the amount of exploding kittens in the deck
     * @return
     */
    public int getEKCount(){
        int count = 0;
        for(int i = 0; i < this.getDeck().size(); i++){
            if(this.getDeck().get(i).getCardType() == 0){
                count++;
            }
        }
        return count;
    }

    /**
     * getter method for actions that have happened
     * @return
     *      returns an ArrayList of integers of what actions were taken
     */
    public ArrayList<Integer> getActionsPerformed(){
        return this.actionsPerformed;
    }

    /**
     * getter method for who performed actions
     * @return
     *      returns and ArrayList of integers for who performed
     */
    public ArrayList<Integer> getWhoPerformed(){
        return this.whoPerformed;
    }

    /**
     * adder method to add an action to the actionsPerformed ArrayList
     * @param action
     *      int for the action that was performed
     */
    public void addActionsPerformed(int action){
        this.actionsPerformed.add(action);
    }


    /**
     * adder method to add the player that performed an action to the
     * whoPerformed ArrayList
     */
    public void addWhoPerformed(){
        this.whoPerformed.add(this.getWhoseTurn());
    }

}
