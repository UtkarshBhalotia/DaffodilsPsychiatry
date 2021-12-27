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
import com.daffodils.psychiatry.helper.DbHelper;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.helper.VolleyCallback;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ChangePassword extends Fragment {

    View root;
    Activity activity;
    EditText edtOldPass, edtNewPass, edtRecheckPass;
    Button btnChangePass;
    Context context;
    DbHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.lyt_change_password, container, false);
        activity = getActivity();
        context = getContext();
        dbHelper = new DbHelper(context);

        edtOldPass = root.findViewById(R.id.edtoldpsw);
        edtNewPass = root.findViewById(R.id.edtnewpsw);
        edtRecheckPass = root.findViewById(R.id.edtRecheckPass);
        btnChangePass = root.findViewById(R.id.btnchangepsw);

        edtOldPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, 0, 0);
        edtNewPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, 0, 0);
        edtRecheckPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, 0, 0);

        setHasOptionsMenu(true);

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordService();
            }
        });

        return root;

    }

    public void changePasswordService() {

        String OldPassword = edtOldPass.getText().toString();
        String NewPassword = edtNewPass.getText().toString();
        String RecheckPassword = edtRecheckPass.getText().toString();

        if (ApiConfig.CheckValidattion(OldPassword, false, false)) {
            edtOldPass.setError(getString(R.string.enter_old_pass));
        } else if (ApiConfig.CheckValidattion(NewPassword, false, false)) {
            edtNewPass.setError(getString(R.string.enter_new_pass));
        } else if (!NewPassword.equals(RecheckPassword)) {
            edtRecheckPass.setError(getString(R.string.pass_not_match));

        } else if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();

            params.put("SC", GlobalConst.SC_CUSTOMER_CHANGE_PWD);
            params.put("OldPassword", OldPassword);
            params.put("NewPassword", NewPassword);
            params.put("UserID", GlobalConst.User_id);

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (response.equals("1")) {

                                GlobalConst.Password = edtNewPass.getText().toString();
                                try {
                                    dbHelper.openToWrite();
                                    dbHelper.update_into_LOGIN_Table(GlobalConst.Username, GlobalConst.Password, GlobalConst.Name, GlobalConst.Mobile, GlobalConst.UserType, GlobalConst.User_id, GlobalConst.City_id, GlobalConst.State_id, GlobalConst.CompName, GlobalConst.Address, GlobalConst.Role_id.toString());
                                    dbHelper.close();

                                    setSnackBar("Password Chnaged Successfully", "OK");

                                    //  Toast.makeText(context, "Password changed successfully.", Toast.LENGTH_LONG).show();
                                    //success_popup();
                                    // Toast.makeText(getApplicationContext(), "Password Changed Successfully.", Toast.LENGTH_SHORT).show();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                setSnackBar("Error", "OK");
                                //  Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim(), params, true);

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
        GlobalConst.TOOLBAR_TITLE = getString(R.string.title_change_pass);
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
