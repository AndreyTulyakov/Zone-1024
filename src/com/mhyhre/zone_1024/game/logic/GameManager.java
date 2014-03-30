package com.mhyhre.zone_1024.game.logic;

import android.util.Log;
import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.scenes.RootScene;
import com.mhyhre.zone_1024.scenes.RootScene.GameStates;
import com.mhyhre.zone_1024.utils.Direction;
import com.mhyhre.zone_1024.utils.MoveEventListener;
import com.mhyhre.zone_1024.utils.Size;

public final class GameManager implements MoveEventListener, GameControllable {

    private static GameManager instance;
    private static final int START_TILES = 2;

    private int score;
    private final int winNumber = 1024;
    private boolean keepPlaying = false;
    private final Size size;
    private Grid grid;

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    private GameManager() {
        this.size = new Size(4, 4);
        setup();

    }

    private void setup() {
        Log.i(MainActivity.DEBUG_ID, "GameManager: Setup");
        this.grid = new Grid(size);
        this.score = 0;
        this.keepPlaying = false;

        grid.testInit();
        this.addStartTiles();
    }

    public void update() {

        grid.update();
        if (grid.isLocked() == false) {

            if (grid.hasNumberOrMore(winNumber)) {
                if (keepPlaying == false) {
                    grid.lock();
                    RootScene.Me.setState(GameStates.GAME_WIN_SCENE);
                }

            }
        } else {
            if (keepPlaying == true) {
                grid.unlock();
            }
        }
    }

  

    public void restart() {
        Log.i(MainActivity.DEBUG_ID, "GameManager: Restart!");
        this.setup();
    }

    // Set up the initial tiles to start the game with
    private void addStartTiles() {
        for (int i = 0; i < START_TILES; i++) {
            grid.addRandomTile();
        }
    }

    public SimpleGrid getGrid() {
        return grid;
    }

    @Override
    public void onMoveEvent(Direction direction) {

        if (grid.isLocked() == false) {
            grid.lock();
            grid.move(direction);
            score = grid.calculateTotalValues();

            if (grid.canMove() == false) {
                grid.lock();
                keepPlaying = false;
                RootScene.Me.setState(GameStates.GAME_OVER_SCENE);
            }

        }
    }

    public int getScore() {
        return score;
    }

    public void setKeepPlaying() {
        keepPlaying = true;
    }


}
