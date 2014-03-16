package com.mhyhre.zone_1024.utils;

import org.andengine.util.color.Color;

import android.util.SparseArray;

public class CellColors {
    
    private static CellColors instance;
    
    public static CellColors getInstance() {
        if(instance == null) {
            instance = new  CellColors();
        }
        return instance;
    }
    
    private SparseArray<Color> colorBase;

    private CellColors() {
        colorBase = new SparseArray<Color>();
        
        colorBase.put(0, new Color(0.8f, 0.753f, 0.702f));
        colorBase.put(2, new Color(0.933f, 0.894f, 0.855f));
        colorBase.put(4, new Color(0.929f, 0.878f, 0.784f));
        colorBase.put(8, new Color(0.949f, 0.694f, 0.475f));
        
    }
    
    public Color getColorByCellValue(int value) {
        
        Color result = colorBase.get(value);
        if(result == null) {
            return Color.BLACK;
        }
        return result;
    }
    
}
