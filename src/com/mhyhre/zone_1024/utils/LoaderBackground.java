package com.mhyhre.zone_1024.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.color.Color;


import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.game.logic.SimpleTile;

public class LoaderBackground extends SpriteBatch {

    private static final int COUNT_OF_SPRITES = 4*8;
    private Size size;
    private ArrayList<SimpleTile> tiles;
    private List<Integer> valuesList;
    private Random random;
    private final ITextureRegion cellRegion;
    private TileColors tileColor;
    private float tilesRandomAngle;

    public LoaderBackground(float x, float y) {
        super(x, y, MainActivity.resources.getTextureAtlas("User_Interface"), COUNT_OF_SPRITES, MainActivity.getVboManager());

        tileColor = TileColors.getInstance();
        valuesList = Arrays.asList(2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096);
        size = new Size(4, 8);
        tiles = new ArrayList<SimpleTile>(COUNT_OF_SPRITES);
        random = new Random();

        cellRegion = MainActivity.resources.getTextureRegion("Cell");

        tilesRandomAngle = random.nextBoolean() ? 0 : 45;
     
        fillTiles();
    }

    private void fillTiles() {
        for(int x = 0; x < size.getWidth(); x++) {
            for(int y = 0; y < size.getHeight(); y++) {
                int value = valuesList.get(random.nextInt(valuesList.size()));
                int xPosition = (int) (cellRegion.getWidth()+5)*(x-size.getWidth()/2);
                int yPosition = (int) (cellRegion.getHeight()+5)*y;
                tiles.add(new SimpleTile(xPosition, yPosition, value));
            } 
        }
    }

    private void updateTiles() {
        
        int rebornY = (int) ((size.getHeight()-1)*(cellRegion.getWidth()+5));
        
        for(int x = 0; x < size.getWidth(); x++) {
            for(int y = 0; y < size.getHeight(); y++) {
                SimpleTile tile = tiles.get(y * size.getWidth() + x);
                if(tile.getY() < -(cellRegion.getWidth()+5)) {
                    tile.setY(rebornY);
                    int value = valuesList.get(random.nextInt(valuesList.size()));
                    tile.setValue(value);
                }
                tile.setY(tile.getY() - 1);
            } 
        }
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        updateTiles();
        
        
        for (SimpleTile tile : tiles) {
            Color c = tileColor.getColorByCellValue(tile.getValue());
            float cAll = (c.getRed()+ c.getGreen()+ c.getBlue())/3;
            this.draw(cellRegion, tile.getX(), tile.getY(), cellRegion.getWidth(), cellRegion.getHeight(),
                    tilesRandomAngle, cAll,cAll,cAll, 0.5f);
        }
        this.submit();

        super.onManagedUpdate(pSecondsElapsed);
    }
}
