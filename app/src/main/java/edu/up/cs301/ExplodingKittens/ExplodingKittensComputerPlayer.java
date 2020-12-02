package edu.up.cs301.ExplodingKittens;


import java.util.ArrayList;

import edu.up.cs301.ExplodingKittens.EKActions.DrawCardAction;
import edu.up.cs301.ExplodingKittens.EKActions.PlayAttackCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayFavorCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayFutureCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayNopeCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayShuffleCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlaySkipCard;
import edu.up.cs301.ExplodingKittens.EKActions.Trade2Action;
import edu.up.cs301.ExplodingKittens.EKActions.Trade3Action;
import edu.up.cs301.ExplodingKittens.EKActions.Trade5Action;
import edu.up.cs301.game.GameFramework.GameComputerPlayer;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;

/**
 * AI  dumb computer player
 *
 * @authors Chandler Lau, Ka'ulu Ng, Samuel Warrick
 * @version 11/25/2020
 */

public class ExplodingKittensComputerPlayer extends GameComputerPlayer {


    public ExplodingKittensComputerPlayer(String name) {
        super(name);

    }

    /**
     * Receives information (gamestate) and decides the best
     * actions from there
     * @param info
     * recieves info sent to this player class
     * and makes a random move, either it will draw,
     * play a card, or try and trade a card.
     * If any of the actions fails, then it will draw a card
     */
    @Override
    protected void receiveInfo(GameInfo info) throws InterruptedException {
        //Making sure that the info variable is
        //able to be type casted into a EKGameState
        if(!(info instanceof EKGameState)){ return;}
        EKGameState computerState = (EKGameState) info;

        if(!computerState.hasPlayerLost(0)) {
            Thread.sleep(700);
        }
        else{
            Thread.sleep(300);
        }

        //check to see if it's this player's turn
        if(computerState.getWhoseTurn() != this.playerNum){
            return;
        }
        if(computerState.getWhoseTurn() == this.playerNum){
            //Generates a number between 0 and 9 to determine
            //what the computer will do
            // 40% chance to draw a card
            // 40% to play a card
            // 20% chance to do a trade action
            // and will draw if it fails
            int random = (int)(Math.random()*10);

            //if random is 0,1,2,3 then draw a card
            if(random < 4){
                DrawCardAction draw = new DrawCardAction(this);
                this.game.sendAction(draw);
            }
            //if 4,5,6,7 then play a card
            else if(random < 8){
                //Checks to see if the computer's hand is empty
                //if it is, then the computer can't play a card
                if(computerState.getCurrentPlayerHand() != null){
                    //Play the first functional card in your hand and send the appropriate action
                    //It sets the position of that card to playThisCard
                    int playThisCard = 0;
                    for(int i = 0; i < computerState.getCurrentPlayerHand().size(); i++){
                        if(computerState.getCurrentPlayerHand().get(i).getCardType() >= 6 &&
                                computerState.getCurrentPlayerHand().get(i).getCardType() != 12){
                            playThisCard = i;
                            break;
                        }
                    }
                    //Check to see which card was selected by the card selection process
                    switch (computerState.getCurrentPlayerHand().get(playThisCard).getCardType()){
                        case 6:
                            PlayAttackCard attack = new PlayAttackCard(this);
                            this.game.sendAction(attack);
                            break;
                        case 7:
                            PlayShuffleCard shuffle = new PlayShuffleCard(this);
                            this.game.sendAction(shuffle);
                            break;
                        case 8:
                            //Finds a random player that is the game and sets the index of that
                            //player to the targetPlayer variable
                            int targetPlayer = (int)(Math.random()*computerState.getNumPlayers());
                            if(computerState.hasPlayerLost(targetPlayer) || targetPlayer == this.playerNum){
                                while(computerState.hasPlayerLost(targetPlayer) || targetPlayer == this.playerNum){
                                    targetPlayer = (int)(Math.random()*computerState.getNumPlayers());
                                }
                            }
                            //Selects a random card from the target player's hand
                            int randomCardPos = (int)(Math.random()*computerState.getPlayerHand(targetPlayer).size());
                            PlayFavorCard favor = new PlayFavorCard(this, targetPlayer, randomCardPos);
                            this.game.sendAction(favor);
                            break;
                        case 9:
                            PlaySkipCard skip = new PlaySkipCard(this);
                            this.game.sendAction(skip);
                            break;
                        case 10:
                            PlayFutureCard future = new PlayFutureCard(this);
                            this.game.sendAction(future);
                            break;
                        default:
                            DrawCardAction draw = new DrawCardAction(this);
                            this.game.sendAction(draw);
                    }
                }
                else{
                    //If this player's hand is null then draw
                    DrawCardAction draw = new DrawCardAction(this);
                    this.game.sendAction(draw);
                }
            }
            //If 8 or 9 do one of the trade actions based on another random number
            else {
                //Decide which trade action to do randomly
                int decider = (int)(Math.random()*3);
                //Finds a random player that is the game and sets the index of that
                //player to the targetPlayer variable
                int playerSelected = (int)(Math.random()*computerState.getNumPlayers());
                    while(computerState.hasPlayerLost(playerSelected) || playerSelected == this.playerNum){
                        playerSelected = (int)(Math.random()*computerState.getNumPlayers());
                    }
                if(decider == 0) {
                    //Make a Trade2Action and draw if you can't
                    if (computerState.getPlayerHand(this.playerNum).size() >= 2) {
                        Trade2Action trade2 = new Trade2Action(this, playerSelected, 0, 1);
                        this.game.sendAction(trade2);
                    }
                    else {
                        DrawCardAction draw = new DrawCardAction(this);
                        this.game.sendAction(draw);
                    }
                }
                else if(decider == 1){
                    //Make a Trade3Action and draw if you can't
                    if(computerState.getPlayerHand(this.playerNum).size()  >= 3){
                        int cardSelected = (int)((Math.random()*12)+1);
                        Trade3Action trade3 = new Trade3Action(this, playerSelected,
                                0,1,2,cardSelected);
                        this.game.sendAction(trade3);
                    }
                    else {
                        DrawCardAction draw = new DrawCardAction(this);
                        this.game.sendAction(draw);
                    }
                }
                else if(decider == 2){
                    //Make a Trade5Action and draw if you can't
                    if(computerState.getPlayerHand(this.playerNum).size() >= 5) {
                        //Select a random card from the discard pile
                        int cardDesired = (int)(Math.random()*computerState.getDiscardPile().size());

                        if(computerState.getCurrentPlayerHand().size() >= 5) {
                            Trade5Action trade5 = new Trade5Action(this, 0, 1, 2, 3, 4, cardDesired);
                            this.game.sendAction(trade5);
                        }
                    }
                    else {
                        DrawCardAction draw = new DrawCardAction(this);
                        this.game.sendAction(draw);
                    }
                }
            }
        }
    }//receiveInfo

    /**
     * Update the display with actions that the computer player is taking.
     * Do this so that the human player can follow along and understand what
     * the computer players are doing
     */
    public void updateDisplay() {

    }

    @Override
    public void setAsGui(GameMainActivity a) {

    }

}
