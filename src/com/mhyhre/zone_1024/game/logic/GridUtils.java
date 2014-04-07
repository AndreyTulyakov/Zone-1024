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
import com.mhyhre.zone_1024.utils.Direction;
import com.mhyhre.zone_1024.utils.Size;
import com.mhyhre.zone_1024.utils.StreamUtils;

public final class GridUtils {
    
    public static final String GRID_STORE_FILENAME = "grs.bin";

    public static Pair<List<Integer>,List<Integer>> getTraversalList(Direction dir, int lenght) {

        List<Integer> xValues = new LinkedList<Integer>();
        List<Integer> yValues = new LinkedList<Integer>();

        for (int i = 0; i < lenght; i++) {
            xValues.add(i);
            yValues.add(i);
        }
        
        if(dir.getVectorX() > 0) {
            Collections.reverse(xValues);
        }
        if(dir.getVectorY() > 0) {
            Collections.reverse(yValues);
        }
        
        return new Pair<List<Integer>, List<Integer>>(xValues, yValues);
    }
    
    public static Grid loadFromFile() {
        
        Grid grid = new Grid(new Size(4, 4));
        Size size = grid.getSize();
        
        FileInputStream inputStream = null;   
        DataInputStream dataStream = null;
        
        try {
            inputStream = MainActivity.Me.openFileInput(GRID_STORE_FILENAME);
            dataStream = new DataInputStream(inputStream);

            grid.setStep(dataStream.readInt());
            
            for(int y = 0; y < size.getHeight(); y++) {
                for(int x = 0; x < size.getWidth(); x++) {
                    
                    int value = dataStream.readInt();
                    if(value == 0) {
                        grid.setTile(x, y, null);
                    } else {
                        grid.setTile(x, y, new SimpleTile(x, y, value));
                    }
                }
            }
            
            grid.setJokerBeenStep(dataStream.readInt());
            grid.setDemonBeenStep(dataStream.readInt());
            
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

            dataStream.writeInt(grid.getStep());
            
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
            dataStream.writeInt(grid.getJokerBeenStep());
            dataStream.writeInt(grid.getDemonBeenStep());
            
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
