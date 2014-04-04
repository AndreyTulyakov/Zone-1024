package com.mhyhre.zone_1024.utils;

import java.io.Closeable;
import java.io.IOException;

public class StreamUtils {

    public static void silentClose(Closeable target) {
        
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
