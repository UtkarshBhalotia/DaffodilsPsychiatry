package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.activity.MainActivity;
import com.daffodils.psychiatry.activity.RegisterActivity;
import com.daffodils.psychiatry.helper.ApiConfig;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.helper.VolleyCallback;
import com.google.android.material.snackbar.Snackbar;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class CrashCourseFragment extends Fragment {

    View root;
    Activity activity;
    TextView txtpap1_topic, txtpap2_topic, txtpap3_topic, txtpap4_topic, txtDuration;
    Button btnSubscribe;
    RelativeLayout lytCart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.lyt_crash_course, container, false);
        activity = getActivity();
        setHasOptionsMenu(true);

        txtpap1_topic = root.findViewById(R.id.pap1_topic);
        txtpap2_topic = root.findViewById(R.id.pap2_topic);
        txtpap3_topic = root.findViewById(R.id.pap3_topic);
        txtpap4_topic = root.findViewById(R.id.pap4_topic);
        lytCart = root.findViewById(R.id.lytCart);
        txtDuration = root.findViewById(R.id.txtDuration);

        txtDuration.setText("Crash Course 2022 : " + GlobalConst.CRASH_COURSE_DURATION);

        btnSubscribe = root.findViewById(R.id.btnSubscribe);

        txtpap1_topic.setText("Biological basis for craving. drug addiction. anxiety disorders, depressive disorders, Psychosis, ADHD; Neurroanatomy including various neurotransmitters and their role in Psychiatry, Neuropeptide, Endorphin, Limbic system; Psychology including Kindling,  Imprinting, Dream  work, Intelligence  including  Emotional intel ligence, Expressed emotions; Erik Erikson, Piaget, Carl Jung Sigmund Freud, Neo· Freudians: Theories of personality and Projective tests, Biological basis of learning and memory, Cognitive distortions. Defence mechanism;Statistics");
        txtpap2_topic.setText("Adult ADHD, Anxiety , spectrum, Attenuated psychoois syndrome, Body dysmorphic disorder, CANMAT guidelines, Catatonia, Dissociation, Eating disorders, Erectile Dysfunction, Gender identity disorder, Grief reaction,  Lithium  toxicity, Mood  disorders,  Mx  of  acute psychiatric  patient,  OCD,  Outcome  studies of schizophrenia, JED, Recovery in schizophrenia, Schizo.obsessive disorder, Schizophrenia, Sexual dysfunction, Sleep disorders, Suicide and Deliberate self.harm");
        txtpap3_topic.setText("Child Psychiatry including ADHD, Specific Learning disorder, Child abuse,Autistic spectrum disorders, Conduct Disor.ders. Enuresis, Tourette syndrome, Psychosis and Mood disorder in childhood; Delirium tremens, Depots/ LAls (Long acting injectables, ECT; Forensic and Community Psychiatry including Mental health act, 20 17, NM HP. RPwD 20 14, NDPS, IDEAS,Testamentary capacity,Psycho logical autopsy,Medical negligence and others; Geriatric depression, NMS / EPS, PMDD premenstrual dysphoric disorder. POCSO Act, Post partum psychiatric disorder. RTMS, Substance use Disorders including Opioid, Cocaine, Inhalant abuse, Solvent abuse . Nicotine dependence, Benzodiazepine abuse, Club drugs,Tobacco control in India, Epidemiology of SUD in India (AJJMS Study)");
        txtpap4_topic.setText("Breaking the bad news, CL psychiatry, Contributions of famous Psychiatrists, Culture bound syndromes in India, Dementia, Drug trials, EEG, Ethics in psychiatry, Euthanasia, Functional neuroimaging in psychiatry, Headache, HIV, ICU psychosis, Landmaik studies in psychia try, Lobru- function test and Neuropsychological assessment, Neuroleptic malignant syndrome, NeuroPsychiatry including Cerebral dominance, Soft neurological signs, Phantom limb, Frontal Iobe syndrome");

        lytCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new CartFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "Cart");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

            }
        });

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Add to Cart Crash Course ?");
                alertDialog.setMessage("Do you want to Add Crash Course to Cart ?");
                alertDialog.setCancelable(false);
                final AlertDialog alertDialog1 = alertDialog.create();

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {

                            if (!GlobalConst.User_id.isEmpty()){
                                addToCartService("0", GlobalConst.CrashCourse);
                              //  subscribeModuleService("0");

                            } else {
                                Intent i = new Intent(activity, RegisterActivity.class);
                                startActivity(i);
                                Toast.makeText(activity, "Kindly Register to ADD.", Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog1.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        return root;

    }

    public void subscribeModuleService(String ModuleNumber){

        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();

            params.put("SC", GlobalConst.SC_SUBSCRIBE_MODULES);
            params.put("CourseID", GlobalConst.CrashCourseID);
            params.put("ModuleID", ModuleNumber);
            params.put("SubscriptionType", GlobalConst.CrashCourse);
            params.put("UserID", GlobalConst.User_id);

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (GlobalConst.Result.equals("T")){
                                sendMegToUser();
                               // setSnackBar("You have successfully been subscribed.", "OK");

                            } else {
                                setSnackBar("Desc : " + GlobalConst.Description , "OK");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim() , params, true);

        }
    }

    public void addToCartService(String ModuleNumber, String SubscriptionType){

        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();

            params.put("SC", GlobalConst.SC_ADD_TO_CART);
            params.put("ModuleID", ModuleNumber);
            params.put("SubscriptionType", SubscriptionType);
            params.put("UserID", GlobalConst.User_id);
            params.put("CartType", GlobalConst.CART_ADD);

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (GlobalConst.Result.equals("T")){
                                setSnackBar("Item added to cart successfully.", "OK");
                            } else {
                                setSnackBar("Description : " + GlobalConst.Description, "OK");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim() , params, true);

        }
    }

    public void sendMegToUser(){

        String message = "You will be able to avail the context of subscription within a day. For further details contact" +
                " at +91-9872551972 / +91-7528920011 or Email Us @ daffodils.psych@gmail.com. Thank You !!";
        String encoded_message = URLEncoder.encode(message);

        String mainUrl = "http://mysms.msg24.in/api/mt/SendSMS?";

        StringBuilder sbPostData = new StringBuilder(mainUrl);
        sbPostData.append("user=" + "RowallaEnterprises");
        sbPostData.append("&password=" + "123456");
        sbPostData.append("&senderid=" + "RNITBP");
        sbPostData.append("&channel=" + "Trans");
        sbPostData.append("&DCS=" + "0");
        sbPostData.append("&flashsms=" + "0");
        sbPostData.append("&number=" + GlobalConst.Mobile);
        sbPostData.append("&text=" + encoded_message);
        sbPostData.append("&route=" + "08");

        mainUrl = sbPostData.toString();

        RequestQueue queue = Volley.newRequestQueue(activity);
        String url = mainUrl;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Toast.makeText(activity, "Sent successfully.", Toast.LENGTH_LONG).show();
                        sendMsgToAdmin();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    public void sendMsgToAdmin(){

        String message = "New Subscription details : Name : " + GlobalConst.Name + " , Mobile No " + GlobalConst.Mobile +
                " . Plz visit portal to know more." +". Thank You !!";
        String encoded_message = URLEncoder.encode(message);

        String mainUrl = "http://mysms.msg24.in/api/mt/SendSMS?";

        StringBuilder sbPostData = new StringBuilder(mainUrl);
        sbPostData.append("user=" + "RowallaEnterprises");
        sbPostData.append("&password=" + "123456");
        sbPostData.append("&senderid=" + "RNITBP");
        sbPostData.append("&channel=" + "Trans");
        sbPostData.append("&DCS=" + "0");
        sbPostData.append("&flashsms=" + "0");
        sbPostData.append("&number=" + "9872551972");
       // sbPostData.append("&number=" + "8882068510");
        sbPostData.append("&text=" + encoded_message);
        sbPostData.append("&route=" + "08");

        mainUrl = sbPostData.toString();

        RequestQueue queue = Volley.newRequestQueue(activity);
        String url = mainUrl;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(activity, "You have successfully been subscribed.", Toast.LENGTH_LONG).show();
                        Fragment fragment = new PaymentDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("from", "Payment");
                        fragment.setArguments(bundle);
                        MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }


    public void setSnackBar(String message, String action) {
        final Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  Intent intent = new Intent(activity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
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
        GlobalConst.TOOLBAR_TITLE = getString(R.string.title_crash);
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
