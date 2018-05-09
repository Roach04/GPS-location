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
public class PrologueFragment extends Fragment {

    public PrologueFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prologue, container, false);
    }
}
