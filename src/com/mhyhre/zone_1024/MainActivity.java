/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024;

import java.io.IOException;

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
    public static final String PREFERENCE_ID = "MHYHRE_ZONE1024_PREF";
    
    public static MainActivity Me;
    public static Camera camera;
    public static ResourceManager resources;
    public static RootScene sceneRoot;
    
    private static final int width = 540;
    private static final int height = 960;
    private static float halfWidth;
    private static float halfHeight;
    
    private AssetManager assetManager;
    private Vibrator vibrator;
    private static PreferenceManager preferenceManager;
    
    @Override
    public EngineOptions onCreateEngineOptions() {
        
        Log.i(DEBUG_ID, this.getClass().getSimpleName() + ".onCreateEngineOptions");
        
        Me = this;
        assetManager = getAssets();
        
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);      
        preferenceManager = PreferenceManager.getInstance(this, PREFERENCE_ID);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        halfWidth = width / 2.0f;
        halfHeight = height / 2.0f;

        camera = new Camera(0, 0, width, height);

        EngineOptions mEngineOptions = new EngineOptions(
                true, ScreenOrientation.PORTRAIT_FIXED,
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
        if (sceneRoot != null) {
            sceneRoot.onSceneBackPress();
        }
    }
    
    @Override
    public void onDestroyResources() throws IOException {
        Log.i(DEBUG_ID, "MainActivity: onDestroyResources()");
        super.onDestroyResources();
    }
    

    public AssetManager getAssetManager() {
        return assetManager;
    }
    
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }
    
    
    
    
    @Override
    protected void onPause() {
        Log.i(DEBUG_ID, "Game paused");
        this.getEngine().stop();
        super.onPause();
    }
    
    
    @Override
    public void onResume() {
        Log.i(DEBUG_ID, "Game resumed");
        this.getEngine().start();
        super.onResume();
    }

    public void onDestroy() {

        preferenceManager.savePreferences();

        if (BuildConfig.DEBUG)
            Log.i(DEBUG_ID, this.getClass().getSimpleName() + ".onDestroy");
        
        super.onDestroy();
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

    public static void vibrate(long milliseconds) {
        if (PreferenceManager.isVibroEnabled()) {
            Me.vibrator.vibrate(milliseconds);
        }
    }

    
    public static VertexBufferObjectManager getVboManager() {
        if(Me != null) {
            return Me.getVertexBufferObjectManager();
        }
        return null;
    }


    public static PreferenceManager getPreferenceManager() {
        return preferenceManager;
    }

}
