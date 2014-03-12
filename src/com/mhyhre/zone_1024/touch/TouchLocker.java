/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.touch;

import com.mhyhre.zone_1024.MainActivity;

import android.util.Log;

public class TouchLocker {

    final private float lockPause;
    private boolean locked;
    
    private boolean isFirstUpdate;
    private float secondElapsed;

    public TouchLocker(float lockPause) {
        locked = false;
        this.lockPause = lockPause;
    }

    public void lock() {
        isFirstUpdate = true;
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    public void update(final float inSecondElapsed) {
        
        if(isLocked() == false) {
            return;
        }
        
        if(isFirstUpdate) {
            secondElapsed = 0;
            isFirstUpdate = false;
        }
        
        if (secondElapsed > lockPause) {
            unlock();
            Log.i(MainActivity.DEBUG_ID, "First:" + secondElapsed + " Pause:" + lockPause);
        } else {
            secondElapsed += inSecondElapsed;
        }
    }

    public boolean isLocked() {
        return locked;
    }
}
