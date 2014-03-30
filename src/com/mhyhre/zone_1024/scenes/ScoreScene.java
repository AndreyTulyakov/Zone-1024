package com.mhyhre.zone_1024.scenes;

import java.util.ArrayList;
import java.util.Map;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.util.adt.align.HorizontalAlign;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.R;
import com.mhyhre.zone_1024.game.ScoresTable;

public class ScoreScene extends SimpleScene {
    
    ScoresTable scoresTable;
    ArrayList<Text> texts;
    
    public ScoreScene() {
        setBackground(new Background(0.0f, 0.1f, 0.0f));
        setBackgroundEnabled(true);
        
        this.setPosition(MainActivity.getHalfWidth(), MainActivity.getHalfHeight());
        
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
    }
    
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        hideAllTexts();

        Map<String, Integer> scores = scoresTable.getScores();

        
        float recordHeight = 50;
        float offset = -recordHeight * (scores.keySet().size() / 2.0f);
 
        
        int counter = 1;
        for(String name: scores.keySet()) {
            Text text = texts.get(counter);
            
            text.setText(name + " - " + scores.get(name));
            text.setPosition(text.getX(), offset + recordHeight * counter);
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
