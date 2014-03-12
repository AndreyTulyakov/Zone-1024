/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.touch;

import org.andengine.input.touch.TouchEvent;

public class TouchSlideDetector {
    
    private final float MINIMAL_OFFSET = 100;
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
                
                TouchDirections direction = calculateMovement(
                        touchStart, new TouchPoint(touchEvent.getX(), touchEvent.getY()), MINIMAL_OFFSET, VECTOR_FACTOR);
                
                if(direction != TouchDirections.NONE) {
                    hunter.onDetectedMotionEvent(direction);
                }
            }
            
            if(touchEvent.isActionMove()) {
                TouchDirections direction = calculateMovement(
                        touchStart, new TouchPoint(touchEvent.getX(), touchEvent.getY()), MINIMAL_OFFSET, VECTOR_FACTOR);
                if(direction != TouchDirections.NONE) {
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

    
    private TouchDirections calculateMovement(TouchPoint firstPoint, TouchPoint lastPoint, float minimalOffset, float vectorFactor) {
        
        TouchPoint resultVector = new TouchPoint(
                lastPoint.getX() - firstPoint.getX(), lastPoint.getY() - firstPoint.getY());
        
        if(resultVector.getX() >= minimalOffset) {
            if(Math.abs(resultVector.getX()) > Math.abs(resultVector.getY()*vectorFactor)) {
                return TouchDirections.RIGHT;
            }
        }
        
        if(resultVector.getX() <= -minimalOffset) {
            if(Math.abs(resultVector.getX()) > Math.abs(resultVector.getY()*vectorFactor)) {
                return TouchDirections.LEFT;
            }
        }
        
        if(resultVector.getY() >= minimalOffset) {
            if(Math.abs(resultVector.getY()) > Math.abs(resultVector.getX()*vectorFactor)) {
                return TouchDirections.UP;
            }
        }
        
        if(resultVector.getY() <= -minimalOffset) {
            if(Math.abs(resultVector.getY()) > Math.abs(resultVector.getX()*vectorFactor)) {
                return TouchDirections.DOWN;
            }
        }
            
        return TouchDirections.NONE;
    }
    
}
