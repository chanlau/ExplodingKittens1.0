package edu.up.cs301.ExplodingKittens;

import edu.up.cs301.ExplodingKittens.EKActions.DrawCardAction;
import edu.up.cs301.game.GameFramework.GameComputerPlayer;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;

public class ExplodingKittensSmartComputerPlayer extends GameComputerPlayer {

    int probability;
    int random;

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public ExplodingKittensSmartComputerPlayer(String name) {
        super(name);
    }

    @Override
    protected void receiveInfo(GameInfo info) {

        if(!(info instanceof EKGameState)){ return;}
        EKGameState computerState = (EKGameState) info;

        //check to see if it's this player's turn
        if(computerState.getWhoseTurn() != this.playerNum){
            return;
        }

        if(computerState.getWhoseTurn() == this.playerNum) {
            random = (int) Math.random()*101;
            //TODO: create method to say how many players left in game
            probability =
                    (computerState.getDeck().size()/computerState.getEKCount())*100;

            if (random <= probability){
                //play a card: Skip, Attack, STF -> Shuffle
                //If none, try to get a card (Trade 2, 3, 5, or favor)
                // (Prioritize Defuse)
                //else draw a card
                int cardToPlay;
                if(computerState.getCurrentPlayerHand() != null){
                    for(int i = 0; i < computerState.getCurrentPlayerHand().size(); i++){
                        switch(computerState.getCurrentPlayerHand().get(i).getCardType()){
                            //attack card
                            case 6:
                                break;
                            //favor
                            case 8:
                                break;
                            //skip
                            case 9:
                                break;
                            //see the future
                            case 10:
                                break;
                        }
                    }
                }
            }
            else{
                //draw a card
                DrawCardAction draw = new DrawCardAction(this);
                this.game.sendAction(draw);
            }

        }

    }
}
