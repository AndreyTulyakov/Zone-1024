/*
 * Copyright (C) 2013-2014 Andrey Tulyakov
 * @mail: mhyhre@gmail.com
 */

package com.mhyhre.zone_1024.touch;

import org.andengine.input.touch.TouchEvent;

import com.mhyhre.zone_1024.utils.Direction;
import com.mhyhre.zone_1024.utils.MoveEventListener;

public class TouchSlidingEventDetector {
    
    private final float MINIMAL_OFFSET = 80;
    private final float VECTOR_FACTOR = 1.3f;
    
    private MoveEventListener listener;
    
    private boolean isTouched;
    private TouchPoint touchStart;
    
    public TouchSlidingEventDetector(MoveEventListener listener) {
        this.listener = listener;
    }
    
    public void onTouchEvent(final TouchEvent touchEvent) {
        
        // Using only 1 touch
        if (touchEvent.getPointerID() != 0)
            return;
           
        if(isTouched) {
            
            if(touchEvent.isActionCancel()|| touchEvent.isActionUp() || touchEvent.isActionOutside()) {
                isTouched = false;
                
                Direction direction = calculateMovement(
                        touchStart, new TouchPoint(touchEvent.getX(), touchEvent.getY()), MINIMAL_OFFSET, VECTOR_FACTOR);
                
                if(direction != null) {
                    listener.onMoveEvent(direction);
                }
            }
            
            if(touchEvent.isActionMove()) {
                Direction direction = calculateMovement(
                        touchStart, new TouchPoint(touchEvent.getX(), touchEvent.getY()), MINIMAL_OFFSET, VECTOR_FACTOR);
                if(direction != null) {
                    isTouched = false;
                    listener.onMoveEvent(direction);
                }
            }
            
        } else { 
            if(touchEvent.isActionDown()) {
                isTouched = true;
                touchStart = new TouchPoint(touchEvent.getX(), touchEvent.getY());
            }
        }
    }

    
    private Direction calculateMovement(TouchPoint firstPoint, TouchPoint lastPoint, float minimalOffset, float vectorFactor) {
        
        TouchPoint resultVector = new TouchPoint(
                lastPoint.getX() - firstPoint.getX(), lastPoint.getY() - firstPoint.getY());
        
        if(resultVector.getX() >= minimalOffset) {
            if(Math.abs(resultVector.getX()) > Math.abs(resultVector.getY()*vectorFactor)) {
                return Direction.RIGHT;
            }
        }
        
        if(resultVector.getX() <= -minimalOffset) {
            if(Math.abs(resultVector.getX()) > Math.abs(resultVector.getY()*vectorFactor)) {
                return Direction.LEFT;
            }
        }
        
        if(resultVector.getY() >= minimalOffset) {
            if(Math.abs(resultVector.getY()) > Math.abs(resultVector.getX()*vectorFactor)) {
                return Direction.UP;
            }
        }
        
        if(resultVector.getY() <= -minimalOffset) {
            if(Math.abs(resultVector.getY()) > Math.abs(resultVector.getX()*vectorFactor)) {
                return Direction.DOWN;
            }
        }
            
        return null;
    }
    
}
