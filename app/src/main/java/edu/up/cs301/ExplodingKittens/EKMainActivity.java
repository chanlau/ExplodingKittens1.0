package edu.up.cs301.ExplodingKittens;

import java.util.ArrayList;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.gameConfiguration.GameConfig;
import edu.up.cs301.game.GameFramework.gameConfiguration.GamePlayerType;

/**
 * Final Release:
 * The game functions as intended with some limitations that are listed:
 *
 * 1. The Nope Card can only be use to Nope a card that was played by the
 * previous player only and only for attack, skip, and nope cards played.
 * This is because all other cards actions besides skip, attack, and nope
 * happen immediately and having every card played then send a request to
 * each player asking if they would like to nope the card was too difficult
 * and would have slowed down the pace of play significantly.
 *
 * 2. The Favor card only takes a random card from a target play instead of
 * allowing that player to choose the card. This was similar to the nope
 * card as it would be difficult to implement sending the action and waiting
 * then receiving an action (selecting a card) from the other play while.
 *
 * 3. Defuse cards are not "optional" to play. That is it will automatically
 * play the defuse card for you to defuse an exploding kitten.
 *
 *
 * Changes since Beta release:
 * 1. When a player defuses an Exploding Kitten card, they now have the
 * option to place the card back in a specific spot in the deck, or place it
 * randomly in the deck.
 *
 * 2. The left and right scroll buttons disappear if there are no more cards
 * to scroll through in that direction. This signals the edge of the player's
 * hand in that direction.
 *
 * 3. The deck image button can now be used to draw a card and end the
 * player's turn. The "end turn" button is still functional and present.
 *
 * 4. More unit tests have been created in EK_LocalGameTest.
 *
 * Known Bugs:
 * There are no known bugs.
 *
 * @author Samuel Warrick
 * @author Kaulu Ng
 * @author Chandler Lau
 * @version December 2020
 */
public class EKMainActivity extends GameMainActivity {

    // the port number that this game will use when playing over the network
    private static final int PORT_NUMBER = 7513;

    @Override
    public GameConfig createDefaultConfig() {
        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        // a human player player type (player type 0)
        playerTypes.add(new GamePlayerType("Local Human Player") {
            public GamePlayer createPlayer(String name) {
                return new EKHumanPlayer(name);
            }});

        // a computer player type (player type 1)
        playerTypes.add(new GamePlayerType("Computer Player") {
            public GamePlayer createPlayer(String name) {
                return new EKComputerPlayer(name);
            }});

        // a computer player type (player type 2)
        playerTypes.add(new GamePlayerType("Smart Computer Player") {
            public GamePlayer createPlayer(String name) {
                return new EKSmartComputerPlayer(name);
            }});

        // Create a game configuration class for Counter:
        // - player types as given above
        // - from 1 to 2 players
        // - name of game is "Counter Game"
        // - port number as defined above
        GameConfig defaultConfig = new GameConfig(playerTypes, 2, 4,
                "Exploding Kittens" + " Game",
                PORT_NUMBER);

        // Add the default players to the configuration
        defaultConfig.addPlayer("Human", 0); // player 1: a human player
        defaultConfig.addPlayer("Computer 1", 2); // player 2: a computer player
        defaultConfig.addPlayer("Computer 2", 2); // player 3: a computer
        // player
        defaultConfig.addPlayer("Computer 3", 2); // player 4: a computer
        // player


        // Set the default remote-player setup:
        // - player name: "Remote Player"
        // - IP code: (empty string)
        // - default player type: human player
        defaultConfig.setRemoteData("Remote Player", "", 0);

        // return the configuration
        return defaultConfig;
    }

    /**
     * creates instance of local game
     * @return
     *      returns a LocalGame object
     */
    @Override
    public LocalGame createLocalGame() {
        //Creates the LocalGame depending on the number of players in the menu
        return new EKLocalGame(this.getTablePlayersSize());
    }

}