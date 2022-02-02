package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.helper.GlobalConst;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.daffodils.psychiatry.R.layout.lyt_faculty;

public class FacultyFragment extends Fragment {

    View root;
    Activity activity;
    LinearLayout llDrShivali, llDrMohit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(lyt_faculty, container, false);
        llDrShivali = root.findViewById(R.id.llDrShivali);
        llDrMohit = root.findViewById(R.id.llDrMohit);

        activity = getActivity();
        setHasOptionsMenu(true);

        llDrShivali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://scholar.google.com/citations?user=os6JXB8AAAAJ&hl=en"));
                startActivity(browserIntent);
            }
        });

        llDrMohit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://scholar.google.co.in/citations?user=I9lyD3wAAAAJ&hl=en"));
                startActivity(browserIntent);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalConst.TOOLBAR_TITLE = getString(R.string.title_faculty);
        getActivity().invalidateOptionsMenu();
        hideKeyboard();
    }

    public void hideKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(root.getApplicationWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
