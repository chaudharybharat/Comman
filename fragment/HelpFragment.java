package com.aktivo.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.ConnectionUtil;
import com.aktivo.response.PostCMS;
import com.aktivo.response.PostCMSReponse;
import com.aktivo.webservices.WebApiClient;
import com.commonmodule.mi.utils.Validation;
import com.raizlabs.android.dbflow.sql.language.Delete;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.tv_term_and_condtion)
    TextView tv_term_and_condtion;
    @BindView(R.id.tv_not_found)
    TextView tv_not_found;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_aktivo)
    TextView tv_aktivo;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.llMainScroll)
    ScrollView llMainScroll;
    boolean flag = false;

    public static final String FAQ = "FAQ";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.help_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        tv_back.setVisibility(View.GONE);
        tv_aktivo.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_title.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        //tv_title.setText(CommonKeys.TERMSANDCONDITION);
        tv_term_and_condtion.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_back.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        setHeader();
        initComponet();
        mActivity.tagmanager("Settings Help Screen", "settings_help_view");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (flag = false) {
                flag = true;
                llMainScroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        mActivity.tagmanager("Settings Help Screen", "settings_help_scroll");

                    }
                });
            }

        }
    }

    private void setHeader() {
        mActivity.seletect_tab(CommonKeys.CLEAR_TAB);
        mActivity.setToolbarTopVisibility(false);
        //  mActivity.setTitleToolbar(CommonKeys.TERMSANDCONDITION);
        //  mActivity.setToolbarBottomVisibility(false);
        //mActivity.setMenuGone();
        mActivity.enableDrawer();
        if (Validation.isRequiredField(common_methods.getTodayHaveData().getHelp_background_image())) {
            mActivity.setBackgroudnImage(common_methods.getTodayHaveData().getHelp_background_image());
        }
    }

    private void initComponet() {
        List<PostCMS> postCMS = PostCMS.getPostCMSDetail();
        if (postCMS != null && postCMS.size() > 0) {

            for (int i = 0; i < postCMS.size(); i++) {
                if (postCMS.get(i).getCode().equalsIgnoreCase(FAQ)) {
                    String pricvay_policy = postCMS.get(i).getDescription();
                    if (Validation.isRequiredField(pricvay_policy)) {
                        tv_term_and_condtion.setText(Html.fromHtml(pricvay_policy));
                        tv_not_found.setVisibility(View.GONE);
                        tv_back.setVisibility(View.VISIBLE);
                    } else {
                        tv_not_found.setVisibility(View.VISIBLE);
                    }
                }

            }
        } else if (ConnectionUtil.isInternetAvailable(mActivity)) {
            callCmsApi();
        }
    }

    private void callCmsApi() {
        progressbar.setVisibility(View.VISIBLE);
        WebApiClient.getInstance(mActivity).getWebApi().callPostCMSApi().enqueue(new Callback<PostCMSReponse>() {
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
                                    if (data.get(i).getCode().equalsIgnoreCase(FAQ)) {
                                        String pricvay_policy = data.get(i).getDescription();
                                        if (Validation.isRequiredField(pricvay_policy)) {
                                            tv_term_and_condtion.setText(Html.fromHtml(pricvay_policy));
                                            tv_not_found.setVisibility(View.GONE);
                                            tv_back.setVisibility(View.VISIBLE);

                                        } else {
                                            tv_not_found.setVisibility(View.VISIBLE);
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
                progressbar.setVisibility(View.GONE);
                tv_back.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<PostCMSReponse> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
                tv_back.setVisibility(View.VISIBLE);

            }
        });
    }


    @OnClick({R.id.tv_back, R.id.iv_menu})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                mActivity.onBackPressed();
                break;
            case R.id.iv_menu:
                mActivity.openDrawer();
                break;

            default:
                break;
        }

    }
}
