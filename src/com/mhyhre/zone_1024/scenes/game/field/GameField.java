/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.scenes.game.field;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.scenes.SimpleScene;

public class GameField extends SimpleScene {

    Rectangle background;
    SpriteBatch cellsGraphics;

    public GameField() {
        
        setBackgroundEnabled(false);
        show();
        
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
        
        
        cellsGraphics = new SpriteBatch(MainActivity.resources.getTextureAtlas("User_Interface"),
                50, MainActivity.getVboManager());
        attachChild(cellsGraphics);
        cellsGraphics.submit();
    }

}
