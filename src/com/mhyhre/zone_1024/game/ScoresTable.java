package com.mhyhre.zone_1024.game;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.text.format.Time;
import android.util.Log;

import com.mhyhre.zone_1024.MainActivity;
import com.mhyhre.zone_1024.utils.TimeAndDate;

public class ScoresTable {
    
    private static ScoresTable instance = null;
    private static final char FILE_TYPE_ID = 'S';    
    private static final String SCORES_FILENAME = "scores.txt";
    public static final int MAXIMAL_COUNT_OF_RECORDS = 8;
    
    private Map<TimeAndDate, Integer> scores;
    
    public static ScoresTable getInstance() {
        if(instance == null) {
            instance = new ScoresTable();
        }
        return instance;
    }
    
    private ScoresTable() {
        
        scores = new HashMap<TimeAndDate, Integer>();
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
            
            for(int i = 0; i < countOfRecords; i++) {
                
                int s = dataStream.readInt();
                int m = dataStream.readInt();
                int h = dataStream.readInt();
                
                int d = dataStream.readInt();
                int mo = dataStream.readInt();
                int y = dataStream.readInt();
                
                TimeAndDate timeDate = new TimeAndDate(s,m,h,d,mo,y);
                int score = dataStream.readInt();
                
                scores.put(timeDate, score);
            }
            
            
          } catch (FileNotFoundException e) {  
              // Do nothing
          } catch (IOException e) {
              clearRecords();
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
            
            for (TimeAndDate timeDate: scores.keySet()) {

                dataStream.writeInt(timeDate.getSeconds());
                dataStream.writeInt(timeDate.getMinutes());
                dataStream.writeInt(timeDate.getHours());
                
                dataStream.writeInt(timeDate.getDay());
                dataStream.writeInt(timeDate.getMonth());
                dataStream.writeInt(timeDate.getYear());
                
                dataStream.writeInt(scores.get(dataStream));
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
    
    public void addTestRecord() {
        addRecord(1024);
    }
    
    public void clearRecords() {
        scores.clear();
    }
    
    
    public void addRecord(int score) {
        
        if(isNeedAdd(score) == false){
            return;
        }
        
        Calendar cal = Calendar.getInstance(); 
        TimeAndDate timeDate = new TimeAndDate(
                cal.get(Calendar.SECOND), cal.get(Calendar.MINUTE), cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
        
        scores.put(timeDate, score);
    }

    
    public Map<TimeAndDate, Integer> getScores() {
        return scores;
    }
    
    public boolean isNeedAdd(int currentScore) {
        
        if(scores.keySet().size() < MAXIMAL_COUNT_OF_RECORDS) {
            return true;
        } else {
            for(TimeAndDate key: scores.keySet()) {
                if(scores.get(key) < currentScore) {
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
