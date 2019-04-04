package com.aktivo.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.aktivo.MainActivity;
import com.aktivo.R;
import com.aktivo.activity.FullImageActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Methods {

    private static final String TAG = "IMAGE PATH";
    public static Context con;
    public static Activity activity;
    public static ProgressHUD mProgressHUD;

    private static Spannable sp_text;
    private static StyleSpan boldSpan;


    public Methods(Context con, Activity activity) {
        this.con = con;
        this.activity = activity;
    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    public void send_Loc_BroadCast(String BR_name,Context activity){
        try{
            LocalBroadcastManager.getInstance(activity).
                    sendBroadcast(new Intent(BR_name));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void send_Loc_BroadCast_intent(Context activity,Intent intent){
        try{
            LocalBroadcastManager.getInstance(activity).
                    sendBroadcast(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void send_Loc_BroadCast_intent2(Context activity,Intent intent){
        try{
            LocalBroadcastManager.getInstance(activity).
                    sendBroadcast(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void Reg_Loc_BroadCast(BroadcastReceiver BR_receiver, Activity activity, String br_name){
        try{
            LocalBroadcastManager.getInstance(activity).registerReceiver(BR_receiver,
                    new IntentFilter(br_name.toString()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void UnReg_Loc_BroadCast(BroadcastReceiver BR_name,Activity mActivity){
        try{
            LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(BR_name);
        }catch (Exception e){
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

    public static void loadFullImage(Context mContext, String img_position, String post_id, String is_from, boolean isToOpenProfilePic) {
        Intent intent = new Intent(mContext, FullImageActivity.class);
        intent.putExtra("POST_ID", post_id);
        intent.putExtra("IS_FROM", is_from);
        intent.putExtra("OPEN_PROFILE_PIC", isToOpenProfilePic);
        intent.putExtra("IMG_POSITION", img_position);
        mContext.startActivity(intent);
    }
    public static void navigationBarColor(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.black));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(Color.parseColor("#D86003"));
//                activity.getResources().getColor(R.color.colorPrimary)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int dpToPx(int dp, Activity activity) {
        try {
            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
            int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return px;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }


    public static void imageLoading(SimpleDraweeView simpleDraweeView, int width, int height, Uri uri) {

        int defaultWidth = 200;

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width == 0 ? defaultWidth : width, height == 0 ? defaultWidth : height))
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(simpleDraweeView.getController())
                .setImageRequest(request)
                .build();
        simpleDraweeView.setController(controller);


    }



    public static void ShowSnackbar(View v, String msg) {
        try {

            Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
            snackbar.setAction("Action", null).show();
            View snackbarView = snackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(13);
            textView.setTextColor(con.getResources().getColor(R.color.white));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidString(String s) {
        if (s != null && !s.isEmpty()) {
            return true;
        }

        return false;
    }

    public static boolean isValidEmail(String s) {
        if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
            return true;
        }

        return false;
    }

    public static boolean isValidWebAddress(String s) {
        if (Patterns.WEB_URL.matcher(s).matches()) {
            return true;
        }

        return false;
    }

    public static boolean isValidUniqueID(String s, int length) {
        if (s.toString().trim().length() >= length) {
            return true;
        }
        return false;
    }

    public static boolean isValidPassword(String s, int length) {

        if (s.toString().trim().length() >= length) {
            /*if(s.co)*/
            Pattern p = Pattern.compile("([0-9])");
            Matcher m = p.matcher(s);

            if (m.find()) {
                return true;
            }
        }
        return false;

    }

    public static boolean isStringMatch(String s1, String s2) {

        if (s1.equals(s2)) {
            return true;
        }
        return false;

    }

    public static boolean isStringMatchWithoutCase(String s1, String s2) {
        if (s1.equalsIgnoreCase(s2)) {
            return true;
        }

        return false;
    }


    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public Date DateFromMilliSeconds(Long milliseconds) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date time_date = cal.getTime();
            Date date = new Date(milliseconds);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void isProgressShow(Context mContext) {
        try {
            if (mProgressHUD == null) {
                mProgressHUD = ProgressHUD.show(mContext,
                        "Please wait", false, false, null);
            } else {
                if (!mProgressHUD.isShowing()) {
                    mProgressHUD = ProgressHUD.show(mContext,
                            "Please wait", false, false, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void isProgressShowMessage(Context mContext, final String message) {
        try {
            if (mProgressHUD == null) {
                mProgressHUD = ProgressHUD.show(mContext,
                        "" + message, false, false, null);
            } else {
                if (!mProgressHUD.isShowing()) {
                    mProgressHUD = ProgressHUD.show(mContext,
                            "" + message, false, false, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void isProgressShowNoMessage(Context mContext) {
        if (mProgressHUD == null) {
            mProgressHUD = ProgressHUD.show(mContext, null, false, false, null);
        } else {
            if (!mProgressHUD.isShowing()) {
                mProgressHUD = ProgressHUD.show(mContext, null, false, false,
                        null);
            }
        }
    }

    public void showToast(Activity activity,String msg){
        //Toast.makeText(activity,""+msg,Toast.LENGTH_SHORT).show();
    }

    public static void sendBroadCast(String BR_name, Activity activity) {
        try {
            Intent intent = new Intent(BR_name);
            activity.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendBroadCast(String BR_name, Context activity,int total_comments) {
        try {
            Intent intent = new Intent(BR_name);
            intent.putExtra("TOTAL_COMMENTS", total_comments);
            activity.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendBroadCast_new(String BR_name,String product_id,String total_quantity,Activity activity) {
        try {
            Intent intent = new Intent(BR_name);
            intent.putExtra("PRODUCT_ID",product_id);
            intent.putExtra("TOTAL_QUANTITY",total_quantity);
            activity.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void isProgressHide() {
        try {
            if (mProgressHUD != null) {

                if (mProgressHUD.isShowing()) {
                    mProgressHUD.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getDatefromTimestamp(String timeStamp) {
        try {
            long time_value = 0;
            if (timeStamp.toString().length() > 0) {
                time_value = Long.parseLong(timeStamp);
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date netDate = (new Date(time_value * 1000));
            Date oldDate = dateFormat.parse(dateFormat.format(netDate));
            String finalTime = null;
            Date currentDate = new Date();

            long diff = currentDate.getTime() - oldDate.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (oldDate.before(currentDate)) {
                Log.e("oldDate", "is previous date");
                Log.e("Difference: ", " seconds: " + seconds + " minutes: " + minutes
                        + " hours: " + hours + " days: " + days);

                if(hours<24)
                {
                    if(hours<1)
                    {
                        finalTime=minutes+" min. "+" ago";
                    }
                    else {
                        if (hours == 1) {
                            finalTime = hours + " hour" + " ago";
                        } else {
                            finalTime = hours + " hours" + " ago";
                        }
                    }

                }

                else {
                    if(days==1)
                    {
                        finalTime=days+" day"+" ago";
                    }
                    else {
                        finalTime=days+" days"+" ago";
                    }

                }

            }

            return finalTime;


        } catch (Exception ex) {

        }
        return "0";
    }
    public static String getDurationPost(String duration){

        String currentDateTime =duration ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String finalTime = null;
        try {

            Date oldDate = dateFormat.parse(currentDateTime);
            System.out.println("oldDate = " + oldDate);
            Date currentDate = new Date();
            System.out.println("currentDate = " + currentDate);
            long diff = currentDate.getTime() - oldDate.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (oldDate.before(currentDate)) {
                Log.e("oldDate", "is previous date");
                Log.e("Difference: ", " seconds: " + seconds + " minutes: " + minutes
                        + " hours: " + hours + " days: " + days);

                if(hours<24)
                {
                    if(hours<1)
                    {
                        if (minutes<1){
                            finalTime = "Just now"  ;
                        }else {
                            finalTime=minutes+" min. "+" ago";
                        }
                    }
                    else {
                        if (hours == 1) {
                            finalTime = hours + " hour" + " ago";
                        } else {
                            finalTime = hours + " hours" + " ago";
                        }
                    }
                }
                else {
                    if(days==1)
                    {
                        finalTime=days+" day"+" ago";
                    }
                    else {
                        finalTime=days+" days"+" ago";
                    }
                }
            }

        } catch (ParseException e) {
         Log.e("test","=>>>>>>>"+e.getLocalizedMessage());
            e.printStackTrace();
        }

        return finalTime;
    }

    public  static Date DateFromMilliSecondsNew(Long milliseconds){
        try{
            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date time_date = cal.getTime();
            Date date = new Date(milliseconds);
            return date;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String printDifferenceNew(Date date_small, Date date_Bigger) {
        try {
            if(date_small == null) {
                return "";
            } else if(date_Bigger == null) {
                return "";
            } else if(date_small != null && date_Bigger != null) {
                long e = date_Bigger.getTime() - date_small.getTime();
                long secondsInMilli = 1000L;
                long minutesInMilli = secondsInMilli * 60L;
                long hoursInMilli = minutesInMilli * 60L;
                long daysInMilli = hoursInMilli * 24L;
                long elapsedDays = e / daysInMilli;
                e %= daysInMilli;
                long elapsedHours = e / hoursInMilli;
                e %= hoursInMilli;
                long elapsedMinutes = e / minutesInMilli;
                e %= minutesInMilli;
                long elapsedSeconds = e / secondsInMilli;
                String time = "";
                int week = 0;
                System.out.println("elapsedDays = " + elapsedDays);
                System.out.println("elapsedDays%7 = " + Integer.valueOf(String.valueOf(elapsedDays/7))+"     " +elapsedDays);
                if(elapsedDays > 0L) {
                    if(elapsedDays == 1L) {
                        time = elapsedDays + " day ";
                    } else {
                        if (elapsedDays > 365L){
                            time = " Years ";
                        }else if (elapsedDays == 365L){
                            time = " Year ";
                        }else if (elapsedDays < 7L){
                            time = elapsedDays + " days ";
                        }else if (elapsedDays > 6L&&elapsedDays < 365L){

                            week = Integer.valueOf(String.valueOf(elapsedDays/7));
                            if (week==1){
                                time = Integer.valueOf(String.valueOf(elapsedDays/7)) +  " week";
                            }else if (week>1){
                                time = Integer.valueOf(String.valueOf(elapsedDays/7)) +  " weeks";
                            }
                        }
                    }

                    return time + " ago";
                } else if(elapsedHours > 0L) {
                    if(elapsedHours == 1L) {
                        time = elapsedHours + " Hour ";
                    } else {
                        time = elapsedHours + " Hours ";
                    }

                    return time + " ago";
                } else if(elapsedMinutes > 0L) {
                    if(elapsedMinutes == 1L) {
                        time = elapsedMinutes + " min ";
                    } else {
                        time = elapsedMinutes + " mins ";
                    }

                    return time + " ago";
                } else if(elapsedSeconds > 0L) {
                   /* if(elapsedSeconds == 1L) {
                        time = elapsedSeconds + " sec ";
                    } else {
                        time = elapsedSeconds + " secs ";
                    }*/

                    return "Just now";
                } else {
                    return "";
                }
            } else {
                return "";
            }
        } catch (Exception var22) {
            var22.printStackTrace();
            return "";
        }
    }




    public static String getDurationMedia(String duration) {

        String[] newDate=duration.split("T");

        String date=newDate[0];

        String oldtime=newDate[1];

        String time=newDate[newDate.length-1];

        System.out.println("only time"+time);

        String newTime[]=oldtime.split("\\+");

        String main=newTime[0];
        System.out.println("new time"+newTime[0]);
        String total=date+" "+main;
        System.out.println("------------- total time--------"+total);

        String currentDateTime =total ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String finalTime = null;
        try {

            Date oldDate = dateFormat.parse(currentDateTime);
            System.out.println("oldDate = " + oldDate);
            Date currentDate = new Date();
            System.out.println("currentDate = " + currentDate);
            long diff = currentDate.getTime() - oldDate.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (oldDate.before(currentDate)) {
                Log.e("oldDate", "is previous date");
                Log.e("Difference: ", " seconds: " + seconds + " minutes: " + minutes
                        + " hours: " + hours + " days: " + days);

                if(hours<24)
                {
                    if(hours<1)
                    {
                        finalTime=minutes+" min. "+" ago";
                    }
                    else {
                        if (hours == 1) {
                            finalTime = hours + " hour" + " ago";
                        } else {
                            finalTime = hours + " hours" + " ago";
                        }
                    }

                }

                else {
                    if(days==1)
                    {
                        finalTime=days+" day"+" ago";
                    }
                    else {
                        finalTime=days+" days"+" ago";
                    }

                }

            }

        } catch (ParseException e) {

            e.printStackTrace();
        }

        return finalTime;
    }


    public static void loadCentrifieddataCustomMethod(WebView view, String data, String data01, String data0, String data1, String data2) {

        /*text-align:justify;*/
        String youtContentStr = String.valueOf(Html
                .fromHtml("<![CDATA[<body style=\"text-align:justify;background-color:white; \">"
                        + "<font size=\"2\"  color=\"red\">" + data + "</font>"
                        + "<font size=\"2\"  color=\"red\" style=\"text-decoration:new\">" + "<br><br>" + data01 + "&nbsp;" + "</font>"
                        + "<font size=\"2\"  color=\"blue\" style=\"text-decoration:underline\">" + "<a href=\"https://stripe.com/\">" + data0 + "</a>" + "</font>"
                        + "<font size=\"2\" color=\"red\">" + data1 + "&nbsp;" + "</font>"
                        + "<font size=\"2\" color=\"blue\" style=\"text-decoration:underline\">" + data2 + "</font>"
                        + "</body>]]>"));

       /* String youtContentStr1 = String.valueOf(Html
                .fromHtml("<![CDATA[<body style=\"text-align:center;color:blue;background-color:white; \">"
                        + "" + data1
                        + "</body>]]>"));*/
        view.loadData(youtContentStr, "text/html", "utf-8");

    }


    public static void loadjustidata(TextView view, String data) {

        /*text-align:justify;*/
        String youtContentStr = String.valueOf(Html
                .fromHtml("<![CDATA[<body style=\"text-align:justify;background-color:white; \">"
                        + "<font size=\"2\"  color=\"black\">" + data + "</font>"
                        + "</body>]]>"));

       /* String youtContentStr1 = String.valueOf(Html
                .fromHtml("<![CDATA[<body style=\"text-align:center;color:blue;background-color:white; \">"
                        + "" + data1
                        + "</body>]]>"));*/
        view.setText(Html.fromHtml(youtContentStr));

    }


    public static void loadCentrifieddataCustom(WebView view, String data, String data01, String data02) {

        String youtContentStr = String.valueOf(Html
                .fromHtml("<![CDATA[<body style=\"text-align:justify;background-color:white; \">"
                        + "<font size=\"3\"  color=\"black\">" + data + "</font>"
                        + "<font size=\"1\"  color=\"black\" style=\"text-decoration:new\">" + "<br><br>" + data01 + "&nbsp;" + "</font>"
                        + "<font size=\"1\"  color=\"black\" style=\"text-decoration:new\">" + "<br><br>" + data02 + "&nbsp;" + "</font>"
                        /*+ "<font size=\"2\"  color=\"blue\" style=\"text-decoration:underline\">"+"<a href=\"https://stripe.com/\">"+data0+"</a>"+"</font>"
                        + "<font size=\"2\" color=\"red\">"+data1+"&nbsp;"+"</font>"
                        + "<font size=\"2\" color=\"blue\" style=\"text-decoration:underline\">"+data2+"</font>"*/
                        + "</body>]]>"));


       /* String youtContentStr1 = String.valueOf(Html
                .fromHtml("<![CDATA[<body style=\"text-align:center;color:blue;background-color:white; \">"
                        + "" + data1
                        + "</body>]]>"));*/

        view.loadData(youtContentStr, "text/html", "utf-8");

    }


    public void loadHtmlToWebview(String path, WebView webView) {

        try {
            webView.loadUrl("file:///android_asset/html/" + path);

            webView.setBackgroundColor(Color.TRANSPARENT);
            WebSettings webSettings = webView.getSettings();
            webSettings.setDefaultTextEncodingName("utf-8");
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                webSettings.setAllowFileAccessFromFileURLs(true);
                webSettings.setAllowUniversalAccessFromFileURLs(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }





    /*public  String handleError(View view,final RetrofitError error)
    {

        try{

            if (error != null)
            {
                if (error.getBody() != null) {
                    RestError body = (RestError) error.getBodyAs(RestError.class);

                    if (body.message != null) {
                        ShowSnackbar(view,body.message);
                        Common_Methods_ID common_methods_id = new Common_Methods_ID(activity);

                        if(body.message.contains("user is not active")||body.message.contains("user not register with us")||body.message.contains("You are no longer registered with Flightpic. Please register again")){
                            common_methods_id.showAlertDialog(activity, null, "", "", body.message);
                        }

                      *//*  if(body.message.contains("An account has been found under the same email address")){
                            common_methods_id.showAlertDialogLogin(activity,"","","","");
                        }*//*


                        return body.message;
                    }
                    else {
                        ShowSnackbar(view, "Request failed.");
                        //CommonMethods.toast("Request failed.", true);
                    }

                }
                else {
                    if (error.getMessage() != null) {
                        //CommonMethods.toast(error.getMessage(), true);
                        ShowSnackbar(view, error.getMessage());
                        Common_Methods_ID common_methods_id = new Common_Methods_ID(activity);
                        if(error.getMessage().contains("user is not active")||error.getMessage().contains("You are no longer registered with FlightPIC. Please register again")){
                            common_methods_id.showAlertDialog(activity, null, "", "", error.getMessage());
                        }
                        if(error.getMessage().contains("An account has been found under the same email address")){
                            common_methods_id.showAlertDialogLogin(activity,"","","","");
                        }

                        return error.getMessage();
                    }
                    else {
                        ShowSnackbar(view, error.getMessage());
                        //CommonMethods.toast(Commonmessage.NETWORK_ERROR, true);
                        return Commonmessage.network_error;
                    }
                }
            }}
        catch (Exception e)
        {
            e.printStackTrace();
            if (e instanceof NetworkErrorException)
            {
                System.out.println("ERROR NETWORK_ERROR == " + Commonmessage.network_error);
                ShowSnackbar(view, Commonmessage.network_error);
                return Commonmessage.network_error;
            }
            else {
                System.out.println("ERROR UNKNOWN_ERROR == " + Commonmessage.unknown_error);
               // CommonMethods.toast(Commonmessage.unknown_error, true);
                ShowSnackbar(view, Commonmessage.unknown_error);
                return Commonmessage.unknown_error;
            }
        }
        return Commonmessage.unknown_error;

    }*/

    public static int getWidth(Context context) {

        try {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            int width = display.getWidth();
            System.out.println("Width is ::::>>>>>" + width);
            return width;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    public String printDifference(Date date_small, Date date_Bigger) {

        try {

            if (date_small == null) {
                return "";
            }
            if (date_Bigger == null) {
                return "";
            }
            //milliseconds
            if (date_small == null || date_Bigger == null) {
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


            String time = "";

            if (elapsedDays > 0) {


                if (elapsedDays == 1) {
                    time = elapsedDays + " Day ";
                } else {
                    time = elapsedDays + " Days ";
                }
                return time + " ago";
            }

            if (elapsedHours > 0) {


                if (elapsedHours == 1) {
                    time = elapsedHours + " Hr ";
                } else {
                    time = elapsedHours + " Hrs ";
                }
                return time + " ago";
            }

            if (elapsedMinutes > 0) {


                if (elapsedMinutes == 1) {
                    time = elapsedMinutes + " Min ";
                } else {
                    time = elapsedMinutes + " Mins ";
                }
                return time + " ago";
            }

            if (elapsedSeconds > 0) {


                if (elapsedSeconds == 1) {
                    time = elapsedSeconds + " Sec ";
                } else {
                    time = elapsedSeconds + " Secs ";
                }
                return time + " ago";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return "";
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
    public static Bitmap retriveVideoFrameFromVideo(String videoPath) {
        Bitmap bitmap = null;
        try {
            bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }



    public static void Create_Save_Bitmap(Bitmap bitmapPicture,
                                          String foldername, String filename) {
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
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void storeImage(Bitmap image,MainActivity mainActivity) {

        File pictureFile = getOutputMediaFile(mainActivity);
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }
    /** Create a File for saving an image or video */
    public  File getOutputMediaFile(MainActivity mainActivity){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + mainActivity.getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public static File Create_Save_Bitmap_(Bitmap bitmapPicture,
                                          String foldername, String filename) {
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
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                "FlightPic/flightpic_img.png");

        if(file!=null){
            return file;
        }else{
            return null;
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


            Create_Save_Bitmap(bmpimg, "FlightPic", "user_img" + "" + ".png");

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static InputStream getConnection(String url) {

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




    /*public static void callDefaultSharing(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String sAux = "\nLet me recommend you this application\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {
            //e.toString();
            e.printStackTrace();
        }
    } */

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

    public static void showAlertDialogCommon(final Activity activity, final String title, final String positiveButtonText, final String negativeButtonText, final String message) {

        try {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    /*title = activity.getString(R.string.logout);*/
                    if (title != null) {
                   /*     if (Methods.isValidString(title)) {
                            builder.setTitle(title);
                        }*/
                    }

                    builder.setCancelable(false);
                    builder.setMessage(message)
                            .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            }).setNegativeButton(negativeButtonText/*activity.getString(R.string.cancel)*/, new DialogInterface.OnClickListener() {
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

    }


  /*  public static boolean getPermissionCheckMultiple(final MainActivity mActivity, final List<PermissionCheck> list_of_permission) {

        int hasReadWritePermission = 0;
        for (int i = 0; i <list_of_permission.size() ; i++) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hasReadWritePermission = mActivity.checkSelfPermission(String.valueOf(list_of_permission.get(i).getpermissionName()));

                if (hasReadWritePermission != PackageManager.PERMISSION_GRANTED) {

                    final String permission = list_of_permission.get(i).getpermissionName();
                    final int requestCode = list_of_permission.get(i).getRequestCode();

                    if (!mActivity.shouldShowRequestPermissionRationale(String.valueOf(list_of_permission.get(i).getpermissionName()))) {

                        showMessageOKCancel(mActivity,list_of_permission.get(i).getpermissionMessage(),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            mActivity.requestPermissions(new String[] {String.valueOf(permission)},
                                                    requestCode);
                                        }

                                    }
                                });

                        return false;
                    }
                    mActivity.requestPermissions(new String[] {String.valueOf(list_of_permission.get(i).getpermissionName())},
                            list_of_permission.get(i).getRequestCode());
                    return false;
                }
            }
        }

        //insertDummyContact();
        return true;

    }*/


    public static void showMessageOKCancel(MainActivity mActivity, String message, DialogInterface.OnClickListener okListener) {

        android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(mActivity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create();
        alertDialog.setCancelable(false);
        alertDialog.show();

    }



    public static int getSeekBarSeekValue(String value, int progress){

        if (value != null) {
            final String CurrentString = value;
            String[] separatedString = CurrentString.split(":");
            final String min_ = separatedString[0];
            final String sec_ = separatedString[1];
            System.out.println(min_ + " >>Mins" + "\n" + sec_ + " >>Seconds");

            int total_seconds = (Integer.parseInt(min_) * 60) + Integer.parseInt(sec_);
            int elapsed_seconds = (total_seconds * progress) / 100;
            System.out.println("elapsed_seconds = " + elapsed_seconds);

            return elapsed_seconds;
        }
        return 0;
    }

    public static void shareImages(final Context mContext, String image, final String extraText){

   /*     final String text;
        text = extraText == null?"":extraText;

        try {

            Picasso.with(mContext).load(image).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(mContext, bitmap));
                    i.putExtra(Intent.EXTRA_TEXT, text);
                    i.setType("image*//*");
                    mContext.startActivity(Intent.createChooser(i, "Share Using"));
                }
                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });

        } catch(Exception e) {
            //e.toString();
            e.printStackTrace();
        }*/
    }

    public static Uri getLocalBitmapUri(Context mContext, Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public static void shareText(Context mContext, String extraText){
        try {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/html");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<p>"+extraText+"</p>"));
            mContext.startActivity(Intent.createChooser(sharingIntent, "Share using"));
        } catch(Exception e) {
            //e.toString();
            e.printStackTrace();
        }
    }

    public static void shareVideo(Context mContext, String video_url, String extraText){

        final String text;
        text = extraText == null?"":extraText;

        try {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, text + "\n" + video_url);
            mContext.startActivity(Intent.createChooser(sharingIntent, "Share using"));

        } catch(Exception e) {
            //e.toString();
            e.printStackTrace();
        }
    }

    public static void shareAudio(Context mContext, String audio_url, String extraText){

        final String text;
        text = extraText == null?"":extraText;
        try {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, text + "\n" + audio_url);
            mContext.startActivity(Intent.createChooser(sharingIntent, "Share using"));
        } catch(Exception e) {
            //e.toString();
            e.printStackTrace();
        }
    }

    public static Spannable setSpanText(String textToSpan, int spanLength){
        sp_text = new SpannableString(textToSpan);
        boldSpan = new StyleSpan(Typeface.BOLD);
        sp_text.setSpan(boldSpan, 0, spanLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp_text.setSpan(new ForegroundColorSpan(Color.parseColor("#3c3c41")), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/Gilroy-Bold.ttf");
        sp_text.setSpan(new TypefaceSpan(String.valueOf(font)), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp_text;
    }

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


}
