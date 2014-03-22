package com.mhyhre.zone_1024.scenes;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;
import org.andengine.util.adt.color.Color;


import android.util.Log;
import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.PreferenceManager;
import com.mhyhre.zone_1024.R;
import com.mhyhre.zone_1024.scenes.RootScene.GameStates;


public class LoaderScene extends SimpleScene {

    private static final Color blue = new Color(0.51f, 0.549f, 1.0f);
    private static final Color red = new Color(1.0f, 0.549f, 0.51f);
    Rectangle bigStartButton;
    Rectangle bigExitButton; 
    
    public LoaderScene() {
        
        setBackground(new Background(0.0f, 0.0f, 0.0f));
        setBackgroundEnabled(true);
        
        IFont font = MainActivity.resources.getFont("White Furore");
        IFont fontBlack = MainActivity.resources.getFont("Furore");
        

        addTitle(font);
        addStartButton(fontBlack);
        addExitButton(fontBlack);
        addVibroButton();
        addSoundButton();
    }

    private void addTitle(IFont font) {
        String strTextTitle = MainActivity.Me.getString(R.string.app_name);
        Text textTitle = new Text(0, 0, font, strTextTitle, MainActivity.getVboManager());
        textTitle.setPosition(MainActivity.getHalfWidth() * 1.5f, MainActivity.getHalfHeight());
        attachChild(textTitle);
        
    }


    
    private void addStartButton(IFont font) {
        bigStartButton = new Rectangle(MainActivity.getWidth()/4, 30, 200, 60, MainActivity.getVboManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    MainActivity.vibrate(30);
                    RootScene.Me.setState(GameStates.NEW_GAME);
                }
                return true;
            }
        };
        bigStartButton.setColor(blue);
        attachChild(bigStartButton);
        registerTouchArea(bigStartButton);
        bigStartButton.setAlpha(0);

        String strTextStart = MainActivity.Me.getString(R.string.start);
        Text textStart = new Text(0, 0, font, strTextStart, MainActivity.getVboManager());
        textStart.setPosition(bigStartButton);
        attachChild(textStart);
    }
    
    private void addExitButton(IFont font) {
        bigExitButton = new Rectangle((MainActivity.getWidth()/4)*3, 30, 200, 60, MainActivity.getVboManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    MainActivity.vibrate(30);
                    RootScene.Me.setState(GameStates.TERMINATE);
                }
                return true;
            }
        };
        bigExitButton.setColor(red);
        attachChild(bigExitButton);
        registerTouchArea(bigExitButton);
        bigExitButton.setAlpha(0);

        String strTextStart = MainActivity.Me.getString(R.string.exit);
        Text textStart = new Text(0, 0, font, strTextStart, MainActivity.getVboManager());
        textStart.setPosition(bigExitButton);
        attachChild(textStart);
    }
    
    private void addSoundButton() {
        
        Sprite mSpriteSound = new Sprite(0, 0, MainActivity.resources.getTextureRegion("ButtonSound"), MainActivity.Me.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {

                    if (PreferenceManager.isSoundEnabled() == true) {
                        this.setColor(red);
                        PreferenceManager.setSoundEnabled(false);
                    } else {
                        this.setColor(blue);
                        PreferenceManager.setSoundEnabled(true);
                        MainActivity.resources.playSound("roboClick");
                    }
                    MainActivity.vibrate(40);
                }
                return true;
            }
        };
        mSpriteSound.setPosition((MainActivity.getWidth()/8)*5, MainActivity.getHeight() - 40);
        attachChild(mSpriteSound);
        registerTouchArea(mSpriteSound);

        if (PreferenceManager.isSoundEnabled()) {
            mSpriteSound.setColor(blue);
        } else {
            mSpriteSound.setColor(red);
        }
    }

    private void addVibroButton() {
        Sprite mSpriteVibro = new Sprite(0, 0, MainActivity.resources.getTextureRegion("ButtonVibration"), MainActivity.Me.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {

                    if (PreferenceManager.isVibroEnabled() == true) {
                        this.setColor(red);
                        PreferenceManager.setVibroEnabled(false);
                    } else {
                        this.setColor(blue);
                        PreferenceManager.setVibroEnabled(true);
                        MainActivity.vibrate(40);
                    }
                    MainActivity.vibrate(40);
                }
                return true;
            }
        };
        mSpriteVibro.setPosition((MainActivity.getWidth()/8)*7, MainActivity.getHeight() - 40);
        attachChild(mSpriteVibro);
        registerTouchArea(mSpriteVibro);

        if (PreferenceManager.isVibroEnabled()) {
            mSpriteVibro.setColor(blue);
        } else {
            mSpriteVibro.setColor(red);
        }
    }
    
    @Override
    public void show() {
        AlphaModifier alphaMode = new AlphaModifier(2, 0.0f, 1.0f);
        alphaMode.setAutoUnregisterWhenFinished(true);
        
        bigStartButton.registerEntityModifier(alphaMode);
        bigExitButton.registerEntityModifier(alphaMode);
        

        super.show();
    }
}
