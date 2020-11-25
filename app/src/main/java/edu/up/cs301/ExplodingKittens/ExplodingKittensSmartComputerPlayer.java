package edu.up.cs301.ExplodingKittens;

import java.util.ArrayList;

import edu.up.cs301.ExplodingKittens.EKActions.DrawCardAction;
import edu.up.cs301.ExplodingKittens.EKActions.PlayAttackCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayFavorCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayFutureCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayNopeCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayShuffleCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlaySkipCard;
import edu.up.cs301.ExplodingKittens.EKActions.Trade2Action;
import edu.up.cs301.ExplodingKittens.EKActions.Trade3Action;
import edu.up.cs301.ExplodingKittens.EKActions.Trade5Action;
import edu.up.cs301.game.GameFramework.GameComputerPlayer;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;

/**
 *An Exploding Kittens Smart Computer Player Class
 *Computer acts based on probability, available cards, and previous player
 * actions
 *
 * @author Samuel Warrick
 * @author Kaulu Ng
 * @author Chandler Lau
 * @version November 2020
 */

public class ExplodingKittensSmartComputerPlayer extends GameComputerPlayer {

    private double probability;
    private double random;
    private int STFDeckSize = 0;
    private int cardToPlayPos;
    private ArrayList<Integer> STFArray;
    private EKGameState computerState;
    private boolean previousSTF;
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public ExplodingKittensSmartComputerPlayer(String name) {
        super(name);
        this.probability = 0;
        this.random = 0;
        this.cardToPlayPos = 0;
        this.STFDeckSize = 0;
        this.STFArray = new ArrayList<Integer>();
        this.previousSTF = false;
    }

    //receiveInfo: Computer takes in EKGameState to decide the course of action
    @Override
    protected void receiveInfo(GameInfo info) {

        if(!(info instanceof EKGameState)){ return;}
        this.computerState = (EKGameState) info;

        //check to see if it's this player's turn
        if(this.computerState.getWhoseTurn() != this.playerNum){
            return;
        }
       else if(this.computerState.getWhoseTurn() == this.playerNum) {
           if(checkForDefuse() == false){
               if(Trade5() == true){
                   return;
               };
           }

                //If next card is EK based on STF play a card
                if (nextCardEK() == true) {
                    if (checkForPlayableCard() == true) {
                        playCard(this.computerState, cardToPlayPos);
                    }
                    else {
                        if (getACard(this.computerState) == true) {
                            if (checkForPlayableCard() == true) {
                                //playCard(this.computerState, cardToPlayPos);
                                return;
                            }
                        }
                    }
                }


                //generates random number from 0 - 100
                random = (double) Math.random() * 101;
                probability =
                        (((double)computerState.getEKCount()*computerState.getCardsToDraw()) / (double)computerState.getDeck().size()) * 100;
                if(checkForDefuse() == false){
                    probability = probability *1.5;
                }

                //checks previous turns and acts accordingly
                checkPreviousTurns();

                //if random # is within probability play a card
                //otherwise draw
                if (random <= probability) {
                    //play a card: Skip, Attack, STF -> Shuffle
                    //If none, try to get a card (Trade 2, 3, 5, or favor)
                    // (Prioritize Defuse)
                    //else draw a card
                        if (computerState.getCurrentPlayerHand() != null) {
                            if (checkForPlayableCard() == true) {
                                playCard(this.computerState, this.cardToPlayPos);
                            } else {
                                if (getACard(this.computerState) == true) {
                                    return;
                                }
                            }
                        }
                }
                else {
                    //draw a card
                    DrawCardAction draw = new DrawCardAction(this);
                    this.game.sendAction(draw);
                }
                DrawCardAction draw = new DrawCardAction(this);
                this.game.sendAction(draw);
        }

    }//receive info

    //playCard: Used to play a card based on the computers current situation
    private void playCard(EKGameState computerState, int CardPos){
        if(checkForPlayableCard() == true) {
            //Check to see which card was selected by the card selection process
            switch (computerState.getCurrentPlayerHand().get(CardPos).getCardType()) {
                case 6:
                    PlayAttackCard attack = new PlayAttackCard(this);
                    this.game.sendAction(attack);
                    break;
                case 7:
                    PlayShuffleCard shuffle = new PlayShuffleCard(this);
                    this.game.sendAction(shuffle);
                    DrawCardAction draw = new DrawCardAction(this);
                    this.game.sendAction(draw);
                    break;
                case 9:
                    PlaySkipCard skip = new PlaySkipCard(this);
                    this.game.sendAction(skip);
                    break;
                case 10:
                    PlayFutureCard future = new PlayFutureCard(this);
                    this.game.sendAction(future);
                    seeTheFuture();
                    break;
                default:
                    DrawCardAction drawCard = new DrawCardAction(this);
                    this.game.sendAction(drawCard);
                    break;
            }
        }
        else{
            if(getACard(this.computerState) == true){
                if(checkForPlayableCard() == true) {
                    playCard(this.computerState, cardToPlayPos);
                }
            }
            else{
                DrawCardAction draw = new DrawCardAction(this);
                this.game.sendAction(draw);
            }
        }
        }//playCard()

    //seeTheFuture
    //Updates See the Future array
    //If card is not EK then draw
    //If cars is EK then play card
    public void seeTheFuture(){
        int cardsToAdd = 3;
        //add top 3 cards of deck to STF array
        if(computerState.getDeck().size() < 3){
            cardsToAdd = computerState.getDeck().size();
        }

        for(int i = 0; i < cardsToAdd; i++) {
            this.STFArray.add(this.computerState.getDeck().get(i).getCardType());
        }

        //save current deck size
        this.STFDeckSize = this.computerState.getDeck().size();

        //if next card is an EK attempt to play a card otherwise draw a card
        if(computerState.getDeck().get(0).getCardType() == 0){
            if(checkForPlayableCard() == true){
                playCard(this.computerState, cardToPlayPos);
            }
            else{
                if(getACard(this.computerState) == true){
                    playCard(this.computerState, cardToPlayPos);
                }
                else{
                    DrawCardAction draw = new DrawCardAction(this);
                    game.sendAction(draw);
                }
            }
        }
        else{
            DrawCardAction draw = new DrawCardAction(this);
            this.game.sendAction(draw);
        }
    } //seeTheFuture

    //getACard(): Used if player doesn't have a playable card but needs one
    public boolean getACard(GameInfo info){

        //targets player with highest amount of cards in hand
        int counter = 0;
        int target = 0;
        for(int i = 0; i < this.computerState.getNumPlayers(); i++){
            if(i != this.playerNum){
                if(this.computerState.getPlayerHand(i).size() > counter){
                    target = i;
                }
            }
        }

        if(this.computerState.getPlayerHand(target).size() != 0){
            while(this.computerState.getPlayerHand(target).get(0).getCardType() == 0){
                    if (target == computerState.getNumPlayers() - 1) {
                        target = 0;
                    }
                    else {
                        target = target + 1;
                    }
                while(this.computerState.getPlayerHand(target).size() == 0){
                    if(target == computerState.getNumPlayers() - 1){
                        target = 0;
                    }
                    else {
                        target = target + 1;
                    }
                }
            }

        }

        //play favor on target if you have it
        for(int x = 0; x < this.computerState.getCurrentPlayerHand().size(); x++) {
            if (this.computerState.getCurrentPlayerHand().get(x).getCardType() == 8) {
                int randomCardPos =
                        (int)(Math.random()*this.computerState.getPlayerHand(target).size());
                PlayFavorCard favor = new PlayFavorCard(this, target, randomCardPos);
                this.game.sendAction(favor);
                return true;
            }
        }

        //Trade 3
        if(checkForMatches(3) != -1){
            int cardPos1 = -1;
            int cardPos2 = -1;
            int cardPos3 = -1;
            for(int i = 0; i < this.computerState.getCurrentPlayerHand().size(); i++){
                if(this.computerState.getCurrentPlayerHand().get(i).getCardType() == checkForMatches(3)){
                    if(cardPos1 != -1){
                        if(cardPos2 != -1){
                            cardPos3 = i;
                        }
                        else{
                            cardPos2 = i;
                        }
                    }
                    else{
                        cardPos1 = i;
                    }
                }
            } //for loop

            //chooses random card from 6 - 10 to decide what card to ask for
            int randomCard = (int) (Math.random() * 5) + 6;
            Trade3Action trade3 = new Trade3Action(this, target, cardPos1,
                    cardPos2, cardPos3, randomCard);
            this.game.sendAction(trade3);
            return true;
        }

        //play trade 2 if you have 2 of the same card
        if(checkForMatches(2) != -1){
            int cardPos1 = -1;
            int cardPos2 = -1;
            for(int i = 0; i < this.computerState.getCurrentPlayerHand().size(); i++){
                if(this.computerState.getCurrentPlayerHand().get(i).getCardType() == checkForMatches(2)){
                    if (cardPos1 != -1){
                        cardPos2 = i;
                    }
                    else{
                        cardPos1 = i;
                    }
                }
            }
            Trade2Action trade2 = new Trade2Action(this, target, cardPos1, cardPos2);
            this.game.sendAction(trade2);
            return true;
        } //trade 2 cards logic

        //Trade 5
        if(Trade5() == true){
            return true;
        }

        return false;
    } //getCard


    //Checks if there are numOfMatches of the same card in player hand
    //returns -1 if there are less than numOfMatches of a card
    //returns the CardType of a card with the amount of matches
    public int checkForMatches(int numOfMatches){
        for(int a = 1; a < 12; a++) {
            int counter = 0;
            for (int i = 0; i < this.computerState.getCurrentPlayerHand().size(); i++) {
                if(this.computerState.getCurrentPlayerHand().get(i).getCardType() == a){
                    counter++;
                    if(counter >= numOfMatches){
                        return a;
                    }
                }
            }
        }
        return -1;
    } //checkForMatches

    //Checks if the player has a defuse in hand
    public boolean checkForDefuse(){
        for(int i = 0; i < this.computerState.getCurrentPlayerHand().size(); i++){
            if(this.computerState.getCurrentPlayerHand().get(i).getCardType() == 12){
                return true;
            }
        }
        return false;
    }//checkForDefuse

    //Checks if next card is an EK based on STF card
    public boolean nextCardEK(){
        //checks that STF Array isn't empty
        if(this.STFArray.size() == 0){
            return false;
        }
        else if(this.STFArray.size() != 0) {
            //Updating STFArray
            //If deck size changed since STF activated, update array accordingly
            if (this.computerState.getDeck().size() < this.STFDeckSize) {
                //if deck is smaller than STF deck size remove cards from
                // STFArray accordingly
                if (this.STFDeckSize - this.computerState.getDeck().size() >= 3) {
                    this.STFArray.clear();
                    return false;
                } else if (this.STFDeckSize - this.computerState.getDeck().size() < 3) {
                    if (this.STFDeckSize - this.computerState.getDeck().size() != 0) {
                        for (int i = 0; i < this.STFDeckSize - this.computerState.getDeck().size(); i++) {
                            if(this.STFArray.size() != 0) {
                                this.STFArray.remove(0);
                            }
                        }
                        this.STFDeckSize = this.computerState.getDeck().size();
                    }
                }
            }

            //checks to see if deck has been altered since see the future
            //was activated and if STF array still relevant
            for (int i = 0; i < this.STFArray.size(); i++) {
                if (this.STFArray.get(i) != this.computerState.getDeck().get(i).getCardType()) {
                    this.STFArray.clear();
                    return false;
                }
            }

            //if next card is EK return true
            if(this.STFArray.size() != 0) {
                if (this.STFArray.get(0) == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    } //nextCardEK

    //Checks if player has a playable card that's good for the situation
    public boolean checkForPlayableCard(){
        //if you know next card is an Exploding Kitten
        if(nextCardEK() == true || previousSTF == true){
            for(int i = 0; i < this.computerState.getCurrentPlayerHand().size(); i++){
                //check for cards SKIP ATTACK SHUFFLE
                switch(this.computerState.getCurrentPlayerHand().get(i).getCardType()){
                    case 6:
                        this.cardToPlayPos = i;
                        return true;
                    case 7:
                        this.cardToPlayPos = i;
                        return true;
                    case 9:
                        this.cardToPlayPos = i;
                        return true;
                }
            }
            return false;
        }
        //If computer doesn't know if next card is an EK
        else{
            for(int i = 0; i < this.computerState.getCurrentPlayerHand().size(); i++){
                //check for cards SKIP ATTACK STF
                switch(this.computerState.getCurrentPlayerHand().get(i).getCardType()){
                    case 6:
                        this.cardToPlayPos = i;
                        return true;
                    case 9:
                        this.cardToPlayPos = i;
                        return true;
                    case 10:
                        this.cardToPlayPos = i;
                        return true;
                }
            }
            return false;
        }
    }//checkForPlayableCard

    //Trade 5
    //Checks if player's capable of trading 5 and does it if possible
    public boolean Trade5(){

        int cardPos1 = -1;
        int cardPos2 = -1;
        int cardPos3 = -1;
        int cardPos4 = -1;
        int cardPos5 = -1;
        int counter = 0;
        boolean found5Unique = false;

        ArrayList<Integer> trade5Array = new ArrayList<Integer>();
        //check to see if there are 5 unique cards in hand to trade
        if(this.computerState.getCurrentPlayerHand().size() < 5){
            return false;
        }
        for(int i = 0; i < this.computerState.getCurrentPlayerHand().size(); i++){
            boolean uniqueCard = true;

            for(int a = 0; a < trade5Array.size(); a++){
                if(this.computerState.getCurrentPlayerHand().get(i).getCardType() == trade5Array.get(a)){
                    uniqueCard = false;
                }
            }

            if(uniqueCard == true){
                trade5Array.add(this.computerState.getCurrentPlayerHand().get(i).getCardType());
                if(cardPos1 != -1){
                    if(cardPos2 != -1){
                        if(cardPos3 != -1){
                            if(cardPos4 != -1){
                                if(cardPos5 != -1){
                                }
                                else {
                                    cardPos5 = i;
                                }
                            }
                            else{
                                cardPos4 = i;
                            }
                        }
                        else {
                            cardPos3 = i;
                        }
                    }
                    else{
                        cardPos2 = i;
                    }
                }
                else{
                    cardPos1 = i;
                }

                counter++;
                if(counter >= 5){
                    found5Unique = true;
                }
            }

        }
        //If the player has 5 unique cards and it knows an EK is next, try to
        // get a card from discard pile
        if(found5Unique == true){
            int discardPos = 0;
            if(nextCardEK()){
                for(int b = 0; b < this.computerState.getDiscardPile().size(); b++){
                    switch(this.computerState.getDiscardPile().get(b).getCardType()){
                        case 6:
                            discardPos = b;
                            break;
                        case 7:
                            discardPos = b;
                            break;
                        case 8:
                            discardPos = b;
                            break;
                        case 9:
                            discardPos = b;
                            break;
                        case 10:
                            discardPos = b;
                            break;
                        case 12:
                            discardPos = b;
                            break;
                    }
                }
                Trade5Action trade = new Trade5Action(this, cardPos1,
                        cardPos2, cardPos3, cardPos4, cardPos5, discardPos);
                this.game.sendAction(trade);
                return true;
            }

            //If player doesn't have a defuse, try to get one from the
            // discard pile
            if(checkForDefuse() == false){
                for(int c = 0; c < this.computerState.getDiscardPile().size(); c++){
                    if(this.computerState.getDiscardPile().get(c).getCardType() == 12){
                        Trade5Action trade = new Trade5Action(this, cardPos1,
                                cardPos2, cardPos3, cardPos4, cardPos5, c);
                        this.game.sendAction(trade);
                        return true;
                    }
                }
            }
        }
        else{
            return false;
        }
        //if you have a defuse then skip this
        return false;
    }//Trade5

    //Checks the actionsPerformed array to check if there are any previous
    // actions that the computer can use to decide it's next move
    public void checkPreviousTurns(){

        if(this.computerState.getActionsPerformed() == null){
            return;
        }
        if(this.computerState.getActionsPerformed().size() == 0){
            return;
        }
        int actionTracker = this.computerState.getActionsPerformed().size() - 1;
        //if a STF was activated and players skipped, play a card because the
        // next is probably EK
        if(this.computerState.getActionsPerformed().get(actionTracker) == 6 ||
                this.computerState.getActionsPerformed().get(actionTracker) == 9){

            while(this.computerState.getActionsPerformed().get(actionTracker) == 6 || this.computerState.getActionsPerformed().get(actionTracker) == 9 ||
                    this.computerState.getActionsPerformed().get(actionTracker) == 11){
                    actionTracker = actionTracker - 1;
                if(this.computerState.getActionsPerformed().get(actionTracker) == 10){
                    //set previous stf so that the computer knows someone
                    // played STF and skipped their turn
                    previousSTF = true;

                    //if player has nope, play nope or another card
                    if(this.computerState.getCurrentPlayerHand().size() != 0){
                        for(int i = 0; i < this.computerState.getCurrentPlayerHand().size(); i++){
                            if(this.computerState.getCurrentPlayerHand().get(i).getCardType() == 11){
                                PlayNopeCard nope = new PlayNopeCard(this);
                                this.game.sendAction(nope);
                            }
                            else {
                                    if (checkForPlayableCard() == true) {
                                        playCard(computerState, cardToPlayPos);
                                    }
                                    else if(getACard(computerState) == true) {
                                            if (checkForPlayableCard() == true) {
                                                playCard(computerState, cardToPlayPos);
                                            }
                                        }
                            }

                        }
                    }

                    else if(checkForPlayableCard() == true){
                        playCard(this.computerState, cardToPlayPos);
                    }
                }
                else{
                    previousSTF = false;
                }
            }

            //if probable that next card is EK, and previous player attack,
            // skipped, or noped, play nope card
            if(random <= probability){
                if(this.computerState.getCurrentPlayerHand().size() != 0){
                    for(int i = 0; i < this.computerState.getCurrentPlayerHand().size(); i++){
                        if(this.computerState.getCurrentPlayerHand().get(i).getCardType() == 11){
                            PlayNopeCard nope = new PlayNopeCard(this);
                            this.game.sendAction(nope);
                        }
                    }
                }
            }
        }


    }//checkPreviousTurns

}
