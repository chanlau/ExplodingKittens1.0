package edu.up.cs301.ExplodingKittens;

import android.graphics.Color;
import android.view.View;


import java.util.ArrayList;

import edu.up.cs301.game.GameFramework.GameHumanPlayer;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.R;

public class ExplodingKittensHumanPlayer extends GameHumanPlayer {

    private EKGameState state;
    ArrayList<Card> playerHand;

    private GameMainActivity myActivity;

    /*

     */

    /**
     * constructor
     * @param num
     * @param name
     *      number corresponding to this player
     *      name corresponding to  this player
     */
    public ExplodingKittensHumanPlayer(int num, String name) {
        super(num, name);
        this.playerHand = new ArrayList<Card>();

    }

    //This returns the top level surface view of main GUI
    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }

    /**
     * Receives information (hopefully the gamestate) and decides the best
     * actions from there
     * @param info
     *     information, presumably the gamestate
     */
    public void receiveInfo(GameInfo info) {
        //indicates which card is being shown in hand first
        int firstInHand = 0;

        if(info instanceof EKGameState == false){
            flash(Color.RED, 500);
            return;
        }
        else{
            EKGameState currentState = (EKGameState) info;
            //this switch statement returns card type for printing
            //set up a for loop to update these guys
            //incomplete cuz sams working on it i think
            switch (currentState.getPlayers().get(this.playerNum).getPlayerHand().get(firstInHand).getCardType()){

            };
        }
    }

    /**
     * Updates the GUI with new information after actions are taken
     */
    public void updateDisplay() {

    }

    public void onClick() {

    }

    public void setAsGui(GameMainActivity activity) {
        myActivity = activity;
        //load the layout for GUI
        activity.setContentView(R.layout.activity_main);

        //Initialize widgets
    }

}
