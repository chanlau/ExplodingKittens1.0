/**
 * Date: 10/20/2020
 * Authors: Chandler Lau, Ka'ulu Ng, Samuel Warrick
 * Version: Project #d Final
 */

package edu.up.cs301.ExplodingKittens.EKActions;


import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class PlayNopeCard extends GameAction {

    /**
     * Basic PlayNopeCard action with the player who takes the action
     * @param player
     *      player who is playing the action
     */
    public PlayNopeCard(GamePlayer player){
        super(player);
    }
}
