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

public class ExplodingKittensComputerPlayer extends GameComputerPlayer {

    int random;

    public ExplodingKittensComputerPlayer(String name) {
        super(name);

    }

    /**
     * Receives information (hopefully the gamestate) and decides the best
     * actions from there
     * @param info
     *     information, presumably the gamestate
     */
    @Override
    protected void receiveInfo(GameInfo info) {

        EKGameState computerState = (EKGameState) info;
        //check to see if it's this player's turn
        if(computerState.getWhoseTurn() != this.playerNum){
            return;
        }
        if(computerState.getWhoseTurn() == this.playerNum){
            random = (int)(Math.random()*3);
            //if 0 then draw a card
            if(random == 0){
                DrawCardAction draw = new DrawCardAction(this);
                this.game.sendAction(draw);
            }
            //if 1 then play a card
            else if(random == 1){
                if(computerState.getPlayerHand(this.playerNum) != null){
                    //Play the first card in your hand and send the appropriate action
                    switch (computerState.getPlayerHand(this.playerNum).get(0).getCardType()){
                        case 6:
                            PlayAttackCard attack = new PlayAttackCard(this);
                            this.game.sendAction(attack);
                            break;
                        case 7:
                            PlayShuffleCard shuffle = new PlayShuffleCard(this);
                            this.game.sendAction(shuffle);
                            break;
                        case 8:
                            int holder = 0;
                            //Find the first player hand that isn't null and isn't this player's hand
                            for(int i = 0; i < computerState.getNumPlayers(); i++){
                                if(computerState.getPlayerHands().get(i) != null && i != this.playerNum){
                                    holder = i;
                                    break;
                                }
                            }
                            int randomCardPos = (int)(Math.random()*computerState.getPlayerHand(holder).size());
                            PlayFavorCard favor = new PlayFavorCard(this, holder, randomCardPos);
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
                        case 11:
                            PlayNopeCard nope = new PlayNopeCard(this);
                            this.game.sendAction(nope);
                            break;
                        default:
                            DrawCardAction draw = new DrawCardAction(this);
                            this.game.sendAction(draw);
                    }
                }
                else{
                    DrawCardAction draw = new DrawCardAction(this);
                    this.game.sendAction(draw);
                }
            }
            //If 2, do one of the trade actions based on another random number
            else if(random == 2){
                int decider = (int)(Math.random()*3);
                int playerSelected = 0;
                //Find the first player hand that isn't null and isn't this player's hand
                for(int i = 0; i < computerState.getNumPlayers(); i++){
                    if(computerState.getPlayerHands().get(i) != null && i != this.playerNum){
                        playerSelected = i;
                        break;
                    }
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
                        int cardSelected = (int) ((Math.random() * 12) + 1);
                        Trade5Action trade5 = new Trade5Action(this, 0, 1, 2, 3, 4, cardSelected);
                        this.game.sendAction(trade5);
                    }
                    else {
                        DrawCardAction draw = new DrawCardAction(this);
                        this.game.sendAction(draw);
                    }
                }
            }
        }


    }

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
