package com.aktivo.fragment;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aktivo.Model.Hobby;
import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.Common_Methods;
import com.aktivo.Utils.Methods;
import com.aktivo.Utils.MyPreferences;
import com.aktivo.response.Area_of_interest;
import com.aktivo.response.BaseCommanRespons;
import com.aktivo.response.MarketUrlResponse;
import com.aktivo.response.ProfileResponse;
import com.aktivo.response.UserDetailTable;
import com.aktivo.webservices.WebApi;
import com.aktivo.webservices.WebApiClient;
import com.commonmodule.mi.Activity.MIActivity;
import com.commonmodule.mi.utils.SpinnerCustomized;
import com.commonmodule.mi.utils.Validation;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.janmuller.android.simplecropimage.CropImage;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.spiner_mostly)
    SpinnerCustomized spiner_mostly;
    @BindView(R.id.user_pic)
    SimpleDraweeView user_pic;
    @BindView(R.id.tv_cm)
    TextView tv_cm;
    @BindView(R.id.tv_kg)
    TextView tv_kg;
    @BindView(R.id.tv_round_cm)
    TextView tv_round_cm;
    @BindView(R.id.tv_round_kg)
    TextView tv_round_kg;
    @BindView(R.id.tv_seekbar)
    TextView tv_seekbar;
    @BindView(R.id.tv_birth)
    TextView tv_birth;
    @BindView(R.id.tv_cm_weight)
    TextView tv_cm_weight;
    @BindView(R.id.tv_kg_weight)
    TextView tv_kg_weight;
    @BindView(R.id.tv_chagne)
    TextView tv_chagne;
    @BindView(R.id.tv_round_cm_weight)
    TextView tv_round_cm_weight;
    @BindView(R.id.tv_round_kg_weight)
    TextView tv_round_kg_weight;
    @BindView(R.id.recyclview_hobby)
    RecyclerView recyclview_hobby;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.drinkseekBar)
    SeekBar drinkseekBar;
    @BindView(R.id.edt_first)
    EditText edt_first;
    @BindView(R.id.edt_last_name)
    EditText edt_last_name;
    @BindView(R.id.edt_phone)
    EditText edt_phone;
    @BindView(R.id.edt_height)
    EditText edt_height;
    @BindView(R.id.edt_weight)
    EditText edt_weight;
    @BindView(R.id.tv_first)
    TextView tv_first;
    @BindView(R.id.tv_last_name)
    TextView tv_last_name;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_birth_date_black)
    TextView tv_birth_date_black;
    @BindView(R.id.tv_height)
    TextView tv_height;
    @BindView(R.id.tv_weight)
    TextView tv_weight;
    @BindView(R.id.tv_how_many_cigrate)
    TextView tv_how_many_cigrate;
    @BindView(R.id.tv_mostday_office)
    TextView tv_mostday_office;
    @BindView(R.id.tv_how_many_drink)
    TextView tv_how_many_drink;
    @BindView(R.id.tv_drinkseekbar)
    TextView tv_drinkseekbar;
    @BindView(R.id.ll_gender)
    RelativeLayout ll_gender;
    @BindView(R.id.tv_female)
    TextView tv_female;
    @BindView(R.id.tv_round_female)
    TextView tv_round_female;
    @BindView(R.id.tv_round_male)
    TextView tv_round_male;
    @BindView(R.id.tv_male)
    TextView tv_male;
    @BindView(R.id.tv_gender)
    TextView tv_gender;
    @BindView(R.id.edt_gender)
    EditText edt_gender;
    @BindView(R.id.tv_bedtime_black)
    TextView tv_bedtime_black;
    @BindView(R.id.tv_bedtime)
    TextView tv_bedtime;
    @BindView(R.id.tv_wakeup_black)
    TextView tv_wakeup_black;
    @BindView(R.id.tv_wakeup)
    TextView tv_wakeup;
    @BindView(R.id.tv_lorem)
    TextView tv_lorem;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_aktivo)
    TextView tv_aktivo;
    @BindView(R.id.llMainScroll)
    NestedScrollView llMainScroll;

    String mCurrentPhotoPath = "";
    ArrayAdapter<String> spinnerPhoneCodeAdaptor;
    private List<Area_of_interest> hobbyList;
    private HobbyAdaptor hobbyAdaptor;
    Calendar myCalendar;
    Calendar calendarMinData;
    DatePickerDialog.OnDateSetListener datePickerListner;
    //  boolean is_selected_birth_date=false;
    Uri user_profice_url;
    String user_selecete_image_str_path = "";
    public static final int PHOTO_TAKE_CAMERA = 5;
    public static final int PHOTO_GALLARY_OPEN = 6;
    ArrayList<String> areaofintrest;
    UserDetailTable userDetailTable;
    ArrayList<String> selectedintrest;
    boolean flag = false;
    public static String strweightunit, strheightunit, checkedintrest = "", Strsleepformat = "", Strwakeupformate = "",
            strSex = "", strsmokeseekbar = "", strdrinkseekbar = "";
    private String blockCharacterSet = "~#^|$%&*!";
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.edit_profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userDetailTable = UserDetailTable.getUserDetail();
        ButterKnife.bind(this, view);
        setHeader();
        intiComponet();
        mActivity.tagmanager("Settings Edit Profile Screen","settings_edit_profile_view");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            llMainScroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (flag = false){
                        mActivity.tagmanager("Settings Edit Profile Screen","settings_edit_profile_scroll");
                        flag = true;
                    }

                }
            });
        }
    }

    private void setHeader() {
        mActivity.seletect_tab(CommonKeys.CLEAR_TAB);
        mActivity.setToolbarTopVisibility(false);
        mActivity.setToolbarBottomVisibility(true);
        tv_aktivo.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_title.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        mActivity.enableDrawer();
        if (Validation.isRequiredField(common_methods.getTodayHaveData().getEdit_profile_background_image())) {
            mActivity.setBackgroudnImage(common_methods.getTodayHaveData().getEdit_profile_background_image());
        }
    }

    private void intiComponet() {

        edt_first.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        edt_last_name.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        edt_phone.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        edt_height.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        edt_weight.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_birth.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        Typeface tv_text_font = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-Medium.ttf");
        tv_height.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Medium));
        tv_weight.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Medium));
        tv_bedtime.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_wakeup.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        edt_gender.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_how_many_drink.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Medium));
        tv_gender.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Medium));
        tv_bedtime_black.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Medium));
        tv_wakeup_black.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Medium));
        tv_seekbar.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_drinkseekbar.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));

        tv_lorem.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Medium));

        tv_cm.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_kg.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_cm_weight.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_kg_weight.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));


        tv_birth_date_black.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Medium));
        tv_first.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Medium));
        tv_last_name.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Medium));
        tv_phone.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Medium));
        tv_how_many_cigrate.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Medium));

//        Typeface tv_mostday_office_font = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-Bold.ttf");
//        tv_mostday_office.setTypeface(tv_mostday_office_font);

        tv_chagne.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        myCalendar = Calendar.getInstance();
        calendarMinData = Calendar.getInstance();
        calendarMinData.setTime(new Date());
        calendarMinData.add(Calendar.YEAR, -18);

        edt_first.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        edt_last_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        edt_height.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        edt_phone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        edt_weight.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tv_birth.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        edt_gender.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tv_bedtime.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tv_wakeup.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        //edt_first.setFilters(new InputFilter[] { filter });


        edt_first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mActivity.tagmanager("First Name Edit","settings_edit_profile_fname_input");
                try {
                    if (edt_first.getText().toString().isEmpty() || edt_first.getText().toString().equalsIgnoreCase("")) {
                        edt_first.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.invalid, 0);
                    } else {
                        edt_first.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.valid, 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        edt_last_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mActivity.tagmanager("Last Name Edit","settings_edit_profile_lname_input");
                try {
                    if (edt_last_name.getText().toString().isEmpty() || edt_last_name.getText().toString().equalsIgnoreCase("")) {
                        edt_last_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.invalid, 0);
                    } else {
                        edt_last_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.valid, 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mActivity.tagmanager("Phone Number Edit","settings_edit_profile_number_input");

                try {
                    if (edt_phone.getText().toString().isEmpty() || edt_phone.getText().toString().equalsIgnoreCase("")) {
                        edt_phone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.invalid, 0);
                    } else {
                        edt_phone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.valid, 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mActivity.tagmanager("Height Edit","settings_edit_profile_height_input");
                try {
                    if (edt_height.getText().toString().isEmpty() || edt_height.getText().toString().equalsIgnoreCase("")) {
                        edt_height.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.invalid, 0);
                    } else {
                        edt_height.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.valid, 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mActivity.tagmanager("Weight Edit","settings_edit_profile_weight_input");
                try {
                    if (edt_weight.getText().toString().isEmpty() || edt_weight.getText().toString().equalsIgnoreCase("")) {
                        edt_weight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.invalid, 0);
                    } else {
                        edt_weight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.valid, 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_bedtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.tagmanager("Bedtime Edit","settings_edit_profile_sleep_bedtime_input");
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(mActivity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour == 0) {
                            selectedHour += 12;
                            Strsleepformat = "AM";
                        } else if (selectedHour == 12) {
                            Strsleepformat = "PM";
                        } else if (selectedHour > 12) {
                            selectedHour -= 12;
                            Strsleepformat = "PM";
                        } else {
                            Strsleepformat = "AM";
                        }

                        try {
                            String now = selectedHour + ":" + selectedMinute + " " + Strsleepformat;
                            Log.e("now time: ", now);
                            SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm aa");
                            SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm");
                            String time24 = outFormat.format(inFormat.parse(now));
                            Log.e("24 hour format : ", time24);
                            tv_bedtime.setText(time24 + ":" + "00");
                            if (userDetailTable != null) {
                                if (Validation.isRequiredField(userDetailTable.get_id())) {
                                    callUploadImageApi(userDetailTable.get_id(), edt_first.getText().toString(), edt_last_name.getText().toString(),
                                            edt_gender.getText().toString(), edt_phone.getText().toString(), tv_birth.getText().toString(), edt_height.getText().toString(),
                                            edt_weight.getText().toString(), strweightunit, strheightunit, strsmokeseekbar,
                                            strdrinkseekbar, tv_bedtime.getText().toString(), tv_wakeup.getText().toString(), checkedintrest);
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Exception : " + e.getMessage());
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        tv_wakeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.tagmanager("Wake-up Edit","settings_edit_profile_sleep_waketime_input");

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(mActivity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour == 0) {
                            selectedHour += 12;
                            Strwakeupformate = "AM";
                        } else if (selectedHour == 12) {
                            Strwakeupformate = "PM";
                        } else if (selectedHour > 12) {
                            selectedHour -= 12;
                            Strwakeupformate = "PM";
                        } else {
                            Strwakeupformate = "AM";
                        }

                        try {
                            String now = selectedHour + ":" + selectedMinute + " " + Strwakeupformate;
                            Log.e("now time: ", now);
                            SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm aa");
                            SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm");
                            String time24 = outFormat.format(inFormat.parse(now));
                            Log.e("24 hour format : ", time24);
                            tv_wakeup.setText(time24 + ":" + "00");
                            if (userDetailTable != null) {
                                if (Validation.isRequiredField(userDetailTable.get_id())) {
                                    callUploadImageApi(userDetailTable.get_id(), edt_first.getText().toString(), edt_last_name.getText().toString(),
                                            edt_gender.getText().toString(), edt_phone.getText().toString(), tv_birth.getText().toString(), edt_height.getText().toString(),
                                            edt_weight.getText().toString(), strweightunit, strheightunit, strsmokeseekbar,
                                            strdrinkseekbar, tv_bedtime.getText().toString(), tv_wakeup.getText().toString(), checkedintrest);
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Exception : " + e.getMessage());
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        datePickerListner = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                //is_selected_birth_date=true;
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "YYY/M/D"; //In which you need put here
                //String date_myFormat = "dd MMM YYYY"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(CommonKeys.DATE_FORMATE, Locale.US);

                String birth_date = sdf.format(myCalendar.getTime().getTime());
                if (Validation.isRequiredField(birth_date)) {
                    tv_birth.setText(birth_date);
                    if (userDetailTable != null) {
                        if (Validation.isRequiredField(userDetailTable.get_id())) {
                            callUploadImageApi(userDetailTable.get_id(), edt_first.getText().toString(), edt_last_name.getText().toString(),
                                    edt_gender.getText().toString(), edt_phone.getText().toString(), tv_birth.getText().toString(), edt_height.getText().toString(),
                                    edt_weight.getText().toString(), strweightunit, strheightunit, strsmokeseekbar,
                                    strdrinkseekbar, tv_bedtime.getText().toString(), tv_wakeup.getText().toString(), checkedintrest);
                        }
                    }
                }
                Log.e("test", "birhdate==>>" + sdf.format(myCalendar.getTime()));

            }

        };
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar bar, int i, boolean b) {
                mActivity.tagmanager("Cigarettes Edit","settings_edit_profile_cigarette_slide");
                if (i == 20) {
                    tv_seekbar.setText(" " + i + "" + "+");
                } else {
                    tv_seekbar.setText(" " + i);

                }

                strsmokeseekbar = i + "";
            }

            @Override
            public void onStartTrackingTouch(SeekBar bar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar bar) {

                if (userDetailTable != null) {
                    if (Validation.isRequiredField(userDetailTable.get_id())) {
                        callUploadImageApi(userDetailTable.get_id(), edt_first.getText().toString(), edt_last_name.getText().toString(),
                                edt_gender.getText().toString(), edt_phone.getText().toString(), tv_birth.getText().toString(), edt_height.getText().toString(),
                                edt_weight.getText().toString(), strweightunit, strheightunit, strsmokeseekbar,
                                strdrinkseekbar, tv_bedtime.getText().toString(), tv_wakeup.getText().toString(), checkedintrest);
                    }
                }

            }
        });

        drinkseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mActivity.tagmanager("Drinks Edit","settings_edit_profile_drink_slide");

                if (progress == 20) {
                    tv_drinkseekbar.setText("" + progress + "" + "+");
                } else {
                    tv_drinkseekbar.setText("" + progress);
                }

                strdrinkseekbar = progress + "";
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (userDetailTable != null) {
                    if (Validation.isRequiredField(userDetailTable.get_id())) {
                        callUploadImageApi(userDetailTable.get_id(), edt_first.getText().toString(), edt_last_name.getText().toString(),
                                edt_gender.getText().toString(), edt_phone.getText().toString(), tv_birth.getText().toString(), edt_height.getText().toString(),
                                edt_weight.getText().toString(), strweightunit, strheightunit, strsmokeseekbar,
                                strdrinkseekbar, tv_bedtime.getText().toString(), tv_wakeup.getText().toString(), checkedintrest);
                    }
                }
            }
        });

        //hobby recyclview
//        hobbyList = Hobby.getListBobby();
//        hobbyAdaptor = new HobbyAdaptor(hobbyList);
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 2, GridLayoutManager.VERTICAL, false);
//        recyclview_hobby.setLayoutManager(layoutManager);
//        recyclview_hobby.setItemAnimator(new DefaultItemAnimator());
//        // recyclview_hobby.addItemDecoration(new DividerItemDecoration(mActivity.getResources().getDrawable(R.drawable.row_divider)));
//        recyclview_hobby.setAdapter(hobbyAdaptor);
//        recyclview_hobby.setNestedScrollingEnabled(false);

        user_pic.setImageURI(CommonKeys.Dummy_PROFILE_PIC);
        final List<String> spinner_code = new ArrayList<String>();
        /// spinner_list_all.add("All");
        spinner_code.add("abc");
        spinner_code.add("xyz");
        spinner_code.add("test");
        spinnerPhoneCodeAdaptor = new ArrayAdapter<String>(mActivity, R.layout.custom_spinner, spinner_code);
        spiner_mostly.setAdapter(spinnerPhoneCodeAdaptor);
        spiner_mostly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_mostday_office.setText(spinner_code.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (userDetailTable != null) {
            if (Validation.isRequiredField(userDetailTable.get_id())) {
                DisplayProfileData(userDetailTable.get_id());
            }
        }

    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(.\\d+)?");
    }

    @OnClick({R.id.iv_info_dedtime,R.id.iv_back,R.id.iv_menu, R.id.tv_mostday_office, R.id.rl_im, R.id.ll_height, R.id.ll_weight, R.id.tv_birth, R.id.tv_save, R.id.tv_cancel, R.id.ll_gender})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                mActivity.onBackPressed();
                break;
                case R.id.iv_info_dedtime:
                    common_methods.setCutemDialogMessage(mActivity,"If you are not connected to a wearable, we recommend you enter when you typically go to bed and wake up. \n" +
                            "This helps us capture your sleep data more precisely, and provides you, with a more accurate Aktivo Score!");
               //openBedWakeInfoTimeDialog("Bed Time infomation Dialog");
                    mActivity.tagmanager("Sleep Zone info button","settings_edit_profile_sleepinfo_click");
                break;
             case R.id.iv_menu:
                mActivity.openDrawer();
                if (userDetailTable != null) {
                    if (Validation.isRequiredField(userDetailTable.get_id())) {
                        callUploadImageApi(userDetailTable.get_id(), edt_first.getText().toString(), edt_last_name.getText().toString(),
                                edt_gender.getText().toString(), edt_phone.getText().toString(), tv_birth.getText().toString(), edt_height.getText().toString(),
                                edt_weight.getText().toString(), strweightunit, strheightunit, strsmokeseekbar,
                                strdrinkseekbar, tv_bedtime.getText().toString(), tv_wakeup.getText().toString(), checkedintrest);
                    }
                }
                break;
            case R.id.tv_mostday_office:
                spiner_mostly.performClick();
                break;
            case R.id.rl_im:
                mActivity.tagmanager("Change Profile Photo button","settings_edit_profile_photo_click");
                checkStoragePermission();
                break;
            case R.id.ll_height:
                changHeigthLayout();
                mActivity.tagmanager("Height switch units","settings_edit_profile_height_toggle");
                break;
            case R.id.ll_weight:
                changeWeightlayout();
                mActivity.tagmanager("Weight switch units","settings_edit_profile_weight_input");

                break;
            case R.id.tv_birth:
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        mActivity, datePickerListner, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(calendarMinData.getTime().getTime());
                datePickerDialog.show();
                break;
            case R.id.tv_save:
//                if (userDetailTable != null) {
//                    if (Validation.isRequiredField(userDetailTable.get_id())) {
//                        UpdateProfile(userDetailTable.get_id(), edt_first.getText().toString(), edt_last_name.getText().toString(),
//                                edt_phone.getText().toString(), tv_birth.getText().toString(), edt_height.getText().toString(),
//                                edt_weight.getText().toString(), strweightunit, strheightunit, String.valueOf(seekBar.getProgress()),
//                                String.valueOf(drinkseekBar.getProgress()), "", "", checkedintrest);
//                    }
//                }

                if (userDetailTable != null) {
                    if (Validation.isRequiredField(userDetailTable.get_id())) {
                        callUploadImageApi(userDetailTable.get_id(), edt_first.getText().toString(), edt_last_name.getText().toString(),
                                edt_gender.getText().toString(), edt_phone.getText().toString(), tv_birth.getText().toString(), edt_height.getText().toString(),
                                edt_weight.getText().toString(), strweightunit, strheightunit, String.valueOf(seekBar.getProgress()),
                                String.valueOf(drinkseekBar.getProgress()), tv_bedtime.getText().toString(), tv_wakeup.getText().toString(), checkedintrest);
                    }
                }

                break;
            case R.id.ll_gender:
                changeGender();
                mActivity.tagmanager("Gender Edit","settings_edit_profile_gender_toggle");
                break;
            default:
                break;
        }
    }

    private void openBedWakeInfoTimeDialog(String text) {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comman_confirm);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_yes = (TextView) dialog.findViewById(R.id.yes);
        TextView tv_cancle = (TextView) dialog.findViewById(R.id.cancle);
        TextView tv_header = (TextView) dialog.findViewById(R.id.tv_header);
        tv_header.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_yes.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_header.setText(text);
      /*  camera_btn.setText(""+languageTable.getFROM_CAMERA());
        galary.setText(""+languageTable.getFROM_GALLERY());
        tv_header.setText(""+languageTable.getSELECT_OPTION());*/
//        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);

        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });


    }

    private void changeGender() {
        if (tv_round_female.getVisibility() == View.VISIBLE) {
            tv_round_female.setVisibility(View.GONE);
            tv_female.setVisibility(View.VISIBLE);
            tv_round_male.setVisibility(View.VISIBLE);
            tv_male.setVisibility(View.GONE);
            strSex = "Female";
            edt_gender.setText("Female");

        } else {
            tv_round_female.setVisibility(View.VISIBLE);
            tv_female.setVisibility(View.GONE);
            tv_round_male.setVisibility(View.GONE);
            tv_male.setVisibility(View.VISIBLE);
            strSex = "Male";
            edt_gender.setText("Male");

        }
        if (userDetailTable != null) {
            if (Validation.isRequiredField(userDetailTable.get_id())) {
                callUploadImageApi(userDetailTable.get_id(), edt_first.getText().toString(), edt_last_name.getText().toString(),
                        edt_gender.getText().toString(), edt_phone.getText().toString(), tv_birth.getText().toString(), edt_height.getText().toString(),
                        edt_weight.getText().toString(), strweightunit, strheightunit, String.valueOf(seekBar.getProgress()),
                        String.valueOf(drinkseekBar.getProgress()), tv_bedtime.getText().toString(), tv_wakeup.getText().toString(), checkedintrest);
            }
        }

    }

    private void changeWeightlayout() {
        if (tv_round_cm_weight.getVisibility() == View.VISIBLE) {
            tv_round_cm_weight.setVisibility(View.GONE);
            tv_cm_weight.setVisibility(View.VISIBLE);
            tv_round_kg_weight.setVisibility(View.VISIBLE);
            tv_kg_weight.setVisibility(View.GONE);
            strweightunit = "POUND";

            double finalValue = Math.round((Double.parseDouble(edt_weight.getText().toString()) * 2.2046) * 100.0) / 100.0;
            edt_weight.setText(String.valueOf(finalValue));
            if (Validation.isRequiredField(userDetailTable.get_id())) {
                callUploadImageApi(userDetailTable.get_id(), edt_first.getText().toString(), edt_last_name.getText().toString(),
                        edt_gender.getText().toString(), edt_phone.getText().toString(), tv_birth.getText().toString(), edt_height.getText().toString(),
                        edt_weight.getText().toString(), strweightunit, strheightunit, String.valueOf(seekBar.getProgress()),
                        String.valueOf(drinkseekBar.getProgress()), tv_bedtime.getText().toString(), tv_wakeup.getText().toString(), checkedintrest);
            }

        } else {
            tv_round_cm_weight.setVisibility(View.VISIBLE);
            tv_cm_weight.setVisibility(View.GONE);
            tv_round_kg_weight.setVisibility(View.GONE);
            tv_kg_weight.setVisibility(View.VISIBLE);
            strweightunit = "KG";
            double value = Math.round((Double.parseDouble(edt_weight.getText().toString()) * 0.4535) * 100.0) / 100.0;
            edt_weight.setText(String.valueOf(value));
            if (Validation.isRequiredField(userDetailTable.get_id())) {
                callUploadImageApi(userDetailTable.get_id(), edt_first.getText().toString(), edt_last_name.getText().toString(),
                        edt_gender.getText().toString(), edt_phone.getText().toString(), tv_birth.getText().toString(), edt_height.getText().toString(),
                        edt_weight.getText().toString(), strweightunit, strheightunit, String.valueOf(seekBar.getProgress()),
                        String.valueOf(drinkseekBar.getProgress()), tv_bedtime.getText().toString(), tv_wakeup.getText().toString(), checkedintrest);
            }
        }
    }

    private void changHeigthLayout() {
        if (tv_round_cm.getVisibility() == View.VISIBLE) {
            tv_round_cm.setVisibility(View.GONE);
            tv_cm.setVisibility(View.VISIBLE);
            tv_round_kg.setVisibility(View.VISIBLE);
            tv_kg.setVisibility(View.GONE);
            strheightunit = "FEET";
            Log.e("get feet inch", String.valueOf(Double.parseDouble(edt_height.getText().toString()) * 0.033));
            double finalValue = Math.round((Double.parseDouble(edt_height.getText().toString()) * 0.033) * 100.0) / 100.0;
            edt_height.setText(String.valueOf(finalValue));
            if (Validation.isRequiredField(userDetailTable.get_id())) {
                callUploadImageApi(userDetailTable.get_id(), edt_first.getText().toString(), edt_last_name.getText().toString(),
                        edt_gender.getText().toString(), edt_phone.getText().toString(), tv_birth.getText().toString(), edt_height.getText().toString(),
                        edt_weight.getText().toString(), strweightunit, strheightunit, String.valueOf(seekBar.getProgress()),
                        String.valueOf(drinkseekBar.getProgress()), tv_bedtime.getText().toString(), tv_wakeup.getText().toString(), checkedintrest);
            }


        } else {
            tv_round_cm.setVisibility(View.VISIBLE);
            tv_cm.setVisibility(View.GONE);
            tv_round_kg.setVisibility(View.GONE);
            tv_kg.setVisibility(View.VISIBLE);
            strheightunit = "CM";

            Log.e("get cm", String.valueOf(Double.parseDouble(edt_height.getText().toString()) * 30.48));
            double value = Math.round((Double.parseDouble(edt_height.getText().toString()) * 30.48) * 100.0) / 100.0;
            edt_height.setText(String.valueOf(value));
            if (Validation.isRequiredField(userDetailTable.get_id())) {
                callUploadImageApi(userDetailTable.get_id(), edt_first.getText().toString(), edt_last_name.getText().toString(),
                        edt_gender.getText().toString(), edt_phone.getText().toString(), tv_birth.getText().toString(), edt_height.getText().toString(),
                        edt_weight.getText().toString(), strweightunit, strheightunit, String.valueOf(seekBar.getProgress()),
                        String.valueOf(drinkseekBar.getProgress()), tv_bedtime.getText().toString(), tv_wakeup.getText().toString(), checkedintrest);
            }

        }

    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (Common_Methods.isPermissionNotGranted(mActivity, permissions)) {
                requestPermissions(permissions, CommonKeys.PERMISSION_CODE);
                return;
            } else {
                openPhotoDilogGallery();
            }
        } else {

            openPhotoDilogGallery();
        }
    }

    private void openPhotoDilogGallery() {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_choose_image);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setTitle(R.string.choose_option);
        dialog.show();

        TextView camera_btn = (TextView) dialog.findViewById(R.id.camera);
        RelativeLayout rl_close = (RelativeLayout) dialog.findViewById(R.id.rl_close);
        TextView galary = (TextView) dialog.findViewById(R.id.galery);
        TextView tv_header = (TextView) dialog.findViewById(R.id.tv_header);
        tv_header.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        galary.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        camera_btn.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
      /*  camera_btn.setText(""+languageTable.getFROM_CAMERA());
        galary.setText(""+languageTable.getFROM_GALLERY());
        tv_header.setText(""+languageTable.getSELECT_OPTION());*/
//        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        rl_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        camera_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

/*
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            *//* create instance of File with name img.jpg *//*
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                            *//* put uri as extra in intent object *//*
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                startActivityForResult(intent,PHOTO_TAKE_CAMERA);*/
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;

                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("test", "==>" + e.getLocalizedMessage());
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = null;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            // only for gingerbread and newer versions

                            try {
                                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                Uri contentUri = FileProvider.getUriForFile(mActivity, "com.aktivo.fileprovider", createImageFile());

                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

                                startActivityForResult(takePictureIntent, PHOTO_TAKE_CAMERA);
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("test", "eerroor=>>" + e.getLocalizedMessage());
                            }
                        } else {
                               /* try {
                                    photoURI = Uri.fromFile(createImageFile());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }*/


                            try {
                                dispatchTakePictureIntent();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                              /*  Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            //create instance of File with name img.jpg *//**//*
                                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                            *//* put uri as extra in intent object *//*
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                                startActivityForResult(intent,PHOTO_TAKE_CAMERA);*/
                        }


                    }
                }


                dialog.dismiss();
            }

        });

        galary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, PHOTO_GALLARY_OPEN);
                   /* Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , PHOTO_GALLARY_OPEN);*/

                    //startActivityForResult(new avtImageChooser().create(), 500);
                } catch (Exception e) {
                }
                dialog.dismiss();
            }

        });
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = Uri.fromFile(createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PHOTO_TAKE_CAMERA);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CommonKeys.PERMISSION_CODE:
                if (Common_Methods.isPermissionNotGranted(mActivity, permissions)) {
                    Common_Methods.whichPermisionNotGranted(mActivity, permissions, grantResults);
                } else {
                    //                    mapFragment.getMapAsync(ContactUsFragment.this);
                    openPhotoDilogGallery();

                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intetnData) {
        super.onActivityResult(requestCode, resultCode, intetnData);
        // retrive selected photo
        switch (requestCode) {

            case PHOTO_TAKE_CAMERA:
                if (resultCode == RESULT_OK) {

                   /* String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "_";
                    File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM), "Camera");
                    File image = null;
                    try {
                        image = File.createTempFile(
                                imageFileName,  *//* prefix *//*
                                ".jpg",         *//* suffix *//*
                                storageDir      *//* directory *//*
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Save a file: path for use with ACTION_VIEW intents
                    mCurrentPhotoPath = "file:" + image.getAbsolutePath();*/
                 /*   Uri imageUri = Uri.parse(mCurrentPhotoPath);
                    File file = new File(imageUri.getPath());
                    try {
                        InputStream ims = new FileInputStream(file);
                        ivPreview.setImageBitmap(BitmapFactory.decodeStream(ims));
                    } catch (FileNotFoundException e) {
                        return;
                    }*/
                    cropCapturedImage();
                    /*if(intetnData!=null){
                        Uri selectedImage = intetnData.getData();
                        if(selectedImage!=null){
                           // iv_user_pic.setImageURI(selectedImage);
                          String  mPath = getPath(selectedImage);
                            cropCapturedImageGalley(mPath);
                            user_profice_url =selectedImage;
                          //  path = intetnData.getStringArrayListExtra(Define.INTENT_PATH).get(0);
                        }
                    }*/

                }
                break;
            case PHOTO_GALLARY_OPEN:
                if (resultCode == RESULT_OK) {
                    if (intetnData != null) {
                        Uri selectedImage = intetnData.getData();
                        if (selectedImage != null) {
                            String mPath = getPath(selectedImage);
                            user_selecete_image_str_path = mPath;
                            cropCapturedImageGalley(mPath);
                            //iv_user_pic.setImageURI(selectedImage);
                            user_profice_url = selectedImage;

                        }
                    }

                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    cropCapturedImage();
                }
                break;

            case 2:
                if (resultCode == RESULT_OK) {
                    try {
                        String path = intetnData.getStringExtra(CropImage.IMAGE_PATH);
                        user_selecete_image_str_path = path;
                        // if nothing received
                        if (path == null) {
                            return;
                        }
                        Bitmap profilePic = BitmapFactory.decodeFile(path);
//                        iv_user_profile.setLocalImageBitmap(profilePic);

                        Uri uri = common_methods.getImageUri(mActivity, profilePic);
                        user_profice_url = uri;
                        user_pic.setImageURI(uri);

                        if (userDetailTable != null) {
                            if (Validation.isRequiredField(userDetailTable.get_id())) {
                                callUploadImageApi(userDetailTable.get_id(), edt_first.getText().toString(), edt_last_name.getText().toString(),
                                        edt_gender.getText().toString(), edt_phone.getText().toString(), tv_birth.getText().toString(), edt_height.getText().toString(),
                                        edt_weight.getText().toString(), strweightunit, strheightunit, strsmokeseekbar,
                                        strdrinkseekbar, tv_bedtime.getText().toString(), tv_wakeup.getText().toString(), checkedintrest);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        new UnknownError("Error =>>>Crop image error");

                    }
                }
            default:
                break;
        }
    }

    private void cropCapturedImage() {

        try {
            Intent intent = new Intent(mActivity, CropImage.class);
            intent.putExtra(CropImage.IMAGE_PATH, mCurrentPhotoPath);
            intent.putExtra(CropImage.SCALE, true);
            intent.putExtra(CropImage.ASPECT_X, 2);// change ration here via
            intent.putExtra(CropImage.ASPECT_Y, 2);

            startActivityForResult(intent, 2);// final static int 1
        } catch (Exception e) {
            new UnknownError("Error =>>>Crop image error");

        }
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = mActivity.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    private void cropCapturedImageGalley(String strpath) {

        try {

            Intent intent = new Intent(mActivity, CropImage.class);
            intent.putExtra(CropImage.IMAGE_PATH, strpath);
            intent.putExtra(CropImage.SCALE, true);
            intent.putExtra(CropImage.ASPECT_X, 2);// change ration here via
            intent.putExtra(CropImage.ASPECT_Y, 2);
            startActivityForResult(intent, 2);// final static int 1
        } catch (Exception e) {
            new UnknownError("Error =>>>Crop image error");
        }
    }

    private File createImageFile() throws IOException {
        mCurrentPhotoPath = "";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        File direct = new File(Environment.getExternalStorageDirectory() + "/Activo");
        // File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!direct.exists())
            direct.mkdirs();
        File image = new File(direct, "IMG_" + timeStamp + ".JPG");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        } else {

            mCurrentPhotoPath = image.getAbsolutePath();
        }
        Log.e("test", "===>" + mCurrentPhotoPath);
        return image;
    }

    public class HobbyAdaptor extends RecyclerView.Adapter<HobbyAdaptor.MyViewHolder> {

        private List<Area_of_interest> hobbyList;
        private ArrayList<String> SELECTEDINTREST;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.img)
            ImageView img;
            @BindView(R.id.tv_name)
            TextView tv_name;

            public MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);

            }
        }

        public HobbyAdaptor(List<Area_of_interest> langList, ArrayList<String> selectedintrest) {
            this.hobbyList = langList;
            this.SELECTEDINTREST = selectedintrest;
        }

        public void setDataUpdate(List<Area_of_interest> langList) {
            this.hobbyList = langList;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_ipsum_dolor, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int pos) {
            holder.tv_name.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
            final int position = pos;
            // final VehicalBrandList vehicalBrand = vehicalBrandLists.get(position);
            if (hobbyList.get(position) != null) {
                if (Validation.isRequiredField(hobbyList.get(position).getTitle())) {
                    holder.tv_name.setText(hobbyList.get(position).getTitle());
                }

                if (hobbyList.get(position).getStatus().equalsIgnoreCase("Yes")) {
                    holder.img.setVisibility(View.VISIBLE);
                    holder.tv_name.setTextColor(mActivity.getResources().getColor(R.color.txt_black));
                    holder.img.setImageResource(R.drawable.valid);
                } else {
                    holder.tv_name.setTextColor(mActivity.getResources().getColor(R.color.txt_black));
                    holder.img.setVisibility(View.INVISIBLE);
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.tagmanager("Areas of Interest Edit","settings_edit_profile_interests_click");

                    if (hobbyList.get(position).getStatus().equalsIgnoreCase("Yes")) {
                        holder.img.setVisibility(View.VISIBLE);
                        holder.tv_name.setTextColor(mActivity.getResources().getColor(R.color.txt_black));
                        holder.img.setImageResource(R.drawable.valid);
                        hobbyList.get(position).setStatus("No");
                    } else {
                        holder.tv_name.setTextColor(mActivity.getResources().getColor(R.color.txt_black));
                        holder.img.setVisibility(View.INVISIBLE);
                        hobbyList.get(position).setStatus("Yes");
                    }
                    notifyDataSetChanged();
                    checkedintrest = "";
                    // pending

                    for (int i = 0; i < hobbyList.size(); i++) {
                        if (hobbyList.get(i).getStatus().equalsIgnoreCase("Yes")) {
                            String s = hobbyList.get(i).get_id();
                            if (checkedintrest.equalsIgnoreCase("") || checkedintrest.isEmpty()) {
                                checkedintrest = s;
                            } else {
                                checkedintrest = checkedintrest + "|" + s;
                            }

                        } else {


                        }
                    }

                    Log.e("test", "get selectd==>" + checkedintrest);

                }
            });

        }


        @Override
        public int getItemCount() {
            return hobbyList.size();
        }
    }

    public void DisplayProfileData(final String userid) {
        WebApiClient.getInstance(mActivity).getWebApi().DisplayProfile(userid).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                            //hobby recyclview
                            // hobbyList = Hobby.getListBobby();
                            hobbyList = new ArrayList<>();
                            selectedintrest = new ArrayList<>();
                            final List<Area_of_interest> area_of_interest = response.body().getArea_of_interest();

                            for (int i = 0; i < area_of_interest.size(); i++) {

                                hobbyList.add(area_of_interest.get(i));
                                /*Log.e("test", "==>>" + area_of_interest.get(i));
                                if (area_of_interest.get(i).getStatus().equalsIgnoreCase("Yes")) {
                                    selectedintrest.add("true");
                                } else {
                                    selectedintrest.add("false");
                                }*/
                            }

                            hobbyAdaptor = new HobbyAdaptor(hobbyList, selectedintrest);
                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mActivity, 2, GridLayoutManager.VERTICAL, false);
                            recyclview_hobby.setLayoutManager(layoutManager);
                            recyclview_hobby.setItemAnimator(new DefaultItemAnimator());
                            recyclview_hobby.setAdapter(hobbyAdaptor);
                            recyclview_hobby.setNestedScrollingEnabled(false);
                            //  Log.e("get intrest",selectedintrest+"");

                            // set data
                            edt_first.setText(response.body().getData().getFirstname());
                            edt_last_name.setText(response.body().getData().getLastname());
                            edt_phone.setText(response.body().getData().getPhone());
                            tv_birth.setText(response.body().getData().getDate_of_birth());
                            edt_height.setText(response.body().getData().getHeight());
                            edt_weight.setText(response.body().getData().getWeight());
                            if (response.body().getData().getSmokes().equalsIgnoreCase("20")) {
                                tv_seekbar.setText(response.body().getData().getSmokes() + "" + "+");
                            } else {
                                tv_seekbar.setText(response.body().getData().getSmokes());
                            }

                            if (response.body().getData().getDrinks().equalsIgnoreCase("20")) {
                                tv_drinkseekbar.setText(response.body().getData().getDrinks() + "" + "+");
                            } else {
                                tv_drinkseekbar.setText(response.body().getData().getDrinks());
                            }


                            seekBar.setProgress(Integer.parseInt(response.body().getData().getSmokes()));
                            drinkseekBar.setProgress(Integer.parseInt(response.body().getData().getDrinks()));
                            user_pic.setImageURI(response.body().getData().getPhoto());
                            tv_bedtime.setText(response.body().getData().getBadtime());
                            tv_wakeup.setText(response.body().getData().getWakeup());
                            edt_gender.setText(response.body().getData().getSex());

                            strweightunit = response.body().getData().getWeight_unit();
                            strheightunit = response.body().getData().getHeight_unit();
                            strSex = response.body().getData().getSex();

                            if (response.body().getData().getHeight_unit().equalsIgnoreCase("FEET")) {
                                tv_round_cm.setVisibility(View.GONE);
                                tv_cm.setVisibility(View.VISIBLE);
                                tv_round_kg.setVisibility(View.VISIBLE);
                                tv_kg.setVisibility(View.GONE);
                            } else {
                                tv_round_cm.setVisibility(View.VISIBLE);
                                tv_cm.setVisibility(View.GONE);
                                tv_round_kg.setVisibility(View.GONE);
                                tv_kg.setVisibility(View.VISIBLE);
                            }

                            if (response.body().getData().getWeight_unit().equalsIgnoreCase("POUND")) {
                                tv_round_cm_weight.setVisibility(View.GONE);
                                tv_cm_weight.setVisibility(View.VISIBLE);
                                tv_round_kg_weight.setVisibility(View.VISIBLE);
                                tv_kg_weight.setVisibility(View.GONE);
                            } else {
                                tv_round_cm_weight.setVisibility(View.VISIBLE);
                                tv_cm_weight.setVisibility(View.GONE);
                                tv_round_kg_weight.setVisibility(View.GONE);
                                tv_kg_weight.setVisibility(View.VISIBLE);
                            }

                            if (response.body().getData().getSex().equalsIgnoreCase("Male")) {
                                tv_round_female.setVisibility(View.VISIBLE);
                                tv_female.setVisibility(View.GONE);
                                tv_round_male.setVisibility(View.GONE);
                                tv_male.setVisibility(View.VISIBLE);
                            } else {
                                tv_round_female.setVisibility(View.GONE);
                                tv_female.setVisibility(View.VISIBLE);
                                tv_round_male.setVisibility(View.VISIBLE);
                                tv_male.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable throwable) {
                common_methods.setExceptionMessage(throwable, mActivity);
            }
        });
    }

//    public void UpdateProfile(final String user_id, final String firstname, final String lastname, final String phone,
//                              final String birthdate, final String height, final String weight, final String strweightunit, final String strheightunit,
//                              final String smoke, final String drink, final String badtime, final String wakup, final String intrest) {
//
//        WebApiClient.getInstance(mActivity).getWebApi().callUpdateProfile(user_id, firstname, lastname, phone, birthdate, height, weight,
//                strweightunit, strheightunit, smoke, drink, badtime, wakup, intrest).enqueue(new Callback<BaseCommanRespons>() {
//            @Override
//            public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {
//                if (response != null) {
//                    if (response.body() != null) {
//                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
//
//
//                        }
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<BaseCommanRespons> call, Throwable throwable) {
//                common_methods.setExceptionMessage(throwable, mActivity);
//            }
//        });
//    }

    private void callUploadImageApi(final String user_id, final String firstname, final String lastname, final String sex, final String phone,
                                    final String birthdate, final String height, final String weight, final String strweightunit, final String strheightunit,
                                    final String smoke, final String drink, final String badtime, final String wakup, final String intrest) {

        Log.e("test", "intrest=>>>>>>>>" + intrest);
        if (user_selecete_image_str_path != null && !user_selecete_image_str_path.equalsIgnoreCase("")) {
            File file;
            if (Validation.isRequiredField(user_selecete_image_str_path)) {
                file = new File(user_selecete_image_str_path);
                //      }
                final MultipartBody.Part body;
                if (file != null) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    body = MultipartBody.Part.createFormData(WebApi.PHOTO, file.getAbsolutePath(), requestFile);

                } else {
                    Log.e("test", "ERROR  ==> file get null");
                    return;
                }
                if (body == null) {
                    return;
                    //if body found null it returns it's
                }

                RequestBody user_idBody = RequestBody.create(MediaType.parse("text/plain"), user_id);
                RequestBody user_firstnameBody = RequestBody.create(MediaType.parse("text/plain"), firstname);
                RequestBody user_lastnameBody = RequestBody.create(MediaType.parse("text/plain"), lastname);
                RequestBody user_sexBody = RequestBody.create(MediaType.parse("text/plain"), sex);
                RequestBody user_phoneBody = RequestBody.create(MediaType.parse("text/plain"), phone);
                RequestBody user_dobBody = RequestBody.create(MediaType.parse("text/plain"), birthdate);
                RequestBody user_heightBody = RequestBody.create(MediaType.parse("text/plain"), height);
                RequestBody user_weightBody = RequestBody.create(MediaType.parse("text/plain"), weight);
                RequestBody user_weightunitBody = RequestBody.create(MediaType.parse("text/plain"), strweightunit);
                RequestBody user_heightunitBody = RequestBody.create(MediaType.parse("text/plain"), strheightunit);
                RequestBody user_smokeBody = RequestBody.create(MediaType.parse("text/plain"), smoke);
                RequestBody user_drinkBody = RequestBody.create(MediaType.parse("text/plain"), drink);
                RequestBody user_badtimeBody = RequestBody.create(MediaType.parse("text/plain"), badtime);
                RequestBody user_wakeupBody = RequestBody.create(MediaType.parse("text/plain"), wakup);
                RequestBody user_areaofintrestBody = RequestBody.create(MediaType.parse("text/plain"), intrest);

                WebApiClient.getInstance(mActivity).getWebApi().callUpdateProfileWithImage
                        (user_idBody, user_firstnameBody, user_lastnameBody, user_sexBody, user_phoneBody, user_dobBody, user_heightBody,
                                user_weightBody, user_weightunitBody, user_heightunitBody, user_smokeBody, user_drinkBody,
                                user_badtimeBody, user_wakeupBody, user_areaofintrestBody, body).enqueue(new Callback<BaseCommanRespons>() {
                    @Override
                    public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {

                        if (response != null) {
                            if (response.body() != null) {
                                if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                                } else {
                                    mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                                    UserDetailTable userDetailTable=UserDetailTable.getUserDetail();
                                    if(userDetailTable!=null){
                                        userDetailTable.setBadtime(badtime);
                                        userDetailTable.setWakeup(wakup);
                                        userDetailTable.save();
                                    }

                                }
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<BaseCommanRespons> call, Throwable t) {
                        Methods.isProgressHide();
                        common_methods.setExceptionMessage(t, mActivity);

                        Log.e("test", "=>>" + t.getLocalizedMessage());
                    }
                });
            }
        } else {

            WebApiClient.getInstance(mActivity).getWebApi().callUpdateProfile(user_id, firstname, lastname, sex, phone, birthdate, height, weight,
                    strweightunit, strheightunit, smoke, drink, badtime, wakup, intrest).enqueue(new Callback<BaseCommanRespons>() {
                @Override
                public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {
                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                                UserDetailTable userDetailTable=UserDetailTable.getUserDetail();
                                if(userDetailTable!=null){
                                  userDetailTable.setBadtime(badtime);
                                  userDetailTable.setWakeup(wakup);
                                  userDetailTable.save();
                                }
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

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("test", "calll Onstop");
        if (userDetailTable != null) {
            if (Validation.isRequiredField(userDetailTable.get_id())) {
                callUploadImageApi(userDetailTable.get_id(), edt_first.getText().toString(), edt_last_name.getText().toString(),
                        edt_gender.getText().toString(), edt_phone.getText().toString(), tv_birth.getText().toString(), edt_height.getText().toString(),
                        edt_weight.getText().toString(), strweightunit, strheightunit, strsmokeseekbar,
                        strdrinkseekbar, tv_bedtime.getText().toString(), tv_wakeup.getText().toString(), checkedintrest);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Log.e("test", "calll onHiddenChanged false");

        } else {
            Log.e("test", "calll onHiddenChanged ture");
            if (userDetailTable != null) {
                if (Validation.isRequiredField(userDetailTable.get_id())) {
                    callUploadImageApi(userDetailTable.get_id(), edt_first.getText().toString(), edt_last_name.getText().toString(),
                            edt_gender.getText().toString(), edt_phone.getText().toString(), tv_birth.getText().toString(), edt_height.getText().toString(),
                            edt_weight.getText().toString(), strweightunit, strheightunit, strsmokeseekbar,
                            strdrinkseekbar, tv_bedtime.getText().toString(), tv_wakeup.getText().toString(), checkedintrest);
                }
            }

        }
    }
}
