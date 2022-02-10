package com.daffodils.psychiatry.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.adapter.VideosAdapter;
import com.daffodils.psychiatry.helper.ApiConfig;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.DbHelper;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.helper.Utils;
import com.daffodils.psychiatry.helper.VolleyCallback;
import com.daffodils.psychiatry.model.VideosGetterSetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
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
    String m_DeviceID;

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

        m_DeviceID = Settings.Secure.getString(getApplication().getContentResolver(),
                Settings.Secure.ANDROID_ID);


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
            params.put("EmailID", Email);
            params.put("Password", Password);

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (GlobalConst.Result.equals("T")) {

                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.length() == 0) {
                                    Toast.makeText(context, "Sorry, No records Found", Toast.LENGTH_LONG).show();
                                } else {
                                    GlobalConst.User_id = jsonObject.getString("UserID");
                                    GlobalConst.Name = jsonObject.getString("Name");
                                    GlobalConst.Mobile = jsonObject.getString("MobileNo");
                                    GlobalConst.Password = jsonObject.getString("Password");
                                    GlobalConst.Username = jsonObject.getString("EmailID");
                                    GlobalConst.ModuleID = jsonObject.getString("ModuleID");
                                    GlobalConst.DeviceID = jsonObject.getString("DeviceID");
                                    GlobalConst.isDeviceChanged = jsonObject.getString("IsDeviceChanged");

                                    if (GlobalConst.isDeviceChanged.equals("1")){
                                        updateDeviceIdService();
                                    } else {

                                        if (m_DeviceID.equals(GlobalConst.DeviceID)){
                                            dbHelper.openToWrite();
                                            dbHelper.insert_LoginTime_into_database(GlobalConst.Username, GlobalConst.Password, GlobalConst.Name, GlobalConst.Mobile, GlobalConst.User_id, GlobalConst.ModuleID);
                                            dbHelper.close();

                                            Toast.makeText(context, "Login Success", Toast.LENGTH_LONG).show();
                                            Intent ii = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(ii);
                                            finish();
                                        } else {
                                            Toast.makeText(context, "You are allowed to login for only one device. Kindly contact admin.", Toast.LENGTH_LONG).show();

                                        }


                                    }

                                }
                            } else {
                                Toast.makeText(context, GlobalConst.Description, Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim(), params, true);

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

                            if (GlobalConst.Result.equals("T")) {
                                String message = "Your password for the registered number is " + GlobalConst.GetPassword + ". Thank You !!";
                                String encoded_message = URLEncoder.encode(message);

                                String mainUrl = "http://mysms.msg24.in/api/mt/SendSMS?";

                                StringBuilder sbPostData = new StringBuilder(mainUrl);
                                sbPostData.append("user=" + "RowallaEnterprises");
                                sbPostData.append("&password=" + "123456");
                                sbPostData.append("&senderid=" + "RNITBP");
                                sbPostData.append("&channel=" + "Trans");
                                sbPostData.append("&DCS=" + "0");
                                sbPostData.append("&flashsms=" + "0");
                                sbPostData.append("&number=" + MobileNo);
                                sbPostData.append("&text=" + encoded_message);
                                sbPostData.append("&route=" + "08");

                                mainUrl = sbPostData.toString();

                                RequestQueue queue = Volley.newRequestQueue(context);
                                String url = mainUrl;

                                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Toast.makeText(context, "Password sent successfully.", Toast.LENGTH_LONG).show();

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
                                    }
                                });

                                queue.add(stringRequest);
                            } else {
                                Toast.makeText(context, GlobalConst.Description, Toast.LENGTH_LONG).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim(), params, true);

        }

    }

    public void updateDeviceIdService() {

        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();

            params.put("SC", GlobalConst.SC_UPDATE_DEVICEID);
            params.put("UserID", GlobalConst.User_id);
            params.put("DeviceID", m_DeviceID);

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (GlobalConst.Result.equals("T")) {

                                try {
                                    dbHelper.openToWrite();
                                    dbHelper.insert_LoginTime_into_database(GlobalConst.Username, GlobalConst.Password, GlobalConst.Name, GlobalConst.Mobile, GlobalConst.User_id, GlobalConst.ModuleID);
                                    dbHelper.close();

                                    Toast.makeText(context, "Login Success", Toast.LENGTH_LONG).show();
                                    Intent ii = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(ii);
                                    finish();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(context, "Error : " + GlobalConst.Description.toString() , Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim(), params, true);

        }
    }
}
