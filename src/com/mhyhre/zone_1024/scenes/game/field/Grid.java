package com.mhyhre.zone_1024.scenes.game.field;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.mhyhre.zone_1024.utils.Position;
import com.mhyhre.zone_1024.utils.Size;

public class Grid {

    private Size size;
    private Tile[][] cells;
    private Random random;

    public Grid(Size size) {
        random = new Random();
        this.size = size;
        cells = new Tile[size.getWidth()][size.getHeight()];
    }

    private Position randomAvaibleCell() {
        List<Position> freeCells = availableCells();
        if (freeCells.size() > 0) {
            return freeCells.get(random.nextInt(freeCells.size())).clone();
        }
        return null;
    }

    private List<Position> availableCells() {
        LinkedList<Position> freePositions = new LinkedList<Position>();
        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                if (cells[x][y] == null) {
                    freePositions.add(new Position(x, y));
                }
            }
        }
        return freePositions;
    }

    public boolean isCellsAvailable() {
        return availableCells().size() > 0;
    };

    public boolean cellAvailable(Position cell) {
        return !cellOccupied(cell);
    };

    public boolean cellOccupied(Position cell) {
        return cellContent(cell) != null;
    };

    public Tile cellContent(Position cell) {
        if (withinBounds(cell)) {
            return cells[cell.getX()][cell.getY()];
        } else {
            return null;
        }
    };

    public void insertTile(Tile tile) {
        cells[tile.x][tile.y] = tile;
    }

    public void removeTile(Tile tile) {
        cells[tile.x][tile.y] = null;
    };

    public boolean withinBounds(Position position) {
        boolean value = (position.getX() >= 0) && (position.getX() < size.getWidth());
        value &= (position.getY() >= 0) && (position.getY() < size.getHeight());
        return value;
    };
}
