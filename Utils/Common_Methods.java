package com.aktivo.Utils;

import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.NetworkOnMainThreadException;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.aktivo.AnimationDialog.Effectstype;
import com.aktivo.AnimationDialog.NiftyDialogBuilder;
import com.aktivo.MainActivity;
import com.aktivo.R;
import com.aktivo.response.TodayYouHave;
import com.commonmodule.mi.utils.Validation;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by kundan on 7/4/16.
 */
public class Common_Methods {

    //dfljsdlkjl
    static int device_width = 0;
    static int device_height = 0;
    public static MainActivity mActivity = null;
    public Common_Methods(MainActivity activity) {
        mActivity = activity;
    }
    private Effectstype effect;

    public String getDeviceId(MainActivity mActivity){
       return  Settings.Secure.getString(mActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    public String getDeviceName(){
        return Build.MODEL;
    }
    public void setStatusBarGone(Activity context,int color_value){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = context.getWindow();
            window.setStatusBarColor(context.getResources().getColor(color_value));
            //window.setNavigationBarColor(getResources().getColor(R.color.transparent));
        }

    }
    TodayYouHave todayYouHave;
    public TodayYouHave getTodayHaveData(){
             if(todayYouHave==null){
                 todayYouHave = TodayYouHave.getTodayYouHaveDetail();
             }else {
                 return todayYouHave;
             }
        return todayYouHave;
    }
    public int getTotalWeeksInYear(int year) {
        Calendar mCalendar = new GregorianCalendar(TimeZone.getDefault());
        mCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        // Workaround
        mCalendar.set(year,
                Calendar.DECEMBER,
                31);
        int totalDaysInYear = mCalendar.get(Calendar.DAY_OF_YEAR);
        System.out.println(totalDaysInYear);
        int totalWeeks = totalDaysInYear / 7;
        return totalWeeks;
    }


    public void setCutemDialogMessage(final MainActivity mActivity, String messge){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View dialogView= inflater.inflate(R.layout.include_opt_layout, null);
        TextView textView=(TextView) dialogView.findViewById(R.id.tv_messge_api_sucess);
        ImageView iv_close=(ImageView) dialogView.findViewById(R.id.ic_close);

        textView.setText(Html.fromHtml(""+messge));
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.getWindow().setDimAmount(0.0f);
        wmlp.gravity = Gravity.TOP | Gravity.LEFT | Gravity.RIGHT;
        wmlp.x = 50;   //x position
        wmlp.y = 50;   //y position
        wmlp.windowAnimations = R.style.DialogAnimation;
        dialog.show();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });

    }

    public String getBackgroundImage(MainActivity mActivity,String value){
        String image="http://1h23on8hs5s44c2iqm9tze2x.wpengine.netdna-cdn.com/wp-content/uploads/2014/02/Studio-5.jpg";

        switch (value){
            case CommonKeys.BACKGROUND_MAIN:

                String imageback=MyPreferences.getPref(mActivity,value);
                if(Validation.isRequiredField(imageback)){
                    image=imageback;
                }
                break;
            case CommonKeys.BACKGROUND_SUB:
                String imageback_sub=MyPreferences.getPref(mActivity,value);
                if(Validation.isRequiredField(imageback_sub)){
                    image=imageback_sub;
                }
                break;

                default:
        }
        return image;
    }


    public void openDialog(MainActivity mActivity,String msg) {

        final NiftyDialogBuilder dialogBuilder= NiftyDialogBuilder.getInstance(mActivity);
       // dialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBuilder
                //.withTitle(null)  no title
                /*.withTitleColor(mActivity.getResources().getColor(R.color.transparent))                                  //def
                .withDividerColor(mActivity.getResources().getColor(R.color.transparent))                              //def
                .withMessage("This is a modal Dialog.")                     //.withMessage(null)  no Msg
                .withMessageColor(mActivity.getResources().getColor(R.color.transparent))                              //def  | withMessageColor(int resid)
                .withDialogColor(mActivity.getResources().getColor(R.color.transparent))          */                     //def  | withDialogColor(int resid)
              //  .withIcon(getResources().getDrawable(R.drawable.icon))
                .withDuration(700)                                          //def
                .withEffect(effectAnimation(5))  .setTextTv_Textview(""+msg)                                       //def Effectstype.Slidetop
                /*.withButton1Text("OK")                                      //def gone
                .withButton2Text("Cancel")   */                               //def gone
                .isCancelableOnTouchOutside(true)   .setIconClic(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        })                        //def    | isCancelable(true)
               .setButton1Click(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       dialogBuilder.dismiss();

                   }
               });

        dialogBuilder.show();
        /*TextView tv_done = (TextView) dialogBuilder.findViewById(R.id.tv_done);
        TextView tv_text = (TextView) dialogBuilder.findViewById(R.id.tv_text);
        ImageView iv_close = (ImageView) dialogBuilder.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        tv_text.setText(Html.fromHtml(""+msg));
        // tv_title.setText(styledString);
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });*/

       /* final Dialog dialog = new Dialog(mActivity,R.style.DialogAnimation);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.comman_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();
        TextView tv_done = (TextView) dialog.findViewById(R.id.tv_done);
        TextView tv_text = (TextView) dialog.findViewById(R.id.tv_text);
        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
       *//* SpannableString styledString
                = new SpannableString(msg);
        // change text color
        styledString.setSpan(new StyleSpan(Typeface.BOLD), msg.indexOf("your"), msg.length(), 0);
        styledString.setSpan(new ForegroundColorSpan(Color.BLACK), msg.indexOf("your"), msg.length(),0);
        tv_text.setText(styledString);*//*
        tv_text.setText(Html.fromHtml(""+msg));
        // tv_title.setText(styledString);
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

    }

    public void openDataAvalibityNotAvaliableDialog(){
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.data_avalibality_not_available);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
        TextView tv_header = (TextView) dialog.findViewById(R.id.tv_title);
        tv_header.setTypeface(getFont(mActivity, CommonKeys.Montserrat_Light));
        tv_yes.setTypeface(getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_no.setTypeface(getFont(mActivity, CommonKeys.Montserrat_Bold));

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
    }

    public Typeface getFont(MainActivity mActivity,int font_name){
        Typeface font_type;
        switch (font_name){
            case 1:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-Black.ttf");
                break;
            case 2:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-BlackItalic.ttf");
                break;
            case 3:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-Bold.ttf");
                break;
            case 4:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-BoldItalic.ttf");
                break;
            case 5:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-ExtraBold.ttf");
                break;
            case 6:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-ExtraBoldItalic.ttf");
                break;
            case 7:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-ExtraLight.ttf");
                break;
            case 8:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-ExtraLightItalic.ttf");
                break;
            case 9:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-Italic.ttf");
                break;
            case 10:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-Light.ttf");
                break;
            case 11:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-LightItalic.ttf");
                break;

            case 12:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-Medium.ttf");
                break;

            case 13:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-MediumItalic.ttf");
                break;

            case 14:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-Regular.ttf");
                break;
            case 15:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-SemiBold.ttf");
                break;
                case 16:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-SemiBoldItalic.ttf");
                break;
                case 17:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-Thin.ttf");
                break;
                case 18:
                font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-ThinItalic.ttf");
                break;
               default:
                   font_type= Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-Light.ttf");
                   break;

        }
        return font_type;

    }


    public Effectstype effectAnimation(int value){
        Effectstype effect;
        switch (value){
            case 1:
                effect= Effectstype.Fadein;
                break;
            case 2:
                effect=Effectstype.Slideright;
                break;
                case 3:
                effect=Effectstype.Slideleft;
                break;
                case 4:
                effect=Effectstype.Slidetop;
                break;
            case 5:
                effect=Effectstype.SlideBottom;
                break;
            case 6:
                effect=Effectstype.Newspager;
                break;
            case 7:
                effect=Effectstype.Fall;
                break;
            case 8:
                effect=Effectstype.Sidefill;
                break;
                case 9:
                effect=Effectstype.Fliph;
                break;
            case 10:
                effect=Effectstype.Flipv;
                break;
                case 11:
                effect=Effectstype.RotateBottom;
                break;
             case 12:
                effect=Effectstype.RotateLeft;
                break;
                case 13:
                effect=Effectstype.Slit;
                break;
                case 14:
                    effect=Effectstype.Shake;
                break;
                default:
                    effect=Effectstype.Fadein;
                    break;

        }
        return effect;
    }


    public static String getRealPathFromURI(MainActivity mActivity, Uri contentURI) {
        String result;
        Cursor cursor = mActivity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    public static int getDeviceWidth(MainActivity mainActivity) {
        if (device_width == 0) {
            Display display = mainActivity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            device_width = size.x;
        }
        return device_width;
    }

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int getDeviceHeight(MainActivity mainActivity) {
        if (device_height == 0) {
            Display display = mainActivity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            device_height = size.y;
        }
        return device_height;
    }
   /* public static String getUserId(){
        try{
            Login_Table login_table_data = new Select().from(Login_Table.class).querySingle();
            if(login_table_data!=null){
                if(login_table_data.getId()!=null){
                    return login_table_data.getId();
                }else{
                    return "";
                }
            }else{
                return "";
            }


        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }*/


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public  static String getPath(Uri uri,Context context) {
       String[] projection = {MediaStore.MediaColumns.DATA};
       Cursor cursor = ((Activity)context).managedQuery(uri, projection, null, null, null);
       int column_index = cursor
               .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
       cursor.moveToFirst();
       String imagePath = cursor.getString(column_index);

       return cursor.getString(column_index);
   }

    public static boolean checkIsMarshMallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? true : false;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isPermissionNotGranted(Context context, String[] permissions) {
        boolean flag = false;
        for (int i = 0; i < permissions.length; i++) {
            if (context.checkSelfPermission(permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                flag = true;
                break;
            }


        }
        return flag;
    }

    public static void whichPermisionNotGranted(Context context, String[] permissions, int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
              // throw  new UnknownError("ERROR==>>>>Authentication Permission Not Enabled");
            }
        }
    }
    public static void setExceptionMessage(Throwable t, MainActivity mActivity) {
        if (t instanceof SocketTimeoutException) {
            mActivity.showCroutonsMessage(mActivity,mActivity.getResources().getString(R.string.server_timeout_error));
           // mActivity.showSnakbarMessage(mActivity.getResources().getString(R.string.server_timeout_error));
        } else if (t instanceof UnknownHostException) {
            mActivity.showCroutonsMessage(mActivity,mActivity.getResources().getString(R.string.no_internet_connection));
            // mActivity.showSnakbarMessage(mActivity.getResources().getString(R.string.no_internet_connection));
        } else if (t instanceof NetworkErrorException || t instanceof NetworkOnMainThreadException) {
            mActivity.showCroutonsMessage(mActivity,mActivity.getResources().getString(R.string.network_error));
            // mActivity.showSnakbarMessage(mActivity.getResources().getString(R.string.network_error));
        } else {
//            mActivity.toastSnackBar("");
        }
    }
    public static String stringToDate(Date aDate,String aFormat) {

        SimpleDateFormat formatter = new SimpleDateFormat(aFormat);
        String date = formatter.format(new Date());
        return date;

    }
    public static String getDate(long milliSeconds,String formate) {
        // Create a DateFormatter object for displaying date in specified
        // format.
        SimpleDateFormat formatter = new SimpleDateFormat(formate);
        // Create a calendar object that will convert the date and time value in
        // milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((int) milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public String getProfileDateFormat(String date){

        SimpleDateFormat input = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date oneWayTripDate = input.parse(date);    // parse input
            return output.format(oneWayTripDate);    // format output
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getDateFormate(Long date){
        SimpleDateFormat sdf = new SimpleDateFormat(CommonKeys.DATE_FORMATE, Locale.US);

        String date_formate = sdf.format(date);

        return date_formate;
    }


   /* public static void getLogoutWithClearData(){


        Delete.tables(Comment_Table.class,
                Filter_City_Table.class,
                Filter_Country_Table.class,
                Filter_Course_Table.class,
                Filter_Institute_Name_Table.class,
                Filter_State_Table.class,
                FollowUnfollow_Table.class,
                ForgotPassword_Table.class,
                GoLive_Images_Table.class,
                GoLive_Table.class,
                GoLiveDetail_Images_Table.class,
                GoLiveDetail_Table.class,
                HolidayList_Table.class,
                HostelData_Table.class,
                Institute_List_Table.class,
                InstituteUnfollow_Table.class,
                InstituteUnfollow_Table.class,
                Login_Table.class,
                MyPerformance_Table.class,
                NotificationList_Table.class,
                ResultDetail_Table.class,
                ResultList_Table.class,
                SubjectDetail_Class_Perfo_Table.class,
                SubjectDetail_Exam_Table.class,
                SubjectName_Class_Perfo_Table.class
                );

    }*/

    public static String firstlaterCapse(String name) {
        StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    public String getCurrentDateFull(){

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        System.out.println("formattedDate >>>"+formattedDate);
        return formattedDate;

    }




    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    public int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static void watchYoutubeVideo(MainActivity mActivity, String id){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            mActivity.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            mActivity.startActivity(intent);
        }

    }

    public String getDateWithFormat(long timeStamp){

        try{
            //20 June 2016, 10:04 PM
            DateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, hh:mm a");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "";
        }
    }



    public String printDifference(Date date_small, Date date_Bigger,final String timestamp) {

        try{

            System.out.println("date_small >>>> " +date_small+"<<<<<>>>>>>"+"date_Bigger <<<>>>"+date_Bigger);

            if(date_small==null){
                return "";
            }
            if(date_Bigger==null){
                return "";
            }
            //milliseconds
            if(date_small==null||date_Bigger==null){
                return "";
            }

            long different = date_Bigger.getTime() - date_small.getTime();


            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

              /*
            Just now(if time < 1 min)
            2 min(if time < 1 hour)
            1 hour(if time < 1 day)
            27 Jan 2015, 08:20 AM (If time > 1 day)
*/
            String time = "";

            if (elapsedDays > 1) {

                time = getDatefromTimestamp(timestamp);
                return time;

            }


            if (elapsedDays == 1) {
                time = elapsedDays + " day ";

                return time;
            }

            if (elapsedHours > 0) {


                if (elapsedHours == 1) {
                    time = elapsedHours + " hr ";
                } else {
                    time = elapsedHours + " hrs ";
                }
                return time;
            }

            if (elapsedMinutes > 0) {


                if (elapsedMinutes == 1) {
                    time = elapsedMinutes + " min ";
                } else {
                    time = elapsedMinutes + " mins ";
                }
                return time+"";
            }

            if (elapsedSeconds > 0) {


                if (elapsedSeconds == 1) {
                    time = elapsedSeconds + " sec ";
                } else {
                    time = elapsedSeconds + " secs ";
                }
                return "Just now";
            }


        }catch (Exception e){
            e.printStackTrace();
            return "";
        }

        return "";
    }

    /*Just now(if time < 1 min)
    2 min(if time < 1 hour)
    1 hour(if time < 1 day)
    27 Jan 2015, 08:20 AM (If time > 1 day)
*/

    public static String getDatefromTimestamp(String timeStamp){
        try{

            long time_value = 0;

            if(timeStamp.toString().length()>0){
                time_value = Long.parseLong(timeStamp);
            }

            DateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm a");
            Date netDate = (new Date(time_value*1000));

            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "0";
        }
    }

    public static String getDateFormateCutome(String timeStamp){
        try{

            long time_value = 0;

            if(timeStamp.toString().length()>0){
                time_value = Long.parseLong(timeStamp);
            }

            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date netDate = (new Date(time_value*1000));

            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "0";
        }
    }



    public String getCurrentMonthTwoDigit(){
        //create Date object
        Date date = new Date();

        //formatting month in M format like 1,2 etc
        String strDateFormat = "MM";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        System.out.println("Current Month in MM format : " + sdf.format(date));

        return sdf.format(date);

    }

    public int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        //System.out.println("Current time =&gt; "+c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        System.out.println("FormattedDate" + formattedDate);
        return formattedDate;
    }

    public Date getCurrentDateToday() {

        try{
            Calendar c = Calendar.getInstance();
            //System.out.println("Current time =&gt; "+c.getTime());
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = df.format(c.getTime());
            System.out.println("FormattedDate" + formattedDate);

            Date dateObj = df.parse(formattedDate);
            return dateObj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Date getReturnDate(String name){

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObj = null;
        try {
            dateObj = df.parse(name);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateObj;

    }


    public boolean getCompareDate(Date date){

        if (getCurrentDateToday().after(date)||getCurrentDateToday().equals(date)) {
            return true;
        }else{
            return false;
        }

    }
    public static void showMessageOKCancel_new(MainActivity mActivity, String message, DialogInterface.OnClickListener okListener,DialogInterface.OnClickListener cancelListener) {

        new android.support.v7.app.AlertDialog.Builder(mActivity)
                .setMessage(message)
                .setPositiveButton("OK",okListener)
                .setNegativeButton("Cancel", cancelListener)
                .create()
                .show();

    }



    /*public static boolean getPermissionCheckMultiple(final MainActivity mActivity, final List<PermissionCheck> list_of_permisson) {

        int hasReadWritePermisson = 0;
        for (int i = 0; i <list_of_permisson.size() ; i++) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hasReadWritePermisson = mActivity.checkSelfPermission(String.valueOf(list_of_permisson.get(i).getpermissionName()));

                if (hasReadWritePermisson != PackageManager.PERMISSION_GRANTED) {

                    final String permisssion = list_of_permisson.get(i).getpermissionName();
                    final int requestCode = list_of_permisson.get(i).getRequestCode();

                    if (!mActivity.shouldShowRequestPermissionRationale(String.valueOf(list_of_permisson.get(i).getpermissionName()))) {
                        showMessageOKCancel(mActivity,list_of_permisson.get(i).getpermissionMessage(),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            mActivity.requestPermissions(new String[] {String.valueOf(permisssion)},
                                                    requestCode);
                                        }

                                    }
                                });
                        return false;
                    }
                    mActivity.requestPermissions(new String[] {String.valueOf(list_of_permisson.get(i).getpermissionName())},
                            list_of_permisson.get(i).getRequestCode());
                    return false;
                }
            }
        }

        //insertDummyContact();
        return true;

    }*/


    public static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "<sup>th</sup>";
        }
        switch (day % 10) {
            case 1:
                return "<sup>st</sup>";
            case 2:
                return "<sup>nd</sup>";
            case 3:
                return "<sup>rd</sup>";
            default:
                return "<sup>th</sup>";
        }
    }
    @SuppressLint("WrongConstant")
    public boolean getPermissonCheck(final MainActivity mActivity, final String permisson, final int request_code, String msg) {

        int hasReadWritePermisson = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasReadWritePermisson = mActivity.checkSelfPermission(permisson);

            if (hasReadWritePermisson != PackageManager.PERMISSION_GRANTED) {

                if (!mActivity.shouldShowRequestPermissionRationale(permisson)) {
                    showMessageOKCancel(mActivity, msg,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        mActivity.requestPermissions(new String[]{permisson},
                                                request_code);
                                    }

                                }
                            });
                    return false;
                }
                mActivity.requestPermissions(new String[]{permisson},
                        request_code);
                return false;
            }
        }

        //insertDummyContact();
        return true;

    }

    public void getWebview( MainActivity mActivity,String url ){

        try{
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            mActivity.startActivity(i);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public  void emailintent(MainActivity mActivity, String email) {


        Intent intent = new Intent(Intent.ACTION_SEND);


        intent.setData(Uri.parse(email));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "FlightPic");
        intent.putExtra(Intent.EXTRA_TEXT,  "");

        intent.setType("plain/text");

        intent.setPackage("com.google.android.gm");
        mActivity.startActivity(intent);
    }
   /*public void callImage(SchoolInfoFragment schoolInfoFragment) {

//        List<PermissionCheck>  permissionCheckList= new ArrayList<>();
//        permissionCheckList.add(new PermissionCheck(Manifest.permission.READ_EXTERNAL_STORAGE,mActivity.getResources().getString(R.string.you_need_to),101));
//        permissionCheckList.add(new PermissionCheck(android.Manifest.permission.CAMERA,mActivity.getResources().getString(R.string.you_need_to_access),102));

            FishBun.with(mActivity)
                    .setCamera(true)
                    .setPickerCount(1)
                    .startAlbum();

    }*/


    public static void showMessageOKCancel(MainActivity mActivity, String message, DialogInterface.OnClickListener okListener) {

        new android.support.v7.app.AlertDialog.Builder(mActivity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();

    }



    public static String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length() - 1);
    }

    public static void checkDataSizeRecycleview(TextView textView, String message, RecyclerView recycle, final int mSize) {
        try {
            if (mSize > 0) {
                recycle.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(message);
                recycle.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public int getCurrentDay_of_week(){

        try{
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            switch (day) {
                case Calendar.SUNDAY:
                    // Current day is Sunday

                    return 6;

                case Calendar.MONDAY:
                    // Current day is Monday

                    return 0;

                case Calendar.TUESDAY:
                    // etc.

                    return 1;

                case Calendar.WEDNESDAY:
                    // Current day is Monday

                    return 2;

                case Calendar.THURSDAY:
                    // etc.

                    return 3;
                case Calendar.FRIDAY:
                    // etc.

                    return 4;
                case Calendar.SATURDAY:
                    // etc.

                    return 5;

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;

    }
    public static File savebitmap(String filename, Bitmap bitmap) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/Intoo");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/Intoo/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/Intoo/"), filename + ".png");
        if (file.exists()) {
            file.delete();
        }


        OutputStream outStream = null;


        try {
            // make a new bitmap from your file
            // Bitmap bitmap = BitmapFactory.decodeFile(file.getName());

            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;

    }
    public static void clearTwitterCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();

        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }
    public static String getVideoThumb(MainActivity context,String path) {

        if(Validation.isRequiredField(path)) {
            try {
                final MediaMetadataRetriever mmrVideo = new MediaMetadataRetriever();
                mmrVideo.setDataSource(context, Uri.parse(path));
                Bitmap bitmap = mmrVideo.getFrameAtTime(10000, MediaMetadataRetriever.OPTION_CLOSEST);
                if (bitmap != null) {
                    String timeStamp = new android.icu.text.SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
                    File file = savebitmap(timeStamp, bitmap);
                    if (file.exists()) {
                        return file.getAbsolutePath();
                    }
                }
            }catch (Exception e){
                e.getLocalizedMessage();
            }
        }
        return "";

    }
    public static void send_Loc_BroadCast_intent2(Context activity, Intent intent) {
        try {
            LocalBroadcastManager.getInstance(activity).
                    sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Reg_Loc_BroadCast(BroadcastReceiver BR_receiver, Activity activity, String br_name) {
        try {
            LocalBroadcastManager.getInstance(activity).registerReceiver(BR_receiver,
                    new IntentFilter(br_name.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void UnReg_Loc_BroadCast(BroadcastReceiver BR_name, Activity mActivity) {
        try {
            LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(BR_name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   /* public static LoginUserDataTable getLoginUser() {
        try {
            LoginUserDataTable signUp_table = SQLite.select().distinct()
                    .from(LoginUserDataTable.class).querySingle();
            return signUp_table;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public void getRate(MainActivity mActivity) {

        try {

            Uri uri = Uri.parse("market://details?id=" + mActivity.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                mActivity.startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                mActivity.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + mActivity.getPackageName())));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setTextStripeLine(TextView tv_content) {

        try {
            tv_content.setPaintFlags(tv_content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static String getPath(MainActivity mActivity,Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = mActivity.getContentResolver().query(uri,projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    public String CovertFromOnetoAnotherDate(String name) {

        try {

            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
            Date date = originalFormat.parse(name);
            String formattedDate = targetFormat.format(date);

            return formattedDate;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }


    }



    public String getMonth(int month){

        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
        }

        return "";
    }






    public void getDirectionIntent(MainActivity mActivity, double latitude, double longitude) {
        try {

            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
            mActivity.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getDecimalValue(double key_value) {
        try {
            DecimalFormat precision = new DecimalFormat("0.00");
            return precision.format(key_value);

        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }

    }

    public String getDateFormat(String date) {
        try {
            DateFormat df = new SimpleDateFormat("dd MM yy");
            Date d = df.parse(date);
            df = new SimpleDateFormat("yyyy-MM-dd");
            //System.out.println("DATE FORMAT >>>>"+df.format(d));
            return df.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /*public void showAlertforForgot(final MainActivity activity, final String title, final String positiveButtonText, final String negativeButtonText, final String message) {

        try {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    *//*title = activity.getString(R.string.logout);*//*
                    if (title != null) {
                    *//* if (Methods.isValidString(title)) {
                            builder.setTitle(title);
                       }*//*
                    }

                    builder.setCancelable(false);
                    builder.setMessage(message)
                            .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    activity.pushFragmentDontIgnoreCurrent(new ResetPasswordFragment(), activity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);

                                }
                            }).setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                            .show();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/




    /*public static void showAlertDialogCommon(final ActivityModel activity, final String title, final String positiveButtonText, final String negativeButtonText, final String message) {

        try {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    *//*title = activity.getString(R.string.logout);*//*
                    if (title != null) {
                   *//*     if (Methods.isValidString(title)) {
                            builder.setTitle(title);
                        }*//*
                    }

                    builder.setCancelable(false);
                    builder.setMessage(message)
                            .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getLogoutWithClearData();
                                    mActivity.clearBackStack();
                                    mActivity.pushFragmentIgnoreCurrent(new LoginFragment(), MIActivity.FRAGMENT_JUST_REPLACE);
                                    mActivity.closeMenu();

                                }
                            }).setNegativeButton(negativeButtonText*//*activity.getString(R.string.cancel)*//*, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            })
                            .show();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/



    ///

    //

    //

    /*public void showAlertLogout(final MainActivity activity, final String title, final String positiveButtonText, final String negativeButtonText, final String message) {

        try {

              *//*title = activity.getString(R.string.logout);*//*
            if (title != null) {
                   *//*     if (Methods.isValidString(title)) {
                            builder.setTitle(title);
                        }*//*
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new android.support.v7.app.AlertDialog.Builder(activity)
                    .setCancelable(false)
                    .setMessage(message)
                            .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    // Unsubscribe to stop getting notifications after logout
                                    OneSignal.setSubscription(false);

                                    getLogoutWithClearData();
                                    activity.clearBackStack();
                                    activity.pushFragmentIgnoreCurrent(new LoginFragment(), MIActivity.FRAGMENT_JUST_REPLACE);
                                    activity.closeMenu();

                                }
                            }).setNegativeButton(negativeButtonText*//*activity.getString(R.string.cancel)*//*, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                            .show();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/


    /*public void showAlertNotification(final MainActivity activity, final String title, final String positiveButtonText, final String negativeButtonText, final String message) {

        try {

              *//*title = activity.getString(R.string.logout);*//*
            if (title != null) {
                   *//*
                   if (Methods.isValidString(title)) {
                            builder.setTitle(title);
                   }
                   *//*
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new android.support.v7.app.AlertDialog.Builder(activity)
                            .setCancelable(false)
                            .setMessage(message)
                            .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    if(!mActivity.getVisibleFragment().equals(new NotificationFragment().getClass().getname())){
                                       mActivity.pushFragmentIgnoreCurrent(new NotificationFragment(), mActivity.FRAGMENT_JUST_REPLACE);
                                    }else{
                                        //sendBroadCast to NotificationFrag

                                    }

                                }
                            }).setNegativeButton(negativeButtonText*//*activity.getString(R.string.cancel)*//*, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                            .show();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String getCounter(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return getCounter(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + getCounter(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }


    /*public void setDrawablewihtFresco(SimpleDraweeView sd_image){

        try{

            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.drawable.video_place_holder).build();
            sd_image.setImageURI(imageRequest.getSourceUri());

        }catch (Exception e){
            e.printStackTrace();
        }

    }*/



    public static void showAlertDialogforLike(final Activity activity, final String positiveButtonText, final String negativeButtonText, final String message) {

        try {


            new android.support.v7.app.AlertDialog.Builder(activity)
                    .setMessage(message)
                    .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                    .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        }
                    })
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public int getViewPagerHeight() {
        try {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int width = displaymetrics.widthPixels;
            int height = displaymetrics.heightPixels;
            int final_height = (width * 2) / 4;
            return final_height;
        } catch (Exception e) {
            e.printStackTrace();
            return 300;
        }
    }

    public class DownloadImage extends AsyncTask<String, Void, String> {
        String image_url = "";

        public DownloadImage(String image_url) {
            this.image_url = image_url;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... URL) {

            download_img(image_url);

            return "";
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    public void download_img(String imageurl) {
        // String imageurl = "YOUR URL";
        try {
            Bitmap bmpimg = null;
            InputStream in = null;

            try {
                Log.i("URL", imageurl);
                URL url = new URL(imageurl);
                URLConnection urlConn = url.openConnection();

                HttpURLConnection httpConn = (HttpURLConnection) urlConn;

                httpConn.connect();

                in = httpConn.getInputStream();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bmpimg = BitmapFactory.decodeStream(getConnection(imageurl));

            if (bmpimg == null) {
                System.out.println("Your image is null>>>>>>>");
            }


            Create_Save_Bitmap(bmpimg, "PUPO_user", "profile_img" + "" + ".png");

        } catch (Exception e) {
            e.printStackTrace();

        }
    }



    public static void Create_Save_Bitmap(Bitmap bitmapPicture, String foldername, String filename) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(),
                    foldername);

            if (!file.exists()) {
                file.mkdir();
            } else {
                file.delete();
                file.mkdir();
            }

            // long Time = System.currentTimeMillis();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(String.format(Environment
                        .getExternalStorageDirectory()
                        + "/"
                        + foldername
                        + "/"
                        + filename));
                if (fos != null) {
                    bitmapPicture.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                }
            } catch (Exception e) {

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public InputStream getConnection(String url) {

        try {

            //String url = "http://www.twitter.com";

            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setReadTimeout(5000);
            conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            conn.addRequestProperty("User-Agent", "Mozilla");
            conn.addRequestProperty("Referer", "google.com");

            System.out.println("Request URL ... " + url);

            boolean redirect = false;

            // normally, 3xx is redirect
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }

            System.out.println("Response Code ... " + status);

            if (redirect) {

                // get redirect url from "location" header field
                String newUrl = conn.getHeaderField("Location");

                // get the cookie if need, for login
                String cookies = conn.getHeaderField("Set-Cookie");

                // open the new connnection again
                conn = (HttpURLConnection) new URL(newUrl).openConnection();
                conn.setRequestProperty("Cookie", cookies);
                conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                conn.addRequestProperty("User-Agent", "Mozilla");
                conn.addRequestProperty("Referer", "google.com");

                System.out.println("Redirect to URL : " + newUrl);

            }

            return conn.getInputStream();

            /*BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            */


            /*String inputLine;
            StringBuffer html = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                html.append(inputLine);
            }
            in.close();*/

            /*System.out.println("URL Content... \n" + html.toString());*/
            /*System.out.println("Done");*/

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static boolean hasNavBar(Resources resources) {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }

    public static String getYoutubeVideoId(String youtubeUrl) {
        String video_id="";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http"))
        {

            String expression = "^.*((youtu.be"+ "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
            CharSequence input = youtubeUrl;

            Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches())
            {
                String groupIndex1 = matcher.group(7);
                if(groupIndex1!=null && groupIndex1.length()==11)
                    video_id = groupIndex1;
            }
        }
        System.out.println("@@VIDEO_ID::::::::" + video_id);
        return video_id;
    }

    //decodes image and scales it to reduce memory consumption
    public static Bitmap decodeFile(File f) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG,100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    /*public static void checkLocationPermission(int LOCATION_PERMISSION_CODE, Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                mActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_CODE);

            } else {
                // check for Gps status here and if Gps is disabled, show popup to enable Gps service and then push Home Fragment
                if (!mActivity.checkGpsStatus()) {
                    context.settingsRequest();
                }
                else {
                    context.callHome();
                }

            }
        } else {
            // check for Gps status here and if Gps is disable, show popup to enable Gps service and then push Home Fragment
            if (!mActivity.checkGpsStatus()) {
                mActivity.settingsRequest();
            }
            else {
                context.callHome();
            }
        }
    }*/


    public static Bitmap getRoundedCornerBitmap(Context context, Bitmap input, int pixels , int w , int h , boolean squareTL, boolean squareTR, boolean squareBL, boolean squareBR  ) {

        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);

//make sure that our rounded corner is scaled appropriately
        final float roundPx = pixels*densityMultiplier;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);


//draw rectangles over the corners we want to be square
        if (squareTL ){
            canvas.drawRect(0, 0, w/2, h/2, paint);
        }
        if (squareTR ){
            canvas.drawRect(w/2, 0, w, h/2, paint);
        }
        if (squareBL ){
            canvas.drawRect(0, h/2, w/2, h, paint);
        }
        if (squareBR ){
            canvas.drawRect(w/2, h/2, w, h, paint);
        }

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(input, 0,0, paint);

        return output;
    }

    /*public static void setExceptionMessage(Throwable t, MainActivity mActivity) {
        if (t instanceof SocketTimeoutException) {
            mActivity.showSnakbarMessage(mActivity.getResources().getString(R.string.server_timeout_error));
//            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.server_timeout_error), Toast.LENGTH_SHORT).show();
        } else if (t instanceof UnknownHostException) {
            mActivity.showSnakbarMessage(mActivity.getResources().getString(R.string.no_internet_connection));
//            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        } else if (t instanceof NetworkErrorException || t instanceof NetworkOnMainThreadException) {
            mActivity.showSnakbarMessage(mActivity.getResources().getString(R.string.network_error));
//            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        } else {
//            mActivity.toastSnackBar("");
        }
    }*/

}
