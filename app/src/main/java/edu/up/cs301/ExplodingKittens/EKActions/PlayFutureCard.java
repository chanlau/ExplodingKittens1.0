package edu.up.cs301.ExplodingKittens.EKActions;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class PlayFutureCard extends GameAction {

    /**
     * Basic PlayFutureCard action with the player that takes the action
     * @param p
     *      player that is playing the action
     */
    public PlayFutureCard(GamePlayer p){
        super(p);
    }
}
