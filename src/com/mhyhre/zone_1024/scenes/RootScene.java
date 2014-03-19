/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.scenes;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.game.logic.GameManager;
import com.mhyhre.zone_1024.touch.TouchSlidingEventDetector;
import com.mhyhre.zone_1024.utils.Size;

import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

public class RootScene extends Scene {

    private GameVisualizationScene gameScene;
    private GameManager gameManager;
    private TouchSlidingEventDetector moveEventDetector;


    public RootScene() {
        setBackgroundEnabled(false);

        MainActivity.resources.LoadResourcesForPreloader();
        MainActivity.resources.loadAtlases();
        MainActivity.resources.loadFonts();
        MainActivity.resources.loadSounds();
        
        gameManager = new GameManager(new Size(4,4));
        moveEventDetector = new TouchSlidingEventDetector(gameManager);

        gameScene = new GameVisualizationScene(gameManager);
        attachChild(gameScene);
        gameScene.show();
    }

    public void onSceneBackPress() {
        
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {     
        moveEventDetector.onTouchEvent(pSceneTouchEvent);
        gameScene.onSceneTouchEvent(pSceneTouchEvent);
        return super.onSceneTouchEvent(pSceneTouchEvent);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        gameManager.update();
        super.onManagedUpdate(pSecondsElapsed);
    }
    
/*
    public void gameOver() {
        MainActivity.vibrate(100);
        MainActivity.Me.finish();
    }
*/
}
