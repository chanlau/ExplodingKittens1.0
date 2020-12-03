
package edu.up.cs301.ExplodingKittens;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import edu.up.cs301.ExplodingKittens.EKActions.DrawCardAction;
import edu.up.cs301.ExplodingKittens.EKActions.PlayAttackCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayDefuseCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayFavorCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayFutureCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayNopeCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayShuffleCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlaySkipCard;
import edu.up.cs301.ExplodingKittens.EKActions.Trade2Action;
import edu.up.cs301.ExplodingKittens.EKActions.Trade3Action;
import edu.up.cs301.ExplodingKittens.EKActions.Trade5Action;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;
import edu.up.cs301.game.R;

/**
 *An Exploding Kittens Local Game class
 * Holds majority of the logic of different game actions and cards.
 * Modifies the gamestates of the game
 *
 * @author Samuel Warrick
 * @author Kaulu Ng
 * @author Chandler Lau
 * @version November 2020
 */

public class EKLocalGame extends LocalGame{

    //Instance variable representing the current state
    private EKGameState currState;
    private int deckIndexChoice = 0;


    //constructor
    public EKLocalGame(int numOfPlayers) {
        this.currState = new EKGameState(numOfPlayers);
    }


    //send updated state to a given player
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {

        EKGameState gameCopy = new EKGameState(currState);
        p.sendInfo(gameCopy);
    }//sendUpdatedState

    //checks if player can move
    @Override
    protected boolean canMove(int playerIdx) {
        return playerIdx == currState.getWhoseTurn();
    }//canMove

    //checks if game is over
    @Override
    protected String checkIfGameOver() {
        //See how many players have lost the game
        int playersLost = 0;
        for(int i = 0; i < players.length; i++){
            if(checkHand(currState.getPlayerHands().get(i),0) != -1){
                playersLost++;
            }
        }
        if(playersLost == (currState.getNumPlayers()-1)) {
            for(int i = 0; i < currState.getNumPlayers(); i++){
                if(checkHand(currState.getPlayerHands().get(i),0) == -1){
                    return "Congratulations, " + playerNames[i] + "! You won";
                }
            }

        }
        return null;
    }//checkIfGameOver

    //Takes in an instance of GameAction and acts according to the action
    // taken in
    @Override
    protected boolean makeMove(GameAction action) {

        /*
         * check to see if it's about to be the
         *human player's turn and clear the log
         * it that is true
         */
        if(currState.getWhoseTurn() == 0){
            currState.clearPlayerLog(true);
        }

        //check which action is being taken
        if (action instanceof PlayNopeCard) {
            return Nope(action.getPlayer());
        }
        else if (action instanceof DrawCardAction) {

                return drawCard(action.getPlayer());

        } else if (action instanceof PlayFavorCard) {
                return Favor(action.getPlayer(),
                        ((PlayFavorCard) action).getTarget(),
                        ((PlayFavorCard) action).getChoice());
        } else if (action instanceof PlayAttackCard) {

                return Attack(action.getPlayer());
        } else if (action instanceof PlayShuffleCard) {

                return Shuffle(action.getPlayer());
        } else if (action instanceof PlaySkipCard) {

                return Skip(action.getPlayer());
        } else if (action instanceof PlayFutureCard) {

                return SeeTheFuture(action.getPlayer());
        } else if (action instanceof PlayDefuseCard) {

                return Defuse(action.getPlayer());
        } else if (action instanceof Trade2Action) {

                return trade2(action.getPlayer(), ((Trade2Action) action).getTarget(),
                        ((Trade2Action) action).getPosC1(), ((Trade2Action) action).getPosC2());
        } else if (action instanceof Trade3Action) {

                return trade3(action.getPlayer(), ((Trade3Action) action).getTarget(),
                        ((Trade3Action) action).getPosC1(),
                        ((Trade3Action) action).getPosC2(),
                        ((Trade3Action) action).getPosC3(),
                        ((Trade3Action) action).getTargetValue());
        } else if (action instanceof Trade5Action) {

                return trade5(action.getPlayer(), ((Trade5Action) action).getPosC1(),
                        ((Trade5Action) action).getPosC2(),
                        ((Trade5Action) action).getPosC3(),
                        ((Trade5Action) action).getPosC4(), ((Trade5Action) action).getPosC5(),
                        ((Trade5Action) action).getTargetValue());
        }
        else{
            //error message
            Log.d("Invalid Action",
                    "Action provided was an invalid action");
            return false;
        }



    }//makeMove

    /**
     * Attack card, current player ends their turn without drawing a card and
     * forces the next player to draw two cards before ending their turn
     * @param p
     *      takes in a GamePlayer object parameter
     * @return
     *      returns a boolean if the card was found and the attack executed
     */
    public boolean Attack(GamePlayer p) {
        int card = checkHand(currState.getCurrentPlayerHand(), 6);
        //move the card into the discard pile
        if(card == -1){
            return false;
        }
        currState.getDiscardPile().add(currState.getCurrentPlayerHand().get(card));
        currState.getCurrentPlayerHand().remove(card);

        //Sending a message to the log
        String logMessage = playerNames[currState.getWhoseTurn()] + " played an Attack card ";
        currState.addToPlayerLog(logMessage);
        Log.d("Log Played Attack", logMessage);

        //adds attack action to array
        currState.addActionsPerformed(6);
        currState.addWhoPerformed();

        //This player doesn't have to draw and the next player has to take two cards
        int tempCardsToDraw = currState.getCardsToDraw();
        currState.setCardsToDraw(0);
        nextTurn();
        currState.setCardsToDraw(tempCardsToDraw+1);


        return true;
    }//Attack()

    /**
     * Nope card cancels the previous player's card if it was an attack,
     * skip, or nope card
     * @param p
     *      takes in a GamePlayer object
     * @return
     *      returns true if the action was successful and false if not
     */
    public boolean Nope(GamePlayer p) {
        //Get the last action played
        int actionTracker = currState.getActionsPerformed().size() - 1;
        //find position of nope in hand
        int card = checkHand(currState.getCurrentPlayerHand(), 11);
        //check if player has nope in hand
        if (card == -1) {
            return false;
        }
        //Make sure there is an action to nope
        if (currState.getActionsPerformed().size() == 0) {
            return false;
        }
        //check if last action was attack, skip, or nope, otherwise return false
        if (currState.getActionsPerformed().get(actionTracker) == 6 || currState.getActionsPerformed().get(actionTracker) == 9 ||
                currState.getActionsPerformed().get(actionTracker) == 11) {

            //adds nope action to array
            currState.addActionsPerformed(11);
            currState.addWhoPerformed();

            //add nope to discard and remove it from hand
            currState.getDiscardPile().add(currState.getCurrentPlayerHand().get(card));
            currState.getCurrentPlayerHand().remove(card);

            //Sending a message to the log
            String logMessage = playerNames[currState.getWhoseTurn()] + " played a Nope card";
            currState.addToPlayerLog(logMessage);
            Log.d("Log Played Nope", logMessage);

        //if last action was skip undo skip
        if (currState.getActionsPerformed().get(actionTracker) == 9) {
            String logSkip = playerNames[currState.getWhoseTurn()] + " Nope'd a Skip card";
            currState.addToPlayerLog(logSkip);
            Log.d("Log Played Nope", logSkip);
            decrementTurn();
        }
        //if last action was attack undo attack
        else if (currState.getActionsPerformed().get(actionTracker) == 6) {
            currState.setCardsToDraw(currState.getCardsToDraw() - 1);
            String logAttack = playerNames[currState.getWhoseTurn()] + " Nope'd an Attack card";
            currState.addToPlayerLog(logAttack);
            Log.d("Log Played Nope", logAttack);
            decrementTurn();
        }
        //if last card was nope, check for more nopes
        else if (currState.getActionsPerformed().get(actionTracker) == 11) {
            //while the current value in the actions array is nope, go back 1
            while (currState.getActionsPerformed().get(actionTracker) == 11) {
                actionTracker = actionTracker - 1;
            }
            //if action (not nope) wasn't performed by this player undo it
            if (currState.getWhoPerformed().get(actionTracker) != currState.getWhoseTurn()) {
                if (currState.getActionsPerformed().get(actionTracker) == 6) {
                    //undo attaack
                    decrementTurn();
                    currState.setCardsToDraw(currState.getCardsToDraw() - 1);
                } else if (currState.getActionsPerformed().get(actionTracker) == 9) {
                    decrementTurn();
                }
            }
            //if action (not nope) was performed by this player redo it
            else if (currState.getWhoPerformed().get(actionTracker) == currState.getWhoseTurn()) {
                //do skip or attack
                if (currState.getActionsPerformed().get(actionTracker) == 6) {
                    incrementTurn();
                    currState.setCardsToDraw(currState.getCardsToDraw() + 1);
                } else if (currState.getActionsPerformed().get(actionTracker) == 9) {
                    incrementTurn();
                }
            }
        }

        return true;
    }
       else{
           return false;
        }
    }

    /**
     * method to find whose turn it was previously
     * @return
     *      returns an int representing whose turn it was previously
     */
    public int lastTurn(){
        int last = currState.getWhoseTurn();
        if(last == 0){
            last = currState.getNumPlayers() - 1;
        }
        else{
            last = last - 1;
        }
        //Determine if the last player is still playing the game
        if(currState.getPlayerHand(last).size() != 0) {
            while (currState.hasPlayerLost(last)) {
                if (last == 0) {
                    last = currState.getNumPlayers() - 1;
                } else {
                    last = last - 1;
                }
            }
        }
        return last;
    }//lastTurn()

    /**
     * method to decrement the turn, making sure to wrap around to the next
     * player if the number would be less than 0
     */
    public void decrementTurn(){
        if(currState.getWhoseTurn() == 0){
            currState.setWhoseTurn(currState.getNumPlayers() - 1);
        }
        else{
            currState.setWhoseTurn(currState.getWhoseTurn() - 1);
        }
        while(currState.hasPlayerLost(currState.getWhoseTurn())){
            if(currState.getWhoseTurn() == 0){
                currState.setWhoseTurn(currState.getNumPlayers() - 1);
                if(currState.getCurrentPlayerHand().size() == 0){
                    return;
                }
            }
            else{
                currState.setWhoseTurn(currState.getWhoseTurn() - 1);
                if(currState.getCurrentPlayerHand().size() == 0){
                    return;
                }
            }
        }
    }//decrementTurn()

    /**
     * increments to the next player's turn
     */
    public void incrementTurn(){
        //sets whose turn to next turn
        currState.setWhoseTurn((currState.getWhoseTurn()+1)%(currState.getNumPlayers()));
        if(currState.getCurrentPlayerHand() != null) {
            while (currState.getCurrentPlayerHand().get(0).getCardType() == 0) {
                currState.setWhoseTurn((currState.getWhoseTurn() + 1) % (currState.getPlayerHands().size()));
            }
        }
    }


    /**
     * Favor card, current player selects a target player and takes a random
     * card from their hand
     * @param p
     *      GamePlayer object for the current player
     * @param target
     *      integer for the target player of the favor card
     * @param targCardPos
     *      position of the target card to steal
     * @return
     *      returns true if the action was successful, else false
     */
    public boolean Favor(GamePlayer p, int target, int targCardPos) {
        int card = checkHand(currState.getCurrentPlayerHand(), 8);
        if(card == -1){
            return false;
        }
        if(currState.getPlayerHand(target).size() == 0){
            return false;
        }
        if(targCardPos >= currState.getPlayerHand(target).size()){
            return false;
        }
        //copy selected card from target player to current player
        currState.getCurrentPlayerHand().add(currState.getPlayerHand(target).get(targCardPos));
        currState.getPlayerHand(target).remove(targCardPos);
        //move the played favor card to the discard pile and remove it from
        // the players hand
        currState.getDiscardPile().add(currState.getCurrentPlayerHand().get(card));
        currState.getCurrentPlayerHand().remove(card);

        //Sending a message to the log
        String logMessage = playerNames[currState.getWhoseTurn()] + " played a Favor card ";
        currState.addToPlayerLog(logMessage);
        Log.d("Log Played Favor", logMessage);

        return true;
    }//Favor()

    /**
     * See the future card, current player looks at the top three cards of
     * the deck
     * @param p
     *      GamePlayer object for the current player
     * @return
     *      returns true if the action was successful and false otherwise
     */
    public boolean SeeTheFuture(GamePlayer p) {
        int card = checkHand(currState.getCurrentPlayerHand(), 10);
        if(card == -1){
            return false;
        }
        currState.getDiscardPile().add(currState.getCurrentPlayerHand().get(card));
        currState.getCurrentPlayerHand().remove(card);


        //adds STF action to action history array
        currState.addActionsPerformed(10);
        currState.addWhoPerformed();

        //Sending a message to the log
        String logMessage = playerNames[currState.getWhoseTurn()] + " played a SeeTheFuture card ";
        currState.addToPlayerLog(logMessage);
        Log.d("Log Played SeeTheFuture", logMessage);

        return true;
    }//SeeTheFuture()

    /**
     * shuffle card, shuffle the deck randomly
     * @param p
     *      GamePlayer object for the current player
     * @return
     *      returns true if the action was successful and false otherwise
     */
    public boolean Shuffle(GamePlayer p) {
        /**
         * External Citation
         * Date: 19 October 2020
         * Problem: Was unsure if there was an easy way to shuffle an array
         * list
         * <p>
         * Resource:
         * https://www.java2novice.com/java-collections-and-util/arraylist
         * /shuffle/
         * Solution: Used the example code to shuffle the deck
         */
        //find position of the shuffle card in players hand
        int card = checkHand(currState.getCurrentPlayerHand(), 7);
        if(card == -1){
            return false;
        }
        //add the played shuffle card to the discard pile and remove it from
        //the players hand
        currState.getDiscardPile().add(currState.getCurrentPlayerHand().get(card));
        currState.getCurrentPlayerHand().remove(card);
        //shuffle the deck
        Collections.shuffle(currState.getDeck());

        //adds shuffle action to action history array
        currState.addActionsPerformed(7);
        currState.addWhoPerformed();

        //Sending a message to the log
        String logMessage = playerNames[currState.getWhoseTurn()] + " played a Shuffle card ";
        currState.addToPlayerLog(logMessage);
        Log.d("Log Played Shuffle", logMessage);

        return true;
    }

    /**
     * skip card, current player ends their turn without drawing a card
     * @param p
     *      GamePlayer object for the current player
     * @return
     *      returns true if the action was successful, false otherwise
     */
    public boolean Skip(GamePlayer p) {
        int card = checkHand(currState.getCurrentPlayerHand(), 9);
        if(card == -1){
            return false;
        }
        //finds skip in hand and removes it before incrementing the turn;
        currState.getDiscardPile().add(currState.getCurrentPlayerHand().get(card));
        currState.getCurrentPlayerHand().remove(card);

        //Sending a message to the log
        String logMessage = playerNames[currState.getWhoseTurn()] + " played a Skip card ";
        currState.addToPlayerLog(logMessage);
        Log.d("Log Played Skip", logMessage);

        //call the nextTurn method to move to the next player
        currState.setCardsToDraw(currState.getCardsToDraw()-1);
        if(currState.getCardsToDraw() > 1){
            return true;
        }

        //adds attack action to array
        currState.addActionsPerformed(9);
        currState.addWhoPerformed();

        nextTurn();

        return true;
    }

    /**
     * Defuse card, defuses an exploding kitten card and saves the player
     * from losing the game. If the payer does not have a defuse card they
     * lose the game
     * @param p
     *      GamePlayer parameter for current player
     * @return
     *      returns true if the action was successful and false otherwise
     */
    public boolean Defuse(GamePlayer player) {
        //Initalizing logMesage
        String logMessage;
        //check if there is a defuse card in the hand
        int defusePos = checkHand(currState.getCurrentPlayerHand(), 12);
        int explodePos = checkHand(currState.getCurrentPlayerHand(), 0);
        if(defusePos != -1 && explodePos != -1){

            currState.getDiscardPile().add(currState.getCurrentPlayerHand().get(defusePos));
            currState.getCurrentPlayerHand().remove(explodePos);
            currState.getCurrentPlayerHand().remove(defusePos);

            //Sending a message to the log
            logMessage = playerNames[currState.getWhoseTurn()] + " defused an ExplodingKitten ";
            currState.addToPlayerLog(logMessage);
            Log.d("Log Played Defuse", logMessage);

            if(player instanceof EKHumanPlayer){
                humanDefuse((EKHumanPlayer)player);
            }
            else if(player instanceof EKSmartComputerPlayer){

            }
            else {
                //Insert the exploding kitten in a random spot *Dumb Move
                int randPos = (int) (Math.random() * (currState.getDeck().size()));
                currState.getDeck().add(randPos, new Card(0));

            }
            return true;
        }

        //Sending a message to the log
        logMessage = playerNames[currState.getWhoseTurn()] + " failed to defuse an ExplodingKitten ";
        currState.addToPlayerLog(logMessage);
        Log.d("Log Failed to Defuse", logMessage);

        return false;
    }

    /**
     * draw a card action, draws a card and places it in the player's hand
     * and ends the player's turn. If the card is an exploding kitten and
     * they are not able to defuse the exploding kitten, populate the
     * player's hand with 1 exploding kitten card
     * @param player
     *      GamePlayer object for current player
     * @return
     *      returns true if the action was successful and false otherwise
     */
    public boolean drawCard(GamePlayer player) {
        //checks if deck is empty
        if(currState.getDeck().size() == 0){
            return false;
        }
        if (currState.getDeck().get(0) == null || player == null) {
            return false;
        }
        if (currState.getCardsToDraw() == 0){
            nextTurn();
            return true;
        }
        //add top card of deck to hand and remove it from deck
        currState.getCurrentPlayerHand().add(currState.getDeck().get(0));
        //currState.getPlayerHands().get(this.currState.getWhoseTurn()).add(currState.getDeck().get(0));
        currState.getDeck().remove(0);
        currState.setCardsToDraw(currState.getCardsToDraw()-1);

        //Sending a message to the log
        String logMessage = playerNames[currState.getWhoseTurn()] + " drew a card ";
        currState.addToPlayerLog(logMessage);
        Log.d(" Log Draw Card", logMessage);

        //Check if the player drew an Exploding Kitten and they can't defuse it, then they lose
        if(checkHand(currState.getCurrentPlayerHand(),0) != -1){
            if(!Defuse(player)) {
                Card temp = new Card(0);
                currState.getCurrentPlayerHand().clear();
                currState.getCurrentPlayerHand().add(temp);
                nextTurn();
                return true;
            }
        }
        //Progress the turn count
        if(currState.getCardsToDraw() == 0){
            //adds draw action to action history array
            currState.addActionsPerformed(1);
            currState.addWhoPerformed();
            nextTurn();
        }
        else{
            currState.addActionsPerformed(1);
            currState.addWhoPerformed();
            drawCard(player);
        }
        return true;
    }

    /**
     * trade 2 action, takes 2 cards from the player's hand and check if they
     * are the same. If they are the same take a random card from the target
     * player's hand.
     * @param play
     *      GamePlayer object for current player
     * @param targ
     *      int to represent the target player for the trade
     * @param a
     *      int for the card position of the first card to trade
     * @param b
     *      int for the card position of the second card to trade
     * @return
     *      returns true if the action was successful and false otherwise
     */
    public boolean trade2(GamePlayer play, int targ, int a, int b) {
        if(a >= currState.getCurrentPlayerHand().size() || b >= currState.getCurrentPlayerHand().size()){
            return false;
        }
        if(currState.getPlayerHand(targ).size() == 0 || currState.getPlayerHand(targ) == null){
            return false;
        }

        //determine if the two cards are of the same card type
        Card trade1 = currState.getCurrentPlayerHand().get(a);
        Card trade2 = currState.getCurrentPlayerHand().get(b);
        if (trade1.getCardType() == trade2.getCardType()) {
            //update the players hand
            if(b > a){
                currState.getCurrentPlayerHand().remove(b);
                currState.getCurrentPlayerHand().remove(a);
            }
            else if (a > b){
                currState.getCurrentPlayerHand().remove(a);
                currState.getCurrentPlayerHand().remove(b);
            }
            else {
                return false;
            }
            //copy the new card from the target player into the player hand
            int randomPos = (int)(Math.random()*(currState.getPlayerHand(targ).size()));
            currState.getCurrentPlayerHand().add(currState.getPlayerHand(targ).get(randomPos));
            //remove the target player card that was stolen
            currState.getPlayerHand(targ).remove(randomPos);

            //Sending a message to the log
            String logMessage = playerNames[currState.getWhoseTurn()] + " traded 2 cards ";
            currState.addToPlayerLog(logMessage);
            Log.d("Log Trade 2", logMessage);

            return true;
        }

        return false;
    }

    /**
     * trade 3 action, takes 3 cards and checks if they are all the same type
     * . If they are all the same, checks the target player's hand for a card
     * of the specified type. If a card is found move it to the current
     * player's hand and take it out of the target player's hand.
     * @param play
     *      GamePlayer object for current player
     * @param targ
     *      int to represent the targ player
     * @param a
     *      int for the location of the current player hand card 1
     * @param b
     *      int for the location of the current player hand card 2
     * @param c
     *      int for the location of the current player hand card 3
     * @param targCard
     *      int for the card type of the desired card
     * @return
     *      return true for a successful action and false otherwise
     */
    public boolean trade3(GamePlayer play, int targ, int a, int b, int c,
                          int targCard) {
        if(a >= currState.getCurrentPlayerHand().size() || b >= currState.getCurrentPlayerHand().size() || c >= currState.getCurrentPlayerHand().size()){
            return false;
        }
        //determine if the three cards are of the same type
        Card trade1 = currState.getCurrentPlayerHand().get(a);
        Card trade2 = currState.getCurrentPlayerHand().get(b);
        Card trade3 = currState.getCurrentPlayerHand().get(c);

        if (trade1.getCardType() == trade2.getCardType() &&
                trade2.getCardType() == trade3.getCardType()) {
            //update the players hand
            currState.getCurrentPlayerHand().remove(c);
            currState.getCurrentPlayerHand().remove(b);
            currState.getCurrentPlayerHand().remove(a);

            //Send to the log that they traded 3 cards
            String logMessage = playerNames[currState.getWhoseTurn()] + " traded 3 cards ";
            currState.addToPlayerLog(logMessage);
            Log.d("Log Trade 3", logMessage);

            //check to see if the target player has the desired card
            int targCardPos = checkHand(currState.getPlayerHand(targ), targCard);
                if (targCardPos != -1){
                    //add the desired card to the player hand and remove it
                    // from the target player hand
                    currState.getCurrentPlayerHand().add(currState.getPlayerHand(targ).get(targCardPos));
                    currState.getPlayerHand(targ).remove(targCardPos);
                    //Sending a message to the log
                    logMessage = playerNames[currState.getWhoseTurn()] + "'s chosen card was found ";
                }
                else{
                    //Sending a message to the log
                    logMessage = playerNames[currState.getWhoseTurn()] + "'s chosen card was not found ";
                }
            currState.addToPlayerLog(logMessage);
            Log.d("Log Trade 3 card found/not found", logMessage);

            return true;
        }
        return false;
    }

    /**
     * trade 5 cards, check the 5 cards to see if they are all unique. If
     * they are remove the target card from the discard pile and put it in
     * the player's hand
     * @param p
     *      GamePlayer object for the current player
     * @param cardPos1
     *      int for the first card in the player's hand to trade
     * @param cardPos2
     *      int for the second card in the player's hand to trade
     * @param cardPos3
     *      int for the third card in the player's hand to trade
     * @param cardPos4
     *      int for the fourth card in the player's hand to trade
     * @param cardPos5
     *      int for the fifth card in the player's hand to trade
     * @param target
     *      int for the position of the target card in the discard pile
     * @return
     *      returns true if the action was successful and false otherwise
     */
    public boolean trade5(GamePlayer p, int cardPos1, int cardPos2, int cardPos3,
                          int cardPos4, int cardPos5, int target) {
        if(cardPos1 >= currState.getCurrentPlayerHand().size() ||
                cardPos2 >= currState.getCurrentPlayerHand().size() ||
                cardPos3 >= currState.getCurrentPlayerHand().size() ||
                cardPos4 >= currState.getCurrentPlayerHand().size() ||
                cardPos5 >= currState.getCurrentPlayerHand().size() ){
            return false;
        }
        
            //determine if the 5 cards are unique
        int comp1 = currState.getCurrentPlayerHand().get(cardPos1).getCardType();
        int comp2 = currState.getCurrentPlayerHand().get(cardPos2).getCardType();
        int comp3 = currState.getCurrentPlayerHand().get(cardPos3).getCardType();
        int comp4 = currState.getCurrentPlayerHand().get(cardPos4).getCardType();
        int comp5 = currState.getCurrentPlayerHand().get(cardPos5).getCardType();
        if(target >= currState.getDiscardPile().size()){
            return false;
        }
        if (comp1 == comp2 || comp1 == comp3 || comp1 == comp4 || comp1 == comp5 ||
                comp2 == comp3 || comp2 == comp4 || comp2 == comp5 ||
                comp3 == comp4 || comp3 == comp5 ||
                comp4 == comp5) {
            return false;
        }
        else {
            //update the players hand
            currState.getCurrentPlayerHand().remove(cardPos5);
            currState.getCurrentPlayerHand().remove(cardPos4);
            currState.getCurrentPlayerHand().remove(cardPos3);
            currState.getCurrentPlayerHand().remove(cardPos2);
            currState.getCurrentPlayerHand().remove(cardPos1);
            //copy the desired card to the players hand
            currState.getCurrentPlayerHand().add(currState.getDiscardPile().get(target));
            //remove the card from the discard pile
            currState.getDiscardPile().remove(target);

            //Sending a message to the log
            String logMessage = playerNames[currState.getWhoseTurn()] + " traded 5 cards ";
            currState.addToPlayerLog(logMessage);
            Log.d("Log Trade 5", logMessage);

            return true;
        }
    } // trade5

    //increments turn and wraps around from the last player to the first player
    //skips over player if they have an exploding kitten
    public void nextTurn() {
        if(currState.getCardsToDraw() == 0){
            currState.setWhoseTurn((currState.getWhoseTurn()+1)%(currState.getNumPlayers()));
        }
        if(currState.getCurrentPlayerHand() != null && currState.getCurrentPlayerHand().size() != 0) {
            while (currState.getCurrentPlayerHand().get(0).getCardType() == 0) {
                currState.setWhoseTurn((currState.getWhoseTurn() + 1) % (currState.getPlayerHands().size()));
                if(currState.getCurrentPlayerHand().size() == 0){
                    currState.setCardsToDraw(1);
                    return;
                }
            }
        }
        currState.setCardsToDraw(1);
    }//nextTurn


    /**
     * method to check the player's hand for a card of the given card type
     * @param hand
     *      ArrayList of cards that is the player's hand
     * @param cardTypeValue
     *      int for the card type that is being searched for
     * @return
     */
    public int checkHand(ArrayList<Card> hand, int cardTypeValue) {

        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getCardType() == cardTypeValue) {
                return i;
            }
        }
        return -1;
    }//checkHand

    //Smart defuse method allows the player to insert the exploding kitten wherever they choose
    //https://www.codelocker.net/p/101/android-create-a-popup-window-with-buttons/

    /**
     * Smart defuse method allows the player to insert the exploding kitten wherever they choose
     * @param player
     */
    protected void humanDefuse(final EKHumanPlayer player){

        /**
         * External Citation
         * Problem: Wanted to create a popup window for help
         * Source: ThreeThirteenTeam help_button code
         * Solution: used their code as a framework to create our
         * own help window
         */
        GameMainActivity playerMainActivity = player.getMyActivity();


        //inflate the view
        LayoutInflater inflater = (LayoutInflater)
                playerMainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Set the layout of the view to the popup window
        View popupView = inflater.inflate(R.layout.insert_explodingkitten_window, null);


        // create popup window that will match the dimensions of the screen
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow chooseWindow = new PopupWindow(popupView, width, height, focusable);

        //prevent clickable background
        chooseWindow.setBackgroundDrawable(null);

        // show popup window
        chooseWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        //Initalizing all of the buttons for this popup window
        Button randomButton = (Button)popupView.findViewById(R.id.randomButton);
        Button minusButton = (Button)popupView.findViewById(R.id.minusButton);
        Button plusButton = (Button)popupView.findViewById(R.id.plusButton);
        Button enterButton = (Button)popupView.findViewById(R.id.enterButton);
        final TextView textChoice = (TextView)popupView.findViewById(R.id.choiceDeckIndex);
        textChoice.setText(Integer.toString(currState.getDeck().size()));

            //On click methods for all of the buttons on the view 
             randomButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     int randPos = (int) (Math.random() * (currState.getDeck().size()));
                     currState.getDeck().add(randPos, new Card(0));
                     chooseWindow.dismiss();
                     Log.d("Log RandButton", playerNames[currState.getWhoseTurn()] +
                             "put the exploding kitten in a random position");
                 }
             });
             enterButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     currState.getDeck().add(deckIndexChoice, new Card(0));
                     chooseWindow.dismiss();
                     Log.d("Log EnterButton", playerNames[currState.getWhoseTurn()] +
                             "put the exploding kitten in a specifc position");
                 }
             });
             minusButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(deckIndexChoice > 0) {
                         deckIndexChoice--;
                         textChoice.setText(Integer.toString(deckIndexChoice));
                     }
                 }
             });
             plusButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(deckIndexChoice < currState.getDeck().size()){
                         deckIndexChoice++;
                         textChoice.setText(Integer.toString(deckIndexChoice));
                     }
                 }
             });

    }

    /**
     * Getter method to return local game's instance of EKGameState
     * @return
     *      returns the current GameState
     */
    public EKGameState getCurrState(){
        return this.currState;
    }

    /**
     * Getter to get the array of players
     * @return
     *  returns an array of players
     */
    public GamePlayer[] getPlayers(){
        return players;
    }

}//EK_LocalGame
