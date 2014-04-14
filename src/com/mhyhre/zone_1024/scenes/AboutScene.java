package com.mhyhre.zone_1024.scenes;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.R;

import android.util.Log;

public class AboutScene extends SimpleScene {
    
    private Text textInfo;

    public AboutScene() {
        setBackground(new Background(0.0f, 0.0f, 0.0f));
        setBackgroundEnabled(true);
        
        final String strAbout = MainActivity.Me.getString(R.string.about_text);
        
        textInfo = new Text(0, 0, MainActivity.resources.getFont("WhiteMono24"), strAbout, MainActivity.Me.getVertexBufferObjectManager());
        textInfo.setPosition(MainActivity.getHalfWidth(), MainActivity.getHalfHeight());
        attachChild(textInfo);
        
    }

}
