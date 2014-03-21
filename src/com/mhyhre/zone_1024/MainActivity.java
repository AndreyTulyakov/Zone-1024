/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024;

import org.andengine.BuildConfig;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import com.mhyhre.zone_1024.scenes.RootScene;

import android.content.res.AssetManager;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;


public class MainActivity extends SimpleBaseGameActivity {

    public static final String DEBUG_ID = "MHYHRE";
    public static final String PREFERENCE_ID = "MY_PREF";
    
    public static MainActivity Me;
    public static Camera camera;
    public static ResourceManager resources;
    public static RootScene sceneRoot;
    
    private static final int width = 960;
    private static final int height = 540;
    private static float halfWidth;
    private static float halfHeight;
    
    private AssetManager assetManager;
    private Vibrator vibrator;
    
    private boolean vibroEnabled = true;
    private boolean soundEnabled = true;
    

    @Override
    public EngineOptions onCreateEngineOptions() {

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(40);

        Me = this;
        assetManager = getAssets();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        halfWidth = width / 2.0f;
        halfHeight = height / 2.0f;

        camera = new Camera(0, 0, width, height);

        EngineOptions mEngineOptions = new EngineOptions(
                true, ScreenOrientation.LANDSCAPE_FIXED,
                new RatioResolutionPolicy(metrics.widthPixels,
                metrics.heightPixels), camera);
        mEngineOptions.getAudioOptions().setNeedsSound(true);
        mEngineOptions.getTouchOptions().setNeedsMultiTouch(true);
        return mEngineOptions;
    }
    
    
    public static RootScene getRootScene() {
        return sceneRoot;
    }

    @Override
    public void onCreateResources() {
        if (BuildConfig.DEBUG)
            Log.i(DEBUG_ID, this.getClass().getSimpleName() + ".onCreateResources");
    }

    @Override
    public Scene onCreateScene() {
        if (BuildConfig.DEBUG)
            Log.i(DEBUG_ID, this.getClass().getSimpleName() + ".onCreateScene");
        this.mEngine.registerUpdateHandler(new FPSLogger());

        resources = new ResourceManager();

        sceneRoot = new RootScene();
        return sceneRoot;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (BuildConfig.DEBUG)
                Log.i(DEBUG_ID, this.getClass().getSimpleName() + ".onKeyDown: KEYCODE_BACK");
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (RootScene.getState() == RootScene.GameStates.LOADER) {
            super.onBackPressed();
        } else {
            sceneRoot.onSceneBackPress();
        }
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public void onDestroy() {

        if (BuildConfig.DEBUG)
            Log.i(DEBUG_ID, this.getClass().getSimpleName() + ".onDestroy");
        
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static float getWidth() {
        return width;
    }

    public static float getHeight() {
        return height;
    }

    public static float getHalfWidth() {
        return halfWidth;
    }

    public static float getHalfHeight() {
        return halfHeight;
    }

    public static boolean isVibroEnabled() {
        return Me.vibroEnabled;
    }

    public static void setVibroEnabled(boolean enabled) {
        Me.vibroEnabled = enabled;
    }

    public static boolean isSoundEnabled() {
        return Me.soundEnabled;
    }

    public static void setSoundEnabled(boolean enabled) {
            Me.soundEnabled = enabled;
    }

    public static void vibrate(long milliseconds) {
        if (Me.vibroEnabled) {
            Me.vibrator.vibrate(milliseconds);
        }
    }
    
    public static VertexBufferObjectManager getVboManager() {
        if(Me != null) {
            return Me.getVertexBufferObjectManager();
        }
        return null;
    }

}
