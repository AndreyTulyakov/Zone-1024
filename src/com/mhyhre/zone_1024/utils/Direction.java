/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.utils;

public enum Direction {
    
    NONE(0,0),
    UP(0,1),
    DOWN(0,-1),
    LEFT(-1,0),
    RIGHT(1,0);
    
    private final int vectorX;
    private final int vectorY;
    
    private Direction(int xDirection, int yDirection) {
        this.vectorX = xDirection;
        this.vectorY = yDirection;
    }
    
    public int getVectorX() {
        return vectorX;
    }

    public int getVectorY() {
        return vectorY;
    }
}
