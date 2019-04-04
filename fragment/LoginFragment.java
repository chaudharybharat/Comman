package com.aktivo.fragment;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aktivo.MainActivity;
import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.ConnectionUtil;
import com.aktivo.Utils.Methods;
import com.aktivo.Utils.MyPreferences;
import com.aktivo.Utils.SharedPrefManager;
import com.aktivo.response.BaseCommanRespons;
import com.aktivo.response.SinginResponse;
import com.aktivo.response.UserDetailTable;
import com.aktivo.webservices.WebApiClient;
import com.commonmodule.mi.Activity.MIActivity;
import com.commonmodule.mi.utils.Validation;
import com.raizlabs.android.dbflow.sql.language.Delete;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.edt_email)
    EditText edt_email;
    @BindView(R.id.edt_otp)
    EditText edt_otp;
    @BindView(R.id.tv_signin)
    TextView tv_signin;
    @BindView(R.id.tv_click_here)
    TextView tv_click_here;
    @BindView(R.id.tv_login_problme)
    TextView tv_login_problme;
    @BindView(R.id.tv_privaincy_policy)
    TextView tv_privaincy_policy;
    @BindView(R.id.check_term_condition)
    CheckBox check_term_condition;
    @BindView(R.id.ll_verfication_three_second)
    RelativeLayout ll_verfication_three_second;
    @BindView(R.id.tv_msg)
    TextView tv_msg;
    @BindView(R.id.tv_aktivo)
    TextView tv_aktivo;
    @BindView(R.id.sing)
    TextView tv_sing;
    @BindView(R.id.tv_resend_otp)
    TextView tv_resend_otp;
    @BindView(R.id.tv_view_second)
    View tv_view_second;
    @BindView(R.id.rl_main)
    RelativeLayout rl_main;
    @BindView(R.id.rl_otp_send)
    RelativeLayout rl_otp_send;

    String resend_verification_code = "Resend <b>Verification Code</b>";
    String RECIVE_VERIFICATION = "REQUEST VERIFICATION CODE";
    String SINGIN_TEXT = "SIGN IN";
    String pricyc_and_terma_condtion = "To continue with sign-in tick the checkbox to accept <b>Aktivolabs Terms & Conditions</b> and <b>Privacy Policy.</b>";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setHeader();
        intiComponet();
        tv_resend_otp.setText(Html.fromHtml(resend_verification_code));

        mActivity.tagmanager("Sign-In Screen", "signin_screen_view");
    }

    private void setHeader() {
        String bg_image = MyPreferences.getPrefLoginBg(mActivity, CommonKeys.LOGIN_BG_IMGAG);
        if (Validation.isRequiredField(bg_image)) {
            mActivity.setBackgroudnImage(bg_image);
        }
        mActivity.disableDrawer();
        mActivity.setToolbarTopVisibility(false);
        mActivity.setToolbarBottomVisibility(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setHeader();
        }
    }


    private void intiComponet() {
        Typeface aktivo_font = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-Bold.ttf");
        Typeface title = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-ExtraLight.ttf");
        tv_aktivo.setTypeface(aktivo_font);
        tv_msg.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_sing.setTypeface(title);
        edt_otp.setEnabled(false);
        tv_signin.setText(RECIVE_VERIFICATION);
        String privance_policy = "I agree to Aktivolabs Terms & Conditions and Privacy Policy";
        SpannableString styledString = new SpannableString(privance_policy);
        // change text color
        styledString.setSpan(new StyleSpan(Typeface.BOLD), privance_policy.indexOf("Terms"), privance_policy.indexOf("Conditions") + 10, 0);
        styledString.setSpan(new ForegroundColorSpan(Color.BLACK), privance_policy.indexOf("Terms"), privance_policy.indexOf("Conditions") + 10, 0);
        styledString.setSpan(new StyleSpan(Typeface.BOLD), privance_policy.indexOf("Privacy"), privance_policy.length(), 0);
        styledString.setSpan(new ForegroundColorSpan(Color.BLACK), privance_policy.indexOf("Privacy"), privance_policy.length(), 0);

        // clickable text
        ClickableSpan clickableSpan_pricancy_policy = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // We display a Toast. You could do anything you want here.
                mActivity.pushFragmentDontIgnoreCurrent(new PrivacyPolicy(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                mActivity.tagmanager("Privacy Policy link", "signin_privacy_click");
            }
        };
        ClickableSpan clickableSpan_term_and_condition = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // We display a Toast. You could do anything you want here.

//                Bundle params = new Bundle();
//                params.putString("click_page_name","TERMS & CONDITION");
//                params.putString("USERNAME","Hitendra");
//                mFirebaseAnalytics.logEvent("signin_tc_click", params);

                mActivity.tagmanager("Terms & Conditions link", "signin_tc_click");
                mActivity.pushFragmentDontIgnoreCurrent(new TermAndConditionFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
            }
        };
        tv_privaincy_policy.setMovementMethod(LinkMovementMethod.getInstance());
        styledString.setSpan(clickableSpan_term_and_condition, privance_policy.indexOf("Terms"), privance_policy.indexOf("Conditions") + 10, 0);
        styledString.setSpan(clickableSpan_pricancy_policy, privance_policy.indexOf("Privacy"), privance_policy.length(), 0);

        tv_privaincy_policy.setText(styledString);
        //edt_email.setText("");
        Typeface Medium_font = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-Medium.ttf");
        edt_email.setTypeface(Medium_font);
        edt_otp.setTypeface(Medium_font);
        Typeface font_problmem = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-Light.ttf");
        tv_signin.setTypeface(Medium_font);
        tv_resend_otp.setTypeface(Medium_font);
        tv_login_problme.setTypeface(font_problmem);
        tv_privaincy_policy.setTypeface(font_problmem);
        Typeface click_here = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-Bold.ttf");


    }

    private long mLastClickTime = 0;

    @OnClick({R.id.tv_signin, R.id.tv_click_here, R.id.tv_resend_otp, R.id.iv_resend})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_signin:
                try {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getUserDetail();
                mActivity.tagmanager("Request Verification Code button", "signin_request_vc_click");

                break;
            case R.id.tv_click_here:
                mActivity.pushFragmentDontIgnoreCurrent(new AktivoFAQ(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                mActivity.tagmanager("Problem Login FAQ link", "signin_faq_click");
                break;
            case R.id.tv_resend_otp:
            case R.id.iv_resend:

                mActivity.tagmanager("Resend Verification Code button", "signin_resend_vc_click");
                try {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String email_id = edt_email.getText().toString().trim();
                if (Validation.isRequiredField(email_id)) {
                    if (ConnectionUtil.isInternetAvailable(mActivity)) {
                        callLoginApi(email_id, true);
                    }
                }
                break;

            default:
                break;
        }
    }

    private void getUserDetail() {
        String tv_textValue = tv_signin.getText().toString();
        if (tv_textValue.equalsIgnoreCase(RECIVE_VERIFICATION)) {
            String email = edt_email.getText().toString().trim();
            if (Validation.isRequiredField(email)) {
                if (Validation.isEmailValid(email)) {
                    edt_email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.valid_sign, 0);
                    if (check_term_condition.isChecked()) {

                        if (ConnectionUtil.isInternetAvailable(mActivity)) {
                            callLoginApi(email, false);
                        }
                        // openDialogEmailAlredayExists();
                    } else {
                        //  common_methods.openDialog(mActivity,pricyc_and_terma_condtion);
                        common_methods.setCutemDialogMessage(mActivity, pricyc_and_terma_condtion);
                        //  mActivity.showCroutonsMessage(mActivity, mActivity.getResources().getString(R.string.valid_checked_term_and_condtion));
                    }
                } else {
                    // common_methods.openDialog(mActivity,mActivity.getResources().getString(R.string.valid_not_valid_email));
                    common_methods.setCutemDialogMessage(mActivity, mActivity.getResources().getString(R.string.valid_not_valid_email));
                    // mActivity.showCroutonsMessage(mActivity, mActivity.getResources().getString(R.string.valid_not_valid_email));
                    edt_email.requestFocus();
                    edt_email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.invalid, 0);
                }
            } else {
                edt_email.requestFocus();
                common_methods.setCutemDialogMessage(mActivity, mActivity.getResources().getString(R.string.valid_email));
                // common_methods.openDialog(mActivity,mActivity.getResources().getString(R.string.valid_email));
                //  mActivity.showCroutonsMessage(mActivity, mActivity.getResources().getString(R.string.valid_email));
                edt_email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.invalid, 0);
            }
        } else {
            String otp = edt_otp.getText().toString().trim();
            String email = edt_email.getText().toString().trim();
            if (Validation.isRequiredField(email)) {
                if (!Validation.isEmailValid(email)) {
                    edt_email.requestFocus();
                    common_methods.setCutemDialogMessage(mActivity, mActivity.getResources().getString(R.string.valid_not_valid_email));
                    // common_methods.openDialog(mActivity,mActivity.getResources().getString(R.string.valid_not_valid_email));
                    // mActivity.showCroutonsMessage(mActivity, mActivity.getResources().getString(R.string.valid_not_valid_email));
                    edt_email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.invalid, 0);
                    return;
                } else {
                    edt_email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.valid_sign, 0);
                }
            }
            if (Validation.isRequiredField(otp)) {
                edt_otp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.valid_sign, 0);
                if (check_term_condition.isChecked()) {
                    if (ConnectionUtil.isInternetAvailable(mActivity)) {
                        callOtpWithSingInApi(email, otp);
                    }
                    // openDialogEmailAlredayExists();
                    mActivity.tagmanager("T&C Checkbox", "signin_tc_checkbox_click");
                } else {
                    common_methods.setCutemDialogMessage(mActivity, pricyc_and_terma_condtion);
                    // common_methods.openDialog(mActivity,pricyc_and_terma_condtion);
                    // mActivity.showCroutonsMessage(mActivity, mActivity.getResources().getString(R.string.valid_checked_term_and_condtion));
                }

            } else {
                common_methods.setCutemDialogMessage(mActivity, mActivity.getResources().getString(R.string.valid_otp));
                //common_methods.openDialog(mActivity,mActivity.getResources().getString(R.string.valid_otp));
                //  mActivity.showCroutonsMessage(mActivity, mActivity.getResources().getString(R.string.valid_otp));
                edt_otp.requestFocus();
                edt_otp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.invalid, 0);
            }

        }

    }

    private void callLoginApi(String email, final boolean is_resend) {
        Methods.isProgressShow(mActivity);
        WebApiClient.getInstance(mActivity).getWebApi().callSignInApi(email).enqueue(new Callback<BaseCommanRespons>() {
            @Override
            public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                            // openVerificationThreeSecond(response.body().getMessage());
                            common_methods.setCutemDialogMessage(mActivity, response.body().getMessage());
                            if (!is_resend) {
                                tv_signin.setText(SINGIN_TEXT);
                                rl_otp_send.setVisibility(View.VISIBLE);
                                tv_signin.setVisibility(View.VISIBLE);
                            }
                            edt_otp.setEnabled(true);
                            edt_otp.setClickable(true);
                            edt_otp.setFocusable(true);
                            edt_email.setEnabled(false);
                            tv_view_second.setVisibility(View.VISIBLE);

                        } else {
                            common_methods.setCutemDialogMessage(mActivity, response.body().getMessage());
                            // common_methods.openDialog(mActivity,response.body().getMessage());
                        }
                    }
                }
                Methods.isProgressHide();
            }

            @Override
            public void onFailure(Call<BaseCommanRespons> call, Throwable t) {
                common_methods.setExceptionMessage(t, mActivity);
                Methods.isProgressHide();
            }
        });

    }

    private void callOtpWithSingInApi(String email, String otp) {

        Methods.isProgressShow(mActivity);
        WebApiClient.getInstance(mActivity).getWebApi().callSignInWihtOtpApi(email, otp).enqueue(new Callback<SinginResponse>() {
            @Override
            public void onResponse(Call<SinginResponse> call, Response<SinginResponse> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                            Delete.table(UserDetailTable.class);
                            mActivity.clearBackStack();
                            mActivity.clearBackStackFragmets();
                            mActivity.onselectTabPostion(0);
                            mActivity.setMenuVisibleGone(true);
                            UserDetailTable userDetail = response.body().getData();
                            if (userDetail != null) {
                                userDetail.save();
                            }
                            MyPreferences.setPref(mContext, MyPreferences.LOGIN_PREFERENCES, CommonKeys.TRUE);
                            MyPreferences.setPref(mContext, CommonKeys.WHAT_IS_ACTIVO_PRFR_KEY, CommonKeys.TRUE);
                            MyPreferences.setPref(mContext, CommonKeys.TURORIAL_SCREEN_SHOW_AGAIN, CommonKeys.TRUE);
                            MyPreferences.setPref(mContext, CommonKeys.USER_COMMING_FIRST_TIME_HOME, CommonKeys.TRUE);
                            GenerateAktivoScore(response.body().getData().get_id());
                            callDeviceRegister(response.body().getData().get_id());
                            // mActivity.pushFragmentDontIgnoreCurrent(new TutorialMainFragment(), MIActivity.FRAGMENT_JUST_ADD);

                            if (!CommonKeys.is_login_current) {
                                mActivity.pushFragmentDontIgnoreCurrent(new HomeFragment(), MIActivity.FRAGMENT_JUST_ADD);
                            } else {
                                mActivity.pushFragmentDontIgnoreCurrent(new HomeFragment(), MIActivity.FRAGMENT_JUST_REPLACE);

                            }

                        } else {
                            common_methods.setCutemDialogMessage(mActivity, response.body().getMessage());
                            // common_methods.openDialog(mActivity,response.body().getMessage());
                        }
                    }
                }
                Methods.isProgressHide();
            }

            @Override
            public void onFailure(Call<SinginResponse> call, Throwable t) {
                common_methods.setExceptionMessage(t, mActivity);
                Methods.isProgressHide();
            }
        });
    }


    public void openVerificationThreeSecond(final String msg) {
        ll_verfication_three_second.setVisibility(View.VISIBLE);
        // tv_msg.setText(""+msg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // common_methods.openDialog(mActivity,msg);
                common_methods.setCutemDialogMessage(mActivity, msg);
                ll_verfication_three_second.setVisibility(View.GONE);
            }
        }, 2000);

    }

    public void GenerateAktivoScore(final String userid) {

        WebApiClient.getInstance(mActivity).getWebApi().GenerateAktivoScore(userid).enqueue(new Callback<BaseCommanRespons>() {
            @Override
            public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {

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

    private boolean checkValidation(String email, String otp) {

        boolean retuen_value = true;
        if (!Validation.isRequiredField(email)) {
            edt_email.requestFocus();
            //common_methods.openDialog(mActivity,mActivity.getResources().getString(R.string.valid_email));
            // mActivity.showCroutonsMessage(mActivity,mActivity.getResources().getString(R.string.valid_email));
            edt_email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.invalid, 0);
            retuen_value = false;
        } else {
            if (!Validation.isEmailValid(email)) {
                // common_methods.openDialog(mActivity,mActivity.getResources().getString(R.string.valid_not_valid_email));
                //  mActivity.showCroutonsMessage(mActivity, mActivity.getResources().getString(R.string.valid_not_valid_email));
                edt_email.requestFocus();
                edt_email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.invalid, 0);
                retuen_value = false;
            } else {
                edt_email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.valid_sign, 0);

            }
        }
        if (!Validation.isRequiredField(otp)) {
            edt_otp.requestFocus();
            // common_methods.openDialog(mActivity,mActivity.getResources().getString(R.string.valid_otp));
            //  mActivity.showCroutonsMessage(mActivity,mActivity.getResources().getString(R.string.valid_otp));
            edt_otp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.invalid, 0);

            retuen_value = false;
            ;
        } else {
            edt_otp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.valid_sign, 0);

        }
        return retuen_value;
    }

    public void callDeviceRegister(final String userid) {
        Log.e("get data", userid + "::" + Settings.Secure.getString(mActivity.getContentResolver(), Settings.Secure.ANDROID_ID) + "::" + String.valueOf(android.os.Build.MODEL) + "::" + "Android" + "::" + SharedPrefManager.getDeviceToken(mActivity));
        WebApiClient.getInstance(mActivity).getWebApi().callDeviceRegister(userid, Settings.Secure.getString(mActivity.getContentResolver(), Settings.Secure.ANDROID_ID), String.valueOf(android.os.Build.MODEL), "Android", SharedPrefManager.getDeviceToken(mActivity)).enqueue(new Callback<BaseCommanRespons>() {
            @Override
            public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                            Log.e("device register", "success");
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
