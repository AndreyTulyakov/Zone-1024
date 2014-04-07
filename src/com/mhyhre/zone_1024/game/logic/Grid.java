package com.mhyhre.zone_1024.game.logic;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import android.util.Log;
import android.util.Pair;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.game.logic.SimpleTile.AfterMove;
import com.mhyhre.zone_1024.utils.Direction;
import com.mhyhre.zone_1024.utils.Position;
import com.mhyhre.zone_1024.utils.Size;

public class Grid extends SimpleGrid {

    private static final int JOKER_MINIMAL_EXIST_STEP = 40;
    private static final int DEMON_MINIMAL_EXIST_STEP = 20;

    public static final int JOKER_VALUE = -1;
    public static final int DEMON_VALUE = -2;

    private Random random;
    private boolean canMoving;
    private boolean lastMovingSuccess;

    private int step = 0;

    private int jokerBeenStep;
    private int demonBeenStep;

    public Grid(Size size) {
        super(size);
        random = new Random();
        canMoving = true;

        jokerBeenStep = JOKER_MINIMAL_EXIST_STEP + random.nextInt(10);
        demonBeenStep = DEMON_MINIMAL_EXIST_STEP + random.nextInt(10);
    }

    public Position randomAvaibleCell() {
        List<Position> freeCells = availableCells();
        if (freeCells.size() > 0) {
            return freeCells.get(random.nextInt(freeCells.size())).clone();
        }
        return null;
    }





    // Adds a tile in a random free position
    public SimpleTile addRandomTile() {
        if (cellsAvailable()) {
            int value = Math.random() < 0.9 ? 2 : 4;
            Position position = randomAvaibleCell();
            SimpleTile tile = new SimpleTile(position.getX(), position.getY(), value);
            insertTile(position.getX(), position.getY(), tile);
            tile.bang();
            return tile;
        }

        return null;
    }

    public void addDemonTile() {
        if (cellsAvailable()) {
            Position position = randomAvaibleCell();
            SimpleTile tile = new SimpleTile(position.getX(), position.getY(), DEMON_VALUE);
            insertTile(position.getX(), position.getY(), tile);
            tile.bang();
        }
    }

    public void addJokerTile() {
        if (cellsAvailable()) {
            Position position = randomAvaibleCell();
            SimpleTile tile = new SimpleTile(position.getX(), position.getY(), JOKER_VALUE);
            insertTile(position.getX(), position.getY(), tile);
            tile.bang();
        }
    }


    public void update() {

        boolean readyFlag = true;

        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                if (tiles[x][y] != null) {
                    tiles[x][y].update();
                    readyFlag &= tiles[x][y].isMoveComplete();
                }
            }
        }

        ListIterator<SimpleTile> iter = movingTiles.listIterator();
        while (iter.hasNext()) {

            SimpleTile tile = iter.next();
            tile.update();
            readyFlag &= tile.isMoveComplete();
            if (tile.isMoveComplete()) {
                iter.remove();
            }
        }

        // Must write aftermove changes
        if (isLocked() && readyFlag == true) {

            // remove moving tiles
            movingTiles.clear();

            for (int x = 0; x < size.getWidth(); x++) {
                for (int y = 0; y < size.getHeight(); y++) {

                    SimpleTile tile = tiles[x][y];

                    if (tile != null) {
                        switch (tile.getAfterMove()) {

                        case MUST_SUMMED:
                            tile.setValue(tile.getValue() * 2);
                            tile.bang();
                            break;
                        case NONE:
                            removeTile(x, y);
                            insertTile(tile.getTargetX(), tile.getTargetY(), tile);
                            break;
                        case MUST_BE_DELETED:
                            removeTile(x, y);
                            break;
                        default:
                            break;
                        }
                    }
                }
            }

            if (lastMovingSuccess) {

                if (jokerBeenStep == step) {
                    addJokerTile();
                } else {
                    if (demonBeenStep == step) {
                        addDemonTile();
                    } else {
                        addRandomTile();
                    }
                }

            }
            unlock();
        }
    }

    private Position findTargetForTile(final Position cell, Direction vector) {

        SimpleTile currentTile = getTile(cell);
        if (currentTile == null) {
            return null;
        }

        Position previous = null;
        Position last = cell.clone();

        // Progress towards the vector direction until an obstacle is found
        do {
            previous = last.clone();
            last.set(previous.getX() + vector.getVectorX(), previous.getY() + vector.getVectorY());

            if (inGridRange(last) == false) {
                return previous;
            }

            if (isBusyCell(last)) {

                switch (currentTile.getValue()) {

                case DEMON_VALUE:
                    return previous;


                case JOKER_VALUE:
                    if(getTile(last).isWasChanged() == false && getTile(last).getValue() != JOKER_VALUE) {
                        return last;
                    } else {
                        return previous;
                    }


                default:
                    if ((getTile(cell).getValue() == getTile(last).getValue() && getTile(last).isWasChanged() == false)) {
                        return last;
                    } else {
                        return previous;
                    }
                }
            }

        } while (true);
    }

    /**
     * @return return scores in this move
     */
    public void move(Direction direction) {

        if (isLocked() == true) {
            return;
        }

        lock();

        lastMovingSuccess = false;

        // Creating traversal path
        Pair<List<Integer>, List<Integer>> traversal = GridUtils.getTraversalList(direction, size.getWidth());
        List<Integer> traversalX = traversal.first;
        List<Integer> traversalY = traversal.second;

        resetTilesMovingInfo();
        Position currentPosition = new Position(0, 0);

        // Creating loop by traversals
        for (Integer x : traversalX) {
            for (Integer y : traversalY) {

                SimpleTile tile = getTile(x, y);

                if (tile != null) {
                    currentPosition.set(x, y);
                    Position targetPosition = findTargetForTile(currentPosition, direction);

                    if (currentPosition.equals(targetPosition) == true) {
                        continue;
                    } else {

                        SimpleTile targetTile = getTile(targetPosition);

                        // ≈сли €чейка не взаимодействует с другими €чейками
                        // If target is free cell
                        if (targetTile == null) {
                            removeTile(x, y);
                            insertTile(targetPosition.getX(), targetPosition.getY(), tile);
                            tile.setTargetPosition(targetPosition.getX(), targetPosition.getY());
                            tile.setAfterMove(AfterMove.NONE);

                        } else {
                            
                            removeTile(x, y);
                            movingTiles.add(tile);
                            
                            tile.setTargetPosition(targetTile.getTargetX(), targetTile.getTargetY());

                            targetTile.setAfterMove(AfterMove.MUST_SUMMED);
                            targetTile.setWasChanged(true);
                            
                            // if target a sum tile
                            if(tile.getValue() == JOKER_VALUE) {
                                if(targetTile.getValue() == DEMON_VALUE) { 
                                    targetTile.setAfterMove(AfterMove.MUST_BE_DELETED);
                                    tile.setAfterMove(AfterMove.MUST_BE_DELETED);
                                }
                            }
                        }
                        lastMovingSuccess = true;
                    }
                }
            }
        }

        if (lastMovingSuccess) {
            step++;
        }

        canMoving = canMakeMove();
    }

    private boolean canMakeMove() {

        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                if (tiles[x][y] != null) {
                    if (hasNeighborWithEqualValue(x, y)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    // Need correct positions
    public boolean hasNeighborWithEqualValue(int x, int y) {
        SimpleTile tile = getTile(x, y);
        SimpleTile left = getTile(x - 1, y);
        SimpleTile right = getTile(x + 1, y);
        SimpleTile down = getTile(x, y - 1);
        SimpleTile top = getTile(x, y + 1);

        if (left != null) {
            if (tile.getValue() == left.getValue()) {
                return true;
            }
        }
        if (right != null) {
            if (tile.getValue() == right.getValue()) {
                return true;
            }
        }
        if (down != null) {
            if (tile.getValue() == down.getValue()) {
                return true;
            }
        }
        if (top != null) {
            if (tile.getValue() == top.getValue()) {
                return true;
            }
        }

        return false;
    }

    public void resetTilesMovingInfo() {

        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                if (tiles[x][y] != null) {
                    tiles[x][y].setWasChanged(false);
                    tiles[x][y].setAfterMove(AfterMove.NONE);
                }
            }
        }
    }

    public boolean availableCell(Position cell) {
        return tiles[cell.getX()][cell.getY()] == null;
    }

    public void setTile(int x, int y, SimpleTile tile) {
        if (size.inRange(x, y)) {
            tiles[x][y] = tile;
        }
    }

    public void testInit() {

        /*
         * int value = 1;
         * 
         * for (int y = 0; y < size.getHeight(); y++) { for (int x = 0; x < size.getWidth()-1; x++) { value *= 2; tiles[x][y] = new SimpleTile(x, y, value); } }
         */

        tiles[0][0] = new SimpleTile(0, 0, JOKER_VALUE);
        tiles[0][1] = new SimpleTile(0, 1, JOKER_VALUE);
        tiles[0][2] = new SimpleTile(0, 2, JOKER_VALUE);
        tiles[0][3] = new SimpleTile(0, 3, JOKER_VALUE);
        
        tiles[1][0] = new SimpleTile(1, 0, DEMON_VALUE);
        tiles[1][1] = new SimpleTile(1, 1, JOKER_VALUE);
        tiles[1][2] = new SimpleTile(1, 2, JOKER_VALUE);
        tiles[1][3] = new SimpleTile(1, 3, DEMON_VALUE);
        
        tiles[2][0] = new SimpleTile(2, 0, DEMON_VALUE);
        tiles[2][1] = new SimpleTile(2, 1, DEMON_VALUE);
        tiles[2][2] = new SimpleTile(2, 2, DEMON_VALUE);
        tiles[2][3] = new SimpleTile(2, 3, DEMON_VALUE);
        
    }

    public boolean isCanMoving() {
        return canMoving;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getJokerBeenStep() {
        return jokerBeenStep;
    }

    public int getDemonBeenStep() {
        return demonBeenStep;
    }

    public void setJokerBeenStep(int jokerBeenStep) {
        this.jokerBeenStep = jokerBeenStep;
    }

    public void setDemonBeenStep(int demonBeenStep) {
        this.demonBeenStep = demonBeenStep;
    }

}
