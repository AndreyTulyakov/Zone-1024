package com.mhyhre.zone_1024.scenes;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;
import org.andengine.util.adt.color.Color;

import android.util.Log;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.scenes.RootScene.GameStates;

public class LoaderScene extends SimpleScene {

    public LoaderScene() {
        
        setBackground(new Background(0.0f, 0.0f, 0.0f));
        setBackgroundEnabled(true);
        
        // FIXME: re move it to R. string resources
        // Restart label
        String strTextTitle = "ZONE-1024";
        String strTextStart = "start";
        
        IFont font = MainActivity.resources.getFont("White Furore");
        IFont fontBlack = MainActivity.resources.getFont("Furore");
        
        Text textTitle = new Text(0, 0, font, strTextTitle, MainActivity.getVboManager());
        textTitle.setPosition(MainActivity.getHalfWidth() * 1.5f, MainActivity.getHalfHeight());
        attachChild(textTitle);
        
        Rectangle bigStartButton = new Rectangle(120, 20, 200, 40, MainActivity.getVboManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
                    Log.i(MainActivity.DEBUG_ID, "LoaderScene::Start - clicked");
                    MainActivity.vibrate(30);
                    RootScene.Me.setState(GameStates.NEW_GAME);
                }
                return true;
            }
        };
        bigStartButton.setColor(Color.WHITE);
        attachChild(bigStartButton);
        registerTouchArea(bigStartButton);
        
        Text textStart = new Text(0, 0, fontBlack, strTextStart, MainActivity.getVboManager());
        textStart.setPosition(bigStartButton);
        attachChild(textStart);

    }
}
