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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.Common_Methods;
import com.aktivo.table.Almost_over;
import com.aktivo.table.Ongoing;
import com.commonmodule.mi.utils.Validation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnGoingFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    OnGogingAdaptor onGogingAdaptor;
    List<Ongoing> onGogingList;
    @BindView(R.id.tv_not_found)
    TextView tv_not_found;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    boolean flag = false;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Ongoing> ongoingsList = Ongoing.getOngoingList();
            if (onGogingList != null && !onGogingList.isEmpty()) {
                onGogingList.clear();
            }
            if (ongoingsList != null) {
                for (int i = 0; i < ongoingsList.size(); i++) {
                    onGogingList.add(ongoingsList.get(i));
                    onGogingAdaptor.setDataUpdate(ongoingsList);
                    // Log.e("test","=>>"+overList.get(i));
                }
            }
            if (onGogingList.size() == 0) {
                tv_not_found.setVisibility(View.VISIBLE);
            } else {
                tv_not_found.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_going, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        tv_not_found.setText(mActivity.getResources().getString(R.string.not_found_compet));
        tv_not_found.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        Common_Methods.Reg_Loc_BroadCast(broadcastReceiver, mActivity, CommonKeys.UPADTE_LISTCOMPLETLIST_BR);
        setHeader();
        initComponet();
        mActivity.tagmanager("Compete Screen", "compete_ongoing_view");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setHeader();

        }
    }

    private void initComponet() {
        onGogingList = new ArrayList<>();

        List<Ongoing> ongoingList = Ongoing.getOngoingList();
        if (ongoingList != null) {
            for (int i = 0; i < ongoingList.size(); i++) {
                onGogingList.add(ongoingList.get(i));
                // Log.e("test","=>>"+overList.get(i));
            }

        }
        if (onGogingList != null && onGogingList.size() > 0) {
            tv_not_found.setVisibility(View.GONE);
        } else {
            tv_not_found.setVisibility(View.VISIBLE);
        }
        onGogingAdaptor = new OnGogingAdaptor(onGogingList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(onGogingAdaptor);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (flag = false) {
                    mActivity.tagmanager("Ongoing Compete Screen", "compete_ongoing_scroll");
                    flag = true;
                }
            }
        });


    }

    private void setHeader() {
        if (Validation.isRequiredField(common_methods.getTodayHaveData().getCompete_background_image())) {
            mActivity.setBackgroudnImage(common_methods.getTodayHaveData().getCompete_background_image());
        }

        //mActivity.setTitleToolbar(CommonKeys.COMPLETED);
    }

      /*===============================OngoingAdaptor=======================================================*/

    public class OnGogingAdaptor extends RecyclerView.Adapter<OnGogingAdaptor.MyViewHolder> {

        private List<Ongoing> ongoingList;

        public void setDataUpdate(List<Ongoing> OngoingList) {
            this.ongoingList = OngoingList;
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


        public OnGogingAdaptor(List<Ongoing> OngoinglistData) {
            this.ongoingList = OngoinglistData;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_complete_aktivo, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Ongoing ongoingDetail = ongoingList.get(position);
            if (ongoingDetail != null) {
                if (ongoingDetail != null) {
                    holder.tv_title.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                    holder.tv_day.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                    holder.tv_txt.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
                    if (Validation.isRequiredField(ongoingDetail.getTitle())) {
                        holder.tv_title.setText(ongoingDetail.getTitle());
                    }
                    if (Validation.isRequiredField(ongoingDetail.getDayRange())) {
                        holder.tv_day.setText(ongoingDetail.getDayRange());
                    }
                    if (Validation.isRequiredField(ongoingDetail.getDescription())) {
                        holder.tv_txt.setText(ongoingDetail.getDescription());
                    }
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mActivity.pushFragmentDontIgnoreCurrent(CompletedAktivoDetailFragment.getInstance(CommonKeys.COMMING_FROM_ONGOING, ongoingList.get(position).get_id()), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);

                        }
                    });


                }
            }
        }

        @Override
        public int getItemCount() {
            return ongoingList.size();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Common_Methods.UnReg_Loc_BroadCast(broadcastReceiver, mActivity);
    }
}
