package edu.up.cs301.ExplodingKittens;

import edu.up.cs301.ExplodingKittens.EKActions.DrawCardAction;
import edu.up.cs301.ExplodingKittens.EKActions.PlayAttackCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayFavorCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayFutureCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlayShuffleCard;
import edu.up.cs301.ExplodingKittens.EKActions.PlaySkipCard;
import edu.up.cs301.ExplodingKittens.EKActions.Trade2Action;
import edu.up.cs301.game.GameFramework.GameComputerPlayer;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;

public class ExplodingKittensSmartComputerPlayer extends GameComputerPlayer {

    int probability;
    int random;

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public ExplodingKittensSmartComputerPlayer(String name) {
        super(name);
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
            random = (int) Math.random()*101;
            //TODO: create method to say how many players left in game
            probability =
                    (computerState.getDeck().size()/computerState.getEKCount())*100;

            if (random <= probability){
                //play a card: Skip, Attack, STF -> Shuffle
                //If none, try to get a card (Trade 2, 3, 5, or favor)
                // (Prioritize Defuse)
                //else draw a card
                int cardToPlay;
                if(computerState.getCurrentPlayerHand() != null){
                    for(int i = 0; i < computerState.getCurrentPlayerHand().size(); i++){
                        switch(computerState.getCurrentPlayerHand().get(i).getCardType()){
                            //attack card
                            case 6:
                                break;
                            //favor
                            case 8:
                                break;
                            //skip
                            case 9:
                                break;
                            //see the future
                            case 10:
                                break;
                        }
                    }
                }
            }
            else{
                //draw a card
                DrawCardAction draw = new DrawCardAction(this);
                this.game.sendAction(draw);
            }

        }

    }//receive info

    private void playCard(GameInfo info){

        if(!(info instanceof EKGameState)){ return;}
        EKGameState computerState = (EKGameState) info;

        if(computerState.getCurrentPlayerHand() != null){
            //Play the first functional card in your hand and send the appropriate action
            //It sets the position of that card to playThisCard
            int playThisCard = 0;
            for(int i = 0; i < computerState.getCurrentPlayerHand().size(); i++){
                if(computerState.getCurrentPlayerHand().get(i).getCardType() >= 6 &&
                        computerState.getCurrentPlayerHand().get(i).getCardType() != 12){
                    playThisCard = i;
                    break;
                }
            }
            //Check to see which card was selected by the card selection process
            switch (computerState.getCurrentPlayerHand().get(playThisCard).getCardType()){
                case 6:
                    PlayAttackCard attack = new PlayAttackCard(this);
                    this.game.sendAction(attack);
                    break;
                case 9:
                    PlaySkipCard skip = new PlaySkipCard(this);
                    this.game.sendAction(skip);
                    break;
                case 10:
                    PlayFutureCard future = new PlayFutureCard(this);
                    this.game.sendAction(future);
                    seeTheFuture(info);
                    break;
                default:
                    if(getACard(info)){
                        playCard(info);
                    }
                    else {
                        DrawCardAction draw = new DrawCardAction(this);
                        this.game.sendAction(draw);
                    }
            }
        }
    } //playCard()

    public void seeTheFuture(GameInfo info){
        if(!(info instanceof EKGameState)){ return;}
        EKGameState computerState = (EKGameState) info;

        //if next card is an EK attempt to play a card otherwise draw a card
        if(computerState.getDeck().get(0).getCardType() == 0){
            playCard(info);
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
                return true;
            }
        }

        //play trade 2 if you have 2 of the same card
        if(checkForTwo(info) != -1){
            int cardPos1 = -1;
            int cardPos2 = -1;
            for(int i = 0; i < computerState.getCurrentPlayerHand().size(); i++){
                if(computerState.getCurrentPlayerHand().get(i).getCardType() == checkForTwo(info)){
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
        } //trade 2 cards logic

        //Trade 3
        //Trade 5

        return false;
    } //getCard

    public int checkForTwo(GameInfo info){
        EKGameState computerState = (EKGameState) info;
        for(int a = 1; a < 12; a++) {
            int counter = 0;
            for (int i = 0; i < computerState.getCurrentPlayerHand().size(); i++) {
                if(computerState.getCurrentPlayerHand().get(i).getCardType() == a){
                    counter++;
                    if(counter >= 2){
                        return a;
                    }
                }
            }
        }
        return -1;
    }
}
