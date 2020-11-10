package edu.up.cs301.ExplodingKittens.EKActions;

import edu.up.cs301.ExplodingKittens.Player;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class Trade3Action extends GameAction {
    private Player targetPlayer;
    private int PosC1, PosC2, PosC3, targetCardValue;

    public Trade3Action(Player player, Player target, int card1, int card2, int card3, int targetCard){
        super(player);
        this.targetPlayer = target;
        this.PosC1 = card1;
        this.PosC2 = card2;
        this.PosC3 = card3;
        this.targetCardValue = targetCard;
    }

    //Getter methods for all of the instance variables
    public Player getTarget(){
        return targetPlayer;
    }
    public int getPosC1(){return PosC1;}
    public int getPosC2(){return PosC2;}
    public int getPosC3(){return PosC3;}
    public int getTargetValue(){return targetCardValue;}

}