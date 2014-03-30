package com.mhyhre.zone_1024.game;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.util.Pair;

import com.mhyhre.zone_1024.MainActivity;

public class ScoresTable {
    
    private static ScoresTable instance = null;  
    private static final String SCORES_FILENAME = "scores.txt";
    public static final int MAXIMAL_COUNT_OF_RECORDS = 8;
    public static final int MAXIMAL_NAME_LENGTH = 8;   
    
    private List<Pair<Integer, String>> scores;
    
    public static ScoresTable getInstance() {
        if(instance == null) {
            instance = new ScoresTable();
        }
        return instance;
    }
    
    private ScoresTable() {
        
        scores = new ArrayList<Pair<Integer, String>>(MAXIMAL_COUNT_OF_RECORDS);
        readScores();
    }
    
    private void readScores() {
        
        FileInputStream inputStream = null;   
        DataInputStream dataStream = null;
        
        try {
            inputStream = MainActivity.Me.openFileInput(SCORES_FILENAME);
            dataStream = new DataInputStream(inputStream);
            
            int countOfRecords = dataStream.readInt();
            if(countOfRecords < 0 || countOfRecords >= MAXIMAL_COUNT_OF_RECORDS) {
                throw new IllegalStateException("Incorrect records count!");
            }
                       
            char[] nameBuffer = new char[MAXIMAL_NAME_LENGTH];
            
            for(int i = 0; i < countOfRecords; i++) {

                for(int characterIndex = 0; characterIndex < MAXIMAL_NAME_LENGTH; characterIndex++){
                    nameBuffer[characterIndex] = dataStream.readChar();
                }
                
                String name = new String(nameBuffer);
                int score = dataStream.readInt();
                
                scores.add(new Pair<Integer, String>(score, name));
            }
            

            
          } catch (FileNotFoundException e) {  
              Log.i(MainActivity.DEBUG_ID, "ScoresTable::readScores:FileNotFoundException:" + e.getMessage());
          } catch (IOException e) {
              Log.i(MainActivity.DEBUG_ID, "ScoresTable::readScores:IOException:" + e.getMessage());
              clearRecords();
          } catch (IllegalStateException e) {
              Log.i(MainActivity.DEBUG_ID, "readScores:IllegalStateException: " + e.getMessage());
          } finally {
              silentClose(dataStream);
          }
    }
    
    
    public void saveScores() {
        
        FileOutputStream outputStream = null;
        DataOutputStream dataStream = null;
        
        try {
            outputStream = MainActivity.Me.openFileOutput(SCORES_FILENAME, MainActivity.MODE_PRIVATE);
            dataStream = new DataOutputStream(outputStream);

            dataStream.writeInt(scores.size());
            
            for (Pair<Integer, String> entry: scores) {

                dataStream.writeChars(entry.second);
                dataStream.writeInt(entry.first);
            }
            
        } catch (FileNotFoundException e) {
            Log.i(MainActivity.DEBUG_ID, "ScoresTable::saveScores:" + e.getMessage());
        } catch (IOException e) {
            Log.i(MainActivity.DEBUG_ID, "ScoresTable::saveScores:" + e.getMessage());
        } catch (IllegalStateException e) {
            Log.i(MainActivity.DEBUG_ID, "ScoresTable::saveScores:" + e.getMessage());
        } finally {
            silentClose(dataStream);
        }
    }
    
    public void addTestRecord() {
        addRecord("ANON", 1024);
    }
    
    public void clearRecords() {
        scores.clear();
    }
    
    private String prepareName(String name) {
        if(name.length() == 0) {
            name = "PLAYER";
        }
        
        if(name.length() > MAXIMAL_NAME_LENGTH) {
            name = name.substring(0, MAXIMAL_NAME_LENGTH);
        }
        
        if(name.length() < MAXIMAL_NAME_LENGTH) {
            name = String.format("%-" + MAXIMAL_NAME_LENGTH + "s", name);
        }
        
        name = name.replace(' ', '.');
        return name;
    }
    
    
    public void addRecord(String name, int score) {
        
        Log.i(MainActivity.DEBUG_ID, "Add record " + name + " - " + score + "...");
        
        if(isNeedAdd(score) == false){
            return;
        }
        
        name = prepareName(name);
        
        // If dublicate - return
        for(Pair<Integer, String> value: scores) {
            if(value.second.equals(name)) {
                if(value.first == score) {
                    return;
                }
            }
        }
        
        scores.add(new Pair<Integer, String>(score, name));
        
        Log.i(MainActivity.DEBUG_ID, "Record Added: " + name + " - " + score + "...");
        
    }

    
    public List<Pair<Integer, String>> getScores() {
        return scores;
    }
    
    public boolean isNeedAdd(int currentScore) {
        
        if(scores.size() < MAXIMAL_COUNT_OF_RECORDS) {
            return true;
        } else {
            for(Pair<Integer, String> value: scores) {
                if(value.first < currentScore) {
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
