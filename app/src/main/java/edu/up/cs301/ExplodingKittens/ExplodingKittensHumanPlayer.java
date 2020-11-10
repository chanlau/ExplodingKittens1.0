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
    /*
    int variable to keep track of which player is currently selected for a
    trade 2 or 3
     */
    private int tradePlayer;
    /*
    buttons
     */
    private Button leftScroll = null;
    private Button rightScroll = null;
    private Button trade2Btn = null;
    private Button trade3Btn = null;
    private Button trade5Btn = null;
    private Button enterBtn = null;
    private Button playBtn = null;
    private Button endTurn = null;
    /* image buttons that will be set to the corresponding image buttons for
    he players hand and will be updated as the players hand adds or
    subtracts cards or they scroll through their hand. They will also be
    used to show the discard pile when the player wants to look through it */
    private ImageButton card1 = null;
    private ImageButton card2 = null;
    private ImageButton card3 = null;
    private ImageButton card4 = null;
    private ImageButton card5 = null;
    /*
    image buttons for the players that will be selectable for trade 2 and 3
     */
    private ImageButton player2 = null;
    private ImageButton player3 = null;
    private ImageButton player4 = null;
    //an array list of image buttons to be used
    private ImageButton imagesHand[] = new ImageButton[5];
    // the discard pile image button
    private ImageButton discardPileBtn = null;
    /*
    booleans to keep track of which trade is currently selected
     */
    private boolean trade2 = false;
    private boolean trade3 = false;
    private boolean trade5 = false;
    // a boolean to keep track of if the player is looking at the discard
    // pile (true) or not (false)
    private boolean switchedDiscard = false;
    /*
    a boolean to keep track of if the player can select other players for
    trade 2 or trade 3
     */
    private boolean selectablePlayers = false;
    /*
    a boolean to keep track of if the player is selecting a card (true) or
    not (false)
     */
    private boolean selectingCard = false;

    /*

     */

    /**
     * constructor
     * @param name
     *      number corresponding to this player
     *      name corresponding to this player
     */
    public ExplodingKittensHumanPlayer(String name) {
        super(name);
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
                // check to see if the player is selecting a card or not
                if (selectingCard == false) {
                    // set the appropriate image to each button
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
                } // if statement for selectingCard
                else {
                    switch (cardType) {
                        case 0:
                            imagesHand[i].setImageResource(R.drawable.explodingkittencard);
                            break;
                        case 1:
                            imagesHand[i].setImageResource(R.drawable.selecttacocatcard);
                            break;
                        case 2:
                            imagesHand[i].setImageResource(R.drawable.selectbeardcatcard);
                            break;
                        case 3:
                            imagesHand[i].setImageResource(R.drawable.selecthairypotatocatcard);
                            break;
                        case 4:
                            imagesHand[i].setImageResource(R.drawable.selectrainbowralphingcatcard);
                            break;
                        case 5:
                            imagesHand[i].setImageResource(R.drawable.selectcattermeloncard);
                            break;
                        case 6:
                            imagesHand[i].setImageResource(R.drawable.selectattackcard);
                            break;
                        case 7:
                            imagesHand[i].setImageResource(R.drawable.selectshufflecard);
                            break;
                        case 8:
                            imagesHand[i].setImageResource(R.drawable.selectfavorcard);
                            break;
                        case 9:
                            imagesHand[i].setImageResource(R.drawable.selectskipcard);
                            break;
                        case 10:
                            imagesHand[i].setImageResource(R.drawable.selectseethefuturecard);
                            break;
                        case 11:
                            imagesHand[i].setImageResource(R.drawable.selectnopecard);
                            break;
                        case 12:
                            imagesHand[i].setImageResource(R.drawable.selectdefusecard);
                            break;
                        default:
                            break;
                    } //switch statement
                } // else statement for selectingCard
            } //for loop
    } //updateDisplay method

    public void setAsGui(GameMainActivity activity) {
        myActivity = activity;

        //loads layout for GUI
        activity.setContentView(R.layout.activity_main);

        //initialize the widget reference member variables
        this.leftScroll = (Button)activity.findViewById(R.id.leftScroll);
        this.rightScroll = (Button)activity.findViewById(R.id.rightScroll);
        this.trade2Btn = (Button)activity.findViewById(R.id.trade2);
        this.trade3Btn = (Button)activity.findViewById(R.id.trade3);
        this.trade5Btn = (Button)activity.findViewById(R.id.trade5);
        this.enterBtn = (Button)activity.findViewById(R.id.enter);
        this.playBtn = (Button)activity.findViewById(R.id.play);
        this.endTurn = (Button)activity.findViewById(R.id.endTurn);
        this.card1 = (ImageButton)activity.findViewById(R.id.imageButton5);
        this.card2 = (ImageButton)activity.findViewById(R.id.imageButton6);
        this.card3 = (ImageButton)activity.findViewById(R.id.imageButton7);
        this.card4 = (ImageButton)activity.findViewById(R.id.imageButton8);
        this.card5 = (ImageButton)activity.findViewById(R.id.imageButton9);
        this.player2 = (ImageButton)activity.findViewById(R.id.player2);
        this.player3 = (ImageButton)activity.findViewById(R.id.player3);
        this.player4 = (ImageButton)activity.findViewById(R.id.player4);
        this.discardPileBtn =
                (ImageButton)activity.findViewById(R.id.discardPile);

        // listen for button presses
        leftScroll.setOnClickListener(this);
        rightScroll.setOnClickListener(this);
        trade2Btn.setOnClickListener(this);
        trade3Btn.setOnClickListener(this);
        trade5Btn.setOnClickListener(this);
        enterBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        endTurn.setOnClickListener(this);
        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        card5.setOnClickListener(this);
        player2.setOnClickListener(this);
        player3.setOnClickListener(this);
        player4.setOnClickListener(this);
        discardPileBtn.setOnClickListener(this);
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
                    } else {
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
                        } else {
                            cardHand[i] = cardHand[i] + 1;
                        }
                    } else {
                        if (cardHand[i] + 1 >= state.getDiscardPile().size()) {
                            break;
                        } else {
                            cardHand[i] = cardHand[i] + 1;
                        }
                    }
                }
            } //right scroll button

            else if (button == button.findViewById(R.id.trade2)) {
                selectablePlayers = true;
                trade2 = true;
                trade3 = false;
                trade5 = false;
            } // trade2 button

            else if (button == button.findViewById(R.id.trade3)) {
                selectablePlayers = true;
                trade2 = false;
                trade3 = true;
                trade5 = false;
            } // trade3 button

            else if (button == button.findViewById(R.id.trade5)) {
                trade2 = false;
                trade3 = false;
                trade5 = true;
            } else if (button == button.findViewById(R.id.enter)) {
                //for trade 2
                /*
                check to make sure that only two cards are selected then send the action
                 */
                if (switchedDiscard == false) {
                    if (selectablePlayers == true) {
                        int numSelected = 0;
                        int tradeCards[] = new int[2];
                        int c = 0;
                        for (int i = 0; i < this.getPlayerHand().size(); i++) {
                            if (this.getPlayerHand().get(i).getSelected() == true) {
                                if (numSelected < 2) {
                                    tradeCards[c] = i;
                                    c++;
                                }
                                numSelected++;
                            }
                        }
                        if (numSelected == 2) {

                        } else {
                        }
                        selectablePlayers = false;
                    } // boolean selectablePlayers
                } // boolean switchedDiscard
            } // enter button

            updateDisplay();
        } // if statement for instance of button

        else if (button instanceof ImageButton) {

            if (button == button.findViewById(R.id.discardPile)) {
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
                 button in question. Reverse the current selection state of
                 the card
                 */
                if (switchedDiscard == false) {
                    if (this.getPlayerHand().get(cardHand[0]).getSelected() == true) {
                        this.getPlayerHand().get(cardHand[0]).setSelected(false);
                    } else {
                        this.getPlayerHand().get(cardHand[0]).setSelected(true);
                    }
                }
                /*
                if the discard pile is selected, perform the same action on
                the discard pile card
                 */
                else {
                    if (state.getDiscardPile().get(cardHand[4]).getSelected() == true) {
                        state.getDiscardPile().get(cardHand[4]).setSelected(false);
                    } else {
                        state.getDiscardPile().get(cardHand[4]).setSelected(true);
                    }
                }
            } else if (button == button.findViewById(R.id.imageButton6)) {
                 /*
                check to see if the player hand previously selected the image
                 button in question. Reverse the current selection state of
                 the card
                 */
                if (switchedDiscard == false) {
                    if (this.getPlayerHand().get(cardHand[1]).getSelected() == true) {
                        this.getPlayerHand().get(cardHand[1]).setSelected(false);
                    } else {
                        this.getPlayerHand().get(cardHand[1]).setSelected(true);
                    }
                }
                /*
                if the discard pile is selected, perform the same action on
                the discard pile card
                 */
                else {
                    if (state.getDiscardPile().get(cardHand[4]).getSelected() == true) {
                        state.getDiscardPile().get(cardHand[4]).setSelected(false);
                    } else {
                        state.getDiscardPile().get(cardHand[4]).setSelected(true);
                    }
                }
            } else if (button == button.findViewById(R.id.imageButton7)) {
                /*
                check to see if the player hand previously selected the image
                 button in question. Reverse the current selection state of
                 the card
                 */
                if (switchedDiscard == false) {
                    if (this.getPlayerHand().get(cardHand[2]).getSelected() == true) {
                        this.getPlayerHand().get(cardHand[2]).setSelected(false);
                    } else {
                        this.getPlayerHand().get(cardHand[2]).setSelected(true);
                    }
                }
                /*
                if the discard pile is selected, perform the same action on
                the discard pile card
                 */
                else {
                    if (state.getDiscardPile().get(cardHand[4]).getSelected() == true) {
                        state.getDiscardPile().get(cardHand[4]).setSelected(false);
                    } else {
                        state.getDiscardPile().get(cardHand[4]).setSelected(true);
                    }
                }
            } else if (button == button.findViewById(R.id.imageButton8)) {
                /*
                check to see if the player hand previously selected the image
                 button in question. Reverse the current selection state of
                 the card
                 */
                if (switchedDiscard == false) {
                    if (this.getPlayerHand().get(cardHand[3]).getSelected() == true) {
                        this.getPlayerHand().get(cardHand[3]).setSelected(false);
                    } else {
                        this.getPlayerHand().get(cardHand[3]).setSelected(true);
                    }
                }
                /*
                if the discard pile is selected, perform the same action on
                the discard pile card
                 */
                else {
                    if (state.getDiscardPile().get(cardHand[4]).getSelected() == true) {
                        state.getDiscardPile().get(cardHand[4]).setSelected(false);
                    } else {
                        state.getDiscardPile().get(cardHand[4]).setSelected(true);
                    }
                }
            } else if (button == button.findViewById(R.id.imageButton9)) {
                /*
                check to see if the player hand previously selected the image
                 button in question. Reverse the current selection state of
                 the card
                 */
                if (switchedDiscard == false) {
                    if (this.getPlayerHand().get(cardHand[4]).getSelected() == true) {
                        this.getPlayerHand().get(cardHand[4]).setSelected(false);
                    } else {
                        this.getPlayerHand().get(cardHand[4]).setSelected(true);
                    }
                }
                /*
                if the discard pile is selected, perform the same action on
                the discard pile card
                 */
                else {
                    if (state.getDiscardPile().get(cardHand[4]).getSelected() == true) {
                        state.getDiscardPile().get(cardHand[4]).setSelected(false);
                    } else {
                        state.getDiscardPile().get(cardHand[4]).setSelected(true);
                    }
                }
            }

            updateDisplay();
        }
    } //onClick method

}
