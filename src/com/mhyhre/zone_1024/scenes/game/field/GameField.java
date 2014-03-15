/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.scenes.game.field;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.util.Log;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.scenes.SimpleScene;
import com.mhyhre.zone_1024.utils.Directions;
import com.mhyhre.zone_1024.utils.Size;

public class GameField extends SimpleScene {

    FieldData data;

    Rectangle background;
    SpriteBatch cellsGraphics;

    public GameField() {

        setBackgroundEnabled(false);
        show();

        data = new FieldData(new Size(5, 5));

        background = new Rectangle(MainActivity.getHalfWidth(), MainActivity.getHalfHeight(), 400, 400, MainActivity.getVboManager()) {

            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {

                if (pSceneTouchEvent.isActionDown()) {
                    Log.i(MainActivity.DEBUG_ID, "GameField: Touched background!");
                }
                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        background.setColor(0, 0.2f, 0.6f);
        attachChild(background);
        registerTouchArea(background);

        cellsGraphics = new SpriteBatch(MainActivity.resources.getTextureAtlas("User_Interface"), 50, MainActivity.getVboManager());
        attachChild(cellsGraphics);
        drawCells();
    }

    private void drawCells() {

        ITextureRegion cellRegion = MainActivity.resources.getTextureRegion("EquipmentCell");
        Size cellsOffset = new Size((int)cellRegion.getWidth()+10, (int)cellRegion.getHeight()+10);
        
        
        for (int x = 0; x < data.getWidth(); x++) {
            for (int y = 0; y < data.getWidth(); y++) {
                
                cellsGraphics.draw(cellRegion, x*cellsOffset.getWidth(), y*cellsOffset.getHeight(), 
                        cellRegion.getWidth(), cellRegion.getHeight(), 0, 1, 1, 1, 1);
            }
        }

        cellsGraphics.submit();
    }
    
    public void onMoveField(Directions moveDicrection) {
        data.slideTo(moveDicrection);
    }
}
