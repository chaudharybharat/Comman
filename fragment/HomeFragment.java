package com.aktivo.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.ConnectionUtil;
import com.aktivo.Utils.Methods;
import com.aktivo.Utils.MyPreferences;
import com.aktivo.Utils.OnSwipeTouchListener;
import com.aktivo.Utils.ProgressbarCustome;
import com.aktivo.response.BaseCommanRespons;
import com.aktivo.response.PostCMS;
import com.aktivo.response.PostCMSReponse;
import com.aktivo.response.TodayYouHave;
import com.aktivo.response.TodayYouHaveResponse;
import com.aktivo.response.TodayYouhaveModel;
import com.aktivo.response.UserDetailTable;
import com.aktivo.webservices.WebApiClient;
import com.commonmodule.mi.utils.Validation;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.raizlabs.android.dbflow.sql.language.Delete;

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
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.view.View.X;
import static java.text.DateFormat.getDateInstance;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_progressbar)
    TextView tv_progressbar;
    @BindView(R.id.pb)
    ProgressbarCustome pb;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.circleIndicator)
    CircleIndicator circleIndicator;
    @BindView(R.id.recyclview)
    RecyclerView recyclerView;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.tv_not_found)
    TextView tv_not_found;
    @BindView(R.id.tv_today)
    TextView tv_today;
    @BindView(R.id.tv_you_have)
    TextView tv_you_have;
    @BindView(R.id.tv_what_is_activo)
    TextView tv_what_is_activo;
    @BindView(R.id.tv_not_found_silder)
    TextView tv_not_found_silder;
    @BindView(R.id.tv_txt_valence)
    TextView tv_txt_valence;
    @BindView(R.id.swipe_home)
    SwipeRefreshLayout swipe_home;
    @BindView(R.id.ll_main)
    LinearLayout ll_main;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_aktivo)
    TextView tv_aktivo;
    @BindView(R.id.tv_user_tv)
    TextView tv_user_tv;

    @BindView(R.id.tv_hours_value)
    TextView tv_hours_value;
    @BindView(R.id.tv_min_value)
    TextView tv_min_value;
    @BindView(R.id.tv_second_value)
    TextView tv_second_value;

    @BindView(R.id.tv_hours)
    TextView tv_hours;
    @BindView(R.id.tv_min)
    TextView tv_min;
    @BindView(R.id.tv_second)
    TextView tv_second;
    @BindView(R.id.tv_what_is_activo_second)
    TextView tv_what_is_activo_second;
    @BindView(R.id.ll_user_timer)
    LinearLayout ll_user_timer;
    @BindView(R.id.rl_valence)
    RelativeLayout rl_valence;
    @BindView(R.id.rl_progressbar)
    RelativeLayout rl_progressbar;
    @BindView(R.id.rl_viewpager)
    RelativeLayout rl_viewpager;
    @BindView(R.id.recycler_view_slider)
    RecyclerView recycler_view_slider;
    @BindView(R.id.check_first)
    ImageView check_first;
    @BindView(R.id.check_second)
    ImageView check_second;
    @BindView(R.id.check_third)
    ImageView check_third;
    @BindView(R.id.ll_indicator)
    LinearLayout ll_indicator;
    @BindView(R.id.llMainScroll)
    ScrollView llMainScroll;
    int SLIDER_PUSH_FRAMENT_CONDITION = 0;
    Unbinder unbinder;

    public static GoogleApiClient mClient = null;
    private HomeDataAdaptor homeDataAdaptor;
    List<TodayYouhaveModel> todayYouhavelist;
    UserDetailTable userDetailTable;
    ArrayList<String> slider_dateList;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    String google_fit_str = "<b>Connect to Google Fit or your wearable to get started</b>";
    public static final String WHAT_IS_AKTIVO = "WHAT_IS_AKTIVO";
    Dialog dialogBuilder;
    ImageView tv_closer_what_id_dialog;
    TextView tv_detail, tv_nodata;
    ProgressBar progressBar_what_is_activito;
    String aktivo_avalible = "Hold Tight! Your very first <b>Aktivo Score</b> will be available in:";

    FragmentManager fragmentManager;

    String success_full_synchronize = "Successfully synchronized with <b>GoogleFit</b>.";
    static int ZERO_TIMER = 0;
    static int TIMER_START = 1;
    static int START_FINAL = 2;
    public int condtion_postion = ZERO_TIMER;
    public String getBedTime = "", getWakupTime = "";
    long diffbedMinutes = 0;
    long diffwakupMinutes = 0;
    String user_id = "";
    String you_connect_timer_commeing_you_have_api = "";
    ArrayList<String> allactivity = new ArrayList<>();
    ArrayList<String> allactivitydate = new ArrayList<>();
    ArrayList<String> allactivitytime = new ArrayList<>();
    ArrayList<String> activitytype = new ArrayList<>();
    ArrayList<String> txtactivity = new ArrayList<>();
    ArrayList<String> FilterNewTime = new ArrayList<>();
    ArrayList<String> FilterNewDate = new ArrayList<>();
    boolean homeflag=false;

    SliderAdapter sliderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        setHeader();
/*
        mViewPager.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {


            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                Log.e("test","onSwipeLeft");
                int current=mViewPager.getCurrentItem();
                if(current<slider_dateList.size()){
                    mViewPager.setCurrentItem(current+1);
                }

            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                int current=mViewPager.getCurrentItem();
                if(current>0){
                    mViewPager.setCurrentItem(current-1);
                }

                Log.e("test","onSwipeRight");

            }
        });*/
        CommonKeys.is_Progress_bar_update = false;
        fragmentManager = getChildFragmentManager();
        userDetailTable = UserDetailTable.getUserDetail();
        //user comming first time show 2 second clear data screen show
        String is_value = MyPreferences.getPref(mContext, CommonKeys.USER_COMMING_FIRST_TIME_HOME);
        if (is_value.equalsIgnoreCase(CommonKeys.TRUE)) {
            ll_main.setVisibility(View.GONE);
            MyPreferences.setPref(mContext, CommonKeys.USER_COMMING_FIRST_TIME_HOME, CommonKeys.FALSE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ll_main.setVisibility(View.VISIBLE);
                }
            }, 2000);
        } else {
            ll_main.setVisibility(View.VISIBLE);
        }

        swipe_home.setOnRefreshListener(this);
        setFont();
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
       /* if (userDetailTable != null) {
            if (Validation.isRequiredField(userDetailTable.get_id())) {
                if (MyPreferences.getPref(mActivity, CommonKeys.CONNECTEDTYPE).equalsIgnoreCase("validic")) {
                    callSaveGoogleFitData(userDetailTable.get_id(),false);
                }
            }
        }*/


        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date2 = null, date3 = null;
        try {
            date2 = format.parse(getBedTime);
            date3 = format.parse(getWakupTime);


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
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        initComponet();
        mActivity.tagmanager("Aktivo Home Screen","home_view");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            llMainScroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (homeflag == false){
                        mActivity.tagmanager("Aktivo Home Screen","home_scroll");
                        homeflag = true;

                    }
                }
            });
        }
    }

    private void setFont() {
        tv_what_is_activo.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_what_is_activo_second.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_you_have.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_today.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_txt_valence.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_progressbar.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_not_found_silder.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_not_found.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_hours_value.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_min_value.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_second_value.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_hours.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_min.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_second.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_user_tv.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
    }

    private void initComponet() {
        ll_user_timer.setVisibility(View.GONE);
        rl_progressbar.setVisibility(View.GONE);

        // Log.e("test","date=>>"+);
       /* String user_is_login=MyPreferences.getPref(mActivity,CommonKeys.IS_USER_START_SERVICE_RUNNING);
        if(user_is_login!=null && !user_is_login.equalsIgnoreCase(CommonKeys.TRUE)){
            Log.e("test","call service");
            MyPreferences.setPref(mActivity,CommonKeys.IS_USER_START_SERVICE_RUNNING,CommonKeys.TRUE);
            mActivity.startService(new Intent(mActivity, HomeTimerService.class).setAction(CommonKeys.START_TIMER_SERIVE));
            startTimer(CommonKeys.USER_LOGIN_CURRENT_TIME_SECOND);
        }else {
            Log.e("test","call not service");
            startTimer(CommonKeys.USER_LOGIN_CURRENT_TIME_SECOND);
        }*/

        // setProgrebarValue(70);
        slider_dateList = new ArrayList<>();
        sliderAdapter = new SliderAdapter(slider_dateList);
        //recycleview set        recyclerView.setNestedScrollingEnabled(false);
        todayYouhavelist = new ArrayList<>();
        homeDataAdaptor = new HomeDataAdaptor(todayYouhavelist);

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`

        // changes done by hitendra
//        final LinearLayoutManager mLayoutManager_horizontal
//                = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);

        final LinearLayoutManager mLayoutManager_horizontal
                = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {

                    private static final float SPEED = 300f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }

                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

        };

        recycler_view_slider.setLayoutManager(mLayoutManager_horizontal);

        recycler_view_slider.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipe_home.setEnabled(topRowVerticalPosition >= 0);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        final RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(mActivity) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        LinearSnapHelper snapHelper = new LinearSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                View centerView = findSnapView(layoutManager);
                if (centerView == null)
                    return RecyclerView.NO_POSITION;

                int position = layoutManager.getPosition(centerView);
                int targetPosition = -1;
                if (layoutManager.canScrollHorizontally()) {
                    if (velocityX < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                if (layoutManager.canScrollVertically()) {
                    if (velocityY < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                final int firstItem = 0;
                final int lastItem = layoutManager.getItemCount() - 1;
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
                return targetPosition;
            }
        };
        snapHelper.attachToRecyclerView(recycler_view_slider);

        recycler_view_slider.setAdapter(sliderAdapter);

        recycler_view_slider.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPos = mLayoutManager_horizontal.findFirstVisibleItemPosition();
                int lastPos = mLayoutManager_horizontal.findLastVisibleItemPosition();
                int middle = Math.abs(lastPos - firstPos) / 2 + firstPos;

                int selectedPos = -1;
                for (int i = 0; i < sliderAdapter.getItemCount(); i++) {
                    if (i == middle) {
                        selectedPos = i;
                    } else {

                    }
                }
                setSloiderIndicateror(selectedPos);
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        // recyclview_hobby.addItemDecoration(new DividerItemDecoration(mActivity.getResources().getDrawable(R.drawable.row_divider)));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(homeDataAdaptor);
        if (ConnectionUtil.isInternetAvailable(mActivity)) {
            String connecet_type = MyPreferences.getPref(mActivity, CommonKeys.CONNECTEDTYPE);

            if (Validation.isRequiredField(connecet_type)) {
                if (connecet_type.equalsIgnoreCase("validic")) {
                    //    callSaveGoogleFitData(userDetailTable.get_id(), false);
                    callGenerateAktivoScoreApi(user_id, false);
                } else {
                    //user commming first time that time call on
                    if (!CommonKeys.IS_GOOGLE_FIT_DATA_SYNCRO) {
                        CommonKeys.IS_GOOGLE_FIT_DATA_SYNCRO = true;
                        fitInstalled();
                    } else {
                        callGenerateAktivoScoreApi(user_id, false);
                    }
                }
            } else {
                callGenerateAktivoScoreApi(user_id, false);
            }

        }

    }

    CountDownTimer countDownTimer;

    private void startTimer(long noOfMinutes) {

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer.onFinish();
        }
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long current_millisecond = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hours = String.format("%02d", TimeUnit.MILLISECONDS.toHours(current_millisecond));
                String min = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(current_millisecond) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(current_millisecond)));
                String second = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(current_millisecond) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(current_millisecond)));

                Log.e("test", "hours=>" + hours);
                Log.e("test", "min=>" + min);
                Log.e("test", "second=>" + second);
                tv_hours_value.setText("" + hours);
                tv_min_value.setText("" + min);
                tv_second_value.setText("" + second);
            }

            public void onFinish() {
                Log.e("test", "TIME'S UP!!"); //On finish change timer text
            }
        }.start();
    }

    Call<TodayYouHaveResponse> todayYouHaveResponseCall;

    private void callTodayYouHaveApi(String user_id, final boolean is_swifRefresh) {
        if (!swipe_home.isRefreshing()) {
            progressbar.setVisibility(View.VISIBLE);
        } else {
            progressbar.setVisibility(View.GONE);
        }
        tv_not_found.setVisibility(View.GONE);
        rl_valence.setVisibility(View.GONE);
        todayYouHaveResponseCall = WebApiClient.getInstance(mActivity).getWebApi().callTodayYouHave(user_id);
        todayYouHaveResponseCall.enqueue(new Callback<TodayYouHaveResponse>() {
            @Override
            public void onResponse(Call<TodayYouHaveResponse> call, Response<TodayYouHaveResponse> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                            if (response.body().getData() != null) {
                                ll_user_timer.setVisibility(View.GONE);
                                Delete.table(TodayYouHave.class);
                                response.body().getData().save();
                                //get user connected_type
                                if (Validation.isRequiredField(response.body().getPlatform_connected_type())) {
                                    MyPreferences.setPref(mActivity, CommonKeys.CONNECTEDTYPE, response.body().getPlatform_connected_type());
                                }
                                if (Validation.isRequiredField(response.body().getData().getHome_background_image())) {
                                    MyPreferences.setPref(mActivity, CommonKeys.BACKGROUND_MAIN, response.body().getData().getHome_background_image());
                                    mActivity.setBackgroudnImage(response.body().getData().getHome_background_image());
                                }

                                if (todayYouhavelist != null && !todayYouhavelist.isEmpty()) {
                                    todayYouhavelist.clear();
                                }
                                if (slider_dateList != null && !slider_dateList.isEmpty()) {
                                    slider_dateList.clear();
                                }

                                if (response.body().getPlatform_connected_type().equalsIgnoreCase("")) {
                                    rl_valence.setVisibility(View.VISIBLE);
                                    //zero counnter
                                    rl_progressbar.setVisibility(View.GONE);
                                    ll_user_timer.setVisibility(View.VISIBLE);
                                    condtion_postion = ZERO_TIMER;
                                    tv_user_tv.setText(Html.fromHtml(google_fit_str));
                                    tv_not_found_silder.setVisibility(View.GONE);
                                    slider_dateList = getTimerZeroTextSlider();
                                    todayYouhavelist = getTimerZeroThattime(Integer.parseInt(response.body().getData().getPositivity_score()));
                                    sliderAdapter.setDataUpdate(slider_dateList);
                                    SLIDER_PUSH_FRAMENT_CONDITION = ZERO_TIMER;
                                    //   mSectionsPagerAdapter = new SectionsPagerAdapter(fragmentManager, slider_dateList, ZERO_TIMER);
                                    // Set up the ViewPager with the sections adapter.
                                    // mViewPager.setAdapter(mSectionsPagerAdapter);
                                    // mViewPager.setCurrentItem(0);
                                    // circleIndicator.setViewPager(mViewPager);
                                    homeDataAdaptor.setDataUpdate(todayYouhavelist);

                                } else {
                                    rl_valence.setVisibility(View.VISIBLE);
                                    if (!response.body().getAvailability_status().equalsIgnoreCase("available")) {
                                        if (response.body().getPlatform_connected_type().equalsIgnoreCase("validic")) {
                                            //only first time call after background remove again open time call below code
                                            if (!CommonKeys.IS_GOOGLE_FIT_SYNCHRONIZE) {
                                                openDataAvalibityNotAvaliableDialog(true);
                                                CommonKeys.IS_GOOGLE_FIT_SYNCHRONIZE = true;
                                            }
                                        }
                                        if (response.body().getPlatform_connected_type().equalsIgnoreCase("google_fit")) {
                                            openDataAvalibityNotAvaliableDialog(false);
//                                            if (!CommonKeys.IS_GOOGLE_FIT_DATA_SYNCRO) {
//                                                you_connect_timer_commeing_you_have_api = response.body().getPlatform_connected_datetime();
//                                                fitInstalled();
//                                                CommonKeys.IS_GOOGLE_FIT_DATA_SYNCRO = true;
//                                            }
                                        }
                                    }
                                    if (response.body().getUser_data_status().equalsIgnoreCase("Yes")) {
                                        //old
                                        //tv_txt_valence.setText(Html.fromHtml("Find your Positivity Score by clicking on 'mic button now."));
                                        tv_txt_valence.setText(Html.fromHtml(response.body().getData().getPositivity()));
                                        rl_valence.setVisibility(View.VISIBLE);
                                        if (Validation.isRequiredField(response.body().getData().getPositivity())) {
                                            tv_txt_valence.setText(Html.fromHtml(response.body().getData().getPositivity()));
                                        }
                                        condtion_postion = START_FINAL;
                                        rl_progressbar.setVisibility(View.VISIBLE);
                                        ll_user_timer.setVisibility(View.GONE);
                                        if (response.body().getData().getSlider() != null && response.body().getData().getSlider().length > 0) {
                                            for (int i = 0; i < response.body().getData().getSlider().length; i++) {
                                                slider_dateList.add(response.body().getData().getSlider()[i]);
                                            }
                                            if (slider_dateList.size() > 0) {
                                                tv_not_found_silder.setVisibility(View.GONE);
                                            } else {
                                                tv_not_found_silder.setVisibility(View.VISIBLE);
                                            }
                                            if (Validation.isRequiredField(response.body().getData().getAktivo_score())) {
                                                int porgressbar = Integer.parseInt(response.body().getData().getAktivo_score());
                                                if (porgressbar == 0) {
                                                    tv_progressbar.setText("0.");
                                                } else {
                                                    if (!CommonKeys.is_Progress_bar_update) {
                                                        setProgrebarValue(porgressbar);
                                                    }
                                                }
                                            }
                                            SLIDER_PUSH_FRAMENT_CONDITION = START_FINAL;
                                            // mSectionsPagerAdapter = new SectionsPagerAdapter(fragmentManager, slider_dateList, START_FINAL);
                                            // Set up the ViewPager with the sections adapter.
                                            // mViewPager.setAdapter(mSectionsPagerAdapter);
                                            // mViewPager.setCurrentItem(0);
                                            // circleIndicator.setViewPager(mViewPager);
                                            sliderAdapter.setDataUpdate(slider_dateList);
                                        } else {
                                            tv_not_found_silder.setVisibility(View.VISIBLE);
                                        }
                                        todayYouhavelist.add(new TodayYouhaveModel(CommonKeys.CALORIES_BURNED, response.body().getData().getCalories_burned()));
                                        todayYouhavelist.add(new TodayYouhaveModel(CommonKeys.STEPS_TAKEN, response.body().getData().getSteps_taken()));
                                        todayYouhavelist.add(new TodayYouhaveModel(CommonKeys.SLEEP, response.body().getData().getSleep()));
                                        todayYouhavelist.add(new TodayYouhaveModel(CommonKeys.RESTING_HEART_RATE, response.body().getData().getResting_heart_rate()));
                                        todayYouhavelist.add(new TodayYouhaveModel(CommonKeys.ACTIVE_MINUTES, response.body().getData().getActive_minutes()));
                                        homeDataAdaptor.setDataUpdate(todayYouhavelist);

                                    } else {
                                        rl_valence.setVisibility(View.VISIBLE);
                                        //24 hours timer start
                                        condtion_postion = TIMER_START;
                                        tv_user_tv.setText(Html.fromHtml(aktivo_avalible));

                                        ll_user_timer.setVisibility(View.VISIBLE);
                                        rl_progressbar.setVisibility(View.GONE);
                                        //time continue for 24 hours
                                        String time_millisecond = response.body().getPlatform_connected_datetime();

                                        if (Validation.isRequiredField(time_millisecond)) {

                                            Calendar calendar = Calendar.getInstance();
                                            calendar.setTime(new Date()); /* whatever*/
                                            long milissecond_current = calendar.getTimeInMillis();
                                            //c.setTimeZone(...); if necessary
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                                            format.setCalendar(calendar);
                                            Date date = null;
                                            if (time_millisecond != null) {
                                                try {
                                                    date = format.parse(time_millisecond);
                                                    calendar.setTime(date);
                                                } catch (ParseException e) {
                                                    Log.e("test", "data formate not Proper ");
                                                    e.printStackTrace();
                                                }
                                            }
                                            long currenTimeStamp = milissecond_current;
                                            long connected_divives_time_stamp = calendar.getTimeInMillis();

                                            long dirrent_tamp = currenTimeStamp - connected_divives_time_stamp;

                                            long last_time = CommonKeys.USER_LOGIN_CURRENT_TIME_SECOND - dirrent_tamp;

                                            startTimer(last_time);

                                            tv_not_found_silder.setVisibility(View.GONE);
                                            slider_dateList = getTimerTwentyFourHourSlider(response.body().getComplete_profile_percentage(), response.body().getPlatform_connectivity_status());
                                            todayYouhavelist = getTwentyFourHourList(Integer.parseInt(response.body().getData().getPositivity_score()));
                                            SLIDER_PUSH_FRAMENT_CONDITION = TIMER_START;
                                            // mSectionsPagerAdapter = new SectionsPagerAdapter(fragmentManager, slider_dateList, TIMER_START);
                                            // Set up the ViewPager with the sections adapter.
                                            //  mViewPager.setAdapter(mSectionsPagerAdapter);
                                            // mViewPager.setCurrentItem(0);
                                            //circleIndicator.setViewPager(mViewPager);
                                            sliderAdapter.setDataUpdate(slider_dateList);
                                            homeDataAdaptor.setDataUpdate(todayYouhavelist);


                                        }

                                    }

                                }

                            }
                        } else {
                            mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                        }
                    }
                }
                if (todayYouhavelist != null && todayYouhavelist.isEmpty()) {
                    tv_not_found.setVisibility(View.VISIBLE);
                } else {
                    tv_not_found.setVisibility(View.GONE);

                }

                if (swipe_home.isRefreshing()) {
                    swipe_home.setRefreshing(false);
                }
                progressbar.setVisibility(View.GONE);
                Methods.isProgressHide();
            }

            @Override
            public void onFailure(Call<TodayYouHaveResponse> call, Throwable t) {
                if (is_visible_fragment) {
                    progressbar.setVisibility(View.GONE);
                    Methods.isProgressHide();
                    if (swipe_home.isRefreshing()) {
                        swipe_home.setRefreshing(false);
                    }
                    if (todayYouhavelist != null && todayYouhavelist.isEmpty()) {
                        tv_not_found.setVisibility(View.VISIBLE);
                    } else {
                        tv_not_found.setVisibility(View.GONE);

                    }
                }
            }
        });
    }

    private void setProgrebarValue(final int progrebarValue) {
        // Set the progress status zero on each button click
        progressStatus = 0;
        CommonKeys.is_Progress_bar_update = true;
        // Start the lengthy operation in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < progrebarValue) {
                    // Update the progress status
                    progressStatus += 1;
                    // Try to sleep the thread for 20 milliseconds
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Update the progress bar
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pb.setProgress(progressStatus);
                            // Show the progress on TextView
                            tv_progressbar.setText(progressStatus + ".");
                            // If task execution completed
                            if (progressStatus == 100) {
                                // Set a message of completion
                                //tv.setText("Operation completed.");
                            }
                        }
                    });
                }
            }
        }).start(); // Start the operation
    }

    private void setHeader() {
        mActivity.seletect_tab(CommonKeys.HOME_TAB);
        mActivity.setToolbarTopVisibility(false);
        mActivity.setToolbarBottomVisibility(true);
        tv_aktivo.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_title.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_title.setText(CommonKeys.HOME);
        mActivity.enableDrawer();
        // mActivity.setMenuVisibleGone(true);
        // mActivity.setTitleToolbar(CommonKeys.HOME);
        //   mActivity.setBackgroudnImage(common_methods.getBackgroundImage(mActivity, CommonKeys.BACKGROUND_MAIN));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setHeader();
        } else {
            Log.e("test", "call onhiddent");
        }
    }

    @OnClick({R.id.tv_what_is_activo_second, R.id.tv_txt_valence, R.id.tv_what_is_activo, R.id.pic_last_valence, R.id.iv_menu, R.id.pb})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pic_last_valence:

                mActivity.tagmanager("Microphone Button", "home_positivity_mic_click");
                mActivity.clearOnTabSelectedListeners();
                mActivity.pushFragmentDontIgnoreCurrent(new AktivoValenceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                break;
            case R.id.iv_menu:
                mActivity.openDrawer();

                break;
            case R.id.tv_txt_valence:
                if (condtion_postion == START_FINAL) {
                    mActivity.tagmanager("Today: Positivity Stats", "home_today_positivity_stats_click");
                    mActivity.clearBackStack();
                    mActivity.clearBackStackFragmets();
                    mActivity.onselectTabPostion(4);
                    mActivity.pushFragmentDontIgnoreCurrent(AktivoMyStatsFragment.getInstance(CommonKeys.POSITIVITY), mActivity.FRAGMENT_JUST_REPLACE);
                } else {
                    mActivity.pushFragmentDontIgnoreCurrent(new AktivoValenceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                }

                // mActivity.pushFragmentDontIgnoreCurrent(AktivoMyStatsFragment.getInstance(CommonKeys.POSITIVITY), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                break;
            case R.id.tv_what_is_activo:
            case R.id.tv_what_is_activo_second:
                mActivity.tagmanager("What is Aktivo Score Link", "home_whatIsAktivoScore_click");
                showChangeLangDialog();
                // mActivity.pushFragmentDontIgnoreCurrent(new WhatIsScoreAktivoFragment(),mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                break;
            case R.id.pb:
                mActivity.tagmanager("Aktivo Score Progress Bar", "home_aktivo_score_pbar_click");
                mActivity.clearBackStack();
                mActivity.clearBackStackFragmets();
                mActivity.onselectTabPostion(4);
                mActivity.pushFragmentDontIgnoreCurrent(new AktivoMyStatsFragment(), mActivity.FRAGMENT_JUST_REPLACE);
                // mActivity.pushFragmentDontIgnoreCurrent(new WhatIsScoreAktivoFragment(),mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                break;

            default:
        }
    }

    @Override
    public void onRefresh() {
        if (ConnectionUtil.isInternetAvailable(mActivity)) {
            mActivity.tagmanager("Manual Refresh", "home_pull_to_refresh");
            String connectType = MyPreferences.getPref(mActivity, CommonKeys.CONNECTEDTYPE);
            if (Validation.isRequiredField(connectType)) {
                //if user type validic call api
                if (connectType.equalsIgnoreCase("validic")) {
                    callSaveGoogleFitData(userDetailTable.get_id(), true);
                }
                if (connectType.equalsIgnoreCase("google_fit")) {
                    fitInstalled();
                }
            } else {
                callGenerateAktivoScoreApi(user_id, true);
            }
            //callTodayYouHaveApi(userDetailTable.get_id(), true);
        } else {
            swipe_home.setRefreshing(false);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        ArrayList<String> name = new ArrayList<>();
        int condtion;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<String> name, int condition) {
            super(fm);
            this.name = name;
            this.condtion = condition;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(name.get(position), position, condtion);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return name.size();
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "SECTION 1";
//                case 1:
//                    return "SECTION 2";
//                case 2:
//                    return "SECTION 3";
//            }
//            return null;
//        }
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        Typeface ExtraLight_font = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-ExtraLight.ttf");

        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String POSTION = "postion";
        private static final String CONDITION = "condtion";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String text, int postion, int condtion) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_SECTION_NUMBER, text);
            args.putInt(POSTION, postion);
            args.putInt(CONDITION, condtion);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.viewpager_only_seting, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.tv_string);
            textView.setTypeface(ExtraLight_font);

            //"<span style=\"font-family: '\(appFonts.ExtraLight)'; font-size: \(self.font!.pointSize)\">%@</span>"
            //  textView.setText(Html.fromHtml("<span style=font-family:appFonts.ExtraLight'; font-size:30"+getArguments().getString(ARG_SECTION_NUMBER)+"</span>"));
            textView.setText(Html.fromHtml(getArguments().getString(ARG_SECTION_NUMBER)));
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int current_postiion = getArguments().getInt(POSTION);
                    int condtion = getArguments().getInt(CONDITION);
                    switch (condtion) {
                        case 0:
                            switch (current_postiion) {
                                case 0:
                                    //  mActivity.clearBackStack();
                                    // mActivity.clearBackStackFragmets();
                                    //  mActivity.onselectTabPostion(4);
                                    mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                                    break;

                                case 1:
                              /*  mActivity.clearBackStack();
                                mActivity.clearBackStackFragmets();
                                mActivity.onselectTabPostion(3);*/
                                    mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                                    break;
                                case 2:
                               /* mActivity.clearBackStack();
                                mActivity.clearBackStackFragmets();
                                mActivity.onselectTabPostion(2);*/
                                    mActivity.pushFragmentDontIgnoreCurrent(new AktivoValenceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                                    break;

                                default:
                                    break;
                            }

                            break;
                        case 1:
                            switch (current_postiion) {
                                case 0:
                                    //  mActivity.clearBackStack();
                                    // mActivity.clearBackStackFragmets();
                                    //  mActivity.onselectTabPostion(4);
                                    mActivity.pushFragmentDontIgnoreCurrent(new EditProfileFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                                    break;
                                case 1:
                              /*  mActivity.clearBackStack();
                                mActivity.clearBackStackFragmets();
                                mActivity.onselectTabPostion(3);*/
                                    mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                                    break;
                                case 2:
                               /* mActivity.clearBackStack();
                                mActivity.clearBackStackFragmets();
                                mActivity.onselectTabPostion(2);*/
                                    mActivity.pushFragmentDontIgnoreCurrent(new AktivoValenceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 2:
                            switch (current_postiion) {
                                case 0:
                                    mActivity.clearBackStack();
                                    mActivity.clearBackStackFragmets();
                                    mActivity.onselectTabPostion(4);
                                    mActivity.pushFragmentDontIgnoreCurrent(new AktivoMyStatsFragment(), mActivity.FRAGMENT_JUST_REPLACE);
                                    break;
                                case 1:
                                    mActivity.clearBackStack();
                                    mActivity.clearBackStackFragmets();
                                    mActivity.onselectTabPostion(3);
                                    mActivity.pushFragmentDontIgnoreCurrent(new CompletedAktivoFragment(), mActivity.FRAGMENT_JUST_REPLACE);
                                    break;
                                case 2:
                                    mActivity.clearBackStack();
                                    mActivity.clearBackStackFragmets();
                                    mActivity.onselectTabPostion(2);
                                    mActivity.pushFragmentDontIgnoreCurrent(new ExerciseFragmet(), mActivity.FRAGMENT_JUST_REPLACE);
                                    break;

                                default:
                                    break;
                            }

                            break;
                    }


                }


            });
            return rootView;
        }
    }



    /*=============================Adaptor Sider======================================================*/

    public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.MyViewHolder> {

        private ArrayList<String> sliderList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_string)
            TextView tv_name;

            public MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        public SliderAdapter(ArrayList<String> langList) {
            this.sliderList = langList;
        }

        public void setDataUpdate(ArrayList<String> langList) {
            if (langList.size() > 0) {
                ll_indicator.setVisibility(View.VISIBLE);
            } else {
                ll_indicator.setVisibility(View.GONE);

            }
            this.sliderList = langList;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewpager_only_seting, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

            // final VehicalBrandList vehicalBrand = vehicalBrandLists.get(position);
            holder.tv_name.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));

            if (sliderList.get(position) != null) {
                if (Validation.isRequiredField(sliderList.get(position))) {
                    holder.tv_name.setText(Html.fromHtml(sliderList.get(position)));
                }
               /* if (Validation.isRequiredField(homedataList.get(position).getImg())) {
                    holder.img.setImageURI(homedataList.get(position).getImg());
                }*/
                holder.tv_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int current_postiion = position;
                        int condtion = SLIDER_PUSH_FRAMENT_CONDITION;
                        switch (condtion) {
                            case 0:
                                switch (current_postiion) {
                                    case 0:

                                        mActivity.tagmanager("Aktivo Score Carousel 1", "home_carousel1_click");
                                        //  mActivity.clearBackStack();
                                        // mActivity.clearBackStackFragmets();
                                        //  mActivity.onselectTabPostion(4);
                                        mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                                        break;

                                    case 1:
                                        mActivity.tagmanager("Aktivo Score Carousel 2", "home_carousel2_click");
                              /*  mActivity.clearBackStack();
                                mActivity.clearBackStackFragmets();
                                mActivity.onselectTabPostion(3);*/
                                        mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                                        break;
                                    case 2:
                               /* mActivity.clearBackStack();
                                mActivity.clearBackStackFragmets();
                                mActivity.onselectTabPostion(2);*/
                                        mActivity.tagmanager("Aktivo Score Carousel 3", "home_carousel3_click");
                                        mActivity.pushFragmentDontIgnoreCurrent(new AktivoValenceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                                        break;

                                    default:
                                        break;
                                }

                                break;
                            case 1:
                                switch (current_postiion) {
                                    case 0:
                                        //  mActivity.clearBackStack();
                                        // mActivity.clearBackStackFragmets();
                                        //  mActivity.onselectTabPostion(4);
                                        mActivity.tagmanager("Aktivo Score Carousel 1", "home_carousel1_click");
                                        mActivity.pushFragmentDontIgnoreCurrent(new EditProfileFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                                        break;
                                    case 1:
                                        mActivity.tagmanager("Aktivo Score Carousel 2", "home_carousel2_click");
                              /*  mActivity.clearBackStack();
                                mActivity.clearBackStackFragmets();
                                mActivity.onselectTabPostion(3);*/
                                        mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                                        break;
                                    case 2:
                                        mActivity.tagmanager("Aktivo Score Carousel 3", "home_carousel3_click");
                               /* mActivity.clearBackStack();
                                mActivity.clearBackStackFragmets();
                                mActivity.onselectTabPostion(2);*/
                                        mActivity.pushFragmentDontIgnoreCurrent(new AktivoValenceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case 2:
                                switch (current_postiion) {
                                    case 0:
                                        mActivity.tagmanager("Aktivo Score Carousel 1", "home_carousel1_click");
                                        mActivity.clearBackStack();
                                        mActivity.clearBackStackFragmets();
                                        mActivity.onselectTabPostion(4);
                                        mActivity.pushFragmentDontIgnoreCurrent(new AktivoMyStatsFragment(), mActivity.FRAGMENT_JUST_REPLACE);
                                        break;
                                    case 1:
                                        mActivity.tagmanager("Aktivo Score Carousel 2", "home_carousel2_click");
                                        mActivity.clearBackStack();
                                        mActivity.clearBackStackFragmets();
                                        mActivity.onselectTabPostion(3);
                                        mActivity.pushFragmentDontIgnoreCurrent(new CompletedAktivoFragment(), mActivity.FRAGMENT_JUST_REPLACE);
                                        break;
                                    case 2:
                                        mActivity.tagmanager("Aktivo Score Carousel 3", "home_carousel3_click");
                                        mActivity.clearBackStack();
                                        mActivity.clearBackStackFragmets();
                                        mActivity.onselectTabPostion(2);
                                        mActivity.pushFragmentDontIgnoreCurrent(new ExerciseFragmet(), mActivity.FRAGMENT_JUST_REPLACE);
                                        break;

                                    default:
                                        break;
                                }

                                break;
                        }


                    }
                });

            }


        }

        @Override
        public int getItemCount() {
            return sliderList.size();
        }
    }

    private void setSloiderIndicateror(int position) {
        switch (position) {
            case 0:

                check_first.setImageResource(R.drawable.indicatro_selected_second);
                check_second.setImageResource(R.drawable.inselected_undicate_second);
                check_third.setImageResource(R.drawable.inselected_undicate_second);
                break;
            case 1:

                check_first.setImageResource(R.drawable.inselected_undicate_second);
                check_second.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.indicatro_selected_second));
                check_third.setImageResource(R.drawable.inselected_undicate_second);
                break;

            case 2:

                check_first.setImageResource(R.drawable.inselected_undicate_second);
                check_second.setImageResource(R.drawable.inselected_undicate_second);
                check_third.setImageResource(R.drawable.indicatro_selected_second);
                break;

            default:
                break;
        }
    }
    /*=============================Adaptor======================================================*/

    public class HomeDataAdaptor extends RecyclerView.Adapter<HomeDataAdaptor.MyViewHolder> {

        private List<TodayYouhaveModel> todayYouhaveList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.pic_first)
            SimpleDraweeView pic_first;
            @BindView(R.id.tv_txt)
            TextView tv_name;

            public MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        public HomeDataAdaptor(List<TodayYouhaveModel> langList) {
            this.todayYouhaveList = langList;
        }

        public void setDataUpdate(List<TodayYouhaveModel> langList) {
            this.todayYouhaveList = langList;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_home_data, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

            // final VehicalBrandList vehicalBrand = vehicalBrandLists.get(position);
            if (todayYouhaveList.get(position) != null) {
                if (Validation.isRequiredField(todayYouhaveList.get(position).getDescraption())) {
                    holder.tv_name.setText(Html.fromHtml(todayYouhaveList.get(position).getDescraption()));
                }
                holder.tv_name.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
               /* if (Validation.isRequiredField(homedataList.get(position).getImg())) {
                    holder.img.setImageURI(homedataList.get(position).getImg());
                }*/
                holder.pic_first.setVisibility(View.GONE);
                if (Validation.isRequiredField(todayYouhaveList.get(position).getDescraption())) {
                    if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.SLIDER)) {
                        holder.pic_first.setVisibility(View.VISIBLE);
                        holder.pic_first.setImageResource(R.drawable.burned);
                    }
                    if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.RESTING_HEART_RATE)) {
                        holder.pic_first.setVisibility(View.VISIBLE);

                        holder.pic_first.setImageResource(R.drawable.heart);
                    }
                    if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.CALORIES_BURNED)) {
                        holder.pic_first.setVisibility(View.VISIBLE);

                        holder.pic_first.setImageResource(R.drawable.burned);
                    }
                    if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.ACTIVE_MINUTES)) {
                        holder.pic_first.setVisibility(View.VISIBLE);

                        holder.pic_first.setImageResource(R.drawable.burned);
                    }
                    if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.SLEEP)) {
                        holder.pic_first.setVisibility(View.VISIBLE);

                        holder.pic_first.setImageResource(R.drawable.slept);
                    }
                    if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.STEPS_TAKEN)) {

                        holder.pic_first.setVisibility(View.VISIBLE);
                        holder.pic_first.setImageResource(R.drawable.taken);
                    }
                }
            }
            holder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (condtion_postion == START_FINAL) {

                        mActivity.clearBackStack();
                        mActivity.clearBackStackFragmets();
                        mActivity.onselectTabPostion(4);

                        if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.SLIDER)) {


                            mActivity.pushFragmentDontIgnoreCurrent(AktivoMyStatsFragment.getInstance(CommonKeys.SLIDER), mActivity.FRAGMENT_JUST_REPLACE);

                        } else if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.RESTING_HEART_RATE)) {
                            mActivity.pushFragmentDontIgnoreCurrent(AktivoMyStatsFragment.getInstance(CommonKeys.RESTING_HEART_RATE), mActivity.FRAGMENT_JUST_REPLACE);
                            mActivity.tagmanager("Today: Resting BPM Stats", "home_today_restingbpm_stats_click");

                        } else if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.CALORIES_BURNED)) {
                            mActivity.tagmanager("Today: Calories Stats", "home_today_calories_stats_click");

                            mActivity.pushFragmentDontIgnoreCurrent(AktivoMyStatsFragment.getInstance(CommonKeys.CALORIES_BURNED), mActivity.FRAGMENT_JUST_REPLACE);

                        } else if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.ACTIVE_MINUTES)) {
                            mActivity.pushFragmentDontIgnoreCurrent(AktivoMyStatsFragment.getInstance(CommonKeys.ACTIVE_MINUTES), mActivity.FRAGMENT_JUST_REPLACE);

                        } else if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.SLEEP)) {
                            mActivity.pushFragmentDontIgnoreCurrent(AktivoMyStatsFragment.getInstance(CommonKeys.SLEEP), mActivity.FRAGMENT_JUST_REPLACE);
                            mActivity.tagmanager("Today: Sleep Stats", "home_today_sleep_stats_click");

                        } else if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.STEPS_TAKEN)) {
                            mActivity.pushFragmentDontIgnoreCurrent(AktivoMyStatsFragment.getInstance(CommonKeys.STEPS_TAKEN), mActivity.FRAGMENT_JUST_REPLACE);
                            mActivity.tagmanager("Today: Steps Stats", "home_today_steps_stats_click");

                        }
                    } else if (condtion_postion == ZERO_TIMER) {

                        if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.SLIDER)) {

                            mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);

                        } else if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.RESTING_HEART_RATE)) {
                            mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                            mActivity.tagmanager("Today: Resting BPM Stats", "home_today_restingbpm_stats_click");

                        } else if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.CALORIES_BURNED)) {
                            mActivity.tagmanager("Today: Calories Stats", "home_today_calories_stats_click");

                            mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);

                        } else if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.ACTIVE_MINUTES)) {
                            mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);

                        } else if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.SLEEP)) {
                            mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                            mActivity.tagmanager("Today: Sleep Stats", "home_today_sleep_stats_click");

                        } else if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.STEPS_TAKEN)) {
                            mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                            mActivity.tagmanager("Today: Steps Stats", "home_today_steps_stats_click");

                        }
                    } else if (condtion_postion == TIMER_START) {

                        if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.SLIDER)) {

                            mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);

                        } else if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.RESTING_HEART_RATE)) {
                            mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                            mActivity.tagmanager("Today: Resting BPM Stats", "home_today_restingbpm_stats_click");

                        } else if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.CALORIES_BURNED)) {
                            mActivity.tagmanager("Today: Calories Stats", "home_today_calories_stats_click");

                            mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);

                        } else if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.ACTIVE_MINUTES)) {
                            mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);

                        } else if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.SLEEP)) {
                            mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                            mActivity.tagmanager("Today: Sleep Stats", "home_today_sleep_stats_click");

                        } else if (todayYouhaveList.get(position).getTitle().equalsIgnoreCase(CommonKeys.STEPS_TAKEN)) {
                            mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                            mActivity.tagmanager("Today: Steps Stats", "home_today_steps_stats_click");

                        }

                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  mActivity.pushFragmentDontIgnoreCurrent(new AktivoValenceFragment(),mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                }
            });

        }

        @Override
        public int getItemCount() {
            return todayYouhaveList.size();
        }
    }

    Call<BaseCommanRespons> baseCommanResponsCalll;

    public void callGenerateAktivoScoreApi(final String userid, final boolean is_swirefer) {

        if (!swipe_home.isRefreshing()) {
            Methods.isProgressShow(mActivity);
        }

        baseCommanResponsCalll = WebApiClient.getInstance(mActivity).getWebApi().GenerateAktivoScore(userid);
        baseCommanResponsCalll.enqueue(new Callback<BaseCommanRespons>() {
            @Override
            public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                            if (ConnectionUtil.isInternetAvailable(mActivity)) {
                                callTodayYouHaveApi(userDetailTable.get_id(), is_swirefer);
                            } else {
                                // tv_not_found.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (ConnectionUtil.isInternetAvailable(mActivity)) {
                                callTodayYouHaveApi(userDetailTable.get_id(), is_swirefer);
                            } else {
                                //tv_not_found.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseCommanRespons> call, Throwable throwable) {
                // common_methods.setExceptionMessage(throwable, mActivity);
                Methods.isProgressHide();
                if (is_visible_fragment) {
                    if (swipe_home.isRefreshing()) {
                        swipe_home.setRefreshing(false);
                    }

                    if (ConnectionUtil.isInternetAvailable(mActivity)) {
                        callTodayYouHaveApi(userDetailTable.get_id(), false);
                    } else {
                        tv_not_found.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void showChangeLangDialog() {
        dialogBuilder = new Dialog(getActivity());
        dialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBuilder.setContentView(R.layout.dilaoge_what_is_aktivo);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        tv_closer_what_id_dialog = (ImageView) dialogBuilder.findViewById(R.id.imgCancle);
        tv_detail = (TextView) dialogBuilder.findViewById(R.id.tv_what_is_activo_detail);
        tv_nodata = (TextView) dialogBuilder.findViewById(R.id.tv_noDataFound);
        TextView tv_what_activo = (TextView) dialogBuilder.findViewById(R.id.tv_what_activo);
        tv_what_activo.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_detail.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        progressBar_what_is_activito = (ProgressBar) dialogBuilder.findViewById(R.id.progressbar);
        List<PostCMS> postCMS = PostCMS.getPostCMSDetail();
        if (postCMS != null && postCMS.size() > 0) {
            for (int i = 0; i < postCMS.size(); i++) {
                if (postCMS.get(i).getCode().equalsIgnoreCase(WHAT_IS_AKTIVO)) {
                    String what_is_aktivo = postCMS.get(i).getDescription();
                    if (Validation.isRequiredField(what_is_aktivo)) {

                        tv_detail.setText(Html.fromHtml(what_is_aktivo));
                        tv_nodata.setVisibility(View.GONE);

                    } else {
                        tv_not_found.setVisibility(View.VISIBLE);
                        tv_nodata.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (ConnectionUtil.isInternetAvailable(mActivity)) {
                        callCmsApi();
                    }
                }

            }
        } else if (ConnectionUtil.isInternetAvailable(mActivity)) {
            callCmsApi();
        } else {
            tv_not_found.setVisibility(View.VISIBLE);
        }
        dialogBuilder.show();
        tv_closer_what_id_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });

    }

    Call<PostCMSReponse> postCMSReponseCalll;

    private void callCmsApi() {
        tv_not_found.setVisibility(View.GONE);
        progressBar_what_is_activito.setVisibility(View.VISIBLE);
        postCMSReponseCalll = WebApiClient.getInstance(mActivity).getWebApi().callPostCMSApi();
        postCMSReponseCalll.enqueue(new Callback<PostCMSReponse>() {
            @Override
            public void onResponse(Call<PostCMSReponse> call, Response<PostCMSReponse> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getData() != null) {
                            Delete.table(PostCMS.class);
                            List<PostCMS> data = response.body().getData();
                            if (data != null && data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    data.get(i).save();
                                    if (data.get(i).getCode().equalsIgnoreCase(WHAT_IS_AKTIVO)) {
                                        String what_is_aktivo = data.get(i).getDescription();
                                        if (Validation.isRequiredField(what_is_aktivo)) {

                                            tv_detail.setText(Html.fromHtml(what_is_aktivo));
                                            tv_nodata.setVisibility(View.GONE);

                                        } else {
                                            tv_not_found.setVisibility(View.VISIBLE);
                                            tv_nodata.setVisibility(View.VISIBLE);
                                        }
                                    }

                                }
                            }

                        }
                    }
                }
                //      progressbar.setVisibility(View.GONE);
                progressBar_what_is_activito.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<PostCMSReponse> call, Throwable t) {
                //    progressbar.setVisibility(View.GONE);
                if (is_visible_fragment) {
                    progressBar_what_is_activito.setVisibility(View.GONE);
                    tv_not_found.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    Call<BaseCommanRespons> validiccallapi;

    public void callSaveGoogleFitData(final String user_id, final boolean is_swifRefresh) {
        if (!is_swifRefresh) {
            Methods.isProgressShow(mActivity);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Date date = calendar.getTime();
        /* whatever*/
        //c.setTimeZone(...); if necessary
        //  format.setCalendar(calendar);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.e("test", "calendar data_time" + sdf.format(date));
        validiccallapi = WebApiClient.getInstance(mActivity).getWebApi().callSaveGoogleFitData(user_id, "validic", "", sdf.format(date));
        validiccallapi.enqueue(new Callback<BaseCommanRespons>() {
            @Override
            public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                            callGenerateAktivoScoreApi(user_id, false);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<BaseCommanRespons> call, Throwable throwable) {
                Methods.isProgressHide();
                // common_methods.setExceptionMessage(throwable, mActivity);
            }
        });


    }

    public ArrayList<String> getTimerZeroTextSlider() {
        ArrayList<String> slider_dateList = new ArrayList<>();
        slider_dateList.add("Have a wearable? Allow Aktivo to <b>Sync now!</b>");
        slider_dateList.add("Let’s set you up! <b>Click here</b> to connect with GoogleFit.");
        slider_dateList.add("Why wait? Get started with our emotions analytics platform - <b>Aktivo Positivity!</b>");
        return slider_dateList;
    }

    public ArrayList<String> getTimerTwentyFourHourSlider(String user_profile, String platform_connectivity_status) {

        ArrayList<String> slider_dateList = new ArrayList<>();
        if (!user_profile.equalsIgnoreCase("100")) {
            slider_dateList.add("Your profile is <b>" + user_profile + "%</b> complete. Edit now for a personalized experience!");
        } else {
            slider_dateList.add("Your Profile is 100% <b>complete!</b>");
        }
        //both condtion

        if (platform_connectivity_status.equalsIgnoreCase("Yes")) {
            slider_dateList.add("Your <b>wearable</b> has been successfully synced.");
        } else {
            slider_dateList.add("Have a wearable? Allow Aktivo to <b>Sync now!</b>");
        }
        //or
        //  slider_dateList.add("Your <b>wearable</b> has been successfully synced.");

        slider_dateList.add("Why wait? Get started with our emotions analytics platform -<b>Aktivo Positivity!</b>");
        return slider_dateList;
    }


    public List<TodayYouhaveModel> getTimerZeroThattime(int activo_score) {
        List<TodayYouhaveModel> todayYouhaveList = new ArrayList<>();

        if (activo_score > 0) {
            tv_txt_valence.setText(Html.fromHtml("Your Aktivo Positivity score is <b>" + activo_score + "</b>. Great start! Use it anytime to check how you feel."));
        } else {
            tv_txt_valence.setText(Html.fromHtml("Aktivo Positivity allows you to find out how positive or negative you feel. Try it for yourself!"));
        }
        todayYouhaveList.add(new TodayYouhaveModel(CommonKeys.CALORIES_BURNED, "Simply connect your wearable or your phone to get started. To connect,<b>click here!</b>"));

        todayYouhaveList.add(new TodayYouhaveModel(CommonKeys.STEPS_TAKEN, "Want to get started? Simply connect your wearable or your phone. To connect,<b>click here!</b>"));
        todayYouhaveList.add(new TodayYouhaveModel(CommonKeys.SLEEP, "Sleep is important, so is connecting your wearable or phone to Aktivo. To connect,<b>click here</b>"));
        todayYouhaveList.add(new TodayYouhaveModel(CommonKeys.RESTING_HEART_RATE, "Hear your heart beat? Connect your wearable and start to measure. To connect,<b>click here!</b>"));


        return todayYouhaveList;

        //   progressbar.setVisibility(View.GONE);
    }

    public List<TodayYouhaveModel> getTwentyFourHourList(int psoti__score) {
        List<TodayYouhaveModel> todayYouhaveList = new ArrayList<>();
        if (psoti__score > 0) {
            tv_txt_valence.setText(Html.fromHtml("Your Aktivo Positivity score is <b>" + psoti__score + "</b>. Great start! Use it anytime to check how you feel."));
        } else {
            tv_txt_valence.setText(Html.fromHtml("Aktivo Positivity allows you to find out how positive or negative you feel. Try it for yourself!"));
        }
        todayYouhaveList.add(new TodayYouhaveModel(CommonKeys.CALORIES_BURNED, "Simply connect your wearable or your phone to get started. To connect, <b>click here!</b>"));

        todayYouhaveList.add(new TodayYouhaveModel(CommonKeys.STEPS_TAKEN, "Want to get started? Simply connect your wearable or your phone. To connect,<b>click here!</b>"));
        todayYouhaveList.add(new TodayYouhaveModel(CommonKeys.SLEEP, "Sleep is important, so is connecting your wearable or phone to Aktivo. To connect,<b>click here</b>"));
        todayYouhaveList.add(new TodayYouhaveModel(CommonKeys.RESTING_HEART_RATE, "Hear your heart beat? Connect your wearable and start to measure. To connect,<b>click here!</b>"));


        return todayYouhaveList;

        //   progressbar.setVisibility(View.GONE);
    }


    //======================================================google fit=======================================
    @CheckResult
    public boolean fitInstalled() {
        if (!swipe_home.isRefreshing()) {
            methods.isProgressShow(mActivity);
        }
        try {
            getActivity().getPackageManager().getPackageInfo("com.google.android.apps.fitness", PackageManager.GET_ACTIVITIES);
            Log.e("Return", "True");
            buildFitnessClient();
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // methods.isProgressHide();
            callGenerateAktivoScoreApi(user_id, true);
            Log.e("Return", "False");
            mActivity.tagmanager("Data Error Sync popup", "home_gf_sync_error");
            common_methods.setCutemDialogMessage(mActivity, "To continue, please download Google Fit from the Play Store.");
            //Toast.makeText(getActivity(), "Please install Google-Fit", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    private void buildFitnessClient() {
        try {
           /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                                                @Override
                                                public void onConnected(Bundle bundle) {
                                                    Log.i("test", "Connected!!!");
                                                    // Now you can make calls to the Fitness APIs.  What to do?
                                                    // Look at some data!!
                                                    new VerifyDataTask().execute();
                                                }

                                                @Override
                                                public void onConnectionSuspended(int i) {
                                                    // If your connection to the sensor gets lost at some point,
                                                    // you'll be able to determine the reason and react to it here.
                                                    if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                                        Log.i("test", "Connection lost.  Cause: Network Lost.");
                                                    } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                                        Log.i("test", "Connection lost.  Reason: Service Disconnected");
                                                    }
                                                }
                                            }
                    )
                    .enableAutoManage(getActivity(), 0, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                            mActivity.tagmanager("Data Error Sync popup", "home_gf_sync_error");

                            Log.i("test", "Google Play services connection failed. Cause: " + result.toString());
                            callGenerateAktivoScoreApi(user_id, false);
                           /* common_methods.setCutemDialogMessage(mActivity, "Google Play services connection failed. Cause: " +
                                    result.toString());*/
                            //  common_methods.setCutemDialogMessage(mActivity, "===>>"+ result.toString());
                            common_methods.setCutemDialogMessage(mActivity, "Oops, looks like we’re unable to connect to Google Fit. We’ve called in the experts! Please try again soon.");
                            /*Toast.makeText(getActivity(), "Google Play services connection failed. Cause: " +
                                    result.toString(), Toast.LENGTH_LONG).show();*/
                        }
                    })
                    .build();
        } catch (Exception e) {
            callGenerateAktivoScoreApi(user_id, false);
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
                            Log.e("get step", Steps + ":::" + StepDate + ":::" + StepTime);

                        }
                    }

                }
            }

        }

    }

    // For Activity
    ArrayList<String> activity;
    ArrayList<String> activityCreatedDate;
    ArrayList<String> activityCreatedTime;

    // For Light Activity
    ArrayList<String> lightActivity;
    ArrayList<String> lightActivityCreatedDate;
    ArrayList<String> lightActivityCreatedTime;

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
    ArrayList<String> allDays = new ArrayList<>();

    ArrayList<String> SleepBand = new ArrayList<>();
    ArrayList<String> SleepBandTime = new ArrayList<>();
    ArrayList<String> SleepBandDate = new ArrayList<>();
    ArrayList<String> FilterSleepBandTime = new ArrayList<>();
    ArrayList<String> FilterSleepBandDate = new ArrayList<>();

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

            activity = new ArrayList<String>();
            activityCreatedDate = new ArrayList<String>();
            activityCreatedTime = new ArrayList<String>();

            lightActivity = new ArrayList<String>();
            lightActivityCreatedDate = new ArrayList<String>();
            lightActivityCreatedTime = new ArrayList<String>();

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
                        && !bucket.getActivity().equalsIgnoreCase("on_foot")
                        && !bucket.getActivity().equalsIgnoreCase("sleep.light")
                        && !bucket.getActivity().equalsIgnoreCase("sleep.awake")
                        && !bucket.getActivity().equalsIgnoreCase("sleep.deep")
                        && !bucket.getActivity().equalsIgnoreCase("rem.sleep")
                        && !bucket.getActivity().equalsIgnoreCase("sleep")
                        && !bucket.getActivity().equalsIgnoreCase("in_vehicle") && activeTime >= 10 &&
                        StartDate.substring(0, StartDate.indexOf(' ')).equalsIgnoreCase(EndDate.substring(0, EndDate.indexOf(' ')))) {
                    activity.add(String.valueOf(activeTime));
                    activityCreatedDate.add(StartDate.substring(0, StartDate.indexOf(' ')));
                    activityCreatedTime.add(StartDate.substring(StartDate.indexOf(' ') + 1));

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
                    lightActivity.add(String.valueOf(activeTime));
                    lightActivityCreatedDate.add(StartDate.substring(0, StartDate.indexOf(' ')));
                    lightActivityCreatedTime.add(StartDate.substring(StartDate.indexOf(' ') + 1));
                }

                if (bucket.getActivity().equalsIgnoreCase("on_foot") && StartDate.substring(0, StartDate.indexOf(' ')).equalsIgnoreCase(EndDate.substring(0, EndDate.indexOf(' ')))) {
                    lightActivity.add(String.valueOf(activeTime));
                    lightActivityCreatedDate.add(StartDate.substring(0, StartDate.indexOf(' ')));
                    lightActivityCreatedTime.add(StartDate.substring(StartDate.indexOf(' ') + 1));
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

//                        Date date3 = format.parse(EndDate.substring(EndDate.indexOf(' ') + 1));
//                        Date date4 = format.parse("00:00:00");

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
                Log.i("test", "\tField: " + field.getName() +
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

        if (lightActivityCreatedDate.size() > 0 && lightActivityCreatedTime.size() > 0) {
            FilterLA = new ArrayList<>();
            FilterLACreatedDate = new ArrayList<>();
            FilterLACreatedTime = new ArrayList<>();

            String LAPreview = lightActivityCreatedDate.get(0);
            String LAPreviewTime = lightActivityCreatedTime.get(0);
            double LAsum = 0;

            for (int i = 0; i < lightActivity.size(); i++) {
                if (LAPreview.equalsIgnoreCase(lightActivityCreatedDate.get(i))) {
                    LAPreview = lightActivityCreatedDate.get(i);
                    LAPreviewTime = lightActivityCreatedTime.get(i);
                    LAsum = LAsum + Double.valueOf(lightActivity.get(i));
                } else {
                    FilterLACreatedDate.add(LAPreview);
                    FilterLACreatedTime.add(LAPreviewTime);
                    LAPreview = lightActivityCreatedDate.get(i);
                    FilterLA.add(String.valueOf(LAsum));
                    LAsum = 0;
                    LAsum = LAsum + Double.valueOf(lightActivity.get(i));
                }
            }

            FilterLA.add(String.valueOf(LAsum));
            FilterLACreatedDate.add(LAPreview);
            FilterLACreatedTime.add(LAPreviewTime);

        } else {
            Log.e("test", "Data is not available");
        }

        // For Activity
        if (activityCreatedDate.size() > 0 && activityCreatedTime.size() > 0) {
            FilterActivity = new ArrayList<>();
            FilterActivityCreatedDate = new ArrayList<>();
            FilterActivityCreatedTime = new ArrayList<>();
            String preview = activityCreatedDate.get(0);
            String previewtime = activityCreatedTime.get(0);
            double sum = 0;
            for (int i = 0; i < activity.size(); i++) {
                if (preview.equalsIgnoreCase(activityCreatedDate.get(i))) {
                    preview = activityCreatedDate.get(i);
                    previewtime = activityCreatedTime.get(i);
                    sum = sum + Double.valueOf(activity.get(i));
                } else {
                    FilterActivityCreatedDate.add(preview);
                    FilterActivityCreatedTime.add(previewtime);
                    preview = activityCreatedDate.get(i);
                    FilterActivity.add(String.valueOf(sum));
                    sum = 0;
                    sum = sum + Double.valueOf(activity.get(i));

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
//        if (StepDate.size() > 0 && StepTime.size() > 0) {
//            FilterSteps = new ArrayList<>();
//            FilterStepDate = new ArrayList<>();
//            FilterStepTime = new ArrayList<>();
//
//            String stepPreview = StepDate.get(0);
//            String stepPreviewTime = StepTime.get(0);
//
//            double stepSum = 0;
//            for (int i = 0; i < Steps.size(); i++) {
//                if (stepPreview.equalsIgnoreCase(StepDate.get(i))) {
//                    stepPreview = StepDate.get(i);
//                    stepPreviewTime = StepTime.get(i);
//                    stepSum = stepSum + Double.valueOf(Steps.get(i));
//                } else {
//                    FilterStepDate.add(stepPreview);
//                    FilterStepTime.add(stepPreviewTime);
//                    //     Log.e("get sum=>>", sum + "");
//                    stepPreview = StepDate.get(i);
//                    FilterSteps.add(String.valueOf(stepSum));
//                    stepSum = 0;
//                    stepSum = stepSum + Double.valueOf(Steps.get(i));
//                }
//            }
//
//            FilterSteps.add(String.valueOf(stepSum));
//            FilterStepDate.add(stepPreview);
//            FilterStepTime.add(stepPreviewTime);
//        } else {
//            Log.e("test", "Data is not available");
//        }

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

   /* public void CreateJsonResponse() {
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

                // new steps

                for (int j = 0; j < Steps.size(); j++) {
                    JSONObject stepOBJ = new JSONObject();
                    stepOBJ.put("steps", Steps.get(j));
                    stepOBJ.put("created_date", StepDate.get(j));
                    stepOBJ.put("created_time", StepTime.get(j));
                    stepsarray.put(j, stepOBJ);
                }

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

                Log.e("test", "data not available");

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

    public void bindSavePostInfo(final String jsonString) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Date date = calendar.getTime();

        String connected_type = MyPreferences.getPref(mActivity, CommonKeys.CONNECTEDTYPE);
        if (!Validation.isRequiredField(connected_type)) {
            methods.isProgressHide();
            if (swipe_home.isRefreshing()) {
                swipe_home.setRefreshing(false);
            }
            return;
        }

        String user_time = "";
        if (Validation.isRequiredField(you_connect_timer_commeing_you_have_api)) {
            user_time = you_connect_timer_commeing_you_have_api;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Log.e("test", "calendar data_time" + sdf.format(date));
            user_time = sdf.format(date);
        }
        /* whatever*/
        //c.setTimeZone(...); if necessary
        //  format.setCalendar(calendar);


        WebApiClient.getInstance(mActivity).getWebApi().callSaveGoogleFitData(user_id, connected_type, jsonString, user_time).enqueue(new Callback<BaseCommanRespons>() {
            @Override
            public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                            mClient.stopAutoManage(getActivity());
                            mClient.disconnect();
                            common_methods.setCutemDialogMessage(mActivity, success_full_synchronize);
                            //Toast.makeText(getActivity(), "Your Google-Fit data were insterted successfully", Toast.LENGTH_LONG).show();
                            MyPreferences.setPref(mActivity, "ConnectedType", "google_fit");      //store ConnectedType
                            //refresh data youhave
                            callGenerateAktivoScoreApi(user_id, false);
                            mActivity.tagmanager("Data Successful Sync popup", "home_gf_sync_success");

                        }
                    }
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        methods.isProgressHide();

                    }
                });
            }

            @Override
            public void onFailure(Call<BaseCommanRespons> call, Throwable throwable) {
                // common_methods.setExceptionMessage(throwable, mActivity);
                if (is_visible_fragment) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            methods.isProgressHide();
                        }
                    });
                    if (swipe_home.isRefreshing()) {
                        swipe_home.setRefreshing(false);
                    }
                }
            }
        });
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

    public void openDataAvalibityNotAvaliableDialog(final boolean is_validic) {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.data_avalibality_not_available);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_yes = (TextView) dialog.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) dialog.findViewById(R.id.tv_no);
        TextView tv_header = (TextView) dialog.findViewById(R.id.tv_title);
        tv_header.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
        tv_yes.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_no.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                if (is_validic) {
                    fitInstalled();
                    CommonKeys.is_Progress_bar_update = false;
                } else {
                    mActivity.pushFragmentDontIgnoreCurrent(new ConnectDeviceFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                }

            }
        });

        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
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

    boolean is_visible_fragment = true;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        is_visible_fragment = false;
        if (baseCommanResponsCalll != null) {
            baseCommanResponsCalll.cancel();
        }
        if (validiccallapi != null) {
            validiccallapi.cancel();
        }
        if (postCMSReponseCalll != null) {
            postCMSReponseCalll.cancel();
        }
        if (todayYouHaveResponseCall != null) {
            todayYouHaveResponseCall.cancel();
        }
        // unbinder.unbind();
        Log.e("test", "onDestroyView call==>>");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if (homeDataAdaptor != null) {
            homeDataAdaptor.;
            mViewPagerAdapter = null;
        }*/
        //unbinder.unbind();
        Log.e("test", "on Destory call==>>");
    }
}
