package com.mhyhre.zone_1024.game.logic;

public interface GameControllable {

    public void restart();
    
    public boolean isWon();
    public boolean isOver();

    public SimpleGrid getGrid();
}
