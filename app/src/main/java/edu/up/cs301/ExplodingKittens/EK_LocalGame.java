
package edu.up.cs301.ExplodingKittens;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;
import edu.up.cs301.game.GameFramework.infoMessage.GameState;
import static java.sql.Types.NULL;

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

public class EK_LocalGame extends LocalGame{

    //Instance variable representing the current state
    private EKGameState currState;


    //constructor
    public EK_LocalGame(int numOfPlayers) {
        //creates a game with 4 players
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



    /****************************************************************************
     * Card Methods shown below are:
     * Attack
     * Nope
     * Favor
     * SeeTheFuture
     * Shuffle*
     * Skip*
     * Defuse*
     * drawCard*
     * Trade2
     * Trade3
     * Trade5
     * nextTurn
     * checkCard
     ***************************************************************************/
    //Attack card
    //current player ends their turn without drawing a card and forces the
    //next player to draw two cards before ending their turn
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

    //Nope Card
    //Cancels a previous move if it was an attack, skip, or nope
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
            decrementTurn();
            String logSkip = playerNames[currState.getWhoseTurn()] + " Nope'd a Skip card";
            currState.addToPlayerLog(logSkip);
            Log.d("Log Played Nope", logSkip);
        }
        //if last action was attack undo attack
        else if (currState.getActionsPerformed().get(actionTracker) == 6) {
            decrementTurn();
            currState.setCardsToDraw(currState.getCardsToDraw() - 1);
            String logAttack = playerNames[currState.getWhoseTurn()] + " Nope'd an Attack card";
            currState.addToPlayerLog(logAttack);
            Log.d("Log Played Nope", logAttack);
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

    //returns the value of the last player who made a move
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

    //decrements turn
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

    //increments turn but doesn't set cards to draw to 1
    public void incrementTurn(){
        //sets whose turn to next turn
        currState.setWhoseTurn((currState.getWhoseTurn()+1)%(currState.getNumPlayers()));
        if(currState.getCurrentPlayerHand() != null) {
            while (currState.getCurrentPlayerHand().get(0).getCardType() == 0) {
                currState.setWhoseTurn((currState.getWhoseTurn() + 1) % (currState.getPlayerHands().size()));
            }
        }
    }


    //Favor card
    //current player selects a target player and target player gives current
    //player a card (randomly))
    public boolean Favor(GamePlayer p, int target, int targCardPos) {
        int card = checkHand(currState.getCurrentPlayerHand(), 8);
        if(card == -1){
            return false;
        }
        if(currState.getPlayerHand(target).size() == 0){
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

    //See the Future card
    //current player looks at the top three cards of the deck
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

    //Shuffle card
    //shuffles the deck randomly
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

    //Skip card
    //current players turn ends without drawing a card
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

    ///Play Defuse card
    //if the current player does not have a defuse card, they lose the game,
    //if they do have a defuse card play the defuse card and reshuffle the
    //exploding kitten card back into the deck
    public boolean Defuse(GamePlayer p) {
        //Initalizing logMesage
        String logMessage;
        //check if there is a defuse card in the hand
        int defusePos = checkHand(currState.getCurrentPlayerHand(), 12);
        int explodePos = checkHand(currState.getCurrentPlayerHand(), 0);
        if(defusePos != -1 && explodePos != -1){
            currState.getDiscardPile().add(currState.getCurrentPlayerHand().get(defusePos));
            int randPos = (int)(Math.random()*(currState.getDeck().size()));
            currState.getDeck().add(randPos,currState.getCurrentPlayerHand().get(explodePos));
            currState.getCurrentPlayerHand().remove(explodePos);
            currState.getCurrentPlayerHand().remove(defusePos);

            //Sending a message to the log
            logMessage = playerNames[currState.getWhoseTurn()] + " defused an ExplodingKitten ";
            currState.addToPlayerLog(logMessage);
            Log.d("Log Played Defuse", logMessage);

            return true;
        }

        //Sending a message to the log
        logMessage = playerNames[currState.getWhoseTurn()] + " failed to defuse an ExplodingKitten ";
        currState.addToPlayerLog(logMessage);
        Log.d("Log Failed to Defuse", logMessage);

        return false;
    }

    //draw a card and end the turn of the player
    //replaces their hand with a single exploding kitten card
    //if they are not able to defuse an exploding kitten
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
            if(!(Defuse(player))){
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

    public boolean trade2(GamePlayer play, int targ, int a, int b) {
        if(a >= currState.getCurrentPlayerHand().size() || b >= currState.getCurrentPlayerHand().size()){
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

    //Trade 5 cards
    //current player selects 5 different cards and trades them for a card
    //from the discard pile
    public boolean trade5(GamePlayer p, int cardPos1, int cardPos2, int cardPos3,
                          int cardPos4, int cardPos5, int target) {
        //determine if the 5 cards are unique
        int comp1 = currState.getCurrentPlayerHand().get(cardPos1).getCardType();
        int comp2 = currState.getCurrentPlayerHand().get(cardPos2).getCardType();
        int comp3 = currState.getCurrentPlayerHand().get(cardPos3).getCardType();
        int comp4 = currState.getCurrentPlayerHand().get(cardPos4).getCardType();
        int comp5 = currState.getCurrentPlayerHand().get(cardPos5).getCardType();
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


    //check to see if the card type exists in the players hand, if it
    // does, return the position of the card.
    //If it doesn't return -1
    public int checkHand(ArrayList<Card> hand, int cardTypeValue) {

        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getCardType() == cardTypeValue) {
                return i;
            }
        }
        return -1;
    }//checkHand

    //Getter method to return local game's instance of EKGameState
    public EKGameState getCurrState(){
        return this.currState;
    }

}//EK_LocalGame
