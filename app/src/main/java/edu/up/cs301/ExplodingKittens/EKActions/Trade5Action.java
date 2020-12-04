package edu.up.cs301.ExplodingKittens.EKActions;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class Trade5Action extends GameAction {
    /*
    player: player that made the action
    PosC1: position of the first card in the player's hand that is being traded
    PosC2: position of the second card in the player's hand that is being traded
    PosC3: position of the third card in the player's hand that is being traded
    PosC4: position of the fourth card in the player's hand that is being traded
    PosC5: position of the fifth card in the player's hand that is being traded
    targetCardValue: position of the card that the player is targeting in the discard pile
     */
    private int PosC1, PosC2, PosC3, PosC4, PosC5, targetCardPos;

    /**
     * Basic Trade5Action
     * @param player
     *      player that made the action
     * @param card1
     *      position of the first card in the player's hand that is being traded
     * @param card2
     *      position of the second card in the player's hand that is being traded
     * @param card3
     *      position of the third card in the player's hand that is being traded
     * @param card4
     *      position of the fourth card in the player's hand that is being traded
     * @param card5
     *      position of the fifth card in the player's hand that is being traded
     * @param targetPos
     *      position of the card that the player is targeting in the discard pile
     */
    public Trade5Action(GamePlayer player, int card1, int card2, int card3, int card4, int card5, int targetPos) {
        super(player);
        this.PosC1 = card1;
        this.PosC2 = card2;
        this.PosC3 = card3;
        this.PosC4 = card4;
        this.PosC5 = card5;
        this.targetCardPos = targetPos;
    }

    public int getPosC1(){return PosC1;}
    public int getPosC2(){return PosC2;}
    public int getPosC3(){return PosC3;}
    public int getPosC4(){return PosC4;}
    public int getPosC5(){return PosC5;}
    public int getTargetValue(){return targetCardPos;}
}
