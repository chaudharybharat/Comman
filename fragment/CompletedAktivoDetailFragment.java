package com.aktivo.fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.table.Almost_over;
import com.aktivo.table.Competitors;
import com.aktivo.table.Ongoing;
import com.aktivo.table.Over;
import com.commonmodule.mi.utils.Validation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedAktivoDetailFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.recyclview)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_day)
    TextView tv_day;
    @BindView(R.id.tv_compltitors)
    TextView tv_compltitors;
    @BindView(R.id.tv_descraption)
    TextView tv_descraption;
    @BindView(R.id.tv_title_header)
    TextView tv_title_header;
    @BindView(R.id.tv_aktivo)
    TextView tv_aktivo;
    boolean flag = false;
    private CompetitorsAdaptor connectDeviceAdaptor;
    List<Competitors> competitorListData;
    public static final String COMMING_FROM = "coming_from";
    public static final String COMPETE_ID = "compete_id";

    public static CompletedAktivoDetailFragment getInstance(String comming_from, String compete_id) {
        CompletedAktivoDetailFragment completedAktivoDetailFragment = new CompletedAktivoDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(COMMING_FROM, comming_from);
        bundle.putString(COMPETE_ID, compete_id);
        completedAktivoDetailFragment.setArguments(bundle);
        return completedAktivoDetailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.completed_aktivo_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setHeader();
        initComponet();
        setFont();
        getBundleValue();
    }

    private void setFont() {
        tv_title.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_day.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_descraption.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_compltitors.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
    }

    private void getBundleValue() {

        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(COMMING_FROM)) {
                String user_coming_from = arguments.getString(COMMING_FROM);
                String comete_id = arguments.getString(COMPETE_ID);
                List<Competitors> competitorsList = new ArrayList<>();
                if (user_coming_from.equalsIgnoreCase(CommonKeys.COMMING_FROM_OVER)) {
                    if (Validation.isRequiredField(comete_id)) {
                        Over over = Over.getOverDetail(comete_id);
                        if (over != null) {
                            String destration = over.getDescription();
                            String title = over.getTitle();
                            String date_rage = over.getDayRange();
                            if (Validation.isRequiredField(destration)) {
                                tv_descraption.setText(destration);
                            }
                            if (Validation.isRequiredField(title)) {
                                tv_title.setText(title);
                            }
                            if (Validation.isRequiredField(date_rage)) {
                                tv_day.setText(date_rage);
                            }
                        }
                        competitorsList = Competitors.getCompetitorsList(comete_id);
                    }

                } else if (user_coming_from.equalsIgnoreCase(CommonKeys.COMMING_FROM_ONGOING)) {
                    if (Validation.isRequiredField(comete_id)) {
                        Ongoing ongoing = Ongoing.getOngoingDetail(comete_id);
                        if (ongoing != null) {
                            String destration = ongoing.getDescription();
                            String title = ongoing.getTitle();
                            String date_rage = ongoing.getDayRange();
                            if (Validation.isRequiredField(destration)) {
                                tv_descraption.setText(destration);
                            }
                            if (Validation.isRequiredField(title)) {
                                tv_title.setText(title);
                            }
                            if (Validation.isRequiredField(date_rage)) {
                                tv_day.setText(date_rage);
                            }
                        }
                        competitorsList = Competitors.getCompetitorsList(comete_id);
                    }
                } else if (user_coming_from.equalsIgnoreCase(CommonKeys.COMMING_FROM_ALMOST)) {

                    if (Validation.isRequiredField(comete_id)) {
                        Almost_over almostover = Almost_over.getAlmost_overDetail(comete_id);
                        if (almostover != null) {
                            String destration = almostover.getDescription();
                            String title = almostover.getTitle();
                            String date_rage = almostover.getDayRange();
                            if (Validation.isRequiredField(destration)) {
                                tv_descraption.setText(destration);
                            }
                            if (Validation.isRequiredField(title)) {
                                tv_title.setText(title);
                            }
                            if (Validation.isRequiredField(date_rage)) {
                                tv_day.setText(date_rage);
                            }
                        }
                        competitorsList = Competitors.getCompetitorsList(comete_id);
                    }
                }
                connectDeviceAdaptor.setDataUpdate(competitorsList);
            }
        }
    }

    private void initComponet() {

        //recycleview set
        competitorListData = new ArrayList<>();
        connectDeviceAdaptor = new CompetitorsAdaptor(competitorListData);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // recyclview_hobby.addItemDecoration(new DividerItemDecoration(mActivity.getResources().getDrawable(R.drawable.row_divider)));
        recyclerView.setAdapter(connectDeviceAdaptor);
        recyclerView.setNestedScrollingEnabled(false);
        mActivity.tagmanager("Compete Sub Screen","compete_sub_screen_view");
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (flag = false) {
                    mActivity.tagmanager("Compete Sub Screen", "compete_sub_screen_scroll");
                    flag = true;
                }
            }
        });
    }

    private void setHeader() {
        mActivity.setToolbarTopVisibility(false);
        mActivity.setToolbarBottomVisibility(true);
        tv_aktivo.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_title_header.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        mActivity.enableDrawer();
        if (Validation.isRequiredField(common_methods.getTodayHaveData().getCompete_background_image())) {
            mActivity.setBackgroudnImage(common_methods.getTodayHaveData().getCompete_background_image());
        }
    }

    @OnClick({R.id.iv_menu, R.id.iv_back})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_menu:
                mActivity.openDrawer();
                break;
            case R.id.iv_back:
                mActivity.onBackPressed();
            default:
                break;
        }

    }

    public class CompetitorsAdaptor extends RecyclerView.Adapter<CompetitorsAdaptor.MyViewHolder> {

        private List<Competitors> competitorList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_title)
            TextView tv_title;
            @BindView(R.id.tv_total_walk)
            TextView tv_total_walk;

            public MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);

            }
        }

        public CompetitorsAdaptor(List<Competitors> competitorList) {
            this.competitorList = competitorList;
        }

        public void setDataUpdate(List<Competitors> competitorList) {
            this.competitorList = competitorList;
            notifyDataSetChanged();
        }

        @Override
        public CompetitorsAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_cometitorwalk, parent, false);

            return new CompetitorsAdaptor.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CompetitorsAdaptor.MyViewHolder holder, final int pos) {
            final int position = pos;
            // final VehicalBrandList vehicalBrand = vehicalBrandLists.get(position);
            if (competitorList.get(position) != null) {
                holder.tv_total_walk.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                holder.tv_title.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
                if (Validation.isRequiredField(competitorList.get(position).getName())) {
                    holder.tv_title.setText(competitorList.get(position).getName());
                }
                if (Validation.isRequiredField(competitorList.get(position).getAchieve())) {
                    holder.tv_total_walk.setText(competitorList.get(position).getAchieve().trim());
                }
            }
        }

        @Override
        public int getItemCount() {
            return competitorList.size();
        }
    }

}
