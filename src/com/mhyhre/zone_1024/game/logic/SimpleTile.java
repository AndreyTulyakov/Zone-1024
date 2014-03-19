package com.mhyhre.zone_1024.game.logic;

/**
 * Simple, read-only tile 
 */
public class SimpleTile {
    
    protected int x;
    protected int y;
    protected int value;
    
    protected SimpleTile(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
    }
}
