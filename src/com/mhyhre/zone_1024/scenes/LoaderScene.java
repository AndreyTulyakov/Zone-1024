package com.mhyhre.zone_1024.scenes;

import java.util.LinkedList;
import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;
import org.andengine.util.adt.color.Color;

import android.hardware.SensorEvent;
import android.util.Log;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.PreferenceManager;
import com.mhyhre.zone_1024.R;
import com.mhyhre.zone_1024.scenes.RootScene.GameStates;
import com.mhyhre.zone_1024.utils.AccelerometrListener;


public class LoaderScene extends SimpleScene implements AccelerometrListener {

    private static final float WAKE_UP_TIME = 2;
    private static final float ACCELEROMETR_SENSITIVITY = 1.0f;
    /*
    private static final Color blue = new Color(0.51f, 0.549f, 1.0f);
    private static final Color red = new Color(1.0f, 0.549f, 0.51f);
    private static final Color green = new Color(0.549f, 1.0f, 0.51f);
    */
    
    private List<Entity> slowShowList;
    private Text textTitle;
    private Text textPressToStart;
    private float pressAlphaSum;
    private float wakeUpCounter;
    private float elemetsAlpha;
    
    public LoaderScene() {
        
        slowShowList = new LinkedList<Entity>();
        
        setBackground(new Background(0.0f, 0.0f, 0.0f));
        setBackgroundEnabled(true);
        
        IFont font32 = MainActivity.resources.getFont("WhiteMono32");
        IFont font16 = MainActivity.resources.getFont("WhiteMono16");
        


        addStartButton(font16);
        addExitButton();
        //addVibroButton();
        //addSoundButton();
        addAboutButton();
        
        addTitle(font32);
    }

    private void addTitle(IFont font) {
        String strTextTitle = MainActivity.Me.getString(R.string.app_name);
        textTitle = new Text(0, 0, font, strTextTitle, MainActivity.getVboManager());
        textTitle.setPosition(MainActivity.getHalfWidth() * 1.5f, MainActivity.getHalfHeight());
        attachChild(textTitle);
    }


    
    private void addStartButton(IFont font) {
        
        Rectangle bigStartButton = new Rectangle(0, 0, MainActivity.getWidth()-160, MainActivity.getHalfHeight(), MainActivity.getVboManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    MainActivity.vibrate(30);
                    RootScene.Me.setState(GameStates.NEW_GAME);
                }
                return true;
            }
        };
        bigStartButton.setPosition(MainActivity.getHalfWidth(), MainActivity.getHeight()/4);
        bigStartButton.setVisible(false);
        attachChild(bigStartButton);
        registerTouchArea(bigStartButton);

        
        String strTextPressToStart = MainActivity.Me.getString(R.string.press_to_start);
        textPressToStart = new Text(0, 0, font, strTextPressToStart, MainActivity.getVboManager());
        textPressToStart.setPosition(bigStartButton);
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

        slowShowList.add(bigExitButton);
    }
    /*
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
                        MainActivity.resources.playSound("roboClick");
                    }
                    MainActivity.vibrate(40);
                }
                return true;
            }
        };
        mSpriteSound.setPosition((MainActivity.getWidth()/8)*3, MainActivity.getHeight() - 40);
        attachChild(mSpriteSound);
        registerTouchArea(mSpriteSound);
        slowShowList.add(mSpriteSound);
        
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
        mSpriteVibro.setPosition((MainActivity.getWidth()/8)*5, MainActivity.getHeight() - 40);
        attachChild(mSpriteVibro);
        registerTouchArea(mSpriteVibro);
        slowShowList.add(mSpriteVibro);
        
        if (PreferenceManager.isVibroEnabled()) {
            mSpriteVibro.setAlpha(1.0f);
        } else {
            mSpriteVibro.setAlpha(0.5f);
        }
    }
    */
    
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
        slowShowList.add(aboutButton);
    }
    
    @Override
    public void show() {
        wakeUp();
        AlphaModifier alphaMode = new AlphaModifier(1.5f, 0.0f, 1.0f);
        alphaMode.setAutoUnregisterWhenFinished(true);
        textTitle.setAlpha(0);
        textTitle.registerEntityModifier(alphaMode);
        super.show();
    }

    private static float lastAccelerometrSum = 0;
    @Override
    public void onAccelerometerEvent(SensorEvent event) {
        float[] values = event.values;
        
        float sum = (Math.abs(values[0]) +  Math.abs(values[1]) + Math.abs(values[2]));
        Log.i(MainActivity.DEBUG_ID, "Accelerometr sum:" + sum);
        if(Math.abs(sum-lastAccelerometrSum) > ACCELEROMETR_SENSITIVITY) {
            wakeUp();
        }
        lastAccelerometrSum = sum;
    }
    
    public void wakeUp() {
        wakeUpCounter = WAKE_UP_TIME;
        elemetsAlpha = 1.0f;
        for(Entity entity: slowShowList) {
            entity.setAlpha(elemetsAlpha);
        }

    }
    
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        pressAlphaSum += 1.5f * pSecondsElapsed;
        if(pressAlphaSum > Math.PI) {
            pressAlphaSum = 0;
        }
        textPressToStart.setAlpha((float)Math.sin(pressAlphaSum));
        
        if(wakeUpCounter <= 0) {
            wakeUpCounter = 0;
            
            if(elemetsAlpha > 0.0f) {
                for(Entity entity: slowShowList) {
                    entity.setAlpha(elemetsAlpha);
                }
                elemetsAlpha -= 0.005f;
            } else {
                elemetsAlpha = 0;
            }
        } else {
            wakeUpCounter -= pSecondsElapsed;
        }
            
        super.onManagedUpdate(pSecondsElapsed);
    }
}
