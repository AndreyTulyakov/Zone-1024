package com.mhyhre.zone_1024.game.logic;

import android.util.Log;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.utils.Direction;
import com.mhyhre.zone_1024.utils.MoveEventListener;
import com.mhyhre.zone_1024.utils.Size;

public final class GameManager implements MoveEventListener, GameControllable {

    private static GameManager instance;
    private static final int START_TILES = 2;

    private int score;
    private boolean pause;
    private boolean over;
    private boolean won;
    private boolean keepPlaying;
    private final int winNumber = 32;//1024;


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
        this.over = false;
        this.won = false;
        this.keepPlaying = false;

        this.score = 0;
        
        this.addStartTiles();
    }
    
    public void update() {
        if(pause){
            return;
        }
        
        grid.update();
        
        if(grid.hasNumber(winNumber)){
            won = true;
        }
        
        if(grid.hasFreeCell() == false) {
            // if grid can't move anything
            if(true){
                    over = true;
            }
        }
    }

    public void restart() {
        Log.i(MainActivity.DEBUG_ID, "GameManager: Restart!");
        this.setup();
    }

    public void keepPlaying() {
        this.keepPlaying = true;
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
                over = true;
                won = false;
            }
        }
    }

    @Override
    public void pause() {
        pause = true;
    }

    @Override
    public void resume() {
        pause = false;
    }

    @Override
    public boolean isWon() {
        return won;
    }

    @Override
    public boolean isOver() {
        return over;
    }

    @Override
    public boolean isKeepPlaying() {
        return keepPlaying;
    }
    
    public int getScore() {
        return score;
    }
}
