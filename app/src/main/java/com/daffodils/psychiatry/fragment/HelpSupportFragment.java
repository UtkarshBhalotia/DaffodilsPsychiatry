package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.activity.MainActivity;
import com.daffodils.psychiatry.helper.ApiConfig;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.helper.VolleyCallback;
import com.daffodils.psychiatry.ui.CircleImageView;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class HelpSupportFragment extends Fragment {

    View root;
    Activity activity;
    EditText edtFrom, edtSubject, edtShortDesc, edtTo;
    Button btnSend;
    Context context;
    CircleImageView imgCall, imgEmail, imgVisit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.lyt_helpsupport, container, false);
        activity = getActivity();
        setHasOptionsMenu(true);

        edtFrom = root.findViewById(R.id.edtFrom);
        edtTo = root.findViewById(R.id.edtTo);
        edtSubject = root.findViewById(R.id.edtSubject);
        edtShortDesc = root.findViewById(R.id.edtShortDesc);
        btnSend = root.findViewById(R.id.btnSend);
        edtFrom.setText(GlobalConst.Username);

        context = getContext();

        edtFrom.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email, 0, 0, 0);
        edtTo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone, 0, 0, 0);
        edtSubject.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_text, 0, 0, 0);
        edtShortDesc.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_text, 0, 0, 0);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpSupportService();
            }
        });

        return root;

    }

    public void helpSupportService(){

        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();

            params.put("SC", GlobalConst.SC_HELP_SUPPORT);
            params.put("Subject", edtSubject.getText().toString());
            params.put("Message", edtShortDesc.getText().toString());
            params.put("UserID", GlobalConst.User_id);

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (response.equals("1")){
                                setSnackBar("Your request has been added.", "OK");
                                //Toast.makeText(context, "Your request has been added.", Toast.LENGTH_LONG).show();
                            } else {
                                setSnackBar("Error in processing request.", "OK");
                                //Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim() , params, true);

        }
    }

    public void setSnackBar(String message, String action) {
        final Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(Color.RED);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalConst.TOOLBAR_TITLE = getString(R.string.title_help);
        activity.invalidateOptionsMenu();
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
