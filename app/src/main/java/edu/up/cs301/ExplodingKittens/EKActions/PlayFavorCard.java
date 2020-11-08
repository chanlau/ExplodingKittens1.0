/**
 * Date: 10/20/2020
 * Authors: Chandler Lau, Ka'ulu Ng, Samuel Warrick
 * Version: Project #d Final
 */

package edu.up.cs301.ExplodingKittens.EKActions;


import edu.up.cs301.ExplodingKittens.Player;
import edu.up.cs301.game.GameFramework.actionMessage.GameAction;

public class PlayFavorCard extends GameAction {
    private Player target;
    private int choice;

    public PlayFavorCard(Player p, Player t, int c){
        super(p);
        this.target = t;
        this.choice = c;
    }

    public Player getTarget(){
        return this.target;
    }
    public int getChoice() {return this.choice;}
}
