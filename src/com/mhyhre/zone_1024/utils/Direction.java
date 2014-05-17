/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.utils;

public enum Direction {
    
    UP(new Vector2D(0,1)),
    RIGHT(new Vector2D(1,0)),
    DOWN(new Vector2D(0,-1)),
    LEFT(new Vector2D(-1,0));
    
    
    private final Vector2D vector;
    
    private Direction(Vector2D vector) {
        this.vector = vector;
    }
    
    public Vector2D getVector() {
        return vector;
    }
    
    public Direction getOpposite() {
    	switch(this) {
		case DOWN:
			return UP;

		case LEFT:
			return RIGHT;

		case RIGHT:
			return LEFT;

		case UP:
			return DOWN;

		default:
			return null;
    	
    	}
    }
    
}
