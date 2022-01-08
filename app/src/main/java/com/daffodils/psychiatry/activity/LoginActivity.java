package com.daffodils.psychiatry.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.helper.ApiConfig;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.DbHelper;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.helper.Utils;
import com.daffodils.psychiatry.helper.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText edtEmail, edtPwd, edtmobileNo;
    Context context;
    Activity activity;
    LinearLayout llLogin, llRegister;
    Dialog dialogForgotPass;
    DbHelper dbHelper;
    TextView tvSignUp, tvForgotPass, txtOk, txtCancel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_login);

        llLogin = findViewById(R.id.lytlogin);
        llRegister = findViewById(R.id.signUpLyt);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPass = findViewById(R.id.tvForgotPass);

        btnLogin = findViewById(R.id.btnlogin);

        edtEmail = findViewById(R.id.edtLoginEmail);
        edtPwd = findViewById(R.id.edtloginpassword);

        context = getApplicationContext();
        activity = LoginActivity.this;
        dbHelper = new DbHelper(context);

        edtEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email, 0, 0, 0);
        edtPwd.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, R.drawable.ic_show, 0);

        Utils.setHideShowPassword(edtPwd);
        edtEmail.requestFocus();

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openForgotPassDialog();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginService();
            }
        });
    }

    private void openForgotPassDialog() {

        dialogForgotPass = new Dialog(LoginActivity.this);
        dialogForgotPass.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogForgotPass.setContentView(R.layout.forgot_pass_layout);

        edtmobileNo = dialogForgotPass.findViewById(R.id.edtMobileNo);
        txtOk = dialogForgotPass.findViewById(R.id.btnContinue);
        txtCancel = dialogForgotPass.findViewById(R.id.btnNo);

        edtmobileNo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone, 0, 0, 0);

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtOk.setEnabled(false);
                forgetPasswordService();
                // dialogForgotPass.dismiss();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForgotPass.dismiss();
            }
        });


        WindowManager.LayoutParams layoutparams = new WindowManager.LayoutParams();
        Window window = dialogForgotPass.getWindow();
        layoutparams.copyFrom(window.getAttributes());
        layoutparams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutparams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutparams);
        dialogForgotPass.setCancelable(true);
        dialogForgotPass.show();
    }

    public void loginService() {

        String Email = edtEmail.getText().toString();
        String Password = edtPwd.getText().toString();

        if (ApiConfig.CheckValidattion(Email, false, false)) {
            edtEmail.setError(getString(R.string.enter_email));
        } else if (ApiConfig.CheckValidattion(Email, true, false)) {
            edtEmail.setError(getString(R.string.enter_valid_email));
        } else if (ApiConfig.CheckValidattion(Password, false, false)) {
            edtPwd.setError(getString(R.string.enter_pass));

        } else if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();

            params.put("SC", GlobalConst.SC_LOGIN);
            params.put("UserName", Email);
            params.put("Password", Password);

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (response.contains("Table")){
                                JSONObject objectbject = new JSONObject(response);
                                JSONArray jsonArray = objectbject.getJSONArray("Table");

                                if (jsonArray.length() == 0){
                                    Toast.makeText(context, "Sorry, No records Found", Toast.LENGTH_LONG).show();
                                } else {

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject innerObj = jsonArray.getJSONObject(i);

                                        GlobalConst.Name = (innerObj.getString("Name"));
                                        GlobalConst.Username = (innerObj.getString("EmailID"));
                                        GlobalConst.Mobile = (innerObj.getString("MobileNo"));
                                        GlobalConst.Address = (innerObj.getString("Address"));
                                        GlobalConst.CompName = (innerObj.getString("CompanyName"));
                                        GlobalConst.Password = innerObj.getString("Password");
                                        GlobalConst.UserType = innerObj.getString("UserType");
                                        GlobalConst.User_id = innerObj.getString("UserID");
                                        GlobalConst.State_id = innerObj.getString("StateID");
                                        GlobalConst.City_id = innerObj.getString("CityID");

                                        if (GlobalConst.UserType.equals("A")) {
                                            GlobalConst.Role_id = innerObj.getInt("RoleID");
                                        } else {
                                            GlobalConst.Role_id = 0;
                                        }

                                        dbHelper.openToWrite();

                                        Cursor cursor = dbHelper.fetch_Login_Data_From_TABLE(GlobalConst.Username, GlobalConst.Password);
                                        if (cursor.getCount() > 0) {
                                            if (cursor.moveToLast()) {

                                                GlobalConst.Username = cursor.getString(cursor.getColumnIndex(dbHelper.USERNAME));
                                                GlobalConst.Password = cursor.getString(cursor.getColumnIndex(dbHelper.PASSWORD));
                                                GlobalConst.Name = cursor.getString(cursor.getColumnIndex(dbHelper.NAME));
                                                GlobalConst.Mobile = cursor.getString(cursor.getColumnIndex(dbHelper.MOBILE)); //i.e company mode or party mode
                                                GlobalConst.UserType = cursor.getString(cursor.getColumnIndex(dbHelper.USERTYPE));
                                                GlobalConst.User_id = cursor.getString(cursor.getColumnIndex(dbHelper.USER_ID));
                                                GlobalConst.CompName = cursor.getString(cursor.getColumnIndex(dbHelper.COMP_NAME));
                                                GlobalConst.City_id = cursor.getString(cursor.getColumnIndex(dbHelper.CITY_ID));
                                                GlobalConst.State_id = cursor.getString(cursor.getColumnIndex(dbHelper.STATE_ID));
                                                GlobalConst.Address = cursor.getString(cursor.getColumnIndex(dbHelper.ADDRESS));
                                                GlobalConst.Role_id = Integer.valueOf(cursor.getString(cursor.getColumnIndex(dbHelper.ROLE_ID)));

                                            } else {

                                            }

                                        } else {

                                            dbHelper.insert_LoginTime_into_database(GlobalConst.Username, GlobalConst.Password, GlobalConst.Name, GlobalConst.Mobile, GlobalConst.UserType, GlobalConst.User_id, GlobalConst.City_id, GlobalConst.State_id, GlobalConst.CompName, GlobalConst.Address, GlobalConst.Role_id.toString());
                                            dbHelper.close();

                                            Toast.makeText(context, "Login Success", Toast.LENGTH_LONG).show();
                                            Intent ii = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(ii);
                                            //fetchDetails();
                                        }

                                    }

                                }
                            } else {
                                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim() , params, true);

        }

    }

    public void forgetPasswordService() {

        dialogForgotPass.dismiss();

        String MobileNo = edtmobileNo.getText().toString();

        if (ApiConfig.CheckValidattion(MobileNo, false, false)) {
            edtmobileNo.setError(getString(R.string.enter_mobile_no));
        } else if (ApiConfig.CheckValidattion(MobileNo, false, true)) {
            edtmobileNo.setError(getString(R.string.enter_valid_mobile_no));

        } else if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();

            params.put("SC", GlobalConst.SC_FORGET_PASSWORD);
            params.put("MobileNo", MobileNo);

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (response.equals("1")){
                                Toast.makeText(context, "Password Sent Successfully.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim() , params, true);

        }

    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
