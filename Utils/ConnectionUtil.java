package com.aktivo.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.aktivo.MainActivity;


/**
 * Created by mind on 2/10/15.
 */
public class ConnectionUtil {


    /**
     * Checks if the Internet connection is available.
     *
     * @return Returns true if the Internet connection is available. False otherwise.
     * *
     */
    public static boolean isInternetAvailable(MainActivity ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        // if network is NOT available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        else
        {
            ctx.showCroutonsMessage(ctx, "No Internet Connection");
            return false;
        }
        //Toast.makeText(ctx,"No internet connection",Toast.LENGTH_SHORT).show();

    }
    public static boolean isInternetAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        // if network is NOT available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        else
        {

                Toast.makeText(ctx,"Err, looks like you’re offline. Please enable your Internet connection to continue.",Toast.LENGTH_SHORT).show();


            return false;
        }


       // Toast.makeText(ctx,Utility.getStringSharedPreferences(ctx,StringDifferentLanguage.NO_INTERNET_CONNECTION),Toast.LENGTH_SHORT).show();
        //Toast.makeText(ctx,"No internet connection",Toast.LENGTH_SHORT).show();
    }

}
