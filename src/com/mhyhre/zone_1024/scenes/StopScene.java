package com.mhyhre.zone_1024.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.scenes.RootScene.GameStates;

public class StopScene extends SimpleScene {

    public StopScene() {
        
        setBackground(new Background(0.0f, 0.0f, 0.0f));
        setBackgroundEnabled(true);
        
        // FIXME: re move it to R. string resources
        // Restart label
        final String strStop = "Do you want stop game?";
        IFont font = MainActivity.resources.getFont("White Furore");
        Text textStop = new Text(0, 0, font, strStop, MainActivity.getVboManager());
        textStop.setPosition(MainActivity.getHalfWidth(), MainActivity.getHalfHeight());
        attachChild(textStop);
        
        // Buttons yes/no
        Sprite spriteButtonYes = new Sprite(0, 94, MainActivity.resources.getTextureRegion("Yes"), MainActivity.Me.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {

                    MainActivity.vibrate(30);
                    RootScene.Me.setState(GameStates.LOADER);
                }
                return true;
            }
        };
        spriteButtonYes.setPosition(MainActivity.getWidth()/4, MainActivity.getHeight()/4);

        
        // Buttons yes no
        Sprite spriteButtonNo = new Sprite(0, 94, MainActivity.resources.getTextureRegion("No"), MainActivity.Me.getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {

                    MainActivity.vibrate(30);
                    RootScene.Me.setState(GameStates.GAME_PROCESS);
                }
                return true;
            }
        };
        spriteButtonNo.setPosition((MainActivity.getWidth()/4)*3, MainActivity.getHeight()/4);
        
        attachChild(spriteButtonYes);
        attachChild(spriteButtonNo);
        
        registerTouchArea(spriteButtonYes);
        registerTouchArea(spriteButtonNo);

    }
}
