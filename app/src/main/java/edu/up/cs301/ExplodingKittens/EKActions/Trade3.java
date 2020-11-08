package edu.up.cs301.ExplodingKittens.EKActions;

import edu.up.cs301.ExplodingKittens.Player;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class Trade3 extends GameAction {
    private Player targetPlayer;
    private int PosC1, PosC2, PosC3, targetCardValue;

    public Trade3(Player p, Player t, int c1, int c2, int c3, int t_card){
        super(p);
        this.targetPlayer = t;
        this.PosC1 = c1;
        this.PosC2 = c2;
        this.PosC3 = c3;
        this.targetCardValue = t_card;
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
