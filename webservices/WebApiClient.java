package com.aktivo.webservices;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import okhttp3.logging.HttpLoggingInterceptor;

public class WebApiClient {

    private WebApi webApi;
    public static WebApiClient webApiClient;
    private static Context mcontext;
    public static Retrofit retrofit_new = null;

    /*public static String version = "2";*/
//okHttpClient.setReadTimeout(60 * 1000, TimeUnit.MILLISECONDS);
    public static WebApiClient getInstance(Context context) {

        if (webApiClient == null)
            webApiClient = new WebApiClient();
            mcontext = context;
        return webApiClient;

    }

    private WebApiClient() {
     //just an empty constructor for now
    }


    public WebApi getWebApi() {

        System.setProperty("http.keepAlive", "false");

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request newRequest = chain.request().newBuilder()
                       // .addHeader("Content-Type","application/x-www-form-urlencoded")
                        .build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient client = new OkHttpClient.
                Builder().
                addInterceptor(logging).
                addInterceptor(interceptor).
                readTimeout(30, TimeUnit.SECONDS).
                connectTimeout(30, TimeUnit.SECONDS).
                build();
        client.connectTimeoutMillis();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        retrofit_new = retrofit;

        webApi = retrofit.create(WebApi.class);

        return webApi;

    }


    public WebApi getWebApi_without_new() {

        System.setProperty("http.keepAlive", "false");

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request newRequest = chain.request().newBuilder()
                        .addHeader("Content-Type","application/x-www-form-urlencoded")
                        .build();
                return chain.proceed(newRequest);
            }
        };

        OkHttpClient client = new OkHttpClient.
                Builder().
                addInterceptor(logging).
                addInterceptor(interceptor).
                build();

        client.connectTimeoutMillis();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        retrofit_new = retrofit;
        webApi = retrofit.create(WebApi.class);

        return webApi;

    }



  /*  public WebApi getWebApi_with_Token(){

        try{

          //  System.setProperty("http.keepAlive", "false");

            if(Common_Methods.getLoginUser()!=null){
                token = Common_Methods.getLoginUser().getToken();
                *//*if(Common_Methods.getLoginUser().getCurrent_lat()!=null){
                    latLong = Common_Methods.getLoginUser().getCurrent_lat()+","+Common_Methods.getLoginUser().getCurrent_lng();
                }*//*
                String latlng_new = MyPreferences.getPref(mcontext,"CURRENT_LAT_LNG");
                latLong = latlng_new==null?"0,0":latlng_new;
                latLong = latlng_new.length()==0?"0,0":latlng_new;

            }

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            System.out.println("TOKEN"+token);
            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

                    Request newRequest = chain.request().newBuilder()
                            .removeHeader("Authorization")
                            .addHeader("Content-Type","application/x-www-form-urlencoded")
                            .addHeader("x-user-location",latLong)
                            .addHeader("Authorization", "Bearer "+token).build();
                    return chain.proceed(newRequest);
                }
            };
            httpClient.addInterceptor(interceptor);
            httpClient.addInterceptor(logging);

            OkHttpClient client = httpClient.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WebApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            retrofit_new = retrofit;

            webApi = retrofit.create(WebApi.class);
            return webApi;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }*/


    /*
<<<<<<< Updated upstream
*/

   /* public WebApi getWebApi_another1(){
=======

    public WebApi getWebApi_another1(){
>>>>>>> Stashed changes

        try{
            System.setProperty("http.keepAlive", "false");

            if(Common_Methods.getLoginUser()!=null){
               token = Common_Methods.getLoginUser().getToken();
            }

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            System.out.println("TOKEN"+token);
            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

                    Request newRequest = chain.request().newBuilder()
                            .removeHeader("Token")
                            .addHeader("Token", token).build();
                    return chain.proceed(newRequest);
                }
            };
            httpClient.addInterceptor(interceptor);
            httpClient.addInterceptor(logging);




            OkHttpClient client = httpClient.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WebApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            webApi = retrofit.create(WebApi.class);
            return webApi;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }*/




}
