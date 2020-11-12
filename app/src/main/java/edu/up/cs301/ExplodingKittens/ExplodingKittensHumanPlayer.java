package edu.up.cs301.ExplodingKittens;

import android.graphics.Color;
import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.ArrayList;

import edu.up.cs301.ExplodingKittens.EKActions.DrawCardAction;
import edu.up.cs301.ExplodingKittens.EKActions.PlayAttackCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayDefuseCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayFavorCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayFutureCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayNopeCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayShuffleCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlaySkipCard;
import edu.up.cs301.ExplodingKittens.EKActions.Trade2Action;
import edu.up.cs301.ExplodingKittens.EKActions.Trade3Action;
import edu.up.cs301.ExplodingKittens.EKActions.Trade5Action;
import edu.up.cs301.game.GameFramework.GameHumanPlayer;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.R;

public class ExplodingKittensHumanPlayer extends GameHumanPlayer implements View.OnClickListener {

    private EKGameState state;

    private GameMainActivity myActivity;

    /* an array of 5 ints that correspond to index positions in the target
     array list. This could be the discard pile or the players hand */
    private int cardHand[] = new int[5];
    /*
    an array of ints that correspond to card types for when the player wants
    to trade 3 they can see all possible cards displayed in their hand and
    select the card they want
     */
    private Card allCards[] = new Card[12];
    /*
     array to store the index location of cards to trade from the players hand
     */
    int tradeCards[] = new int[5];
    /*
    int variable to keep track of which player is currently selected for a
    trade 2 or 3
     */
    private int tradePlayer;
    // int variable to keep track of target card that may get stolen
    private int targCard;
    // int to keep track of what part of trade 3 we are at: 0 for no trade
    // yet, 1 for first stage, 2 for second stage
    private int trade3Stage = 0;
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
    //TextViews
    private TextView player0CardCount = null;
    private TextView player1CardCount = null;
    private TextView player2CardCount = null;
    private TextView player3CardCount = null;
    private TextView turnText = null;
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
    a boolean to keep track of if the player is selecting a card (true) or
    not (false)
     */
    private boolean selectingCard = false;
    /*
    boolean to keep track of when the player is looking at a seeTheFuture card
     */
    private boolean seeTheFutHand = false;


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
        /*
        imagesHand[0] = card1;
        imagesHand[1] = card2;
        imagesHand[2] = card3;
        imagesHand[3] = card4;
        imagesHand[4] = card5;
        */
        /*
        initialize the allCards array with all cards (except exploding kitten
         card)
         */
        for (int y = 0; y < 12; y++) {
            allCards[y] = new Card(y+1);
        }
        //set tradePlayer to 1
        this.tradePlayer = 1;
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
            this.state = new EKGameState((EKGameState) info);
                for(int i = 0; i < 5; i++){
                 this.cardHand[i] = i;
                }
            updateDisplay();
            }
    } // receiveInfo method

    /**
     * display the top 3 cards of the draw pile in place of the players hand
     */
    public void seeTheFuture() {
        card4.setImageResource(R.drawable.blankcard);
        card5.setImageResource(R.drawable.blankcard);
        for (int i = 0; i < 3; i++) {
            switch (state.getDeck().get(i).getCardType()) {
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
        }
    }

    /**
     * changes the other players hands when they are selected
     */
    public void otherPlayerHands() {
        /*
        update each player so that if the current player selects another
        person it does not keep the previously selected player
         */
        if (tradePlayer == 1) {
            player2.setImageResource(R.drawable.selectcardback);
        }
        else {
            player2.setImageResource(R.drawable.cardback);
        }
        if (tradePlayer == 2) {
            player3.setImageResource(R.drawable.selectcardback);
        }
        else {
            player3.setImageResource(R.drawable.cardback);
        }
        if (tradePlayer == 3) {
            player4.setImageResource(R.drawable.selectcardback);
        }
        else {
            player4.setImageResource(R.drawable.cardback);
        }
    }

    /**
     * Updates the GUI with new information after actions are taken
     */
    public void updateDisplay() {
        /* updates the display with the card type, the array is changed in
        the onClick method depending on whether the players hand is being
        viewed or the discard pile is being viewed and is updated with the
        card indexes for the given array */

            int query = 0;
            for (int i = 0; i < 5; i++) {
                int cardType;
            /*
            determine what deck the player is looking at to set the switch
            parameter to the correct card type from that deck:

                if stmnt 1: the player is looking at the discard deck

                if stmnt 2: the player is looking at all possible cards they
                can steal from another player in place of their own hand and
                will be selecting one as part of the trade 3 action

                if stmnt 3: the player is looking at their own hand
             */
                if (switchedDiscard == true) {
                    if(state.getDiscardPile().size() <= i){
                        cardType = 15;
                    }
                    else {
                        cardType = state.getDiscardPile().get(cardHand[i]).getCardType();
                        selectingCard = state.getDiscardPile().get(cardHand[i]).getSelected();
                    }
                }
                else if (switchedDiscard == false && trade3 == true && trade3Stage == 2) {
                    cardType = allCards[cardHand[i]].getCardType();
                    selectingCard = allCards[cardHand[i]].getSelected();
                }
                else {
                    if(state.getPlayerHand(this.playerNum).size() <= i) {
                        cardType = 15;
                    }
                    else {
                        cardType = state.getPlayerHand(this.playerNum).get(cardHand[i]).getCardType();
                        selectingCard =
                                state.getPlayerHand(this.playerNum).get(cardHand[i]).getSelected();
                    }

                }

                /*
                determine what image should be displayed for each image
                button, a selected image or a not selected image
                 */
                if (!selectingCard && !seeTheFutHand) {
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
                            imagesHand[i].setImageResource(R.drawable.blankcard);
                            break;
                    } //switch statement
                } // if statement for selectingCard
                // now for cases when selecting a card is true
                else if (!seeTheFutHand) {
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
                            imagesHand[i].setImageResource(R.drawable.blankcard);
                            break;
                    } //switch statement
                } // else statement for selectingCard
            } //for loop
        player0CardCount.setText("Your Card Count: " + state.getPlayerHand(0).size());
        player1CardCount.setText("Card Count: " + state.getPlayerHand(1).size());
        player2CardCount.setText("Card Count: " + state.getPlayerHand(2).size());
        player3CardCount.setText("Card Count: " + state.getPlayerHand(3).size());
        turnText.setText("Player " + state.getWhoseTurn() + "'s Turn");
        otherPlayerHands();
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
        this.player0CardCount =
                (TextView)activity.findViewById(R.id.player0cards);
        this.player1CardCount =
                (TextView)activity.findViewById(R.id.player1cards);
        this.player2CardCount =
                (TextView)activity.findViewById(R.id.player2cards);
        this.player3CardCount =
                (TextView)activity.findViewById(R.id.player3cards);
        this.turnText = (TextView)activity.findViewById(R.id.turntext);


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

        imagesHand[0] = card1;
        imagesHand[1] = card2;
        imagesHand[2] = card3;
        imagesHand[3] = card4;
        imagesHand[4] = card5;
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
            if (button == leftScroll) {
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
            else if (button == rightScroll) {
                for (int i = 0; i < 5; i++) {
                    /*
                    check to see if we are using the discard pile or the
                    player hand array so we know what array size as upper bound
                     */
                    if (!switchedDiscard && trade3Stage != 2) {
                        if (cardHand[4] + 1 >= state.getPlayerHand(this.playerNum).size()) {
                            break;
                        } else {
                            cardHand[i] = cardHand[i] + 1;
                        }
                    }
                    else if (switchedDiscard && trade3Stage != 2) {
                        if (cardHand[4] + 1 >= state.getDiscardPile().size()) {
                            break;
                        } else {
                            cardHand[i] = cardHand[i] + 1;
                        }
                    }
                    else if (trade3Stage == 2) {
                        if (cardHand[4] + 1 >= allCards.length) {
                            break;
                        } else {
                            cardHand[i] = cardHand[i] + 1;
                        }
                    }
                }
            } //right scroll button

            else if (button == trade2Btn) {
                if (trade2) { trade2 = false; }
                else { trade2 = true; }
                trade3 = false;
                trade5 = false;
                // deselect all player hand cards
                for (int a = 0; a < state.getPlayerHand(this.playerNum).size(); a++) {
                    state.getPlayerHand(this.playerNum).get(a).setSelected(false);
                }
            } // trade2 button

            else if (button == trade3Btn) {
                trade2 = false;
                if (trade3) {
                    trade3 = false;
                    trade3Stage = 0;
                }
                else {
                    trade3 = true;
                    trade3Stage = 1;
                }
                trade5 = false;
                // deselect all player hand cards
                for (int a = 0; a < state.getPlayerHand(this.playerNum).size(); a++) {
                    state.getPlayerHand(this.playerNum).get(a).setSelected(false);
                }
            } // trade3 button

            else if (button == trade5Btn) {
                trade2 = false;
                trade3 = false;
                if (trade5) { trade5 = false; }
                else { trade5 = true; }
                // deselect all player hand cards
                for (int a = 0; a < state.getPlayerHand(this.playerNum).size(); a++) {
                    state.getPlayerHand(this.playerNum).get(a).setSelected(false);
                }
            } // trade5 button

            else if(button == playBtn) {
                // check to make sure that this is not a trade or discard pile
                if (trade2 == false && trade3 == false && trade5 == false && switchedDiscard == false) {
                    // check to see that only 1 card is selected
                    int a = 0;
                    int index = 0;
                    for (int i = 0; i < state.getPlayerHand(this.playerNum).size(); i++) {
                        if (state.getPlayerHand(this.playerNum).get(i).getSelected()) {
                            a++;
                            index = i;
                        }
                    }
                    if (a == 1) {
                        //find what card is too be played
                        switch(state.getPlayerHand(this.playerNum).get(index).getCardType()) {
                            case 0:
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                            case 5:
                                break;
                            case 6:
                                PlayAttackCard attackCard =
                                 new PlayAttackCard(this);
                                game.sendAction(attackCard);
                                break;
                            case 7:
                                PlayShuffleCard shuffleCard =
                                        new PlayShuffleCard(this);
                                game.sendAction(shuffleCard);
                                break;
                            case 8:
                                int rand =
                                        (int)(Math.random()*state.getPlayerHand(tradePlayer).size());
                                PlayFavorCard favorCard =
                                        new PlayFavorCard(this, tradePlayer,
                                                rand);
                                game.sendAction(favorCard);
                                break;
                            case 9:
                                PlaySkipCard skipCard = new PlaySkipCard(this);
                                game.sendAction(skipCard);
                                break;
                            case 10:
                                seeTheFutHand = true;
                                PlayFutureCard futureCard = new PlayFutureCard(this);
                                game.sendAction(futureCard);
                                if(state.getWhoseTurn() == this.playerNum) {
                                    seeTheFuture();
                                }
                                break;
                            case 11:
                                PlayNopeCard nopeCard = new PlayNopeCard(this);
                                game.sendAction(nopeCard);
                                break;
                            case 12:
                                PlayDefuseCard defuseCard =
                                        new PlayDefuseCard(this);
                                game.sendAction(defuseCard);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            else if (button == enterBtn) {
                int numSelected = 0;
                int c = 0;
                //for trade 2
                /*
                check to make sure that only two cards are selected then send the action
                 */
                if (switchedDiscard == false && trade2 == true && trade3 == false && trade5 == false && seeTheFutHand == false) {
                    // find the cards to be traded
                    for (int i = 0; i < state.getPlayerHand(this.playerNum).size(); i++) {
                        if (state.getPlayerHand(this.playerNum).get(i).getSelected()) {
                            numSelected++;
                            if (numSelected > 0 && numSelected < 3) {
                                tradeCards[c] = i;
                                c++;
                            }
                        }
                    }
                    Trade2Action trade2Act = new Trade2Action(this,
                            tradePlayer, tradeCards[0], tradeCards[1]);
                    game.sendAction(trade2Act);
                    trade2 = false;
                    for (int p = 0; p < 5; p++) {
                        tradeCards[p] = 0;
                    }
                }
                //trade3
                else if (switchedDiscard == false && trade2 == false && trade3 == true && trade5 == false && trade3Stage == 1 && seeTheFutHand == false) {
                    for (int i = 0; i < state.getPlayerHand(this.playerNum).size(); i++) {
                        if (state.getPlayerHand(this.playerNum).get(i).getSelected() == true) {
                            numSelected++;
                            if (numSelected > 0 && numSelected <= 3) {
                                tradeCards[c] = i;
                                c++;
                            }
                        }
                    }
                    // set allCards selected booleans to false
                    for (int i = 0; i < allCards.length; i++) {
                        allCards[i].setSelected(false);
                    }
                    trade3Stage = 2;
                }
                else if (switchedDiscard == false && trade2 == false && trade3 == true && trade5 == false && trade3Stage == 2 && seeTheFutHand == false) {
                    // find the target card to be stolen
                    for (int i = 0; i < allCards.length; i++) {
                        if (allCards[i].getSelected()) {
                            targCard = allCards[i].getCardType();
                        }
                    }
                    // find the cards to be traded
                    int count = 0;
                    for (int a = 0; a < state.getPlayerHand(this.playerNum).size(); a++) {
                        if (state.getPlayerHand(this.playerNum).get(a).getSelected() && count < 3) {
                            tradeCards[count] = a;
                            count++;
                        }
                    }
                    // create and send the action
                    Trade3Action trade3Act = new Trade3Action(this,
                            tradePlayer, tradeCards[0], tradeCards[1],
                            tradeCards[2], targCard);
                    game.sendAction(trade3Act);
                    trade3 = false;
                    trade3Stage = 0;
                    for (int p = 0; p < 5; p++) {
                        tradeCards[p] = 0;
                    }
                }

                //trade5
                else if (switchedDiscard == false && trade2 == false && trade3 == false && trade5 == true && seeTheFutHand == false) {
                    // find the cards to be traded
                    for (int i = 0; i < state.getPlayerHand(this.playerNum).size(); i++) {
                        if (state.getPlayerHand(this.playerNum).get(i).getSelected() == true) {
                            numSelected++;
                            if (numSelected > 0 && numSelected < 6) {
                                tradeCards[c] = i;
                                c++;
                            }
                        }
                    }
                    // find the discard pile card to take
                    int once = 0;
                    int cardVal = 0;
                    for (int a = 0; a < state.getDiscardPile().size(); a++) {
                        if (state.getDiscardPile().get(a).getSelected() && once == 0) {
                            once++;
                            cardVal = state.getDiscardPile().get(a).getCardType();
                        }
                    }
                    Trade5Action trade5Act = new Trade5Action(this,
                            tradeCards[0], tradeCards[1], tradeCards[2],
                            tradeCards[3], tradeCards[4], cardVal);
                    game.sendAction(trade5Act);
                    trade5 = false;
                    for (int p = 0; p < 5; p++) {
                        tradeCards[p] = 0;
                    }
                }

                //see the future card ticked
                else if (seeTheFutHand) {
                    seeTheFutHand = false;
                }

                // deselect all player hand cards
                //for (int a = 0; a < state.getPlayerHand(this.playerNum)
                // .size(); a++) {
                //    state.getPlayerHand(this.playerNum).get(a).setSelected
                //    (false);
                //}
                // deselect all allCards cards
                /*for (int b = 0; b < 11; b++) {
                    allCards[b].setSelected(false);
                }
                // deselect all cards in the discard pile array
                for (int d = 0; d < state.getDiscardPile().size(); d++) {
                    state.getDiscardPile().get(d).setSelected(false);
                }
                // reset the cardHands array
                if (state.getPlayerHand(this.playerNum).size() < 5) {
                    for (int q = 0; q < 5; q++) {
                        cardHand[q] = q;
                    }
                }*/

            } // enter button

            else if (button == endTurn) {
                // deselect all player hand cards
                for (int a = 0; a < state.getPlayerHand(this.playerNum).size(); a++) {
                    state.getPlayerHand(this.playerNum).get(a).setSelected(false);
                }
                // deselect all allCards cards
                for (int b = 0; b < 11; b++) {
                    allCards[b].setSelected(false);
                }
                // deselect all cards in the discard pile array
                for (int c = 0; c < state.getDiscardPile().size(); c++) {
                    state.getDiscardPile().get(c).setSelected(false);
                }
                DrawCardAction drawCard = new DrawCardAction(this);
                game.sendAction(drawCard);
            } // endTurn button

        } // if statement for instance of button

        // for all image buttons
        else if (button instanceof ImageButton) {

            if (button == discardPileBtn) {
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
                    // deselect all cards in the discard pile array
                    for (int a = 0; a < state.getDiscardPile().size(); a++) {
                        state.getDiscardPile().get(a).setSelected(false);
                    }
                }
                updateDisplay();
            } //discard pile button

            else if (button == card1) {
                /*
                check to see if the player hand previously selected the image
                 button in question. Reverse the current selection state of
                 the card
                 */
                if (switchedDiscard == false && trade3Stage != 2) {
                    if (state.getPlayerHand(this.playerNum).get(cardHand[0]).getSelected() == true) {
                        state.getPlayerHand(this.playerNum).get(cardHand[0]).setSelected(false);
                    } else {
                        state.getPlayerHand(this.playerNum).get(cardHand[0]).setSelected(true);
                    }
                }
                /*
                if the discard pile is selected, perform the same action on
                the discard pile card
                 */
                else if (switchedDiscard == true) {
                    if (state.getDiscardPile().get(cardHand[0]).getSelected() == true) {
                        state.getDiscardPile().get(cardHand[0]).setSelected(false);
                    } else {
                        state.getDiscardPile().get(cardHand[0]).setSelected(true);
                    }
                }
                /*
                if the player is doing trade 3 and is selecting a target card
                 to steal from another player, set the booleans in the allCards
                 */
                else if (switchedDiscard == false && trade3Stage == 2) {
                    if (allCards[cardHand[0]].getSelected() == true) {
                        allCards[cardHand[0]].setSelected(false);
                    } else {
                        allCards[cardHand[0]].setSelected(true);
                    }
                }
                // base case do nothing with the booleans
                else {

                }

            } // player hand button 1

            else if (button == card2) {
                /*
                check to see if the player hand previously selected the image
                 button in question. Reverse the current selection state of
                 the card
                 */
                if (switchedDiscard == false && trade3Stage != 2) {
                    if (state.getPlayerHand(this.playerNum).get(cardHand[1]).getSelected() == true) {
                        state.getPlayerHand(this.playerNum).get(cardHand[1]).setSelected(false);
                    } else {
                        state.getPlayerHand(this.playerNum).get(cardHand[1]).setSelected(true);
                    }
                }
                /*
                if the discard pile is selected, perform the same action on
                the discard pile card
                 */
                else if (switchedDiscard == true) {
                    if (state.getDiscardPile().get(cardHand[1]).getSelected() == true) {
                        state.getDiscardPile().get(cardHand[1]).setSelected(false);
                    } else {
                        state.getDiscardPile().get(cardHand[1]).setSelected(true);
                    }
                }
                /*
                if the player is doing trade 3 and is selecting a target card
                 to steal from another player, set the booleans in the allCards
                 */
                else if (switchedDiscard == false && trade3Stage == 2) {
                    if (allCards[cardHand[1]].getSelected() == true) {
                        allCards[cardHand[1]].setSelected(false);
                    } else {
                        allCards[cardHand[1]].setSelected(true);
                    }
                }
                // base case do nothing with the booleans
                else {

                }
            } // player hand button 2

            else if (button == card3) {
                /*
                check to see if the player hand previously selected the image
                 button in question. Reverse the current selection state of
                 the card
                 */
                if (switchedDiscard == false && trade3Stage != 2) {
                    if (state.getPlayerHand(this.playerNum).get(cardHand[2]).getSelected() == true) {
                        state.getPlayerHand(this.playerNum).get(cardHand[2]).setSelected(false);
                    } else {
                        state.getPlayerHand(this.playerNum).get(cardHand[2]).setSelected(true);
                    }
                }
                /*
                if the discard pile is selected, perform the same action on
                the discard pile card
                 */
                else if (switchedDiscard == true) {
                    if (state.getDiscardPile().get(cardHand[2]).getSelected() == true) {
                        state.getDiscardPile().get(cardHand[2]).setSelected(false);
                    } else {
                        state.getDiscardPile().get(cardHand[2]).setSelected(true);
                    }
                }
                /*
                if the player is doing trade 3 and is selecting a target card
                 to steal from another player, set the booleans in the allCards
                 */
                else if (switchedDiscard == false && trade3Stage == 2) {
                    if (allCards[cardHand[2]].getSelected() == true) {
                        allCards[cardHand[2]].setSelected(false);
                    } else {
                        allCards[cardHand[2]].setSelected(true);
                    }
                }
                // base case do nothing with the booleans
                else {

                }
            } // player hand button 3

            else if (button == card4) {
                /*
                check to see if the player hand previously selected the image
                 button in question. Reverse the current selection state of
                 the card
                 */
                if (switchedDiscard == false && trade3Stage != 2) {
                    if (state.getPlayerHand(this.playerNum).get(cardHand[3]).getSelected() == true) {
                        state.getPlayerHand(this.playerNum).get(cardHand[3]).setSelected(false);
                    } else {
                        state.getPlayerHand(this.playerNum).get(cardHand[3]).setSelected(true);
                    }
                }
                /*
                if the discard pile is selected, perform the same action on
                the discard pile card
                 */
                else if (switchedDiscard == true) {
                    if (state.getDiscardPile().get(cardHand[3]).getSelected() == true) {
                        state.getDiscardPile().get(cardHand[3]).setSelected(false);
                    } else {
                        state.getDiscardPile().get(cardHand[3]).setSelected(true);
                    }
                }
                /*
                if the player is doing trade 3 and is selecting a target card
                 to steal from another player, set the booleans in the allCards
                 */
                else if (switchedDiscard == false && trade3Stage == 2) {
                    if (allCards[cardHand[3]].getSelected() == true) {
                        allCards[cardHand[3]].setSelected(false);
                    } else {
                        allCards[cardHand[3]].setSelected(true);
                    }
                }
                // base case do nothing with the booleans
                else {

                }
            } // player hand button 4

            else if (button == card5) {
                /*
                check to see if the player hand previously selected the image
                 button in question. Reverse the current selection state of
                 the card
                 */
                if (switchedDiscard == false && trade3Stage != 2) {
                    if (state.getPlayerHand(this.playerNum).get(cardHand[4]).getSelected() == true) {
                        state.getPlayerHand(this.playerNum).get(cardHand[4]).setSelected(false);
                    } else {
                        state.getPlayerHand(this.playerNum).get(cardHand[4]).setSelected(true);
                    }
                }
                /*
                if the discard pile is selected, perform the same action on
                the discard pile card
                 */
                else if (switchedDiscard == true) {
                    if (state.getDiscardPile().get(cardHand[4]).getSelected() == true) {
                        state.getDiscardPile().get(cardHand[4]).setSelected(false);
                    } else {
                        state.getDiscardPile().get(cardHand[4]).setSelected(true);
                    }
                }
                /*
                if the player is doing trade 3 and is selecting a target card
                 to steal from another player, set the booleans in the allCards
                 */
                else if (switchedDiscard == false && trade3Stage == 2) {
                    if (allCards[cardHand[4]].getSelected() == true) {
                        allCards[cardHand[4]].setSelected(false);
                    } else {
                        allCards[cardHand[4]].setSelected(true);
                    }
                }
                // base case do nothing with the booleans
                else {

                }
            } // player hand button 5

            else if (button == player2) {
                if (state.hasPlayerLost(1)) {
                    tradePlayer = 2;
                    if (state.hasPlayerLost(2)) {
                        tradePlayer = 3;
                    }
                }
                else {
                    tradePlayer = 1;
                    otherPlayerHands();
                }
            } // player2 button

            else if (button == player3) {
                if (state.hasPlayerLost(2)) {
                    tradePlayer = 3;
                    if (state.hasPlayerLost(3)) {
                        tradePlayer = 1;
                    }
                }
                else {
                    tradePlayer = 2;
                    otherPlayerHands();
                }
            } // player3 button

            else if (button == player4) {
                if (state.hasPlayerLost(3)) {
                    tradePlayer = 1;
                    if (state.hasPlayerLost(1)) {
                        tradePlayer = 2;
                    }
                }
                else {
                    tradePlayer = 3;
                    otherPlayerHands();
                }
            } // player4 button
        } // image buttons
        updateDisplay();
    } //onClick method

}
