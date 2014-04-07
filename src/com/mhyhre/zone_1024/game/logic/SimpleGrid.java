package com.mhyhre.zone_1024.game.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.mhyhre.zone_1024.utils.Position;
import com.mhyhre.zone_1024.utils.Size;

/**
 * Simple, read-only grid 
 */
public class SimpleGrid {

    protected Size size;
    protected SimpleTile[][] tiles;
    protected List<SimpleTile> movingTiles;


    public SimpleGrid(Size size) {
        this.size = size;
        tiles = new SimpleTile[size.getWidth()][size.getHeight()];
        movingTiles = new LinkedList<SimpleTile>();
    }
    
    public SimpleTile getTile(Position position) {
        return getTile(position.getX(), position.getY());
    }
    
    protected SimpleTile getTile(int x, int y) {
        if (inGridRange(x, y)) {
            return tiles[x][y];
        } else {
            return null;
        }
    }
    
    public ArrayList<SimpleTile> getAllTiles() {
        
        ArrayList<SimpleTile> allTiles = new ArrayList<SimpleTile>();
        for(int x = 0; x < size.getWidth(); x++) {
            for(int y = 0; y < size.getHeight(); y++) {
                if(tiles[x][y] != null) {
                    allTiles.add(tiles[x][y]);
                }
            }
        }
        for(SimpleTile tile: movingTiles) {
            allTiles.add(tile);
        }
        return allTiles;
    }
    
    public boolean hasNumberOrMore(int number) {
        for(int x = 0; x < size.getWidth(); x++) {
            for(int y = 0; y < size.getHeight(); y++) {
                SimpleTile tile = tiles[x][y];
                if(tile != null) {
                    if(tile.getValue() >= number) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean hasFreeCell() {
        for(int x = 0; x < size.getWidth(); x++) {
            for(int y = 0; y < size.getHeight(); y++) {
                SimpleTile tile = tiles[x][y];
                if(tile == null) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public int calculateTotalValues() {
        int scores = 0;
        for(int x = 0; x < size.getWidth(); x++) {
            for(int y = 0; y < size.getHeight(); y++) {
                SimpleTile tile = tiles[x][y];
                if(tile != null) {
                    scores += tile.value;
                }
            }
        }
        return scores;
    }
    
    public boolean isBusyCell(Position cell) {
        SimpleTile tile = tiles[cell.getX()][cell.getY()];
        if(tile != null) {
            if(tile.getTargetX() == cell.getX() && tile.getTargetY() == cell.getY()) {
                return true;
            }
        }
        return false;
    }
    public Size getSize() {
        return size;
    }
    
    protected boolean inGridRange(Position position) {
        return size.inRange(position.getX(), position.getY());
    }
    
    protected boolean inGridRange(int x, int y) {
        return size.inRange(x, y);
    }
}
