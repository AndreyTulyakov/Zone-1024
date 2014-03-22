package com.mhyhre.zone_1024.scenes;

import java.util.ArrayList;
import java.util.Map;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.game.ScoresTable;

public class ScoreScene extends SimpleScene {
    
    ScoresTable scoresTable;
    ArrayList<Text> texts;
    
    public ScoreScene() {
        setBackground(new Background(0, 0, 0));
        setBackgroundEnabled(true);
        
        scoresTable = ScoresTable.getInstance();
        
        IFont font = MainActivity.resources.getFont("White Furore");
        
        // Configure text labels
        texts = new ArrayList<Text>(ScoresTable.MAXIMAL_COUNT_OF_RECORDS);
        for(int i = 0; i < ScoresTable.MAXIMAL_COUNT_OF_RECORDS; i++) {
            Text text = new Text(0, 0, font, "", MainActivity.getVboManager());
            texts.add(text);
        }
    }
    
    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        hideAllTexts();

        Map<String, Integer> scores = scoresTable.getScores();
        IFont font = MainActivity.resources.getFont("White Furore");
        
        float recordHeight = font.getLineHeight()* 3;
        float tableHeight = recordHeight * scores.size();
        float startHeight = MainActivity.getHalfHeight() - tableHeight/2;
        
        int counter = 0;
        for(String name: scores.keySet()) {
            Text text = texts.get(counter);
            text.setText(name + " - " + scores.get(name));
            text.setPosition(MainActivity.getHalfWidth(), startHeight + counter*recordHeight);
            text.setVisible(true);
            counter++;
        }
        
        super.onManagedUpdate(pSecondsElapsed);
    }
    
    private void hideAllTexts() {
        for(Text text: texts) {
            text.setVisible(false);
        }
    }
}
