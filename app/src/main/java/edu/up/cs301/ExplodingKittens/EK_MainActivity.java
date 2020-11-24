package edu.up.cs301.ExplodingKittens;

import java.util.ArrayList;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.gameConfiguration.GameConfig;
import edu.up.cs301.game.GameFramework.gameConfiguration.GamePlayerType;

/**
 *Main Activity of Exploding Kittens Game
 * Initializes Config screen and creates an Exploding Kittens Local Game
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
        GameConfig defaultConfig = new GameConfig(playerTypes, 1, 4,
                "Exploding Kittens" + " Game",
                PORT_NUMBER);

        // Add the default players to the configuration
        defaultConfig.addPlayer("Human", 0); // player 1: a human player
        defaultConfig.addPlayer("Computer 1", 2); // player 2: a computer player
        defaultConfig.addPlayer("Computer 2", 2); // player 3: a computer player
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
        return new EK_LocalGame();
    }

}