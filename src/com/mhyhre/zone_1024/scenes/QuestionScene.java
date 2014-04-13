package com.mhyhre.zone_1024.scenes;

import com.mhyhre.zone_1024.R;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.game.logic.GameManager;
import com.mhyhre.zone_1024.scenes.RootScene.GameStates;

public class QuestionScene extends SimpleScene {

    private final String strRestart = MainActivity.Me.getString(R.string.q_restart_game);
    private final String strContinueGame = MainActivity.Me.getString(R.string.q_continue_game);
    private Text textQuestion;
    
    public QuestionScene() {
        
        setBackground(new Background(0.0f, 0.0f, 0.0f));
        setBackgroundEnabled(true);

        IFont font = MainActivity.resources.getFont("WhiteMono32");
        
        textQuestion = new Text(0, 0, font, "",64, MainActivity.getVboManager());
        textQuestion.setPosition(MainActivity.getHalfWidth(), MainActivity.getHalfHeight());
        attachChild(textQuestion);
        
        addButtonYes();
        addButtonNo();
    }
    
    
    private void addButtonYes() {
        Sprite spriteButtonYes = new Sprite(0, 0, MainActivity.resources.getTextureRegion("Yes"), MainActivity.Me.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {

                    MainActivity.vibrate(30);
 
                    // Clicked YES!
                    
                    switch(RootScene.getState()) {
                    case RESTART_Q:
                        RootScene.Me.setState(GameStates.NEW_GAME);
                        break;
                       
                    case CONTINUE_GAME_Q:
                        GameManager.getInstance().loadGame();
                        RootScene.Me.setState(GameStates.GAME_PROCESS);
                        break;
                        
                    default:
                        break;
                    }
                }
                return true;
            }
        };
        spriteButtonYes.setPosition(MainActivity.getWidth()/4, MainActivity.getHeight()/4);
        attachChild(spriteButtonYes);
        registerTouchArea(spriteButtonYes);
    }
    
    
    private void addButtonNo() {
        Sprite spriteButtonNo = new Sprite(0, 0, MainActivity.resources.getTextureRegion("No"), MainActivity.Me.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {

                    MainActivity.vibrate(30);
                    
                    // Clicked NO!
                    
                    switch(RootScene.getState()) {
                    case RESTART_Q:
                        RootScene.Me.setState(GameStates.LOADER);
                        break;
                        
                    case CONTINUE_GAME_Q:
                        RootScene.Me.setState(GameStates.NEW_GAME);
                        break;
                        
                    default:
                        break;
                    }
                }
                return true;
            }
        };
        spriteButtonNo.setPosition((MainActivity.getWidth()/4)*3, MainActivity.getHeight()/4);
        attachChild(spriteButtonNo);
        registerTouchArea(spriteButtonNo);
    }
    
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        switch(RootScene.getState()) {
        
        case RESTART_Q:
            textQuestion.setText(strRestart);
            break;
            
        case CONTINUE_GAME_Q:
            textQuestion.setText(strContinueGame);
            break;
            
        default:
            break;
        }
        
        super.onManagedUpdate(pSecondsElapsed);
    }
}
