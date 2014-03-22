package com.mhyhre.zone_1024.scenes;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.IFont;
import org.andengine.util.adt.color.Color;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.R;
import com.mhyhre.zone_1024.game.ScoresTable;
import com.mhyhre.zone_1024.scenes.RootScene.GameStates;

public class LoaderScene extends SimpleScene implements TextWatcher {

    private EditText mEditText;
    private String strPersonName = "unnamed";
    private Text textPersonName;
    
    public LoaderScene() {
        
        setBackground(new Background(0.0f, 0.0f, 0.0f));
        setBackgroundEnabled(true);
        
        this.mEditText = (EditText)MainActivity.Me.findViewById(R.id.textbreakexample_text);
        this.mEditText.addTextChangedListener(this);
        
        String strTextTitle = "ZONE-1024";
        String strTextStart = "start";
        
        IFont font = MainActivity.resources.getFont("White Furore");
        IFont fontBlack = MainActivity.resources.getFont("Furore");
        
        Text textTitle = new Text(0, 0, font, strTextTitle, MainActivity.getVboManager());
        textTitle.setPosition(MainActivity.getHalfWidth() * 1.5f, MainActivity.getHalfHeight());
        attachChild(textTitle);
        
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
        
        Text textStart = new Text(0, 0, fontBlack, strTextStart, MainActivity.getVboManager());
        textStart.setPosition(bigStartButton);
        attachChild(textStart);

        textPersonName = new Text(0, 0, font, strPersonName, 16, MainActivity.getVboManager());
        textPersonName.setPosition(MainActivity.getHalfWidth(), MainActivity.getHalfHeight() - 40);
        attachChild(textPersonName);
    }

    @Override
    public void afterTextChanged(Editable s) {
        String name = s.toString();
        
        if(name.length() == 0) {
            return;
        }
        
        if(name.length() > ScoresTable.MAXIMAL_NAME_LENGTH) {
            name = name.substring(0, ScoresTable.MAXIMAL_NAME_LENGTH);
        }
        
        strPersonName = name;
        textPersonName.setText(strPersonName);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub
        
    }
}
