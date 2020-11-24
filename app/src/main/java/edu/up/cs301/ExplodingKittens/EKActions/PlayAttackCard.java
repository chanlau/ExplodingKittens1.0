package edu.up.cs301.ExplodingKittens.EKActions;


import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class PlayAttackCard extends GameAction {
    //Basic PlayAttackCardA action with a player who made the action
    public PlayAttackCard(GamePlayer p) {
        super(p);
    }

}
