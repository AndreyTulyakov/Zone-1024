package com.mhyhre.zone_1024.game.logic;

import com.mhyhre.zone_1024.utils.Position;
import com.mhyhre.zone_1024.utils.Size;

/**
 * Simple, read-only grid 
 */
public class SimpleGrid {

    protected Size size;
    protected Tile[][] tiles;

    public SimpleGrid(Size size) {
        this.size = size;
        tiles = new Tile[size.getWidth()][size.getHeight()];
    }
    
    public SimpleTile getTile(Position position) {
        if (inGridRange(position)) {
            return tiles[position.getX()][position.getY()];
        } else {
            return null;
        }
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
