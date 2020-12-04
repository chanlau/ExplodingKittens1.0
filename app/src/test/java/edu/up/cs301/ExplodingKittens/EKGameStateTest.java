package edu.up.cs301.ExplodingKittens;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *EKGameStateTest class
 * Contains unit tests for game state methods
 *
 * @author Samuel Warrick
 * @author Kaulu Ng
 * @author Chandler Lau
 * @version December 2020
 */
public class EKGameStateTest {

    /**
     * testing populate deck
     * deck should have 46 cards after populating
     */
    @Test
    public void populateDeck() {
        EKGameState testState = new EKGameState(4);
        testState.getDeck().clear();
        assertEquals(0, testState.getDeck().size());
        testState.populateDeck();
        assertEquals(46, testState.getDeck().size());
    }//populateDeck()

    /**
     * testing populate hands
     * Each hand should have 8 cards including just 1 defuse
     */
    @Test
    public void populateHands() {
        int defuseCounter = 0;
        EKGameState testState = new EKGameState(4);
        for(int i = 0; i < testState.getPlayerHand(0).size(); i++){
            if(testState.getCurrentPlayerHand().get(i).getCardType() == 12){
                defuseCounter = defuseCounter + 1;
            }
        }
        assertEquals(8, testState.getPlayerHand(0).size());
        assertEquals(1, defuseCounter);
    }//populateHands()

    /**
     * Testing populateDefuseExplode
     * populateDefuseExplode should add an additional 2 defuses and 3
     * exploding kittens to the already existing deck
     */
    @Test
    public void populateDefuseExplode() {
        EKGameState testState = new EKGameState(4);
        testState.getDeck().clear();
        assertEquals(0, testState.getDeck().size());
        testState.populateDeck();
        assertEquals(46, testState.getDeck().size());
        testState.populateDefuseExplode();
        assertEquals(51, testState.getDeck().size());
        int EKCount = 0;
        int defuseCount = 0;
        for(int i = 0; i < testState.getDeck().size(); i++){
            if(testState.getDeck().get(i).getCardType() == 0){
                EKCount++;
            }
            if(testState.getDeck().get(i).getCardType() == 12){
                defuseCount++;
            }
        }
        assertEquals(3, EKCount);
        assertEquals(2, defuseCount);
    }//populateDefuseExplode

    /**
     * testing if hasPlayerLost method works
     * Computer should check if the player with a single Exploding
     * Kitten in hand has lost and if player without EK in hand hasn't lost
     */
    @Test
    public void hasPlayerLost() {
        EKGameState state = new EKGameState(4);
        state.getPlayerHand(0).clear();
        state.getPlayerHand(0).add(new Card(0));
        assertEquals(state.hasPlayerLost(0), true);
        assertEquals(state.hasPlayerLost(1), false);
    }//hasPlayerLost()

    /**
     * tests if getEKCount works
     * the default gamestate should have 3 EK's in the deck
     */
    @Test
    public void getEKCount(){
        EKGameState state = new EKGameState(4);
        assertEquals(state.getEKCount(), 3);
    }//getEKCount()


}