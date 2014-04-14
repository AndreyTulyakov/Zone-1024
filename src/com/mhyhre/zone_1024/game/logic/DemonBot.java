package com.mhyhre.zone_1024.game.logic;

import android.util.Log;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.utils.Direction;
import com.mhyhre.zone_1024.utils.Position;

public class DemonBot extends SimpleTile {
    
    public enum Intention {
        NONE, EAT_BY_DIRECTION;
    }

    private static final int HUNGER_DECREMENT = 1;

    private int hunger;
    private Grid grid;
    private Intention behaviorIntention;
    private Direction intentionDirection;

    public DemonBot(int x, int y, int value, Grid grid) {
        super(x, y, value);
        this.grid = grid;
        behaviorIntention = Intention.NONE;
        intentionDirection = Direction.UP;
    }
    
    @Override
    public void setWasChanged(boolean wasChanged) {
        super.setWasChanged(wasChanged);
        if(wasChanged) {
            behaviorIntention = Intention.NONE;
        }
    }

    public void onStep() {


        hunger -= HUNGER_DECREMENT;
        if(hunger < 0) {
            hunger = 0;
        }

        SimpleTile demonTile = null;
        Position position = getPosition();

        // ���� ���-�� ��������� - ��������� ��� �����
        if (behaviorIntention != Intention.NONE) {
            
            // ���� ����� - ���������
            if (isCanEatIntention()) {
                
                Position targetPos = position.addVector(intentionDirection.getVector());

                SimpleTile targetTile = grid.getTile(targetPos);
                hunger += targetTile.getValue();
                grid.removeTile(targetPos.getX(), targetPos.getY());
                
                
                demonTile = grid.getTile(position);
                
                demonTile.setTargetPosition(targetPos.getX(), targetPos.getY());
                grid.removeTile(position.getX(), position.getY());
                grid.insertTile(targetPos.getX(), targetPos.getY(), demonTile);
                
                MainActivity.resources.playSound("DemonEat");
                
                setAfterMove(AfterMove.NONE);
            }

            // �������� ������� � ����� ������
            behaviorIntention = Intention.NONE;
        }

        // ���� ������� � ������ �� ���������
        if (hunger <= 0 && behaviorIntention == Intention.NONE) {

            // ��������� ������?
            thinkingAboutEatingSomething();
        }
    }

    private boolean isCanEatIntention() {

        Position pos = getPosition();
        Position targetPos = pos.addVector(intentionDirection.getVector());

        SimpleTile targetTile = grid.getTile(targetPos);
        if (targetTile != null && targetTile.getValue() > 0 && grid.getSize().inRange(targetPos.getX(), targetPos.getY())) {
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

            // ���� ���������� ���� ������
            if (grid.getSize().inRange(attackedPosition.getX(), attackedPosition.getY())) {

                SimpleTile tile = grid.getTile(attackedPosition);
                if (tile != null) {
                    if (tile.getValue() > 0) {

                        if (tile.getValue() > maximalCellIndex) {
                            maximalCellIndex = tile.getValue();
                            profitDirection = direction;
                            behaviorIntention = Intention.EAT_BY_DIRECTION;
                        }
                    }
                }
            }
        }

        if (behaviorIntention != Intention.NONE) {
            intentionDirection = profitDirection;
        }
    }

    public Position getPosition() {
        return new Position(targetX, targetY);
    }

    public int getHunger() {
        return hunger;
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
