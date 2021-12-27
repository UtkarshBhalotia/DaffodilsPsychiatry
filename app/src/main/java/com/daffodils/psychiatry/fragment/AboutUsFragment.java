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
                intent.setData(Uri.parse("tel:" + "18001038583"));
                startActivity(intent);
            }
        });

        www.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.serviceondoors.com/"));
                startActivity(browserIntent);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ServiceOnDoors"));
                startActivity(browserIntent);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] emails_in_to = {"info@serviceondoors.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, emails_in_to);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Enquiry from Customer App");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
            }
        });
        txt1.setText("ServiceOnDoors.Com is one of the leading AC and RO service provider company in India. The reason why we have been able to recognize ourselves in such a positive influence among customers is a remarkable quality of service. All of our staff is 100% certified professional. Due to this, we leave a positive outlook on our clients regarding our behavior and services which makes us noteworthy. We aim to provide a top RO service and AC service to all our customers in a short amount of time.\n" +
                "\n" +
                "To reach us call on Call @ 1800-103-8583; each and every person can call on this phone no matter where you are in the country.");

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
