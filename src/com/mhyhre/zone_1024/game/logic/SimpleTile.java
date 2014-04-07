package com.mhyhre.zone_1024.game.logic;

/**
 * Simple, read-only tile 
 */
public class SimpleTile {
    
    public enum AfterMove {
        MUST_BE_DELETED,
        NONE,
        MUST_SUMMED;
    }
    
    public static final float ZOOM_BANG_FACTOR = 1.3f;
    public static final float MOVING_COMPLETE_DISTANCE = 0.18f;
    public static final float TILE_MOVING_SPEED = 0.33f;
    
    protected float x;
    protected float y;
    protected int targetX;
    protected int targetY;
    protected int value;
    protected float zoom = 1;
    protected boolean wasChanged;
    protected AfterMove afterMove;


    public SimpleTile(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        this.value = value;
        this.afterMove = AfterMove.NONE;
    }
    
    public boolean isMoveComplete() {
        if(Math.abs(x-targetX) <= MOVING_COMPLETE_DISTANCE) {
            if(Math.abs(y-targetY) <= MOVING_COMPLETE_DISTANCE) {
                return true;
            }
        }
        return false;
    }
    
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
    
    public void setTargetPosition(int x, int y) {
        this.x = targetX;
        this.y = targetY;
        targetX = x;
        targetY = y;
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
            zoom -= 0.025f;
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
        
        if(isMoveComplete() == false) {
            moveToTarget();
        } else {
            x = targetX;
            y = targetY;
        }
        updateZoom();
    }

    private void moveToTarget() {
        if(x < targetX) {
            x += TILE_MOVING_SPEED;
        }
        
        if(x > targetX) {
            x -= TILE_MOVING_SPEED;
        }
        
        if(y < targetY) {
            y += TILE_MOVING_SPEED;
        }
        
        if(y > targetY) {
            y -= TILE_MOVING_SPEED;
        }
    }

    public AfterMove getAfterMove() {
        return afterMove;
    }

    public void setAfterMove(AfterMove afterMove) {
        this.afterMove = afterMove;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }
}
