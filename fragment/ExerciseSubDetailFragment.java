package com.aktivo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.response.CaloriesBurned;
import com.aktivo.response.HeartRateZones;
import com.aktivo.response.ImpactOnMyDay;
import com.commonmodule.mi.utils.Validation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseSubDetailFragment extends BaseFragment implements View.OnClickListener {


    public static final String KEY_CALORIES="key_calories";
    public static final String KEY_HEARTRATEZONES="key_heartRateZones";
    public static final String KEY_IMPACTONMYDAY="key_impactonmyday";
    public static final String KEY_TYPE="key_type";
    public static final String KEY_WEEK="key_week";

    @BindView(R.id.tv_minPeak)
    TextView tv_minPeak;
    @BindView(R.id.tv_minPeak_static)
    TextView tv_minPeak_static;
    @BindView(R.id.tv_min_cardio)
    TextView tv_min_cardio;
    @BindView(R.id.tv_min_cardio_static)
    TextView tv_min_cardio_static;
    @BindView(R.id.tv_min_fat_burn)
    TextView tv_min_fat_burn;
    @BindView(R.id.tv_min_fat_burn_static)
    TextView tv_min_fat_burn_static;
    @BindView(R.id.tv_cals)
    TextView tv_cals;
    @BindView(R.id.tv_cals_static)
    TextView tv_cals_static;
    @BindView(R.id.tv_cals_min)
    TextView tv_cals_min;
    @BindView(R.id.tv_cals_min_static)
    TextView tv_cals_min_static;
    @BindView(R.id.tv_steps_of_taken)
    TextView tv_steps_of_taken;
    @BindView(R.id.tv_calories_burned)
    TextView tv_calories_burned;
    @BindView(R.id.tv_active_minutes)
    TextView tv_active_minutes;
    @BindView(R.id.tv_execise_type)
    TextView tv_execise_type;
    @BindView(R.id.tv_week)
    TextView tv_week;
    @BindView(R.id.tv_title_heart_rate_zone)
    TextView tv_title_heart_rate_zone;
    @BindView(R.id.tv_title_calories_borned)
    TextView tv_title_calories_borned;
    @BindView(R.id.tv_title_impact_on_day)
    TextView tv_title_impact_on_day;
 @BindView(R.id.tv_steps_of_taken_from)
    TextView tv_steps_of_taken_from;
 @BindView(R.id.tv_tv_calories_burned_from)
    TextView tv_tv_calories_burned_from;
 @BindView(R.id.tv_tv_active_minutes_from)
    TextView tv_tv_active_minutes_from;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_aktivo)
    TextView tv_aktivo;

    public static ExerciseSubDetailFragment getInstance(String type,String week,CaloriesBurned caloriesBurned, HeartRateZones heartRateZones, ImpactOnMyDay impactOnMyDay){
        ExerciseSubDetailFragment exerciseSubDetailFragment=new ExerciseSubDetailFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(KEY_CALORIES,caloriesBurned);
        bundle.putParcelable(KEY_HEARTRATEZONES,heartRateZones);
        bundle.putParcelable(KEY_IMPACTONMYDAY,impactOnMyDay);
        bundle.putString(KEY_TYPE,type);
        bundle.putString(KEY_WEEK,week);
        exerciseSubDetailFragment.setArguments(bundle);
       // bundle.put
        return exerciseSubDetailFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.exercise_sub_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        iniComponet();
        setHeader();
        setFont();
        getBundleDataValue();
        mActivity.tagmanager("Detailed Exercise Screen","exercise_detail_view");
    }
    private void setHeader() {
        mActivity.setToolbarTopVisibility(false);
        mActivity.setToolbarBottomVisibility(true);
        tv_aktivo.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Bold));
        tv_title.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_ExtraLight));
        mActivity.enableDrawer();
        if(Validation.isRequiredField(common_methods.getTodayHaveData().getExercise_background_image())){
            mActivity.setBackgroudnImage(common_methods.getTodayHaveData().getExercise_background_image());
        }
    }

    private void setFont() {
        tv_cals_min_static.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_minPeak_static.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_min_cardio_static.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_min_fat_burn_static.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_cals_static.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));

        tv_week.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_execise_type.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_title_impact_on_day.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_title_calories_borned.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_title_heart_rate_zone.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));

        tv_minPeak.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Bold));
        tv_min_cardio.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Bold));
        tv_min_fat_burn.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Bold));
        tv_cals.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Bold));
        tv_cals_min.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Bold));
        tv_steps_of_taken.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_ExtraLight));
        tv_calories_burned.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_ExtraLight));
        tv_active_minutes.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_ExtraLight));
        tv_steps_of_taken_from.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Bold));
        tv_tv_calories_burned_from.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Bold));
        tv_tv_active_minutes_from.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Bold));

    }

    private void getBundleDataValue() {
        Bundle arguments = getArguments();
        if(arguments!=null){
            if(arguments.containsKey(KEY_TYPE)){
              String type=arguments.getString(KEY_TYPE);
              tv_execise_type.setText(""+type);

            }
            if(arguments.containsKey(KEY_WEEK)){
                String week=arguments.getString(KEY_WEEK);
                tv_week.setText(""+week);

            }
            if(arguments.containsKey(KEY_CALORIES)){
                CaloriesBurned caloriesBurned=arguments.getParcelable(KEY_CALORIES);
                if(caloriesBurned!=null){
                    if(Validation.isRequiredField(caloriesBurned.getCals())){
                        tv_cals.setText(Html.fromHtml(caloriesBurned.getCals()));
                    }
                    if(Validation.isRequiredField(caloriesBurned.getCals_min())){
                        tv_cals_min.setText(Html.fromHtml(caloriesBurned.getCals_min()));
                    }
                }
            }
            if(arguments.containsKey(KEY_HEARTRATEZONES)){
                HeartRateZones heartRateZones=arguments.getParcelable(KEY_HEARTRATEZONES);
                if(heartRateZones!=null){
                    if(Validation.isRequiredField(heartRateZones.getCardio())){
                        tv_min_cardio.setText(Html.fromHtml(heartRateZones.getCardio()));
                    }
                    if(Validation.isRequiredField(heartRateZones.getFat_burn())){
                        tv_min_fat_burn.setText(Html.fromHtml(heartRateZones.getFat_burn()));
                    }
                    if(Validation.isRequiredField(heartRateZones.getPeak())){
                        tv_minPeak.setText(Html.fromHtml(heartRateZones.getPeak()));
                    }
                }


            }
            if(arguments.containsKey(KEY_IMPACTONMYDAY)){
                ImpactOnMyDay impactOnMyDay=arguments.getParcelable(KEY_IMPACTONMYDAY);
                 if(impactOnMyDay!=null){
                     if(Validation.isRequiredField(impactOnMyDay.getSteps().getFrom())){
                         tv_steps_of_taken_from.setText(impactOnMyDay.getSteps().getFrom());
                         tv_steps_of_taken.setText("step of "+impactOnMyDay.getSteps().getTo());
                     }
                     if(Validation.isRequiredField(impactOnMyDay.getCalories().getFrom())){
                         tv_tv_calories_burned_from.setText(impactOnMyDay.getCalories().getFrom());
                         tv_calories_burned.setText("calories of "+impactOnMyDay.getCalories().getTo()+" burned");
                     }
                     if(Validation.isRequiredField(impactOnMyDay.getActive_minutes().getFrom())){
                         tv_tv_active_minutes_from.setText(impactOnMyDay.getActive_minutes().getFrom());
                         tv_active_minutes.setText("of "+impactOnMyDay.getActive_minutes().getTo()+" active minutes");
                        // tv_active_minutes.setText(Html.fromHtml(impactOnMyDay.getActive_minutes()));
                     }
                 }

            }
        }
    }

    private void iniComponet() {

    }

    @OnClick({R.id.iv_menu,R.id.iv_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_menu:
                mActivity.openDrawer();
                break;
            case R.id.iv_back:
                mActivity.onBackPressed();
                break;
            default:
                break;
        }
    }
}
