/**
 * Date: 10/20/2020
 * Authors: Chandler Lau, Ka'ulu Ng, Samuel Warrick
 * Version: Project #d Final
 */

package edu.up.cs301.ExplodingKittens.EKActions;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class Trade2Action extends GameAction {

    /*
    player: player that made the action
    targetPlayerIdx: index in the playersHands array that is the target of the action
    PosC1: position of the first card in the player's hand that is being traded
    PosC2: position of the second card in teh player's hand that is being traded
     */
    private int targetPlayerIdx;
    private int PosC1, PosC2;

    /**
     * Basic Trade2Action
     * @param player
     *      player that made the action
     * @param target
     *      index in the playersHands array that is the target of the action
     * @param card1
     *      position of the first card in the player's hand that is being traded
     * @param card2
     *      position of the second card in teh player's hand that is being traded
     */
    public Trade2Action(GamePlayer player, int target, int card1, int card2){
        super(player);
        this.targetPlayerIdx = target;
        this.PosC1 = card1;
        this.PosC2 = card2;
    }

    //Getter methods for all of the instance variables
    public int getTarget(){
        return targetPlayerIdx;
    }
    public int getPosC1(){return PosC1;}
    public int getPosC2(){return PosC2;}
}
