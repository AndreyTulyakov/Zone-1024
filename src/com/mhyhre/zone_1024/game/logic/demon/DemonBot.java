package com.mhyhre.zone_1024.game.logic.demon;

import java.util.LinkedList;
import java.util.Random;

import android.util.Log;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.game.logic.Grid;
import com.mhyhre.zone_1024.game.logic.SimpleTile;
import com.mhyhre.zone_1024.utils.Direction;
import com.mhyhre.zone_1024.utils.Position;

public class DemonBot extends SimpleTile {

    private static final int HUNGER_DECREMENT = 1;

    private int hunger;
    private Grid grid;
    private Intention behaviorIntention;
    private Direction intentionDirection;
    private Random random;

    public DemonBot(int x, int y, int value, Grid grid) {
        super(x, y, value);
        this.grid = grid;
        behaviorIntention = Intention.NONE;
        intentionDirection = Direction.UP;
        random = new Random();
        onStep();
    }

    @Override
    public void setWasChanged(boolean wasChanged) {
        super.setWasChanged(wasChanged);
        if (wasChanged) {
            behaviorIntention = Intention.NONE;
        }
    }

    private void decrementHunger() {
        hunger -= HUNGER_DECREMENT;
        if (hunger < 0) {
            hunger = 0;
        }
    }

    public void onStep() {

        decrementHunger();

        // Если голодны - надо действовать.
        if(isHunger()) {
            
            // Если мы планировали кушать ячейку.
            if(behaviorIntention == Intention.EAT) {
                
                // Если можем - кушаем и засыпаем
                if(isCanEatIntention()) {
                    eatIntention();
                    behaviorIntention = Intention.NONE;
                    return;
                    
                // Если нет то ищем новую еду или двигаемся.
                } else {
                    thinkingAboutEatingSomething();
                    if(behaviorIntention == Intention.NONE) {
                        thinkingAboutMovingSomewhere();
                        return;
                    }
                }
            }
            
            // Если мы планировали двигаться.
            if(behaviorIntention == Intention.MOVE) {
                
                // Если можем - двигаемся и планируем еду или двигаться дальше
                if(isCanMoveIntention()) {
                    moveIntention();
                    thinkingAboutEatingSomething();
                    if(behaviorIntention == Intention.NONE) {
                        thinkingAboutMovingSomewhere();
                        return;
                    }
                    return;
                    
                // Если нет то ищем новую еду вокруг.
                } else {
                    thinkingAboutEatingSomething();
                    return;
                }
            }
            
            // Если мы только проголодались
            if(behaviorIntention == Intention.NONE) {
                
                // Ищем еду вокруг или двигаемся
                thinkingAboutEatingSomething();
                if(behaviorIntention == Intention.NONE) {
                    thinkingAboutMovingSomewhere();
                    return;
                }
            }
            
            
        }
    }

    
    private void moveIntention() {
        Position position = getPosition();
        Position targetPos = position.addVector(intentionDirection.getVector());
        grid.replaceTile(position, targetPos);
    }

    private void eatIntention() {
        
        Position position = getPosition();
        Position targetPos = position.addVector(intentionDirection.getVector());      
        SimpleTile targetTile = grid.getTile(targetPos);
        hunger += targetTile.getValue();      
        
        grid.replaceTile(position, targetPos);
        
        MainActivity.resources.playSound("DemonEat");
        setAfterMove(AfterMove.NONE);   
    }

    private boolean isCanEatIntention() {

        Position pos = getPosition();
        Position targetPos = pos.addVector(intentionDirection.getVector());

        if (grid.getSize().inRange(targetPos) == false) {
            return false;
        }

        SimpleTile targetTile = grid.getTile(targetPos);

        if (targetTile != null) {
            if (targetTile.getValue() > 0) {
                return true;
            }
        }
        return false;
    }
    
    
    private boolean isCanMoveIntention() {

        Position pos = getPosition();
        Position targetPos = pos.addVector(intentionDirection.getVector());

        if (grid.getSize().inRange(targetPos) == false) {
            return false;
        }

        SimpleTile targetTile = grid.getTile(targetPos);

        if (targetTile == null) {
            return true;
        }
        return false;
    }

    
    private void thinkingAboutEatingSomething() {
        
        Direction profitDirection = Direction.UP;
        int maximalCellIndex = 0;

        behaviorIntention = Intention.NONE;

        for (Direction direction : Direction.values()) {

            Position attackedPosition = getPosition().addVector(direction.getVector());

            // Если поблизости есть ячейки
            if (grid.getSize().inRange(attackedPosition.getX(), attackedPosition.getY())) {

                SimpleTile tile = grid.getTile(attackedPosition);
                if (tile != null) {
                    if (tile.getValue() > 0) {

                        if (tile.getValue() > maximalCellIndex) {
                            maximalCellIndex = tile.getValue();
                            profitDirection = direction;
                            behaviorIntention = Intention.EAT;
                        }
                    }
                }
            }
        }

        if (behaviorIntention != Intention.NONE) {
            intentionDirection = profitDirection;
            Log.i(MainActivity.DEBUG_ID, "Demon: thinking direction:" + intentionDirection.name());
        }
    }
    
    
    private void thinkingAboutMovingSomewhere() {
        
        behaviorIntention = Intention.NONE;
        
        LinkedList<Direction> avaibleDirections = new LinkedList<Direction>();
        
        for (Direction direction : Direction.values()) {

            Position attackedPosition = getPosition().addVector(direction.getVector());

            // Если поблизости есть свободные ячейки - добавим их в список
            if (grid.getSize().inRange(attackedPosition)) {

                
                SimpleTile tile = grid.getTile(attackedPosition);
                
                if (tile == null) {
                    avaibleDirections.add(direction);
                    behaviorIntention = Intention.MOVE;
                }
            }
        }

        // Если нашли что-то, то выбираем случайно направление из доступных.
        if (behaviorIntention != Intention.NONE) {    
            intentionDirection = avaibleDirections.get(random.nextInt(avaibleDirections.size()));
        }
    }
    

    public Position getPosition() {
        return new Position(targetX, targetY);
    }

    public int getHunger() {
        return hunger;
    }
    
    public boolean isHunger() {
        return hunger == 0;
    }

    public Intention getBehaviorIntention() {
        return behaviorIntention;
    }

    public Direction getIntentionDirection() {
        return intentionDirection;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public void setBehaviorIntention(Intention behaviorIntention) {
        this.behaviorIntention = behaviorIntention;
    }

    public void setIntentionDirection(Direction intentionDirection) {
        this.intentionDirection = intentionDirection;
    }

}
