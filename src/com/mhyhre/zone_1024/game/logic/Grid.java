package com.mhyhre.zone_1024.game.logic;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import android.util.Log;
import android.util.Pair;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.game.logic.SimpleTile.AfterMove;
import com.mhyhre.zone_1024.game.logic.demon.DemonBot;
import com.mhyhre.zone_1024.utils.Direction;
import com.mhyhre.zone_1024.utils.Position;
import com.mhyhre.zone_1024.utils.Size;

public class Grid extends SimpleGrid {


    public static final int DEMON_VALUE = -1;
    private Random random;
    private boolean canMoving;
    private boolean lastMovingSuccess;
    private DemonBot demon;

    public Grid(Size size, boolean addDefaultDemon) {
        super(size);
        random = new Random();
        canMoving = true;
        
        if(addDefaultDemon) {
            addDemon();
        }
    }
    
    private void addDemon() {
        Position targetPosition = randomAvaibleCell();
        demon = new DemonBot(targetPosition.getX(), targetPosition.getY(), DEMON_VALUE, this);
        insertTile(targetPosition.getX(), targetPosition.getY(), demon);
    }

    public Position randomAvaibleCell() {
        List<Position> freeCells = availableCells();
        if (freeCells.size() > 0) {
            return freeCells.get(random.nextInt(freeCells.size()));
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
               resetTilesMovingInfo();
               demon.onStep();
               addRandomTile();
            }
            

            

            canMoving = canMakeMove();
            unlock();
        }
    }

    private Position findTargetForTile(final Position cell, Direction direction) {

        SimpleTile currentTile = getTile(cell);
        if (currentTile == null) {
            return null;
        }

        Position previous = null;
        Position last = cell;

        // Progress towards the vector direction until an obstacle is found
        do {
            previous = last;
            last = last.addVector(direction.getVector());

            if (inGridRange(last) == false) {
                return previous;
            }

            if (isBusyCell(last)) {

                if(currentTile.getValue() == DEMON_VALUE) {
                    return previous;

                } else {
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

        // Creating loop by traversals
        for (Integer x : traversalX) {
            for (Integer y : traversalY) {

                SimpleTile tile = getTile(x, y);

                if (tile != null && (tile.getValue() != DEMON_VALUE || (tile.getValue() == DEMON_VALUE  && getDemon().isSleep()))) {

                    Position currentPosition = new Position(x, y);
                    Position targetPosition = findTargetForTile(currentPosition, direction);

                    if (currentPosition.equals(targetPosition) == true) {
                        continue;
                        
                    } else {

                        SimpleTile targetTile = getTile(targetPosition);

                        // ≈сли €чейка не взаимодействует с другими €чейками
                        if (targetTile == null) {
                            replaceTile(currentPosition, targetPosition);
                            tile.setAfterMove(AfterMove.NONE);
                            
                            if(tile.getValue() == DEMON_VALUE) {
                                tile.setWasChanged(true);
                            }
                            

                        } else {
                            
                            removeTile(x, y);
                            movingTiles.add(tile);
                            
                            tile.setTargetPosition(targetTile.getTargetX(), targetTile.getTargetY());

                            targetTile.setAfterMove(AfterMove.MUST_SUMMED);
                            targetTile.setWasChanged(true);
                        }
                        lastMovingSuccess = true;
                    }
                }
            }
        }

        canMoving = canMakeMove();
    }
    
    public void replaceTile(Position srcPos, Position destPos) {
        if(srcPos == null || destPos == null) {
            throw new IllegalArgumentException("Null argument");
        }
        
        if(size.inRange(srcPos) == false || size.inRange(destPos) == false) {
            throw new IllegalArgumentException("Argument not in range");
        }
        
        SimpleTile first = getTile(srcPos);
        removeTile(srcPos.getX(), srcPos.getY());
        insertTile(destPos.getX(), destPos.getY(), first);
        
        if(first != null) {
            first.setTargetPosition(destPos.getX(), destPos.getY());
        }
    }

    private boolean canMakeMove() {

       
        
        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                
                
                
                if (tiles[x][y] != null) {
                    Position pos = new Position(0, 0);
                    if (canMoveTile(pos)) {
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
    public boolean canMoveTile(final Position pos) {
        
        if(pos == null || size.inRange(pos) == false) {
            throw new IllegalArgumentException();
        }
        
        SimpleTile tile = getTile(pos);
        
        if(tile == null) {
            return false;
        }
        
        for(Direction dir: Direction.values()) {
            
            Position neighborPos = pos.addVector(dir.getVector());
            SimpleTile neighborTile = getTile(neighborPos);
            
            if(neighborTile != null) {
                if(neighborTile.getValue() == tile.getValue()) {
                    return true;
                }
            } else {
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
        int value = 1;
          
        for (int y = 0; y < 4; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                if(tiles[x][y] == null) {
                    value++;
                    tiles[x][y] = new SimpleTile(x, y, value);
                }
            }
        }
         */
        /*
        Position targetPosition = new Position(0, 0);
        demon = new DemonBot(targetPosition.getX(), targetPosition.getY(), DEMON_VALUE, this);
        insertTile(targetPosition.getX(), targetPosition.getY(), demon);
        demon.setBehaviorIntention(Intention.EAT_BY_DIRECTION);
        demon.setIntentionDirection(Direction.RIGHT);
        demon.setHunger(0);

        tiles[0][1] = new SimpleTile(0, 1, 16);
        tiles[0][2] = new SimpleTile(0, 2, 32);
        tiles[0][3] = new SimpleTile(0, 3, 64);
        
        tiles[1][0] = new SimpleTile(1, 0, 128);
        tiles[1][1] = new SimpleTile(1, 1, 128);
        tiles[1][2] = new SimpleTile(1, 2, 16);
        */
        
    }

    public boolean isCanMoving() {
        return canMoving;
    }

    public DemonBot getDemon() {
        return demon;
    }
    
    public void setDemon(DemonBot bot) {
        demon = bot;
    }

    public boolean isLastMovingSuccess() {
        return lastMovingSuccess;
    }
    
    public Position getDemonPosition() {
        
        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                if (tiles[x][y] != null) {
                    if(tiles[x][y].getValue() == DEMON_VALUE) {
                        return new Position(x, y);
                    }
                }
                
            }
        }
        throw new NullPointerException("Can't find demon position");
    }

    public void logState() {
        
        StringBuilder matrix = new StringBuilder();
        Log.i(MainActivity.DEBUG_ID, "Grid: logState:");
        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                if (tiles[x][y] != null) {
                    matrix.append("" + tiles[x][y].getValue());
                } else {
                    matrix.append("n");
                }
                
            }
            matrix.append("\n");
        }
        
        Log.i(MainActivity.DEBUG_ID, matrix.toString());
        
    }

}
