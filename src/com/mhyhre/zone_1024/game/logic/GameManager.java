package com.mhyhre.zone_1024.game.logic;

import android.util.Log;
import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.PreferenceManager;
import com.mhyhre.zone_1024.scenes.RootScene;
import com.mhyhre.zone_1024.scenes.RootScene.GameStates;
import com.mhyhre.zone_1024.utils.Direction;
import com.mhyhre.zone_1024.utils.MoveEventListener;
import com.mhyhre.zone_1024.utils.Size;

public final class GameManager implements MoveEventListener, GameControllable {

    private static GameManager instance;
    private static final int START_TILES = 2;
    private static final int WIN_NUMBER = 1024;

    private int score;
    private boolean gameFinished = false;
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
        this.grid = new Grid(size, PreferenceManager.isMonsterEnabled());
        this.score = 0;
        this.gameFinished = false;
        
        //grid.testInit();
        this.addStartTiles();
    }

    public void loadGame() {
        if (MainActivity.getPreferenceManager().isGameWasNotEnded()) {
            
            grid = GridUtils.loadFromFile();
            if(grid != null) {        
                score = grid.calculateTotalValues();
                gameFinished = false;
            } else {
                setup();
            }
        } else {
            setup();
        }
    }

    public void saveGame() {

        PreferenceManager.setGameWasNotEnded(!isGameFinished());
        if (isGameFinished() == false) {
            GridUtils.saveToFile(grid);
        }
    }

    public void update() {
        
        grid.update();
        
        if (gameFinished) {
            return;
        }

        if (grid.isLocked() == false) {

            score = grid.calculateTotalValues();

            if (grid.isCanMoving() == false) {
            	
            	score = grid.calculateTotalValues();
                PreferenceManager.setGameWasNotEnded(false);
                gameFinished = true;
                
                if (grid.hasNumberOrMore(WIN_NUMBER)) {
                    RootScene.Me.setState(GameStates.GAME_WIN_SCENE);
                } else {
                    RootScene.Me.setState(GameStates.GAME_OVER_SCENE);
                }

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

    public Grid getGrid() {
        return grid;
    }

    @Override
    public void onMoveEvent(Direction direction) {
        if (grid.isLocked() == false) {
            grid.move(direction);
        }
    }

    public int getScore() {
        return score;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

}
