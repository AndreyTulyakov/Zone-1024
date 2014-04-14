/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.R;
import com.mhyhre.zone_1024.game.logic.DemonBot;
import com.mhyhre.zone_1024.game.logic.GameControllable;
import com.mhyhre.zone_1024.game.logic.GameManager;

public class GameScene extends SimpleScene {

    private Background background;
    private Text textEntityScores;
    private Text textDemonHunger;
    private GameControllable gameManager;
    private GameField gameField;
    private final String strScore;
    private final String strDemonHunger;

    public GameScene(GameControllable gameManager) {

        this.gameManager = gameManager;
        
        strScore = MainActivity.Me.getString(R.string.scores);
        strDemonHunger = MainActivity.Me.getString(R.string.demon_hunger);
        
        background = new Background(0.0f, 0.0f, 0.0f);
        setBackground(background);
        setBackgroundEnabled(true);

        gameField = new GameField(gameManager.getGrid().getSize());
        attachChild(gameField);

        // Text
        final IFont font = MainActivity.resources.getFont("WhiteMono32");

        textEntityScores = new Text(0, 0, font, "", 32, MainActivity.Me.getVertexBufferObjectManager());
        textEntityScores.setPosition(MainActivity.getHalfWidth(), MainActivity.getHeight() - 40);
        attachChild(textEntityScores);
        
        textDemonHunger = new Text(0, 0, font, "", 32, MainActivity.Me.getVertexBufferObjectManager());
        textDemonHunger.setPosition(MainActivity.getHalfWidth(), 40);
        attachChild(textDemonHunger);
    }


    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        textEntityScores.setText(strScore + " " + GameManager.getInstance().getScore());
        gameField.update(gameManager.getGrid());
        
        DemonBot demon = gameManager.getGrid().getDemon();
        
        textDemonHunger.setText(strDemonHunger + " " + demon.getHunger());
        
        super.onManagedUpdate(pSecondsElapsed);
    }

}