package com.mhyhre.zone_1024.scenes;

import java.util.ArrayList;
import java.util.List;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;
import org.andengine.util.adt.align.HorizontalAlign;

import android.util.Pair;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.R;
import com.mhyhre.zone_1024.game.ScoresTable;

public class ScoreScene extends SimpleScene {
    
    private static final int PRESS_TIMEOUT = 5;
    
    ScoresTable scoresTable;
    ArrayList<Text> texts;
    private static double startPressTime = 0;
    private static double currentTime = 0;
    
    public ScoreScene() {
        setBackground(new Background(0.0f, 0.1f, 0.0f));
        setBackgroundEnabled(true);
        
        this.setPosition(MainActivity.getHalfWidth(), MainActivity.getHalfHeight());
        
        currentTime = 0;
        
        scoresTable = ScoresTable.getInstance();
        
        IFont font = MainActivity.resources.getFont("WhiteMonoScores");
        
        // Configure text labels
        texts = new ArrayList<Text>(ScoresTable.MAXIMAL_COUNT_OF_RECORDS);
        for(int i = 0; i < ScoresTable.MAXIMAL_COUNT_OF_RECORDS + 1; i++) {
            Text text = new Text(0, 0, font, "", 64, MainActivity.getVboManager());
            text.setHorizontalAlign(HorizontalAlign.LEFT);
            texts.add(text);
            attachChild(text);
        }
        
        CharSequence strScoresTable = MainActivity.Me.getText(R.string.scores_table);
        texts.get(0).setText(strScoresTable);
        
        IFont font16 = MainActivity.resources.getFont("WhiteMono16");
        addClearButton(font16);
    }
    
    
    private void addClearButton(IFont font) {
        
        Rectangle clearButton = new Rectangle(0, 0, MainActivity.getWidth()*0.2f, MainActivity.getHalfHeight() * 0.6f, MainActivity.getVboManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    MainActivity.vibrate(30);
                    startPressTime = currentTime;
                    return true;
                }
                
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    MainActivity.vibrate(30);
                    double dtime = Math.abs(startPressTime - currentTime);
                    if( dtime > PRESS_TIMEOUT-2 && dtime < PRESS_TIMEOUT+2) {
                        scoresTable.clearRecords();
                        startPressTime = -1;
                        MainActivity.vibrate(100);
                    } else {
                        
                    }
                    return true;
                }
                
                
                
                return true;
            }
        };
        clearButton.setPosition(0, - MainActivity.getHalfHeight()*0.60f);
        clearButton.setVisible(false);
        attachChild(clearButton);
        registerTouchArea(clearButton);

        
        String strTextPressToClear = MainActivity.Me.getString(R.string.clear_scores);
        Text textPressToStart = new Text(0, 0, font, strTextPressToClear, MainActivity.getVboManager());
        textPressToStart.setPosition(clearButton);
        textPressToStart.setHorizontalAlign(HorizontalAlign.CENTER);
        attachChild(textPressToStart);
    }
    
    
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        currentTime += pSecondsElapsed;
        
        hideAllTexts();

        List<Pair<Integer, String>> scores = scoresTable.getScores();

        
        float recordHeight = 50;
        float offset = -recordHeight * (scores.size() / 2.0f);
 
        
        int counter = 1;
        for(Pair<Integer, String> value: scores) {
            Text text = texts.get(counter);
            
            text.setText(value.second + " - " + value.first);
            text.setPosition(text.getX(), offset + recordHeight * (scores.size() - counter));
            text.setVisible(true);
            
            counter++;
            
            if(counter > texts.size()) {
                break;
            }
        }
        
        Text caption = texts.get(0);
        caption.setPosition(0, offset + recordHeight * counter);
        caption.setVisible(true);
        
        super.onManagedUpdate(pSecondsElapsed);
    }
    
    private void hideAllTexts() {
        for(Text text: texts) {
            text.setVisible(false);
        }
    }
}
