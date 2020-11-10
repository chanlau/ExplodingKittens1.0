package edu.up.cs301.ExplodingKittens;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import edu.up.cs301.ExplodingKittens.EKActions.DrawCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayAttackCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayFavorCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayFutureCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayNopeCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayShuffleCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlaySkipCard;
import edu.up.cs301.ExplodingKittens.EKActions.Trade2;
import edu.up.cs301.ExplodingKittens.EKActions.Trade3;
import edu.up.cs301.ExplodingKittens.EKActions.Trade5;
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;
import edu.up.cs301.game.GameFramework.infoMessage.GameState;

import static java.sql.Types.NULL;

public class EK_LocalGame extends LocalGame {

    EKGameState currState;

    private ArrayList<Card> discardPile;
    private ArrayList<Card> deck;
    private ArrayList<Player> players;
    private int whoseTurn;
    private int cardsToDraw;

    public EK_LocalGame() {
        currState = new EKGameState();
        this.discardPile = new ArrayList<Card>();
        this.deck = new ArrayList<Card>();
        this.players = new ArrayList<Player>();
        this.whoseTurn = 1;
        this.cardsToDraw = 1;
    }

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {

    }

    @Override
    protected boolean canMove(int playerIdx) {
        if(playerIdx == currState.getWhoseTurn()){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    protected boolean makeMove(GameAction action) {
        //check which action is being taken
        if (action instanceof DrawCard) {
            return drawCard(action.getPlayer());
        } else if (action instanceof PlayNopeCard) {
            return Nope(action.getPlayer());
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
        } else if (action instanceof PlayFutureCard){
            return SeeTheFuture(action.getPlayer()) ;
        } else if (action instanceof Trade2) {
            return trade2(action.getPlayer(), ((Trade2) action).getTarget(),
                    ((Trade2) action).getPosC1(), ((Trade2) action).getPosC2());
        } else if (action instanceof Trade3) {
            return trade3(action.getPlayer(), ((Trade3) action).getTarget(),
                    ((Trade3) action).getPosC1(),
                    ((Trade3) action).getPosC2(),
                    ((Trade3) action).getPosC3(),
                    ((Trade3) action).getTargetValue());
        } else if (action instanceof Trade5) {
            return trade5(action.getPlayer(), ((Trade5) action).getPosC1(),
                    ((Trade5) action).getPosC2(),
                    ((Trade5) action).getPosC3(),
                    ((Trade5) action).getPosC4(), ((Trade5) action).getPosC5(),
                    ((Trade5) action).getTargetValue());
        }
        //error message
        else {
            Log.d("Invalid Action",
                    "Action provided was an invalid action");
        }
        return false;
    }


    @Override
    protected String checkIfGameOver() {
        return null;
    }


    /****************************************************************************
     * Card Methods
     *************************************************************************/
    //Attack card
    //current player ends their turn without drawing a card and forces the
    //next player to draw two cards before ending their turn
    public boolean Attack(Player p) {
        int card = checkHand(p, 6);
        //move the card into the discard pile
        currState.getDiscardPile().add(p.getPlayerHand().get(card));
        p.getPlayerHand().remove(card);
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
        currState.getDiscardPile().add(p.getPlayerHand().get(card));
        p.getPlayerHand().remove(card);
        return false;
    }

    //Favor card
    //current player selects a target player and target player gives current
    //player a card of target players choosing
    public boolean Favor(Player p, Player t, int targCard) {
        int card = checkHand(p, 8);
        //copy selected card from target player to current player
        p.getPlayerHand().add(t.getPlayerHand().get(card));
        //move the played favor card to the discard pile and remove it from
        // the players hand
        currState.getDiscardPile().add(p.getPlayerHand().get(targCard));
        t.getPlayerHand().remove(targCard);
        return true;
    }

    //See the Future card
    //current player looks at the top three cards of the deck
    public boolean SeeTheFuture(Player p) {
        int card = checkHand(p, 10);
        currState.getDiscardPile().add(p.getPlayerHand().get(card));
        p.getPlayerHand().remove(card);
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
        currState.getDiscardPile().add(p.getPlayerHand().get(position));
        p.getPlayerHand().remove(position);
        //shuffle the deck
        Collections.shuffle(currState.getDeck());

        return true;
    }

    //Skip card
    //current players turn ends without drawing a card
    public boolean Skip(Player p) {
        int card = checkHand(p, 9);
        //finds skip in hand and removes it before incrementing the turn;
        currState.getDiscardPile().add(p.getPlayerHand().get(card));
        currState.getPlayers().get(p.getPlayerNum()).getPlayerHand().remove(card);
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
            currState.getDiscardPile().add(p.getPlayerHand().get(defusePos));
            p.getPlayerHand().remove(defusePos);
            currState.getDeck().add(p.getPlayerHand().get(explodePos));
            Collections.shuffle(currState.getDeck());
            return true;
        }
        currState.removePlayer(p);
        return false;
    }

    //draw a card and end the turn of the player
    public boolean drawCard(Player player) {
        //checks if deck is empty
        if (currState.getDeck().get(0) == null) {
            return false;
        }
        //add top card of deck to hand and remove it from deck
        player.getPlayerHand().add(currState.getDeck().get(0));
        currState.getDeck().remove(0);
        currState.setCardsToDraw(currState.getCardsToDraw()-1);
        //alternate turn
        if(currState.getCardsToDraw() == 0){
            nextTurn();
            currState.setCardsToDraw(1);
        }
        return true;
    }

    public boolean trade2(Player play, Player targ, int a, int b) {
        //determine if the two cards are of the same card type
        Card trade1 = play.getPlayerHand().get(a);
        Card trade2 = play.getPlayerHand().get(b);
        if (trade1.getCardType() == trade2.getCardType()) {
            //update the players hand
            play.getPlayerHand().remove(b);
            play.getPlayerHand().remove(a);
            //copy the new card from the target player into the player hand
            Random rand = new Random();
            int random = rand.nextInt(targ.getPlayerHand().size() + 1);
            play.getPlayerHand().add(targ.getPlayerHand().get(random));
            //remove the target player card that was stolen
            targ.getPlayerHand().remove(random);
            return true;
        }

        return false;
    }

    public boolean trade3(Player play, Player targ, int a, int b, int c,
                          int targCard) {
        //determine if the three cards are of the same type
        Card trade1 = play.getPlayerHand().get(a);
        Card trade2 = play.getPlayerHand().get(b);
        Card trade3 = play.getPlayerHand().get(c);
        if (trade1.getCardType() == trade2.getCardType() &&
                trade2.getCardType() == trade3.getCardType()) {
            //update the players hand
            play.getPlayerHand().remove(c);
            play.getPlayerHand().remove(b);
            play.getPlayerHand().remove(a);
            //check to see if the target player has the desired card
            for (int i = 0; i < targ.getPlayerHand().size(); i++) {
                if (targCard == targ.getPlayerHand().get(i).getCardType()) {
                    //add the desired card to the player hand and remove it
                    // from the target player
                    //hand
                    play.getPlayerHand().add(targ.getPlayerHand().get(i));
                    targ.getPlayerHand().remove(i);
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
        int comp1 = p.getPlayerHand().get(cardPos1).getCardType();
        int comp2 = p.getPlayerHand().get(cardPos2).getCardType();
        int comp3 = p.getPlayerHand().get(cardPos3).getCardType();
        int comp4 = p.getPlayerHand().get(cardPos4).getCardType();
        int comp5 = p.getPlayerHand().get(cardPos5).getCardType();
        if (comp1 == comp2 || comp1 == comp3 || comp1 == comp4 || comp1 == comp5 ||
                comp2 == comp3 || comp2 == comp4 || comp2 == comp5 ||
                comp3 == comp4 || comp3 == comp5 ||
                comp4 == comp5) {
            //update the players hand
            p.getPlayerHand().remove(cardPos5);
            p.getPlayerHand().remove(cardPos4);
            p.getPlayerHand().remove(cardPos3);
            p.getPlayerHand().remove(cardPos2);
            p.getPlayerHand().remove(cardPos1);
            //copy the desired card to the players hand
            p.getPlayerHand().add(currState.getDiscardPile().get(target));
            //remove the card from the discard pile
            currState.getDiscardPile().remove(target);
        }
        return false;
    }

    //increments turn
    public void nextTurn() {
        currState.setWhoseTurn(currState.getWhoseTurn()+1);
        while (currState.getPlayers().get(currState.getWhoseTurn()).checkForExplodingKitten()) {
            currState.setWhoseTurn(currState.getWhoseTurn()+1);
        }
    }


    //check for the card
    public int checkHand(Player p, int card) {
        //check to see if the card type exists in the players hand, if it
        // does return the position of the card
        for (int i = 0; i < p.getPlayerHand().size(); i++) {
            if (p.getPlayerHand().get(i).getCardType() == card) {
                return i;
            }
        }
        return NULL;
    }

    //restart the deck
    public void populateDeck() {
        int i;
        int j;
        //puts 4 of each cat card, attack, shuffle, favor, skip cards
        for (i = 1; i <= 9; i++) {
            for (j = 0; j < 4; j++) {
                currState.getDeck().add(new Card(i));
            }
        }
        // puts 5 See the Future and Nope Cards into deck
        for (i = 10; i <= 11; i++) {
            for (j = 0; j < 5; j++) {
                currState.getDeck().add(new Card(i));
            }
        }

    }

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

    //adds appropriate amt. of cards to all players hands
    public void populateHands() {
        int i, j;
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 7; j++) {
                drawCard(currState.getPlayers().get(i));
            }
            currState.getPlayers().get(i).getPlayerHand().add(new Card(12));
        }
    }

//sets all players hands to be able to do each action once

    public void makeTestHand() {
        int i, j;
        for (i = 0; i < currState.getPlayers().size(); i++) {
            //puts 3 tacocats in hand
            for (j = 0; j < 3; j++) {
                currState.getPlayers().get(i).getPlayerHand().add(new Card(1));
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

    public EKGameState getCurrState(){
        return this.currState;
    }

}
