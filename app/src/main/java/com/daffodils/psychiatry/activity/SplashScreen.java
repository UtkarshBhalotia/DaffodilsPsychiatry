package com.daffodils.psychiatry.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.google.android.exoplayer2.C;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashScreen extends Activity {

    TextView txtRelease;
    Context context;
    Activity activity;
    DbHelper dBhelper = new DbHelper(this);
    private static final int REQUEST_APP_SETTINGS = 1;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int PERMISSION_REQUEST_CODES = 201;

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

                                    if (Build.VERSION.SDK_INT > 22 && !hasPermissions(requiredPermissions)) {
                                        checkPermission();
                                    } else {
                                      //  fetchLoginData(context);
                                        getDurationDetails();
                                    }

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

    private void getDurationDetails(){
        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();
            params.put("SC", GlobalConst.SC_GET_COURSE_DURATION);

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (GlobalConst.Result.equals("T")) {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject;

                                for(int i =0; i<jsonArray.length();i++){

                                    jsonObject = jsonArray.getJSONObject(i);
                                    String CourseName = jsonObject.getString("CourseName");
                                    String CourseDuration = jsonObject.getString("CourseDuration");

                                    if (CourseName.equals("Full Foundation Course")){
                                        GlobalConst.FULL_COURSE_DURATION = CourseDuration;
                                    }else if (CourseName.equals("Crash Course")){
                                        GlobalConst.CRASH_COURSE_DURATION = CourseDuration;
                                    } else if (CourseName.equals("MRCPsych Course")){
                                        GlobalConst.MRCPSYCH_DURATION = CourseDuration;
                                    } else if (CourseName.equals("Combined Course")){
                                        GlobalConst.COMBINED_COURSE_DURATION = CourseDuration;
                                    } else {
                                        GlobalConst.ANY_MODULE_DURATION = "2 Months";
                                    }

                                }

                                fetchLoginData(context);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim() , params, true);

        }
    }

    private static final String[] requiredPermissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            /* ETC.. */
    };


    private void checkPermission() {

        final Dialog dialogdeactivate = new Dialog(SplashScreen.this);
        dialogdeactivate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogdeactivate.setContentView(R.layout.dialog_checkpermission);
        TextView heading = dialogdeactivate.findViewById(R.id.headingtext);
        TextView subheading = dialogdeactivate.findViewById(R.id.txtdelete);

        heading.setText("Permission Required!");
        heading.setTextColor(getResources().getColor(R.color.red));

        subheading.setText("Allow access to Phone Storage for seamless functionality of Daffodils App.(Settings >> Permissions >> Storage ) ");
        TextView yes = (TextView) dialogdeactivate.findViewById(R.id.ok);
        TextView no = (TextView) dialogdeactivate.findViewById(R.id.cancle);
        ImageView image = dialogdeactivate.findViewById(R.id.overlapImage);
        yes.setText("ALLOW");
        no.setText("DENY");
        image.setBackgroundDrawable(getResources().getDrawable(R.drawable.folder));

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogdeactivate.dismiss();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + context.getPackageName()));
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                finish();

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogdeactivate.dismiss();
                Intent i = new Intent(SplashScreen.this, SplashScreen.class);
                startActivity(i);
                finish();
            }
        });

        WindowManager.LayoutParams layoutparams = new WindowManager.LayoutParams();
        Window window = dialogdeactivate.getWindow();
        layoutparams.copyFrom(window.getAttributes());
        layoutparams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutparams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutparams);

        dialogdeactivate.getWindow().setBackgroundDrawable(
                new ColorDrawable(
                        android.graphics.Color.TRANSPARENT));
        dialogdeactivate.setCancelable(true);
        dialogdeactivate.show();

    }


    public boolean hasPermissions(@NonNull String... permissions) {
        for (String permission : permissions)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(permission))
                    return false;
            }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_APP_SETTINGS) {
            if (hasPermissions(requiredPermissions)) {
                Toast.makeText(this, "All permissions granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions not granted.", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

