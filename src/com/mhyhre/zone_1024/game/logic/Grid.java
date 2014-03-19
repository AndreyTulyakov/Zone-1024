package com.mhyhre.zone_1024.game.logic;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.mhyhre.zone_1024.utils.Position;
import com.mhyhre.zone_1024.utils.Size;

public class Grid extends SimpleGrid {

    private Random random;
    private boolean locked;
    
    public Grid(Size size) {
        super(size);
        random = new Random();
    }
    
    public Position randomAvaibleCell() {
        List<Position> freeCells = availableCells();
        if (freeCells.size() > 0) {
            return freeCells.get(random.nextInt(freeCells.size())).clone();
        }
        return null;
    }

    public List<Position> availableCells() {
        LinkedList<Position> freePositions = new LinkedList<Position>();
        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                if (tiles[x][y] == null) {
                    freePositions.add(new Position(x, y));
                }
            }
        }
        return freePositions;
    }

    public boolean cellsAvailable() {
        return availableCells().size() > 0;
    }


    public void prepareTiles() {
        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                Tile tile = tiles[x][y];
                if (tile != null) {
             
                }
            }
        }
    }

    private void insertTile(int x, int y, Tile tile) {
        if(inGridRange(x, y)) {
            tiles[x][y] = tile;
        }
    }

    private void removeTile(int x, int y, Tile tile) {
        if(inGridRange(x, y)) {
            tiles[x][y] = null;
        }
    }

    
    // Adds a tile in a random free position
    public void addRandomTile() {
        if (cellsAvailable()) {
            int value = Math.random() < 0.9 ? 2 : 4;
            Position position = randomAvaibleCell();
            Tile tile = new Tile(position.getX(),position.getY(), value);
            insertTile(position.getX(), position.getY(), tile);
        }
    }
    
    public void lock() {
        locked = true;
    }
    
    public void unlock() {
        locked = false;
    }
    
    public boolean isLocked() {
        return locked;
    }

    public void update() {
        
        if(allTilesIsReady()) {
            unlock();
            return;
        } else {
            for (int x = 0; x < size.getWidth(); x++) {
                for (int y = 0; y < size.getHeight(); y++) {
                    Tile tile = tiles[x][y];
                    if (tile != null) {
                        tile.updatePosition();
                    }
                }
            }
        }
    }
    
    public boolean allTilesIsReady() {
        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                Tile tile = tiles[x][y];
                if (tile != null) {
                    if(tile.isDestinationReached() == false) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


}
