package com.aktivo.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aktivo.Model.ExcirciseModel;
import com.aktivo.Model.Week;
import com.aktivo.R;
import com.aktivo.Utils.CenterLayoutManager;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.Methods;
import com.aktivo.Utils.RecyclerTouchListener;
import com.aktivo.response.Activites;
import com.aktivo.response.ExerciseData;
import com.aktivo.response.ExrciseResponse;
import com.aktivo.response.First_slide;
import com.aktivo.response.Second_slide;
import com.aktivo.response.Table_activity_data;
import com.aktivo.response.UserDetailTable;
import com.aktivo.webservices.WebApiClient;
import com.commonmodule.mi.utils.Validation;
import com.facebook.drawee.view.SimpleDraweeView;
import com.raizlabs.android.dbflow.sql.language.Delete;

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
public class ExerciseFragmet extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.circleIndicator)
    CircleIndicator circleIndicator;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.recyclerview_week)
    RecyclerView recyclerview_week;
    ActivityAdaptor activityAdaptor;
    List<Activites> activitesList;
    @BindView(R.id.tv_not_found)
    TextView tv_not_found;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.tv_week)
    TextView tv_week;
    @BindView(R.id.tv_top_week)
    TextView tv_top_week;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_aktivo)
    TextView tv_aktivo;
    @BindView(R.id.tv_selected_date)
    TextView tv_selected_date;
    UserDetailTable userDetailTable;
    private List<Week> movieListData = new ArrayList<>();
    private PickerAdapter pickerAdapter;
    int selected_value = 0;
    Handler scollHander;
    Runnable runnable_scolling;
    int device_width;
    boolean scolling_continue = false;
    LinearLayoutManager mLayoutManager;
    String user_id = "";
    int current_week = 0;
    int current_year = 0;
    CenterLayoutManager layoutManager_week;
    int selected_week = 0;
    int recycleview_postion_current_week = 0;
    String preview_week = "test";
    boolean is_visble_fragment = true;
    Unbinder unbinder;
    boolean call_first_curren_data_api = false;
    boolean flagweekscroll = false;
    boolean flag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercuse_fragmet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        setHeader();

        userDetailTable = UserDetailTable.getUserDetail();
        if (userDetailTable != null) {
            if (Validation.isRequiredField(userDetailTable.get_id())) {
                user_id = userDetailTable.get_id();
            }
        }
        initComponet();
        setFont();
        mActivity.tagmanager("Exercise Screen", "exercise_view");
    }

    private void setFont() {
        tv_week.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_top_week.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_not_found.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_not_found.setText(mActivity.getResources().getString(R.string.not_found_exercise));
    }

    private void initComponet() {

        scollHander = new Handler();
        initWeekRecycleview();
        setViewpageData();
        setBottomRecyclviewData();
        String user_id = "";


    }

    private void initWeekRecycleview() {
        device_width = common_methods.getDeviceWidth(mActivity);

        pickerAdapter = new PickerAdapter(mActivity, movieListData, recyclerview_week);
        mLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
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
        pickerAdapter.swapData(movieListData);
        layoutManager_week = new CenterLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        recyclerview_week.setLayoutManager(layoutManager_week);
        //set current week
        recyclerview_week.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (flagweekscroll == false) {
                    flagweekscroll = true;
                    mActivity.tagmanager("Horizontal Week Bar", "exercise_week_scroll");
                }

                int firstPos = layoutManager_week.findFirstVisibleItemPosition();
                int lastPos = layoutManager_week.findLastVisibleItemPosition();
                int middle = Math.abs(lastPos - firstPos) / 2 + firstPos;

                Log.e("test", "pickerAdapter size=>" + pickerAdapter.getItemCount());
                Log.e("test", "movieListData size=>" + movieListData.size());

                int selectedPos = -1;
                for (int i = 0; i < movieListData.size(); i++) {
                    if (i == middle) {
                        int a = i - 1;
                        if (i >= recycleview_postion_current_week + 1) {
                            //  layoutManager_week.smoothScrollToPosition(recyclerview_week, null,recycleview_postion_current_week);
                            recyclerview_week.scrollToPosition(current_week - 1);

                            if (call_first_curren_data_api) {
                                if (scollHander != null) {
                                    scollHander.removeCallbacks(runnable_scolling);
                                }
                                scollHander.postDelayed(runnable_scolling, 100);
                                selected_week = a;
                                tv_selected_date.setText("" + a);
                            }
                        } else {
                            if (call_first_curren_data_api) {
                                if (scollHander != null) {
                                    scollHander.removeCallbacks(runnable_scolling);
                                }
                                scollHander.postDelayed(runnable_scolling, 100);
                                selected_week = a;
                                tv_selected_date.setText("" + a);
                            }
                        }
                    } else {
                    }
                }

            }
        });

        SnapHelper snapHelper = new LinearSnapHelper();
        //  rv.setLayoutManager(pickerLayoutManager);
        snapHelper.attachToRecyclerView(recyclerview_week);
        recyclerview_week.setAdapter(pickerAdapter);
        Log.e("test", "current_week=>" + current_week);
        runnable_scolling = new Runnable() {
            @Override
            public void run() {
                callExeciseApi(selected_week, current_year);
            }
        };

        recyclerview_week.setOnFlingListener(snapHelper);
        recyclerview_week.addOnItemTouchListener(new RecyclerTouchListener(mActivity, recyclerview_week, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {


                if (position > recycleview_postion_current_week) {
                    layoutManager_week.smoothScrollToPosition(recyclerview_week, null, recycleview_postion_current_week);
                } else {
                    layoutManager_week.smoothScrollToPosition(recyclerview_week, null, position);
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
                layoutManager_week.smoothScrollToPosition(recyclerview_week, null, current_week + 1);

                callExeciseApi(current_week, current_year);
                selected_week = current_week;
                tv_selected_date.setText("" + current_week);
            }
        }, 100);

        Log.e("test", "current_week=>" + current_week);
        //  layoutManager.smoothScrollToPosition(recyclerview_week,null,15);


    }

    private void setBottomRecyclviewData() {
        activitesList = new ArrayList<>();
        activityAdaptor = new ActivityAdaptor(activitesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(activityAdaptor);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (flag = false) {
                    mActivity.tagmanager("Exercise Screen", "exercise_scroll");
                    flag = true;
                }

            }
        });
    }

    private void setViewpageData() {
        List<ExcirciseModel> exerciseList = new ArrayList<>();
        exerciseList = ExcirciseModel.getExcirseList();
        setupViewPager(mViewPager);
        // Set up the ViewPager with the sections adapter.
        mViewPager.setCurrentItem(0);
        circleIndicator.setViewPager(mViewPager);
        mActivity.tagmanager("Carousel 1", "exercise_carousel1_swipe");
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.e("test", "position" + position);
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the Tab.
                if (position == 1) {
                    mActivity.tagmanager("Carousel 2", "exercise_carousel2_swipe");
                } else if (position == 0) {
                    mActivity.tagmanager("Carousel 1", "exercise_carousel1_swipe");
                }

            }
        });
    }


    private void setHeader() {
        mActivity.seletect_tab(CommonKeys.EXERCISE_TAB);
        mActivity.setToolbarTopVisibility(false);
        mActivity.setToolbarBottomVisibility(true);
        tv_aktivo.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_title.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        mActivity.enableDrawer();
        if (Validation.isRequiredField(common_methods.getTodayHaveData().getExercise_background_image())) {
            //  mActivity.setBackgroudnImage(common_methods.getTodayHaveData().getExercise_background_image());
        }

        //  mActivity.setBackgroudnImage("http://aktivolabs.coderspreview.com:1338/flashscreen/g1kfqf32y8.jpg");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setHeader();
        }
    }

    @OnClick(R.id.iv_menu)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu:
                mActivity.openDrawer();
                break;
            default:
                break;
        }
    }

    ExerciseTab1 exerciseTab1 = new ExerciseTab1();
    ExerciseTab2 exerciseTab2 = new ExerciseTab2();
    ExerciseTab3 exerciseTab3 = new ExerciseTab3();
    ExerciseTab4 exerciseTab4 = new ExerciseTab4();
    ExerciseTab5 exerciseTab5 = new ExerciseTab5();

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(exerciseTab1, "");
        adapter.addFragment(exerciseTab2, "");
        // adapter.addFragment(exerciseTab3, "");
        // adapter.addFragment(exerciseTab4, "");
        //adapter.addFragment(exerciseTab5, "");
        viewPager.setAdapter(adapter);
    }

    Call<ExrciseResponse> exrciseResponseCall;

    private void callExeciseApi(int week, int year) {
        if (current_week >= week) {
            exrciseResponseCall = WebApiClient.getInstance(mActivity).getWebApi().callExecirseApi(user_id, year, week);
            exrciseResponseCall.enqueue(new Callback<ExrciseResponse>() {
                @Override
                public void onResponse(Call<ExrciseResponse> call, Response<ExrciseResponse> response) {

                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                                //mActivity.showCroutonsMessage(mActivity,response.body().getMessage());
                                Methods.isProgressHide();
                                if (activitesList != null && !activitesList.isEmpty()) {
                                    activitesList.clear();
                                }
                                final ExerciseData data = response.body().getData();

                                if (data != null) {


                                    String start_end_week = data.getWeek_start() + "-" + data.getWeek_end();
                                    tv_week.setText("" + start_end_week);
                                    //first excerise Slide
                                    if (data.getFirst_slide() != null) {
                                        Delete.table(First_slide.class);
                                        data.getFirst_slide().save();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                exerciseTab1.onActivityTime(data.getFirst_slide());
                                            }
                                        }, 1000);

                                    }
                                    //second excerise Slide

                                    if (exerciseTab2 != null) {
                                        Delete.table(Second_slide.class);
                                        Log.e("test", "size data =>>>>" + data.getSecond_slide().size());
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                exerciseTab2.onUpdateCalenderData(data.getSecond_slide());
                                            }
                                        }, 1000);

                                        for (int i = 0; i < data.getSecond_slide().size(); i++) {
                                            data.getSecond_slide().get(i).save();
                                        }
                                    }

                                    List<Table_activity_data> table_activity_data = data.getTable_activity_data();
                                    int size = table_activity_data.size();
                                    if (table_activity_data != null && size > 0) {
                                        for (int i = 0; i < size; i++) {
                                            List<Activites> activites = table_activity_data.get(i).getActivites();
                                            if (activites != null && activites.size() > 0) {
                                                for (int j = 0; j < activites.size(); j++) {
                                                    Activites activites1 = activites.get(j);
                                                    activites1.setDate(table_activity_data.get(i).getDate());
                                                    activitesList.add(activites1);
                                                }
                                            }
                                        }
                                    }

                                    setBoottomData(activitesList);
                                }

                                Log.e("test", "" + response.body().getData().getFirst_slide());

                            } else {

                            }
                        }
                    }
                    call_first_curren_data_api = true;
                }

                @Override
                public void onFailure(Call<ExrciseResponse> call, Throwable t) {
                    common_methods.setExceptionMessage(t, mActivity);
                    if (is_visble_fragment) {
                        setBoottomData(activitesList);
                    }
                    call_first_curren_data_api = true;

                    Log.e("test", "=>>" + t.getLocalizedMessage());
                }
            });

        } else {
            Log.e("test", "curremt week not " + week);
        }


    }

    private void setBoottomData(List<Activites> activitesList) {
        if (activityAdaptor == null) {
            activityAdaptor = new ActivityAdaptor(activitesList);
        } else {
            preview_week = "test";
            activityAdaptor.setDataUpdate(activitesList);
        }
        if (activitesList != null && !activitesList.isEmpty()) {
            //gone not datafound
            tv_not_found.setVisibility(View.GONE);
        } else {
            tv_not_found.setVisibility(View.VISIBLE);

            //visble no datafound
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

      /*===============================WEEKLY RECODE WALK SPORT Adaptor=======================================================*/

    public class ActivityAdaptor extends RecyclerView.Adapter<ActivityAdaptor.MyViewHolder> {

        private List<Activites> activityList;

        public void setDataUpdate(List<Activites> activityLists) {
            this.activityList = activityLists;
            notifyDataSetChanged();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_activity_name)
            TextView tv_activity_name;
            @BindView(R.id.tv_time)
            TextView tv_time;
            @BindView(R.id.img_view)
            SimpleDraweeView img_view;
            @BindView(R.id.tv_detail)
            TextView tv_detail;
            @BindView(R.id.tv_week_btn)
            TextView tv_week_btn;


            public MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);

            }
        }

        public ActivityAdaptor(List<Activites> moviesList) {
            this.activityList = moviesList;
        }

        @Override
        public ActivityAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_excercise_bottom_recycleview, parent, false);
            return new ActivityAdaptor.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ActivityAdaptor.MyViewHolder holder, int position) {
            final Activites activityExerciseModel = activityList.get(position);
            if (activityExerciseModel != null) {

                if (activityExerciseModel != null) {

                    holder.tv_activity_name.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                    holder.tv_week_btn.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                    holder.tv_time.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
                    holder.tv_detail.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
                    String detail = "" + activityExerciseModel.getDuration() + "," + activityExerciseModel.getAverage_heart_rate() + "," + activityExerciseModel.getCalories();

                    if (Validation.isRequiredField(activityExerciseModel.getType())) {
                        holder.tv_activity_name.setText(activityExerciseModel.getType() + ",");
                    }
                    if (Validation.isRequiredField(activityExerciseModel.getTime())) {
                        holder.tv_time.setText(activityExerciseModel.getTime());
                    }
                    if (Validation.isRequiredField(detail)) {
                        holder.tv_detail.setText(detail);
                    }
                    if (Validation.isRequiredField(activityExerciseModel.getTypeURL())) {
                        holder.img_view.setImageURI(activityExerciseModel.getTypeURL());
                    }

                    if (!preview_week.equalsIgnoreCase(activityList.get(position).getDate())) {
                        holder.tv_week_btn.setText(activityList.get(position).getDate());
                        holder.tv_week_btn.setVisibility(View.VISIBLE);
                        preview_week = activityList.get(position).getDate();
                    } else {
                        preview_week = activityList.get(position).getDate();
                        holder.tv_week_btn.setVisibility(View.GONE);

                    }
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mActivity.tagmanager("Activity Details", "exercise_detail_click");
                            mActivity.pushFragmentDontIgnoreCurrent(ExerciseSubDetailFragment.getInstance(activityExerciseModel.getType(), activityExerciseModel.getDate(), activityExerciseModel.getCaloriesBurned(), activityExerciseModel.getHeartRateZones(), activityExerciseModel.getImpactOnMyDay()), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                        }
                    });


                }
            }
        }

        @Override
        public int getItemCount() {
            return activityList.size();
        }
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
                        mActivity.tagmanager("Horizontal Week Bar", "exercise_week_click");
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


    @Override
    public void onDestroyView() {
        is_visble_fragment = false;
        super.onDestroyView();
        if (exrciseResponseCall != null) {
            exrciseResponseCall.cancel();
        }

        //unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unbinder.unbind();
        Log.e("test", "on Destory call==>>");
    }
}
