package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.helper.GlobalConst;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AboutUsFragment extends Fragment {

    View root;
    Activity activity;
    TextView txt1, fb, email, call, www;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.lyt_aboutus, container, false);
        activity = getActivity();
        setHasOptionsMenu(true);
        txt1 = root.findViewById(R.id.txt1);

        call = root.findViewById(R.id.callus);
        email = root.findViewById(R.id.email);
        fb = root.findViewById(R.id.app);
        www = root.findViewById(R.id.visitus);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "+91-8512812518"));
                startActivity(intent);
            }
        });

        www.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://daffodilspsychiatry.com/"));
                startActivity(browserIntent);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.facebook.com/PsychMasterClasses"));
                startActivity(browserIntent);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] emails_in_to = {"daffodils.psych@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, emails_in_to);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Enquiry from Daffodils Psychiatry App");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
            }
        });
        txt1.setText("Daffodils Psych learning master classes is an initiative by DAFFODILS BRAIN CARE CENTER to provide world class education in field of Psychiatry & Psychology. We aim to provide extra edge to our students with great learning opportunities and knowledge about theoritical and practical aspects of the subject by providing evidence based learning platform.");

        return root;

    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalConst.TOOLBAR_TITLE = getString(R.string.title_about);
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
