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

public class EK_LocalGame extends LocalGame {

    //Instance variable representing the current state
    private EKGameState currState;
    //Instance variable representing the previous state of the game
    private EKGameState previousState;

    public EK_LocalGame() {
        //crashes here
        this.currState = new EKGameState(4);
        //call method to set currState playerNum
        this.previousState = null;
    }


    //send updated state to a given player
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        EKGameState gameCopy = new EKGameState(currState);
        p.sendInfo(gameCopy);
    }//sendUpdatedState

    @Override
    protected boolean canMove(int playerIdx) {
        return playerIdx == currState.getWhoseTurn();
    }


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
    }

    @Override
    protected boolean makeMove(GameAction action) {
        //check which action is being taken
        if (action instanceof PlayNopeCard) {
            return Nope(action.getPlayer());
        }
        else {
            this.previousState = new EKGameState(this.currState);
            if (action instanceof DrawCardAction) {

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
        }

        //error message
        Log.d("Invalid Action",
                "Action provided was an invalid action");
        return false;
    }



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
        Log.d("Log Played Attack", playerNames[currState.getWhoseTurn()] + " played an Attack card ");

        //This player doesn't have to draw and the next player has to take two cards
        int tempCardsToDraw = currState.getCardsToDraw();
        currState.setCardsToDraw(0);
        nextTurn();
        currState.setCardsToDraw(tempCardsToDraw+1);

        return true;
    }


    //Nope card
    public boolean Nope(GamePlayer p) {
        int card = checkHand(currState.getCurrentPlayerHand(), 11);
        //move the played nope card to the discard pile and remove it from
        //the players hand
        if(card == -1){
            return false;
        }
        currState.getDiscardPile().add(currState.getCurrentPlayerHand().get(card));
        currState.getCurrentPlayerHand().remove(card);

        //Sending a message to the log
        Log.d("Log Played Nope", playerNames[currState.getWhoseTurn()] + " played a Nope card");

        EKGameState temp = new EKGameState(currState);
        currState = previousState;
        previousState = temp;

        return true;
    }

    //Favor card
    //current player selects a target player and target player gives current
    //player a card (randomly))
    public boolean Favor(GamePlayer p, int target, int targCardPos) {
        int card = checkHand(currState.getCurrentPlayerHand(), 8);
        if(card == -1){
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
        Log.d("Log Played Favor", playerNames[currState.getWhoseTurn()] + " played a Favor card ");

        return true;
    }

    //See the Future card
    //current player looks at the top three cards of the deck
    public boolean SeeTheFuture(GamePlayer p) {
        int card = checkHand(currState.getCurrentPlayerHand(), 10);
        if(card == -1){
            return false;
        }
        currState.getDiscardPile().add(currState.getCurrentPlayerHand().get(card));
        currState.getCurrentPlayerHand().remove(card);

        //Sending a message to the log
        Log.d("Log Played SeeTheFuture", playerNames[currState.getWhoseTurn()] + " played a SeeTheFuture card ");

        return true;
    }

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

        //Sending a message to the log
        Log.d("Log Played Shuffle", playerNames[currState.getWhoseTurn()] + " played a Shuffle card ");

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
        Log.d("Log Played Skip", playerNames[currState.getWhoseTurn()] + " played a Skip card ");

        //call the nextTurn method to move to the next player
        currState.setCardsToDraw(currState.getCardsToDraw()-1);
        if(currState.getCardsToDraw() > 1){
            return true;
        }
        nextTurn();

        return true;
    }

    ///Play Defuse card
    //if the current player does not have a defuse card, they lose the game,
    //if they do have a defuse card play the defuse card and reshuffle the
    //exploding kitten card back into the deck
    public boolean Defuse(GamePlayer p) {
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
            Log.d("Log Played Defuse", playerNames[currState.getWhoseTurn()] + " defused an ExplodingKitten ");

            return true;
        }

        //Sending a message to the log
        Log.d("Log Failed to Defuse", playerNames[currState.getWhoseTurn()] + " failed to defuse an Exploding Kitten ");

        return false;
    }

    //draw a card and end the turn of the player
    //replaces their hand with a single exploding kitten card
    //if they are not able to defuse an exploding kitten
    public boolean drawCard(GamePlayer player) {
        //checks if deck is empty
        if (currState.getDeck().get(0) == null || player == null) {
            return false;
        }
        if (currState.getCardsToDraw() == 0){
            nextTurn();
            return true;
        }
        //add top card of deck to hand and remove it from deck
        currState.getPlayerHands().get(this.currState.getWhoseTurn()).add(currState.getDeck().get(0));
        currState.getDeck().remove(0);
        currState.setCardsToDraw(currState.getCardsToDraw()-1);

        //Sending a message to the log
        Log.d(" Log Draw Card", playerNames[currState.getWhoseTurn()] + " drew a card ");

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
            nextTurn();
        }
        else{
            drawCard(player);
        }
        return true;
    }

    public boolean trade2(GamePlayer play, int targ, int a, int b) {
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
            else if(a == b){
                return false;
            }
            //copy the new card from the target player into the player hand
            int randomPos = (int)(Math.random()*(currState.getPlayerHand(targ).size()));
            currState.getCurrentPlayerHand().add(currState.getPlayerHand(targ).get(randomPos));
            //remove the target player card that was stolen
            currState.getPlayerHand(targ).remove(randomPos);

            //Sending a message to the log
            Log.d("Log Trade 2", playerNames[currState.getWhoseTurn()] + " traded 2 cards ");

            return true;
        }

        return false;
    }

    public boolean trade3(GamePlayer play, int targ, int a, int b, int c,
                          int targCard) {
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
            //check to see if the target player has the desired card
            int targCardPos = checkHand(currState.getPlayerHand(targ), targCard);
                if (targCardPos != -1){
                    //add the desired card to the player hand and remove it
                    // from the target player hand
                    currState.getCurrentPlayerHand().add(currState.getPlayerHand(targ).get(targCardPos));
                    currState.getPlayerHand(targ).remove(targCardPos);
                }

            //Sending a message to the log
            Log.d("Log Trade 3", playerNames[currState.getWhoseTurn()] + " traded 3 cards ");

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
            //int targCardPos = checkHand(currState.getDiscardPile(), target);
            currState.getCurrentPlayerHand().add(currState.getDiscardPile().get(target));
            //remove the card from the discard pile
            currState.getDiscardPile().remove(target);

            //Sending a message to the log
            Log.d("Log Trade 5", playerNames[currState.getWhoseTurn()] + " " +
                    "traded 5 cards ");

            return true;
        }
    } // trade5

    //increments turn and wraps around from the last player to the first player
    //skips over player if they have an exploding kitten
    public void nextTurn() {
        if(currState.getCardsToDraw() == 0){
            currState.setWhoseTurn((currState.getWhoseTurn()+1)%(currState.getNumPlayers()));
        }
        while (currState.getCurrentPlayerHand().get(0).getCardType() == 0) {
            currState.setWhoseTurn((currState.getWhoseTurn()+1)%(currState.getPlayerHands().size()));
        }
        currState.setCardsToDraw(1);
    }


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
    }

    public EKGameState getCurrState(){
        return this.currState;
    }

}
