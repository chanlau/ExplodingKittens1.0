package edu.up.cs301.ExplodingKittens.EKActions;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class PlaySkipCard extends GameAction {
    //Basic PlaySkipCard action with
    //the player that takes the action
    public PlaySkipCard(GamePlayer p){
        super(p);
    }
}
