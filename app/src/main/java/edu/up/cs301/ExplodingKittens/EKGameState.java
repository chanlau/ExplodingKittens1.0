/**
 * Date: 10/20/2020
 * Authors: Chandler Lau, Ka'ulu Ng, Samuel Warrick
 * Version: Project #d Final
 */

package edu.up.cs301.ExplodingKittens;

import java.util.ArrayList;
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
    //private ArrayList<Player> players;
    private int whoseTurn;
    private int cardsToDraw;
    private ArrayList<ArrayList<Card>> playerHands;
    private ArrayList<Card> player1Hand;
    private ArrayList<Card> player2Hand;
    private ArrayList<Card> player3Hand;
    private ArrayList<Card> player4Hand;


    //constructor
    public EKGameState() {
        this.discardPile = new ArrayList<Card>();
        this.deck = new ArrayList<Card>();
        //this.players = new ArrayList<Player>();
        this.whoseTurn = 1;
        this.cardsToDraw = 1;
        this.playerHands.add(player1Hand);
        this.playerHands.add(player2Hand);
        this.playerHands.add(player3Hand);
        this.playerHands.add(player4Hand);
        //populateDeck();
        //populateHands();
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
        this.whoseTurn = gamestate.whoseTurn;
        this.cardsToDraw = gamestate.cardsToDraw;

        //deep copy of the gamestate discardPile
        for (int a = 0; a < gamestate.getDiscardPile().size(); a++) {
            //create a copy of the given card from the discard pile
            Card newCard =
                    new Card(gamestate.getDiscardPile().get(a).getCardType());
            //add that new copy to the current discard pile
            discardPile.add(newCard);
        }
        //deep copy of the gamestate deck
        for (int b = 0; b < gamestate.getDeck().size(); b++) {
            //create a copy of the given card from the deck
            Card newCard2 = new Card(gamestate.getDiscardPile().get(b).getCardType());
            //add that new copy to the current deck
            deck.add(newCard2);
        }
        //deep copy of the playerHands array list
        for(int c = 0; c < 4; c++){

        }

        this.playerHands = gamestate.playerHands;

        //copy of whose turn it is
        this.whoseTurn = gamestate.getWhoseTurn();
    }


/**
    //to string class
    //@Override
    public String ToString(){
        String discardString = getDiscardPile().toString();
        String deckString = Integer.toString(getDeck().size());
        String turnString = Integer.toString(getWhoseTurn());
        String cardsToDrawString = Integer.toString(cardsToDraw);
        String PlayerString = getPlayers().toString();
        String Player0String = getPlayers().get(0).getPlayerHand().toString();
        String Player1String = getPlayers().get(1).getPlayerHand().toString();
        String Player2String = getPlayers().get(2).getPlayerHand().toString();
        String Player3String = getPlayers().get(3).getPlayerHand().toString();
        return ("Discard Pile: " + discardString + "\n Cards in Deck: " + deckString + "\n Turn: " + turnString
                + "\n Cards to Draw Counter" + cardsToDrawString + "\n Players:" + Player0String + "\n Player 1 Hand:" +
                Player0String + "\n Player 2 Hand: " + Player1String + "\n Player 3 Hand" + Player2String +
                "\n Player 4 Hand: " + Player3String);
    }
 */


    //restart the deck
    public void populateDeck() {
        int i;
        int j;
        //puts 4 of each cat card, attack, shuffle, favor, skip cards
        for (i = 1; i <= 9; i++) {
            for (j = 0; j < 4; j++) {
                this.deck.add(new Card(i));
            }
        }
        // puts 5 See the Future and Nope Cards into deck
        for (i = 10; i <= 11; i++) {
            for (j = 0; j < 5; j++) {
                this.deck.add(new Card(i));
            }
        }

    }

    //populates player's hands with cards from deck
    public void populateHands() {
        int i, j;
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 7; j++) {
                this.playerHands.get(i).add(deck.get(0));
                this.deck.remove(0);

            }
            this.playerHands.get(i).add(new Card(12));
        }
    }

   //Getters and Setters

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

}


