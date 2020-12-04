package edu.up.cs301.ExplodingKittens;

import org.junit.Test;

import static org.junit.Assert.*;

public class EKGameStateTest {

    @Test
    public void populateDeck() {
        EKGameState testState = new EKGameState(4);
        testState.getDeck().clear();
        assertEquals(0, testState.getDeck().size());
        testState.populateDeck();
        assertEquals(46, testState.getDeck().size());
    }

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
    }

    @Test
    public void populateDefuseExplode() {
        EKGameState testState = new EKGameState(4);
        testState.getDeck().clear();
        assertEquals(0, testState.getDeck().size());
        testState.populateDeck();
        assertEquals(46, testState.getDeck().size());
        testState.populateDefuseExplode();
        assertEquals(51, testState.getDeck().size());
    }

    @Test
    public void hasPlayerLost() {
        EKGameState state = new EKGameState(4);
        state.getPlayerHand(0).clear();
        state.getPlayerHand(0).add(new Card(0));
        assertEquals(state.hasPlayerLost(0), true);
        assertEquals(state.hasPlayerLost(1), false);
    }

    @Test
    public void getEKCount(){
        EKGameState state = new EKGameState(4);
        assertEquals(state.getEKCount(), 3);
    }


}