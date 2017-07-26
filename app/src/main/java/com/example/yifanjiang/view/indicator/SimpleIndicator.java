package com.example.yifanjiang.view.indicator;

import android.graphics.PointF;

/**
 * Created by yifan.jiang on 7/14/2017.
 */

public class SimpleIndicator {

    private PointF mPtLastMove = new PointF();
    private float mLastPos = 0;
    private float mCurrentPosY = 0;

    public float getOffsetX() {
        return mOffsetX;
    }

    public float getOffsetY() {
        return mOffsetY;
    }

    private float mOffsetX;
    private float mOffsetY;


    public void pressDown(float x, float y){
        mPtLastMove.set(x,y);
        mCurrentPosY = y;
    }

    public void onRelease(){

    }

    public void onMove(float x,float y){
        mOffsetX = x - mPtLastMove.x ;
        mOffsetY = y - mPtLastMove.y ;
        mPtLastMove.set(x,y);
        mCurrentPosY = y;
    }


    public float getLastPos() {
        return mLastPos;
    }

    public float getCurrentPosY() {
        return mCurrentPosY;
    }

    public void setCurrentPos(float currentPos) {
        mLastPos = mCurrentPosY;
        mCurrentPosY = currentPos;
    }
}
