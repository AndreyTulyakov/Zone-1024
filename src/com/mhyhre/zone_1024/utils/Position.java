/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.utils;

public final class Position implements Cloneable {
    
    private int x;
    private int y;
    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Position clone() {
        return new Position(x, y);
    }
    
    @Override 
    public boolean equals(Object obj) {
        
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof Position))
            return false;

        Position position = (Position) obj;
        return (x == position.x) && (y == position.y);
    }
    
    @Override
    public String toString() {
        return "[x=" + x + ", y=" + y + "]";
    }
}
