package edu.up.cs301.ExplodingKittens;

import android.graphics.Color;
import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import java.util.ArrayList;

import edu.up.cs301.game.GameFramework.GameHumanPlayer;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.R;

public class ExplodingKittensHumanPlayer extends GameHumanPlayer implements View.OnClickListener {

    private EKGameState state;
    ArrayList<Card> playerHand;

    private GameMainActivity myActivity;

    private int cardHand[] = new int[5];
    private ImageButton card1 = null;
    private ImageButton card2 = null;
    private ImageButton card3 = null;
    private ImageButton card4 = null;
    private ImageButton card5 = null;
    private ImageButton imagesHand[] = new ImageButton[5];
    private ImageButton discardPileBtn = null;
    private boolean switchedDiscard = false;

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
        for (int i = 0; i < 5; i++) {
            cardHand[i] = i;
        }
        //set the image buttons for the players hand to their
        //card hand to begin the game (5 left most cards)
        /**for (int i = 0; i < 5; i++) {
         switch (this.playerHand.get(i).getCardType()) {
         case 0:
         cardHand[i] = 0;
         break;
         case 1:
         cardHand[i] = 1;
         break;
         case 2:
         cardHand[i] = 2;
         break;
         case 3:
         cardHand[i] = 3;
         break;
         case 4:
         cardHand[i] = 4;
         break;
         case 5:
         cardHand[i] = 5;
         break;
         case 6:
         cardHand[i] = 6;
         break;
         case 7:
         cardHand[i] = 7;
         break;
         case 8:
         cardHand[i] = 8;
         break;
         case 9:
         cardHand[i] = 9;
         break;
         case 10:
         cardHand[i] = 10;
         break;
         case 11:
         cardHand[i] = 11;
         break;
         case 12:
         cardHand[i] = 12;
         break;
         default:
         break;
         } //switch statement
         } //for loop*/
    } //ExplodingKittensHumanPlayer class

    //This returns the top level surface view of main GUI
    public View getTopView() {
        return myActivity.findViewById(R.id.topGUI);
    }

    /**
     * Receives information (hopefully the gamestate) and decides the best
     * actions from there
     * @param info
     *     information, presumably the gamestate
     */
    public void receiveInfo(GameInfo info) {

        if(info instanceof EKGameState == false){
            flash(Color.RED, 500);
            return;
        }
        else{
            updateDisplay();
            }
        }

    /**
     * Updates the GUI with new information after actions are taken
     */
    public void updateDisplay() {
        for (int i = 0; i < 5; i++) {
            switch (this.playerHand.get(cardHand[i]).getCardType()) {
                case 0:
                    imagesHand[i].setImageResource(R.drawable.explodingkittencard);
                    break;
                case 1:
                    imagesHand[i].setImageResource(R.drawable.tacocatcard);
                    break;
                case 2:
                    imagesHand[i].setImageResource(R.drawable.beardcatcard);
                    break;
                case 3:
                    imagesHand[i].setImageResource(R.drawable.hairypotatocatcard);
                    break;
                case 4:
                    imagesHand[i].setImageResource(R.drawable.rainbowralphingcatcard);
                    break;
                case 5:
                    imagesHand[i].setImageResource(R.drawable.cattermeloncard);
                    break;
                case 6:
                    imagesHand[i].setImageResource(R.drawable.attackcard);
                    break;
                case 7:
                    imagesHand[i].setImageResource(R.drawable.shufflecard);
                    break;
                case 8:
                    imagesHand[i].setImageResource(R.drawable.favorcard);
                    break;
                case 9:
                    imagesHand[i].setImageResource(R.drawable.skipcard);
                    break;
                case 10:
                    imagesHand[i].setImageResource(R.drawable.seethefuturecard);
                    break;
                case 11:
                    imagesHand[i].setImageResource(R.drawable.nopecard);
                    break;
                case 12:
                    imagesHand[i].setImageResource(R.drawable.defusecard);
                    break;
                default:
                    break;
            } //switch statement
        } //for loop
    } //updateDisplay class

    public void setAsGui(GameMainActivity activity) {
        myActivity = activity;

        //loads layout for GUI
        activity.setContentView(R.layout.activity_main);
    }

    /**
     * Method to change the imagebuttons of the player hand when the left arrow
     * button is pressed to scroll through the players hand
     * @param button
     */

    @Override
    public void onClick(View button) {
        if (button instanceof Button) {
            // if the button pressed is the left scroll button, shift all of
            // the cards to the left and update the display. If this would
            // shift to a card that is out of bounds (i.e. -1 instead of 0)
            // then do no shift the cards
            if (button == button.findViewById(R.id.leftScroll)) {
                for (int i = 0; i < 5; i++) {
                    if (cardHand[i] - 1 == -1) {
                        break;
                    }
                    else {
                        cardHand[i] = cardHand[i] - 1;
                    }
                }
            }
            // same with the right scroll button, but instead check for out of
            // bounds on the right
            else if (button == button.findViewById(R.id.rightScroll)) {
                for (int i = 0; i < 5; i++) {
                    if (switchedDiscard == false) {
                        if (cardHand[i] + 1 >= this.playerHand.size()) {
                            break;
                        }
                        else {
                            cardHand[i] = cardHand[i] + 1;
                        }
                    }
                    else {
                        if (cardHand[i] + 1 >= state.getDiscardPile().size()) {
                            break;
                        }
                        else {
                            cardHand[i] = cardHand[i] + 1;
                        }
                    }

                }
            }
            else if (button == button.findViewById(R.id.discardPile)) {
                // check to see if the player wants to view the discard pile
                // or close the discard pile that they are actively viewing
                if (switchedDiscard == false) {
                    switchedDiscard = true;
                    //display the discard pile in place of the players cards
                    for (int i = 0; i < 5; i++) {
                        cardHand[i] = i;
                    }

                }
            }
            updateDisplay();
        }
    }

}
