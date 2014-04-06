/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.scenes;

import java.util.Map;

import android.util.Log;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.PreferenceManager;
import com.mhyhre.zone_1024.game.logic.GameManager;
import com.mhyhre.zone_1024.game.logic.GridUtils;
import com.mhyhre.zone_1024.touch.TouchSlidingEventDetector;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

public class RootScene extends Scene {
    
    public enum GameStates {
        // Q - Question
        LOADER,
        ABOUT,
        NEW_GAME,
        SCORES_VIEW,
        GAME_PROCESS,
        RESTART_Q,
        GAME_WIN_SCENE,
        GAME_OVER_SCENE,
        TERMINATE,
        CONTINUE_GAME_Q;
    }

    public static RootScene Me;
    private static GameStates state = GameStates.LOADER;
    
    private LoaderScene loaderScene;
    private GameScene gameScene;
    private QuestionScene questionScene;
    private ScoreScene scoreScene;
    private GameOverScene gameOverScene;
    private AboutScene aboutScene;
    private GameWinScene gameWinScene;

    
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

        gameManager = GameManager.getInstance();
        moveEventDetector = new TouchSlidingEventDetector(gameManager); 
        
        loaderScene = new LoaderScene();
        attachChild(loaderScene);
        
        aboutScene = new AboutScene();
        attachChild(aboutScene);
        
        scoreScene = new ScoreScene();
        attachChild(scoreScene);

        questionScene = new QuestionScene();
        attachChild(questionScene);
   
        gameScene = new GameScene(gameManager);
        attachChild(gameScene);
        
        gameOverScene = new GameOverScene();
        attachChild(gameOverScene);
        
        gameWinScene = new GameWinScene();
        attachChild(gameWinScene);
        
        setState(GameStates.LOADER);
    }
    
    public void setState(GameStates state) {
        RootScene.state = state;
        
        Log.i(MainActivity.DEBUG_ID, "Selected state: " + state);
        
        loaderScene.hide();
        scoreScene.hide();
        questionScene.hide();
        gameScene.hide();
        gameOverScene.hide();
        gameWinScene.hide();
        aboutScene.hide();
        
        
        switch(state) {
        
        case LOADER:
            loaderScene.show();
            break;
            
        case ABOUT:
            aboutScene.show();
            break;
            
        case GAME_PROCESS:
            gameScene.show();
            break;
            
        case SCORES_VIEW:
            scoreScene.show();
            break;
            
        case NEW_GAME:
            gameManager.restart();
            setState(GameStates.GAME_PROCESS);
            break;
            
        case CONTINUE_GAME_Q:
            questionScene.show();
            break;
            
        case RESTART_Q:
            questionScene.show();
            break;
            
        case GAME_WIN_SCENE:
            gameScene.show();
            gameWinScene.show();
            break;
            
        case GAME_OVER_SCENE:
            gameScene.show();
            gameOverScene.show();
            break;
            
        case TERMINATE:
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
            
        case GAME_OVER_SCENE:
            setState(GameStates.RESTART_Q);
            break;
            
        case ABOUT:
            setState(GameStates.LOADER);
            break;

        case GAME_PROCESS:
            gameManager.saveGame();
            setState(GameStates.LOADER);
            break;
            
        case SCORES_VIEW:
            setState(GameStates.LOADER);
            break;
            
        case LOADER:
            setState(GameStates.TERMINATE);
            break;
            
        case RESTART_Q:
            break;

        case CONTINUE_GAME_Q:
            setState(GameStates.LOADER);
            break;           
            
        case GAME_WIN_SCENE:
            setState(GameStates.SCORES_VIEW);
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
            break;
            
        case ABOUT:
            aboutScene.onSceneTouchEvent(pSceneTouchEvent);
            break;
            
        case GAME_OVER_SCENE:
            gameOverScene.onSceneTouchEvent(pSceneTouchEvent);
            break;
            
        case GAME_PROCESS:           
            moveEventDetector.onTouchEvent(pSceneTouchEvent);
            gameScene.onSceneTouchEvent(pSceneTouchEvent);
            break;
            
        case NEW_GAME:
            break;
            
        case SCORES_VIEW:
            scoreScene.onSceneTouchEvent(pSceneTouchEvent);
            break;
            
        case CONTINUE_GAME_Q:
            questionScene.onSceneTouchEvent(pSceneTouchEvent);
            break;
            
        case RESTART_Q:
            questionScene.onSceneTouchEvent(pSceneTouchEvent);
            break;
            
        case GAME_WIN_SCENE:
            gameWinScene.onSceneTouchEvent(pSceneTouchEvent);
            break;
        default:
            break;
        }
        
        return super.onSceneTouchEvent(pSceneTouchEvent);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        
        switch (state) {
        
        case GAME_PROCESS:
            gameManager.update();
            break;
            
        case GAME_OVER_SCENE:
            gameManager.update();
            break;
            
        case GAME_WIN_SCENE:
            gameManager.update();
            break;
            
        default:
            break;
        }   
        super.onManagedUpdate(pSecondsElapsed);
    }
    

}
