package com.mhyhre.zone_1024.scenes;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.PreferenceManager;
import com.mhyhre.zone_1024.R;
import com.mhyhre.zone_1024.scenes.RootScene.GameStates;
import com.mhyhre.zone_1024.utils.LoaderBackground;


public class LoaderScene extends SimpleScene{

    private Text textTitle;
    private Text textPressToStart;
    private float pressAlphaSum;

    private LoaderBackground loaderBackground;
    
    public LoaderScene() {
        
        setBackground(new Background(0.0f, 0.0f, 0.0f));
        setBackgroundEnabled(true);
        
        
        IFont font32 = MainActivity.resources.getFont("WhiteMono48");
        IFont font16 = MainActivity.resources.getFont("WhiteMono16");
        
        loaderBackground = new LoaderBackground(MainActivity.getHalfWidth(), 0);
        attachChild(loaderBackground);

        addStartButton(font16);
        addExitButton();
        addScoresButton();
        addVibroButton();
        addSoundButton();
        addAboutButton();
        
        addTitle(font32);
    }

    private void addTitle(IFont font) {
        String strTextTitle = MainActivity.Me.getString(R.string.app_name);
        textTitle = new Text(0, 0, font, strTextTitle, MainActivity.getVboManager());
        textTitle.setPosition(MainActivity.getHalfWidth(), MainActivity.getHalfHeight());
        attachChild(textTitle);
    }


    
    private void addStartButton(IFont font) {
        
        Rectangle bigStartButton = new Rectangle(0, 0, MainActivity.getWidth()-160, MainActivity.getHalfHeight()*1.2f, MainActivity.getVboManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    MainActivity.vibrate(30);
                    
                    if(MainActivity.getPreferenceManager().isGameWasNotEnded()) {
                        RootScene.Me.setState(GameStates.CONTINUE_GAME_Q);
                    } else {
                        RootScene.Me.setState(GameStates.NEW_GAME);
                    }
                }
                return true;
            }
        };
        bigStartButton.setPosition(MainActivity.getHalfWidth(), MainActivity.getHalfHeight());
        bigStartButton.setVisible(false);
        attachChild(bigStartButton);
        registerTouchArea(bigStartButton);

        
        String strTextPressToStart = MainActivity.Me.getString(R.string.press_to_start);
        textPressToStart = new Text(0, 0, font, strTextPressToStart, MainActivity.getVboManager());
        textPressToStart.setPosition(MainActivity.getHalfWidth(), MainActivity.getHeight()/4);
        attachChild(textPressToStart);
    }
    
    private void addExitButton() {
        
        Sprite bigExitButton = new Sprite((MainActivity.getWidth()/8)*7,  MainActivity.getHeight() - 60,
                MainActivity.resources.getTextureRegion("No"), MainActivity.getVboManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    MainActivity.vibrate(30);
                    RootScene.Me.setState(GameStates.TERMINATE);
                }
                return true;
            }
        };
        attachChild(bigExitButton);
        registerTouchArea(bigExitButton);

    }
    
    
    private void addScoresButton() {
        
        Sprite scoresButton = new Sprite(MainActivity.getHalfWidth(),  MainActivity.getHeight() - 60,
                MainActivity.resources.getTextureRegion("ScoresIcon"), MainActivity.getVboManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    MainActivity.vibrate(30);
                    RootScene.Me.setState(GameStates.SCORES_VIEW);
                }
                return true;
            }
        };
        attachChild(scoresButton);
        registerTouchArea(scoresButton);

    }
    
    
    private void addSoundButton() {
        
        Sprite mSpriteSound = new Sprite(0, 0, MainActivity.resources.getTextureRegion("ButtonSound"), MainActivity.Me.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {

                    if (PreferenceManager.isSoundEnabled() == true) {
                        this.setAlpha(0.5f);
                        PreferenceManager.setSoundEnabled(false);
                    } else {
                        this.setAlpha(1.0f);
                        PreferenceManager.setSoundEnabled(true);
                        MainActivity.resources.playSound("DemonEat");
                    }
                    MainActivity.vibrate(40);
                }
                return true;
            }
        };
        mSpriteSound.setPosition((MainActivity.getWidth()/8), 60);
        attachChild(mSpriteSound);
        registerTouchArea(mSpriteSound);

        if (PreferenceManager.isSoundEnabled()) {
            mSpriteSound.setAlpha(1.0f);
        } else {
            mSpriteSound.setAlpha(0.5f);
        }
    }

    private void addVibroButton() {
        Sprite mSpriteVibro = new Sprite(0, 0, MainActivity.resources.getTextureRegion("ButtonVibration"), MainActivity.Me.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {

                    if (PreferenceManager.isVibroEnabled() == true) {
                        this.setAlpha(0.5f);
                        PreferenceManager.setVibroEnabled(false);
                    } else {
                        this.setAlpha(1.0f);
                        PreferenceManager.setVibroEnabled(true);
                        MainActivity.vibrate(40);
                    }
                    MainActivity.vibrate(40);
                }
                return true;
            }
            
        };
        mSpriteVibro.setPosition(((MainActivity.getWidth()/8)*7), 60);
        attachChild(mSpriteVibro);
        registerTouchArea(mSpriteVibro);
  
        if (PreferenceManager.isVibroEnabled()) {
            mSpriteVibro.setAlpha(1.0f);
        } else {
            mSpriteVibro.setAlpha(0.5f);
        }
    }
   
    
    private void addAboutButton() {
        
        Sprite aboutButton = new Sprite((MainActivity.getWidth()/8),  MainActivity.getHeight() - 60,
                MainActivity.resources.getTextureRegion("QuestionIcon"), MainActivity.getVboManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    MainActivity.vibrate(30);
                    RootScene.Me.setState(GameStates.ABOUT);
                }
                return true;
            }
        };
        attachChild(aboutButton);
        registerTouchArea(aboutButton);
        
    }
    
    @Override
    public void show() {
        
        AlphaModifier alphaMode = new AlphaModifier(2f, 0.0f, 1.0f);
        alphaMode.setAutoUnregisterWhenFinished(true);
        textTitle.setAlpha(0);
        textTitle.registerEntityModifier(alphaMode);
        
        // For blink synchronization
        pressAlphaSum = 0;
        
        super.show();
    }
    
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        
        pressAlphaSum += 1.5f * pSecondsElapsed;
        if(pressAlphaSum > Math.PI) {
            pressAlphaSum = 0;
        }
        textPressToStart.setAlpha((float)Math.sin(pressAlphaSum));
  
        super.onManagedUpdate(pSecondsElapsed);
    }
}
