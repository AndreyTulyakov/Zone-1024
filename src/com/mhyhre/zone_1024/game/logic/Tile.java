package com.mhyhre.zone_1024.game.logic;


public class Tile extends SimpleTile {
    
    private static final int MINIMAL_DESTINATION_DISTANCE = 8;
    private static final int TILE_SPEED = 10;
    private static final int TILE_POSITION_FACTOR = 100;
    
    private SimpleTile targetTile;

    protected Tile(int x, int y, int value) {
        super(x, y, value);
        convertPosition();
    }
    
    private void convertPosition() {
        x *= TILE_POSITION_FACTOR;
        y *= TILE_POSITION_FACTOR;       
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        convertPosition();
    }

    
    public void setValue(int value) {
        this.value = value;
    }
    
    public SimpleTile getTargetTile() {
        return targetTile;
    }
    
    public void setTargetTile(SimpleTile targetTile) {
        this.targetTile = targetTile;
    }
    
    public boolean isDestinationReached() {
        if(targetTile == null) {
            return true;
        }
        boolean reachedDistanceX = Math.abs(targetTile.getX()-x) < MINIMAL_DESTINATION_DISTANCE;
        boolean reachedDistanceY = Math.abs(targetTile.getY()-y) < MINIMAL_DESTINATION_DISTANCE;
        
        if(reachedDistanceX) {
            x = targetTile.getX();
        }
        if(reachedDistanceY) {
            y = targetTile.getY();
        }
        
        return reachedDistanceX && reachedDistanceY;
    }
    
    public void updatePosition() {
        if(isDestinationReached()) {
            return;
        }
        
        if(targetTile.getX() > getX()) {
            x += TILE_SPEED;
        }
        
        if(targetTile.getX() < getX()) {
            x -= TILE_SPEED;
        }
        
        if(targetTile.getY() > getY()) {
            y += TILE_SPEED;
        }
        
        if(targetTile.getY() < getY()) {
            y -= TILE_SPEED;
        }
    }
}
