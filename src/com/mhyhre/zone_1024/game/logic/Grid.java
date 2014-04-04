package com.mhyhre.zone_1024.game.logic;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.util.Log;
import android.util.Pair;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.utils.Direction;
import com.mhyhre.zone_1024.utils.Position;
import com.mhyhre.zone_1024.utils.Size;

public class Grid extends SimpleGrid {

    private Random random;
    private boolean locked;

    public Grid(Size size) {
        super(size);
        random = new Random();
    }

    public Position randomAvaibleCell() {
        List<Position> freeCells = availableCells();
        if (freeCells.size() > 0) {
            return freeCells.get(random.nextInt(freeCells.size())).clone();
        }
        return null;
    }

    public List<Position> availableCells() {
        LinkedList<Position> freePositions = new LinkedList<Position>();
        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                if (tiles[x][y] == null) {
                    freePositions.add(new Position(x, y));
                }
            }
        }
        return freePositions;
    }

    public boolean cellsAvailable() {
        return availableCells().size() > 0;
    }

    public boolean isCellAvailable(Position cell) {
        if (inGridRange(cell)) {
            return tiles[cell.getX()][cell.getY()] != null;
        }
        return false;
    }

    private void insertTile(int column, int row, SimpleTile tile) {
        if (inGridRange(column, row)) {
            tiles[column][row] = tile;
        }
    }

    private void removeTile(Position cellPosition) {
        if (inGridRange(cellPosition)) {
            tiles[cellPosition.getX()][cellPosition.getY()] = null;
        }
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

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    public boolean isLocked() {
        return locked;
    }

    public void update() {

        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                if (tiles[x][y] != null) {
                    tiles[x][y].update();
                }
            }
        }
    }

    private Position findTargetForTile(final Position cell, Direction vector) {
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
                if (getTile(cell).getValue() == getTile(last).getValue() && getTile(last).isWasChanged() == false) {
                    return last;
                } else {
                    return previous;
                }
            }

        } while (true);
    }

    public void resetTilesMovingInfo() {

        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                if (tiles[x][y] != null) {
                    tiles[x][y].setWasChanged(false);
                }
            }
        }
    }

    public boolean availableCell(Position cell) {
        return tiles[cell.getX()][cell.getY()] == null;
    }

    /**
     * @return return scores in this move
     */
    public int move(Direction direction) {

        Log.i(MainActivity.DEBUG_ID, "Grid::Move::To " + direction.name());

        int scores = 0;

        // Creating traversal path
        Pair<List<Integer>, List<Integer>> traversal = GridUtils.getTraversalList(direction, size.getWidth());
        List<Integer> traversalX = traversal.first;
        List<Integer> traversalY = traversal.second;

        resetTilesMovingInfo();
        Position currentPosition = new Position(0, 0);

        boolean somethingWasMoved = false;

        // Creating loop by traversals
        for (Integer x : traversalX) {
            for (Integer y : traversalY) {

                SimpleTile tile = getTile(x, y);

                if (tile != null) {
                    currentPosition.set(x, y);
                    Position targetPosition = findTargetForTile(currentPosition, direction);

                    if (currentPosition.equals(targetPosition) == true) {
                        Log.i(MainActivity.DEBUG_ID, "Grid::Move::Position" + currentPosition.toString() + " Cant move to " + direction.name());
                        continue;
                    } else {

                        SimpleTile targetTile = getTile(targetPosition);

                        if (targetTile == null) {
                            tile.setPosition(targetPosition.getX(), targetPosition.getY());
                            insertTile(targetPosition.getX(), targetPosition.getY(), tile);
                            removeTile(currentPosition);
                        } else {
                            targetTile.setValue(targetTile.getValue() * 2);
                            targetTile.setWasChanged(true);
                            targetTile.bang();
                            removeTile(currentPosition);
                            scores += targetTile.getValue();
                        }
                        somethingWasMoved = true;
                    }
                }
            }
        }

        if (somethingWasMoved) {
            addRandomTile();
        }
        
        if (canMove() == false) {
            
        }
        
        unlock();
        return scores;
    }

    public boolean canMove() {

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

    public void testInit() {
       
        
        int value = 1;
        
            for (int y = 0; y < size.getHeight(); y++) {
                for (int x = 0; x < size.getWidth()-1; x++) {
                value *= 2;
                tiles[x][y] = new SimpleTile(x, y, value);
            }
        }
        
               
        //tiles[0][0] = new SimpleTile(0, 0, 512);
        //tiles[0][1] = new SimpleTile(0, 1, 512);
    }



}
