package edu.up.cs301.ExplodingKittens.EKActions;

import edu.up.cs301.ExplodingKittens.Player;
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class PlayDefuseCard extends GameAction {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public PlayDefuseCard(GamePlayer player) {
        super(player);
    }
}
