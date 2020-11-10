/**
 * Date: 10/20/2020
 * Authors: Chandler Lau, Ka'ulu Ng, Samuel Warrick
 * Version: Project #d Final
 */

package edu.up.cs301.ExplodingKittens.EKActions;


import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class PlayFavorCard extends GameAction {
    private int target;
    private int choice;

    public PlayFavorCard(GamePlayer p, int targetPlayerIdx, int selection){
        super(p);
        this.target = targetPlayerIdx;
        this.choice = selection;
    }

    public int getTarget(){
        return this.target;
    }
    public int getChoice() {return this.choice;}
}
