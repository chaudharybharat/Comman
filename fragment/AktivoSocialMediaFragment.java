package com.aktivo.fragment;


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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aktivo.Model.ConnectDevice;
import com.aktivo.Model.SocialMedail;
import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.commonmodule.mi.utils.Validation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AktivoSocialMediaFragment extends BaseFragment {

    @BindView(R.id.recyclview)
    RecyclerView recyclerView;
    private ConnectDeviceAdaptor connectDeviceAdaptor;
    List<SocialMedail> scialMedialList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.aktivo_social_media_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        setHeader();
        initComponet();
    }

    private void initComponet() {

        //recycleview set
        scialMedialList=new ArrayList<>();
        scialMedialList= SocialMedail.getListVlaue();
        connectDeviceAdaptor=new ConnectDeviceAdaptor(scialMedialList);
        RecyclerView.LayoutManager  layoutManager=new LinearLayoutManager(mActivity, LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // recyclview_hobby.addItemDecoration(new DividerItemDecoration(mActivity.getResources().getDrawable(R.drawable.row_divider)));
        recyclerView.setAdapter(connectDeviceAdaptor);
        recyclerView.setNestedScrollingEnabled(false);

    }
    private void setHeader() {
        mActivity.setToolbarTopVisibility(true);
        mActivity.setToolbarBottomVisibility(true);
        mActivity.setTitleToolbar(CommonKeys.SCOIAL_MEDIA);
        mActivity.setBackgroudnImage(common_methods.getBackgroundImage(mActivity,CommonKeys.BACKGROUND_MAIN));

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            setHeader();
        }
    }

    public class ConnectDeviceAdaptor extends RecyclerView.Adapter<ConnectDeviceAdaptor.MyViewHolder> {

        private List<SocialMedail> scoialMedaiList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_social_media_name)
            TextView tv_social_media_name;
            @BindView(R.id.tv_sub_name)
            TextView tv_sub_name;
            @BindView(R.id.rl_main)
            RelativeLayout rl_main;

            public MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);

            }
        }
        public ConnectDeviceAdaptor(List<SocialMedail> socialList) {
            this.scoialMedaiList = socialList;
        }
        public void setDataUpdate(List<SocialMedail> socialList) {
            this.scoialMedaiList = socialList;
            notifyDataSetChanged();
        }
        @Override
        public ConnectDeviceAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_social_media, parent, false);

            return new ConnectDeviceAdaptor.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ConnectDeviceAdaptor.MyViewHolder holder, final int pos) {
            final int position=pos;
            // final VehicalBrandList vehicalBrand = vehicalBrandLists.get(position);
            if (scoialMedaiList.get(position)!= null) {
                if (Validation.isRequiredField(scoialMedaiList.get(position).getSocial_media())) {
                    holder.tv_social_media_name.setText(scoialMedaiList.get(position).getSocial_media());
                }
                 if (Validation.isRequiredField(scoialMedaiList.get(position).getSub_name())) {
                    holder.tv_sub_name.setText(scoialMedaiList.get(position).getSub_name());
                }
                if(scoialMedaiList.get(position).is_connceted()){
                    holder.rl_main.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
                   holder.tv_social_media_name.setTextColor(mActivity.getResources().getColor(R.color.black));
                   holder.tv_sub_name.setTextColor(mActivity.getResources().getColor(R.color.black_aa));
                }else {
                    holder.rl_main.setBackgroundColor(mActivity.getResources().getColor(R.color.transparent_white));
                    holder.tv_social_media_name.setTextColor(mActivity.getResources().getColor(R.color.black_aa));
                    holder.tv_sub_name.setTextColor(mActivity.getResources().getColor(R.color.black_aa));
                }
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               /*     if(scoialMedaiList.get(position).isIs_selected()){
                        notifayTextColor(false,position);
                    }else {
                        notifayTextColor(true,position);
                    }
*/
                }
            });

        }
        private void notifayTextColor(boolean is_value,int pos) {

            for (int i = 0; i < scoialMedaiList.size() ; i++) {

                if(i==pos){
                    scoialMedaiList.get(i).setIs_connceted(is_value);
                    break;
                }

            }
            notifyDataSetChanged();
        }


        @Override
        public int getItemCount() {
            return scoialMedaiList.size();
        }
    }

}
