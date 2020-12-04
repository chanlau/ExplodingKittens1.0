package edu.up.cs301.ExplodingKittens;

import org.junit.Test;

import edu.up.cs301.game.GameFramework.GamePlayer;

import static org.junit.Assert.*;

public class EK_LocalGameTest {

    /**
     * tests if the populate deck method is working properly by changing the
     * deck size and checking against the expected size
     */
    @Test
    public void populateDeck(){
        EKLocalGame testGame = new EKLocalGame(4);

        testGame.getCurrState().populateDeck();
        assertEquals(23,testGame.getCurrState().getDeck().size());
    }

    /**
     * tests if the populate defuse explode is working properly by
     */
    @Test
    public void populateDefuseExplode(){
        EKLocalGame testGame = new EKLocalGame(4);
        assertEquals(0, testGame.getCurrState().getDeck().size());

    }

    /**
     * tests if the hasPlayerLost function is working properly
     */
    @Test
    public void hasPlayerLost(){
        EKGameState state = new EKGameState(4);
        state.getPlayerHand(0).clear();
        state.getPlayerHand(0).add(new Card(0));
        assertEquals(state.hasPlayerLost(0), true);
        assertEquals(state.hasPlayerLost(1), false);
    }

    //this doesnt work
    @Test
    public void Skip(){
        EKLocalGame local = new EKLocalGame(4);
        EKHumanPlayer p1 = new EKHumanPlayer("p");
        EKComputerPlayer p2 = new EKComputerPlayer("p2");
        //local.setGamePlayers(2);
        local.getPlayers()[0] = p1;
        local.getPlayers()[1] = p2;
        EKGameState state = new EKGameState(local.getCurrState());
        state.getPlayerHand(0).add(new Card(9));
        local.Skip(p1);
        assertEquals(state.getWhoseTurn(), 1);
    }


    @Test
    public void shuffle() {
        EKLocalGame test = new EKLocalGame(2);
        EKGameState state = new EKGameState(test.getCurrState());
        EKComputerPlayer p1 = new EKComputerPlayer("Player 1");
        EKComputerPlayer p2 = new EKComputerPlayer("Player 2");
        test.setGamePlayers(2);
        String[] names = {"p1", "p2"};
        test.setPlayerNames(names);
        state.populateDeck();
        state.getPlayerHand(0).add(new Card(7));
        assertTrue(test.Shuffle(p1));
        assertEquals(7, state.getDiscardPile().get(0).getCardType());
    }

/*
    @Test
    public void skip() {
        EKLocalGame test = new EKLocalGame(4);
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
        EKLocalGame test = new EKLocalGame(4);
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
        EKLocalGame test = new EKLocalGame(4);
        Player p1 = new Player(1, "Player 1");
        Player p2 = new Player(2, "Player 2");
        test.getCurrState().addPlayer(p1);
        test.getCurrState().addPlayer(p2);
        test.populateDeck();
        assertNull(test.getCurrState().getPlayers().get(0).getPlayerHand());
        test.drawCard(p1);
        assertNotNull(test.getCurrState().getPlayers().get(0).getPlayerHand());
    }*/
}