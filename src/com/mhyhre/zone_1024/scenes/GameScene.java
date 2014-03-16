/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;

import android.util.Log;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.R;
import com.mhyhre.zone_1024.scenes.game.field.GameField;
import com.mhyhre.zone_1024.touch.TouchLocker;
import com.mhyhre.zone_1024.touch.TouchSlideDetector;
import com.mhyhre.zone_1024.touch.TouchMotionsHunter;
import com.mhyhre.zone_1024.utils.Directions;

public class GameScene extends SimpleScene implements TouchMotionsHunter {

    private final float TOUCH_LOCK_TIME = 0.4f; // in seconds

    private Background background;
    private Text textEntityScores;
    private Sprite spriteMenu;
    
    private GameField gameField;
    

    private TouchSlideDetector motionDetector;
    private TouchLocker touchLocker;

    public GameScene() {

        background = new Background(0.2f, 0.6f, 0.7f);
        setBackground(background);
        setBackgroundEnabled(true);


        touchLocker = new TouchLocker(TOUCH_LOCK_TIME);
        touchLocker.lock();
        motionDetector = new TouchSlideDetector(this);

        
        gameField = new GameField();
        attachChild(gameField);
        
        
        // Creating sprites
        spriteMenu = new Sprite(0, 0, MainActivity.resources.getTextureRegion("Button1"), MainActivity.Me.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {

                    Log.i(MainActivity.DEBUG_ID, "Pressed menu icon");
                    MainActivity.vibrate(30);
                }
                return true;
            }
        };
        spriteMenu.setPosition(MainActivity.getHalfWidth(), 24);
        attachChild(spriteMenu);
        registerTouchArea(spriteMenu);

        String textScores = MainActivity.Me.getString(R.string.scores);

        // Text
        final IFont usedFont = MainActivity.resources.getFont("Furore");
        textEntityScores = new Text(0, 0, usedFont, textScores, 24, MainActivity.Me.getVertexBufferObjectManager());
        textEntityScores.setPosition(MainActivity.getHalfWidth(), 24);
        attachChild(textEntityScores);
        
        
        
    }

    @Override
    public boolean onSceneTouchEvent(final TouchEvent pSceneTouchEvent) {

        if (touchLocker.isLocked() == false) {
            motionDetector.onTouchEvent(pSceneTouchEvent);
            gameField.onSceneTouchEvent(pSceneTouchEvent);
            return super.onSceneTouchEvent(pSceneTouchEvent);
        }
        return true;
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
 
        touchLocker.update(pSecondsElapsed);
        super.onManagedUpdate(pSecondsElapsed);
    }

    @Override
    public void onDetectedMotionEvent(Directions move) {

        if (move != Directions.NONE) {
            touchLocker.lock();
            gameField.onMoveField(move);
        }

        textEntityScores.setText(move.name());
    }

}