package com.mhyhre.zone_1024.scenes.game.field;

import com.mhyhre.zone_1024.utils.Position;

public class Tile {

    public int x;
    public int y;
    public int value;
    public Tile mergedFrom;
    public Position previousPosition;
    
    public Tile(Position position,int value) {
        this.x = position.getX();
        this.x = position.getY();
        this.value = value;
    }
    
    public void savePosition() {
        previousPosition = new Position(x, y);
    }
    
    public void updatePosition(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }
}
