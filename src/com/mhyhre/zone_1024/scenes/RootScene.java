/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.scenes;

import com.mhyhre.zone_1024.MainActivity;

import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

public class RootScene extends Scene {

    public static boolean Preloaded = false;
    
    GameScene gameScene;

    public RootScene() {
        setBackgroundEnabled(false);

        MainActivity.resources.LoadResourcesForPreloader();
        MainActivity.resources.loadAtlases();
        MainActivity.resources.loadFonts();
        MainActivity.resources.loadSounds();
        
        gameScene = new GameScene();
        attachChild(gameScene);
        gameScene.show();
    }

    public void onSceneBackPress() {
 
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        gameScene.onSceneTouchEvent(pSceneTouchEvent);
        return super.onSceneTouchEvent(pSceneTouchEvent);
    }

}
