package com.aktivo.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.Common_Methods;
import com.aktivo.Utils.ConnectionUtil;
import com.aktivo.Utils.Methods;
import com.aktivo.table.Almost_over;
import com.aktivo.response.CompeteData;
import com.aktivo.response.CompeteReponse;
import com.aktivo.table.Competitors;
import com.aktivo.table.Ongoing;
import com.aktivo.table.Over;
import com.aktivo.response.UserDetailTable;
import com.aktivo.webservices.WebApiClient;
import com.commonmodule.mi.utils.Validation;
import com.raizlabs.android.dbflow.sql.language.Delete;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedAktivoFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tv_ongoing)
    TextView tv_ongoing;
    @BindView(R.id.tv_almost_over)
    TextView tv_almost_over;
    @BindView(R.id.tv_over)
    TextView tv_over;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_aktivo)
    TextView tv_aktivo;

    Unbinder unbinder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.completed_aktivo_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder=ButterKnife.bind(this,view);
        setHeader();
        initViewpager();
        initComponet();
        setFont();
    }

    private void setFont() {
        tv_ongoing.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Bold));
        tv_almost_over.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Bold));
        tv_over.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Bold));
    }

    private void initViewpager() {
        setupViewPager(viewPager);
     //   tabLayout.setupWithViewPager(viewPager);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                tabLayoutCurren(position);
                Log.e("test","position"+position);
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the Tab.

            }
        });

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new OnGoingFragment(),"Ongoing");
        adapter.addFragment(new AlMostOverFragment(), "ALMOST OVER");
        adapter.addFragment(new OverFragment(),"OVER");
        viewPager.setAdapter(adapter);
        tabLayoutCurren(0);
    }

    private void initComponet() {
        if(ConnectionUtil.isInternetAvailable(mActivity)){
            UserDetailTable userDetail = UserDetailTable.getUserDetail();
            if(userDetail!=null){
                if(Validation.isRequiredField(userDetail.get_id())){
                    callCompleteDataApi(userDetail.get_id());
                    Log.e("test","Member_code=>"+userDetail.get_id());
                   // "59c118599804d176a0b08fec"
                }
            }
        }
        // Initilization


    }
     Call<CompeteReponse> competeReponseCall;
    private void callCompleteDataApi(final String member_id) {
       competeReponseCall = WebApiClient.getInstance(mActivity).getWebApi().callCompeteApi(member_id);
        competeReponseCall.enqueue(new Callback<CompeteReponse>() {
            @Override
            public void onResponse(Call<CompeteReponse> call, Response<CompeteReponse> response) {
                if(response!=null){
                    if(response.body()!=null){
                        if(response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)){
                            Delete.table(Over.class);
                            Delete.table(Almost_over.class);
                            Delete.table(Ongoing.class);
                            Delete.table(Competitors.class);
                            CompeteData data = response.body().getData();
                            if(data!=null){
                                List<Over> overlist = data.getOver();
                                if(overlist!=null && overlist.size()>0){
                                    for (int i = 0; i <overlist.size() ; i++) {
                                        overlist.get(i).save();
                                        List<Competitors> competitors = overlist.get(i).getCompetitors();
                                        if(competitors!=null && competitors.size()>0){
                                            for (int j = 0; j < competitors.size(); j++) {
                                                competitors.get(j).setId(overlist.get(i).get_id());
                                                competitors.get(j).save();
                                            }
                                        }
                                    }
                                }
                                List<Ongoing> ongoingList = data.getOngoing();
                                if(ongoingList!=null && ongoingList.size()>0){
                                    for (int i = 0; i <ongoingList.size() ; i++) {
                                        ongoingList.get(i).save();
                                        List<Competitors> competitors = ongoingList.get(i).getCompetitors();
                                        if(competitors!=null && competitors.size()>0){
                                            for (int j = 0; j < competitors.size(); j++) {
                                                competitors.get(j).setId(ongoingList.get(i).get_id());
                                                competitors.get(j).save();
                                            }
                                        }
                                    }
                                }
                                List<Almost_over> almost_over = data.getAlmost_over();

                                if(almost_over!=null && almost_over.size()>0){
                                    for (int i = 0; i <almost_over.size() ; i++) {
                                        almost_over.get(i).save();
                                        List<Competitors> competitors = almost_over.get(i).getCompetitors();
                                        if(competitors!=null && competitors.size()>0){
                                            for (int j = 0; j < competitors.size(); j++) {
                                                competitors.get(j).setId(almost_over.get(i).get_id());
                                                competitors.get(j).save();
                                            }
                                        }
                                    }
                                }
                            }
                            
                        }else {
                            mActivity.showCroutonsMessage(mActivity,response.body().getMessage());
                        }
                    }
                }
                Intent updateListBroadcast=new Intent(CommonKeys.UPADTE_LISTCOMPLETLIST_BR);
                Common_Methods.send_Loc_BroadCast_intent2(mActivity,updateListBroadcast);

            }

            @Override
            public void onFailure(Call<CompeteReponse> call, Throwable t) {
                common_methods.setExceptionMessage(t,mActivity);
            }
        });
    }

    private void setHeader() {
        mActivity.seletect_tab(CommonKeys.COMPETE_TAB);
        mActivity.setToolbarTopVisibility(false);
        mActivity.setToolbarBottomVisibility(true);
        tv_aktivo.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Bold));
        tv_title.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_ExtraLight));
        mActivity.enableDrawer();
        if(Validation.isRequiredField(common_methods.getTodayHaveData().getCompete_background_image())){
            mActivity.setBackgroudnImage(common_methods.getTodayHaveData().getCompete_background_image());
        }


        // mActivity.setBackgroudnImage("http://aktivolabs.coderspreview.com:1338/flashscreen/g1kfqf32y8.jpg");
    }



    @OnClick({R.id.tv_almost_over,R.id.tv_over,R.id.tv_ongoing,R.id.iv_menu})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_menu:
                mActivity.openDrawer();
                break;
            case R.id.tv_ongoing:
                tabLayoutCurren(0);
                viewPager.setCurrentItem(0,true);
                mActivity.tagmanager("Challenge Type 'Ongoing' tab","compete_ongoing_click");
                break;
            case R.id.tv_almost_over:
                tabLayoutCurren(1);
                viewPager.setCurrentItem(1,true);
                mActivity.tagmanager("Challenge Type 'Almost Over' tab","compete_almost_over_click");

                break;
            case R.id.tv_over:
                tabLayoutCurren(2);
                viewPager.setCurrentItem(2,true);
                mActivity.tagmanager("ChaChallenge Type 'Over' tab","compete_over_click");
                break;
                default:
                    break;
        }

    }

    private void tabLayoutCurren(int pos) {
        switch (pos){
            case 0:
                tv_ongoing.setTextColor(mActivity.getResources().getColor(R.color.white));
                tv_ongoing.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_black_button));
                tv_almost_over.setTextColor(mActivity.getResources().getColor(R.color.black));
                tv_almost_over.setBackground(null);
                tv_over.setTextColor(mActivity.getResources().getColor(R.color.black));
                tv_over.setBackground(null);
                break;
            case 1:
                tv_almost_over.setTextColor(mActivity.getResources().getColor(R.color.white));
                tv_almost_over.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_black_button));
                tv_ongoing.setTextColor(mActivity.getResources().getColor(R.color.black));
                tv_ongoing.setBackground(null);
                tv_over.setTextColor(mActivity.getResources().getColor(R.color.black));
                tv_over.setBackground(null);
                break;
            case 2:
                tv_over.setTextColor(mActivity.getResources().getColor(R.color.white));
                tv_over.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_black_button));
                tv_almost_over.setTextColor(mActivity.getResources().getColor(R.color.black));
                tv_almost_over.setBackground(null);
                tv_ongoing.setTextColor(mActivity.getResources().getColor(R.color.black));
                tv_ongoing.setBackground(null);
                break;
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            setHeader();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(competeReponseCall!=null){
            competeReponseCall.cancel();
        }
       // unbinder.unbind();
        Log.e("test","on onDestroyView call==>>");

    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("test","onDetach call==>>");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
       // unbinder.unbind();
        Log.e("test","on Destory call==>>");
    }
}
