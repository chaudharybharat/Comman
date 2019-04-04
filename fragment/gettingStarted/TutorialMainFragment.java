package com.aktivo.fragment.gettingStarted;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.MyPreferences;
import com.aktivo.fragment.BaseFragment;
import com.aktivo.fragment.HomeFragment;
import com.aktivo.fragment.LoginFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

import static com.commonmodule.mi.Activity.MIActivity.FRAGMENT_JUST_ADD;
import static com.commonmodule.mi.Activity.MIActivity.FRAGMENT_JUST_REPLACE;

/**
 * Created by mind on 28/11/16.
 */

public class TutorialMainFragment extends BaseFragment implements View.OnClickListener {

    TutorialFragment1 introFragment1;
    TutorialFragment2 introFragment2;
    TutorialFragment3 introFragment3;
    LoginFragment homeFragment;

    @BindView(R.id.circleIndicator)
    CircleIndicator circleIndicator;

    @BindView(R.id.ll_dont_show)
    LinearLayout ll_dont_show;


    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.tv_skip)
    TextView tv_skip;


    @BindView(R.id.iv_next)
    ImageView iv_next;
    @BindView(R.id.check_tutorial_screen)
    CheckBox check_tutorial_screen;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorial_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initComponents();
       // mActivity.setToolbarVisibility(false);
        final FragmentPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

       /* if(Utility.getStringSharedPreferences(mContext, StringDifferentLanguage.SKIP).isEmpty()
                || Utility.getStringSharedPreferences(mContext,StringDifferentLanguage.SKIP).equalsIgnoreCase(""))
        {
            tv_skip.setText("Skip");
        }
        else
        {
            tv_skip.setText(Utility.getStringSharedPreferences(mContext,StringDifferentLanguage.SKIP));

        }*/

        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);
        circleIndicator.setViewPager(viewpager);

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                private static final float thresholdOffset = 0.5f;
                private boolean scrollStarted, checkDirection;

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                    int lastIndex=adapter.getCount()-1;

                    int currentPage =viewpager.getCurrentItem();

                    if(currentPage==2){
                        ll_dont_show.setVisibility(View.VISIBLE);
                    }else {
                        ll_dont_show.setVisibility(View.GONE);

                    }

                    if (checkDirection) {
                        if (thresholdOffset > positionOffset && lastIndex==currentPage ) {
                            mActivity.clearBackStack();
                            mActivity.pushFragmentIgnoreCurrent(new HomeFragment(), FRAGMENT_JUST_REPLACE);
                            Log.i("Left", "going left");
                        } else {

                            Log.i("Right", "going right");
                        }
                        checkDirection = false;
                    }
                }

                @Override
                public void onPageSelected(int position) {

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

    private void initComponents(){
        mActivity.disableDrawer();
        tv_skip.setOnClickListener(this);
        check_tutorial_screen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    MyPreferences.setPref(mActivity,CommonKeys.TURORIAL_SCREEN_SHOW_AGAIN,CommonKeys.FALSE);
                }else {
                    MyPreferences.setPref(mActivity,CommonKeys.TURORIAL_SCREEN_SHOW_AGAIN,CommonKeys.TRUE);

                }
            }
        });
       // mActivity.disableDrawer();
    }
    private int getItem(int i) {
        return viewpager.getCurrentItem() + i;
    }
    @OnClick({R.id.iv_next})
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.tv_skip:
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                FragmentTransaction ft;
//                ft = fm.beginTransaction();
//                ft.replace(R.id.frame_container, new HomeFragment());
//                ft.commit();

                mActivity.clearBackStack();
                mActivity.pushFragmentIgnoreCurrent(new HomeFragment(), FRAGMENT_JUST_REPLACE);

                break;

            case R.id.iv_next:
                int count=getItem(+1);
                if(count<3){
                    viewpager.setCurrentItem(count, true);
                }else {
                    mActivity.clearBackStack();
                    mActivity.pushFragmentIgnoreCurrent(new HomeFragment(), FRAGMENT_JUST_REPLACE);

                }
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

                    introFragment1 = new TutorialFragment1();
                    f = introFragment1;
                    break;
                case 1:
                    introFragment2 = new TutorialFragment2();
                    f = introFragment2;
                    break;
                case 2:
                    introFragment3 = new TutorialFragment3();
                    f = introFragment3;
                    break;
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
}
