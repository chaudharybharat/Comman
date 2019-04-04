package com.aktivo.fragment;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aktivo.GraphMystats.CustomGraphCalories;
import com.aktivo.GraphMystats.CustomGraphHeart;
import com.aktivo.GraphMystats.CustomGraphPositivity;
import com.aktivo.GraphMystats.CustomProgressBarAktivo;
import com.aktivo.GraphMystats.CustomeGraphSleep;
import com.aktivo.GraphMystats.CustomeGraphStepTaken;
import com.aktivo.Model.CarouselResponse;
import com.aktivo.Model.StatuName;
import com.aktivo.Model.Week;
import com.aktivo.R;
import com.aktivo.Utils.CenterLayoutManager;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.ConnectionUtil;
import com.aktivo.Utils.ProgressbarCustome;
import com.aktivo.Utils.RecyclerTouchListener;
import com.aktivo.durationheartModel.ProgressItem;
import com.aktivo.response.MyStatsResponse;
import com.aktivo.response.SeekBarData;
import com.aktivo.response.UserDetailTable;
import com.aktivo.webservices.WebApiClient;
import com.commonmodule.mi.utils.Validation;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AktivoMyStatsFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.recycle_view_toolbar)
    RecyclerView recycle_view_toolbar;
    @BindView(R.id.recyclerview_week)
    RecyclerView recyclerview_week;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tv_week_value)
    TextView tv_week_value;
    @BindView(R.id.graph_activo)
    CustomProgressBarAktivo graph_activo;
    @BindView(R.id.graph_calories)
    CustomGraphCalories graph_calories;
    @BindView(R.id.graph_stepTaken)
    CustomeGraphStepTaken graph_stepTaken;
    @BindView(R.id.graph_positity)
    CustomGraphPositivity graph_positity;
    @BindView(R.id.graph_sleep)
    CustomeGraphSleep graph_sleep;
    @BindView(R.id.graph_heart)
    CustomGraphHeart graph_heart;
    @BindView(R.id.pb)
    ProgressbarCustome pb;
    @BindView(R.id.tv_progressbar)
    TextView tv_progressbar;
    @BindView(R.id.tv_text_progress)
    TextView tv_text_progress;
    @BindView(R.id.iv_next)
    ImageView iv_next;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_aktivo)
    TextView tv_aktivo;
    @BindView(R.id.tv_selected_date)
    TextView tv_selected_date;
    @BindView(R.id.llMaScroll)
    ScrollView llMaScroll;
    ToolbarAdaptor toolbarAdaptor;
    private List<StatuName> statusList = new ArrayList<>();
    CenterLayoutManager layoutManager;
    LinearLayoutManager mLayoutManager_toolbar;
    private ArrayList<ProgressItem> progressItemList;
    private ArrayList<SeekBarData> seekBars_list;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    String MY_STATS = "AKTIVO";
    private List<Week> movieListData = new ArrayList<>();
    int current_week = 0;
    int selected_week = 0;
    int current_year = 0;
    String user_id = "";
    ArrayList<String> myStatsApikey_name;
    ArrayList<Integer> mColorList;
    public static String COMMING_FROM = "comming_from";
    PickerAdapter adapterWeek;
    int selected_value = 0;
    int recycleview_postion_current_week = 0;
    boolean scolling_continue = false;
    int device_width = 0;
    Handler scollHander;
    Runnable runnable_scolling;

    Unbinder unbinder;
    boolean call_first_curren_data_api = false;
    boolean flag = false;

    public static AktivoMyStatsFragment getInstance(String comming_from) {
        AktivoMyStatsFragment aktivoMyStatsFragment = new AktivoMyStatsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(COMMING_FROM, comming_from);
        aktivoMyStatsFragment.setArguments(bundle);
        return aktivoMyStatsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aktivo_my_stats, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        setHeader();
        scollHander = new Handler();
        //set
        mColorList = new ArrayList<>();
        mColorList.add(mActivity.getResources().getColor(R.color.bar_1));
        mColorList.add(mActivity.getResources().getColor(R.color.bar_2));
        mColorList.add(mActivity.getResources().getColor(R.color.bar_3));
        mColorList.add(mActivity.getResources().getColor(R.color.bar_4));
        myStatsApikey_name = new ArrayList<>();
        myStatsApikey_name.add("AKTIVO");
        myStatsApikey_name.add("POSITIVITY");
        myStatsApikey_name.add("CALORIES");
        myStatsApikey_name.add("STEPS_TAKEN");
        myStatsApikey_name.add("BPM");
        myStatsApikey_name.add("SLEEP");
        myStatsApikey_name.add("DISTANCE_COVERED");
        initAllGraphs(0, 0);
        UserDetailTable userDetailTable = UserDetailTable.getUserDetail();
        if (userDetailTable != null) {
            if (Validation.isRequiredField(userDetailTable.get_id())) {
                user_id = userDetailTable.get_id();
            }
        }
        //initDataToSeekbar();
        // initViewpager();
        initWeekRecycleview();
        intiComponet();
        tv_week_value.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_text_progress.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
        //setProgrebarValue(70);
        String progres_str = "You have <b>a really good</b>Aktivo score! <b>Well done!</b>You have raised your score by <b>13</b> points in the last <b>14</b>days!";

        //  tv_text_progress.setText(Html.fromHtml(progres_str));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getBundleDataValue();
            }
        }, 300);
        mActivity.tagmanager("My Stats Screen", "mystats_aktivo_view");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            llMaScroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (flag == false) {
                        if (MY_STATS.equalsIgnoreCase("AKTIVO")) {
                            mActivity.tagmanager("Aktivo Stats Screen", "mystats_aktivo_scroll");
                            flag = true;
                        } else if (MY_STATS.equalsIgnoreCase("POSITIVITY")) {
                            mActivity.tagmanager("Positivity Stats Screen", "mystats_positivity_scroll");
                            flag = true;
                        } else if (MY_STATS.equalsIgnoreCase("CALORIES")) {
                            mActivity.tagmanager("Calories Stats Screen", "mystats_calories_scroll");
                            flag = true;
                        } else if (MY_STATS.equalsIgnoreCase("STEPS_TAKEN")) {
                            mActivity.tagmanager("Steps Stats Screen", "mystats_steps_scroll");
                            flag = true;
                        } else if (MY_STATS.equalsIgnoreCase("BPM")) {
                            mActivity.tagmanager("Resting BPM Stats Screen", "mystats_restingbpm_scroll");
                            flag = true;
                        } else if (MY_STATS.equalsIgnoreCase("SLEEP")) {
                            mActivity.tagmanager("Sleep Stats Screen", "mystats_sleep_scroll");
                            flag = true;
                        } else if (MY_STATS.equalsIgnoreCase("DISTANCE_COVERED")) {

                        }

                    }

                }
            });
        }
    }

    private void getBundleDataValue() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(COMMING_FROM)) {
                String comming_from = arguments.getString(COMMING_FROM);
                if (Validation.isRequiredField(comming_from)) {
                    switch (comming_from) {

                        case CommonKeys.SLIDER:
                            break;
                        case CommonKeys.POSITIVITY:
                            toolbarAdaptor.notifyItmeView(1);
                            //  recycle_view_toolbar.smoothScrollToPosition(6);
                            MY_STATS = myStatsApikey_name.get(1);
                            // callAktiviStatsApi(current_week, current_year, myStatsApikey_name.get(1));
                            break;
                        case CommonKeys.RESTING_HEART_RATE:
                            toolbarAdaptor.notifyItmeView(4);
                            //  recycle_view_toolbar.smoothScrollToPosition(6);
                            recycle_view_toolbar.getLayoutManager().scrollToPosition(6);
                            MY_STATS = myStatsApikey_name.get(4);
                            // callAktiviStatsApi(current_week, current_year, myStatsApikey_name.get(4));


                            break;

                        case CommonKeys.CALORIES_BURNED:
                            // recycle_view_toolbar.smoothScrollToPosition(3);
                            toolbarAdaptor.notifyItmeView(2);
                            recycle_view_toolbar.getLayoutManager().scrollToPosition(2);
                            MY_STATS = myStatsApikey_name.get(2);
                            // callAktiviStatsApi(current_week, current_year, myStatsApikey_name.get(2));
                            break;
                        case CommonKeys.ACTIVE_MINUTES:
                            toolbarAdaptor.notifyItmeView(3);
                            //  recycle_view_toolbar.smoothScrollToPosition(2);
                            recycle_view_toolbar.getLayoutManager().scrollToPosition(2);
                            MY_STATS = myStatsApikey_name.get(3);
                            // callAktiviStatsApi(current_week, current_year, myStatsApikey_name.get(3));

                            break;
                        case CommonKeys.SLEEP:
                            toolbarAdaptor.notifyItmeView(5);
                            // recycle_view_toolbar.smoothScrollToPosition(6);
                            recycle_view_toolbar.getLayoutManager().scrollToPosition(6);
                            MY_STATS = myStatsApikey_name.get(5);
                            // callAktiviStatsApi(current_week, current_year, myStatsApikey_name.get(5));
                            break;
                        case CommonKeys.STEPS_TAKEN:
                            toolbarAdaptor.notifyItmeView(3);
                            //  recycle_view_toolbar.smoothScrollToPosition(4);
                            recycle_view_toolbar.getLayoutManager().scrollToPosition(3);
                            MY_STATS = myStatsApikey_name.get(3);
                            // callAktiviStatsApi(current_week, current_year, myStatsApikey_name.get(3));
                            break;

                        default:
                            break;
                    }
                } else {
                    // callAktiviStatsApi(current_week, current_year, MY_STATS);

                }
            } else {
                // callAktiviStatsApi(current_week, current_year, MY_STATS);

            }
        } else {
            // callAktiviStatsApi(current_week, current_year, MY_STATS);
        }

    }

    private void setProgrebarValue(final int progrebarValue) {
        // Set the progress status zero on each button click
        progressStatus = 0;
        // Start the lengthy operation in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus <= progrebarValue) {
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
                            //  tv_progressbar.setText(progressStatus + ".");
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

    private void initWeekRecycleview() {
        tv_week_value.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        Calendar now = Calendar.getInstance();
        device_width = common_methods.getDeviceWidth(mActivity);
        movieListData = new ArrayList<>();
        current_week = now.get(Calendar.WEEK_OF_YEAR);
        selected_week = current_week;

        current_year = now.get(Calendar.YEAR);
        int totalWeeks = common_methods.getTotalWeeksInYear(current_year);
        movieListData = Week.getListVlaue(totalWeeks);
        recycleview_postion_current_week = current_week + 1;

        for (int i = 0; i < movieListData.size(); i++) {
            if (i > recycleview_postion_current_week) {
                movieListData.get(i).setDisable(true);
            } else {
                movieListData.get(i).setDisable(false);
            }

        }
        layoutManager = new CenterLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        recyclerview_week.setLayoutManager(layoutManager);
        //set current week

        recyclerview_week.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                int firstPos = layoutManager.findFirstVisibleItemPosition();
                int lastPos = layoutManager.findLastVisibleItemPosition();
                int middle = Math.abs(lastPos - firstPos) / 2 + firstPos;

                for (int i = 0; i < adapterWeek.getItemCount(); i++) {
                    if (i == middle) {
                        int a = i - 1;
                        if (i >= recycleview_postion_current_week + 1) {
                            recyclerview_week.scrollToPosition(current_week - 1);

                            if (call_first_curren_data_api) {
                                if (scollHander != null) {
                                    scollHander.removeCallbacks(runnable_scolling);
                                }
                                selected_week = a;
                                tv_selected_date.setText("" + a);
                                scollHander.postDelayed(runnable_scolling, 100);
                            }

                            // layoutManager.smoothScrollToPosition(recyclerview_week, null,recycleview_postion_current_week);
                        } else {
                            if (call_first_curren_data_api) {
                                if (scollHander != null) {
                                    scollHander.removeCallbacks(runnable_scolling);
                                }
                                scolling_continue = true;
                                selected_week = a;
                                tv_selected_date.setText("" + a);
                                scollHander.postDelayed(runnable_scolling, 100);
                            }

                        }
                    } else {

                    }
                }


            }
        });

        runnable_scolling = new Runnable() {
            @Override
            public void run() {
                scolling_continue = false;
                callAktiviStatsApi(selected_week, current_year, MY_STATS);

                Log.e("test", "setlected date:=>" + selected_week);
                if (MY_STATS.equalsIgnoreCase("AKTIVO")) {

                    mActivity.tagmanager("Aktivo Stats Horizontal Week Calender", "mystats_aktivo_week_scroll");

                } else if (MY_STATS.equalsIgnoreCase("POSITIVITY")) {
                    mActivity.tagmanager("Positivity Stats Horizontal Week Calender", "mystats_positivity_week_scroll");

                } else if (MY_STATS.equalsIgnoreCase("CALORIES")) {
                    mActivity.tagmanager("Calories Stats Horizontal Week Calender", "mystats_calories_week_scroll");

                } else if (MY_STATS.equalsIgnoreCase("STEPS_TAKEN")) {
                    mActivity.tagmanager("Steps Stats Horizontal Week Calender", "mystats_steps_week_scroll");

                } else if (MY_STATS.equalsIgnoreCase("BPM")) {
                    mActivity.tagmanager("Resting BPM Stats Horizontal Week Calender", "mystats_restingbpm_week_scroll");

                } else if (MY_STATS.equalsIgnoreCase("SLEEP")) {
                    mActivity.tagmanager("Sleep Stats Horizontal Week Calender", "mystats_sleep_week_scroll");

                } else if (MY_STATS.equalsIgnoreCase("DISTANCE_COVERED")) {

                }
            }
        };
        adapterWeek = new PickerAdapter(mActivity, movieListData, recyclerview_week);
        SnapHelper snapHelper = new LinearSnapHelper();
        //  rv.setLayoutManager(pickerLayoutManager);
        snapHelper.attachToRecyclerView(recyclerview_week);
        recyclerview_week.setAdapter(adapterWeek);
        Log.e("test", "current_week=>" + current_week);


        recyclerview_week.setOnFlingListener(snapHelper);
        recyclerview_week.addOnItemTouchListener(new RecyclerTouchListener(mActivity, recyclerview_week, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {


                if (position > recycleview_postion_current_week) {
                    layoutManager.smoothScrollToPosition(recyclerview_week, null, recycleview_postion_current_week);
                } else {
                    layoutManager.smoothScrollToPosition(recyclerview_week, null, position);
                }

                //  rv.smoothScrollToPosition(position);
              /*  final int scrollX = (view.getLeft() - (center)) + (view.getWidth() / 2);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rv.scrollBy(scrollX, 0);
                    }
                }, 100);*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }

            @Override
            public void onDoubleClick(View view, int position) {

            }
        }));

        // recyclerview_week.smoothScrollToPosition(current_week);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutManager.smoothScrollToPosition(recyclerview_week, null, current_week + 1);
                Log.e("test", "selected_week=>" + selected_week);
                callAktiviStatsApi(current_week, current_year, MY_STATS);
                selected_week = current_week;
                tv_selected_date.setText("" + current_week);


            }
        }, 100);


        Log.e("test", "current_week=>" + current_week);
        //  layoutManager.smoothScrollToPosition(recyclerview_week,null,15);


    }


    private void initViewpager() {
        setupViewPager(viewPager);
        //   tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //   tabLayoutCurren(position);
                Log.e("test", "position" + position);
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the Tab.

            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        List<CarouselResponse> carouselResponses = new ArrayList<>();
        carouselResponses.add(new CarouselResponse(progressItemList));
        carouselResponses.add(new CarouselResponse(progressItemList));
        carouselResponses.add(new CarouselResponse(progressItemList));
        carouselResponses.add(new CarouselResponse(progressItemList));

        CarouselFragmentAdaptor dynamicSliderAdapter = new CarouselFragmentAdaptor(viewPager, mActivity, carouselResponses);
        viewPager.setAdapter(dynamicSliderAdapter);
        //circleIndicator.setViewPager(viewpager);
        viewPager.setCurrentItem(0);
        // tabLayoutCurren(0);
    }

    private void setHeader() {
        mActivity.seletect_tab(CommonKeys.MY_STATS_TAB);
        mActivity.setToolbarTopVisibility(false);
        mActivity.setToolbarBottomVisibility(true);
        tv_aktivo.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_title.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        mActivity.enableDrawer();
        if (Validation.isRequiredField(common_methods.getTodayHaveData().getStats_background_image())) {
            // mActivity.setBackgroudnImage(common_methods.getTodayHaveData().getStats_background_image());
        }
        // mActivity.setBackgroudnImage("http://aktivolabs.coderspreview.com:1338/flashscreen/g1kfqf32y8.jpg");
    }

    private void intiComponet() {

        Typeface title = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-ExtraLight.ttf");
        // tv_week.setTypeface(title);
        device_width = common_methods.getDeviceWidth(mActivity);
        statusList = new ArrayList<>();
        statusList = StatuName.getListVlaue();
        toolbarAdaptor = new ToolbarAdaptor(statusList);
        mLayoutManager_toolbar = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        recycle_view_toolbar.setLayoutManager(mLayoutManager_toolbar);
        recycle_view_toolbar.setItemAnimator(new DefaultItemAnimator());
        recycle_view_toolbar.setAdapter(toolbarAdaptor);
        //set current week

        toolbarAdaptor.dataNotifydata(statusList);
        recycle_view_toolbar.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("test", "statusList.size()=>>" + statusList.size());
              /*  int firstVisiblePosition = mLayoutManager_toolbar.findFirstVisibleItemPosition();
                Log.e("test", "last postion=>>" + firstVisiblePosition);

                for (int i = 0; i < statusList.size(); i++) {
                    if (i == firstVisiblePosition) {
                        statusList.get(i).setIs_selected(true);
                        Toast.makeText(mActivity, "Click week itme" + i, Toast.LENGTH_LONG).show();
                    } else {
                        statusList.get(i).setIs_selected(false);
                    }
                }
                toolbarAdaptor.dataNotifydata(statusList);
*/
               /* RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(firstVisiblePosition + 4);
                if(viewHolder!=null){

                }*/

            }
        });

        //recyclerView.setOnFlingListener(snapHelper);
        recycle_view_toolbar.addOnItemTouchListener(new RecyclerTouchListener(mActivity, recycle_view_toolbar, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Log.e("test", "scrolll ing postion" + position);

                //  Toast.makeText(mActivity,"Click week here itme"+new_postion,Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // mLayoutManager_toolbar.scrollToPositionWithOffset(position, 0);
                        //  statusList.get(position).setIs_selected(true);
                        //toolbarAdaptor.dataNotifydata(statusList);

                    }
                }, 200);

            }

            @Override
            public void onLongClick(View view, int position) {

            }

            @Override
            public void onDoubleClick(View view, int position) {

            }
        }));


        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  notifyItmeView(position);
                mActivity.tagmanager("Feature Bar Arrow Button", "mystats_feature_arrow_click");
                int position = mLayoutManager_toolbar.findLastVisibleItemPosition();
                int move_postion = position + 1;
                if (move_postion < statusList.size()) {
                    recycle_view_toolbar.smoothScrollToPosition(move_postion);
                }
            }
        });
    }

    @OnClick({R.id.iv_share, R.id.tv_share, R.id.iv_menu})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_share:
                break;
            case R.id.iv_share:
                break;
            case R.id.iv_menu:
                mActivity.openDrawer();
                break;
            default:
                break;
        }

    }

    public class ToolbarAdaptor extends RecyclerView.Adapter<ToolbarAdaptor.MyViewHolder> {

        public List<StatuName> moviesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_name, year, genre;

            public MyViewHolder(View view) {
                super(view);
                tv_name = (TextView) view.findViewById(R.id.tv_name);

            }
        }

        public ToolbarAdaptor(List<StatuName> moviesList) {
            this.moviesList = moviesList;
        }

        public void dataNotifydata(List<StatuName> moviesList) {
            this.moviesList = moviesList;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_status_horizontal, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int po) {

            final int position = po;
            StatuName movie = moviesList.get(position);
            holder.tv_name.setText(movie.getName());
            Typeface date = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-Bold.ttf");
            holder.tv_name.setTypeface(date);
            String valuedata = moviesList.get(position).getName();
            if (valuedata != null && !valuedata.equalsIgnoreCase("")) {
                if (moviesList.get(position).isIs_selected()) {
                    holder.tv_name.setBackground(getResources().getDrawable(R.drawable.shape_black_button));
                    holder.tv_name.setTextColor(getResources().getColor(R.color.white));

                } else {
                    holder.tv_name.setTextColor(getResources().getColor(R.color.black));
                    holder.tv_name.setBackground(null);
                }

            } else {
                holder.tv_name.setText("");
                holder.tv_name.setBackground(null);
            }
            Log.e("test", "is selecetd value postion" + position);


            holder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = false;
                    /*LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
                    int totalVisibleItems = layoutManager.findLastVisibleItemPosition() - layoutManager.findFirstVisibleItemPosition();
                    int centeredItemPosition = totalVisibleItems / 2;
                    recyclerView.smoothScrollToPosition(position);
                    recyclerView.setScrollY(centeredItemPosition );*/
                    notifyItmeView(position);
                    MY_STATS = myStatsApikey_name.get(position);
                    myStatsApikey_name.get(position);
                    callAktiviStatsApi(selected_week, current_year, myStatsApikey_name.get(position));
                    int totla = statusList.size();

                    if (position < totla - 4 && position >= 4) {

                    }

                    if (position == 2) {
                        recycle_view_toolbar.smoothScrollToPosition(3);
                    }

                    if (position == 3) {
                        recycle_view_toolbar.smoothScrollToPosition(4);
                    }

                    if (position == 4) {
                        recycle_view_toolbar.smoothScrollToPosition(5);
                    }

                    if (position == 5) {
                        recycle_view_toolbar.smoothScrollToPosition(6);
                    }

                    if (position == 6) {
                        recycle_view_toolbar.smoothScrollToPosition(7);
                    }
                 /*   int scrollX = (v.getLeft() - (device_width / 2)) + (v.getWidth() / 2);
                    int center = (device_width - v.getWidth())/2;

                    Log.e("test","=>>"+center);
                    recyclerView.smoothScrollBy(center, 0);
                    recyclerView.smoothScrollBy(center, 0);*/
                }
            });



         /*   holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
                    int totalVisibleItems = layoutManager.findLastVisibleItemPosition() - layoutManager.findFirstVisibleItemPosition();
                    int centeredItemPosition = totalVisibleItems / 2;
                    recyclerView.smoothScrollToPosition(position);
                    recyclerView.setScrollY(centeredItemPosition );
                    notifyItmeView(position);
                }
            });*/
        }

        private void notifyItmeView(int pos) {
            for (int i = 0; i < moviesList.size(); i++) {
                if (i == pos) {
                    Log.e("test", "=>>true");
                    moviesList.get(i).setIs_selected(true);
                } else {
                    Log.e("test", "=>>false");
                    moviesList.get(i).setIs_selected(false);
                }

            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setHeader();
    }

    public class CarouselFragmentAdaptor extends PagerAdapter {

        public Context context;
        private final LayoutInflater layoutInflater;
        List<CarouselResponse> carouselResponsesList;
        ViewPager viewPager;

        public CarouselFragmentAdaptor(ViewPager viewPager, Context context, List<CarouselResponse> carouselResponsesList) {
            layoutInflater = LayoutInflater.from(context);
            this.viewPager = viewPager;
            this.carouselResponsesList = carouselResponsesList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return carouselResponsesList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View layout = (View) layoutInflater.inflate(R.layout.row_carousel_mystate, container, false);
            CustomProgressBarAktivo seekbar = (CustomProgressBarAktivo) layout.findViewById(R.id.seekBar);
            CircleIndicator circleIndicator = (CircleIndicator) layout.findViewById(R.id.circleIndicator);
            SimpleDraweeView image = (SimpleDraweeView) layout.findViewById(R.id.sv_spalce);
            TextView txtContent = (TextView) layout.findViewById(R.id.tv_txt);


            // txtTitle.setText(splaceListdata.get(position).getDescription());
            circleIndicator.setViewPager(viewPager);
          /*  txtTitle.setTypeface(title_font);
            txtContent.setTypeface(title_font);*/
            container.addView(layout, 0);
            seekbar.getThumb().mutate().setAlpha(0);
            // graph_activo.initData(carouselResponsesList.get(position).getProgressItems());
            seekbar.invalidate();
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void initDataToSeekbar() {
        ProgressItem mProgressItem;
        progressItemList = new ArrayList<ProgressItem>();
        // red span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = 25;
        Log.i("Mainactivity", mProgressItem.progressItemPercentage + "");
        mProgressItem.color = getResources().getColor(R.color.bar_1);
        mProgressItem.text = "you are";
        mProgressItem.heart_value = 45;
        progressItemList.add(mProgressItem);
        // blue span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = 25;
        mProgressItem.color = getResources().getColor(R.color.bar_2);
        mProgressItem.text = "You";
        mProgressItem.heart_value = 45;

        progressItemList.add(mProgressItem);
        // green span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = 25;
        mProgressItem.color = getResources().getColor(R.color.bar_3);
        mProgressItem.text = "Test";
        mProgressItem.heart_value = 75;

        progressItemList.add(mProgressItem);

        //white span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = 25;
        mProgressItem.color = getResources().getColor(R.color.bar_4);
        mProgressItem.text = "Your Network";
        mProgressItem.heart_value = 95;
        progressItemList.add(mProgressItem);

       /* mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = 20;
        mProgressItem.color = getResources().getColor(R.color.gray4);
        mProgressItem.text ="Your";
        mProgressItem.heart_value =90;
        progressItemList.add(mProgressItem);*/


    }

    public class PickerAdapter extends RecyclerView.Adapter<PickerAdapter.TextVH> {

        private Context context;
        private List<Week> dataList;
        private RecyclerView recyclerView;

        public PickerAdapter(Context context, List<Week> dataList, RecyclerView recyclerView) {
            this.context = context;
            this.dataList = dataList;
            this.recyclerView = recyclerView;
        }

        @Override
        public PickerAdapter.TextVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.picker_item_layout, parent, false);
            view.getLayoutParams().width = (int) (device_width / 5);
            return new PickerAdapter.TextVH(view);
        }

        @Override
        public void onBindViewHolder(PickerAdapter.TextVH holder, final int position) {
            PickerAdapter.TextVH textVH = holder;
            textVH.pickerTxt.setText(dataList.get(position).getWeek());
            //  params.width = device_width/4;
            //  textVH.pickerTxt.setLayoutParams(params);
            Week movie = dataList.get(position);
            holder.pickerTxt.setText(dataList.get(position).getWeek());
            holder.pickerTxt.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
            String valuedata = dataList.get(position).getWeek();
            if (!movie.isDisable()) {

                holder.pickerTxt.setTextColor(getResources().getColor(R.color.black));
                holder.pickerTxt.setBackground(null);

            } else {
                holder.pickerTxt.setBackground(null);
                holder.pickerTxt.setTextColor(getResources().getColor(R.color.gray_light));
            }
            textVH.pickerTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerView != null) {
                        if (MY_STATS.equalsIgnoreCase("AKTIVO")) {
                            mActivity.tagmanager("Aktivo Stats Horizontal Week Calender", "mystats_aktivo_week_click");

                        } else if (MY_STATS.equalsIgnoreCase("POSITIVITY")) {
                            mActivity.tagmanager("Positivity Stats Horizontal Week Calender", "mystats_positivity_week_click");

                        } else if (MY_STATS.equalsIgnoreCase("CALORIES")) {
                            mActivity.tagmanager("Calories Stats Horizontal Week Calender", "mystats_calories_week_click");

                        } else if (MY_STATS.equalsIgnoreCase("STEPS_TAKEN")) {
                            mActivity.tagmanager("Steps Stats Horizontal Week Calender", "mystats_steps_week_click");

                        } else if (MY_STATS.equalsIgnoreCase("BPM")) {
                            mActivity.tagmanager("Resting BPM Stats Horizontal Week Calender", "mystats_restingbpm_week_click");

                        } else if (MY_STATS.equalsIgnoreCase("SLEEP")) {
                            mActivity.tagmanager("Sleep Stats Horizontal Week Calender", "mystats_sleep_week_click");

                        } else if (MY_STATS.equalsIgnoreCase("DISTANCE_COVERED")) {

                        }


                        // tv_selected_date.setText(dataList.get(position).getWeek());
                        Log.e("test", "=>>" + position);
                        //pickerLayoutManager.scrollToPosition(position);
                        int postion = position;
                        if (selected_value != position) {
                            if (selected_value <= position) {
                                //my phone
                                //postion = position + 1;

                                // postion = position + 1;
                                Log.e("test", "pstion bigger");
                                // recyclerView.smoothScrollToPosition(postion);

                            } else {
                                Log.e("test", "pstion lower");
                                //my phone
                                postion = position + 1;
                                // recyclerView.smoothScrollToPosition(postion);


                            }

                        }

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public void swapData(List<Week> newData) {
            dataList = newData;
            notifyDataSetChanged();
        }

        class TextVH extends RecyclerView.ViewHolder {
            TextView pickerTxt;

            public TextVH(View itemView) {
                super(itemView);
                pickerTxt = (TextView) itemView.findViewById(R.id.picker_item);
            }
        }
    }

    //AKTIVO
    Call<MyStatsResponse> myStatsResponseCall;

    public void callAktiviStatsApi(int week, int your, final String mystats) {
        setVisibleGraph(mystats);
        Log.e("test", "mystats==>>" + mystats);
        if (current_week >= week) {
            if (ConnectionUtil.isInternetAvailable(mActivity)) {
                myStatsResponseCall = WebApiClient.getInstance(mActivity).getWebApi().postAktivoMyStats(user_id, week, your, mystats);
                myStatsResponseCall.enqueue(new Callback<MyStatsResponse>() {
                    @Override
                    public void onResponse(Call<MyStatsResponse> call, Response<MyStatsResponse> response) {
                        if (response != null) {
                            if (response.body() != null) {
                                if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                                    if (response.body().getData() != null) {
                                        if (Validation.isRequiredField(response.body().getData().getScore())) {
                                            setProgrebarValue(Integer.parseInt(response.body().getData().getScore()));
                                        }
                                        if (Validation.isRequiredField(response.body().getData().getScore())) {
                                            tv_progressbar.setText(response.body().getData().getScore() + ".");
                                        } else {
                                            tv_progressbar.setText("0.");
                                        }
                                        String week_start_end = response.body().getData().getWeek_start() + "-" + response.body().getData().getWeek_end();
                                        tv_week_value.setText(week_start_end);

                                        if (Validation.isRequiredField(response.body().getData().getCaption())) {
                                            tv_text_progress.setVisibility(View.VISIBLE);
                                            tv_text_progress.setText(Html.fromHtml(response.body().getData().getCaption()));
                                        } else {
                                            tv_text_progress.setVisibility(View.GONE);
                                        }

                                  /* seekBars_list = new ArrayList<SeekBarData>();
                                  // seekBars_list.add(new SeekBarData("Network",25));
                                     seekBars_list.add(new SeekBarData("You",55));
                                    graph_activo.initData(seekBars_list);
                                    graph_activo.initColorData(mColorList);
                                    graph_activo.invalidate();*/

                                        if (Validation.isRequiredField(response.body().getData().getYou()) && Validation.isRequiredField(response.body().getData().getYour_network())) {
                                            //  initAllGraphs(Integer.parseInt(response.body().getData().getYou()),Integer.parseInt(response.body().getData().getYour_network()));
                                               /* Log.e("test", "getYour_network" + Integer.parseInt(response.body().getData().getYour_network()));
                                                Log.e("test", "getYou" + Integer.parseInt(response.body().getData().getYou()));*/
                                            switch (mystats) {
                                                case "AKTIVO":
                                                    mActivity.tagmanager("Feature Bar Aktivo tab", "mystats_feature_aktivo_click");
                                                    graph_activo.setVisibility(View.VISIBLE);
                                                    graph_positity.setVisibility(View.GONE);
                                                    graph_stepTaken.setVisibility(View.GONE);
                                                    graph_calories.setVisibility(View.GONE);
                                                    seekBars_list = new ArrayList<SeekBarData>();
                                                    seekBars_list.add(new SeekBarData("You", Integer.parseInt(response.body().getData().getYou())));
                                                    seekBars_list.add(new SeekBarData(" Your Network", Integer.parseInt(response.body().getData().getYour_network())));
                                                    // seekBars_list.add(new SeekBarData(" You", 55));
                                                    // seekBars_list.add(new SeekBarData(" Your Network", 60));

                                                    // graph_activo.initColorData(mColorList);
                                                    graph_activo.initData(seekBars_list);
                                                    graph_activo.invalidate();
                                                    break;
                                                case "POSITIVITY":
                                                    mActivity.tagmanager("Feature Bar Positivity tab", "mystats_feature_positivity_click");
                                                    graph_positity.setVisibility(View.VISIBLE);
                                                    graph_activo.setVisibility(View.GONE);
                                                    graph_stepTaken.setVisibility(View.GONE);
                                                    graph_calories.setVisibility(View.GONE);
                                                    graph_sleep.setVisibility(View.GONE);
                                                    graph_heart.setVisibility(View.GONE);
                                                    seekBars_list = new ArrayList<SeekBarData>();
                                                    seekBars_list.add(new SeekBarData("You", Integer.parseInt(response.body().getData().getYou())));
                                                    seekBars_list.add(new SeekBarData(" Your Network", Integer.parseInt(response.body().getData().getYour_network())));
                                                    // seekBars_list.add(new SeekBarData("You",Float.parseFloat("66.5")));
                                                    //seekBars_list.add(new SeekBarData(" Your Network", 30));
                                                    // graph_activo.initColorData(mColorList);
                                                    graph_positity.initData(seekBars_list);
                                                    graph_positity.invalidate();


                                                    break;
                                                case "CALORIES":
                                                    mActivity.tagmanager("Feature Bar Calories tab", "mystats_feature_calories_click");
                                                    graph_calories.setVisibility(View.VISIBLE);
                                                    graph_stepTaken.setVisibility(View.GONE);
                                                    graph_activo.setVisibility(View.GONE);
                                                    graph_positity.setVisibility(View.GONE);
                                                    graph_sleep.setVisibility(View.GONE);
                                                    graph_heart.setVisibility(View.GONE);

                                                    seekBars_list = new ArrayList<SeekBarData>();
                                                    seekBars_list.add(new SeekBarData("You", Integer.parseInt(response.body().getData().getYou())));
                                                    seekBars_list.add(new SeekBarData(" Your Network", Integer.parseInt(response.body().getData().getYour_network())));
                                                    //  seekBars_list.add(new SeekBarData(" You",1500));
                                                    // seekBars_list.add(new SeekBarData(" Your Network",2000));
                                                    // graph_activo.initColorData(mColorList);
                                                    graph_calories.initData(seekBars_list);
                                                    graph_calories.invalidate();

                                                    break;
                                                case "STEPS_TAKEN":
                                                    mActivity.tagmanager("Feature Bar Steps tab", "mystats_feature_steps_click");

                                                    graph_stepTaken.setVisibility(View.VISIBLE);
                                                    graph_calories.setVisibility(View.GONE);
                                                    graph_activo.setVisibility(View.GONE);
                                                    graph_positity.setVisibility(View.GONE);
                                                    graph_sleep.setVisibility(View.GONE);
                                                    graph_heart.setVisibility(View.GONE);

                                                    seekBars_list = new ArrayList<SeekBarData>();
                                                    seekBars_list.add(new SeekBarData("You", Integer.parseInt(response.body().getData().getYou())));
                                                    seekBars_list.add(new SeekBarData(" Your Network", Integer.parseInt(response.body().getData().getYour_network())));
                                                    // graph_activo.initColorData(mColorList);
                                                    graph_stepTaken.initData(seekBars_list);
                                                    graph_stepTaken.invalidate();
                                                    break;
                                                case "BPM":
                                                    mActivity.tagmanager("Feature Bar Resting BPM tab", "mystats_feature_restingbpm_click");

                                                    graph_heart.setVisibility(View.VISIBLE);
                                                    graph_stepTaken.setVisibility(View.GONE);
                                                    graph_calories.setVisibility(View.GONE);
                                                    graph_activo.setVisibility(View.GONE);
                                                    graph_positity.setVisibility(View.GONE);
                                                    graph_sleep.setVisibility(View.GONE);
                                                    seekBars_list = new ArrayList<SeekBarData>();
                                                    seekBars_list.add(new SeekBarData("You", Integer.parseInt(response.body().getData().getYou())));
                                                    seekBars_list.add(new SeekBarData(" Your Network", Integer.parseInt(response.body().getData().getYour_network())));
                                                    // seekBars_list.add(new SeekBarData(" You", 55));
                                                    // seekBars_list.add(new SeekBarData(" Your Network", 65));
                                                    // graph_activo.initColorData(mColorList);
                                                    graph_heart.initData(seekBars_list);
                                                    graph_heart.invalidate();

                                                    break;
                                                case "SLEEP":
                                                    mActivity.tagmanager("Feature Bar Sleep tab", "mystats_feature_sleep_click");

                                                    graph_sleep.setVisibility(View.VISIBLE);
                                                    graph_heart.setVisibility(View.GONE);
                                                    graph_stepTaken.setVisibility(View.GONE);
                                                    graph_calories.setVisibility(View.GONE);
                                                    graph_activo.setVisibility(View.GONE);
                                                    graph_positity.setVisibility(View.GONE);
                                                    seekBars_list = new ArrayList<SeekBarData>();
                                                    seekBars_list.add(new SeekBarData("You", Float.parseFloat(response.body().getData().getYou())));
                                                    seekBars_list.add(new SeekBarData(" Your Network", Float.parseFloat(response.body().getData().getYour_network())));

                                                    // graph_activo.initColorData(mColorList);
                                                    graph_sleep.initData(seekBars_list);
                                                    graph_sleep.invalidate();

                                                    break;
                                                case "DISTANCE_COVERED":

                                                    break;
                                                default:
                                                    break;
                                            }


                                        }

                                    }
                                }
                            }
                        }
                        call_first_curren_data_api = true;
                    }

                    @Override
                    public void onFailure(Call<MyStatsResponse> call, Throwable throwable) {
                        common_methods.setExceptionMessage(throwable, mActivity);
                        call_first_curren_data_api = true;

                    }
                });
            }
        } else {
            Log.e("test", "ahead week");
        }

    }


    private void setVisibleGraph(String mystats) {
        switch (mystats) {

            case "AKTIVO":
                graph_activo.setVisibility(View.VISIBLE);
                graph_positity.setVisibility(View.GONE);
                graph_stepTaken.setVisibility(View.GONE);
                graph_calories.setVisibility(View.GONE);
                graph_sleep.setVisibility(View.GONE);
                graph_heart.setVisibility(View.GONE);
                break;
            case "POSITIVITY":
                graph_positity.setVisibility(View.VISIBLE);
                graph_activo.setVisibility(View.GONE);
                graph_stepTaken.setVisibility(View.GONE);
                graph_calories.setVisibility(View.GONE);
                graph_sleep.setVisibility(View.GONE);
                graph_heart.setVisibility(View.GONE);
                break;
            case "CALORIES":
                graph_calories.setVisibility(View.VISIBLE);
                graph_stepTaken.setVisibility(View.GONE);
                graph_activo.setVisibility(View.GONE);
                graph_positity.setVisibility(View.GONE);
                graph_sleep.setVisibility(View.GONE);
                graph_heart.setVisibility(View.GONE);

                break;
            case "STEPS_TAKEN":
                graph_stepTaken.setVisibility(View.VISIBLE);
                graph_calories.setVisibility(View.GONE);
                graph_activo.setVisibility(View.GONE);
                graph_positity.setVisibility(View.GONE);
                graph_sleep.setVisibility(View.GONE);
                graph_heart.setVisibility(View.GONE);

                break;
            case "BPM":
                graph_heart.setVisibility(View.VISIBLE);
                graph_stepTaken.setVisibility(View.GONE);
                graph_calories.setVisibility(View.GONE);
                graph_activo.setVisibility(View.GONE);
                graph_positity.setVisibility(View.GONE);
                graph_sleep.setVisibility(View.GONE);

                break;
            case "SLEEP":
                graph_sleep.setVisibility(View.VISIBLE);
                graph_heart.setVisibility(View.GONE);
                graph_stepTaken.setVisibility(View.GONE);
                graph_calories.setVisibility(View.GONE);
                graph_activo.setVisibility(View.GONE);
                graph_positity.setVisibility(View.GONE);

                break;
            case "DISTANCE_COVERED":

                break;
            default:
                break;
        }

    }

    private void initAllGraphs(int you, int you_network) {
        seekBars_list = new ArrayList<SeekBarData>();
        seekBars_list.add(new SeekBarData("You", you));
        seekBars_list.add(new SeekBarData(" Your Network", you_network));

        //Aktivo
        graph_activo.getThumb().mutate().setAlpha(0);
        graph_activo.initColorData(mColorList);
        graph_activo.initData(seekBars_list);
        graph_activo.invalidate();

        //Positivity
        graph_positity.getThumb().mutate().setAlpha(0);
        Log.e("test", "mColorList" + mColorList.size());
        graph_positity.initColorData(mColorList);
        graph_positity.initData(seekBars_list);
        graph_positity.invalidate();

        //Calories
        graph_calories.getThumb().mutate().setAlpha(0);
        Log.e("test", "mColorList" + mColorList.size());
        graph_calories.initColorData(mColorList);
        graph_calories.initData(seekBars_list);
        graph_calories.invalidate();

        //StepTaken
        graph_stepTaken.getThumb().mutate().setAlpha(0);
        Log.e("test", "mColorList" + mColorList.size());
        graph_stepTaken.initColorData(mColorList);
        graph_stepTaken.initData(seekBars_list);
        graph_stepTaken.invalidate();

        //heart
        graph_heart.getThumb().mutate().setAlpha(0);
        Log.e("test", "mColorList" + mColorList.size());
        graph_heart.initColorData(mColorList);
        graph_heart.initData(seekBars_list);
        graph_heart.invalidate();

        //sleep
        graph_sleep.getThumb().mutate().setAlpha(0);
        Log.e("test", "mColorList" + mColorList.size());
        graph_sleep.initColorData(mColorList);
        graph_sleep.initData(seekBars_list);
        graph_sleep.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("test", "onDestroyView call==>>");
        if (myStatsResponseCall != null) {
            myStatsResponseCall.cancel();
        }
        // unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("test", "onDetach call==>>");
    }


}