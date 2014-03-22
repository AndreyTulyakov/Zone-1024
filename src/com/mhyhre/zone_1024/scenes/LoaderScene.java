package com.mhyhre.zone_1024.scenes;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;
import org.andengine.util.adt.color.Color;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.R;
import com.mhyhre.zone_1024.game.ScoresTable;
import com.mhyhre.zone_1024.scenes.RootScene.GameStates;

public class LoaderScene extends SimpleScene {


    public LoaderScene() {
        
        setBackground(new Background(0.0f, 0.0f, 0.0f));
        setBackgroundEnabled(true);
        
        

        
        IFont font = MainActivity.resources.getFont("White Furore");
        IFont fontBlack = MainActivity.resources.getFont("Furore");
        

        addTitle(font);
        addStartButton(fontBlack);
    }
    
    
    
    private void addTitle(IFont font) {
        String strTextTitle = MainActivity.Me.getString(R.string.app_name);
        Text textTitle = new Text(0, 0, font, strTextTitle, MainActivity.getVboManager());
        textTitle.setPosition(MainActivity.getHalfWidth() * 1.5f, MainActivity.getHalfHeight());
        attachChild(textTitle);
    }



    private void addStartButton(IFont font) {
        Rectangle bigStartButton = new Rectangle(120, 30, 200, 60, MainActivity.getVboManager()) {
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
        
        String strTextStart = MainActivity.Me.getString(R.string.start);
        Text textStart = new Text(0, 0, font, strTextStart, MainActivity.getVboManager());
        textStart.setPosition(bigStartButton);
        attachChild(textStart);
    }

}
