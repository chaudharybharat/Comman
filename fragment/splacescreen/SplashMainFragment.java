package com.aktivo.fragment.splacescreen;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.ConnectionUtil;
import com.aktivo.Utils.MyPreferences;
import com.aktivo.fragment.BaseFragment;
import com.aktivo.fragment.LoginFragment;
import com.aktivo.fragment.gettingStarted.TutorialMainFragment;
import com.aktivo.response.SplashTurorial;
import com.aktivo.response.SplashTutorailResponse;
import com.aktivo.webservices.WebApiClient;
import com.facebook.drawee.view.SimpleDraweeView;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.commonmodule.mi.Activity.MIActivity.FRAGMENT_JUST_ADD;

/**
 * Created by mind on 28/11/16.
 */

public class SplashMainFragment extends BaseFragment implements View.OnClickListener {

    SplashScreen1 splaceScreen1;
    SplashScreen2 splaceScreen2;
    SplashScreen3 splaceScreen3;
    SplashScreen4 splaceScreen4;
    TutorialMainFragment tutorialMainFragment;

  /*  @BindView(R.id.circleIndicator)
    CircleIndicator circleIndicator;
*/
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_skip)
    TextView tv_skip;
   /* @BindView(R.id.tv_title)
    TextView tv_title;
*/
   /* @BindView(R.id.tv_detail)
    TextView tv_detail;*/
   Typeface title_font;
    Handler handler;
    TimePicker timePicker;
    FragmentPagerAdapter adapter;
    List<SplashTurorial> splaceListdata;
    boolean is_login_not_push=true;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splace_fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initComponents();
         title_font = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Montserrat-Bold.ttf");

        tv_skip.setOnClickListener(this);
        splaceListdata=new ArrayList<>();

        if(ConnectionUtil.isInternetAvailable(mActivity)){
            callSplashApi();
        }

      //  splaceListdata=SplaceListdata.getListVlaue();
       // mActivity.setToolbarVisibility(false);

        adapter= new ViewPagerAdapter(getChildFragmentManager());
       /* if(Utility.getStringSharedPreferences(mContext, StringDifferentLanguage.SKIP).isEmpty()
                || Utility.getStringSharedPreferences(mContext,StringDifferentLanguage.SKIP).equalsIgnoreCase(""))
        {
            tv_skip.setText("Skip");
        }
        else
        {
            tv_skip.setText(Utility.getStringSharedPreferences(mContext,StringDifferentLanguage.SKIP));

        }*/
        mActivity.tagmanager("Splash Screen 1","splash_screen_1_view");


        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                private static final float thresholdOffset = 0.5f;
                private boolean scrollStarted, checkDirection;

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                   /* if(splaceListdata!=null && !splaceListdata.isEmpty()){

                        tv_first_txt.setText(splaceListdata.get(position).getText_first());
                        tv_second_number.setText(splaceListdata.get(position).getLast());
                    }

*/
                    //updateUI(position);



                    viewpager_scroll_postion =position;
                    if(handler!=null){
                        handler.removeCallbacks(runnable);
                        handler.postDelayed(runnable,3000);
                    }else {
                        handler=new Handler();
                        handler.removeCallbacks(runnable);
                        handler.postDelayed(runnable,3000);
                    }
                    int lastIndex=adapter.getCount()-1;

                    int currentPage =viewpager.getCurrentItem();

                    if (checkDirection) {
                        if (thresholdOffset > positionOffset && lastIndex==currentPage ) {
                            mActivity.clearBackStack();
                            if(handler!=null){

                                handler.removeCallbacks(runnable);
                            }
                            if(is_login_not_push){
                                is_login_not_push=false;
                                mActivity.pushFragmentDontIgnoreCurrentWithAnimation(new LoginFragment(), FRAGMENT_JUST_ADD);
                            }
                            Log.i("Left", "going left");
                        } else {

                            Log.i("Right", "going right");
                        }
                        checkDirection = false;
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    Log.e("test","chagne page "+position);
                    if (position ==1){
                        mActivity.tagmanager("Splash Screen 2","splash_screen_2_view");
                    }else if (position == 2){
                        mActivity.tagmanager("Splash Screen 3","splash_screen_3_view");
                    }else if (position == 0){
                        mActivity.tagmanager("Splash Screen 1","splash_screen_1_view");
                    }
                }
                @Override
                public void onPageScrollStateChanged(int state) {
                    if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                        scrollStarted = true;
                        checkDirection = true;
                    } else {
                        scrollStarted = false;
                    }

                }
            });

    }

    private void callSplashApi() {
        WebApiClient.getInstance(mActivity).getWebApi().callSplashTutorialApi().enqueue(new Callback<SplashTutorailResponse>() {
            @Override
            public void onResponse(Call<SplashTutorailResponse> call, Response<SplashTutorailResponse> response) {

                if(response!=null){
                    if(response.body()!=null){
                        if(response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)){
                            Delete.table(SplashTutorailResponse.class);
                           if(splaceListdata!=null && !splaceListdata.isEmpty()){
                               splaceListdata.clear();
                           }
                            final SplashTutorailResponse body = response.body();
                            if(body!=null){
                               body.save();
                           }
                            SplashTurorial[] data = response.body().getData();
                            String login_bg_image=response.body().getLogin_background_image();
                           if(login_bg_image!=null){
                               MyPreferences.setPrefClearLoginBg(mActivity,CommonKeys.LOGIN_BG_IMGAG);
                               MyPreferences.setPrefloginBg(mActivity,CommonKeys.LOGIN_BG_IMGAG,login_bg_image);
                           }
                            if(data!=null){
                                for (int i = 0; i <data.length ; i++) {
                                    splaceListdata.add(data[i]);
                                }
                                DynamicSliderAdapter dynamicSliderAdapter=new DynamicSliderAdapter(viewpager,mActivity,splaceListdata);
                                viewpager.setAdapter(dynamicSliderAdapter);
                                //circleIndicator.setViewPager(viewpager);
                                viewpager.setCurrentItem(0);
                                viewpager_scroll_postion =0;
                                handler=new Handler();
                                handler=new Handler();
                                handler.removeCallbacks(runnable);
                                handler.postDelayed(runnable,3000);
                            }

                        }else {

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SplashTutorailResponse> call, Throwable t) {
                common_methods.setExceptionMessage(t, mActivity);
            }
        });
    }
    int viewpager_scroll_postion =0;
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            viewpager_scroll_postion=viewpager_scroll_postion+1;
            if(viewpager_scroll_postion <3){
                viewpager.setCurrentItem(viewpager_scroll_postion);
            }else {
                mActivity.clearBackStack();
                if(handler!=null){
                    handler.removeCallbacks(runnable);
                }
                if(is_login_not_push) {
                    is_login_not_push = false;
                    mActivity.pushFragmentDontIgnoreCurrentWithAnimation(new LoginFragment(), FRAGMENT_JUST_ADD);
                }
            }
            System.out.println("viewpager"+ viewpager_scroll_postion);
        }
    };
    private void updateUI(final int fi) {
           /* handler.post(new Runnable() {
                public void run() {
                    gallery.setSelection(i);
                    viewPager.setCurrentItem(i);

                    //textView.setText("Photo #" + i);
                }
            });*/
        handler.postDelayed(new Runnable() {
            public void run() {


            }
        },3000);
    }

    private void initComponents(){
        mActivity.disableDrawer();
        mActivity.setToolbarTopVisibility(false);
        mActivity.setToolbarBottomVisibility(false);

       // mActivity.disableDrawer();
    }
    private int getItem(int i) {
        return viewpager.getCurrentItem() + i;
    }
    @OnClick()
    @Override
    public void onClick(View v) {

        switch (v.getId()){

           /* case R.id.tv_skip:
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                FragmentTransaction ft;
//                ft = fm.beginTransaction();
//                ft.replace(R.id.frame_container, new HomeFragment());
//                ft.commit();

                mActivity.pushFragmentIgnoreCurrent(new LoginFragment(), FRAGMENT_JUST_ADD);

                break;*/

          /*  case R.id.iv_next:
                int count=getItem(+1);
                if(count<3){
                    viewpager.setCurrentItem(count, true);
                }else {
                    mActivity.pushFragmentIgnoreCurrent(new LoginFragment(), FRAGMENT_JUST_ADD);

                }
                break;*/
           /* case R.id.signIn:
                mActivity.clearBackStack();
                mActivity.pushFragmentIgnoreCurrent(new LoginFragment(), FRAGMENT_JUST_ADD);

                break;*/
           /* case R.id.ic_Forward:
                mActivity.clearBackStack();
                mActivity.pushFragmentIgnoreCurrent(new LoginFragment(), FRAGMENT_JUST_ADD);

                break;*/

            default:
                break;

        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = new Fragment();

            switch (position){
                case 0:
                    splaceScreen1 = SplashScreen1.getInstance(splaceListdata.get(0).getImage());
                    f = splaceScreen1;
                    break;
                case 1:
                    splaceScreen2 = SplashScreen2.getInstance(splaceListdata.get(1).getImage());
                    f = splaceScreen2;
                    break;
                case 2:
                    splaceScreen3 = SplashScreen3.getInstance(splaceListdata.get(2).getImage());
                    f = splaceScreen3;
                    break;
               /* case 3:
                    splaceScreen4 = SplashScreen4.getInstance(splaceListdata.get(3).getImage());
                    f = splaceScreen4;*/

            }
            return  f;
        }

        @Override
        public int getCount() {
            return 3;
        }

        /*@Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

            if (position <= getCount()) {
                FragmentManager manager = ((Fragment) object).getFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove((Fragment) object);
                trans.commit();
            }
        }*/
    }

    public class DynamicSliderAdapter extends PagerAdapter {

        public Context context;
        private final LayoutInflater layoutInflater;
        List<SplashTurorial> splaceListdata;
        ViewPager viewPager;

        public DynamicSliderAdapter(ViewPager viewPager,Context context,  List<SplashTurorial> list_data) {
            layoutInflater = LayoutInflater.from(context);
            this.viewPager=viewPager;
            this.splaceListdata = list_data;
            this.context=context;
        }

        @Override
        public int getCount() {
            return splaceListdata.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View layout = (View) layoutInflater.inflate(R.layout.row_splash_screen, container, false);
         CircleIndicator circleIndicator=(CircleIndicator) layout.findViewById(R.id.circleIndicator) ;
            SimpleDraweeView image = (SimpleDraweeView) layout.findViewById(R.id.sv_spalce);
            TextView txtContent = (TextView) layout.findViewById(R.id.tv_detail);
            TextView txtTitle = (TextView) layout.findViewById(R.id.tv_title);

            Picasso.with(context).load(splaceListdata.get(position).getImage()).into(image);
            txtContent.setText(splaceListdata.get(position).getDescription());
           // txtTitle.setText(splaceListdata.get(position).getDescription());
            circleIndicator.setViewPager(viewPager);
            txtTitle.setTypeface(title_font);
            txtContent.setTypeface(title_font);
            container.addView(layout, 0);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
