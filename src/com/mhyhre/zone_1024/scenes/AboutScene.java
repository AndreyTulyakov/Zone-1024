package com.mhyhre.zone_1024.scenes;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;

import com.mhyhre.zone_1024.MainActivity;

import android.util.Log;

public class AboutScene extends SimpleScene {
    
    private static final String ABOUT_FILENAME = "about.txt";
    private Text textInfo;

    public AboutScene() {
        setBackground(new Background(0.0f, 0.1f, 0.0f));
        setBackgroundEnabled(true);
        
        final String strAbout = loadInfo(ABOUT_FILENAME);
        
        textInfo = new Text(0, 0, MainActivity.resources.getFont("WhiteMono24"), strAbout, MainActivity.Me.getVertexBufferObjectManager());
        textInfo.setPosition(MainActivity.getHalfWidth(), MainActivity.getHalfHeight());
        attachChild(textInfo);
        
    }
  
    private String loadInfo(String filename) {

        String text = "";

        InputStream input;
        try {
            input = MainActivity.Me.getAssetManager().open(filename);

            int size = input.available();
            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            text = new String(buffer);

        } catch (IOException e) {
            Log.e(MainActivity.DEBUG_ID, "SceneAbout::loadInfo: " + e.getMessage());
            e.printStackTrace();
        }

        return text;
    }
}
