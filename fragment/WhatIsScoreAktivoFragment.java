package com.aktivo.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.ConnectionUtil;
import com.aktivo.Utils.Methods;
import com.aktivo.Utils.MyPreferences;
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
public class WhatIsScoreAktivoFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.tv_aktivo)
    TextView tv_aktivo;
    @BindView(R.id.iv_menu)
    ImageView iv_menu;
    @BindView(R.id.tv_not_found)
    TextView tv_not_found;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.check_term_condition)
    CheckBox check_term_condition;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
 @BindView(R.id.ll_dont_show)
 LinearLayout ll_dont_show;

    public static final String WHAT_IS_AKTIVO="WHAT_IS_AKTIVO";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.what_is_score_aktivo_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        setHeader();
        intiComponet();
    }
    private void intiComponet() {
        tv_back.setVisibility(View.GONE);
        check_term_condition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Log.e("test","=>>"+isChecked);
                    MyPreferences.setPref(mActivity,CommonKeys.WHAT_IS_ACTIVO_PRFR_KEY,CommonKeys.FALSE);
                }else {
                    MyPreferences.setPref(mActivity,CommonKeys.WHAT_IS_ACTIVO_PRFR_KEY,CommonKeys.TRUE);

                }
            }
        });
        if(ConnectionUtil.isInternetAvailable(mActivity)){
            callCmsApi();
        }
    }
    private void setHeader() {
        mActivity.setToolbarTopVisibility(false);
        mActivity.setToolbarBottomVisibility(false);
        mActivity.setBackgroudnImage(CommonKeys.backgroundwhaite);

    }
    @OnClick({R.id.iv_menu,R.id.tv_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_menu:
                case R.id.tv_back:
                mActivity.onBackPressed();
                break;
                default:
                    break;
        }

    }
    private void callCmsApi() {
        progressbar.setVisibility(View.VISIBLE);
        WebApiClient.getInstance(mActivity).getWebApi().callPostCMSApi().enqueue(new Callback<PostCMSReponse>() {
            @Override
            public void onResponse(Call<PostCMSReponse> call, Response<PostCMSReponse> response) {
                if(response!=null){
                    if(response.body()!=null){
                        if(response.body().getData()!=null){
                            Delete.table(PostCMS.class);
                            List<PostCMS> data = response.body().getData();
                            if(data!=null && data.size()>0){
                                for (int i = 0; i <data.size() ; i++) {
                                    data.get(i).save();
                                    if(data.get(i).getCode().equalsIgnoreCase(WHAT_IS_AKTIVO)){
                                        String what_is_aktivo=data.get(i).getDescription();
                                        if(Validation.isRequiredField(what_is_aktivo)){
                                            tv_aktivo.setText(Html.fromHtml(what_is_aktivo));
                                            tv_not_found.setVisibility(View.GONE);
                                            tv_back.setVisibility(View.VISIBLE);
                                        }else {
                                            tv_not_found.setVisibility(View.VISIBLE);
                                        }
                                    }

                                }
                            }

                        }
                    }
                }
                ll_dont_show.setVisibility(View.VISIBLE);
                tv_back.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);

            }
            @Override
            public void onFailure(Call<PostCMSReponse> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
                tv_back.setVisibility(View.VISIBLE);
                ll_dont_show.setVisibility(View.VISIBLE);

            }
        });
    }

}
