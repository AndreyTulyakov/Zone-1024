package com.mhyhre.zone_1024.game;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.mhyhre.zone_1024.MainActivity;

public class ScoresTable {
    
    private static ScoresTable instance = null;
    private static final char FILE_TYPE_ID = 'S';    
    private static final String SCORES_FILENAME = "scores.txt";
    public static final int MAXIMAL_COUNT_OF_RECORDS = 8;
    public static final int MAXIMAL_NAME_LENGTH = 10;
    
    private Map<String, Integer> scores;
    
    public static ScoresTable getInstance() {
        if(instance == null) {
            instance = new ScoresTable();
        }
        return instance;
    }
    
    private ScoresTable() {
        
        scores = new HashMap<String, Integer>();
        readScores();
    }
    
    private void readScores() {
        DataInputStream dataStream = null;
        
        try {
            FileInputStream inputStream = MainActivity.Me.openFileInput(SCORES_FILENAME);
            dataStream = new DataInputStream(inputStream);
            
            int id = dataStream.readChar();
            if((char)id != FILE_TYPE_ID) {
                throw new IllegalStateException("Incorrect file id!");
            }
            
            int countOfRecords = dataStream.readInt();
            if(countOfRecords < 0 || countOfRecords >= MAXIMAL_COUNT_OF_RECORDS) {
                throw new IllegalStateException("Incorrect records count!");
            }
            
            byte[] name = new byte[MAXIMAL_NAME_LENGTH];
            
            for(int i = 0; i < countOfRecords; i++) {
                dataStream.read(name);
                int score = dataStream.readInt();
                
                scores.put(new String(name), score);
            }
            
            
          } catch (FileNotFoundException e) {  
              // Do nothing.
          } catch (IOException e) {
              // Do nothing.
          } catch (IllegalStateException e) {
              Log.i(MainActivity.DEBUG_ID, "ScoresTable: " + e.getMessage());
          } finally {
              silentClose(dataStream);
          }
        
    }
    
    
    public void saveScores() {
        DataOutputStream dataStream = null;
        
        try {
            FileOutputStream outputStream = MainActivity.Me.openFileOutput(SCORES_FILENAME, MainActivity.MODE_PRIVATE);
            dataStream = new DataOutputStream(outputStream);
            
            dataStream.writeChar(FILE_TYPE_ID);
            dataStream.writeInt(scores.size());
            
            for (String name : scores.keySet()) {
                if(name.length() != MAXIMAL_NAME_LENGTH) {
                    throw new IllegalStateException("Name with strange length!");
                }
                
                dataStream.writeChars(name);
                dataStream.writeInt(scores.get(name));
            }
            
        } catch (FileNotFoundException e) {  
            // Do nothing.
        } catch (IOException e) {
            // Do nothing.
        } catch (IllegalStateException e) {
            Log.i(MainActivity.DEBUG_ID, "ScoresTable::saveScores:" + e.getMessage());
        } finally {
            silentClose(dataStream);
        }
    }
    
    
    public void addRecord(String name, int score) {
        
        if(isNeedAdd(score) == false){
            return;
        }
        
        if(name.length() > MAXIMAL_NAME_LENGTH) {
            name = name.substring(0, MAXIMAL_NAME_LENGTH);
        }
        
        if(name.length() < MAXIMAL_NAME_LENGTH) {
            char[] bytes = new char[MAXIMAL_NAME_LENGTH-name.length()];
            Arrays.fill(bytes, '.');
            String str = new String(bytes);
            name.concat(str);
        }
        
        scores.put(name, score);
    }

    
    public Map<String, Integer> getScores() {
        return scores;
    }
    
    public boolean isNeedAdd(int currentScore) {
        
        if(scores.keySet().size() < MAXIMAL_COUNT_OF_RECORDS) {
            return true;
        } else {
            for(String name: scores.keySet()) {
                if(scores.get(name) < currentScore) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void silentClose(Closeable target) {
        if(target == null) {
            return;
        }
        
        try {
            target.close();
        } catch (IOException e) {
            // Do nothing
        }
    }
    
}
