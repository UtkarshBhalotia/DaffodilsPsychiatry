package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.daffodils.psychiatry.R;

public class FeeStructureFragment extends Fragment {

    View root;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.lyt_pricing, container, false);
        activity = getActivity();
        setHasOptionsMenu(true);

        return root;

    }
}
