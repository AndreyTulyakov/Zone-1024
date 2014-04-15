/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.graphics.Color;
import android.util.Log;


public class ResourceManager {

    private Map<String, ITextureRegion> regions;
    private Map<String, TiledTextureRegion> tiledRegions;
    private Map<String, BitmapTextureAtlas> atlases;
    private Map<String, Font> fonts;
    private Map<String, Sound> sounds;

    public ResourceManager() {
        regions = new HashMap<String, ITextureRegion>();
        tiledRegions = new HashMap<String, TiledTextureRegion>();
        atlases = new HashMap<String, BitmapTextureAtlas>();
        fonts = new HashMap<String, Font>();
        sounds = new HashMap<String, Sound>();
    }

    public ITextureRegion getTextureRegion(String key) {
        if (!regions.containsKey(key))
            Log.e(MainActivity.DEBUG_ID, "ResourceManager::getTextureRegion: invalid key - " + key);
        return regions.get(key);
    }

    public TiledTextureRegion getTiledTextureRegion(String key) {
        if (!tiledRegions.containsKey(key))
            Log.e(MainActivity.DEBUG_ID, "ResourceManager::getTiledTextureRegion: invalid key - " + key);
        return tiledRegions.get(key);
    }

    public BitmapTextureAtlas getTextureAtlas(String key) {
        if (!atlases.containsKey(key))
            Log.e(MainActivity.DEBUG_ID, "ResourceManager::getTextureAtlas: invalid key - " + key);
        return atlases.get(key);
    }

    public Font getFont(String key) {
        if (!fonts.containsKey(key))
            Log.e(MainActivity.DEBUG_ID, "ResourceManager::getFont: invalid key - " + key);
        return fonts.get(key);
    }

    public Sound getSound(String key) {
        if (!sounds.containsKey(key))
            Log.e(MainActivity.DEBUG_ID, "ResourceManager::getSound: invalid key - " + key);
        return sounds.get(key);
    }


    public void playSound(String key) {
        if (!sounds.containsKey(key))
            Log.e(MainActivity.DEBUG_ID, "ResourceManager::playSound: invalid key - " + key);
        if (PreferenceManager.isSoundEnabled()) {
            sounds.get(key).play();
        }
    }

    public void loadAtlases() {

        BitmapTextureAtlas atlas;

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        
        // Load ui graphics
        atlas = new BitmapTextureAtlas(MainActivity.Me.getTextureManager(), 512, 128, TextureOptions.BILINEAR);
        BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, MainActivity.Me, "User_Interface.png", 0, 0);
        atlas.load();
        atlases.put("User_Interface", atlas);

        regions.put("Yes", TextureRegionFactory.extractFromTexture(atlas, 0, 0, 80, 80, false));
        regions.put("No", TextureRegionFactory.extractFromTexture(atlas, 80, 0, 80, 80, false));
        
        regions.put("QuestionIcon", TextureRegionFactory.extractFromTexture(atlas, 320, 0, 80, 80, false));        
        regions.put("ScoresIcon", TextureRegionFactory.extractFromTexture(atlas, 400, 0, 80, 80, false));        
        
        regions.put("ButtonVibration", TextureRegionFactory.extractFromTexture(atlas, 160, 0, 80, 80, false)); 
        regions.put("ButtonSound", TextureRegionFactory.extractFromTexture(atlas, 240, 0, 80, 80, false)); 
        
        
        // Load cells
        atlas = new BitmapTextureAtlas(MainActivity.Me.getTextureManager(), 512, 256, TextureOptions.BILINEAR);
        BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, MainActivity.Me, "cells.png", 0, 0);
        atlas.load();
        
        atlases.put("Cells", atlas);
        
        regions.put("Cell", TextureRegionFactory.extractFromTexture(atlas, 256, 0, 128, 128, false));
        
        regions.put("DemonSleep0", TextureRegionFactory.extractFromTexture(atlas, 0, 0, 128, 128, false));
        regions.put("DemonSleep1", TextureRegionFactory.extractFromTexture(atlas, 128, 0, 128, 128, false));
        
        regions.put("DemonMoving", TextureRegionFactory.extractFromTexture(atlas, 384, 0, 128, 128, false));
        
        regions.put("DemonLeft", TextureRegionFactory.extractFromTexture(atlas, 0, 128, 128, 128, false));                
        regions.put("DemonUp", TextureRegionFactory.extractFromTexture(atlas, 128, 128, 128, 128, false)); 
        regions.put("DemonRight", TextureRegionFactory.extractFromTexture(atlas, 256, 128, 128, 128, false)); 
        regions.put("DemonDown", TextureRegionFactory.extractFromTexture(atlas, 384, 128, 128, 128, false)); 
        
        Log.i(MainActivity.DEBUG_ID, "ResourceManager::loadAtlases: OK");
    }

    public void loadSounds() {
        SoundFactory.setAssetBasePath("sound/");
        
        addSound("WinSound.ogg", "Win");
        addSound("GameOver.ogg", "GameOver");        
        addSound("DemonEat.ogg", "DemonEat");
    }
    

    private void addSound(String filename, String name) {
        Sound snd = null;
        try {
            snd = SoundFactory.createSoundFromAsset(MainActivity.Me.getSoundManager(), MainActivity.Me.getApplicationContext(), filename);
        } catch (IOException e) {
            Log.e(MainActivity.DEBUG_ID, "ResourceManager::addSound: " + e.getMessage());
            e.printStackTrace();
        }
        sounds.put(name, snd);
    }

    public void loadFonts() {

        FontFactory.setAssetBasePath("font/");
        Font mFont;
        
        final ITexture TextureFontPixelWhite48 = new BitmapTextureAtlas(MainActivity.Me.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        mFont = FontFactory.createFromAsset(MainActivity.Me.getFontManager(), TextureFontPixelWhite48, MainActivity.Me.getAssets(), "Hardpixel.ttf", 56, true,
                Color.WHITE);
        mFont.load();
        fonts.put("WhiteMono48", mFont);

        final ITexture TextureFontPixelWhite32 = new BitmapTextureAtlas(MainActivity.Me.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        mFont = FontFactory.createFromAsset(MainActivity.Me.getFontManager(), TextureFontPixelWhite32, MainActivity.Me.getAssets(), "Hardpixel.ttf", 32, true,
                Color.WHITE);
        mFont.load();
        fonts.put("WhiteMono32", mFont);

        final ITexture TextureFontPixelWhite24 = new BitmapTextureAtlas(MainActivity.Me.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        mFont = FontFactory.createFromAsset(MainActivity.Me.getFontManager(), TextureFontPixelWhite24, MainActivity.Me.getAssets(), "Hardpixel.ttf", 24, true,
                Color.WHITE);
        mFont.load();
        fonts.put("WhiteMono24", mFont);
        
        final ITexture TextureFontPixelWhite16 = new BitmapTextureAtlas(MainActivity.Me.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        mFont = FontFactory.createFromAsset(MainActivity.Me.getFontManager(), TextureFontPixelWhite16, MainActivity.Me.getAssets(), "Hardpixel.ttf", 20, true,
                Color.WHITE);
        mFont.load();
        fonts.put("WhiteMono16", mFont);

        Log.i(MainActivity.DEBUG_ID, "ResourceManager::loadFonts: OK");
    }

    public void LoadResourcesForPreloader() {

    }

}
