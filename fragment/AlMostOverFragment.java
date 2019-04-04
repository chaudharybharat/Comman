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
import com.aktivo.table.Over;
import com.commonmodule.mi.utils.Validation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlMostOverFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    AlmostOvetAdaptor alMostOverAdaptor;
    List<Almost_over> almostoverList;
    @BindView(R.id.tv_not_found)
    TextView tv_not_found;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    boolean flag = false;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Almost_over> allmostList = Almost_over.getAlmostOverList();
            if (almostoverList != null && !almostoverList.isEmpty()) {
                almostoverList.clear();
            }
            if (allmostList != null) {
                for (int i = 0; i < allmostList.size(); i++) {
                    almostoverList.add(allmostList.get(i));
                    alMostOverAdaptor.setDataUpdate(allmostList);
                    // Log.e("test","=>>"+overList.get(i));
                }
                if (almostoverList.isEmpty()) {
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
        return inflater.inflate(R.layout.al_most_over_fragment, container, false);
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
        mActivity.tagmanager("Almost Over Compete Screen", "compete_almost_over_view");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setHeader();

        }
    }

    private void initComponet() {
        almostoverList = new ArrayList<>();
        List<Almost_over> almostOverListData = Almost_over.getAlmostOverList();

        if (almostOverListData != null) {
            for (int i = 0; i < almostOverListData.size(); i++) {
                almostoverList.add(almostOverListData.get(i));
                // Log.e("test","=>>"+overList.get(i));
            }

        }
        if (almostoverList != null && almostoverList.size() > 0) {
            tv_not_found.setVisibility(View.GONE);
        } else {
            tv_not_found.setVisibility(View.VISIBLE);
        }
        alMostOverAdaptor = new AlmostOvetAdaptor(almostoverList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(alMostOverAdaptor);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (flag = false) {
                    mActivity.tagmanager("Almost Over Compete Screen", "compete_almost_over_scroll");
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
       /*===============================OngoingAdaptor=======================================================*/

    public class AlmostOvetAdaptor extends RecyclerView.Adapter<AlmostOvetAdaptor.MyViewHolder> {

        private List<Almost_over> almostOverList;

        public void setDataUpdate(List<Almost_over> Almost_overList) {
            this.almostOverList = Almost_overList;
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


        public AlmostOvetAdaptor(List<Almost_over> almost_overlist) {
            this.almostOverList = almost_overlist;
        }

        @Override
        public AlmostOvetAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_complete_aktivo, parent, false);

            return new AlmostOvetAdaptor.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(AlmostOvetAdaptor.MyViewHolder holder, final int position) {
            final Almost_over almost_over = almostOverList.get(position);
            if (almost_over != null) {
                holder.tv_title.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                holder.tv_day.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                holder.tv_txt.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
                if (Validation.isRequiredField(almost_over.getTitle())) {
                    holder.tv_title.setText(almost_over.getTitle());
                }
                if (Validation.isRequiredField(almost_over.getDayRange())) {
                    holder.tv_day.setText(almost_over.getDayRange());
                }
                if (Validation.isRequiredField(almost_over.getDescription())) {
                    holder.tv_txt.setText(almost_over.getDescription());
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.pushFragmentDontIgnoreCurrent(CompletedAktivoDetailFragment.getInstance(CommonKeys.COMMING_FROM_ALMOST, almostOverList.get(position).get_id()), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                    }
                });


            }
        }

        @Override
        public int getItemCount() {
            return almostOverList.size();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Common_Methods.UnReg_Loc_BroadCast(broadcastReceiver, mActivity);
    }
}
