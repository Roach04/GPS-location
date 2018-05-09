package com.project.frag;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.add.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class TrialFragment extends Fragment {

    public TrialFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trial, container, false);
    }
}
