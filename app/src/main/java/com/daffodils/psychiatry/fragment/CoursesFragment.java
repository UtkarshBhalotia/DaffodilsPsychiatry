package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.activity.MainActivity;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.ui.CircleImageView;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.daffodils.psychiatry.R.layout.lyt_courses;

public class CoursesFragment extends Fragment {

    LinearLayout llFoundation, llCrash, llMRCPsych, llDiscount;
    CircleImageView imgFoundation, imgCrash, imgMRCPsych, imgDiscount;
    View root;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(lyt_courses, container, false);
        activity = getActivity();
        setHasOptionsMenu(true);
        llFoundation = root.findViewById(R.id.llFoundation2021);
        llCrash = root.findViewById(R.id.llCrash2022);
        llMRCPsych = root.findViewById(R.id.llMRCPsych);
        llDiscount = root.findViewById(R.id.llDiscount);

        imgFoundation = root.findViewById(R.id.imgFound2021);
        imgCrash = root.findViewById(R.id.imgCrash2022);
        imgMRCPsych = root.findViewById(R.id.imgMRCPsych);
        imgDiscount = root.findViewById(R.id.imgdiscount);

        imgFoundation.setDefaultImageResId(R.drawable.foundation_course);
        imgCrash.setDefaultImageResId(R.drawable.crash_course);
        imgMRCPsych.setDefaultImageResId(R.drawable.mrc_psych);
        imgDiscount.setDefaultImageResId(R.drawable.discount);

        llFoundation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new FoundationCourseFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "Foundation");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

            }
        });

        llCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new CrashCourseFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "Crash");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

            }
        });

        llMRCPsych.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new MRCPsychFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "MRCPsych ");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

            }
        });

        llDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new OngoingDiscountFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "Discount");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalConst.TOOLBAR_TITLE = getString(R.string.title_courses);
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
