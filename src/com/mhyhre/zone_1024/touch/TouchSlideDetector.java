/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.touch;

import org.andengine.input.touch.TouchEvent;

import com.mhyhre.zone_1024.utils.Directions;

public class TouchSlideDetector {
    
    private final float MINIMAL_OFFSET = 90;
    private final float VECTOR_FACTOR = 1.5f;
    
    private TouchMotionsHunter hunter;
    
    boolean isTouched;
    TouchPoint touchStart;
    
    public TouchSlideDetector(TouchMotionsHunter hunter) {
        this.hunter = hunter;

    }
    
    public void onTouchEvent(final TouchEvent touchEvent) {
        
        // Using only 1 touch
        if (touchEvent.getPointerID() != 0)
            return;
           
        if(isTouched) {
            
            if(touchEvent.isActionCancel()|| touchEvent.isActionUp() || touchEvent.isActionOutside()) {
                isTouched = false;
                
                Directions direction = calculateMovement(
                        touchStart, new TouchPoint(touchEvent.getX(), touchEvent.getY()), MINIMAL_OFFSET, VECTOR_FACTOR);
                
                if(direction != Directions.NONE) {
                    hunter.onDetectedMotionEvent(direction);
                }
            }
            
            if(touchEvent.isActionMove()) {
                Directions direction = calculateMovement(
                        touchStart, new TouchPoint(touchEvent.getX(), touchEvent.getY()), MINIMAL_OFFSET, VECTOR_FACTOR);
                if(direction != Directions.NONE) {
                    isTouched = false;
                    hunter.onDetectedMotionEvent(direction);
                }
            }
            
        } else { 
            if(touchEvent.isActionDown()) {
                isTouched = true;
                touchStart = new TouchPoint(touchEvent.getX(), touchEvent.getY());
            }
        }
    }

    
    private Directions calculateMovement(TouchPoint firstPoint, TouchPoint lastPoint, float minimalOffset, float vectorFactor) {
        
        TouchPoint resultVector = new TouchPoint(
                lastPoint.getX() - firstPoint.getX(), lastPoint.getY() - firstPoint.getY());
        
        if(resultVector.getX() >= minimalOffset) {
            if(Math.abs(resultVector.getX()) > Math.abs(resultVector.getY()*vectorFactor)) {
                return Directions.RIGHT;
            }
        }
        
        if(resultVector.getX() <= -minimalOffset) {
            if(Math.abs(resultVector.getX()) > Math.abs(resultVector.getY()*vectorFactor)) {
                return Directions.LEFT;
            }
        }
        
        if(resultVector.getY() >= minimalOffset) {
            if(Math.abs(resultVector.getY()) > Math.abs(resultVector.getX()*vectorFactor)) {
                return Directions.UP;
            }
        }
        
        if(resultVector.getY() <= -minimalOffset) {
            if(Math.abs(resultVector.getY()) > Math.abs(resultVector.getX()*vectorFactor)) {
                return Directions.DOWN;
            }
        }
            
        return Directions.NONE;
    }
    
}
