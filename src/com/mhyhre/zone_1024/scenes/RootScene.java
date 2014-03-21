/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.scenes;

import java.util.Map;

import android.util.Log;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.game.logic.GameManager;
import com.mhyhre.zone_1024.touch.TouchSlidingEventDetector;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

public class RootScene extends Scene {
    
    public enum GameStates {
        // Q - Question
        LOADER,
        NEW_GAME,
        STOP_Q,
        STATISTICS,
        GAME_PROCESS,
        RESTART_Q,
        WIN_SCENE,
        FAIL_SCENE,
        END;
    }

    public static RootScene Me;
    private static GameStates state = GameStates.LOADER;
    
    private LoaderScene loaderScene;
    private GameScene gameScene;
    private RestartScene restartScene;
    private StopScene stopScene;
    
    private GameManager gameManager;
    private TouchSlidingEventDetector moveEventDetector;

    
    Map<String, SimpleScene> scenes;

    public RootScene() {
        Me = this;
        setBackgroundEnabled(false);
        
        MainActivity.resources.LoadResourcesForPreloader();
        MainActivity.resources.loadAtlases();
        MainActivity.resources.loadFonts();
        MainActivity.resources.loadSounds();

        loaderScene = new LoaderScene();
        attachChild(loaderScene);
        
        stopScene = new StopScene();
        attachChild(stopScene);

        gameManager = GameManager.getInstance();
        moveEventDetector = new TouchSlidingEventDetector(gameManager);
        
        gameScene = new GameScene(gameManager);
        attachChild(gameScene);
        
        restartScene = new RestartScene();
        attachChild(restartScene);

        
        setState(GameStates.LOADER);
    }
    
    public void setState(GameStates state) {
        RootScene.state = state;
        
        Log.i(MainActivity.DEBUG_ID, "Selected state: " + state);
        
        restartScene.hide();
        loaderScene.hide();
        stopScene.hide();
        gameScene.hide();
        
        switch(state) {
        
        case LOADER:
            loaderScene.show();
            break;
            
        case GAME_PROCESS:
            gameScene.show();
            break;
            
        case NEW_GAME:
            gameManager.restart();
            gameScene.show();
            setState(GameStates.GAME_PROCESS);
            break;
            
        case STOP_Q:
            stopScene.show();
            break;
            
        case RESTART_Q:
            restartScene.show();
            break;
            
        case WIN_SCENE:
            MainActivity.Me.finish();
            break;
            
        case END:
            MainActivity.Me.finish();
            break;
            
        default:
            break;
        
        }
    }
    
    public static GameStates getState() {
        return state;
    }

    public void onSceneBackPress() {
        
        switch(state) {
        case END:
            break;
        case FAIL_SCENE:
            setState(GameStates.LOADER);
            break;
            
        case GAME_PROCESS:
            setState(GameStates.STOP_Q);
            break;
            
        case LOADER:
            break;
            
        case RESTART_Q:
            break;
            
        case STATISTICS:
            setState(GameStates.LOADER);
            break;
            
        case STOP_Q:
            setState(GameStates.GAME_PROCESS);
            break;
            
        case WIN_SCENE:
            setState(GameStates.LOADER);
            break;
            
        default:
            break;
        
        }
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {  
        
        switch (state) {


        case LOADER:
            loaderScene.onSceneTouchEvent(pSceneTouchEvent);
            
        case GAME_PROCESS:
            
            moveEventDetector.onTouchEvent(pSceneTouchEvent);
            gameScene.onSceneTouchEvent(pSceneTouchEvent);
            break;
        case NEW_GAME:
            break;
        case RESTART_Q:
            restartScene.onSceneTouchEvent(pSceneTouchEvent);
        case STOP_Q:
            stopScene.onSceneTouchEvent(pSceneTouchEvent);
            break;
        case WIN_SCENE:
            break;
        default:
            break;
        }
        
        return super.onSceneTouchEvent(pSceneTouchEvent);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        
        switch (state) {
        
        case LOADER:
            
        
        case END:
            break;
        case GAME_PROCESS:
            gameManager.update();
            
            if(gameManager.isOver()) {
                setState(GameStates.RESTART_Q);
            }
            
            if(gameManager.isWon()) {
                
                    setState(GameStates.RESTART_Q);
               
            }
            break;
        case NEW_GAME:
            break;
        case RESTART_Q:
            break;
        case WIN_SCENE:
            break;
        default:
            break;
        }   
        super.onManagedUpdate(pSecondsElapsed);
    }

}
