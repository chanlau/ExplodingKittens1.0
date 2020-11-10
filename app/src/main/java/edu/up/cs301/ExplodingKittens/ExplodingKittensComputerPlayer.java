package edu.up.cs301.ExplodingKittens;


import java.util.ArrayList;

import edu.up.cs301.ExplodingKittens.EKActions.DrawCardAction;
import edu.up.cs301.ExplodingKittens.EKActions.PlayAttackCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayFavorCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayFutureCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayNopeCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayShuffleCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlaySkipCard;
import edu.up.cs301.game.GameFramework.GameComputerPlayer;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;

public class ExplodingKittensComputerPlayer extends GameComputerPlayer {

    int random;
    ArrayList<Card> playerHand;

    public ExplodingKittensComputerPlayer(int num, String name) {
        super(num, name);
        this.playerHand = new ArrayList<Card>();

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
        if(computerState.getWhoseTurn() == this.playerNum){
            random = (int)(Math.random()*2);
            //if 1 then draw a card, if two then play a card
            if(random == 0){
                DrawCardAction draw = new DrawCardAction(this);
                this.game.sendAction(draw);
            }
            else if(random == 1){
                //go through hand to check for cards to play
                if(playerHand != null){
                    switch (playerHand.get(0).getCardType()){
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
                            //Find the first player that isn't null and isn't this player
                            for(int i = 0; i < computerState.getPlayers().size(); i++){
                                Player temp = computerState.getPlayers().get(i);
                                if(temp != null && temp.getPlayerNum() != this.playerNum){
                                    holder = i;
                                }
                            }
                            PlayFavorCard favor = new PlayFavorCard(this, computerState.getPlayers().get(holder),0);
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
                    }
                }
                else{
                    DrawCardAction draw = new DrawCardAction(this);
                    this.game.sendAction(draw);
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
