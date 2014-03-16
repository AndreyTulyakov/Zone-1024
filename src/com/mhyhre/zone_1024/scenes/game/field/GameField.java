/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.scenes.game.field;

import java.util.ArrayList;
import java.util.LinkedList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;

import android.util.Log;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.scenes.SimpleScene;
import com.mhyhre.zone_1024.utils.CellColors;
import com.mhyhre.zone_1024.utils.Directions;
import com.mhyhre.zone_1024.utils.Size;

public class GameField extends SimpleScene {

    private final String cellsRegionName = "EquipmentCell";
    private final int BETWEEN_CELLS_SIZE = 4;

    private FieldData data;
    private Rectangle background;
    private SpriteBatch cellsGraphics;

    private ArrayList<Text> cellsTextEntityList;

    private CellColors cellColors;

    public GameField() {

        setBackgroundEnabled(false);
        show();
        
        cellColors = CellColors.getInstance();

        data = new FieldData(new Size(5, 5));
        data.putNewElement();

        ITextureRegion cellRegion = MainActivity.resources.getTextureRegion(cellsRegionName);
        int backgroundSizeX = ((int) cellRegion.getWidth() + BETWEEN_CELLS_SIZE) * data.getWidth() + BETWEEN_CELLS_SIZE;
        int backgroundSizeY = ((int) cellRegion.getHeight() + BETWEEN_CELLS_SIZE) * data.getHeight() + BETWEEN_CELLS_SIZE;

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

        createCellsGraphic();
    }

    private void createCellsGraphic() {

        cellsGraphics = new SpriteBatch(MainActivity.resources.getTextureAtlas("User_Interface"), 50, MainActivity.getVboManager());

        cellsGraphics.setPosition(MainActivity.getHalfWidth() - ((background.getWidth() / 2) - BETWEEN_CELLS_SIZE),
                MainActivity.getHalfHeight() - ((background.getHeight() / 2) - BETWEEN_CELLS_SIZE));
        attachChild(cellsGraphics);

        // Create cells label
        int textCount = data.getWidth() * data.getHeight();
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

    private void updateCellsGraphic() {
        
        ITextureRegion cellRegion = MainActivity.resources.getTextureRegion(cellsRegionName);
        Size cellsOffset = new Size((int) cellRegion.getWidth() + BETWEEN_CELLS_SIZE, (int) cellRegion.getHeight() + BETWEEN_CELLS_SIZE);

        for (int x = 0; x < data.getWidth(); x++) {
            for (int y = 0; y < data.getWidth(); y++) {

                Color cellColor = cellColors.getColorByCellValue(data.get(x, y));

                float cellX = x * cellsOffset.getWidth();
                float cellY = y * cellsOffset.getHeight();
                cellsGraphics.draw(cellRegion, cellX, cellY, cellRegion.getWidth(), cellRegion.getHeight(), 0, cellColor.getRed(), cellColor.getGreen(),
                        cellColor.getBlue(), 1);

                Text cellText = cellsTextEntityList.get(y * data.getWidth() + x);
                if (data.get(x, y) == 0) {
                    cellText.setVisible(false);
                } else {
                    cellText.setText("" + data.get(x, y));
                    cellText.setColor(0,0,0);
                    cellText.setPosition(cellsGraphics.getX() + cellX + cellRegion.getWidth()/2, cellsGraphics.getY() + cellY + cellRegion.getWidth()/2);
                    cellText.setVisible(true);
                    
                }

            }
        }
                     
        cellsGraphics.submit();

    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        updateCellsGraphic();
        super.onManagedUpdate(pSecondsElapsed);
    }

    public void onMoveField(Directions moveDicrection) {
        data.slideTo(moveDicrection);
        data.putNewElement();

        if (data.hasFreeCell() == false) {
            MainActivity.getRootScene().gameOver();
        }
    }
}
