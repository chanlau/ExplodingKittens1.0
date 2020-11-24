/**
 * Date: 10/20/2020
 * Authors: Chandler Lau, Ka'ulu Ng, Samuel Warrick
 * Version: Project #d Final
 */

package edu.up.cs301.ExplodingKittens.EKActions;

import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class PlayShuffleCard extends GameAction {
    //Basic PlayShuffleCard action with
    //the player that takes the action
    public PlayShuffleCard(GamePlayer p){
        super(p);
    }
}
