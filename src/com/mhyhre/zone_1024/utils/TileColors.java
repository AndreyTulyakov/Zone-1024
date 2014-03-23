package com.mhyhre.zone_1024.utils;

import org.andengine.util.color.Color;

import android.util.SparseArray;

public class TileColors {
    
    private static TileColors instance;
    
    public static TileColors getInstance() {
        if(instance == null) {
            instance = new  TileColors();
        }
        return instance;
    }
    
    private SparseArray<Color> colorBase;

    private TileColors() {
        colorBase = new SparseArray<Color>();
        
        colorBase.put(2, new Color(1.00f, 1.00f, 0.10f));
        colorBase.put(4, new Color(1.00f, 0.90f, 0.00f));
        colorBase.put(8, new Color(1.00f, 0.70f, 0.20f));
        
        colorBase.put(16, new Color(1.00f, 0.60f, 0.30f));
        colorBase.put(32, new Color(1.00f, 0.50f, 0.20f)); 
        colorBase.put(64, new Color(1.00f, 0.40f, 0.10f));
        
        colorBase.put(128, new Color(0.70f, 0.70f, 0.90f));
        colorBase.put(256, new Color(0.60f, 0.60f, 0.85f)); 
        colorBase.put(512, new Color(0.50f, 0.50f, 0.80f));
        
        colorBase.put(1024, new Color(0.20f, 1.00f, 0.20f));
        colorBase.put(2048, new Color(0.10f, 0.90f, 0.20f)); 
        colorBase.put(4096, new Color(0.00f, 0.80f, 0.00f)); 
    }
    
    public Color getColorByCellValue(int value) {
        
        Color result = colorBase.get(value);
        if(result == null) {
            return Color.RED;
        }
        return result;
    }
    
}
