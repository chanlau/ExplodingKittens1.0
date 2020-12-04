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
    }
}