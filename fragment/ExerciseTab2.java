package com.aktivo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.MyItemDecoration;
import com.aktivo.interfaceclass.OnUpdateCalenderExrcise;
import com.aktivo.response.Second_slide;
import com.aktivo.response.TempPojo;
import com.aktivo.response.WeeklyModel;
import com.commonmodule.mi.utils.Validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseTab2 extends BaseFragment implements OnUpdateCalenderExrcise {


    @BindView(R.id.recyclview_calender)
    RecyclerView recyclview_calender;
    @BindView(R.id.tv_not_found)
    TextView tv_not_found;
    @BindView(R.id.tv_overview)
    TextView tv_overview;
    String YES = "Yes";

    private List<Second_slide> calanderData = new ArrayList<>();
    ArrayList<WeeklyModel> arrayList_by_week;

    ArrayList<TempPojo> arrayList_temp;
    private CalendarViewAdaptor calendarViewAdapter;
    int device_width;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise_tab2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        intiComponet();
        setRecycleViewData();
        List<Second_slide> calenderListData = Second_slide.getCalenderListData();
        if(calenderListData!=null){
            onUpdateCalenderData(calenderListData);
           // calendarViewAdapter.dataUpdate(calenderListData);
        }
        Log.e("test"," ExerciseTab2 call onViewCreated");

    }
    private void intiComponet() {
        tv_overview.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
    }

    private void setRecycleViewData() {
        LinearLayoutManager linearLayoutManager;
        device_width = common_methods.getDeviceWidth(mActivity);
        calanderData = new ArrayList<>();
        arrayList_by_week = new ArrayList<>();

     /* int total_day=  getMonthTotalDay(2,2017);
        for (int i = 1; i <=total_day ; i++) {
            calanderData.add(new CalanderData(""+i,false,false)) ;
        }
        calanderData.get(20).setIs_seleceted(true);
        calanderData.get(25).setIs_seleceted(true);
        calanderData.get(4).setIs_seleceted(true);
        calanderData.get(10).setIs_current_date(true);*/
        calendarViewAdapter = new CalendarViewAdaptor(arrayList_by_week);
        linearLayoutManager = new LinearLayoutManager(mActivity);
        recyclview_calender.setLayoutManager(linearLayoutManager);
        recyclview_calender.addItemDecoration(new MyItemDecoration());
        recyclview_calender.setAdapter(calendarViewAdapter);
    }

    private int getMonthTotalDay(int month, int year) {
        String MonthOfName = "";
        int number_Of_DaysInMonth = 30;
        switch (month) {
            case 1:
                MonthOfName = "January";
                number_Of_DaysInMonth = 31;
                break;
            case 2:
                MonthOfName = "February";
                if ((year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))) {
                    number_Of_DaysInMonth = 29;
                } else {
                    number_Of_DaysInMonth = 28;
                }
                break;
            case 3:
                MonthOfName = "March";
                number_Of_DaysInMonth = 31;
                break;
            case 4:
                MonthOfName = "April";
                number_Of_DaysInMonth = 30;
                break;
            case 5:
                MonthOfName = "May";
                number_Of_DaysInMonth = 31;
                break;
            case 6:
                MonthOfName = "June";
                number_Of_DaysInMonth = 30;
                break;
            case 7:
                MonthOfName = "July";
                number_Of_DaysInMonth = 31;
                break;
            case 8:
                MonthOfName = "August";
                number_Of_DaysInMonth = 31;
                break;
            case 9:
                MonthOfName = "September";
                number_Of_DaysInMonth = 30;
                break;
            case 10:
                MonthOfName = "October";
                number_Of_DaysInMonth = 31;
                break;
            case 11:
                MonthOfName = "November";
                number_Of_DaysInMonth = 30;
                break;
            case 12:
                MonthOfName = "December";
                number_Of_DaysInMonth = 31;
        }
        return number_Of_DaysInMonth;
    }


    @Override
    public void onUpdateCalenderData(List<Second_slide> calendar_data) {
        Log.e("test","total date=>"+calendar_data.size());
        if (calendar_data != null && calendar_data.size() > 0) {


            calanderData = calendar_data;

            arrayList_by_week = new ArrayList<>();
            arrayList_temp = new ArrayList<>();

            int week_array_size = Integer.valueOf(calanderData.size()/7);

            for(int j = 0 ; j < 35 ; j++)
            {
                TempPojo pojo;

                if(j < calanderData.size())
                {
                    pojo = new TempPojo(calanderData.get(j));
                }
                else
                {
                    pojo = new TempPojo(null);
                }

                arrayList_temp.add(pojo);
            }

            int x = 0;

            if(calanderData.size() > 28) {

                for (int w = 0; w <= week_array_size; w++) {
                    WeeklyModel weeklyModel = new WeeklyModel(arrayList_temp.get(x).getSecond_slide()
                            , arrayList_temp.get(x + 1).getSecond_slide()
                            , arrayList_temp.get(x + 2).getSecond_slide()
                            , arrayList_temp.get(x + 3).getSecond_slide()
                            , arrayList_temp.get(x + 4).getSecond_slide()
                            , arrayList_temp.get(x + 5).getSecond_slide()
                            , arrayList_temp.get(x + 6).getSecond_slide());

                    arrayList_by_week.add(weeklyModel);

                    x = x + 7;
                }
            }
            else
            {
                for (int w = 0; w < week_array_size; w++) {

                    WeeklyModel weeklyModel = new WeeklyModel(arrayList_temp.get(x).getSecond_slide()
                            , arrayList_temp.get(x + 1).getSecond_slide()
                            , arrayList_temp.get(x + 2).getSecond_slide()
                            , arrayList_temp.get(x + 3).getSecond_slide()
                            , arrayList_temp.get(x + 4).getSecond_slide()
                            , arrayList_temp.get(x + 5).getSecond_slide()
                            , arrayList_temp.get(x + 6).getSecond_slide());

                    arrayList_by_week.add(weeklyModel);

                    x = x + 7;
                }
            }
            if(calendarViewAdapter!=null){
                calendarViewAdapter.dataUpdate(arrayList_by_week);
            }
        } else {
            arrayList_by_week = new ArrayList<>();
            calendarViewAdapter.dataUpdate(arrayList_by_week);
        }
    }

    public class CalendarViewAdaptor extends RecyclerView.Adapter<CalendarViewAdaptor.MyViewHolder> {

        public List<WeeklyModel> arrayList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_date1,tv_date2,tv_date3,tv_date4,tv_date5,tv_date6,tv_date7;
            public LinearLayout ll_main_item;
            public ImageView img_arrow;

            public MyViewHolder(View view) {
                super(view);
                tv_date1 = (TextView) view.findViewById(R.id.tv_date1);
                tv_date2 = (TextView) view.findViewById(R.id.tv_date2);
                tv_date3 = (TextView) view.findViewById(R.id.tv_date3);
                tv_date4 = (TextView) view.findViewById(R.id.tv_date4);
                tv_date5 = (TextView) view.findViewById(R.id.tv_date5);
                tv_date6 = (TextView) view.findViewById(R.id.tv_date6);
                tv_date7 = (TextView) view.findViewById(R.id.tv_date7);
                ll_main_item = (LinearLayout) view.findViewById(R.id.ll_main_item);
                img_arrow = (ImageView) view.findViewById(R.id.img_arrow);

            }
        }

        public CalendarViewAdaptor(List<WeeklyModel> calanderDataList1) {
            this.arrayList = calanderDataList1;
        }

        public void dataUpdate(List<WeeklyModel> calanderDataList1) {
            this.arrayList = calanderDataList1;
            notifyDataSetChanged();
        }

        @Override
        public CalendarViewAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_calander_itme, parent, false);

            return new CalendarViewAdaptor.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CalendarViewAdaptor.MyViewHolder holder, int position) {


            WeeklyModel model = arrayList.get(position);

            int postion_Date = position + 1;

            holder.ll_main_item.setBackground(null);
             holder.img_arrow.setVisibility(View.INVISIBLE);

            if(model.getSecond_slide1() != null) {
                holder.tv_date1.setText(changeDateFormat(model.getSecond_slide1().getDate()));
                if (Validation.isRequiredField(model.getSecond_slide1().getToday_status())
                        && model.getSecond_slide1().getToday_status().equalsIgnoreCase(YES)) {
                    if (Validation.isRequiredField(model.getSecond_slide1().getAvailability_status())
                            && model.getSecond_slide1().getAvailability_status().equalsIgnoreCase(YES)) {
                        holder.tv_date1.setBackground(getResources().getDrawable(R.drawable.current_data_avalible_activty_both));
                        holder.tv_date1.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        holder.tv_date1.setBackground(getResources().getDrawable(R.drawable.current_date_drawable));
                        holder.tv_date1.setTextColor(getResources().getColor(R.color.black));
                    }

                    holder.img_arrow.setVisibility(View.VISIBLE);
                    holder.ll_main_item.setBackgroundResource(R.drawable.today_date_background);

                } else if (Validation.isRequiredField(model.getSecond_slide1().getAvailability_status())
                        && model.getSecond_slide1().getAvailability_status().equalsIgnoreCase(YES)) {
                    holder.tv_date1.setBackground(getResources().getDrawable(R.drawable.shape_black_button));
                    holder.tv_date1.setTextColor(getResources().getColor(R.color.white));
                } else {
                    holder.tv_date1.setTextColor(getResources().getColor(R.color.black));
                    holder.tv_date1.setBackground(null);
                }
            }
            else
            {
                holder.tv_date1.setVisibility(View.GONE);
            }
            if(model.getSecond_slide2() != null) {
                holder.tv_date2.setText(changeDateFormat(model.getSecond_slide2().getDate()));
                if (Validation.isRequiredField(model.getSecond_slide2().getToday_status())
                        && model.getSecond_slide2().getToday_status().equalsIgnoreCase(YES)) {
                    if (Validation.isRequiredField(model.getSecond_slide2().getAvailability_status())
                            && model.getSecond_slide2().getAvailability_status().equalsIgnoreCase(YES)) {
                        holder.tv_date2.setBackground(getResources().getDrawable(R.drawable.current_data_avalible_activty_both));
                        holder.tv_date2.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        holder.tv_date2.setBackground(getResources().getDrawable(R.drawable.current_date_drawable));
                        holder.tv_date2.setTextColor(getResources().getColor(R.color.black));
                    }
                    holder.img_arrow.setVisibility(View.VISIBLE);
                    holder.ll_main_item.setBackgroundResource(R.drawable.today_date_background);

                } else if (Validation.isRequiredField(model.getSecond_slide2().getAvailability_status())
                        && model.getSecond_slide2().getAvailability_status().equalsIgnoreCase(YES)) {
                    holder.tv_date2.setBackground(getResources().getDrawable(R.drawable.shape_black_button));
                    holder.tv_date2.setTextColor(getResources().getColor(R.color.white));
                } else {
                    holder.tv_date2.setTextColor(getResources().getColor(R.color.black));
                    holder.tv_date2.setBackground(null);
                }
            }
            else
            {
                holder.tv_date2.setVisibility(View.GONE);
            }

            if(model.getSecond_slide3() != null) {
                holder.tv_date3.setText(changeDateFormat(model.getSecond_slide3().getDate()));
                if (Validation.isRequiredField(model.getSecond_slide3().getToday_status())
                        && model.getSecond_slide3().getToday_status().equalsIgnoreCase(YES)) {
                    if (Validation.isRequiredField(model.getSecond_slide3().getAvailability_status())
                            && model.getSecond_slide3().getAvailability_status().equalsIgnoreCase(YES)) {
                        holder.tv_date3.setBackground(getResources().getDrawable(R.drawable.current_data_avalible_activty_both));
                        holder.tv_date3.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        holder.tv_date3.setBackground(getResources().getDrawable(R.drawable.current_date_drawable));
                        holder.tv_date3.setTextColor(getResources().getColor(R.color.black));
                    }

                    holder.img_arrow.setVisibility(View.VISIBLE);
                    holder.ll_main_item.setBackgroundResource(R.drawable.today_date_background);

                } else if (Validation.isRequiredField(model.getSecond_slide3().getAvailability_status())
                        && model.getSecond_slide3().getAvailability_status().equalsIgnoreCase(YES)) {
                    holder.tv_date3.setBackground(getResources().getDrawable(R.drawable.shape_black_button));
                    holder.tv_date3.setTextColor(getResources().getColor(R.color.white));
                } else {
                    holder.tv_date3.setTextColor(getResources().getColor(R.color.black));
                    holder.tv_date3.setBackground(null);
                }
            }
            else
            {
                holder.tv_date3.setVisibility(View.GONE);
            }

            if(model.getSecond_slide4() != null) {
                holder.tv_date4.setText(changeDateFormat(model.getSecond_slide4().getDate()));
                if (Validation.isRequiredField(model.getSecond_slide4().getToday_status())
                        && model.getSecond_slide4().getToday_status().equalsIgnoreCase(YES)) {
                    if (Validation.isRequiredField(model.getSecond_slide4().getAvailability_status())
                            && model.getSecond_slide4().getAvailability_status().equalsIgnoreCase(YES)) {
                        holder.tv_date4.setBackground(getResources().getDrawable(R.drawable.current_data_avalible_activty_both));
                        holder.tv_date4.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        holder.tv_date4.setBackground(getResources().getDrawable(R.drawable.current_date_drawable));
                        holder.tv_date4.setTextColor(getResources().getColor(R.color.black));
                    }

                    holder.img_arrow.setVisibility(View.VISIBLE);
                    holder.ll_main_item.setBackgroundResource(R.drawable.today_date_background);

                } else if (Validation.isRequiredField(model.getSecond_slide4().getAvailability_status())
                        && model.getSecond_slide4().getAvailability_status().equalsIgnoreCase(YES)) {
                    holder.tv_date4.setBackground(getResources().getDrawable(R.drawable.shape_black_button));
                    holder.tv_date4.setTextColor(getResources().getColor(R.color.white));
                } else {
                    holder.tv_date4.setTextColor(getResources().getColor(R.color.black));
                    holder.tv_date4.setBackground(null);
                }
            }
            else
            {
                holder.tv_date4.setVisibility(View.GONE);
            }

            if(model.getSecond_slide5() != null) {

                holder.tv_date5.setText(changeDateFormat(model.getSecond_slide5().getDate()));
                if (Validation.isRequiredField(model.getSecond_slide5().getToday_status())
                        && model.getSecond_slide5().getToday_status().equalsIgnoreCase(YES)) {
                    if (Validation.isRequiredField(model.getSecond_slide5().getAvailability_status())
                            && model.getSecond_slide5().getAvailability_status().equalsIgnoreCase(YES)) {
                        holder.tv_date5.setBackground(getResources().getDrawable(R.drawable.current_data_avalible_activty_both));
                        holder.tv_date5.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        holder.tv_date5.setBackground(getResources().getDrawable(R.drawable.current_date_drawable));
                        holder.tv_date5.setTextColor(getResources().getColor(R.color.black));
                    }

                    holder.img_arrow.setVisibility(View.VISIBLE);
                    holder.ll_main_item.setBackgroundResource(R.drawable.today_date_background);

                } else if (Validation.isRequiredField(model.getSecond_slide5().getAvailability_status())
                        && model.getSecond_slide5().getAvailability_status().equalsIgnoreCase(YES)) {
                    holder.tv_date5.setBackground(getResources().getDrawable(R.drawable.shape_black_button));
                    holder.tv_date5.setTextColor(getResources().getColor(R.color.white));
                } else {
                    holder.tv_date5.setTextColor(getResources().getColor(R.color.black));
                    holder.tv_date5.setBackground(null);
                }
            }
            else
            {
                holder.tv_date5.setVisibility(View.GONE);
            }


            if(model.getSecond_slide6() != null) {

                holder.tv_date6.setText(changeDateFormat(model.getSecond_slide6().getDate()));
                if (Validation.isRequiredField(model.getSecond_slide6().getToday_status())
                        && model.getSecond_slide6().getToday_status().equalsIgnoreCase(YES)) {
                    if (Validation.isRequiredField(model.getSecond_slide6().getAvailability_status())
                            && model.getSecond_slide6().getAvailability_status().equalsIgnoreCase(YES)) {
                        holder.tv_date6.setBackground(getResources().getDrawable(R.drawable.current_data_avalible_activty_both));
                        holder.tv_date6.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        holder.tv_date6.setBackground(getResources().getDrawable(R.drawable.current_date_drawable));
                        holder.tv_date6.setTextColor(getResources().getColor(R.color.black));
                    }

                    holder.img_arrow.setVisibility(View.VISIBLE);
                    holder.ll_main_item.setBackgroundResource(R.drawable.today_date_background);

                } else if (Validation.isRequiredField(model.getSecond_slide6().getAvailability_status())
                        && model.getSecond_slide6().getAvailability_status().equalsIgnoreCase(YES)) {
                    holder.tv_date6.setBackground(getResources().getDrawable(R.drawable.shape_black_button));
                    holder.tv_date6.setTextColor(getResources().getColor(R.color.white));
                } else {
                    holder.tv_date6.setTextColor(getResources().getColor(R.color.black));
                    holder.tv_date6.setBackground(null);
                }
            }
            else
            {
                holder.tv_date6.setVisibility(View.GONE);
            }

            if(model.getSecond_slide7() != null) {

                holder.tv_date7.setText(changeDateFormat(model.getSecond_slide7().getDate()));
                if (Validation.isRequiredField(model.getSecond_slide7().getToday_status())
                        && model.getSecond_slide7().getToday_status().equalsIgnoreCase(YES)) {
                    if (Validation.isRequiredField(model.getSecond_slide7().getAvailability_status())
                            && model.getSecond_slide7().getAvailability_status().equalsIgnoreCase(YES)) {
                        holder.tv_date7.setBackground(getResources().getDrawable(R.drawable.current_data_avalible_activty_both));
                        holder.tv_date7.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        holder.tv_date7.setBackground(getResources().getDrawable(R.drawable.current_date_drawable));
                        holder.tv_date7.setTextColor(getResources().getColor(R.color.black));
                    }

                    holder.img_arrow.setVisibility(View.VISIBLE);
                    holder.ll_main_item.setBackgroundResource(R.drawable.today_date_background);

                } else if (Validation.isRequiredField(model.getSecond_slide7().getAvailability_status())
                        && model.getSecond_slide7().getAvailability_status().equalsIgnoreCase(YES)) {
                    holder.tv_date7.setBackground(getResources().getDrawable(R.drawable.shape_black_button));
                    holder.tv_date7.setTextColor(getResources().getColor(R.color.white));
                } else {
                    holder.tv_date7.setTextColor(getResources().getColor(R.color.black));
                    holder.tv_date7.setBackground(null);
                }
            }
            else {
                holder.tv_date7.setVisibility(View.GONE);
            }
            /*if (Validation.isRequiredField(calanderDataList.get(position).getToday_status()) && calanderDataList.get(position).getToday_status().equalsIgnoreCase(YES)) {
                if (Validation.isRequiredField(calanderDataList.get(position).getAvailability_status()) && calanderDataList.get(position).getAvailability_status().equalsIgnoreCase(YES)){
                    holder.tv_date.setBackground(getResources().getDrawable(R.drawable.current_data_avalible_activty_both));
                    holder.tv_date.setTextColor(getResources().getColor(R.color.white));
                    holder.tv_date.setPadding(8, 8, 8, 8);
                    Log.e("test","current data and both=>>"+postion_Date);
                }else {
                    Log.e("test","current=>>"+postion_Date);
                    holder.tv_date.setBackground(getResources().getDrawable(R.drawable.current_date_drawable));
                    holder.tv_date.setTextColor(getResources().getColor(R.color.black));
                    holder.tv_date.setPadding(8, 8, 8, 8);
                }


            }
            else if (Validation.isRequiredField(calanderDataList.get(position).getAvailability_status()) && calanderDataList.get(position).getAvailability_status().equalsIgnoreCase(YES)) {
                holder.tv_date.setBackground(getResources().getDrawable(R.drawable.shape_black_button));
                holder.tv_date.setTextColor(getResources().getColor(R.color.white));
            }
            else {
                holder.tv_date.setTextColor(getResources().getColor(R.color.black));
                holder.tv_date.setBackground(null);
            }
            Log.e("test", "is selecetd value postion" + position);*/


        }

        @Override
        public int getItemCount() {
            if (arrayList != null && arrayList.size() == 0) {
                tv_not_found.setVisibility(View.VISIBLE);
            } else {
                tv_not_found.setVisibility(View.GONE);
            }
            return arrayList.size();
        }
    }

    private String changeDateFormat(String date) {
        String outputDate = null;
        SimpleDateFormat output = new SimpleDateFormat("dd", Locale.ENGLISH);
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {


            Date oneWayTripDate = input.parse(date);// parse input

            //Crashlytics.logException(new Throwable("this is issue:"+oneWayTripDate.toString()));
            Log.e("", "datenewinfunction : " + date.toString());// format output
            outputDate = output.format(oneWayTripDate);

        } catch (ParseException e) {


            outputDate = date;

            e.printStackTrace();
        }
        return outputDate;
    }
}
