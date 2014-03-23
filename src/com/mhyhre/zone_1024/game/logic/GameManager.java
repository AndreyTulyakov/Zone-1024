package com.mhyhre.zone_1024.game.logic;

import java.util.Timer;
import java.util.TimerTask;

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
    private static boolean over;
    private boolean won;
    private final int winNumber = 1024;

    private final Size size;
    private Grid grid;
    
    public static GameManager getInstance() {
        if(instance == null) {
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
        this.won = false;
        this.score = 0;
        
        over = false;
        
        grid.testInit();

        
        this.addStartTiles();
    }
    
    public void update() {

        grid.update();
        if(grid.isLocked() == false) {
        
            if(grid.hasNumber(winNumber)){
                won = true;
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
        
        if(grid.isLocked() == false) {
            grid.lock();
            score += grid.move(direction);
            

            if(grid.canMove() == false) {
                grid.lock();
                over = true;
                won = false;
     
                RootScene.Me.setState(GameStates.GAME_OVER_SCENE);
            }

        }
    }


    @Override
    public boolean isWon() {
        return won;
    }

    @Override
    public boolean isOver() {
        return over;
    }
    
    public int getScore() {
        return score;
    }
}
