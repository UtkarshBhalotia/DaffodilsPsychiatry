package com.daffodils.psychiatry.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.helper.ApiConfig;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.DbHelper;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.helper.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends Activity {

    TextView txtRelease;
    Context context;
    Activity activity;
    DbHelper dBhelper = new DbHelper(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lyt_splashscreen);
        txtRelease = findViewById(R.id.txtRelease);
        context = getApplicationContext();
        activity = SplashScreen.this;

        txtRelease.setText("( Rel. " + GlobalConst.MAJOR_VER + "." + GlobalConst.MINOR_VER + " )");


        if (AppController.isConnected(activity)) {
            fetchVersionCode();
        } else {
            Toast.makeText(context, "Please check that whether you are connected to Internet or try again later.", Toast.LENGTH_LONG).show();
        }

    }

    public void fetchVersionCode() {

        if (AppController.isConnected(activity)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("SC", GlobalConst.SC_APP_COMPATIBILITY_VERSION);

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {
                            if (GlobalConst.Result.equals("T")){
                                if (GlobalConst.APP_COMPATABILITY_VERSION.equals(GlobalConst.AppVersion)) {
                                     fetchLoginData(context);
                                } else {
                                    Intent intent = new Intent(Intent.ACTION_VIEW ,Uri.parse(GlobalConst.PLAY_STORE_LINK + getPackageName()));
                                    startActivity(intent);
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim() , params, true);

        }
    }

    private void fetchLoginData(Context applicationContext) {

        try {
            dBhelper.openToWrite();

            Cursor cursor = dBhelper.fetch_Login_Data();
            if (cursor.getCount() > 0) {
                if (cursor.moveToLast()) {

                    GlobalConst.Username = cursor.getString(cursor.getColumnIndex(dBhelper.USERNAME));
                    GlobalConst.Password = cursor.getString(cursor.getColumnIndex(dBhelper.PASSWORD));
                    GlobalConst.Name = cursor.getString(cursor.getColumnIndex(dBhelper.NAME));
                    GlobalConst.Mobile = cursor.getString(cursor.getColumnIndex(dBhelper.MOBILE));
                    GlobalConst.User_id = cursor.getString(cursor.getColumnIndex(dBhelper.USER_ID));
                    GlobalConst.ModuleID = cursor.getString(cursor.getColumnIndex(dBhelper.MODULE_ID));

                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();

                } else {

                }
            } else {

                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();

            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, ""+ e, Toast.LENGTH_LONG).show();
        }
    }

}

