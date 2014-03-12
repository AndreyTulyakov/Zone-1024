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

    public RootScene() {
        setBackgroundEnabled(false);

        /*
        mSceneLoader = new SceneLoader();
        mSceneLoader.show();
        attachChild(mSceneLoader);

        SetState(SceneStates.Splash);

        MainActivity.resources.loadAtlases();
        MainActivity.resources.loadFonts();
        MainActivity.resources.loadSounds();

        // Scenes Creating ------------------------------------------------
        mSceneMainMenu = new SceneMainMenu();
        mSceneAbout = new SceneAbout();
        mSceneExit = new SceneExit();
        mSceneLevelSelector = new SceneLevelSelector();
        mSceneEquipment = new SceneEquipment();
        mSceneGameLoading = new SceneGameLoading();

        mSceneGame = null;

        attachChild(mSceneMainMenu);
        attachChild(mSceneAbout);
        attachChild(mSceneExit);
        attachChild(mSceneLevelSelector);
        attachChild(mSceneEquipment);
        attachChild(mSceneGameLoading);

        // ---------------------------------------------------------------

        Preloaded = true;
        mSceneLoader.mCaptionTapScreen.setVisible(true);
        mSceneLoader.registerTouchArea(mSceneLoader.TapRect);
*/
    }

    public void onSceneBackPress() {
 
    }

    /*
    public void SetState(SceneStates pState) {

        state = pState;

        if (Preloaded) {

            // Hide all scenes.
            mSceneLoader.hide();
            mSceneMainMenu.hide();
            mSceneAbout.hide();
            mSceneExit.hide();
            mSceneLevelSelector.hide();
            mSceneEquipment.hide();
            mSceneGameLoading.hide();

            if (mSceneGame != null)
                mSceneGame.hide();

            switch (state) {
            case Splash:
                mSceneLoader.show();
                break;

            case MainMenu:
                mSceneMainMenu.show();
                break;

            case LevelSelector:
                mSceneLevelSelector.show();
                break;
                
            case Equipment:
                mSceneEquipment.show();
                break;

            case About:
                mSceneAbout.show();
                break;

            case Exit:
                mSceneExit.show();
                break;

            case GameLoading:

                mSceneGameLoading.setLoaded(false);

                detachChild(mSceneGameLoading);

                if (mSceneGame != null) {
                    this.detachChild(mSceneGame);
                    mSceneGame = null;
                }

                mSceneGame = new SceneGame(mSceneLevelSelector.getSelectedLevel().filename);
                mSceneGame.onManagedUpdate(0);
                mSceneGame.pause();

                mSceneGameLoading.setLevelName(mSceneGame.getLevel().getName());
                mSceneGameLoading.setLevelChapter(mSceneGame.getLevel().getChapter());

                this.attachChild(mSceneGame);
                this.attachChild(mSceneGameLoading);

                mSceneGameLoading.show();
                mSceneGame.show();

                mSceneGameLoading.setLoaded(true);

                break;

            case Game:

                if (mSceneGame != null) {

                    mSceneGame.show();
                    mSceneGame.start();
                }
                break;

            case EndGame:

                SetState(SceneStates.LevelSelector);
                break;

            default:

                break;
            }
        }

    }

    public static SceneStates GetState() {
        return state;
    }

    @Override
    public boolean onSceneTouchEvent(final TouchEvent pSceneTouchEvent) {

        if (Preloaded) {
            switch (state) {
            case Splash:
                mSceneLoader.onSceneTouchEvent(pSceneTouchEvent);
                break;

            case MainMenu:
                mSceneMainMenu.onSceneTouchEvent(pSceneTouchEvent);
                break;

            case LevelSelector:
                mSceneLevelSelector.onSceneTouchEvent(pSceneTouchEvent);
                break;
                
            case Equipment:
                mSceneEquipment.onSceneTouchEvent(pSceneTouchEvent);
                break;

            case About:
                mSceneAbout.onSceneTouchEvent(pSceneTouchEvent);
                break;

            case Exit:
                mSceneExit.onSceneTouchEvent(pSceneTouchEvent);
                break;

            case GameLoading:
                mSceneGameLoading.onSceneTouchEvent(pSceneTouchEvent);
                break;

            case Game:
                mSceneGame.onSceneTouchEvent(pSceneTouchEvent);
                break;

            default:
                break;

            }
        }
        return super.onSceneTouchEvent(pSceneTouchEvent);
    }

    public void onSceneBackPress() {

        switch (state) {
        case Splash:

            break;

        case MainMenu:
            SetState(SceneStates.Exit);
            break;

        case GameLoading:

            break;

        case Game:
            SetState(SceneStates.LevelSelector);
            break;

        case LevelSelector:
            SetState(SceneStates.MainMenu);
            break;
            
        case Equipment:
            SetState(SceneStates.LevelSelector);
            break;

        case About:
            SetState(SceneStates.MainMenu);
            break;

        case Exit:
            SetState(SceneStates.MainMenu);
            break;

        default:
            break;
        }
    }
    */

}
