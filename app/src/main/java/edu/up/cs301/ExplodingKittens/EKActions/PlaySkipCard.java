package edu.up.cs301.ExplodingKittens.EKActions;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class PlaySkipCard extends GameAction {

    /**
     * Basic PlaySkipCard action with the player that takes the action
     * @param p
     *      GamePlayer object for the player playing the card
     */
    public PlaySkipCard(GamePlayer p){
        super(p);
    }
}
