package org.smartregister.family.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.family.R;
import org.smartregister.view.fragment.BaseProfileFragment;

/**
 * Created by ndegwamartin on 12/07/2018.
 */
public abstract class BaseFamilyProfileDueFragment extends BaseProfileFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onCreation() {
        //Overriden
    }

    @Override
    protected void onResumption() {
        //Overriden
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_profile_due, container, false);
        return fragmentView;
    }
}