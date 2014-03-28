package com.mhyhre.zone_1024.scenes;

import java.util.ArrayList;
import java.util.Map;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.R;
import com.mhyhre.zone_1024.game.ScoresTable;
import com.mhyhre.zone_1024.utils.TimeAndDate;

public class ScoreScene extends SimpleScene {
    
    ScoresTable scoresTable;
    ArrayList<Text> texts;
    
    public ScoreScene() {
        setBackgroundEnabled(false);
        
        scoresTable = ScoresTable.getInstance();
        
        IFont font = MainActivity.resources.getFont("WhiteMono24");
        
        // Configure text labels
        texts = new ArrayList<Text>(ScoresTable.MAXIMAL_COUNT_OF_RECORDS);
        for(int i = 0; i < ScoresTable.MAXIMAL_COUNT_OF_RECORDS + 1; i++) {
            Text text = new Text(0, 0, font, "", 64, MainActivity.getVboManager());
            texts.add(text);
            attachChild(text);
        }
        
        CharSequence strScoresTable = MainActivity.Me.getText(R.string.scores_table);
        texts.get(0).setText(strScoresTable);
    }
    
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        hideAllTexts();

        Map<TimeAndDate, Integer> scores = scoresTable.getScores();

        
        float recordHeight = 40;
        float offset = -recordHeight * (scores.keySet().size() / 2.0f);
 
        
        int counter = 1;
        for(TimeAndDate date: scores.keySet()) {
            Text text = texts.get(counter);
            
            text.setText(date + " - " + scores.get(date));
            text.setPosition(0, offset + recordHeight * counter);
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
