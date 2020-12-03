package edu.up.cs301.ExplodingKittens;

import org.junit.Test;

import static org.junit.Assert.*;

public class EK_LocalGameTest {

    @Test
    public void populateDeck() {
        EKLocalGame test = new EKLocalGame();
        assertNull(test.getCurrState().getDeck());
        test.populateDeck();
        assertNotNull(test.getCurrState().getDeck());
    }

    @Test
    public void populateDefuseExplode() {
        EKLocalGame test = new EKLocalGame();
        test.populateDeck();
        assertEquals(11, test.getCurrState().getDeck().get(test.getCurrState().getDeck().size()-1).getCardType());
        test.populateDefuseExplode();
        assertEquals(12, test.getCurrState().getDeck().get(test.getCurrState().getDeck().size()-1).getCardType());
    }

    @Test
    public void shuffle() {
        EKLocalGame test = new EKLocalGame();
        Player p1 = new Player(1, "Player 1");
        Player p2 = new Player(2, "Player 2");
        test.getCurrState().addPlayer(p1);
        test.getCurrState().addPlayer(p2);
        test.populateDeck();
        test.makeTestHand();
        assertTrue(test.Shuffle(p1));
        assertEquals(7, test.getCurrState().getDiscardPile().get(0).getCardType());
    }

    @Test
    public void skip() {
        EKLocalGame test = new EKLocalGame();
        Player p1 = new Player(1, "Player 1");
        Player p2 = new Player(2, "Player 2");
        test.getCurrState().addPlayer(p1);
        test.getCurrState().addPlayer(p2);
        test.populateDeck();
        test.makeTestHand();
        assertTrue(test.Skip(p1));
        assertEquals(9, test.getCurrState().getDiscardPile().get(0).getCardType());
    }

    @Test
    public void defuse() {
        EKLocalGame test = new EKLocalGame();
        Player p1 = new Player(1, "Player 1");
        Player p2 = new Player(2, "Player 2");
        test.getCurrState().addPlayer(p1);
        test.getCurrState().addPlayer(p2);
        test.populateDeck();
        test.makeTestHand();
        Card ex = new Card(0);
        test.getCurrState().getPlayers().get(0).getPlayerHand().add(ex);
        assertTrue(test.Defuse(p1));
        assertEquals(12, test.getCurrState().getDiscardPile().get(0).getCardType());
    }

    @Test
    public void drawCard() {
        EKLocalGame test = new EKLocalGame();
        Player p1 = new Player(1, "Player 1");
        Player p2 = new Player(2, "Player 2");
        test.getCurrState().addPlayer(p1);
        test.getCurrState().addPlayer(p2);
        test.populateDeck();
        assertNull(test.getCurrState().getPlayers().get(0).getPlayerHand());
        test.drawCard(p1);
        assertNotNull(test.getCurrState().getPlayers().get(0).getPlayerHand());
    }
}