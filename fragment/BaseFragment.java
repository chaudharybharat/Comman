package com.aktivo.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.aktivo.MainActivity;
import com.aktivo.Utils.Common_Methods;
import com.aktivo.Utils.Methods;
import com.google.firebase.analytics.FirebaseAnalytics;


public class BaseFragment extends Fragment {
    public FirebaseAnalytics mFirebaseAnalytics;

    public static MainActivity mActivity;
    public Context mContext;
    public Methods methods;
    public Common_Methods common_methods;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mActivity = (MainActivity) getActivity();
        this.methods = new Methods(context,mActivity);
        this.common_methods = new Common_Methods(mActivity);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

}
