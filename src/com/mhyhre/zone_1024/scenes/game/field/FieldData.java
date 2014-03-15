/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.scenes.game.field;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.util.Log;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.utils.Directions;
import com.mhyhre.zone_1024.utils.Position;
import com.mhyhre.zone_1024.utils.Size;


public class FieldData {
    
    Size size;
    private int[][] data;
    Random random;
    
    public FieldData(Size size) {
        random = new Random();
        this.size = size;
        data = new int[size.getWidth()][size.getHeight()];
    }
    
    public int get(int x, int y) {
        return data[x][y];
    }
    
    public void set(int x, int y, int value) {
        data[x][y] = value;
    }
    
    public int getWidth() {
        return size.getWidth();
    }
    
    public int getHeight() {
        return size.getHeight();
    }
    
    public void putNewElement() {
        if(hasFreeCell()) {
            
            List<Position> freeCells = getFreeCellsList();
            Position targetCellPosition = freeCells.get(random.nextInt(freeCells.size()));
            set(targetCellPosition.getX(), targetCellPosition.getY(), random.nextBoolean() ? 2 : 4);
            Log.i(MainActivity.DEBUG_ID, "Put new Element:" + targetCellPosition.getX() + "," + targetCellPosition.getY());
        }
    }
    
    public List<Position> getFreeCellsList() {
        List<Position> result = new LinkedList<Position>();

        for(int x = 0; x < getWidth(); x++) {
            for(int y = 0; y < getHeight(); y++) {
                if(data[x][y] == 0) {
                    result.add(new Position(x,y));
                }
            } 
        }
        return result;
    }
    
    public boolean hasFreeCell() {
        for(int x = 0; x < getWidth(); x++) {
            for(int y = 0; y < getHeight(); y++) {
                if(data[x][y] == 0) {
                    return true;
                }
            } 
        }
        return false;
    }
    
    public void slideTo(Directions direct) {
        
        switch(direct) {
        
        case DOWN:
            
            
            break;
        case LEFT:
            break;
        case RIGHT:
            break;
        case UP:
            break;
        default:
            break;
        
        }
        
        
    }
}
