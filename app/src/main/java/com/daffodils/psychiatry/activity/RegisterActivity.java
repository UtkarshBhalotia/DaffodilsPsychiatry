 package com.daffodils.psychiatry.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.adapter.SearchAdapter;
import com.daffodils.psychiatry.adapter.SearchAdapter1;
import com.daffodils.psychiatry.adapter.VideosAdapter;
import com.daffodils.psychiatry.helper.ApiConfig;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.helper.MyFirebaseMessagingService;
import com.daffodils.psychiatry.helper.Utils;
import com.daffodils.psychiatry.helper.VolleyCallback;
import com.daffodils.psychiatry.model.CommonGetterSetter;
import com.daffodils.psychiatry.model.VideosGetterSetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

public class RegisterActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtMobile, edtPass, edtConfirmPass, edtPG, edtCollege, edtPursuing;
    Button btnRegister;
    TextView tvLogin, txtSelectCourse, txtSelectModule;
    Spinner select_course, select_module;
    Activity activity;
    Context context;
    String itemSelect ="";
    RelativeLayout RLSelectCourse, RLSelectModule;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    private String android_id, token_id;
    MyFirebaseMessagingService myFirebaseMessagingService = new MyFirebaseMessagingService();

    public List<String> m_courseType = new ArrayList<>();
    ArrayList<String> arrayList_Course = new ArrayList<>();

    public List<String> m_moduleType = new ArrayList<>();
    ArrayList<String> arrayList_Module = new ArrayList<>();

    public static ArrayList<String> date_array = new ArrayList<String>();
    public static ArrayList<String> date1_array = new ArrayList<String>();
    public static ArrayList<String> value_array = new ArrayList<String>();
    public static ArrayList<String> value1_array = new ArrayList<>();

    public static List<CommonGetterSetter> m_listCourse = new ArrayList<>();
    public static List<CommonGetterSetter> m_listModule = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyt_register);

        activity = RegisterActivity.this;
        context = getApplicationContext();

        android_id = Settings.Secure.getString(getApplication().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        edtName = findViewById(R.id.edtname);
        edtEmail = findViewById(R.id.edtemail);
        edtMobile = findViewById(R.id.edtmobile);
        edtPass = findViewById(R.id.edtpsw);
        edtConfirmPass = findViewById(R.id.edtcpsw);
        edtPG = findViewById(R.id.edtPG);
        edtCollege = findViewById(R.id.edtCollege);
        edtPursuing = findViewById(R.id.edtPursuing);

        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvSignIn);
        RLSelectCourse = findViewById(R.id.RLSelectCourse);
        txtSelectCourse = findViewById(R.id.txtSelectCourse);
        select_course = findViewById(R.id.select_course);

        RLSelectModule = findViewById(R.id.RLSelectModule);
        txtSelectModule = findViewById(R.id.txtSelectModule);
        select_module = findViewById(R.id.select_module);

        edtName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_name, 0, 0, 0);
        edtEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email, 0, 0, 0);
        edtMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone, 0, 0, 0);
        edtPG.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pg, 0, 0 ,0);
        edtCollege.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_college, 0, 0 ,0);
        edtPursuing.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_pg, 0, 0 ,0);
        edtPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, R.drawable.ic_show, 0);
        edtConfirmPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, R.drawable.ic_show, 0);

        Utils.setHideShowPassword(edtPass);
        Utils.setHideShowPassword(edtConfirmPass);

        fetchCoursesDetails();

        RLSelectCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(RegisterActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.search_layout_list);
                RecyclerView airport_recycler_view = (RecyclerView) dialog.findViewById(R.id.airport_recycler_view);
                Button ok = (Button) dialog.findViewById(R.id.btnOk);
                TextView lblHeading = dialog.findViewById(R.id.lblHeading);
                lblHeading.setText("Select Course");

                Window window = dialog.getWindow();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(window.getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(layoutParams);
                dialog.show();
                date_array.clear();
                value_array.clear();

                SearchAdapter1 adapter = new SearchAdapter1(context, arrayList_Course);

            //    airport_recycler_view.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                airport_recycler_view.setLayoutManager(mLayoutManager);
                airport_recycler_view.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
                airport_recycler_view.setItemAnimator(new DefaultItemAnimator());
                airport_recycler_view.setAdapter(adapter);


                airport_recycler_view.addOnItemTouchListener(new MainActivity.RecyclerTouchListener(context, airport_recycler_view, new MainActivity.ClickListener() {

                    public void onClick(View view, int position) {
                       //  itemSelect = m_courseType.get(position);
                        // dialog.dismiss();
                    }


                    public void onLongClick(View view, int position) {

                    }
                }));

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtSelectCourse.setText("");

                        //   insert_into_database();

                        for (int i = 0 ; i < date_array.size(); i++){

                            txtSelectCourse.setText(txtSelectCourse.getText() + " " + date_array.get(i));
                            itemSelect = itemSelect + "," + value_array.get(i);
                        }

                        fetchModuleDetails();


                        dialog.dismiss();
                    }
                });



                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK &&
                                event.getAction() == KeyEvent.ACTION_UP &&
                                !event.isCanceled()) {
                            dialog.cancel();
                            return true;
                        }
                        return false;
                    }
                });


            }
        });

        RLSelectModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(RegisterActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.search_layout_list);
                RecyclerView airport_recycler_view = (RecyclerView) dialog.findViewById(R.id.airport_recycler_view);
                Button ok = (Button) dialog.findViewById(R.id.btnOk);
                TextView lblHeading = dialog.findViewById(R.id.lblHeading);
                lblHeading.setText("Select Module");

                Window window = dialog.getWindow();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(window.getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(layoutParams);
                dialog.show();
                date1_array.clear();
                value1_array.clear();

                SearchAdapter adapter = new SearchAdapter(context, arrayList_Module);

             //   airport_recycler_view.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                airport_recycler_view.setLayoutManager(mLayoutManager);
                airport_recycler_view.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
                airport_recycler_view.setItemAnimator(new DefaultItemAnimator());
                airport_recycler_view.setAdapter(adapter);


                airport_recycler_view.addOnItemTouchListener(new MainActivity.RecyclerTouchListener(context, airport_recycler_view, new MainActivity.ClickListener() {

                    public void onClick(View view, int position) {
                        // itemSelect = arrayList_Service.get(position);
                        // dialog.dismiss();
                    }


                    public void onLongClick(View view, int position) {

                    }
                }));

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtSelectModule.setText("");

                        for (int i = 0 ; i < date1_array.size(); i++){

                            txtSelectModule.setText(txtSelectModule.getText() + " " + date1_array.get(i));
                        }
                        dialog.dismiss();
                    }
                });



                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK &&
                                event.getAction() == KeyEvent.ACTION_UP &&
                                !event.isCanceled()) {
                            dialog.cancel();
                            return true;
                        }
                        return false;
                    }
                });


            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerService();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void registerService() {

        String Name = edtName.getText().toString();
        String Email = edtEmail.getText().toString();
        String Mobile = edtMobile.getText().toString();
        String PG = edtPG.getText().toString();
        String Pursuing = edtPursuing.getText().toString();
        String College = edtCollege.getText().toString();
        String Password = edtPass.getText().toString();
        String ConfirmPassword = edtConfirmPass.getText().toString();

        JSONArray jsonArray = new JSONArray();
        for (int i= 0; i < value_array.size();i++){

            jsonArray.put(value_array.get(i));

        }

        JSONArray jsonArray1 = new JSONArray();
        for (int i= 0; i < value1_array.size();i++){

            jsonArray1.put(value1_array.get(i));

        }


        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("Name", Name.trim());
            jsonObject.put("EmailID", Email.trim());
            jsonObject.put("Password", Password.trim());
            jsonObject.put("MobileNo", Mobile.trim());
         //   jsonObject.put("CourseID", jsonArray);
         //   jsonObject.put("ModuleID", jsonArray1);
            jsonObject.put("YearOfPG", PG.trim());
            jsonObject.put("Pursuing", Pursuing.trim());
            jsonObject.put("College", College.trim());
            jsonObject.put("DeviceID", android_id);
            jsonObject.put("TokenID", token_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("jsonString: "+jsonObject);

        if (ApiConfig.CheckValidattion(Name, false,false)) {
            edtName.setError(getString(R.string.enter_name));
        } else if (ApiConfig.CheckValidattion(Email, false, false)) {
            edtEmail.setError(getString(R.string.enter_email));
        } else if (ApiConfig.CheckValidattion(Email, true, false)) {
            edtEmail.setError(getString(R.string.enter_valid_email));
        } else if (ApiConfig.CheckValidattion(Mobile, false, false)) {
            edtMobile.setError(getString(R.string.enter_mobile_no));
        } else if (ApiConfig.CheckValidattion(Mobile, false, true)) {
            edtMobile.setError(getString(R.string.enter_valid_mobile_no));
        } else if (ApiConfig.CheckValidattion(PG, false, false)) {
            edtPG.setError(getString(R.string.enter_pg_det));
        } else if (ApiConfig.CheckValidattion(College, false, false)) {
            edtCollege.setError(getString(R.string.enter_college));
        } else if (ApiConfig.CheckValidattion(Pursuing, false, false)) {
            edtPursuing.setError(getString(R.string.enter_pursuing));
        } else if (ApiConfig.CheckValidattion(Password, false, false)) {
            edtPass.setError(getString(R.string.enter_pass));
        } else if (ApiConfig.CheckValidattion(ConfirmPassword, false , false)) {
            edtConfirmPass.setError(getString(R.string.enter_confirm_pass));
        } else if (!Password.equals(ConfirmPassword)){
            edtConfirmPass.setError(getString(R.string.pass_not_match));

        } else if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();

            params.put("SC", GlobalConst.SC_REGISTRATION);
            params.put("JSON", jsonObject.toString());

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (GlobalConst.Result.equals("F")){
                                Toast.makeText(context, GlobalConst.Description, Toast.LENGTH_LONG).show();
                            } else {

                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                                // Setting Dialog Message
                                alertDialog.setTitle("Registered Successfully !!");
                              //  alertDialog.setMessage("Please contact to admin for activation of account or visit https://daffodilspsychiatry.com/ for more details.");
                                alertDialog.setMessage("You have successfully being registered. Thank You!!");
                                alertDialog.setCancelable(false);
                                final AlertDialog alertDialog1 = alertDialog.create();

                                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        try {

                                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(i);
                                            finish();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                // Showing Alert Message
                                alertDialog.show();
                               /* Toast.makeText(context, "Registered Successfully. Please contact to admin for activation of account", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();*/
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim() , params, true);

        }

    }

    public void fetchCoursesDetails(){

        m_courseType.clear();
        m_listCourse.clear();
        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();
            params.put("SC", GlobalConst. SC_GET_ALL_COURSES);

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
                                    String CourseID = jsonObject.getString("CourseID");

                                    arrayList_Course.add(CourseName);
                                    CommonGetterSetter commonGetterSetter = new CommonGetterSetter(CourseID, CourseName);
                                    m_listCourse.add(commonGetterSetter);
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

    public void fetchModuleDetails(){

        m_moduleType.clear();
        m_listModule.clear();

        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();
            params.put("SC", GlobalConst.SC_GET_COURSE_MODULES);
            params.put("CourseID", itemSelect.substring(1));

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
                                    String ModuleName = jsonObject.getString("ModuleName");
                                    String ModuleID = jsonObject.getString("ModuleID");

                                    arrayList_Module.add(ModuleName);
                                    CommonGetterSetter commonGetterSetter = new CommonGetterSetter(ModuleID, ModuleName);
                                    m_listModule.add(commonGetterSetter);
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

}

