package com.mhyhre.zone_1024.game.logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.util.Pair;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.game.logic.demon.DemonBot;
import com.mhyhre.zone_1024.game.logic.demon.Intention;
import com.mhyhre.zone_1024.utils.Direction;
import com.mhyhre.zone_1024.utils.Size;
import com.mhyhre.zone_1024.utils.StreamUtils;

public final class GridUtils {
    
    public static final String GRID_STORE_FILENAME = "grs.bin";
    public static final char FILE_TYPE_ID = 'H';

    public static Pair<List<Integer>,List<Integer>> getTraversalList(Direction dir, int lenght) {

        List<Integer> xValues = new LinkedList<Integer>();
        List<Integer> yValues = new LinkedList<Integer>();

        for (int i = 0; i < lenght; i++) {
            xValues.add(i);
            yValues.add(i);
        }
        
        if(dir.getVector().getX() > 0) {
            Collections.reverse(xValues);
        }
        if(dir.getVector().getY() > 0) {
            Collections.reverse(yValues);
        }
        
        return new Pair<List<Integer>, List<Integer>>(xValues, yValues);
    }
    
    public static Grid loadFromFile() {
        
        Grid grid = new Grid(new Size(4, 4), false);
        Size size = grid.getSize();
        
        FileInputStream inputStream = null;   
        DataInputStream dataStream = null;
        
        try {
            inputStream = MainActivity.Me.openFileInput(GRID_STORE_FILENAME);
            dataStream = new DataInputStream(inputStream);
            
            char typeId = dataStream.readChar();
            
            if(typeId != FILE_TYPE_ID) {
                return null;
            }
            

            DemonBot demon = null;

            for(int y = 0; y < size.getHeight(); y++) {
                for(int x = 0; x < size.getWidth(); x++) {
                    
                    int value = dataStream.readInt();
                    
                    if(value == 0) {
                        grid.setTile(x, y, null);
                    }
                    
                    if(value > 0) {
                        grid.setTile(x, y, new SimpleTile(x, y, value));
                    }
                    
                    if(value == Grid.DEMON_VALUE) {
                        demon = new DemonBot(x, y, Grid.DEMON_VALUE, grid);
                        grid.setDemon(demon);
                        grid.setTile(x, y, demon);
                    }
                }
            }
            
            // Load demon data
            demon.setHunger(dataStream.readInt());
            demon.setBehaviorIntention(Intention.values()[dataStream.readInt()]);
            demon.setIntentionDirection(Direction.values()[dataStream.readInt()]);          
            
          } catch (FileNotFoundException e) {  
              return null;
          } catch (IOException e) {
              return null;
          } catch (IllegalStateException e) {
              return null;
          } finally {
              StreamUtils.silentClose(dataStream);
          }
        

        return grid;
    }
    
    public static void saveToFile(Grid grid) {
        Size size = grid.getSize();
        
        FileOutputStream outputStream = null;
        DataOutputStream dataStream = null;
        
        try {
            outputStream = MainActivity.Me.openFileOutput(GRID_STORE_FILENAME, MainActivity.MODE_PRIVATE);
            dataStream = new DataOutputStream(outputStream);

            dataStream.writeChar(FILE_TYPE_ID);
            
            for(int y = 0; y < size.getHeight(); y++) {
                for(int x = 0; x < size.getWidth(); x++) {
                    SimpleTile tile = grid.getTile(x, y);
                    if(tile == null) {
                        dataStream.writeInt(0);
                    } else {
                        dataStream.writeInt(tile.value);
                    }
                }
            }
            
            // Save demon data
            DemonBot demon = grid.getDemon();
            dataStream.writeInt(demon.getHunger());
            dataStream.writeInt(demon.getBehaviorIntention().ordinal());
            dataStream.writeInt(demon.getIntentionDirection().ordinal());
            
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            return;
        } catch (IllegalStateException e) {
            return;
        } finally {
            StreamUtils.silentClose(dataStream);
        }
    }
}
