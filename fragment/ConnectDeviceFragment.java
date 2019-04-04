package com.aktivo.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.Methods;
import com.aktivo.Utils.MyPreferences;
import com.aktivo.response.BaseCommanRespons;
import com.aktivo.response.ConnectedTypeResponse;
import com.aktivo.response.MarketUrlResponse;
import com.aktivo.response.UserDetailTable;
import com.aktivo.webservices.WebApiClient;
import com.commonmodule.mi.utils.Validation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.text.DateFormat.getDateInstance;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConnectDeviceFragment extends BaseFragment implements View.OnClickListener {

//    @BindView(R.id.recyclview)
//    RecyclerView recyclerView;
//    @BindView(R.id.tv_connected_devices)
//    TextView tv_connected_devices;
//    @BindView(R.id.tv_staus)
//    TextView tv_staus;
//    @BindView(R.id.tv_device)
//    TextView tv_device;
//    private ConnectDeviceAdaptor connectDeviceAdaptor;
//    List<ConnectDevice> deviceList;

    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.img3)
    ImageView img3;


    @BindView(R.id.tvConnectGfit)
    TextView tvConnectGfit;
    @BindView(R.id.tvConnectAktivo)
    TextView tvConnectAktivo;
    @BindView(R.id.tvConnectAktivoDesc)
    TextView tvConnectAktivoDesc;
    @BindView(R.id.tvTrackwithOtherDevice)
    TextView tvTrackwithOtherDeviceDesc;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_aktivo)
    TextView tv_aktivo;

    String success_full_synchronize = "Successfully synchronized with <b>GoogleFit</b>.";


    public static final String TAG = "StepCounter";
    ArrayList<String> activitytype = new ArrayList<>();
    // For Activity
    ArrayList<String> Activity;
    ArrayList<String> ActivityCreatedDate;
    ArrayList<String> ActivityCreatedTime;

    // For Light Activity
    ArrayList<String> LightActivity;
    ArrayList<String> LightActivityCreatedDate;
    ArrayList<String> LightActivityCreatedTime;

    // For SB
    ArrayList<String> SB;
    ArrayList<String> SBCreatedDate;
    ArrayList<String> SBCretedTime;

    // For Filter Activity

    ArrayList<String> FilterActivity;
    ArrayList<String> FilterActivityCreatedDate;
    ArrayList<String> FilterActivityCreatedTime;

    // For Filter Light Activity

    ArrayList<String> FilterLA;
    ArrayList<String> FilterLACreatedDate;
    ArrayList<String> FilterLACreatedTime;

    // For Filter SB
    ArrayList<String> FilterSB;
    ArrayList<String> FilterSBCreatedDate;
    ArrayList<String> FilterSBCreatedTime;

    // for Calories
    ArrayList<String> Calories = new ArrayList<>();
    ArrayList<String> CaloriesDate = new ArrayList<>();
    ArrayList<String> Caloriestime = new ArrayList<>();

    // for Steps
    ArrayList<String> Steps = new ArrayList<>();
    ArrayList<String> StepDate = new ArrayList<>();
    ArrayList<String> StepTime = new ArrayList<>();

    // for Filter Calories

    ArrayList<String> FilterCalories = new ArrayList<>();
    ArrayList<String> FilterCaloriesDate = new ArrayList<>();
    ArrayList<String> FilterCaloriestime = new ArrayList<>();

    // for Steps
    ArrayList<String> FilterSteps = new ArrayList<>();
    ArrayList<String> FilterStepDate = new ArrayList<>();
    ArrayList<String> FilterStepTime = new ArrayList<>();

    ArrayList<String> SleepTime = new ArrayList<>();
    ArrayList<String> SleepDate = new ArrayList<>();

    ArrayList<String> FilterSleepDate = new ArrayList<>();
    ArrayList<String> FilterSleepTime = new ArrayList<>();

    ArrayList<String> allactivity = new ArrayList<>();
    ArrayList<String> allactivitydate = new ArrayList<>();
    ArrayList<String> allactivitytime = new ArrayList<>();
    ArrayList<String> txtactivity = new ArrayList<>();


    ArrayList<String> SleepBand = new ArrayList<>();
    ArrayList<String> SleepBandTime = new ArrayList<>();
    ArrayList<String> SleepBandDate = new ArrayList<>();
    ArrayList<String> FilterSleepBandTime = new ArrayList<>();
    ArrayList<String> FilterSleepBandDate = new ArrayList<>();

    ArrayList<String> FilterNewTime = new ArrayList<>();
    ArrayList<String> FilterNewDate = new ArrayList<>();

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    public static GoogleApiClient mClient = null;

    ArrayList<String> allDays = new ArrayList<>();
    private static final String AUTH_PENDING = "auth_state_pending";
    private static final int REQUEST_OAUTH = 1;

    String[] permissionsRequired = new String[]{Manifest.permission.BODY_SENSORS};
    boolean flag = false;

    public String getBedTime = "", getWakupTime = "";
    long diffbedMinutes = 0;
    long diffwakupMinutes = 0;

    String user_id = "";
    UserDetailTable userDetailTable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.connect_device_fragment, container, false);
        return inflater.inflate(R.layout.connect_device_old_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setHeader();
        userDetailTable = UserDetailTable.getUserDetail();
        if (userDetailTable != null) {
            if (Validation.isRequiredField(userDetailTable.get_id())) {
                user_id = userDetailTable.get_id();
            }
            if (Validation.isRequiredField(userDetailTable.getBadtime())) {
                getBedTime = userDetailTable.getBadtime();

            }
            if (Validation.isRequiredField(userDetailTable.getWakeup())) {
                getWakupTime = userDetailTable.getWakeup();
            }
        }

        Log.e("test", "getBedTime=>" + getBedTime + "getWakupTime=>" + getWakupTime);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date2 = null, date3 = null;
        try {
            date2 = format.parse(getBedTime);
            date3 = format.parse(getWakupTime);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        if (date2.before(date3)) {

            long diff = date3.getTime() - date2.getTime();
            diffwakupMinutes = diff / (60 * 1000);
            diffbedMinutes = 0;

            Log.e("get diff bed minutes", diffwakupMinutes + "");

        } else {
            try {
                Date date1 = format.parse("23:59:59");
                long diff = date1.getTime() - date2.getTime();
                long diffSeconds = diff / 1000;
                diffbedMinutes = diff / (60 * 1000);


                Date date4 = format.parse("00:00:00");
                long diffwakup = date3.getTime() - date4.getTime();
                long diffwakupSeconds = diffwakup / 1000;
                diffwakupMinutes = diffwakup / (60 * 1000);
                Log.e("get diff bed minutes", diffbedMinutes + ":::" + diffwakupMinutes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mActivity.tagmanager("Settings Connected Devices Screen", "settings_connected_devices_view");
    }


    private void setHeader() {
        mActivity.seletect_tab(CommonKeys.CLEAR_TAB);
        mActivity.setToolbarTopVisibility(false);
        mActivity.setToolbarBottomVisibility(true);
        tv_aktivo.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_title.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        mActivity.enableDrawer();
        if (Validation.isRequiredField(common_methods.getTodayHaveData().getConnected_devices_background_image())) {
            mActivity.setBackgroudnImage(common_methods.getTodayHaveData().getConnected_devices_background_image());
        }
        ///mActivity.setBackgroudnImage("http://1h23on8hs5s44c2iqm9tze2x.wpengine.netdna-cdn.com/wp-content/uploads/2014/02/Studio-5.jpg");
        // mActivity.setBackgroudnImage(common_methods.getBackgroundImage(mActivity,CommonKeys.BACKGROUND_MAIN));

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setHeader();

        }

    }

    @OnClick({R.id.img1, R.id.img2, R.id.img3, R.id.iv_menu, R.id.iv_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                mActivity.onBackPressed();
                break;
            case R.id.img1:
                mActivity.tagmanager("Connect with Google Fit", "settings_connected_devices_gf_click");
                 /* long millis = System.currentTimeMillis();
                  long seconds = millis/1000;*/
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date()); /* whatever*/
                //c.setTimeZone(...); if necessary
                long long_data = System.currentTimeMillis();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                //  format.setCalendar(calendar);
                Date date = calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                // Log.e("test","seconds===>>>"+long_data);
                MyPreferences.setPref(mActivity, CommonKeys.USER_CURRENT_TIME_DATA, String.valueOf(sdf.format(date)));
                fitInstalled();
//                MyPreferences.setPref(mActivity, "ConnectedType", "google_fit");      //store ConnectedType
                break;
            case R.id.img2:
                break;
            case R.id.img3:
                CommonKeys.IS_GOOGLE_FIT_SYNCHRONIZE = false;
                //        MyPreferences.setPref(mActivity, "ConnectedType", "validic");      //store ConnectedType
//                mActivity.pushFragmentDontIgnoreCurrent(new WebviewLoadUrlFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                CreateValidicUser(user_id);
                break;
            case R.id.iv_menu:
                mActivity.openDrawer();
                break;
            default:
                break;
        }

    }

    @CheckResult
    public boolean fitInstalled() {
        try {
            getActivity().getPackageManager().getPackageInfo("com.google.android.apps.fitness", PackageManager.GET_ACTIVITIES);
            Log.e("Return", "True");
            buildFitnessClient();
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Return", "False");
            common_methods.setCutemDialogMessage(mActivity, "To continue, please download Google Fit from the Play Store.");
            //Toast.makeText(getActivity(), "Please install Google-Fit", Toast.LENGTH_LONG).show();
            mActivity.tagmanager("Google Fit Sync Error Sync popup", "settings_connected_devices_gf_sync_error");

            return false;
        }
    }

    private void buildFitnessClient() {
        try {
/*
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestScopes(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE), new Scope(Scopes.FITNESS_LOCATION_READ))
                    .build();*/
            // Create the Google API Client
            mClient = new GoogleApiClient.Builder(getActivity())
                    // .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addApi(Fitness.HISTORY_API)
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                    .addScope(new Scope(Scopes.FITNESS_BODY_READ))
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                    .addConnectionCallbacks(
                            new GoogleApiClient.ConnectionCallbacks() {
                                @Override
                                public void onConnected(Bundle bundle) {
                                    Log.i(TAG, "Connected!!!");
                                    // Now you can make calls to the Fitness APIs.  What to do?
                                    // Look at some data!!
                                    new VerifyDataTask().execute();
                                }

                                @Override
                                public void onConnectionSuspended(int i) {
                                    // If your connection to the sensor gets lost at some point,
                                    // you'll be able to determine the reason and react to it here.
                                    if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                        Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                                    } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                        Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
                                    }
                                }
                            }
                    )
                    .enableAutoManage(getActivity(), 0, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                            Log.i(TAG, "Google Play services connection failed. Cause: " +
                                    result.toString());
                            common_methods.setCutemDialogMessage(mActivity, "Oops, looks like we’re unable to connect to Google Fit. We’ve called in the experts! Please try again soon.");

                           /* common_methods.setCutemDialogMessage(mActivity, "Google Play services connection failed. Cause: " +
                                    result.toString());*/
                            /*Toast.makeText(getActivity(), "Google Play services connection failed. Cause: " +
                                    result.toString(), Toast.LENGTH_LONG).show();*/
                            mActivity.tagmanager("Google Fit Sync Error Sync popup", "settings_connected_devices_gf_sync_error");

                        }
                    })
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class VerifyDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            // Begin by creating the query.
            DataReadRequest readRequest = queryFitnessData();

            DataReadRequest readRequest1 = stepqueryFitnessData();

            // [START read_dataset]
            // Invoke the History API to fetch the data with the query and await the result of
            // the read request.
            DataReadResult dataReadResult =
                    Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);
            // [END read_dataset]

            // for steps
            DataReadResult stepdataReadResult =
                    Fitness.HistoryApi.readData(mClient, readRequest1).await(1, TimeUnit.MINUTES);

            // For the sake of the sample, we'll print the data so we can see what we just added.
            // In general, logging fitness information should be avoided for privacy reasons.

            stepprintData(stepdataReadResult);
            printData(dataReadResult);
            return null;
        }

    }

    public static DataReadRequest queryFitnessData() {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        DateFormat dateFormat = getDateInstance();
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataSource ACTIVITY_SEGMENT = new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_activity_segment")
                .setAppPackageName("com.google.android.gms")
                .build();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(ACTIVITY_SEGMENT, DataType.AGGREGATE_STEP_COUNT_DELTA)
                //  .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                //  .aggregate(DataType.TYPE_HEART_RATE_BPM, DataType.AGGREGATE_HEART_RATE_SUMMARY)
                .aggregate(DataType.TYPE_ACTIVITY_SEGMENT, DataType.AGGREGATE_ACTIVITY_SUMMARY)
                .bucketByActivitySegment(1, TimeUnit.SECONDS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        // [END build_read_data_request]
        return readRequest;
    }
// for steps

    public static DataReadRequest stepqueryFitnessData() {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);

        long endTime = cal.getTimeInMillis();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.WEEK_OF_YEAR, -1);

        long startTime = cal.getTimeInMillis();

        String StartDate = new SimpleDateFormat("yyyy-MM-dd").format(startTime);
        String EndDate = new SimpleDateFormat("yyyy-MM-dd").format(endTime);

        // [END build_read_data_request]

        DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .setAppPackageName("com.google.android.gms")
                .build();

        DataReadRequest readRequest1 = null;
//        readRequest1 = new DataReadRequest.Builder()
//                //  .aggregate(ESTIMATED_STEP_DELTAS, DataType.AGGREGATE_STEP_COUNT_DELTA)
//                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
//                .bucketByTime(1, TimeUnit.DAYS)
//                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
//
// .setTimeRange(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(StartDate + " " + "00:00:00").getTime(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(EndDate + " " + "23:59:59").getTime(), TimeUnit.MILLISECONDS)
//                .build();


        //exsisting code

        try {
            readRequest1 = new DataReadRequest.Builder()
                    .aggregate(ESTIMATED_STEP_DELTAS, DataType.AGGREGATE_STEP_COUNT_DELTA)
                    //   .aggregate(DataType.TYPE_CALORIES_EXPENDED,DataType.AGGREGATE_CALORIES_EXPENDED)
                    .bucketByTime(1, TimeUnit.DAYS)
                    //  .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                    .setTimeRange(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(StartDate + " " + "00:00:00").getTime(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(EndDate + " " + "23:59:59").getTime(), TimeUnit.MILLISECONDS)
                    .build();
            //  return readRequest1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return readRequest1;
    }

    // for steps

    public void stepprintData(DataReadResult dataReadResult) {

        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: "
                    + dataReadResult.getBuckets().size());

            for (final Bucket bucket : dataReadResult.getBuckets()) {
                //  Log.i(TAG, bucket.getActivity());

                final List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {

                    for (DataPoint dp : dataSet.getDataPoints()) {

                        String scStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dp.getStartTime(TimeUnit.MILLISECONDS));
                        String scEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dp.getEndTime(TimeUnit.MILLISECONDS));

                        for (Field field : dp.getDataType().getFields()) {
                            Log.e(TAG, "\tField: " + field.getName() +
                                    " Value: " + dp.getValue(field));
                            Log.e("date", scStartDate + "---" + scEndDate);
                            Steps.add(dp.getValue(field).toString());
                            StepDate.add(scStartDate.substring(0, scStartDate.indexOf(' ')));
                            StepTime.add(scStartDate.substring(scStartDate.indexOf(' ') + 1));

                        }
                    }

                }
            }

        }

    }

    public void printData(DataReadResult dataReadResult) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.

        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        Calendar cal = Calendar.getInstance();
        // get starting date
        cal.add(Calendar.DAY_OF_YEAR, -8);

        // loop adding one day in each iteration
        for (int i = 0; i < 8; i++) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            //     System.out.println(sdf.format(cal.getTime()));
            allDays.add(sdf.format(cal.getTime()));
        }

        Log.e("get dataresult size", dataReadResult.getBuckets().size() + "");

        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(TAG, "Number of returned buckets of DataSets is: "
                    + dataReadResult.getBuckets().size());

            Activity = new ArrayList<String>();
            ActivityCreatedDate = new ArrayList<String>();
            ActivityCreatedTime = new ArrayList<String>();

            LightActivity = new ArrayList<String>();
            LightActivityCreatedDate = new ArrayList<String>();
            LightActivityCreatedTime = new ArrayList<String>();

            SB = new ArrayList<String>();
            SBCreatedDate = new ArrayList<String>();
            SBCretedTime = new ArrayList<String>();

            SleepTime = new ArrayList<>();
            SleepDate = new ArrayList<>();


            for (final Bucket bucket : dataReadResult.getBuckets()) {
                //  Log.i(TAG, bucket.getActivity());

                String StartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bucket.getStartTime(TimeUnit.MILLISECONDS));
                String EndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bucket.getEndTime(TimeUnit.MILLISECONDS));

                long activeTime = bucket.getEndTime(TimeUnit.MINUTES) - bucket.getStartTime(TimeUnit.MINUTES);
//                Log.e("Activity start date", StartDate);
//                Log.e("Activity end date", EndDate);
//                Log.e("Activity time", activeTime + "");
//                Log.e("Activity", bucket.getActivity());

                txtactivity.add("startdate>>" + " " + StartDate + ":::" + "enddate" + ":::" + EndDate + ":::" + bucket.getActivity() + ":::" + activeTime + "\n");

                if (!bucket.getActivity().equalsIgnoreCase("still")
                        && !bucket.getActivity().equalsIgnoreCase("sleep.light")
                        && !bucket.getActivity().equalsIgnoreCase("sleep.awake")
                        && !bucket.getActivity().equalsIgnoreCase("sleep.deep")
                        && !bucket.getActivity().equalsIgnoreCase("rem.sleep")
                        && !bucket.getActivity().equalsIgnoreCase("sleep") &&
                        !bucket.getActivity().equalsIgnoreCase("unknown") &&
                        !bucket.getActivity().equalsIgnoreCase("tilting")
                        && !bucket.getActivity().equalsIgnoreCase("on_foot")
                        && !bucket.getActivity().equalsIgnoreCase("in_vehicle") && activeTime >= 10 &&
                        StartDate.substring(0, StartDate.indexOf(' ')).equalsIgnoreCase(EndDate.substring(0, EndDate.indexOf(' ')))) {
                    allactivity.add(String.valueOf(activeTime));
                    allactivitydate.add(StartDate.substring(0, StartDate.indexOf(' ')));
                    allactivitytime.add(StartDate.substring(StartDate.indexOf(' ') + 1));
                    activitytype.add(bucket.getActivity());

                }
                // Log.e("get all activity", allactivity + ":::" + allactivitydate + ":::" + allactivitytime + ":::" + activitytype);
                if (!bucket.getActivity().equalsIgnoreCase("still") &&
                        !bucket.getActivity().equalsIgnoreCase("unknown") &&
                        !bucket.getActivity().equalsIgnoreCase("tilting")
                        && !bucket.getActivity().equalsIgnoreCase("sleep.light")
                        && !bucket.getActivity().equalsIgnoreCase("sleep.awake")
                        && !bucket.getActivity().equalsIgnoreCase("sleep.deep")
                        && !bucket.getActivity().equalsIgnoreCase("rem.sleep")
                        && !bucket.getActivity().equalsIgnoreCase("sleep")
                        && !bucket.getActivity().equalsIgnoreCase("on_foot")
                        && !bucket.getActivity().equalsIgnoreCase("in_vehicle") && activeTime >= 10 &&
                        StartDate.substring(0, StartDate.indexOf(' ')).equalsIgnoreCase(EndDate.substring(0, EndDate.indexOf(' ')))) {
                    Activity.add(String.valueOf(activeTime));
                    ActivityCreatedDate.add(StartDate.substring(0, StartDate.indexOf(' ')));
                    ActivityCreatedTime.add(StartDate.substring(StartDate.indexOf(' ') + 1));

                }

                if (!bucket.getActivity().equalsIgnoreCase("still")
                        && !bucket.getActivity().equalsIgnoreCase("in_vehicle") &&
                        !bucket.getActivity().equalsIgnoreCase("unknown") &&
                        !bucket.getActivity().equalsIgnoreCase("tilting")
                        && !bucket.getActivity().equalsIgnoreCase("sleep.light")
                        && !bucket.getActivity().equalsIgnoreCase("sleep.awake")
                        && !bucket.getActivity().equalsIgnoreCase("sleep.deep")
                        && !bucket.getActivity().equalsIgnoreCase("rem.sleep")
                        && !bucket.getActivity().equalsIgnoreCase("sleep")
                        && activeTime < 10 &&
                        StartDate.substring(0, StartDate.indexOf(' ')).equalsIgnoreCase(EndDate.substring(0, EndDate.indexOf(' ')))) {
                    LightActivity.add(String.valueOf(activeTime));
                    LightActivityCreatedDate.add(StartDate.substring(0, StartDate.indexOf(' ')));
                    LightActivityCreatedTime.add(StartDate.substring(StartDate.indexOf(' ') + 1));
                }

                if (bucket.getActivity().equalsIgnoreCase("on_foot") && StartDate.substring(0, StartDate.indexOf(' ')).equalsIgnoreCase(EndDate.substring(0, EndDate.indexOf(' ')))) {
                    LightActivity.add(String.valueOf(activeTime));
                    LightActivityCreatedDate.add(StartDate.substring(0, StartDate.indexOf(' ')));
                    LightActivityCreatedTime.add(StartDate.substring(StartDate.indexOf(' ') + 1));
                }

                if (bucket.getActivity().equalsIgnoreCase("unknown") && StartDate.substring(0, StartDate.indexOf(' ')).equalsIgnoreCase(EndDate.substring(0, EndDate.indexOf(' ')))) {
                    SB.add(String.valueOf(activeTime));
                    SBCreatedDate.add(StartDate.substring(0, StartDate.indexOf(' ')));
                    SBCretedTime.add(StartDate.substring(StartDate.indexOf(' ') + 1));
                }

                if (StartDate.substring(0, StartDate.indexOf(' ')).equalsIgnoreCase(EndDate.substring(0, EndDate.indexOf(' ')))) {
                    if (bucket.getActivity().equalsIgnoreCase("sleep.deep") || bucket.getActivity().equalsIgnoreCase("sleep.awake") ||
                            bucket.getActivity().equalsIgnoreCase("rem.sleep") || bucket.getActivity().equalsIgnoreCase("sleep") ||
                            bucket.getActivity().equalsIgnoreCase("sleep.light")) {
                        SleepBand.add(String.valueOf(activeTime));
                        SleepBandDate.add(StartDate.substring(0, StartDate.indexOf(' ')));
                        SleepBandTime.add(StartDate.substring(StartDate.indexOf(' ') + 1));
                    }

                }

                long diffMinutes = 0;
                long diffMinutes1 = 0;

                long diffMinutes3 = 0;
                long diffMinutes4 = 0;
                if (bucket.getActivity().equalsIgnoreCase("still") &&
                        StartDate.substring(0, StartDate.indexOf(' ')).equalsIgnoreCase(EndDate.substring(0, EndDate.indexOf(' ')))) {
                   /* SB.add(String.valueOf(activeTime));
                    SBCreatedDate.add(StartDate.substring(0, StartDate.indexOf(' ')));
                    SBCretedTime.add(StartDate.substring(StartDate.indexOf(' ') + 1));*/
                    String starttime = StartDate.substring(StartDate.indexOf(' ') + 1, StartDate.indexOf(":")).trim();
                    String endtime = EndDate.substring(EndDate.indexOf(' ') + 1, EndDate.indexOf(":")).trim();
                    //  Log.e("get start time", starttime + "::::" + endtime);


                    // logic for differnt date Sleep
                    String starttime_str = StartDate.substring(StartDate.indexOf(' ') + 1);
                    String endtime_str = EndDate.substring(EndDate.indexOf(' ') + 1);

                    String user_wakeup_time_str = getWakupTime;

//                    Log.e("test", "user_wakeup_time_str ==>>" + user_wakeup_time_str);
//                    Log.e("test", "endtime_str=>>" + endtime_str);
//
//                    Log.e("test", "starttime_str" + starttime_str);
                    // SimpleDateFormat dateFormat = new SimpleDateFormat("hmmaa");
                    // SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                        Date start_date = format.parse(starttime_str);
                        Date end_date = format.parse(endtime_str);
                        Date wakeup_date = format.parse(user_wakeup_time_str);
                        // String out = dateFormat2.format(date);
                        // Log.e("test", "get TimeStamp=>" + start_date.getTime());

                        //time continue for 24 hours
                        //  String end_date ="2019-08-01 10:00:00";
                        String start_time_str = starttime_str;
                        if (start_time_str != null) {

                            Calendar calendar_end = Calendar.getInstance();
                            Calendar calendar_start = Calendar.getInstance();
                            Calendar calendar_wakeup = Calendar.getInstance();
                            format.setCalendar(calendar_wakeup);
                            calendar_wakeup.setTime(wakeup_date);
                            format.setCalendar(calendar_end);
                            calendar_end.setTime(end_date); /* whatever*/
                            long end_time_stapm = calendar_end.getTimeInMillis();
                            //c.setTimeZone(...); if necessary
                            Date date = null;
                            if (start_time_str != null) {
                                try {
                                    date = format.parse(start_time_str);
                                    calendar_start.setTime(date);
                                } catch (ParseException e) {
                                    Log.e("test", "data formate not Proper ");
                                    e.printStackTrace();
                                }
                            }

                            long start_time_final = calendar_start.getTimeInMillis();
                            //  Log.e("test", "end_time_cal=>>" + endtime_str);
                            long user_wakeup_time = calendar_wakeup.getTimeInMillis();


                            if (start_time_final < user_wakeup_time && end_time_stapm > user_wakeup_time) {

                                long totoal = end_time_stapm - start_time_final;
                                long still = end_time_stapm - user_wakeup_time;
                                long sleep_time = totoal - still;
                                String hours = String.format("%02d", TimeUnit.MILLISECONDS.toHours(sleep_time));
                                String min = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(sleep_time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(sleep_time)));
                                String second = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(sleep_time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sleep_time)));

                                //Log.e("test", "sleep_time=" + hours + ":" + min + ":" + second);

                                String hours1 = String.format("%02d", TimeUnit.MILLISECONDS.toHours(still));
                                String min1 = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(still) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(still)));
                                String second1 = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(still) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(still)));

                                //Log.e("test", "still_time=" + hours1 + ":" + min1 + ":" + second1);

                                String final_still = String.valueOf(Integer.parseInt(hours1) * 60 + Integer.parseInt(min1));

                                String hours2 = String.format("%02d", TimeUnit.MILLISECONDS.toHours(totoal));
                                String min2 = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(totoal) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totoal)));
                                String second2 = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(totoal) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totoal)));

                                //Log.e("test", "totoal_time=" + hours2 + ":" + min2 + ":" + second2);

                                String final_sleep = String.valueOf(Integer.parseInt(hours) * 60 + Integer.parseInt(min));

                                // Log.e("sleep min data1", String.valueOf(Integer.parseInt(hours) * 60 + Integer.parseInt(min)));

                                SB.add(final_still);
                                SBCreatedDate.add(StartDate.substring(0, StartDate.indexOf(' ')));
                                SBCretedTime.add(StartDate.substring(StartDate.indexOf(' ') + 1));

                                SleepTime.add(final_sleep);
                                SleepDate.add(StartDate.substring(0, StartDate.indexOf(' ')));

                            } else {
                                if (start_time_final < user_wakeup_time) {
                                    long sleep_time = end_time_stapm - start_time_final;

                                    String hours = String.format("%02d", TimeUnit.MILLISECONDS.toHours(sleep_time));
                                    String min = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(sleep_time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(sleep_time)));
                                    String second = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(sleep_time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sleep_time)));

                                    // Log.e("test", "sleep_time=" + hours + ":" + min + ":" + second);
                                    //  System.out.println("sleep_time= " + sleep_time);
                                    //  Log.e("sleep min data1 else", String.valueOf(Integer.parseInt(hours) * 60 + Integer.parseInt(min)));

                                    SleepTime.add(String.valueOf(Integer.parseInt(hours) * 60 + Integer.parseInt(min)));
                                    SleepDate.add(StartDate.substring(0, StartDate.indexOf(' ')));

                                } else {
                                    // Log.e("test", "ZZZZZ" + activeTime);
                                    SB.add(String.valueOf(activeTime));
                                    SBCreatedDate.add(StartDate.substring(0, StartDate.indexOf(' ')));
                                    SBCretedTime.add(StartDate.substring(StartDate.indexOf(' ') + 1));
                                    System.out.println("out of sleep");
                                }

                            }

                        }


                    } catch (ParseException e) {
                        Log.e("test", "error=>" + e.getLocalizedMessage());
                    }


                } else if (bucket.getActivity().equalsIgnoreCase("still") &&
                        !StartDate.substring(0, StartDate.indexOf(' ')).equalsIgnoreCase(EndDate.substring(0, EndDate.indexOf(' ')))) {
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    try {
                        // Log.e("get differentiate date", StartDate.substring(StartDate.indexOf(' ') + 1));
                        Date date1 = format.parse("23:59:59");
                        Date date2 = format.parse(StartDate.substring(StartDate.indexOf(' ') + 1));
                        long diff = date1.getTime() - date2.getTime();
                        long diffSeconds = diff / 1000;
                        diffMinutes = diff / (60 * 1000);

                        if (diffMinutes > diffbedMinutes) {
                            diffMinutes3 = diffMinutes - diffbedMinutes;
                        }

//                        Date date3 = format.parse(EndDate.substring(EndDate.indexOf(' ') + 1));
//                        Date date4 = format.parse("00:00:00");

                        Date date6 = null, date7 = null;
                        try {
                            date6 = format.parse(getBedTime);
                            date7 = format.parse(getWakupTime);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        Date date3 = null;
                        Date date4 = null;

                        if (date6.before(date7)) {
                            date3 = format.parse(EndDate.substring(EndDate.indexOf(' ') + 1));
                            date4 = format.parse(getBedTime);
                        } else {
                            date3 = format.parse(EndDate.substring(EndDate.indexOf(' ') + 1));
                            date4 = format.parse("00:00:00");
                        }

                        long diff1 = date3.getTime() - date4.getTime();
                        long diffSeconds1 = diff1 / 1000;
                        diffMinutes1 = diff1 / (60 * 1000);

                        if (diffMinutes1 > diffwakupMinutes) {
                            diffMinutes4 = diffMinutes1 - diffwakupMinutes;
                        }

                        long TotalSleep = diffMinutes + diffMinutes1;
                        //  Log.e("get total sleep", TotalSleep + ":::" + diffMinutes + ":::" + diffMinutes1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    Log.e("get diff mins", diffMinutes + "");
//                    Log.e("get diff mins 1", diffMinutes1 + "");

                    if (diffMinutes >= diffbedMinutes) {
                        SleepTime.add(String.valueOf(diffbedMinutes));
                        SleepDate.add(StartDate.substring(0, StartDate.indexOf(' ')));
                    } else {
                        SleepTime.add(diffMinutes + "");
                        SleepDate.add(StartDate.substring(0, EndDate.indexOf(' ')));
                    }

                    if (diffMinutes1 >= diffwakupMinutes) {
                        SleepTime.add(String.valueOf(diffwakupMinutes));
                        SleepDate.add(EndDate.substring(0, StartDate.indexOf(' ')));
                    } else {
                        SleepTime.add(diffMinutes1 + "");
                        SleepDate.add(EndDate.substring(0, EndDate.indexOf(' ')));
                    }


//                    Log.e("test", "HHHH=>" + diffMinutes4);
//                    Log.e("test", "HHHH=>" + diffMinutes3);
                    SB.add(String.valueOf(diffMinutes3 + ""));
                    SBCreatedDate.add(StartDate.substring(0, StartDate.indexOf(' ')));
                    SBCretedTime.add(StartDate.substring(StartDate.indexOf(' ') + 1));

                    SB.add(String.valueOf(diffMinutes4 + ""));
                    SBCreatedDate.add(EndDate.substring(0, EndDate.indexOf(' ')));
                    SBCretedTime.add("00:00:01");

//                    Log.e("get diff mins3", diffMinutes3 + "");
//                    Log.e("get diff mins 4", diffMinutes4 + "");
                }

//                Log.e("get Sleep data", SleepTime + ":::" + SleepDate);
//                Log.e("get start sleep", startsleep);
//                Log.e("get sb data", SB + ":::" + SBCreatedDate + ":::" + SBCretedTime);

                // for dataset
                final List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    //    dumpDataSet(dataSet);

                    DateFormat scdateFormat = DateFormat.getDateInstance();
                    DateFormat sctimeFormat = DateFormat.getTimeInstance();

                    for (DataPoint dp : dataSet.getDataPoints()) {
//                        Log.e(TAG, "Data point:" + dp.getDataType());
//                        Log.e(TAG, "\tType: " + dp.getDataType().getName());

                        String scStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dp.getStartTime(TimeUnit.MILLISECONDS));
                        String scEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dp.getEndTime(TimeUnit.MILLISECONDS));

                        // comment by me

                        for (Field field : dp.getDataType().getFields()) {
                            Log.e(TAG, "\tField: " + field.getName() +
                                    " Value: " + dp.getValue(field));
                            Log.e("date", scStartDate + "---" + scEndDate);
                            if (scStartDate.substring(0, scStartDate.indexOf(' ')).equalsIgnoreCase(scEndDate.substring(0, scEndDate.indexOf(' '))) && field.getName().equalsIgnoreCase("calories")) {
                                Calories.add(dp.getValue(field).toString());
                                CaloriesDate.add(scStartDate.substring(0, scStartDate.indexOf(' ')));
                                Caloriestime.add(scStartDate.substring(StartDate.indexOf(' ') + 1));
                            }

                            // old steps logic

//                              if (scStartDate.substring(0, scStartDate.indexOf(' ')).equalsIgnoreCase(scEndDate.substring(0, scEndDate.indexOf(' '))) && field.getName().equalsIgnoreCase("steps")) {
//                            Steps.add(dp.getValue(field).toString());
//                            StepDate.add(scStartDate.substring(0, scStartDate.indexOf(' ')));
//                            StepTime.add(scStartDate.substring(StartDate.indexOf(' ') + 1));
//
//                               }

                        }


                    }

                }
            }

            FilterData();
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: "
                    + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }
        // [END parse_read_data_result]
    }

    public void dumpDataSet(DataSet dataSet) {
        //  Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());

        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {
            //     Log.i(TAG, "Data point:" + dp.getDataType());
            //        Log.i(TAG, "\tType: " + dp.getDataType().getName());

            String StartDate = new SimpleDateFormat("yyyy-MM-dd").format(dp.getStartTime(TimeUnit.MILLISECONDS));
            String EndDate = new SimpleDateFormat("yyyy-MM-dd").format(dp.getEndTime(TimeUnit.MILLISECONDS));

            // comment by me

            for (Field field : dp.getDataType().getFields()) {
                Log.i(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
                if (StartDate.equalsIgnoreCase(EndDate) && field.getName().equalsIgnoreCase("calories")) {
                    Calories.add(dp.getValue(field).toString());
                    CaloriesDate.add(StartDate);
                    Caloriestime.add(timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                }

                if (StartDate.equalsIgnoreCase(EndDate) && field.getName().equalsIgnoreCase("steps")) {
                    Steps.add(dp.getValue(field).toString());
                    StepDate.add(StartDate);
                    StepTime.add(timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                }
            }
        }

        try {
            FilterData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void FilterData() {

        // for band sleep
        if (SleepBandDate.size() > 0 && SleepBand.size() > 0) {
            FilterSleepBandDate = new ArrayList<>();
            FilterSleepBandTime = new ArrayList<>();

            String SleepbandPreview = SleepBandDate.get(0);
            double bandsleepsum = 0;

            for (int z = 0; z < SleepBand.size(); z++) {
                if (SleepbandPreview.equalsIgnoreCase(SleepBandDate.get(z))) {
                    SleepbandPreview = SleepBandDate.get(z);
                    bandsleepsum = bandsleepsum + Double.valueOf(SleepBand.get(z));
                } else {
                    FilterSleepBandDate.add(SleepbandPreview);
                    SleepbandPreview = SleepBandDate.get(z);
                    FilterSleepBandTime.add(String.valueOf(bandsleepsum));
                    bandsleepsum = 0;
                    bandsleepsum = bandsleepsum + Double.valueOf(SleepBand.get(z));
                }
            }
            FilterSleepBandTime.add(String.valueOf(bandsleepsum));
            FilterSleepBandDate.add(SleepbandPreview);

        }
        Log.e("sleep", FilterSleepBandDate + "::::" + FilterSleepBandTime);


        // For Sleep
        if (SleepDate.size() > 0 && SleepTime.size() > 0) {
            FilterSleepDate = new ArrayList<>();
            FilterSleepTime = new ArrayList<>();

            String SleepPreview = SleepDate.get(0);
            double sleepsum = 0;

            for (int k = 0; k < SleepTime.size(); k++) {
                if (SleepPreview.equalsIgnoreCase(SleepDate.get(k))) {
                    SleepPreview = SleepDate.get(k);
                    sleepsum = sleepsum + Double.valueOf(SleepTime.get(k));
                } else {
                    FilterSleepDate.add(SleepPreview);
                    SleepPreview = SleepDate.get(k);
                    FilterSleepTime.add(String.valueOf(sleepsum));
                    sleepsum = 0;
                    sleepsum = sleepsum + Double.valueOf(SleepTime.get(k));
                }
            }

            FilterSleepTime.add(String.valueOf(sleepsum));
            FilterSleepDate.add(SleepPreview);

            Log.e("get sleep data", FilterSleepTime + ":::" + FilterSleepDate);
        } else {

            Log.e("test", "Data is not available");

        }

        if (SBCreatedDate.size() > 0 && SBCretedTime.size() > 0) {
            FilterSB = new ArrayList<>();
            FilterSBCreatedDate = new ArrayList<>();
            FilterSBCreatedTime = new ArrayList<>();

            String SBPreview = SBCreatedDate.get(0);
            String SBPreviewTime = SBCretedTime.get(0);
            double SBSum = 0;

            for (int i = 0; i < SB.size(); i++) {
                if (SBPreview.equalsIgnoreCase(SBCreatedDate.get(i))) {
                    SBPreview = SBCreatedDate.get(i);
                    SBPreviewTime = SBCretedTime.get(i);
                    SBSum = SBSum + Double.valueOf(SB.get(i));
                } else {
                    FilterSBCreatedDate.add(SBPreview);
                    FilterSBCreatedTime.add(SBPreviewTime);
                    SBPreview = SBCreatedDate.get(i);
                    FilterSB.add(String.valueOf(SBSum));
                    SBSum = 0;
                    SBSum = SBSum + Double.valueOf(SB.get(i));
                }
            }

            FilterSB.add(String.valueOf(SBSum));
            FilterSBCreatedDate.add(SBPreview);
            FilterSBCreatedTime.add(SBPreviewTime);
        } else {
            Log.e("test", "Data is not available");

        }

        // For LightACtivity

        if (LightActivityCreatedDate.size() > 0 && LightActivityCreatedTime.size() > 0) {
            FilterLA = new ArrayList<>();
            FilterLACreatedDate = new ArrayList<>();
            FilterLACreatedTime = new ArrayList<>();

            String LAPreview = LightActivityCreatedDate.get(0);
            String LAPreviewTime = LightActivityCreatedTime.get(0);
            double LAsum = 0;

            for (int i = 0; i < LightActivity.size(); i++) {
                if (LAPreview.equalsIgnoreCase(LightActivityCreatedDate.get(i))) {
                    LAPreview = LightActivityCreatedDate.get(i);
                    LAPreviewTime = LightActivityCreatedTime.get(i);
                    LAsum = LAsum + Double.valueOf(LightActivity.get(i));
                } else {
                    FilterLACreatedDate.add(LAPreview);
                    FilterLACreatedTime.add(LAPreviewTime);
                    LAPreview = LightActivityCreatedDate.get(i);
                    FilterLA.add(String.valueOf(LAsum));
                    LAsum = 0;
                    LAsum = LAsum + Double.valueOf(LightActivity.get(i));
                }
            }

            FilterLA.add(String.valueOf(LAsum));
            FilterLACreatedDate.add(LAPreview);
            FilterLACreatedTime.add(LAPreviewTime);

        } else {
            Log.e("test", "Data is not available");

        }

        // For Activity
        if (ActivityCreatedDate.size() > 0 && ActivityCreatedTime.size() > 0) {
            FilterActivity = new ArrayList<>();
            FilterActivityCreatedDate = new ArrayList<>();
            FilterActivityCreatedTime = new ArrayList<>();
            String preview = ActivityCreatedDate.get(0);
            String previewtime = ActivityCreatedTime.get(0);
            double sum = 0;
            for (int i = 0; i < Activity.size(); i++) {
                if (preview.equalsIgnoreCase(ActivityCreatedDate.get(i))) {
                    preview = ActivityCreatedDate.get(i);
                    previewtime = ActivityCreatedTime.get(i);
                    sum = sum + Double.valueOf(Activity.get(i));
                } else {
                    FilterActivityCreatedDate.add(preview);
                    FilterActivityCreatedTime.add(previewtime);
                    preview = ActivityCreatedDate.get(i);
                    FilterActivity.add(String.valueOf(sum));
                    sum = 0;
                    sum = sum + Double.valueOf(Activity.get(i));

                }
            }

            FilterActivity.add(String.valueOf(sum));
            FilterActivityCreatedDate.add(preview);
            FilterActivityCreatedTime.add(previewtime);
        } else {
            Log.e("test", "Data is not available");
        }

        // For Calories
        if (CaloriesDate.size() > 0 && Caloriestime.size() > 0) {
            FilterCalories = new ArrayList<>();
            FilterCaloriesDate = new ArrayList<>();
            FilterCaloriestime = new ArrayList<>();

            String calpreview = CaloriesDate.get(0);
            String calPreviewTime = Caloriestime.get(0);
            double calsum = 0;

            for (int i = 0; i < Calories.size(); i++) {
                if (calpreview.equalsIgnoreCase(CaloriesDate.get(i))) {
                    calpreview = CaloriesDate.get(i);
                    calPreviewTime = Caloriestime.get(i);
                    calsum = calsum + Double.valueOf(Calories.get(i));
                } else {
                    FilterCaloriesDate.add(calpreview);
                    FilterCaloriestime.add(calPreviewTime);
                    //     Log.e("get sum=>>", sum + "");
                    calpreview = CaloriesDate.get(i);
                    FilterCalories.add(String.valueOf(calsum));
                    calsum = 0;
                    calsum = calsum + Double.valueOf(Calories.get(i));
                }
            }

            FilterCalories.add(String.valueOf(calsum));
            FilterCaloriesDate.add(calpreview);
            FilterCaloriestime.add(calPreviewTime);
        } else {
            Log.e("test", "Data is not available");
        }


        // For Steps
    /*    if (StepDate.size() > 0 && StepTime.size() > 0) {
            FilterSteps = new ArrayList<>();
            FilterStepDate = new ArrayList<>();
            FilterStepTime = new ArrayList<>();

            String stepPreview = StepDate.get(0);
            String stepPreviewTime = StepTime.get(0);

            double stepSum = 0;
            for (int i = 0; i < Steps.size(); i++) {
                if (stepPreview.equalsIgnoreCase(StepDate.get(i))) {
                    stepPreview = StepDate.get(i);
                    stepPreviewTime = StepTime.get(i);
                    stepSum = stepSum + Double.valueOf(Steps.get(i));
                } else {
                    FilterStepDate.add(stepPreview);
                    FilterStepTime.add(stepPreviewTime);
                    //     Log.e("get sum=>>", sum + "");
                    stepPreview = StepDate.get(i);
                    FilterSteps.add(String.valueOf(stepSum));
                    stepSum = 0;
                    stepSum = stepSum + Double.valueOf(Steps.get(i));
                }
            }

            FilterSteps.add(String.valueOf(stepSum));
            FilterStepDate.add(stepPreview);
            FilterStepTime.add(stepPreviewTime);
        } else {
            Log.e("test", "Data is not available");
        }*/


        if (FilterSleepBandTime.size() > 0 && FilterSleepBandDate.size() > 0) {
            FilterNewTime.addAll(FilterSleepBandTime);
            FilterNewDate.addAll(FilterSleepBandDate);
        }

        try {
            if (FilterSleepDate.size() > 0) {
                for (int k = 0; k < FilterSleepDate.size(); k++) {
                    for (int j = 0; j < FilterSleepBandDate.size(); j++) {
                        if (FilterSleepDate.get(k).equalsIgnoreCase(FilterSleepBandDate.get(j))) {
                            Double value = Double.valueOf(FilterSleepTime.get(k)) + Double.valueOf(FilterSB.get(k));
                            FilterSB.set(k, value + "");
                            Log.e("match", "1" + "::" + FilterSleepTime.get(k) + ":::" + value);
                            //  FilterSleepTime.set(k, FilterSleepBandTime.get(j));
                        } else {

                        }
                    }
                }

                for (int i = 0; i < FilterSleepDate.size(); i++) {
                    if (FilterSleepBandDate.contains(FilterSleepDate.get(i))) {

                    } else {
                        FilterNewTime.add(FilterSleepTime.get(i));
                        FilterNewDate.add(FilterSleepDate.get(i));
                    }
                }

                FilterSleepTime = new ArrayList<>();
                FilterSleepDate = new ArrayList<>();

                FilterSleepTime.addAll(FilterNewTime);
                FilterSleepDate.addAll(FilterNewDate);

            } else {
                if (FilterSleepBandTime.size() > 0) {
                    FilterSleepTime = FilterSleepBandTime;
                    FilterSleepDate = FilterSleepBandDate;
                }
            }
            Log.e("get sleep data", FilterSleepTime + ":::" + FilterSleepDate);
            CreateJsonResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void CreateJsonResponse() {
        try {

            if (FilterActivityCreatedDate != null && FilterActivity != null && FilterSBCreatedDate != null && FilterSB != null && FilterLACreatedDate != null && FilterLA != null) {
                HashMap<String, Double> hashMap = new HashMap<>();

                for (int k = 0; k < allDays.size(); k++) {
                    hashMap.put(allDays.get(k).trim(), 0.0);
                }

                for (int z = 0; z < FilterActivityCreatedDate.size(); z++) {
                    if (hashMap.containsKey(FilterActivityCreatedDate.get(z))) {
                        Double sum = hashMap.get(FilterActivityCreatedDate.get(z)) + Double.parseDouble(FilterActivity.get(z));
                        hashMap.put(FilterActivityCreatedDate.get(z), sum);
                    }
                }

                for (int z = 0; z < FilterSB.size(); z++) {

                    if (hashMap.containsKey(FilterSBCreatedDate.get(z))) {
                        Double sum = hashMap.get(FilterSBCreatedDate.get(z)) + Double.parseDouble(FilterSB.get(z));
                        hashMap.put(FilterSBCreatedDate.get(z), sum);
                    }
                }
                for (int z = 0; z < FilterLA.size(); z++) {
                    if (hashMap.containsKey(FilterLACreatedDate.get(z))) {
                        Double sum = hashMap.get(FilterLACreatedDate.get(z)) + Double.parseDouble(FilterLA.get(z));
                        hashMap.put(FilterLACreatedDate.get(z), sum);
                    }
                }

                final JSONObject mainOb = new JSONObject();
                JSONArray array = new JSONArray();
                JSONArray lighACtivityarray = new JSONArray();
                JSONArray sbArray = new JSONArray();
                JSONArray stepsarray = new JSONArray();
                JSONArray caloriesarray = new JSONArray();
                JSONArray sleeparray = new JSONArray();

                // Activity with total sum

//            for (int i = 0; i < FilterActivity.size(); i++) {
//                JSONObject activityOBJ = new JSONObject();
//
//                activityOBJ.put("total_active_minutes", FilterActivity.get(i));
//                activityOBJ.put("created_date", FilterActivityCreatedDate.get(i));
//                activityOBJ.put("created_time", FilterActivityCreatedTime.get(i));
//                array.put(i, activityOBJ);
//            }

                for (int j = 0; j < FilterLA.size(); j++) {
                    JSONObject lightactivityOBJ = new JSONObject();

                    lightactivityOBJ.put("total_light_activity", FilterLA.get(j));
                    lightactivityOBJ.put("created_date", FilterLACreatedDate.get(j));
                    lightactivityOBJ.put("created_time", FilterLACreatedTime.get(j));
                    lighACtivityarray.put(j, lightactivityOBJ);
                }

                for (int k = 0; k < FilterSB.size(); k++) {
                    JSONObject sbOBJ = new JSONObject();

                    sbOBJ.put("total_sb", FilterSB.get(k));
                    sbOBJ.put("created_date", FilterSBCreatedDate.get(k));
                    sbOBJ.put("created_time", FilterSBCreatedTime.get(k));
                    sbArray.put(k, sbOBJ);
                }

//                for (int l = 0; l < FilterSteps.size(); l++) {
//                    JSONObject stepOBJ = new JSONObject();
//                    stepOBJ.put("steps", FilterSteps.get(l));
//                    stepOBJ.put("created_date", FilterStepDate.get(l));
//                    stepOBJ.put("created_time", FilterStepTime.get(l));
//                    stepsarray.put(l, stepOBJ);
//                }

                for (int m = 0; m < FilterCalories.size(); m++) {
                    JSONObject calOBJ = new JSONObject();
                    calOBJ.put("calories_burned", FilterCalories.get(m));
                    calOBJ.put("created_date", FilterCaloriesDate.get(m));
                    calOBJ.put("created_time", FilterCaloriestime.get(m));
                    caloriesarray.put(m, calOBJ);
                }

                // Old Sleep Count
//            for (int k = 0; k < allDays.size(); k++) {
//                Log.e("test", "key=>>" + allDays.get(k));
//                Log.e("test", "valye=>>" + hashMap.get(allDays.get(k).trim()));
//                JSONObject sleepObj = new JSONObject();
//                sleepObj.put("total_sleep", 600);
//                sleepObj.put("created_date", allDays.get(k));
//                sleeparray.put(k, sleepObj);
//            }

                // New Sleep Count

                for (int k = 0; k < FilterSleepTime.size(); k++) {

                    JSONObject sleepObj = new JSONObject();
                    sleepObj.put("total_sleep", FilterSleepTime.get(k));
                    sleepObj.put("created_date", FilterSleepDate.get(k));
                    sleepObj.put("created_time", getBedTime);
                    sleeparray.put(k, sleepObj);
                }

                // for all excercise data

                for (int i = 0; i < allactivity.size(); i++) {
                    JSONObject activityOBJ = new JSONObject();

                    activityOBJ.put("total_active_minutes", allactivity.get(i));
                    activityOBJ.put("created_date", allactivitydate.get(i));
                    activityOBJ.put("created_time", allactivitytime.get(i));
                    activityOBJ.put("activity_category", activitytype.get(i));
                    array.put(i, activityOBJ);
                }

                // new steps

                for (int j = 0; j < Steps.size(); j++) {
                    JSONObject stepOBJ = new JSONObject();
                    stepOBJ.put("steps", Steps.get(j));
                    stepOBJ.put("created_date", StepDate.get(j));
                    stepOBJ.put("created_time", StepTime.get(j));
                    stepsarray.put(j, stepOBJ);
                }

                mainOb.put("active_minutes", array);
                mainOb.put("light_activity", lighACtivityarray);
                mainOb.put("sb", sbArray);
                mainOb.put("steps", stepsarray);
                mainOb.put("calories_burned", caloriesarray);
                mainOb.put("sleep", sleeparray);
                Log.e("Print json", mainOb.toString());

                writeFile(mainOb.toString());
                bindSavePostInfo(mainOb.toString());
            } else {
                Log.e("test", "Data is not available");


                final JSONObject mainOb = new JSONObject();
                JSONArray array = new JSONArray();
                JSONArray lighACtivityarray = new JSONArray();
                JSONArray sbArray = new JSONArray();
                JSONArray stepsarray = new JSONArray();
                JSONArray caloriesarray = new JSONArray();
                JSONArray sleeparray = new JSONArray();

                mainOb.put("active_minutes", array);
                mainOb.put("light_activity", lighACtivityarray);
                mainOb.put("sb", sbArray);
                mainOb.put("steps", stepsarray);
                mainOb.put("calories_burned", caloriesarray);
                mainOb.put("sleep", sleeparray);
                Log.e("Print json", mainOb.toString());
                writeFile(mainOb.toString());
                bindSavePostInfo(mainOb.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/
    public void CreateJsonResponse() {
        try {

            HashMap<String, Double> hashMap = new HashMap<>();

            for (int k = 0; k < allDays.size(); k++) {
                hashMap.put(allDays.get(k).trim(), 0.0);
            }


            if (FilterActivityCreatedDate != null && FilterActivity != null) {
                for (int z = 0; z < FilterActivityCreatedDate.size(); z++) {
                    if (hashMap.containsKey(FilterActivityCreatedDate.get(z))) {
                        Double sum = hashMap.get(FilterActivityCreatedDate.get(z)) + Double.parseDouble(FilterActivity.get(z));
                        hashMap.put(FilterActivityCreatedDate.get(z), sum);
                    }
                }
            }

            if (FilterSBCreatedDate != null && FilterSB != null) {
                for (int z = 0; z < FilterSB.size(); z++) {

                    if (hashMap.containsKey(FilterSBCreatedDate.get(z))) {
                        Double sum = hashMap.get(FilterSBCreatedDate.get(z)) + Double.parseDouble(FilterSB.get(z));
                        hashMap.put(FilterSBCreatedDate.get(z), sum);
                    }
                }
            }

            if (FilterLACreatedDate != null && FilterLA != null) {
                for (int z = 0; z < FilterLA.size(); z++) {
                    if (hashMap.containsKey(FilterLACreatedDate.get(z))) {
                        Double sum = hashMap.get(FilterLACreatedDate.get(z)) + Double.parseDouble(FilterLA.get(z));
                        hashMap.put(FilterLACreatedDate.get(z), sum);
                    }
                }
            }


            final JSONObject mainOb = new JSONObject();
            JSONArray array = new JSONArray();
            JSONArray lighACtivityarray = new JSONArray();
            JSONArray sbArray = new JSONArray();
            JSONArray stepsarray = new JSONArray();
            JSONArray caloriesarray = new JSONArray();
            JSONArray sleeparray = new JSONArray();

            // Activity with total sum

//            for (int i = 0; i < FilterActivity.size(); i++) {
//                JSONObject activityOBJ = new JSONObject();
//
//                activityOBJ.put("total_active_minutes", FilterActivity.get(i));
//                activityOBJ.put("created_date", FilterActivityCreatedDate.get(i));
//                activityOBJ.put("created_time", FilterActivityCreatedTime.get(i));
//                array.put(i, activityOBJ);
//            }

            if (FilterLACreatedDate != null && FilterLA != null) {
                for (int j = 0; j < FilterLA.size(); j++) {
                    JSONObject lightactivityOBJ = new JSONObject();

                    lightactivityOBJ.put("total_light_activity", FilterLA.get(j));
                    lightactivityOBJ.put("created_date", FilterLACreatedDate.get(j));
                    lightactivityOBJ.put("created_time", FilterLACreatedTime.get(j));
                    lighACtivityarray.put(j, lightactivityOBJ);
                }
            } else {
                lighACtivityarray = new JSONArray();
            }

            if (FilterSBCreatedDate != null && FilterSB != null) {
                for (int k = 0; k < FilterSB.size(); k++) {
                    JSONObject sbOBJ = new JSONObject();

                    sbOBJ.put("total_sb", FilterSB.get(k));
                    sbOBJ.put("created_date", FilterSBCreatedDate.get(k));
                    sbOBJ.put("created_time", FilterSBCreatedTime.get(k));
                    sbArray.put(k, sbOBJ);
                }
            } else {
                sbArray = new JSONArray();
            }


//                for (int l = 0; l < FilterSteps.size(); l++) {
//                    JSONObject stepOBJ = new JSONObject();
//                    stepOBJ.put("steps", FilterSteps.get(l));
//                    stepOBJ.put("created_date", FilterStepDate.get(l));
//                    stepOBJ.put("created_time", FilterStepTime.get(l));
//                    stepsarray.put(l, stepOBJ);
//                }


            if (FilterCaloriesDate != null && FilterCalories != null) {
                for (int m = 0; m < FilterCalories.size(); m++) {
                    JSONObject calOBJ = new JSONObject();
                    calOBJ.put("calories_burned", FilterCalories.get(m));
                    calOBJ.put("created_date", FilterCaloriesDate.get(m));
                    calOBJ.put("created_time", FilterCaloriestime.get(m));
                    caloriesarray.put(m, calOBJ);
                }
            } else {
                caloriesarray = new JSONArray();
            }

            // Old Sleep Count
//            for (int k = 0; k < allDays.size(); k++) {
//                Log.e("test", "key=>>" + allDays.get(k));
//                Log.e("test", "valye=>>" + hashMap.get(allDays.get(k).trim()));
//                JSONObject sleepObj = new JSONObject();
//                sleepObj.put("total_sleep", 600);
//                sleepObj.put("created_date", allDays.get(k));
//                sleeparray.put(k, sleepObj);
//            }

            // New Sleep Count

            if (FilterSleepDate != null && FilterSleepTime != null) {
                for (int k = 0; k < FilterSleepTime.size(); k++) {

                    JSONObject sleepObj = new JSONObject();
                    sleepObj.put("total_sleep", FilterSleepTime.get(k));
                    sleepObj.put("created_date", FilterSleepDate.get(k));
                    sleepObj.put("created_time", getBedTime);
                    sleeparray.put(k, sleepObj);
                }
            } else {
                sleeparray = new JSONArray();
            }


            // for all excercise data
            if (allactivity != null) {
                for (int i = 0; i < allactivity.size(); i++) {
                    JSONObject activityOBJ = new JSONObject();

                    activityOBJ.put("total_active_minutes", allactivity.get(i));
                    activityOBJ.put("created_date", allactivitydate.get(i));
                    activityOBJ.put("created_time", allactivitytime.get(i));
                    activityOBJ.put("activity_category", activitytype.get(i));
                    array.put(i, activityOBJ);
                }
            } else {
                array = new JSONArray();
            }

            // new steps

            if (Steps != null) {
                for (int j = 0; j < Steps.size(); j++) {
                    JSONObject stepOBJ = new JSONObject();
                    stepOBJ.put("steps", Steps.get(j));
                    stepOBJ.put("created_date", StepDate.get(j));
                    stepOBJ.put("created_time", StepTime.get(j));
                    stepsarray.put(j, stepOBJ);
                }
            } else {
                stepsarray = new JSONArray();
            }

            mainOb.put("active_minutes", array);
            mainOb.put("light_activity", lighACtivityarray);
            mainOb.put("sb", sbArray);
            mainOb.put("steps", stepsarray);
            mainOb.put("calories_burned", caloriesarray);
            mainOb.put("sleep", sleeparray);
            Log.e("Print json", mainOb.toString());

            writeFile(mainOb.toString());
            bindSavePostInfo(mainOb.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void writeFile(String mainOb) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "AKtivo");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, "aktivo");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(txtactivity + ":::::" + "jason data:" + mainOb);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bindSavePostInfo(final String jsonString) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Date date = calendar.getTime();
        /* whatever*/
        //c.setTimeZone(...); if necessary
        //  format.setCalendar(calendar);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.e("test", "calendar data_time" + sdf.format(date));

        WebApiClient.getInstance(mActivity).getWebApi().callSaveGoogleFitData(user_id, "google_fit", jsonString, sdf.format(date)).enqueue(new Callback<BaseCommanRespons>() {
            @Override
            public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                            mClient.stopAutoManage(getActivity());
                            mClient.disconnect();
                            long millis = System.currentTimeMillis();
                            CommonKeys.IS_GOOGLE_FIT_SYNCHRONIZE = false;
                            CommonKeys.is_Progress_bar_update = false;
                            callUserConnectivityType("google_fit");
                            //Divide millis by 1000 to get the number of seconds.
                            long seconds = millis / 1000;
                            MyPreferences.setPref(mActivity, CommonKeys.USER_CURRENT_TIME_DATA, String.valueOf(seconds));

                            common_methods.setCutemDialogMessage(mActivity, success_full_synchronize);
                            //Toast.makeText(getActivity(), "Your Google-Fit data were insterted successfully", Toast.LENGTH_LONG).show();
                            MyPreferences.setPref(mActivity, CommonKeys.CONNECTEDTYPE, "google_fit");      //store ConnectedType
                            mActivity.tagmanager("Google Fit Sync Successful Sync popup", "settings_connected_devices_gf_sync_success");

                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<BaseCommanRespons> call, Throwable throwable) {
                common_methods.setExceptionMessage(throwable, mActivity);
            }
        });
    }

    public void CreateValidicUser(final String userid) {
        WebApiClient.getInstance(mActivity).getWebApi().CreateValidicUser(userid).enqueue(new Callback<MarketUrlResponse>() {
            @Override
            public void onResponse(Call<MarketUrlResponse> call, Response<MarketUrlResponse> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                            MyPreferences.setPref(mActivity, CommonKeys.CONNECTEDTYPE, "validic");
                            CommonKeys.is_Progress_bar_update = false;
                            callUserConnectivityType("validic");
                            String marketplaceurl = response.body().getData().getMarketplaceurl();
                            //store ConnectedType
                            mActivity.pushFragmentDontIgnoreCurrent(WebviewLoadUrlFragment.getInstance(marketplaceurl), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<MarketUrlResponse> call, Throwable throwable) {
                common_methods.setExceptionMessage(throwable, mActivity);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mClient != null && mClient.isConnected()) {
            mClient.stopAutoManage(getActivity());
            mClient.disconnect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mClient != null && mClient.isConnected()) {
            mClient.stopAutoManage(getActivity());
            mClient.disconnect();
        }
    }

    private void callUserConnectivityType(final String platform_type) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Date date = calendar.getTime();
        /* whatever*/
        //c.setTimeZone(...); if necessary
        //  format.setCalendar(calendar);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.e("test", "calendar data_time" + sdf.format(date));

        WebApiClient.getInstance(mActivity).getWebApi().callUserChangeConnectionType(user_id, platform_type, sdf.format(date)).enqueue(new Callback<ConnectedTypeResponse>() {
            @Override
            public void onResponse(Call<ConnectedTypeResponse> call, Response<ConnectedTypeResponse> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                            Log.e("test", "sussufully call user connecte Type api" + platform_type);
                        } else {
                            Methods.isProgressHide();
                        }
                    }
                }
                Methods.isProgressHide();
            }

            @Override
            public void onFailure(Call<ConnectedTypeResponse> call, Throwable t) {
                Methods.isProgressHide();
                // common_methods.setExceptionMessage(t, mActivity);

                Log.e("test", "=>>" + t.getLocalizedMessage());
            }
        });
    }

}


//    public class ConnectDeviceAdaptor extends RecyclerView.Adapter<ConnectDeviceAdaptor.MyViewHolder> {
//
//        private List<ConnectDevice> deviceList;
//
//
//        public class MyViewHolder extends RecyclerView.ViewHolder {
//            @BindView(R.id.img)
//            ImageView img;
//            @BindView(R.id.tv_device_name)
//            TextView tv_device_name;
//
//            public MyViewHolder(View view) {
//                super(view);
//                ButterKnife.bind(this, view);
//
//            }
//        }
//        public ConnectDeviceAdaptor(List<ConnectDevice> langList) {
//            this.deviceList = langList;
//        }
//        public void setDataUpdate(List<ConnectDevice> langList) {
//            this.deviceList = langList;
//            notifyDataSetChanged();
//        }
//        @Override
//        public ConnectDeviceAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.row_connect_device, parent, false);
//
//            return new ConnectDeviceAdaptor.MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(ConnectDeviceAdaptor.MyViewHolder holder, final int pos) {
//            final int position=pos;
//            // final VehicalBrandList vehicalBrand = vehicalBrandLists.get(position);
//            if (deviceList.get(position)!= null) {
//                holder.tv_device_name.setTypeface(ExtraLight_font);
//                if (Validation.isRequiredField(deviceList.get(position).getDevice_name())) {
//                    holder.tv_device_name.setText(deviceList.get(position).getDevice_name());
//                }
//
//                if(deviceList.get(position).isIs_selected()){
//                    holder.img.setVisibility(View.VISIBLE);
//                    holder.tv_device_name.setTextColor(mActivity.getResources().getColor(R.color.txt_black));
//                }else {
//                    holder.tv_device_name.setTextColor(mActivity.getResources().getColor(R.color.gray_light));
//                    holder.img.setVisibility(View.INVISIBLE);
//                }
//
//
//            }
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(deviceList.get(position).isIs_selected()){
//                        notifayTextColor(false,position);
//                    }else {
//                        notifayTextColor(true,position);
//
//                    }
//
//                }
//            });
//
//        }
//        private void notifayTextColor(boolean is_value,int pos) {
//
//            for (int i = 0; i < deviceList.size() ; i++) {
//
//                if(i==pos){
//                    deviceList.get(i).setIs_selected(is_value);
//                    break;
//                }
//
//            }
//            notifyDataSetChanged();
//        }
//
//
//        @Override
//        public int getItemCount() {
//            return deviceList.size();
//        }
//    }

