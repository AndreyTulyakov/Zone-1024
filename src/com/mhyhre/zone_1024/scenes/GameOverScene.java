package com.mhyhre.zone_1024.scenes;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;

import android.util.Log;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.R;
import com.mhyhre.zone_1024.game.ScoresTable;
import com.mhyhre.zone_1024.game.logic.GameManager;
import com.mhyhre.zone_1024.scenes.RootScene.GameStates;
import com.mhyhre.zone_1024.utils.TextInput;
import com.mhyhre.zone_1024.utils.TextInputListener;

public class GameOverScene extends SimpleScene implements TextInputListener {

    private Rectangle backgroundRect;
    private final float INPUT_DELAY = 1.5f;
    private float timeSum;
    private boolean lockInput;
    private Text textTitle;

    public GameOverScene() {
        setBackgroundEnabled(false);

        IFont font = MainActivity.resources.getFont("WhiteMono32");

        // Generate background rectangle
        backgroundRect = new Rectangle(MainActivity.getHalfWidth(), MainActivity.getHalfHeight(), MainActivity.getWidth(), MainActivity.getHeight(),
                MainActivity.getVboManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    Log.i(MainActivity.DEBUG_ID, "Win Scene: click on!");
                    MainActivity.vibrate(30);

                    
                    if (ScoresTable.getInstance().isNeedAdd(GameManager.getInstance().getScore())) {
                        addRecordScores();
                    } else {
                        RootScene.Me.setState(GameStates.SCORES_VIEW);
                    }
                }
                return true;
            }
        };
        
        attachChild(backgroundRect);
        registerTouchArea(backgroundRect);

        addTitle(font);
    }

    private void addRecordScores() {
        Log.i(MainActivity.DEBUG_ID, "WinScene: addRecordScores text input.");
        if(TextInput.isNowShowed() == false) {
            TextInput.setListener(this);
            TextInput.showTextInput("Enter you name!", "Maximum " + ScoresTable.MAXIMAL_NAME_LENGTH + " characters", MainActivity.Me);
        }
    }

    private void addTitle(IFont font) {
    	
        String strTextTitle = MainActivity.Me.getString(R.string.won);
        
        
        textTitle = new Text(0, 0, font, strTextTitle, 40, MainActivity.getVboManager());
        textTitle.setPosition(MainActivity.getHalfWidth(), MainActivity.getHalfHeight());
        attachChild(textTitle);
    }

    @Override
    public void show() {
    	
    	// If win
    	if(RootScene.getState() == GameStates.GAME_WIN_SCENE) {
    		
	    	backgroundRect.setColor(0.0f, 0.3f, 0.0f);
	        backgroundRect.setAlpha(0);
	        
	        String strTextTitle = MainActivity.Me.getString(R.string.won);
	        textTitle.setText(strTextTitle);
	        
	        MainActivity.resources.playSound("Win");
	
    	} else {
    		backgroundRect.setColor(0.2f, 0.0f, 0.0f);
    		backgroundRect.setAlpha(0);

	        String strTextTitle = MainActivity.Me.getString(R.string.game_over);
	        textTitle.setText(strTextTitle);
    		
            MainActivity.resources.playSound("GameOver");

    	}
    	
        AlphaModifier alphaMod = new AlphaModifier(2, 0, 0.75f);
        alphaMod.setAutoUnregisterWhenFinished(true);
        backgroundRect.registerEntityModifier(alphaMod);
    	
        timeSum = 0;
        lockInput = true;
        super.show();
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        if (lockInput) {
            timeSum += pSecondsElapsed;
            if (timeSum > INPUT_DELAY) {
                lockInput = false;
                Log.i(MainActivity.DEBUG_ID, "Win Scene: unlock input!");
            }
        }

        super.onManagedUpdate(pSecondsElapsed);
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
        if (lockInput == false) {
            return super.onSceneTouchEvent(pSceneTouchEvent);
        }
        return true;
    }

    @Override
    public void textChanged(String text) {
        
        if (TextInput.isOkPressed()) {
            ScoresTable.getInstance().addRecord(TextInput.getResultText(), GameManager.getInstance().getScore());
            ScoresTable.getInstance().saveScores();
            RootScene.Me.setState(GameStates.SCORES_VIEW);
        } else {
            RootScene.Me.setState(GameStates.LOADER);
        }

    }
}
