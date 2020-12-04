package edu.up.cs301.ExplodingKittens.EKActions;


import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class DrawCardAction extends GameAction {

    /**
     * Basic DrawCardAction
     * @param p
     *      player who made the action
     */
     public DrawCardAction(GamePlayer p) {
        super(p);
    }
}
