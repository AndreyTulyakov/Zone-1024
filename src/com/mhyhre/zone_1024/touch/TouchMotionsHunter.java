/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.touch;

import com.mhyhre.zone_1024.utils.Direction;

public interface TouchMotionsHunter {

    public void onDetectedMotionEvent(Direction moveDirection);
    
}
