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

public class GameWinScene extends SimpleScene implements TextInputListener {

    private Rectangle backgroundRect;
    private final float INPUT_DELAY = 2;
    private float timeSum;
    private boolean lockInput;

    public GameWinScene() {
        setBackgroundEnabled(false);

        IFont font = MainActivity.resources.getFont("WhiteMono32");

        // Generate background rectangle
        backgroundRect = new Rectangle(MainActivity.getHalfWidth(), MainActivity.getHalfHeight(), MainActivity.getWidth(), MainActivity.getWidth(),
                MainActivity.getVboManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    Log.i(MainActivity.DEBUG_ID, "Win Scene: click on!");
                    MainActivity.vibrate(30);

                    if (ScoresTable.getInstance().isNeedAdd(GameManager.getInstance().getScore())) {
                        addRecordScores();
                    } else {
                        if(GameManager.getInstance().isGameFinished() == false) {
                            RootScene.Me.setState(GameStates.KEEP_PLAYING_Q);
                        } else {
                            RootScene.Me.setState(GameStates.SCORES_VIEW);
                        }
                    }
                }
                return true;
            }
        };
        backgroundRect.setColor(0.0f, 0.3f, 0.0f, 0.5f);
        attachChild(backgroundRect);
        registerTouchArea(backgroundRect);

        addTitle(font);
    }

    private void addRecordScores() {
        if(TextInput.isNowShowed() == false) {
            TextInput.setListener(this);
            TextInput.showTextInput("Enter you name!", "Maximum " + ScoresTable.MAXIMAL_NAME_LENGTH + " characters", MainActivity.Me);
        }
    }

    private void addTitle(IFont font) {
        String strTextTitle = MainActivity.Me.getString(R.string.won);
        Text textTitle = new Text(0, 0, font, strTextTitle, MainActivity.getVboManager());
        textTitle.setPosition(MainActivity.getHalfWidth(), MainActivity.getHalfHeight());
        attachChild(textTitle);
    }

    @Override
    public void show() {
        backgroundRect.setAlpha(0);

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
        }
        if(GameManager.getInstance().isGameFinished() == false) {
            RootScene.Me.setState(GameStates.KEEP_PLAYING_Q);
        } else {
            RootScene.Me.setState(GameStates.SCORES_VIEW);
        }
    }
}
