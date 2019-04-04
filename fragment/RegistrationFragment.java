package com.aktivo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.aktivo.R;
import com.commonmodule.mi.utils.SpinnerCustomized;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends BaseFragment {

    ArrayAdapter<String> spinnerPhoneCodeAdaptor;
    @BindView(R.id.spiner_mostly)
    SpinnerCustomized spiner_mostly;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.registration_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        setHeader();
        intiComponet();
    }
    private void setHeader() {
        mActivity.setToolbarTopVisibility(false);
    }

    private void intiComponet() {

        final List<String> spinner_code = new ArrayList<String>();
        /// spinner_list_all.add("All");
        spinner_code.add("81");
        spinner_code.add("91");
        spinner_code.add("92");
        spinnerPhoneCodeAdaptor = new ArrayAdapter<String>(mActivity,R.layout.custom_spinner, spinner_code);
        spiner_mostly.setAdapter(spinnerPhoneCodeAdaptor);
        /*sppiner_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edt_code.setText(spinner_code.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }

}
