package com.aktivo.fragment.splacescreen;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aktivo.R;
import com.aktivo.fragment.BaseFragment;
import com.commonmodule.mi.utils.Validation;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashScreen4 extends BaseFragment{


    @BindView(R.id.sv_spalce)
    SimpleDraweeView sv_spalce;
    public static String KEY_IMAG="key";
    public static SplashScreen4 getInstance(String imge) {
        SplashScreen4 splaceScreen1 = new SplashScreen4();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_IMAG, imge);
        splaceScreen1.setArguments(bundle);
        return splaceScreen1;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splace_screen4, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        getBuandleData();
    }
    private void getBuandleData() {
        Bundle arguments = getArguments();
        if(arguments!=null){
            if(arguments.containsKey(KEY_IMAG)){
                String image = arguments.getString(KEY_IMAG);
                if(Validation.isRequiredField(image)){
                    sv_spalce.setImageURI(image);
                }
            }
        }
    }
}
