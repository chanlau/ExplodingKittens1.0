package edu.up.cs301.ExplodingKittens;

import java.util.ArrayList;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.gameConfiguration.GameConfig;
import edu.up.cs301.game.GameFramework.gameConfiguration.GamePlayerType;

/**
 * Beta Release:
 * The game functions as intended with some limitations that are listed
 *
 * 1. The Nope Card can only be use to Nope a card that was played by the
 * previous player only and only for attack, skip, and nope cards played.
 * This is because all other cards actions besides skip, attack, and nope
 * happen immediately and having every card played then send a request to
 * each player asking if they would like to nope the card was too difficult
 * and would have slowed down the pace of play significantly.
 *
 * 2. The Favor card only takes a random card from a target play instead of
 * allowing that pplayer to choose the card. This was similar to the nope
 * card as it would be difficult to implement sending the action and waiting
 * then receiving an action (selecting a card) from the other play while.
 *
 * 3. Defuse cards are not "optional" to play. That is it will automatically
 * play the defuse card for you to defuse an exploding kitten.
 *
 * 4. After defusing an exploding kitten the player does not have the option
 * to return it to a specific location in the deck, it is randomly placed
 * there. In the normal game you would be able to place the card. Choosing
 * the location of the card would have required even more popups/button
 * presses and with so many actions by the human already it was decided that
 * it was an extra option that if we got to we would do but if not it would
 * not be a huge detriment to the game.
 *
 * Changes since Alpha release:
 * 1. The Nope card has been implemented as discussed above.
 *
 * 2. All bugs that were brought up in the github were fixed.
 *
 * 3. A "how to play" button was added describing how to play the game. It
 * can be exited out of by taping the screen anywhere.
 *
 * 4: The "play" button was removed and functionality was integrated with the
 * "enter" button. To play a card simply select the card and press "enter".
 *
 * 5. Generate GUI updates were added to make the game more appealing.
 *
 * 6. Computer actions and player actions for each round are now viewable in
 * the top left of the screen next to the "how to play" button.
 *
 * 7. Smart Computer player has been added to game
 *
 * 8. Possible to play with 2-4 players
 *
 * @author Samuel Warrick
 * @author Kaulu Ng
 * @author Chandler Lau
 * @version November 2020
 */
public class EK_MainActivity extends GameMainActivity {

    // the port number that this game will use when playing over the network
    private static final int PORT_NUMBER = 7513;

    @Override
    public GameConfig createDefaultConfig() {
        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        // a human player player type (player type 0)
        playerTypes.add(new GamePlayerType("Local Human Player") {
            public GamePlayer createPlayer(String name) {
                return new ExplodingKittensHumanPlayer(name);
            }});

        // a computer player type (player type 1)
        playerTypes.add(new GamePlayerType("Computer Player") {
            public GamePlayer createPlayer(String name) {
                return new ExplodingKittensComputerPlayer(name);
            }});

        // a computer player type (player type 2)
        playerTypes.add(new GamePlayerType("Smart Computer Player") {
            public GamePlayer createPlayer(String name) {
                return new ExplodingKittensSmartComputerPlayer(name);
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
        defaultConfig.addPlayer("Computer 1", 1); // player 2: a computer player
        defaultConfig.addPlayer("Computer 2", 1); // player 3: a computer
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

    //creates instance of EKLocalGame
    @Override
    public LocalGame createLocalGame() {
        //Creates the LocalGame depending on the number of players in the menu
        return new EK_LocalGame(this.getTablePlayersSize());
    }

}