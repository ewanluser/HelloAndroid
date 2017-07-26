package com.example.yifanjiang.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yifanjiang.myapplication.R;

/**
 * Created by yifan.jiang on 6/22/2017.
 */
public class CouponLayout extends LinearLayout {
    private final String   TAG  = "CouponLayout";

    private boolean isSecleted = true;

    private int default_bg_color = Color.parseColor("#343641");
    private int default_icon_color = Color.parseColor("#6A6A6C");
    private int selected_bg_color ;
    private String tvContent ;

    private Context context;

    private ImageView iv;
    private TextView  tv;


    private final static float CORNER_RADIUS = 5.0f;

    private Bitmap maskBitmap;
    private Paint paint, maskPaint;
    private float cornerRadius;

    private Bitmap unselectedIcon;

    private int parentWidth;
    private int parentHeight;

    public CouponLayout(Context context){
        super(context);
        init(context, null, 0);

    }

    public CouponLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CouponLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }



    private void init(Context context, AttributeSet attrs, int defStyle) {

        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.coupon_layout, this, true);
        iv = (ImageView) findViewById(R.id.iv);
        tv = (TextView) findViewById(R.id.tv);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CouponLayout);
        selected_bg_color = ta.getColor(R.styleable.CouponLayout_bg_color,default_bg_color);
        tvContent = ta.getString(R.styleable.CouponLayout_mTextContent);
        tv.setText(tvContent);
        tv.setTextColor(ta.getColor(R.styleable.CouponLayout_mTextColor,Color.WHITE));
        tv.setTextSize(ta.getDimensionPixelSize(R.styleable.CouponLayout_mTextSize,10));
        createUnselectIcon();

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CORNER_RADIUS, metrics);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        setWillNotDraw(false);


    }


    @Override
    public void draw(Canvas canvas) {
        Bitmap offscreenBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas offscreenCanvas = new Canvas(offscreenBitmap);
        super.draw(offscreenCanvas);
        if (maskBitmap == null) {
            maskBitmap = createMask(canvas.getWidth(), canvas.getHeight());
        }
        offscreenCanvas.drawBitmap(maskBitmap, 0f, 0f, maskPaint);
        canvas.drawBitmap(offscreenBitmap, 0f, 0f, paint);
        drawBackgroud();
    }

    private void drawBackgroud(){
        if(isSecleted){
            Log.d(TAG,"draw selected");
            this.setBackgroundColor(selected_bg_color);
            iv.setImageBitmap(null);
            iv.setBackgroundResource(R.drawable.cat_checked);
        }
        else{
            Log.d(TAG,"draw not select");
            iv.setBackground(null);
            iv.setImageBitmap(unselectedIcon);
            this.setBackgroundColor(default_bg_color);
        }
    }


    private void createUnselectIcon(){
        if(unselectedIcon != null){
            return;
        }
        unselectedIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat_checked).copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(unselectedIcon);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(default_icon_color);
        canvas.drawColor(default_bg_color);
        paint.setStrokeWidth((float) 3.0);
        canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, Math.min(canvas.getWidth(),canvas.getHeight())/2, paint);
        paint.setColor(default_bg_color);
        canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, Math.min(canvas.getWidth(),canvas.getHeight())/2-2, paint);
    }

    private Bitmap createMask(int width, int height) {
        Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(mask);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, width, height, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRoundRect(new RectF(0, 0, width, height), cornerRadius, cornerRadius, paint);

        return mask;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentWidth, parentHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        Log.d(TAG,"onTouchEvent status" + isSecleted + action);
        if(action == MotionEvent.ACTION_DOWN) {
            isSecleted = !isSecleted;
            Log.d(TAG,"X:"+ event.getX());
        }
        drawBackgroud();
        return super.onTouchEvent(event);
    }





    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }




    /**
     * 设置图片资源
     */
    public void setImageResource(int resId) {
        iv.setImageResource(resId);
    }

    /**
     * 设置显示的文字
     */
    public void setTextViewText(String text) {
        tv.setText(text);
    }





//    public CouponLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }


}
