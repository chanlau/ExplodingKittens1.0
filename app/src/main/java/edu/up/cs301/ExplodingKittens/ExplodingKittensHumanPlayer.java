package edu.up.cs301.ExplodingKittens;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import org.w3c.dom.Text;

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

/**
 * GUI for a human to play the game. Handles all interactions between Human
 * player and game buttons.
 *
 * @authors Chandler Lau, Ka'ulu Ng, Samuel Warrick
 * @version 11/25/2020
 */

public class ExplodingKittensHumanPlayer extends GameHumanPlayer implements View.OnClickListener {

    //gamestate variable
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
    buttons on the left side of the screen for carrying out actions
     */
    private Button leftScroll = null;
    private Button rightScroll = null;
    private Button trade2Btn = null;
    private Button trade3Btn = null;
    private Button trade5Btn = null;
    private Button enterBtn = null;
    private Button playBtn = null;
    private Button endTurn = null;
    private Button helpBtn = null;
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
    private ImageButton player1 = null;
    private ImageButton player2 = null;
    private ImageButton player3 = null;
    //an array list of image buttons to be used
    private ImageButton imagesHand[] = new ImageButton[5];
    // the discard pile image button
    private ImageButton discardPileBtn = null;
    // the deck image button
    private ImageButton deckBtn = null;
    //TextViews
    private TextView player1Label = null;
    private TextView player2Label = null;
    private TextView player3Label = null;
    private TextView player0CardCount = null;
    private TextView player1CardCount = null;
    private TextView player2CardCount = null;
    private TextView player3CardCount = null;
    private TextView turnText = null;
    private TextView cardsToDraw = null;
    private TextView cardsInDeck = null;
    private TextView playerTurn = null;
    private TextView displayCards = null;
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
    boolean to keep track of when the player is looking at the deck for a
    see the future card
     */
    private boolean displayDeck = false;


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
            if (state.getWhoseTurn() == 0) {
                try {
                    printPlayerLog();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            updateDisplay();
        }
    } // receiveInfo method

    /**
     * Prints out the player log.
     * @throws InterruptedException
     */
    public void printPlayerLog() throws InterruptedException {
        /**
         * External Citation
         * Date: 18 November 2020
         * Problem: Could not figure out how to print the message log to the
         * textview and display each log
         * Resource:
         * https://stackoverflow.com/questions/35751444/how-to-display-an-arraylist-in-a-textview-when-a-button-is-clicked
         * Solution: Used the example code to create a StringBuilder and set
         * the textview to multiline as well
         */
        StringBuilder logMessages = new StringBuilder();
        for (String s : state.getPlayerLog()) {
            logMessages.append(s + "\n");
        }
        turnText.setText(logMessages.toString());
    }

    /**
     * updates the discard imagebutton with the most recently discarded card
     */
    public void updateDiscard() {
        if (state.getDiscardPile().size() == 0) {
            return;
        }
        else {
            switch (state.getDiscardPile().get(state.getDiscardPile().size()-1).getCardType()) {
                case 0:
                    discardPileBtn.setImageResource(R.drawable.explodingkittencard);
                    break;
                case 1:
                    discardPileBtn.setImageResource(R.drawable.tacocatcard);
                    break;
                case 2:
                    discardPileBtn.setImageResource(R.drawable.beardcatcard);
                    break;
                case 3:
                    discardPileBtn.setImageResource(R.drawable.hairypotatocatcard);
                    break;
                case 4:
                    discardPileBtn.setImageResource(R.drawable.rainbowralphingcatcard);
                    break;
                case 5:
                    discardPileBtn.setImageResource(R.drawable.cattermeloncard);
                    break;
                case 6:
                    discardPileBtn.setImageResource(R.drawable.attackcard);
                    break;
                case 7:
                    discardPileBtn.setImageResource(R.drawable.shufflecard);
                    break;
                case 8:
                    discardPileBtn.setImageResource(R.drawable.favorcard);
                    break;
                case 9:
                    discardPileBtn.setImageResource(R.drawable.skipcard);
                    break;
                case 10:
                    discardPileBtn.setImageResource(R.drawable.seethefuturecard);
                    break;
                case 11:
                    discardPileBtn.setImageResource(R.drawable.nopecard);
                    break;
                case 12:
                    discardPileBtn.setImageResource(R.drawable.defusecard);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * display the top 3 cards of the draw pile in place of the players hand
     */
    public void seeTheFuture() {
        card4.setImageResource(R.drawable.blankcard);
        card5.setImageResource(R.drawable.blankcard);
        displayDeck = true;
        int seeTop = 3;
        if(state.getDeck().size() < 3){
            seeTop = state.getDeck().size();
        }
        for (int i = 0; i < seeTop; i++) {
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
                    imagesHand[i].setImageResource(R.drawable.blankcard);
                    break;
            } //switch statement
        }

        //Set the remaining cards to blank cards
        for(int j = seeTop; j < 3; j++){
            imagesHand[j].setImageResource(R.drawable.blankcard);
        }

        //Setting the text of the button to "Exit View"
        //in order to help guide the player
        this.enterBtn.setText("Exit View");
    }

    /**
     * changes the other players hands when they are selected to
     * selectedcardback instead of the normal cardback
     */
    public void otherPlayerHands() {
        /*
        update each player so that if the current player selects another
        person it does not keep the previously selected player
         */

        //Draws the number of players on the board depending
        //on how many players are in the game
        if(state.getNumPlayers() == 2){
            drawPlayer1(true);
            drawPlayer2(false);
            drawPlayer3(false);
        }
        else if(state.getNumPlayers() == 3){
            drawPlayer1(true);
            drawPlayer2(true);
            drawPlayer3(false);
        }
        else if(state.getNumPlayers() == 4){
            drawPlayer1(true);
            drawPlayer2(true);
            drawPlayer3(true);
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
        // update whose turn it is
        if(state.getWhoseTurn() == this.playerNum){
            this.playerTurn.setText("Your Turn");
        }
        else{
            this.playerTurn.setText("Player " + state.getWhoseTurn() + "'s Turn");
        }

        //if targeted player has lost, set target to a different player
        if(state.hasPlayerLost(tradePlayer)){
            while(state.hasPlayerLost(tradePlayer)){
                tradePlayer = (tradePlayer + 1) % state.getNumPlayers();
            }
        }
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
                if (switchedDiscard) {
                    if(state.getDiscardPile().size() <= i){
                        cardType = 15;
                    }
                    else {
                        cardType = state.getDiscardPile().get(cardHand[i]).getCardType();
                        selectingCard = state.getDiscardPile().get(cardHand[i]).getSelected();
                        displayCards.setText("Discard Pile");
                    }
                }
                else if (!switchedDiscard && trade3 && trade3Stage == 2) {
                    cardType = allCards[cardHand[i]].getCardType();
                    selectingCard = allCards[cardHand[i]].getSelected();
                    displayCards.setText("Select 1 Card to Request");
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
                    displayCards.setText("Your Hand");

                }
                if (seeTheFutHand) {
                    displayCards.setText("Top 3 Cards of the Draw Pile From Left To Right");
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
        //set textviews of the other players
        setPlayersText();
        cardsInDeck.setText("Cards In Deck: " + state.getDeck().size());
        otherPlayerHands();
        updateDiscard();
    } //updateDisplay method

    /**
     * method for when our game is chosen as the GUI, called from the GUI thread
     * @param activity
     *      the activity under which we are running
     */
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
        this.endTurn = (Button)activity.findViewById(R.id.endTurn);
        this.helpBtn = (Button)activity.findViewById(R.id.help_Button);
        this.card1 = (ImageButton)activity.findViewById(R.id.imageButton5);
        this.card2 = (ImageButton)activity.findViewById(R.id.imageButton6);
        this.card3 = (ImageButton)activity.findViewById(R.id.imageButton7);
        this.card4 = (ImageButton)activity.findViewById(R.id.imageButton8);
        this.card5 = (ImageButton)activity.findViewById(R.id.imageButton9);
        this.player1 = (ImageButton)activity.findViewById(R.id.player1);
        this.player2 = (ImageButton)activity.findViewById(R.id.player2);
        this.player3 = (ImageButton)activity.findViewById(R.id.player3);
        this.discardPileBtn =
                (ImageButton)activity.findViewById(R.id.discardPile);
        this.deckBtn =
                (ImageButton)activity.findViewById(R.id.deck);
        this.player1Label =
                (TextView)activity.findViewById((R.id.player1_label));
        this.player2Label =
                (TextView)activity.findViewById(R.id.player2_label);
        this.player3Label =
                (TextView)activity.findViewById(R.id.player3_label);
        this.player0CardCount =
                (TextView)activity.findViewById(R.id.player0cards);
        this.player1CardCount =
                (TextView)activity.findViewById(R.id.player1cards);
        this.player2CardCount =
                (TextView)activity.findViewById(R.id.player2cards);
        this.player3CardCount =
                (TextView)activity.findViewById(R.id.player3cards);
        this.turnText = (TextView)activity.findViewById(R.id.turntext);
        this.cardsToDraw = (TextView)activity.findViewById(R.id.cardstodraw);
        this.cardsInDeck = (TextView)activity.findViewById(R.id.cardsindeck);
        this.playerTurn = (TextView)activity.findViewById(R.id.playerTurn);
        this.displayCards = (TextView)activity.findViewById(R.id.displayCards);

        // listen for button presses
        leftScroll.setOnClickListener(this);
        rightScroll.setOnClickListener(this);
        trade2Btn.setOnClickListener(this);
        trade3Btn.setOnClickListener(this);
        trade5Btn.setOnClickListener(this);
        enterBtn.setOnClickListener(this);
        endTurn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);
        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        card5.setOnClickListener(this);
        player1.setOnClickListener(this);
        player2.setOnClickListener(this);
        player3.setOnClickListener(this);
        discardPileBtn.setOnClickListener(this);
        deckBtn.setOnClickListener(this);

         /*
        initialize the imagesHand array with the now initialized image buttons
         */
        imagesHand[0] = card1;
        imagesHand[1] = card2;
        imagesHand[2] = card3;
        imagesHand[3] = card4;
        imagesHand[4] = card5;
    } //setAsGui method

    /**
     * There are 17 different buttons that can be selected. They are grouped
     * into instances of buttons and instances of image buttons. To help you
     * find the button you are looking for quickly here are them in order
     * button: leftScroll, rightScroll, trade2Btn, trade3Btn, trade5Btn,
     * playBtn, enterBtn
     * imageButtons: discardPileBtn, card1, card2, card3, card4, card5,
     * player1, player2, player3
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
            if(button == helpBtn){

                /**
                 * External Citation
                 * Problem: Wanted to create a popup window for help
                 * Source: ThreeThirteenTeam help_button code
                 * Solution: used their code as a framework to create our
                 * own help window
                 */


                LayoutInflater inflater = (LayoutInflater)
                        myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //Set the layout of the view to the help window
                View popupView = inflater.inflate(R.layout.help_window, null);


                // create popup window that will match the dimensions of the screen
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.MATCH_PARENT;
                boolean focusable = true;
                final PopupWindow helpWindow = new PopupWindow(popupView, width, height, focusable);

                // show popup window
                helpWindow.showAtLocation(button, Gravity.CENTER, 10, 10);

                // dismiss the popup window when the screen is touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        helpWindow.dismiss();
                        return true;
                    }
                });
            }
            else if (button == leftScroll) {
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

            /*
            trade 2 button sets the trade booleans and resets the play hand
            for selecting the cards to trade
             */
            else if (button == trade2Btn) {
                if (trade2) {
                    trade2 = false;
                    trade2Btn.setText("Trade 2 Off");
                }
                else {
                    trade2 = true;
                    trade2Btn.setText("Trade 2 On");
                }
                trade3 = false;
                trade3Btn.setText("Trade 3 Off");
                trade5 = false;
                trade5Btn.setText("Trade 5 Off");
                // deselect all player hand cards
                for (int a = 0; a < state.getPlayerHand(this.playerNum).size(); a++) {
                    state.getPlayerHand(this.playerNum).get(a).setSelected(false);
                }
            } // trade2 button

             /*
            trade 3 button sets the trade booleans and resets the play hand
            for selecting the cards to trade
             */
            else if (button == trade3Btn) {
                trade2 = false;
                trade2Btn.setText("Trade 2 Off");
                if (trade3) {
                    trade3 = false;
                    trade3Btn.setText("Trade 3 Off");
                    trade3Stage = 0;
                }
                else {
                    trade3 = true;
                    trade3Btn.setText("Trade 3 On");
                    trade3Stage = 1;
                }
                trade5 = false;
                trade5Btn.setText("Trade 5 Off");
                // deselect all player hand cards
                for (int a = 0; a < state.getPlayerHand(this.playerNum).size(); a++) {
                    state.getPlayerHand(this.playerNum).get(a).setSelected(false);
                }
            } // trade3 button

             /*
            trade 5 button sets the trade booleans and resets the play hand
            for selecting the cards to trade
             */
            else if (button == trade5Btn) {
                trade2 = false;
                trade2Btn.setText("Trade 2 Off");
                trade3 = false;
                trade3Btn.setText("Trade 3 Off");
                if(state.getDiscardPile().size() == 0){
                    return;
                }
                if (trade5) {
                    trade5 = false;
                    trade5Btn.setText("Trade 5 Off");
                }
                else {
                    trade5 = true;
                    trade5Btn.setText("Trade 5 On");
                }
                // deselect all player hand cards
                for (int a = 0; a < state.getPlayerHand(this.playerNum).size(); a++) {
                    state.getPlayerHand(this.playerNum).get(a).setSelected(false);
                }
            } // trade5 button

            /*
            play button requires that 1 card is selected and it then executes
             the action for that card. Certain cards have no action and are
             used for trading only.
             */
            else if(button == enterBtn && trade2 == false && trade3 == false && trade5 == false && switchedDiscard == false && seeTheFutHand == false) {
                // check to see that only 1 card is selected
                int a = 0;
                int index = 0;
                for (int i = 0; i < state.getCurrentPlayerHand().size(); i++) {
                    if (state.getCurrentPlayerHand().get(i).getSelected()) {
                        a++;
                        index = i;
                    }
                }
                if (a == 1) {
                    //find what card is too be played
                    switch(state.getCurrentPlayerHand().get(index).getCardType()) {
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
                return;
            }

            /*
            enter button is pressed when the player has already selected a
            trade button and is submitting their selections for trading.
             */
            else if (button == enterBtn) {
                //Set the text of the button back to it's original text
                this.enterBtn.setText("Enter");

                int numSelected = 0;
                int c = 0;
                for(int i = 0; i < 5; i++){
                    cardHand[i] = i;
                }
                playCard();

                /*
                trade 2, check to make sure that only two cards are selected
                then sends the action
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
                    trade2Btn.setText("Trade 2 Off");
                    for (int p = 0; p < 5; p++) {
                        tradeCards[p] = 0;
                    }
                }
               /*
                trade 3 happens in two parts, when the player is selecting
                the cards to trade and the target player and when the player
                is selecting the target card. The first part when trade3Stage
                 = 1 remembers what cards the player will trade. The second
                 part when trade3Stage = 2 remembers what card the player
                 would like to steal and then sends the game action of trade 3
                 */
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
                    trade3Btn.setText("Trade 3 Off");
                    trade3Stage = 0;
                    for (int p = 0; p < 5; p++) {
                        tradeCards[p] = 0;
                    }
                }

                /*
                trade 5, select the 5 cards to trade and the discard pile
                card to receive and send the trade 5 action to the game
                 */
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
                    int cardPos = 0;
                    for (int a = 0; a < state.getDiscardPile().size(); a++) {
                        if (state.getDiscardPile().get(a).getSelected() && once == 0) {
                            once++;
                            cardPos = a;
                        }
                    }
                    Trade5Action trade5Act = new Trade5Action(this,
                            tradeCards[0], tradeCards[1], tradeCards[2],
                            tradeCards[3], tradeCards[4], cardPos);
                    game.sendAction(trade5Act);
                    trade5 = false;
                    trade5Btn.setText("Trade 5 Off");
                    for (int p = 0; p < 5; p++) {
                        tradeCards[p] = 0;
                    }
                }

                //see the future card ticked
                else if (seeTheFutHand) {
                    seeTheFutHand = false;
                    displayDeck = false;
                }

            } // enter button

            /*
            end turn button that resets all of the selected cards to not
            selected and sends the draw card action to the game
             */
            else if (button == endTurn || button == deckBtn) {

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
                // reset the trade booleans
                trade2 = false;
                trade3 = false;
                trade5 = false;
                trade2Btn.setText("Trade 2 Off");
                trade3Btn.setText("Trade 3 Off");
                trade5Btn.setText("Trade 5 Off");
                DrawCardAction drawCard = new DrawCardAction(this);
                game.sendAction(drawCard);
            } // endTurn button
            int queryT = 0;
        } // if statement for instance of button

        // for all image buttons
        else if (button instanceof ImageButton) {

            /*
            discard pile button sets the switchedDiscard boolean to true or not
             */
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
                }
                updateDisplay();
            } //discard pile button

            /*
            card 1 image button that selects or deselcts the card object in
            the correct arralist, either the playershand, discardPile, or the
             allCards array
             */
            else if (button == card1) {
                /*
                check to see if the player hand previously selected the image
                 button in question. Reverse the current selection state of
                 the card
                 */
                if (switchedDiscard == false && trade3Stage != 2) {
                    if(state.getCurrentPlayerHand().size() <= 0){
                        return;
                    }
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
                    if(state.getDiscardPile().size() == 0){
                        return;
                    }
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

            /*
            card 2 image button that selects or deselcts the card object in
            the correct arralist, either the playershand, discardPile, or the
             allCards array
             */
            else if (button == card2) {
                /*
                check to see if the player hand previously selected the image
                 button in question. Reverse the current selection state of
                 the card
                 */
                if (switchedDiscard == false && trade3Stage != 2) {
                    if(state.getCurrentPlayerHand().size() <= 1){
                        return;
                    }
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
                    if(state.getDiscardPile().size() <= 1){
                        return;
                    }
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

            /*
            card 3 image button that selects or deselcts the card object in
            the correct arralist, either the playershand, discardPile, or the
             allCards array
             */
            else if (button == card3) {
                /*
                check to see if the player hand previously selected the image
                 button in question. Reverse the current selection state of
                 the card
                 */
                if (switchedDiscard == false && trade3Stage != 2) {
                    if(state.getCurrentPlayerHand().size() <= 2){
                        return;
                    }
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
                    if(state.getDiscardPile().size() <= 2){
                        return;
                    }
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

            /*
            card 4 image button that selects or deselcts the card object in
            the correct arralist, either the playershand, discardPile, or the
             allCards array
             */
            else if (button == card4) {
                /*
                check to see if the player hand previously selected the image
                 button in question. Reverse the current selection state of
                 the card
                 */
                if (switchedDiscard == false && trade3Stage != 2) {
                    if(state.getCurrentPlayerHand().size() <= 3){
                        return;
                    }
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
                    if(state.getDiscardPile().size() <= 3){
                        return;
                    }
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

            /*
            card 5 image button that selects or deselcts the card object in
            the correct arralist, either the playershand, discardPile, or the
             allCards array
             */
            else if (button == card5) {
                /*
                check to see if the player hand previously selected the image
                 button in question. Reverse the current selection state of
                 the card
                 */
                if (switchedDiscard == false && trade3Stage != 2) {
                    if(state.getCurrentPlayerHand().size() <= 4){
                        return;
                    }
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
                    if(state.getDiscardPile().size() <= 4){
                        return;
                    }
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

            /*
            player 2 image button that can be selected but only if player 2
            has not lost the game yet, if they have find the next player that
             is still in the game and set to them
             */
            else if (button == player1) {
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
            } // player1 button

            /*
            player 3 image button that can be selected but only if player 2
            has not lost the game yet, if they have find the next player that
             is still in the game and set to them
             */
            else if (button == player2) {
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

            /*
            player 4 image button that can be selected but only if player 2
            has not lost the game yet, if they have find the next player that
             is still in the game and set to them
             */
            else if (button == player3) {
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

    /*=============
     */

    public void playCard() {
        // check to make sure that this is not a trade or discard pile
        if (trade2 == false && trade3 == false && trade5 == false && switchedDiscard == false) {
            // check to see that only 1 card is selected
            int a = 0;
            int index = 0;
            for (int i = 0; i < state.getCurrentPlayerHand().size(); i++) {
                if (state.getCurrentPlayerHand().get(i).getSelected()) {
                    a++;
                    index = i;
                }
            }
            if (a == 1) {
                //find what card is too be played
                switch(state.getCurrentPlayerHand().get(index).getCardType()) {
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

    protected void setPlayersText(){
        //set the human text
        setPlayer0Text();

        //Set the text depending on how many players are in the game
        switch(state.getNumPlayers()){
            case 2:
                setPlayer1Text(true);
                setPlayer2Text(false);
                setPlayer3Text(false);
                break;
            case 3:
                setPlayer1Text(true);
                setPlayer2Text(true);
                setPlayer3Text(false);
                break;
            case 4:
                setPlayer1Text(true);
                setPlayer2Text(true);
                setPlayer3Text(true);
            default:
                setPlayer1Text(true);
                setPlayer2Text(true);
                setPlayer3Text(true);
                break;
        }

    }

    //Visually put Player 1 in the game if they are playing
    protected void drawPlayer1(boolean isPlaying){
        if(isPlaying) {
            if(state.getPlayerHand(1).size() != 0){
                if (tradePlayer == 1 && !state.hasPlayerLost(1)) {
                    player1.setImageResource(R.drawable.selectcardback);
                }
                else if (state.hasPlayerLost(1)) {
                    player1.setImageResource(R.drawable.cardbacklost);
                    player1CardCount.setText(allPlayerNames[1] + " has lost the game");
                } else {
                    player1.setImageResource(R.drawable.cardback);
                }

            }
            else if (tradePlayer == 1) {
                player1.setImageResource(R.drawable.selectcardback);
            } else {
                player1.setImageResource(R.drawable.cardback);
            }
        }
        else{
            player1.setImageResource(R.drawable.blankcard);
            player1.setClickable(false);
        }
    }
    //Visually put Player 2 in the game if they are playing
    protected void drawPlayer2(boolean isPlaying){
        if(isPlaying) {
            if(state.getPlayerHand(2).size() != 0){
                if (tradePlayer == 2 && !state.hasPlayerLost(2)) {
                    player2.setImageResource(R.drawable.selectcardback);
                }
                else if (state.hasPlayerLost(2)) {
                    player2.setImageResource(R.drawable.cardbacklost);
                    player2CardCount.setText(allPlayerNames[2] + " has lost the game");
                } else {
                    player2.setImageResource(R.drawable.cardback);
                }

            }
            else if (tradePlayer == 2) {
                player2.setImageResource(R.drawable.selectcardback);
            } else {
                player2.setImageResource(R.drawable.cardback);
            }
        }
        else{
            player2.setImageResource(R.drawable.blankcard);
            player2.setClickable(false);
        }
    }

    //Visually put Player 3 in the game if they are playing
    protected void drawPlayer3(boolean isPlaying){
        if(isPlaying) {
            if(state.getPlayerHand(3).size() != 0){
                if (tradePlayer == 3 && !state.hasPlayerLost(3)) {
                    player3.setImageResource(R.drawable.selectcardback);
                }
                else if (state.hasPlayerLost(3)) {
                    player3.setImageResource(R.drawable.cardbacklost);
                    player3CardCount.setText(allPlayerNames[3] + " has lost the game");
                } else {
                    player3.setImageResource(R.drawable.cardback);
                }

            }
            else if (tradePlayer == 3) {
                player3.setImageResource(R.drawable.selectcardback);
            } else {
                player3.setImageResource(R.drawable.cardback);
            }
        }
        else{
            player3.setImageResource(R.drawable.blankcard);
            player3.setClickable(false);
        }
    }
    //Set's the text of Human Player depending on if they are playing or not
    protected void setPlayer0Text(){
        if(!state.hasPlayerLost(0)){
            player0CardCount.setText("Your Card Count: " + state.getPlayerHand(0).size());
            cardsToDraw.setText("Cards to Draw This Turn: " + state.getCardsToDraw());
        }
        else{
            player0CardCount.setText("You Lost");
            cardsToDraw.setText("");
        }
    }

    //Set's the text of Player 1 depending on if they are playing or not
    protected void setPlayer1Text(boolean isPlaying){
        if(isPlaying){
            if(!state.hasPlayerLost(1)) {
                player1Label.setText(allPlayerNames[1]);
                player1CardCount.setText("Card Count: " + state.getPlayerHand(1).size());
            }
        }
        else{
            player1Label.setText(" ");
            player1CardCount.setText(" ");
        }
    }

    //Set's the text of Player 2 depending on if they are playing or not
    protected void setPlayer2Text(boolean isPlaying){
        if(isPlaying){
            if(!state.hasPlayerLost(2)) {
                player2Label.setText(allPlayerNames[2]);
                player2CardCount.setText("Card Count: " + state.getPlayerHand(2).size());
            }
        }
        else{
            player2Label.setText(" ");
            player2CardCount.setText(" ");
        }
    }

    //Set's the text of Player 3 depending on if they are playing or not
    protected void setPlayer3Text(boolean isPlaying){
        if(isPlaying){
            if(!state.hasPlayerLost(3)) {
                player3Label.setText(allPlayerNames[3]);
                player3CardCount.setText("Card Count: " + state.getPlayerHand(3).size());
            }
        }
        else{
            player3Label.setText(" ");
            player3CardCount.setText(" ");
        }
    }

}


