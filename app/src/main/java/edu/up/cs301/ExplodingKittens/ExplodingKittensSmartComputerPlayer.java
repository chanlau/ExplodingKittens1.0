package edu.up.cs301.ExplodingKittens;

import java.util.ArrayList;

import edu.up.cs301.ExplodingKittens.EKActions.DrawCardAction;
import edu.up.cs301.ExplodingKittens.EKActions.PlayAttackCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayFavorCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayFutureCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayShuffleCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlaySkipCard;
import edu.up.cs301.ExplodingKittens.EKActions.Trade2Action;
import edu.up.cs301.ExplodingKittens.EKActions.Trade3Action;
import edu.up.cs301.ExplodingKittens.EKActions.Trade5Action;
import edu.up.cs301.game.GameFramework.GameComputerPlayer;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;

public class ExplodingKittensSmartComputerPlayer extends GameComputerPlayer {

    int probability;
    int random;
    int STFDeckSize = 0;
    int cardToPlayPos;
    ArrayList<Integer> STFArray;
    EKGameState computerState;
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public ExplodingKittensSmartComputerPlayer(String name) {
        super(name);
        probability = 0;
        random = 0;
        cardToPlayPos = 0;
        STFDeckSize = 0;
        STFArray = new ArrayList<Integer>();
    }

    @Override
    protected void receiveInfo(GameInfo info) {

        if(!(info instanceof EKGameState)){ return;}
        EKGameState computerState = (EKGameState) info;

        //check to see if it's this player's turn
        if(computerState.getWhoseTurn() != this.playerNum){
            return;
        }
        if(computerState.getWhoseTurn() == this.playerNum) {
                //If next card is EK based on STF play a card
                if (nextCardEK(info) == true) {
                    if (checkForPlayableCard(info) == true) {
                        playCard(info, cardToPlayPos);
                    }
                }

                //generates random number from 0 - 100
                random = (int) Math.random() * 101;
                probability =
                        (computerState.getDeck().size() / computerState.getEKCount()) * 100;

                if (random <= probability) {
                    //play a card: Skip, Attack, STF -> Shuffle
                    //If none, try to get a card (Trade 2, 3, 5, or favor)
                    // (Prioritize Defuse)
                    //else draw a card
                    if (computerState.getCurrentPlayerHand() != null) {
                        if (checkForPlayableCard(info) == true) {
                            playCard(info, this.cardToPlayPos);
                        }
                    }
                }
                else {
                    //draw a card
                    DrawCardAction draw = new DrawCardAction(this);
                    this.game.sendAction(draw);
                }
        }

    }//receive info

    private void playCard(EKGameState computerState, int CardPos){


        if(checkForPlayableCard(this.computerState) == true) {
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
                    seeTheFuture(this.computerState);
                    break;
                default:
                    DrawCardAction drawCard = new DrawCardAction(this);
                    this.game.sendAction(drawCard);
                    break;
            }
        }
        else{
            if(getACard(this.computerState) == true){
                if(checkForPlayableCard(this.computerState) == true) {
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
    public void seeTheFuture(GameInfo info){

        //add top 3 cards of deck to STF array
        for(int i = 0; i < 3; i++) {
            this.STFArray.add(this.computerState.getDeck().get(i).getCardType());
        }

        //save current deck size
        this.STFDeckSize = this.computerState.getDeck().size();

        //if next card is an EK attempt to play a card otherwise draw a card
        if(computerState.getDeck().get(0).getCardType() == 0){
            if(checkForPlayableCard(info) == true){
                playCard(this.computerState, cardToPlayPos);
            }
            else{
                if(getACard(info) == true){
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

    public boolean getACard(GameInfo info){
        if(!(info instanceof EKGameState)){ return false;}
        EKGameState computerState = (EKGameState) info;

        //targets player with lowest amount of cards in hand
        int counter = 100;
        int target = 0;
        for(int i = 0; i < computerState.getNumPlayers(); i++){
            if(i != this.playerNum){
                if(computerState.getPlayerHand(i).size() < counter){
                    target = i;
                }
            }
        }

        //play favor on target if you have it
        for(int x = 0; x < computerState.getCurrentPlayerHand().size(); x++) {
            if (computerState.getCurrentPlayerHand().get(x).getCardType() == 4) {
                int randomCardPos = (int)(Math.random()*computerState.getPlayerHand(target).size());
                PlayFavorCard favor = new PlayFavorCard(this, target, randomCardPos);
                this.game.sendAction(favor);
                return true;
            }
        }

        //play trade 2 if you have 2 of the same card
        if(checkForMatches(info, 2) != -1){
            int cardPos1 = -1;
            int cardPos2 = -1;
            for(int i = 0; i < computerState.getCurrentPlayerHand().size(); i++){
                if(computerState.getCurrentPlayerHand().get(i).getCardType() == checkForMatches(info,2)){
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

        //Trade 3
        if(checkForMatches(info, 3) != -1){
            int cardPos1 = -1;
            int cardPos2 = -1;
            int cardPos3 = -1;
            for(int i = 0; i < computerState.getCurrentPlayerHand().size(); i++){
                if(computerState.getCurrentPlayerHand().get(i).getCardType() == checkForMatches(info, 3)){
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
            return true;
        }

        //Trade 5
        if(Trade5(info) == true){
            return true;
        }


        return false;
    } //getCard


    //Checks if there are numOfMatches of the same card in player hand
    //returns -1 if there are less than numOfMatches of a card
    //returns the CardType of a card with the amount of matches
    public int checkForMatches(GameInfo info, int numOfMatches){
        EKGameState computerState = (EKGameState) info;
        for(int a = 1; a < 12; a++) {
            int counter = 0;
            for (int i = 0; i < computerState.getCurrentPlayerHand().size(); i++) {
                if(computerState.getCurrentPlayerHand().get(i).getCardType() == a){
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
    public boolean checkForDefuse(GameInfo info){
        EKGameState computerState = (EKGameState) info;
        for(int i = 0; i < computerState.getCurrentPlayerHand().size(); i++){
            if(computerState.getCurrentPlayerHand().get(i).getCardType() == 12){
                return true;
            }
        }
        return false;
    }//checkForDefuse

    public boolean nextCardEK(GameInfo info){
        EKGameState computerState = (EKGameState) info;
        //checks that STF Array isn't empty
        if(STFArray.size() == 0){
            return false;
        }
        else if(STFArray.size() != 0) {
            //Updating STFArray
            //If deck size changed since STF activated, update array accordingly
            if (computerState.getDeck().size() < this.STFDeckSize) {
                //if deck is smaller than STF deck size remove cards from
                // STFArray accordingly
                if (STFDeckSize - computerState.getDeck().size() >= 3) {
                    STFArray.clear();
                    return false;
                } else if (STFDeckSize - computerState.getDeck().size() < 3) {
                    if (STFDeckSize - computerState.getDeck().size() != 0) {
                        for (int i = 0; i < STFDeckSize - computerState.getDeck().size(); i++) {
                            if(STFArray.size() != 0) {
                                STFArray.remove(0);
                            }
                        }
                        this.STFDeckSize = computerState.getDeck().size();
                    }
                }
            }

            //checks to see if deck has been altered since see the future
            //was activated and if STF array still relevant
            for (int i = 0; i < STFArray.size(); i++) {
                if (STFArray.get(i) != computerState.getDeck().get(i).getCardType()) {
                    STFArray.clear();
                    return false;
                }
            }

            //if next card is EK return true
            if (this.STFArray.get(0) == 0) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    } //nextCardEK

    public boolean checkForPlayableCard(GameInfo info){
        EKGameState computerState = (EKGameState) info;

        //if you know next card is an Exploding Kitten
        if(nextCardEK(info) == true){
            for(int i = 0; i < computerState.getCurrentPlayerHand().size(); i++){
                //check for cards SKIP ATTACK SHUFFLE
                switch(computerState.getCurrentPlayerHand().get(i).getCardType()){
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
        else{
            for(int i = 0; i < computerState.getCurrentPlayerHand().size(); i++){
                //check for cards SKIP ATTACK STF
                switch(computerState.getCurrentPlayerHand().get(i).getCardType()){
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

    public boolean Trade5(GameInfo info){
        EKGameState computerState = (EKGameState) info;

        int cardPos1 = -1;
        int cardPos2 = -1;
        int cardPos3 = -1;
        int cardPos4 = -1;
        int cardPos5 = -1;
        int counter = 0;
        boolean found5Unique = false;

        ArrayList<Integer> trade5Array = new ArrayList<Integer>();
        //check to see if there are 5 unique cards in hand to trade
        if(computerState.getCurrentPlayerHand().size() < 5){
            return false;
        }
        for(int i = 0; i < computerState.getCurrentPlayerHand().size(); i++){
            boolean uniqueCard = true;

            for(int a = 0; a < trade5Array.size(); a++){
                if(computerState.getCurrentPlayerHand().get(i).getCardType() == trade5Array.get(a)){
                    uniqueCard = false;
                }
            }

            if(uniqueCard == true){
                trade5Array.add(computerState.getCurrentPlayerHand().get(i).getCardType());
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

        if(found5Unique == true){
            int discardPos = 0;
            if(nextCardEK(info)){
                for(int b = 0; b < computerState.getDiscardPile().size(); b++){
                    switch(computerState.getDiscardPile().get(b).getCardType()){
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
                return true;
            }

            if(checkForDefuse(info) == false){
                for(int c = 0; c < computerState.getDiscardPile().size(); c++){
                    if(computerState.getDiscardPile().get(c).getCardType() == 12){
                        Trade5Action trade = new Trade5Action(this, cardPos1,
                                cardPos2, cardPos3, cardPos4, cardPos5, c);
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
    }

}
