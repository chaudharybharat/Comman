package com.aktivo.GraphMystats;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import com.aktivo.R;
import com.aktivo.durationheartModel.ClassXY;
import com.aktivo.durationheartModel.Linexypos;
import com.aktivo.durationheartModel.TextPostionY;
import com.aktivo.durationheartModel.TextViewxyPos;
import com.aktivo.response.SeekBarData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by techiestown on 23/1/18.
 */
@SuppressLint("AppCompatCustomView")
public class CustomeGraphStepTaken extends SeekBar implements View.OnTouchListener,View.OnClickListener {
    private ArrayList<SeekBarData> mSeekBar_data;
    private ArrayList<Integer> mColorList;
    List<ClassXY> listtextXy;
    List<TextPostionY> postionYList;
    List<Linexypos> pathListLinestart;
    List<Linexypos> pathListLineend;
    List<TextViewxyPos> pathListText;
    android.os.Handler handler;
    int pos = -1;
    //if you incress bottom_end_point this point also you should inceress onTouch() method inside else you not got proper touch
    int bottom_end_point=200;
    public CustomeGraphStepTaken(Context context) {
        super(context);
        //   mSeekBar_data = new ArrayList<ProgressItem>();
        mColorList = new ArrayList<Integer>();
    }

    public CustomeGraphStepTaken(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomeGraphStepTaken(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void update_date(ArrayList<SeekBarData> seekBars_list){
        this.mSeekBar_data = seekBars_list;
        invalidate();

    }
    public void initData(ArrayList<SeekBarData> seekBars_list) {
        this.mSeekBar_data = seekBars_list;
        handler = new android.os.Handler();
    }
    public void initColorData(ArrayList<Integer> progressItemsList) {
        this.mColorList = progressItemsList;
        handler = new android.os.Handler();
    }

    public int getHeightValue() {
        return 300;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // int mViewWidth = measureWidth(widthMeasureSpec);
        //int mViewHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureWidth(int measureSpec) {
        int preferred = 222;
        return getMeasurement(measureSpec, preferred);
    }

    private int measureHeight(int measureSpec) {
        int preferred = 222;
        return getMeasurement(measureSpec, preferred);
    }

    private int getMeasurement(int measureSpec, int preferred) {
        int specSize = View.MeasureSpec.getSize(measureSpec);
        int measurement;
        switch (View.MeasureSpec.getMode(measureSpec)) {
            case View.MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case View.MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }
    int scaledSize;
    int you_size;
    int layout_widh=0;

    protected void onDraw(Canvas canvas) {
        scaledSize = getResources().getDimensionPixelSize(R.dimen.myFontSize);
        you_size = getResources().getDimensionPixelSize(R.dimen.you);
        bottom_end_point= getHeight()/2-50;
        listtextXy = new ArrayList<>();
        postionYList = new ArrayList<>();
        pathListLinestart = new ArrayList<>();
        pathListLineend = new ArrayList<>();
        pathListText = new ArrayList<>();
        Paint paint_text = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint paint_txt_normal = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_txt_normal.setColor(Color.BLACK);
        paint_txt_normal.setTextSize(scaledSize);

        layout_widh = getResources().getDimensionPixelSize(R.dimen.dp_55);

        Typeface light_font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Light.ttf");
        Typeface bold_font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montserrat-Bold.ttf");
        paint_txt_normal.setTypeface(light_font);

        Paint paint_network = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_network.setColor(Color.BLACK);
        paint_network.setTextSize(scaledSize);
        paint_network.setTypeface(bold_font);
        Paint paint_you = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_you.setColor(Color.BLACK);
        paint_you.setTypeface(bold_font);
        paint_you.setTextSize(you_size);
        Paint paint_line = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_line.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
      /*  if (mSeekBar_data.size() > 0) {*/
        int marginTop = 150;


        //right left padding getWidthh /7 is value set
        int margin_dyanamic = getWidth() / 15;
        int margin_left = margin_dyanamic;
        int every_progres_div = margin_left / 4;
        setOnTouchListener(this);
        setOnClickListener(this);
        int progressBarWidth = getWidth() - margin_left;
        int progressBarHeight = 130;
        int thumboffset = getThumbOffset();
        int lastProgressX = margin_left;
        int total_with = 0;
        int progressItemWidth, progressItemRight = 0;
        for (int i = 0; i < 4; i++) {
            int progressItem = mColorList.get(i);
            Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            progressPaint.setColor(progressItem);
            total_with = (progressBarWidth / 4) - every_progres_div;

            progressItemRight = lastProgressX + total_with;

            if (i == 0) {
                canvas.drawText("500", 0, 3, lastProgressX, 100, paint_txt_normal);
            }
            // for last item give right to progress item to the width
               /* if (i == mSeekBar_data.size() - 1 && progressItemRight != progressBarWidth) {
                    //  progressItemRight = progressBarWidth;
                }*/
            float cornerRadius = 30.0f;
            RectF backgroundRect = new RectF(lastProgressX, thumboffset / 2, progressItemRight, progressBarHeight - thumboffset / 2);
            Rect progressRect = new Rect();
            progressRect.set(lastProgressX, thumboffset / 2, progressItemRight, progressBarHeight - thumboffset / 2);
            int a = progressBarHeight - thumboffset / 2;
            Log.e("test", "aa===>>" + a);
            //round shape oneside
            if (i == 0) {
                Path path1 = RoundedRect(lastProgressX, marginTop, progressItemRight, progressBarHeight - thumboffset / 2, cornerRadius, cornerRadius,
                        true, false, false, true);
                canvas.drawPath(path1, progressPaint);
            } else if (i == mColorList.size() - 1) {
                Path path1 = RoundedRect(lastProgressX, marginTop, progressItemRight, progressBarHeight - thumboffset / 2, cornerRadius, cornerRadius,
                        false, true, true, false);
                canvas.drawPath(path1, progressPaint);
            } else {
                //round shape oneside
                Path path = RoundedRect(lastProgressX, marginTop, progressItemRight, progressBarHeight - thumboffset / 2, cornerRadius, cornerRadius,
                        false, false, false, false);
                canvas.drawPath(path, progressPaint);
            }
            progressPaint.setColor(Color.BLACK);
            progressPaint.setStyle(Paint.Style.STROKE);
            progressPaint.setStrokeWidth(3);


            //center line
            //stratY top to start
            //stopy stop to ...
            if(i<3){
                canvas.drawLine(lastProgressX + total_with, 100, lastProgressX + total_with, progressBarHeight + 40, paint_line);

            }
            //round shape
            //canvas.drawRoundRect(backgroundRect,cornerRadius,cornerRadius,progressPaint);
            lastProgressX = progressItemRight;
            if (i != 0) {
                //   lastProgressX=lastProgressX+5;
            }
            lastProgressX = lastProgressX + 5;
            //   int main_point= (int) (progressItem.heart_value * getWidth()/100);
            // int main_point2=0;

        }
        canvas.drawText("20000+", 0, 6, progressItemRight - layout_widh, 100, paint_txt_normal);


        //main_point2=main_point2-45;
        //Text
        RectF rect = new RectF(lastProgressX, 20, lastProgressX, 400);
        paint_text.setColor(Color.BLACK);
        paint_text.setTextSize(30f);
        paint_text.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));


        // int set_xpoint=dynamicvalue*total_widh/100/2;
        int totla_with_data = total_with * 4;
        Log.e("test", "totla_widh progrss=>" + totla_with_data);
        Log.e("test", "totla_layout_widh=>" + getWidth());
        Log.e("test", "totla_layout_widh=>" + getWidth());


        int fint_45_value = 45 * totla_with_data / 100;

        int value_incere = 1;

        for (int j = 0; j < mSeekBar_data.size(); j++) {
            //start to 0 equal to 45

            if( mSeekBar_data.get(j).getValue()>=500) {

                float value = mSeekBar_data.get(j).getValue() - 500;
                Log.e("test", "====>>" + value);

                float x_point = value * totla_with_data / 19500;
                int totla_with = total_with + margin_left;

                if (x_point < totla_with) {

                } else if (x_point < total_with * 2 + margin_left) {
                    x_point = x_point + 5;
                } else if (x_point < total_with * 3 + margin_left) {
                    x_point = x_point + 10;

                } else if (x_point < total_with * 4 + margin_left) {
                    x_point = x_point + 15;

                }

                String str_data = mSeekBar_data.get(j).getTitle();

                float width = paint_you.measureText(str_data) + 5;
                //    int last=x_point-fint_45_value;
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.BLACK);
                paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.myFontSize));
                // if (mSeekBar_data.get(j).getValue() >= 45) {

                Log.e("test", "test=====================================================>>");

                //  canvas.drawLine(x_point+margin_left,progressBarHeight+30, x_point+margin_left,150, paint);

                //get you text hight
                Rect bounds1 = new Rect();

                paint_text.getTextBounds(str_data, 0, str_data.length(), bounds1);
                int height2 = bounds1.height() + 5;
                float width2 = paint_you.measureText("" + mSeekBar_data.get(j).getValue()) + 5;
                float width_network = paint_network.measureText(str_data) + 5;

                //dot point end of line
                //      Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                // listtextXy.add(new ClassXY(((int)(main_point2 - width)),main_point2));

                float postion_x = x_point + margin_left;

              /*  Log.e("test", "set y pstion=> " + (bottom_end_point * value_incere));
                Log.e("test", "set x start pstion=> " + (postion_x - width));
                Log.e("test", "set x end pstion=> " + postion_x);*/

                postionYList.add(new TextPostionY((bottom_end_point * value_incere) - 50, (bottom_end_point * value_incere) + 50));
                if (j == 0){
                    //29750
                    if( value<5000){
                        //top text
                        pathListText.add(new TextViewxyPos(postion_x ,  getResources().getDimensionPixelSize(R.dimen.margin_sp)));
                        //second postion
                        // right side

                        canvas.drawText(str_data, 0, str_data.length(), postion_x+5, getResources().getDimensionPixelSize(R.dimen.txt_height_data), paint_you);
                        listtextXy.add(new ClassXY(postion_x+5,postion_x+width));

                    }else {
                        //top text
                        pathListText.add(new TextViewxyPos(postion_x ,  getResources().getDimensionPixelSize(R.dimen.margin_sp)));

                        //leftSide
                        canvas.drawText(str_data, 0, str_data.length(), postion_x - width,getResources().getDimensionPixelSize(R.dimen.txt_height_data), paint_you);
                        listtextXy.add(new ClassXY(postion_x - width,postion_x));
                    }

                    canvas.drawLine(postion_x,progressBarHeight+20, postion_x, getResources().getDimensionPixelSize(R.dimen.txt_height_data), paint_line);
                    canvas.drawCircle(postion_x, getResources().getDimensionPixelSize(R.dimen.txt_height_data), 5f, paint);
                    // canvas.drawText(str_data, 0, str_data.length(), postion_x - width, bottom_end_point * value_incere, paint_text);
                    //  listtextXy.add(new ClassXY(postion_x - width,postion_x));

                }else {
                    //29750
                    if( value<6000){
                        pathListText.add(new TextViewxyPos(postion_x ,  getResources().getDimensionPixelSize(R.dimen.margin_sp)));

                        //second postion
                        // right side
                        canvas.drawText(str_data, 0, str_data.length(), postion_x,  getResources().getDimensionPixelSize(R.dimen.txt_height_data_network), paint_network);
                        listtextXy.add(new ClassXY(postion_x ,postion_x+ width_network));

                    }else {
                        //top text
                        pathListText.add(new TextViewxyPos(postion_x ,  getResources().getDimensionPixelSize(R.dimen.margin_sp)));

                        //leftSide
                        canvas.drawText(str_data, 0, str_data.length(), postion_x - width_network, getResources().getDimensionPixelSize(R.dimen.txt_height_data_network), paint_network);
                        listtextXy.add(new ClassXY(postion_x - width_network,postion_x));


                    }






                    /*
                    canvas.drawText(str_data, 0, str_data.length(), postion_x - width, bottom_end_point * value_incere, paint_text);
                    listtextXy.add(new ClassXY(postion_x - width, postion_x));

                } else {
                    Log.e("test", "callllllll............=> ");
                    listtextXy.add(new ClassXY(postion_x - width / 2, postion_x + width / 2));
                    canvas.drawText(str_data, 0, str_data.length(), postion_x - width / 2, (bottom_end_point * value_incere) + height2, paint_text);
               */
                    canvas.drawLine(postion_x,progressBarHeight+20, postion_x, getResources().getDimensionPixelSize(R.dimen.txt_height_data_network), paint_line);
                    canvas.drawCircle(postion_x, getResources().getDimensionPixelSize(R.dimen.txt_height_data_network), 5f, paint);
                }
                //canvas.drawText(str_data, 0, str_data.length(), postion_x - width, bottom_end_point * value_incere + height2, paint_txt_normal);
                /*canvas.drawLine(postion_x, progressBarHeight + 20, postion_x, bottom_end_point * value_incere, paint_line);
                canvas.drawCircle(postion_x, bottom_end_point * value_incere, 5f, paint);
*/

                //top text display 2 second
                //float width2 = paint_text.measureText("" + mSeekBar_data.get(j).getValue()) + 5;
                // canvas.drawLine(postion_x,progressBarHeight, postion_x,progressBarHeight-40, paint);
                // canvas.drawText(""+arrayList.get(j),0,2,postion_x-width2,40,paint_text);
                // pathListText.add(new TextViewxyPos(postion_x-width2, 40));
                pathListLinestart.add(new Linexypos(postion_x , getResources().getDimensionPixelSize(R.dimen.txt_height)));
                // pathListLinestart.add(new Linexypos(postion_x , progressBarHeight-5));
                pathListLineend.add(new Linexypos(postion_x , getResources().getDimensionPixelSize(R.dimen.txt_height)-getResources().getDimensionPixelSize(R.dimen.txt_top)));
                value_incere = value_incere + 1;
            }


                }




                    /*//line black
                   if(i!=0){
                        int value=150*i;
                        Log.e("test","test====>"+value);
                        *//*canvas.drawLine(main_point2,progressBarHeight+30, main_point2,150*i, paint_line);

                        //dot point end of line
                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        paint.setColor(Color.BLACK);
                        paint.setStrokeWidth(1);
                        canvas.drawCircle(main_point2,150*i,5f,paint);*//*
                    }
                    //top line
                    String heart_value=String.valueOf(progressItem.heart_value);
                    float width2 = paint_text.measureText(heart_value)+5;
*/
        //canvas.drawText(heart_value,0,heart_value.length(),main_point-width2,40,paint_text);

        //  canvas.drawLine(main_point,30, main_point,progressBarHeight-12, progressPaint);
                  /*  pathListText.add(new TextViewxyPos(main_point2-width2,40));
                    pathListLinestart.add(new Linexypos(main_point2,40));
                    pathListLineend.add(new Linexypos(main_point2,progressBarHeight-12));*/
        //  }


        //up draw text and line
                /*if(i==3){
                   // canvas.drawLine(main_point,40, main_point,60, progressPaint);
                  //  canvas.drawText("70" ,0,2,main_point,40,paint_text);

                }*/
        //rquere shape
        //  canvas.drawRect(progressRect,progressPaint);


           /* int width = (int)paint_text.measureText("100")+5;
            int last_postion=progressItemRight-width;*/


        //draw top line and point display 2 second
        if (pos != -1) {
            paint_line.setColor(Color.BLACK);
            paint_line.setTextSize(scaledSize);
            paint_line.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            if(pos<mSeekBar_data.size()){
                String heart_value = String.valueOf(mSeekBar_data.get(pos).getValue());

                int heart_value_int= (int) Float.parseFloat(heart_value);
                String value_str=""+String.valueOf(heart_value_int);
                float value_with = paint_txt_normal.measureText(value_str);

                // float width2 = paint_text.measureText(heart_value)+5;
                canvas.drawText(value_str, 0, value_str.length(), pathListText.get(pos).getX()-value_with/2, pathListText.get(pos).getY(), paint_line);
                canvas.drawLine(pathListLinestart.get(pos).getX(), pathListLinestart.get(pos).getY(), pathListLineend.get(pos).getX(), pathListLineend.get(pos).getY(), paint_line);
            }

            //canvas.drawPath(pathListLinestart.get(pos),paint);
            // canvas.drawPath(pathListText.get(pos),paint_text);
            Log.e("test", "ondraw call" + pos);
        }
        Log.e("test", "ondraw call" + pos);

        super.onDraw(canvas);

    }
    public static Path RoundedRect(
            float left, float top, float right, float bottom, float rx, float ry,
            boolean tl, boolean tr, boolean br, boolean bl){
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        if (tr)
            path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        else{
            path.rLineTo(0, -ry);
            path.rLineTo(-rx,0);
        }
        path.rLineTo(-widthMinusCorners, 0);
        if (tl)
            path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        else{
            path.rLineTo(-rx, 0);
            path.rLineTo(0,ry);
        }
        path.rLineTo(0, heightMinusCorners);

        if (bl)
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        else{
            path.rLineTo(0, ry);
            path.rLineTo(rx,0);
        }

        path.rLineTo(widthMinusCorners, 0);
        if (br)
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        else{
            path.rLineTo(rx,0);
            path.rLineTo(0, -ry);
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }

    Runnable runnable_pint_dispay_two_second=new Runnable() {
        @Override
        public void run() {
            pos=-1;
            invalidate();
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int eventAction = event.getAction();

        // you may need the x/y location
        int x = (int)event.getX();
        int y = (int)event.getY();
        Log.e("test","onTouch click touch");
        // put your code in here to handle the event
        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                int main_start_Y=getResources().getDimensionPixelSize(R.dimen.txt_height_data);

                for (int i = 0; i <listtextXy.size() ; i++) {
                    Log.e("test","List x"+listtextXy.get(i).getXendPoint());

                    if(listtextXy.get(i).getXstartPoint()<=x && listtextXy.get(i).getXendPoint()>=x){
                        if(y>main_start_Y-40 && y<main_start_Y+40){
                            Log.e("test","click postion======>>>>1");
                            pos=0;
                            invalidate();
                            if(handler!=null){
                                handler.removeCallbacks(runnable_pint_dispay_two_second);
                                handler.postDelayed(runnable_pint_dispay_two_second,2000);
                            }
                            break;

                        } else if(y>(getResources().getDimensionPixelSize(R.dimen.txt_height_data_network))-40 && y<(getResources().getDimensionPixelSize(R.dimen.txt_height_data_network))+40){
                            pos=1;
                            Log.e("test","click postion======>>>>2");
                            invalidate();
                            if(handler!=null){
                                handler.removeCallbacks(runnable_pint_dispay_two_second);
                                handler.postDelayed(runnable_pint_dispay_two_second,2000);
                            }
                            break;

                        }


                       /*else if(y<480 && y<620){
                        pos=3;
                           Log.e("test","click postion======>>>>3");
                        invalidate();
                        if(handler!=null){
                            handler.removeCallbacks(runnable_pint_dispay_two_second);
                            handler.postDelayed(runnable_pint_dispay_two_second,2000);
                        }
                           break;

                       } else if(y<620 && y<780){
                        //pos=4  ;
                        Log.e("test","click postion======>>>>4");
                        invalidate();
                        if(handler!=null){
                            handler.removeCallbacks(runnable_pint_dispay_two_second);
                            handler.postDelayed(runnable_pint_dispay_two_second,2000);
                        }
                        break;

                    }
*/
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        Log.e("test","click"+v.getY());
    }
    public static float convertPixelsToDp(float px,Context context){

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;

    }
}

