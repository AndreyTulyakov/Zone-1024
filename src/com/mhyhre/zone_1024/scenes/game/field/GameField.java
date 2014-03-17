/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.scenes.game.field;

import java.util.ArrayList;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.color.Color;

import android.util.Log;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.scenes.SimpleScene;
import com.mhyhre.zone_1024.utils.TileColors;
import com.mhyhre.zone_1024.utils.Direction;
import com.mhyhre.zone_1024.utils.Size;

public class GameField extends SimpleScene {

    private final String cellsRegionName = "EquipmentCell";
    private final int BETWEEN_CELLS_SIZE = 4;
    
    private final Size size;
    

    private Grid grid;
    private Rectangle background;
    private SpriteBatch tilesSpriteBatch;


    private ArrayList<Text> cellsTextEntityList;
    private TileColors tileColors;
    

    public GameField(Size size) {

        this.size = size;
        
        setBackgroundEnabled(false);
        show();
        
        tileColors = TileColors.getInstance();

        grid = new Grid(size);

        createBackgroundRect();
        createTilesGraphics();
    }

    private void createTilesGraphics() {

        tilesSpriteBatch = new SpriteBatch(MainActivity.resources.getTextureAtlas("User_Interface"),
                size.getWidth()*size.getHeight(), MainActivity.getVboManager());
        tilesSpriteBatch.setPosition(MainActivity.getHalfWidth() - ((background.getWidth() / 2) - BETWEEN_CELLS_SIZE),
                MainActivity.getHalfHeight() - ((background.getHeight() / 2) - BETWEEN_CELLS_SIZE));
        attachChild(tilesSpriteBatch);

        // Create cells label
        int textCount = size.getWidth() * size.getHeight();
        cellsTextEntityList = new ArrayList<Text>(textCount);

        IFont textFont = MainActivity.resources.getFont("White Furore");
        for (int i = 0; i < textCount; i++) {
            Text text = new Text(0, 0, textFont, "0", 8, MainActivity.getVboManager());
            attachChild(text);
            text.setVisible(false);
            cellsTextEntityList.add(text);
        }
        updateCellsGraphic();
    }
    
    private void createBackgroundRect() {
        
        ITextureRegion cellRegion = MainActivity.resources.getTextureRegion(cellsRegionName);
        int backgroundSizeX = ((int) cellRegion.getWidth() + BETWEEN_CELLS_SIZE) * size.getWidth() + BETWEEN_CELLS_SIZE;
        int backgroundSizeY = ((int) cellRegion.getHeight() + BETWEEN_CELLS_SIZE) * size.getHeight() + BETWEEN_CELLS_SIZE;
        
        background = new Rectangle(MainActivity.getHalfWidth(), MainActivity.getHalfHeight(), backgroundSizeX, backgroundSizeY, MainActivity.getVboManager()) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {

                if (pSceneTouchEvent.isActionDown()) {
                    Log.i(MainActivity.DEBUG_ID, "GameField: Touched background!");
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        background.setColor(0.73f, 0.68f, 0.62f);
        attachChild(background);
        registerTouchArea(background);
    }

    private void updateCellsGraphic() {
        
        ITextureRegion cellRegion = MainActivity.resources.getTextureRegion(cellsRegionName);
        Size cellsOffset = new Size((int) cellRegion.getWidth() + BETWEEN_CELLS_SIZE, (int) cellRegion.getHeight() + BETWEEN_CELLS_SIZE);

        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getWidth(); y++) {

                Color cellColor = tileColors.getColorByCellValue(tiles.getTileValue(x, y));

                float cellX = x * cellsOffset.getWidth();
                float cellY = y * cellsOffset.getHeight();
                tilesSpriteBatch.draw(cellRegion, cellX, cellY, cellRegion.getWidth(), cellRegion.getHeight(), 0, cellColor.getRed(), cellColor.getGreen(),
                        cellColor.getBlue(), 1);

                Text cellText = cellsTextEntityList.get(y * size.getWidth() + x);
                if (tiles.getTileValue(x, y) == 0) {
                    cellText.setVisible(false);
                } else {
                    cellText.setText("" + tiles.getTileValue(x, y));
                    cellText.setColor(0,0,0);
                    cellText.setPosition(tilesSpriteBatch.getX() + cellX + cellRegion.getWidth()/2, tilesSpriteBatch.getY() + cellY + cellRegion.getWidth()/2);
                    cellText.setVisible(true);                  
                }
            }
        }              
        tilesSpriteBatch.submit();
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
            
            tiles.update();
            updateCellsGraphic();

        super.onManagedUpdate(pSecondsElapsed);
    }

    public void onMoveField(Direction moveDicrection) {

        tiles.slideField(moveDicrection);
        tiles.putNewElement();

        if (tiles.hasFreeCell() == false) {
            MainActivity.getRootScene().gameOver();
        }
    }
    
}
