package com.mhyhre.zone_1024.game.logic;

/**
 * Simple, read-only tile 
 */
public class SimpleTile {
    
    public static final float ZOOM_BANG_FACTOR = 1.3f;
    
    protected int x;
    protected int y;
    protected int value;
    protected float zoom = 1;
    
    protected boolean wasChanged;


    public SimpleTile(int x, int y, int value) {
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    public void bang(){
        zoom = ZOOM_BANG_FACTOR;
    }
    
    public float getZoom() {
        return zoom;
    }
    
    protected void updateZoom() {
        if(zoom > 1.0f) {
            zoom -= 0.01f;
        } else {
            zoom = 1.0f;
        }
    }
    
    
    public boolean isWasChanged() {
        return wasChanged;
    }

    public void setWasChanged(boolean wasChanged) {
        this.wasChanged = wasChanged;
    }
    
    public void update() {
        updateZoom();
    }
}
