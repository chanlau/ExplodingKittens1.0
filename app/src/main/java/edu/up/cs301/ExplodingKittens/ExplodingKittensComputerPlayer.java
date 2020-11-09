package edu.up.cs301.ExplodingKittens;


import java.util.ArrayList;

import edu.up.cs301.ExplodingKittens.EKActions.DrawCardAction;
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
        if(computerState.getWhoseTurn() != this.playerNum){
            return;
        }
        else if(computerState.getWhoseTurn() == this.playerNum){
            random = (int)(Math.random()*(2-0)+0);
            //if 1 then draw a card, if two then play a card
            if(random == 0){
                DrawCardAction draw = new DrawCardAction(this);
                this.game.sendAction(draw);
            }
            else{
                //go through hand to check for cards to play
                for(int i = 0; i < this.playerHand.size(); i++){
                    switch(this.playerHand.get(i).getCardType()){
                        case 6:
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
