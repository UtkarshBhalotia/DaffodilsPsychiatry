package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.activity.MainActivity;
import com.daffodils.psychiatry.helper.GlobalConst;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.daffodils.psychiatry.R.layout.lyt_ongoing_discount;

public class OngoingDiscountFragment extends Fragment {

    TextView txtFFC, txtCombo;
    View root;
    Activity activity;
    LinearLayout llFullFound, llCrashCourse, llComboDisc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(lyt_ongoing_discount, container, false);
        activity = getActivity();
        setHasOptionsMenu(true);

        txtFFC = root.findViewById(R.id.txtFFC);
        txtCombo = root.findViewById(R.id.txtCombo);

        llFullFound = root.findViewById(R.id.llFullFoundCourse);
        llComboDisc = root.findViewById(R.id.llComboDisc);
        llCrashCourse = root.findViewById(R.id.llCrashCourse);

        txtFFC.setPaintFlags(txtFFC.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtCombo.setPaintFlags(txtCombo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        llFullFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FoundationCourseFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "FoundationCourse");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
            }
        });

        llComboDisc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FoundationCourseFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "FoundationCourse");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
            }
        });

        llCrashCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CrashCourseFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "CrashCourse");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalConst.TOOLBAR_TITLE = getString(R.string.title_discount);
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
