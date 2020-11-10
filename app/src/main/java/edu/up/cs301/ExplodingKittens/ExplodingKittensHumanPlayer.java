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

    /* an array of 5 ints that correspond to index positions in the target
     array list. This could be the discard pile or the players hand */
    private int cardHand[] = new int[5];
    /* image buttons that will be set to the corresponding image buttons for
    he players hand and will be updated as the players hand adds or
    subtracts cards or they scroll through their hand. They will also be
    used to show the discard pile when the player wants to look through it */
    private ImageButton card1 = null;
    private ImageButton card2 = null;
    private ImageButton card3 = null;
    private ImageButton card4 = null;
    private ImageButton card5 = null;
    //an array list of image buttons to be used
    private ImageButton imagesHand[] = new ImageButton[5];
    // the discard pile image button
    private ImageButton discardPileBtn = null;
    // a boolean to keep track of if the player is looking at the discard
    // pile (true) or not (false)
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
        /*
        set the cardHand array to the first 5 indexes in the players hand
         */
        for (int i = 0; i < 5; i++) {
            cardHand[i] = i;
        }
        /*
        set the imagesHand array to the correct image buttons for the GUI
         */
        imagesHand[0] = card1;
        imagesHand[1] = card2;
        imagesHand[2] = card3;
        imagesHand[3] = card4;
        imagesHand[4] = card5;
    } //ExplodingKittensHumanPlayer method

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

        if(!(info instanceof EKGameState)){
            flash(Color.RED, 500);
        }
        else{
            updateDisplay();
            }
    } // receiveInfo method

    /**
     * Updates the GUI with new information after actions are taken
     */
    public void updateDisplay() {
        /* updates the display with the card type, the array is changed in
        the onClick method depending on whether the players hand is being
        viewed or the discard pile is being viewed and is updated with the
        card indexes for the given array */
        for (int i = 0; i < 5; i++) {
            int cardType;
            /*
            check to see if we are looking at the discard pile or the player
            hand array and choose the correct array index accordingly
             */
            if (switchedDiscard == false) {
                cardType = this.getPlayerHand().get(cardHand[i]).getCardType();
            }
            else {
                cardType =
                        state.getDiscardPile().get(cardHand[i]).getCardType();
            }
            switch (cardType) {
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
    } //updateDisplay method

    public void setAsGui(GameMainActivity activity) {
        myActivity = activity;

        //loads layout for GUI
        activity.setContentView(R.layout.activity_main);

        //initialize the widget reference member variables
        this.card1 = (ImageButton)activity.findViewById(R.id.imageButton5);
        this.card2 = (ImageButton)activity.findViewById(R.id.imageButton6);
        this.card3 = (ImageButton)activity.findViewById(R.id.imageButton7);
        this.card4 = (ImageButton)activity.findViewById(R.id.imageButton8);
        this.card5 = (ImageButton)activity.findViewById(R.id.imageButton9);
        this.discardPileBtn = (ImageButton)activity.findViewById(R.id.discardPile);
    } //setAsGui method

    /**
     * Method to change the imagebuttons of the player hand when the left arrow
     * button is pressed to scroll through the players hand
     * @param button
     */

    @Override
    public void onClick(View button) {
        if (button instanceof Button) {
            /*
            if the button pressed is the left scroll button, shift index to the
            left and update the display. If this would shift the index out of
            bounds (i.e. -1 instead of 0) then do no shift the cards
             */
            if (button == button.findViewById(R.id.leftScroll)) {
                for (int i = 0; i < 5; i++) {
                    if (cardHand[i] - 1 == -1) {
                        break;
                    }
                    else {
                        cardHand[i] = cardHand[i] - 1;
                    }
                }
            } //left scroll button

            /*
            same with the right scroll button, but instead check for out of
            bounds on the right
             */
            else if (button == button.findViewById(R.id.rightScroll)) {
                for (int i = 0; i < 5; i++) {
                    /*
                    check to see if we are using the discard pile or the
                    player hand array so we know what array size as upper bound
                     */
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
            } //right scroll button

            else if (button == button.findViewById(R.id.discardPile)) {
                /*
                check to see if the player wants to view the discard pile
                or close the discard pile that they are actively viewing
                 */
                if (switchedDiscard == false) {
                    switchedDiscard = true;
                    /*
                    change the indexes of the cardHand array to be the first
                    5 indexes for the discard pile
                     */
                    for (int i = 0; i < 5; i++) {
                        cardHand[i] = i;
                    }
                }
                // switchedDiscard == true so switch the displayed cards back
                // to the players hand
                else {
                    switchedDiscard = false;
                    for (int i = 0; i < 5; i++) {
                        cardHand[i] = i;
                    }
                }
            } //discard pile button

            else if (button == button.findViewById(R.id.imageButton5)) {
                /*
                check to see if the player hand previously selected the image
                 button in question. If so carry out the action of the image
                 button, if not select the image button
                 */
            }

            updateDisplay();
        }
    } //onClick method

}
