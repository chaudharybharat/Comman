package com.aktivo.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.Common_Methods;
import com.aktivo.table.Over;
import com.commonmodule.mi.utils.Validation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    OverAdaptor overAdaptor;
    List<Over> overlist;
    @BindView(R.id.tv_not_found)
    TextView tv_not_found;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    boolean flag = false;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Over> overList = Over.getOverList();
            if (overlist != null && !overlist.isEmpty()) {
                overlist.clear();
            }
            if (overList != null) {
                for (int i = 0; i < overList.size(); i++) {
                    overlist.add(overList.get(i));
                    overAdaptor.setDataUpdate(overList);
                    // Log.e("test","=>>"+overList.get(i));
                }
                if (overList.isEmpty()) {
                    tv_not_found.setVisibility(View.VISIBLE);
                } else {
                    tv_not_found.setVisibility(View.GONE);

                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.over_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Common_Methods.Reg_Loc_BroadCast(broadcastReceiver, mActivity, CommonKeys.UPADTE_LISTCOMPLETLIST_BR);
        tv_not_found.setText(mActivity.getResources().getString(R.string.not_found_compet));
        tv_not_found.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        setHeader();
        initComponet();
    }

    private void initComponet() {
        overlist = new ArrayList<>();
        List<Over> overList = Over.getOverList();
        if (overList != null) {
            for (int i = 0; i < overList.size(); i++) {
                overlist.add(overList.get(i));
                // Log.e("test","=>>"+overList.get(i));
            }
        }
        if (overlist != null && overList.size() > 0) {
            tv_not_found.setVisibility(View.GONE);
        } else {
            tv_not_found.setVisibility(View.VISIBLE);
        }
        overAdaptor = new OverAdaptor(overlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(overAdaptor);
        mActivity.tagmanager("Over Compete Screen","compete_over_view");
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (flag = false) {
                    mActivity.tagmanager("Over Compete Screen", "compete_over_scroll");
                    flag = true;
                }

            }
        });
    }

    private void setHeader() {
        if (Validation.isRequiredField(common_methods.getTodayHaveData().getCompete_background_image())) {
            mActivity.setBackgroudnImage(common_methods.getTodayHaveData().getCompete_background_image());
        }

        // mActivity.setTitleToolbar(CommonKeys.LOREMIPSUM);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setHeader();

        }
    }

        /*===============================OverAdaptor=======================================================*/

    public class OverAdaptor extends RecyclerView.Adapter<OverAdaptor.MyViewHolder> {

        private List<Over> overlist;

        public void setDataUpdate(List<Over> myRideModuleArrayList) {
            this.overlist = myRideModuleArrayList;
            notifyDataSetChanged();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_title)
            TextView tv_title;
            @BindView(R.id.tv_day)
            TextView tv_day;
            @BindView(R.id.tv_text)
            TextView tv_txt;


            public MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);

            }
        }


        public OverAdaptor(List<Over> overList) {
            if (overList != null && overList.size() > 0) {
                tv_not_found.setVisibility(View.GONE);
            } else {
                tv_not_found.setVisibility(View.VISIBLE);
            }
            this.overlist = overList;
        }

        @Override
        public OverAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_complete_aktivo, parent, false);

            return new OverAdaptor.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(OverAdaptor.MyViewHolder holder, final int position) {
            final Over overDetail = overlist.get(position);
            if (overDetail != null) {
                if (overDetail != null) {
                    holder.tv_title.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                    holder.tv_day.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                    holder.tv_txt.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
                    if (Validation.isRequiredField(overDetail.getTitle())) {
                        holder.tv_title.setText(overDetail.getTitle());
                    }
                    if (Validation.isRequiredField(overDetail.getDayRange())) {
                        holder.tv_day.setText(overDetail.getDayRange());
                    }
                    if (Validation.isRequiredField(overDetail.getDescription())) {
                        holder.tv_txt.setText(overDetail.getDescription());
                    }
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mActivity.pushFragmentDontIgnoreCurrent(CompletedAktivoDetailFragment.getInstance(CommonKeys.COMMING_FROM_OVER, overlist.get(position).get_id()), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                        }
                    });


                }
            }
        }

        @Override
        public int getItemCount() {
            return overlist.size();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Common_Methods.UnReg_Loc_BroadCast(broadcastReceiver, mActivity);
    }
}
