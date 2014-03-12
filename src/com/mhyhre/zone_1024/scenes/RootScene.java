/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.scenes;

import com.mhyhre.zone_1024.MainActivity;

import org.andengine.entity.scene.Scene;

public class RootScene extends Scene {

    public static boolean Preloaded = false;

    public RootScene() {
        setBackgroundEnabled(false);

        MainActivity.resources.LoadResourcesForPreloader();
        MainActivity.resources.loadAtlases();
        MainActivity.resources.loadFonts();
        MainActivity.resources.loadSounds();
        
        GameScene gameScene = new GameScene();
        attachChild(gameScene);
        gameScene.show();
    }

    public void onSceneBackPress() {
 
    }

}
