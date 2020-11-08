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
    private ArrayList<Player> players;
    private int whoseTurn;
    private int cardsToDraw;


    //constructor
    public EKGameState() {
        this.discardPile = new ArrayList<Card>();
        this.deck = new ArrayList<Card>();
        this.players = new ArrayList<Player>();
        this.whoseTurn = 1;
        this.cardsToDraw = 1;
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
        this.players = new ArrayList<Player>();
        this.whoseTurn = 1;
        this.cardsToDraw = 1;

        //deep copy of the gamestate discardPile
        for (int a = 0; a < gamestate.getDiscardPile().size(); a++) {
            this.discardPile.add(gamestate.getDiscardPile().get(a));
        }
        //deep copy of the gamestate deck
        for (int b = 0; b < gamestate.getDeck().size(); b++) {
            this.deck.add(gamestate.getDeck().get(b));
        }
        //deep copy of the players array list
        for (int c = 0; c < gamestate.getPlayers().size(); c++) {
            this.players.add(gamestate.getPlayers().get(c));
        }
        //copy of whose turn it is
        this.whoseTurn = gamestate.getWhoseTurn();
    }



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

   //Getters and Setters
   public void addPlayer(Player p) {
       players.add(p);
   }

    public ArrayList<Player> getPlayers(){
        return this.players;
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


}


