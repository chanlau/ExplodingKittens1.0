package edu.up.cs301.ExplodingKittens.EKActions;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class Trade5Action extends GameAction {
    private int PosC1, PosC2, PosC3, PosC4, PosC5, targetCardValue;

    public Trade5Action(GamePlayer player, int card1, int card2, int card3, int card4, int card5, int targetValue) {
        super(player);
        this.PosC1 = card1;
        this.PosC2 = card2;
        this.PosC3 = card3;
        this.PosC4 = card4;
        this.PosC5 = card5;
        this.targetCardValue = targetValue;
    }

    public int getPosC1(){return PosC1;}
    public int getPosC2(){return PosC2;}
    public int getPosC3(){return PosC3;}
    public int getPosC4(){return PosC4;}
    public int getPosC5(){return PosC5;}
    public int getTargetValue(){return targetCardValue;}
}
