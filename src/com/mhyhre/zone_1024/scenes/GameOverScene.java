package com.mhyhre.zone_1024.scenes;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;
import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.R;
import com.mhyhre.zone_1024.scenes.RootScene.GameStates;

public class GameOverScene extends SimpleScene {

    private Rectangle backgroundRect;
    private final float inputDelay = 3;
    private float timeSum;
    private boolean lockInput;
    
    public GameOverScene() {
        setBackgroundEnabled(false);
        
        IFont font = MainActivity.resources.getFont("WhiteMono32");
        
        // Generate background rectangle
        backgroundRect = new Rectangle(MainActivity.getHalfWidth(), MainActivity.getHalfHeight(), 
                MainActivity.getWidth(), MainActivity.getWidth(), MainActivity.getVboManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    MainActivity.vibrate(30);
                    RootScene.Me.setState(GameStates.RESTART_Q);
                }
                return true;
            }
        };
        backgroundRect.setColor(0.2f, 0.0f, 0.0f, 0.5f);
        attachChild(backgroundRect);
        registerTouchArea(backgroundRect);

        addTitle(font);
    }
    
    
    private void addTitle(IFont font) {
        String strTextTitle = MainActivity.Me.getString(R.string.game_over);
        Text textTitle = new Text(0, 0, font, strTextTitle, MainActivity.getVboManager());
        textTitle.setPosition(MainActivity.getHalfWidth(), MainActivity.getHalfHeight());
        attachChild(textTitle);
    }
    
    @Override
    public void show() {
        backgroundRect.setAlpha(0);

        AlphaModifier alphaMod = new AlphaModifier(4, 0, 0.75f);
        alphaMod.setAutoUnregisterWhenFinished(true);
        backgroundRect.registerEntityModifier(alphaMod);

        timeSum = 0;
        lockInput = true;
        super.show();
    }
    
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        
        if(lockInput) {
            timeSum += pSecondsElapsed;
            if(timeSum > inputDelay) {
                lockInput = false;
            }
        }
        
        
        super.onManagedUpdate(pSecondsElapsed);
    }
    
    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        if(lockInput == false) {
            return super.onSceneTouchEvent(pSceneTouchEvent);
        }
        return true;
    }
}
