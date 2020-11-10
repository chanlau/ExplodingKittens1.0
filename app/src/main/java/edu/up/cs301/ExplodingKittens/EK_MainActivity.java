package edu.up.cs301.ExplodingKittens;


import java.util.ArrayList;

//import edu.up.cs301.counter.CounterComputerPlayer1;
//import edu.up.cs301.counter.CounterComputerPlayer2;
//import edu.up.cs301.counter.CounterHumanPlayer;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.GamePlayer;
import edu.up.cs301.game.GameFramework.LocalGame;
import edu.up.cs301.game.GameFramework.gameConfiguration.GameConfig;
import edu.up.cs301.game.GameFramework.gameConfiguration.GamePlayerType;

public class EK_MainActivity extends GameMainActivity {

    // the port number that this game will use when playing over the network
    private static final int PORT_NUMBER = 6969;

    @Override
    public GameConfig createDefaultConfig() {
        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        // a human player player type (player type 0)
        playerTypes.add(new GamePlayerType("Local Human Player") {
            public GamePlayer createPlayer(String name) {
                return new ExplodingKittensHumanPlayer(0, name);
            }});

        // a computer player type (player type 1)
        playerTypes.add(new GamePlayerType("Computer Player") {
            public GamePlayer createPlayer(String name) {
                return new ExplodingKittensComputerPlayer(1, name);
            }});

        // a computer player type (player type 2)
        playerTypes.add(new GamePlayerType("Computer Player (GUI)") {
            public GamePlayer createPlayer(String name) {
                return null; //new CounterComputerPlayer2(2, name);
            }});

        // Create a game configuration class for Counter:
        // - player types as given above
        // - from 1 to 2 players
        // - name of game is "Counter Game"
        // - port number as defined above
        GameConfig defaultConfig = new GameConfig(playerTypes, 1, 2, "Counter Game",
                PORT_NUMBER);

        // Add the default players to the configuration
        defaultConfig.addPlayer("Human", 0); // player 1: a human player
        defaultConfig.addPlayer("Computer", 1); // player 2: a computer player

        // Set the default remote-player setup:
        // - player name: "Remote Player"
        // - IP code: (empty string)
        // - default player type: human player
        defaultConfig.setRemoteData("Remote Player", "", 0);

        // return the configuration
        return defaultConfig;
    }

    @Override
    public LocalGame createLocalGame() {
        return new EK_LocalGame();
    }

/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //cardView = (SurfaceView) findViewById(R.id.surfaceview);
    }
*/

}