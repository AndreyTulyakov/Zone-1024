/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.scenes;

import org.andengine.entity.scene.CameraScene;
import org.andengine.input.touch.TouchEvent;

import com.mhyhre.zone_1024.MainActivity;

public class SimpleScene extends CameraScene {

    public SimpleScene() {
        super(MainActivity.camera);
        hide();
    }

    public void show() {
        setVisible(true);
        setIgnoreUpdate(false);
    }

    public void hide() {
        setVisible(false);
        setIgnoreUpdate(true);
    }

}
