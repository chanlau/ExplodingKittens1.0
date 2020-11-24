package edu.up.cs301.ExplodingKittens.EKActions;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class Trade3Action extends GameAction {

    /*Basic Trade3Action
    player: player that made the action
    targetPlayerIdx: index in the playersHands array that is the target of the action
    PosC1: position of the first card in the player's hand that is being traded
    PosC2: position of the second card in the player's hand that is being traded
    PosC3: position of the third card in the player's hand that is being traded
    targetCardValue: value of the card that the player is targeting
     */
    private int targetPlayerIdx;
    private int PosC1, PosC2, PosC3, targetCardValue;

    public Trade3Action(GamePlayer player, int target, int card1, int card2, int card3, int targetCard){
        super(player);
        this.targetPlayerIdx = target;
        this.PosC1 = card1;
        this.PosC2 = card2;
        this.PosC3 = card3;
        this.targetCardValue = targetCard;
    }

    //Getter methods for all of the instance variables
    public int getTarget(){
        return targetPlayerIdx;
    }
    public int getPosC1(){return PosC1;}
    public int getPosC2(){return PosC2;}
    public int getPosC3(){return PosC3;}
    public int getTargetValue(){return targetCardValue;}

}
