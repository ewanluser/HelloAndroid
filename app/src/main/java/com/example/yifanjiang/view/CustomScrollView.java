package com.example.yifanjiang.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.example.yifanjiang.view.indicator.SimpleIndicator;

/**
 * Created by yifan.jiang on 7/14/2017.
 */

public class CustomScrollView extends ViewGroup {

    private SimpleIndicator mIndicator ;
    private Scroller mScroller;

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        @SuppressWarnings({"unused"})
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    private  int childCount =0 ;

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        childCount = getChildCount();
        mIndicator = new SimpleIndicator();
        mScroller = new Scroller(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for(int i =0; i<childCount;i++){
            measureChildWithMargins(getChildAt(i),widthMeasureSpec,0,heightMeasureSpec,0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = getPaddingTop();
        int bottom = 0;
        for(int i =0;i<childCount;i++){
            View child = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams)  child.getLayoutParams();
            //设置上下左右
            int left = getPaddingLeft() + params.leftMargin;
            int right = left + child.getMeasuredWidth();
            top += params.topMargin;
            bottom = top + child.getMeasuredHeight();
            child.layout(left,top,right,bottom);
            top  = bottom + params.bottomMargin;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mIndicator.pressDown(ev.getX(),ev.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mIndicator.onMove(ev.getX(),ev.getY());
//                float offsetY = mIndicator.getOffsetY();
//                scrollBy(0,(int)-offsetY);
                break;
            case MotionEvent.ACTION_UP:
                mIndicator.onRelease();
                fakeDrag();

      }
      return true;

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            invalidate();
        }
    }

    private void fakeDrag(){
        float distance = mIndicator.getCurrentPosY() -0;

        mScroller.startScroll(0,0,0,(int)-distance,1000);
        invalidate();

    }


}
