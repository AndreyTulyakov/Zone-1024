package com.mhyhre.zone_1024.game.logic;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.util.Pair;

import com.mhyhre.zone_1024.utils.Direction;

public final class GridUtils {

    public static Pair<List<Integer>,List<Integer>> getTraversalList(Direction dir, int lenght) {

        List<Integer> xValues = new LinkedList<Integer>();
        List<Integer> yValues = new LinkedList<Integer>();

        for (int i = 0; i < lenght; i++) {
            xValues.add(i);
            yValues.add(i);
        }
        
        if(dir.getVectorX() > 0) {
            Collections.reverse(xValues);
        }
        if(dir.getVectorY() > 0) {
            Collections.reverse(yValues);
        }
        
        return new Pair<List<Integer>, List<Integer>>(xValues, yValues);
    }
}
