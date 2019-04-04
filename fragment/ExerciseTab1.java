package com.aktivo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.interfaceclass.OnUpdateExceriseActivityTime;
import com.aktivo.response.First_slide;
import com.commonmodule.mi.utils.Validation;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseTab1 extends BaseFragment implements OnUpdateExceriseActivityTime {

    @BindView(R.id.activity)
    TextView activity;
    @BindView(R.id.tv_hours_count)
    TextView tv_hours_count;
    @BindView(R.id.tv_hours)
    TextView tv_hours;
    @BindView(R.id.tv_min_count)
    TextView tv_min_count;
    @BindView(R.id.tv_min)
    TextView tv_min;
    String time_date_activity="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise_tab1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        setFont();
        Log.e("test"," ExerciseTab1 call onViewCreated");
        final First_slide firstSlideData = First_slide.getFirstSlideData();
        if(firstSlideData!=null){
            onActivityTime(firstSlideData);
        }

        activity.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
     /* String min_second="6 hours 20 min";
        if(Validation.isRequiredField(min_second)){

            SpannableString styledString
                    = new SpannableString(min_second);
            // change text
            styledString.setSpan(new RelativeSizeSpan(2f), 0, min_second.indexOf("hours")-1, 0);
            styledString.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.black)), 0, min_second.indexOf("hours")-1, 0);
            styledString.setSpan(new RelativeSizeSpan(2f), min_second.indexOf("hours")+5, min_second.indexOf("min")-1, 0);
            styledString.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.black)), min_second.indexOf("hours")+5, min_second.indexOf("min")-1, 0);

            //tv_min_second.setText(styledString);
        }*/
    }

    private void setFont() {
        tv_min.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Light));
        tv_hours.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Light));
        tv_hours_count.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Bold));
        tv_min_count.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Bold));
    }

    @Override
    public void onActivityTime(First_slide time) {
        Log.e("test","=>>"+time);
        String min_hours="";
        if(tv_hours!=null){

            tv_hours.setVisibility(View.GONE);
            tv_hours_count.setVisibility(View.GONE);
            tv_min.setVisibility(View.GONE);
            tv_min_count.setVisibility(View.GONE);

        if(time!=null) {
            if (Validation.isRequiredField(time.getHour())) {
                tv_hours.setVisibility(View.VISIBLE);
                tv_hours_count.setVisibility(View.VISIBLE);
                tv_hours_count.setText(time.getHour());
                int lenge_hour = time.getHour().length();
                Log.e("test", "lenge=>>" + lenge_hour);

               /* if(lenge_hour>=1){
                    tv_hours.setText("hours");
                }else {
                    tv_hours.setText("hour");
                }*/

                tv_hours.setText("hours");
            } else {
                tv_hours.setVisibility(View.GONE);
                tv_hours_count.setVisibility(View.GONE);
            }
            if (Validation.isRequiredField(time.getMin())) {
                tv_min.setVisibility(View.VISIBLE);
                tv_min_count.setVisibility(View.VISIBLE);
                tv_min_count.setText(time.getMin());

                int lenge_min = time.getMin().length();
                Log.e("test", "lenge=>>" + lenge_min);
                tv_min.setText("min");

            } else {
                tv_min.setVisibility(View.GONE);
                tv_min_count.setVisibility(View.GONE);
            }
        }
        }

            //min_hours=CommonKeys.Activity_hours = time.getHour()+ " "+time.getMin();

            //tv_min_second.setText(Html.fromHtml(""+min_hours));

    }
}
