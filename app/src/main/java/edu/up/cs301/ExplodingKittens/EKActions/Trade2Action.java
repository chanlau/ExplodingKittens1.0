/**
 * Date: 10/20/2020
 * Authors: Chandler Lau, Ka'ulu Ng, Samuel Warrick
 * Version: Project #d Final
 */

package edu.up.cs301.ExplodingKittens.EKActions;

import edu.up.cs301.ExplodingKittens.Player;
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class Trade2Action extends GameAction {
    private GamePlayer targetPlayer;
    private int PosC1, PosC2;

    public Trade2Action(GamePlayer player, GamePlayer target, int card1, int card2){
        super(player);
        this.targetPlayer = target;
        this.PosC1 = card1;
        this.PosC2 = card2;
    }

    //Getter methods for all of the instance variables
    public GamePlayer getTarget(){
        return targetPlayer;
    }
    public int getPosC1(){return PosC1;}
    public int getPosC2(){return PosC2;}
}
