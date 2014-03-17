/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.utils;

public class Size {
    
    private final int width;
    private final int height;
    
    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public boolean inRange(int x, int y) {
        if(x >= 0 && y >=0) {
            if( x < width && y < height) {
                return true;
            }
        }
        return false;
    }
}
