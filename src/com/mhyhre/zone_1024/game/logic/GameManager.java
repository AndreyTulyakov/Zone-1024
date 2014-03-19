package com.mhyhre.zone_1024.game.logic;

import com.mhyhre.zone_1024.utils.Direction;
import com.mhyhre.zone_1024.utils.MoveEventListener;
import com.mhyhre.zone_1024.utils.Size;

public final class GameManager implements MoveEventListener {

    private static final int START_TILES = 2;

    private int score;
    private boolean over;
    private boolean won;
    private boolean keepPlaying;

    private final Size size;
    private Grid grid;
    
    

    public GameManager(Size size) {
        this.size = size;
        setup();
    }

    private void setup() {
        this.grid = new Grid(size);
        this.score = 0;
        this.over = false;
        this.won = false;
        this.keepPlaying = false;

        this.addStartTiles();
    }
    
    public void update() {
        grid.update();
    }

    public void restart() {
        this.setup();
    }

    public void keepPlaying() {
        this.keepPlaying = true;
    }

    public boolean isGameTerminated() {
        if (over || (won && !keepPlaying)) {
            return true;
        } else {
            return false;
        }
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
            
            // TODO: Grid moving
        }
    }
}
