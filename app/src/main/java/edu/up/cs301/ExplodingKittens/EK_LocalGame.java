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

    EKGameState currState;
    //Instance variable representing the previous state of the game
    private EKGameState previousState;

    public EK_LocalGame() {
        currState = new EKGameState(players.length);
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


    @Override
    protected String checkIfGameOver() {
        //See how many players have lost the game
        int playersLost = 0;
       /* for(int i = 0; i < players.length; i++){
            if(currState.getPlayers().get(i).checkForExplodingKitten()){
                playersLost++;
            }
        }
        if(playersLost == (currState.getPlayers().size()-1)) {
            for(int i = 0; i < currState.getPlayers().size(); i++){
                if(!(currState.getPlayers().get(i).checkForExplodingKitten())){
                    return "Congratulations, " + currState.getPlayers().get(i).getPlayerName() + "! You won";
                }
            }

        } */
            return null;
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
     * populateDeck*
     * populateDefuseExplode*
     * makeTestHand
     ***************************************************************************/
    //Attack card
    //current player ends their turn without drawing a card and forces the
    //next player to draw two cards before ending their turn
    public boolean Attack(Player p) {
        int card = checkHand(p, 6);
        //move the card into the discard pile
        currState.getDiscardPile().add(currState.getPlayerHands().get(p.playerNum).get(card));
        currState.getPlayerHands().get(p.playerNum).remove(card);
        //increment cards to draw counter and change turn
        currState.setCardsToDraw(currState.getCardsToDraw()+1);
        nextTurn();
        return true;

    }


    //Nope card
    public boolean Nope(Player p) {
        int card = checkHand(p, 11);
        //move the played nope card to the discard pile and remove it from
        //the players hand
        currState.getDiscardPile().add(currState.getPlayerHands().get(p.playerNum).get(card));
        currState.getPlayerHands().get(p.playerNum).remove(card);
        EKGameState temp = new EKGameState(currState);
        currState = previousState;
        previousState = temp;
        return true;
    }

    //Favor card
    //current player selects a target player and target player gives current
    //player a card of target players choosing
    public boolean Favor(Player p, Player t, int targCard) {
        int card = checkHand(p, 8);
        //copy selected card from target player to current player
        currState.getPlayerHands().get(p.playerNum).add(currState.getPlayerHands().get(t.playerNum).get(card));
        //move the played favor card to the discard pile and remove it from
        // the players hand
        currState.getDiscardPile().add(currState.getPlayerHands().get(p.playerNum).get(targCard));
        currState.getPlayerHands().get(t.playerNum).remove(targCard);
        return true;
    }

    //See the Future card
    //current player looks at the top three cards of the deck
    public boolean SeeTheFuture(Player p) {
        int card = checkHand(p, 10);
        currState.getDiscardPile().add(currState.getPlayerHands().get(p.playerNum).get(card));
        currState.getPlayerHands().get(p.playerNum).remove(card);
        return true;
    }

    //Shuffle card
    //shuffles the deck randomly
    public boolean Shuffle(Player p) {
        /**
         * External Citation
         * Date: 19 October 2020
         * Problem: Was unsure if there was an easy way to shuffle and array
         * list
         * <p>
         * Resource:
         * https://www.java2novice.com/java-collections-and-util/arraylist
         * /shuffle/
         * Solution: Used the example code to shuffle the deck
         */
        //find position of the shuffle card in players hand
        int position = checkHand(p, 7);
        //add the played shuffle card to the discard pile and remove it from
        //the players hand
        currState.getDiscardPile().add(currState.getPlayerHands().get(p.playerNum).get(position));
        currState.getPlayerHands().get(p.playerNum).remove(position);
        //shuffle the deck
        Collections.shuffle(currState.getDeck());

        return true;
    }

    //Skip card
    //current players turn ends without drawing a card
    public boolean Skip(Player p) {
        int card = checkHand(p, 9);
        //finds skip in hand and removes it before incrementing the turn;
        currState.getDiscardPile().add(currState.getPlayerHands().get(p.playerNum).get(card));
        //currState.getPlayers().get(p.getPlayerNum()).getPlayerHand().remove
        // (card);
        //call the nextTurn method to move to the next player
        if(currState.getCardsToDraw() > 1){
            currState.setCardsToDraw(currState.getCardsToDraw()-1);
            return true;
        }

        nextTurn();
        return true;
    }

    ///Play Defuse card
    //if the current player does not have a defuse card, they lose the game,
    //if they do have a defuse card play the defuse card and reshuffle the
    //exploding kitten card back into the deck
    public boolean Defuse(Player p) {
        //check if there is a defuse card in the hand
        int defusePos = checkHand(p, 12);
        int explodePos = checkHand(p, 0);
        if(defusePos != NULL && explodePos != NULL){
            currState.getDiscardPile().add(currState.getPlayerHands().get(p.playerNum).get(defusePos));
            currState.getPlayerHands().get(p.playerNum).remove(defusePos);
            currState.getDeck().add(currState.getPlayerHands().get(p.playerNum).get(explodePos));
            Collections.shuffle(currState.getDeck());
            return true;
        }
        return false;
    }

    //draw a card and end the turn of the player
    public boolean drawCard(Player player) {
        //checks if deck is empty
        if (currState.getDeck().get(0) == null || player == null) {
            return false;
        }
        //add top card of deck to hand and remove it from deck
        currState.getPlayerHands().get(player.playerNum).add(currState.getDeck().get(0));
        currState.getDeck().remove(0);
        currState.setCardsToDraw(currState.getCardsToDraw()-1);

        //Check if the player drew an Exploding Kitten and they can't defuse it, then they lose
        if(currState.getPlayerHands().get(player.playerNum).get(currState.getPlayerHands().get(player.playerNum).size()-1).getCardType() == 0){
            if(!(Defuse(player))){
                currState.setCardsToDraw(1);
                nextTurn();
                return true;
            }
        }
        //Progress the turn count
        if(currState.getCardsToDraw() == 0){
            nextTurn();
            currState.setCardsToDraw(1);
        }
        else{
            drawCard(player);
        }
        return true;
    }

    public boolean trade2(Player play, Player targ, int a, int b) {
        //determine if the two cards are of the same card type
        Card trade1 = currState.getPlayerHands().get(play.playerNum).get(a);
        Card trade2 = currState.getPlayerHands().get(play.playerNum).get(b);
        if (trade1.getCardType() == trade2.getCardType()) {
            //update the players hand
            currState.getPlayerHands().get(play.playerNum).remove(b);
            currState.getPlayerHands().get(play.playerNum).remove(a);
            //copy the new card from the target player into the player hand
            Random rand = new Random();
            int random =
                    rand.nextInt(currState.getPlayerHands().get(targ.playerNum).size() + 1);
            currState.getPlayerHands().get(play.playerNum).add(currState.getPlayerHands().get(targ.playerNum).get(random));
            //remove the target player card that was stolen
            currState.getPlayerHands().get(targ.playerNum).remove(random);
            return true;
        }

        return false;
    }

    public boolean trade3(Player play, Player targ, int a, int b, int c,
                          int targCard) {
        //determine if the three cards are of the same type
        Card trade1 = currState.getPlayerHands().get(play.playerNum).get(a);
        Card trade2 = currState.getPlayerHands().get(play.playerNum).get(b);
        Card trade3 = currState.getPlayerHands().get(play.playerNum).get(c);
        if (trade1.getCardType() == trade2.getCardType() &&
                trade2.getCardType() == trade3.getCardType()) {
            //update the players hand
            currState.getPlayerHands().get(play.playerNum).remove(c);
            currState.getPlayerHands().get(play.playerNum).remove(b);
            currState.getPlayerHands().get(play.playerNum).remove(a);
            //check to see if the target player has the desired card
            for (int i = 0; i < currState.getPlayerHands().get(targ.playerNum).size(); i++) {
                if (targCard == currState.getPlayerHands().get(targ.playerNum).get(i).getCardType()) {
                    //add the desired card to the player hand and remove it
                    // from the target player
                    //hand
                    currState.getPlayerHands().get(play.playerNum).add(currState.getPlayerHands().get(targ.playerNum).get(i));
                    currState.getPlayerHands().get(targ.playerNum).remove(i);
                }
            }
            return true;
        }
        return false;
    }

    //Trade 5 cards
    //current player selects 5 different cards and trades them for a card
    //from the discard pile
    public boolean trade5(Player p, int cardPos1, int cardPos2, int cardPos3,
                          int cardPos4, int cardPos5, int target) {
        //determine if the 5 cards are unique
        int comp1 =
                currState.getPlayerHands().get(p.playerNum).get(cardPos1).getCardType();
        int comp2 = currState.getPlayerHands().get(p.playerNum).get(cardPos2).getCardType();
        int comp3 = currState.getPlayerHands().get(p.playerNum).get(cardPos3).getCardType();
        int comp4 = currState.getPlayerHands().get(p.playerNum).get(cardPos4).getCardType();
        int comp5 = currState.getPlayerHands().get(p.playerNum).get(cardPos5).getCardType();
        if (comp1 == comp2 || comp1 == comp3 || comp1 == comp4 || comp1 == comp5 ||
                comp2 == comp3 || comp2 == comp4 || comp2 == comp5 ||
                comp3 == comp4 || comp3 == comp5 ||
                comp4 == comp5) {
            //update the players hand
            currState.getPlayerHands().get(p.playerNum).remove(cardPos5);
            currState.getPlayerHands().get(p.playerNum).remove(cardPos4);
            currState.getPlayerHands().get(p.playerNum).remove(cardPos3);
            currState.getPlayerHands().get(p.playerNum).remove(cardPos2);
            currState.getPlayerHands().get(p.playerNum).remove(cardPos1);
            //copy the desired card to the players hand
            currState.getPlayerHands().get(p.playerNum).add(currState.getDiscardPile().get(target));
            //remove the card from the discard pile
            currState.getDiscardPile().remove(target);
        }
        return false;
    }


    //increments turn
    public void nextTurn() {
        currState.setWhoseTurn((currState.getWhoseTurn()+1)%(currState.getNumPlayers()));

        /*
        while (currState.getPlayers().get(currState.getWhoseTurn()).checkForExplodingKitten()) {
            currState.setWhoseTurn(currState.getWhoseTurn()+1);
        }
         */
    }


    //check for the card
    public int checkHand(Player p, int card) {
        //check to see if the card type exists in the players hand, if it
        // does return the position of the card
        for (int i = 0; i < currState.getPlayerHands().get(p.playerNum).size(); i++) {
            if (currState.getPlayerHands().get(p.playerNum).get(i).getCardType() == card) {
                return i;
            }
        }
        return NULL;
    }

/*

    //adds defuse and explode cards to deck
    public void populateDefuseExplode() {
        int i;
        int j;
        //puts 3 Exploding Kittens into deck
        for (i = 0; i < 3; i++) {
            currState.getDeck().add(new Card(0));
        }

        //Puts 2 defuse into deck
        for (i = 0; i < 2; i++) {
            currState.getDeck().add(new Card(12));
        }
    }


//sets all players hands to be able to do each action once

    public void makeTestHand() {
        int i, j;
        for (i = 0; i < players.length; i++) {
            //puts 3 tacocats in hand
            for (j = 0; j < 3; j++) {
            }
            //puts 2 beardcats in hand
            for (j = 0; j < 2; j++) {
                currState.getPlayers().get(i).getPlayerHand().add(new Card(2));
            }
            //puts one of every card in hand
            for (j = 1; j <= 12; j++) {
                currState.getPlayers().get(i).getPlayerHand().add(new Card(j));
            }
        }

    }

    */

    public EKGameState getCurrState(){
        return this.currState;
    }

}
