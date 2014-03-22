package com.mhyhre.zone_1024.scenes;

import com.mhyhre.zone_1024.R;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.scenes.RootScene.GameStates;

public class QuestionScene extends SimpleScene {

    private final String strStop = MainActivity.Me.getString(R.string.q_stop_game);
    private final String strRestart = MainActivity.Me.getString(R.string.q_restart_game);
    private Text textQuestion;
    
    private Sprite spriteButtonNo;
    private Sprite spriteButtonYes;
    
    public QuestionScene() {
        
        setBackground(new Background(0.0f, 0.0f, 0.0f));
        setBackgroundEnabled(true);

        IFont font = MainActivity.resources.getFont("White Furore");
        
        textQuestion = new Text(0, 0, font, "",64, MainActivity.getVboManager());
        textQuestion.setPosition(MainActivity.getHalfWidth(), MainActivity.getHalfHeight());
        attachChild(textQuestion);
        
        // Buttons yes/no
        spriteButtonYes = new Sprite(0, 0, MainActivity.resources.getTextureRegion("Yes"), MainActivity.Me.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {

                    MainActivity.vibrate(30);
                    
                    switch(RootScene.getState()) {
                    case RESTART_Q:
                        RootScene.Me.setState(GameStates.NEW_GAME);
                        break;
                        
                    case STOP_Q:
                        RootScene.Me.setState(GameStates.LOADER);
                        break;
                    default:
                        break;
                    }
                }
                return true;
            }
        };
        spriteButtonYes.setPosition(MainActivity.getWidth()/4, MainActivity.getHeight()/4);
        spriteButtonYes.setAlpha(0);
        
        // Buttons yes no
        spriteButtonNo = new Sprite(0, 0, MainActivity.resources.getTextureRegion("No"), MainActivity.Me.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {

                    MainActivity.vibrate(30);
                    
                    switch(RootScene.getState()) {
                    case RESTART_Q:
                        RootScene.Me.setState(GameStates.LOADER);
                        break;
                        
                    case STOP_Q:
                        RootScene.Me.setState(GameStates.GAME_PROCESS);
                        break;
                    default:
                        break;
                    }
                }
                return true;
            }
        };
        spriteButtonNo.setPosition((MainActivity.getWidth()/4)*3, MainActivity.getHeight()/4);
        spriteButtonNo.setAlpha(0);
        attachChild(spriteButtonYes);
        attachChild(spriteButtonNo);
        
        registerTouchArea(spriteButtonYes);
        registerTouchArea(spriteButtonNo);
    }
    
    @Override
    public void show() {
        AlphaModifier alphaMode = new AlphaModifier(1, 0.0f, 1.0f);
        alphaMode.setAutoUnregisterWhenFinished(true);
        spriteButtonNo.registerEntityModifier(alphaMode);
        spriteButtonYes.registerEntityModifier(alphaMode);
        super.show();
    }
    
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        switch(RootScene.getState()) {
        
        case RESTART_Q:
            textQuestion.setText(strRestart);
            break;
            
        case STOP_Q:
            textQuestion.setText(strStop);
            break;
            
        default:
            break;
        }
        
        super.onManagedUpdate(pSecondsElapsed);
    }
}
