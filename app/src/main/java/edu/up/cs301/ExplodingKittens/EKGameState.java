/**
 * Date: 10/20/2020
 * Authors: Chandler Lau, Ka'ulu Ng, Samuel Warrick
 * Version: Project #d Final
 */

package edu.up.cs301.ExplodingKittens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import edu.up.cs301.game.GameFramework.infoMessage.GameState;


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


    public EKGameState() {
        this.discardPile = new ArrayList<Card>();
        this.deck = new ArrayList<Card>();
        this.whoseTurn = 0;
        this.cardsToDraw = 1;
        this.playerHands = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            this.playerHands.add(new ArrayList<Card>());
        }
    }

    //constructor
    public EKGameState(int numOfPlayers) {
        this.discardPile = new ArrayList<Card>();
        this.deck = new ArrayList<Card>();
        this.playerHands = new ArrayList<ArrayList<Card>>();
        for(int i = 0; i < numOfPlayers; i++){
            this.playerHands.add(new ArrayList<Card>());
        }
        this.whoseTurn = 0;
        this.cardsToDraw = 1;
        this.numPlayers = numOfPlayers;
        deck.add(0, new Card(9));
        populateDeck();
        populateHands();
        discardPile.add(new Card(1));
        discardPile.add(new Card(12));
    }

    //constructor to copy the given gamestate
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
                Card tempCard = new Card(gamestate.getTargetPlayerHand(c).get(d));
                this.playerHands.get(c).add(tempCard);
            }
        }

    }



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
    }

    public ArrayList<ArrayList<Card>> getPlayerHands(){
        return this.playerHands;
    }

    public ArrayList<Card> getCurrentPlayerHand() {return this.playerHands.get(this.whoseTurn);}

    public ArrayList<Card> getTargetPlayerHand(int targetPlayerIdx){
        if(playerHands.size() < targetPlayerIdx){
            return null;
        }
        return playerHands.get(targetPlayerIdx);
    }

    public ArrayList<Card> getDiscardPile(){
        return this.discardPile;
    }

    public ArrayList<Card> getDeck(){
        return this.deck;
    }

    public int getWhoseTurn(){
        return this.whoseTurn;
    }

    public void setWhoseTurn(int i){
        this.whoseTurn = i;
    }

    public int getCardsToDraw() {return this.cardsToDraw;}

    public void setCardsToDraw(int i){ this.cardsToDraw = i;}

    public ArrayList<Card> getPlayerHand(int playerID){
        return this.playerHands.get(playerID);
    }

    public int getNumPlayers(){
        return this.numPlayers;
    }

    public void populateDeck() {
        int i;
        int j;
        //puts 4 of each cat card, attack, shuffle, favor, skip cards
        for (i = 1; i <= 9; i++) {
            for (j = 0; j < 4; j++) {
                this.getDeck().add(new Card(i));
            }
        }
        // puts 5 See the Future and Nope Cards into deck
        for (i = 10; i <= 11; i++) {
            for (j = 0; j < 5; j++) {
                this.getDeck().add(new Card(i));
            }
        }
        //Collections.shuffle(this.deck);
    }

    //adds appropriate amt. of cards to all players hands
    public void populateHands() {
        int i, j;
        for (i = 0; i < this.getNumPlayers(); i++) {
            for (j = 0; j < 7; j++) {
                this.playerHands.get(i).add(this.getDeck().get(0));
                this.deck.remove(0);
            }
            this.playerHands.get(i).add(new Card(12));
        }
    }

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

    }

    public void setNumPlayers(int val){
        this.numPlayers = val;
    }

}


