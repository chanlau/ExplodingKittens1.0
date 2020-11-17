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
 * @version November 2020
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
        populateDeck();
        populateHands();
        populateDefuseExplode();
    }//EKGameState

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
        this.numPlayers = gamestate.numPlayers;
        //copy of whose turn it is
        this.whoseTurn = gamestate.getWhoseTurn();
        //Copy of # of cards to draw
        this.cardsToDraw = gamestate.getCardsToDraw();

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

    }//EKGameState(copy constructor)



    //to string class
    //@Override
    public String ToString(){
        String discardString = getDiscardPile().toString();
        String deckString = Integer.toString(getDeck().size());
        String turnString = Integer.toString(getWhoseTurn());
        String cardsToDrawString = Integer.toString(cardsToDraw);
        /*
        String PlayerString = getPlayers().toString();
        String Player0String = getPlayers().get(0).getPlayerHand().toString();
        String Player1String = getPlayers().get(1).getPlayerHand().toString();
        String Player2String = getPlayers().get(2).getPlayerHand().toString();
        String Player3String = getPlayers().get(3).getPlayerHand().toString();
        return ("Discard Pile: " + discardString + "\n Cards in Deck: " + deckString + "\n Turn: " + turnString
                + "\n Cards to Draw Counter" + cardsToDrawString + "\n Players:" + Player0String + "\n Player 1 Hand:" +
                Player0String + "\n Player 2 Hand: " + Player1String + "\n Player 3 Hand" + Player2String +
                "\n Player 4 Hand: " + Player3String);
        */
        return "HI this is just a place holder";
    }//ToString

    //getter method to return ArrayList of PlayerHands(Arraylists)
    public ArrayList<ArrayList<Card>> getPlayerHands(){
        return this.playerHands;
    }//getPlayerHands()

    //getter method to return current player's hand
    public ArrayList<Card> getCurrentPlayerHand() {return this.playerHands.get(this.whoseTurn);}

    //getter method to return current discard pile
    public ArrayList<Card> getDiscardPile(){
        return this.discardPile;
    }//getDiscardPile

    //getter method to return current deck
    public ArrayList<Card> getDeck(){
        return this.deck;
    }//getDeck

    //getter method to return current turn
    public int getWhoseTurn(){
        return this.whoseTurn;
    }//getWhoseTurn

    //setter method to set current turn
    public void setWhoseTurn(int i){
        this.whoseTurn = i;
    }//setWhoseTurn

    //getterMethod to get amount of draws required this turn
    public int getCardsToDraw() {return this.cardsToDraw;}

    //setter method to set amount of cards to draw this turn
    public void setCardsToDraw(int i){ this.cardsToDraw = i;}

    //getter method to get specified players hand
    public ArrayList<Card> getPlayerHand(int playerID){
        return this.playerHands.get(playerID);
    }//getPlayerHand

    //getter method to get the amount of players
    public int getNumPlayers(){
        return this.numPlayers;
    }//getNumPlayers

    //Populates the deck with various cards in the game
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
        }
        //shuffles the deck
        Collections.shuffle(this.deck);
    }//populateDeck

    //adds 7 cards to all players hands and a Defuse Card
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

    //adds defuse and explode cards to deck
    public void populateDefuseExplode() {
        int i;
        int j;
        //puts 3 Exploding Kittens into deck
        for (i = 0; i < 3; i++) {
            this.deck.add(new Card(0));
        }

        //Puts 2 defuse cards into deck
        for (i = 0; i < 2; i++) {
            this.deck.add(new Card(12));
        }
        //shuffles them into the deck
        Collections.shuffle(this.deck);
    }//populateDefuseExplode

    //sets all players hands to be able to do each action once
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

    //Checks to see if a player has lost the game
    //returns true if they have lost the game
    //returns false if they haven't
    public boolean hasPlayerLost(int index){
        if(this.playerHands.get(index).get(0).getCardType() == 0){
            return true;
        }
        return false;
    }//hasPlayerLost

    public void addToPlayerLog(String addString){
        this.playerLog.add(addString);
    }//addToPlayerLog

    public void clearPlayerLog(boolean endTurnPressed){
        if(endTurnPressed){
            this.playerLog.clear();
        }
    }//clearPlayerLog

    //setter method to set the amount of players in game
    public void setNumPlayers(int val){
        this.numPlayers = val;
    }

}


