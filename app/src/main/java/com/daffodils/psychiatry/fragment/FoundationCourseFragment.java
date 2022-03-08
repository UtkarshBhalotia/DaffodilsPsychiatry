package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.daffodils.psychiatry.activity.DrawerActivity;
import com.daffodils.psychiatry.activity.LoginActivity;
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

public class FoundationCourseFragment extends Fragment {

    View root;
    Activity activity;
    TextView txtFFC;
    TextView txtmod1_topic, txtmod2_topic, txtmod3_topic, txtmod4_topic, txtmod5_topic, txtmod6_topic, txtmod7_topic, txtmod8_topic, txtmod9_topic;
    TextView txtSubsFullCourse, txtSubsMod1, txtSubsMod2, txtSubsMod3, txtSubsMod4, txtSubsMod5, txtSubsMod6, txtSubsMod7, txtSubsMod8, txtSubsMod9;
    RelativeLayout lyCart;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.lyt_foundation_course, container, false);
        activity = getActivity();
        setHasOptionsMenu(true);

        txtmod1_topic = root.findViewById(R.id.mod1_topic);
        txtmod2_topic = root.findViewById(R.id.mod2_topic);
        txtmod3_topic = root.findViewById(R.id.mod3_topic);
        txtmod4_topic = root.findViewById(R.id.mod4_topic);
        txtmod5_topic = root.findViewById(R.id.mod5_topic);
        txtmod6_topic = root.findViewById(R.id.mod6_topic);
        txtmod7_topic = root.findViewById(R.id.mod7_topic);
        txtmod8_topic = root.findViewById(R.id.mod8_topic);
        txtmod9_topic = root.findViewById(R.id.mod9_topic);
        txtFFC = root.findViewById(R.id.txtFFC);

        txtFFC.setPaintFlags(txtFFC.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        txtSubsFullCourse = root.findViewById(R.id.txtSubsFullCourse);
        txtSubsMod1 = root.findViewById(R.id.txtSubsMod1);
        txtSubsMod2 = root.findViewById(R.id.txtSubsMod2);
        txtSubsMod3 = root.findViewById(R.id.txtSubsMod3);
        txtSubsMod4 = root.findViewById(R.id.txtSubsMod4);
        txtSubsMod5 = root.findViewById(R.id.txtSubsMod5);
        txtSubsMod6 = root.findViewById(R.id.txtSubsMod6);
        txtSubsMod7 = root.findViewById(R.id.txtSubsMod7);
        txtSubsMod8 = root.findViewById(R.id.txtSubsMod8);
        txtSubsMod9 = root.findViewById(R.id.txtSubsMod9);

        lyCart = root.findViewById(R.id.lytCart);

        txtmod1_topic.setText("Specific learning disorder, Temperament, Elimination disorders, Child Forensic Psychiatry(POCSO Act), Mood disorders in children , Schizophrenia in children, Intellectual disability, Autism, ADHD, Conduct disorder, ODD, Tourette's syndrome.");
        txtmod2_topic.setText("Schizophrenia, Mood disorders, Anxiety and Grief Disorders, Suicide, Mood Stabilizers, OCD, Body Dysmorphic disorder.");
        txtmod3_topic.setText("Sleep and Psychiatry, Somatoform Disorder, Dissociative Disorders, Factitious Disorders, Gender Identity Disorders, Sexual Disorders, Dementia, Special population in Psychiatry.");
        txtmod4_topic.setText("Criminal Responsibilty and Psychiatry, Fitness to stand trail, MHCA-2017, NDPS, RPWD, Informed consent, Medical Negliganece, Psychological, Autopsy, Testamentary Capacity, Psychiatrist in court, IDEAS, NMHS, NMHP, DMHP.");
        txtmod5_topic.setText("Alcohol, Cannabis & Endocannabinoid system, Opioids, Cocaine, Inhalants, Tobacco, Behavioral Addiction, Dual Diagnosis.");
        txtmod6_topic.setText("Intelligence, Emotional Quotient, Kindling, Imprinting, Theory of Mind, Learning, Memory, Expressed emotions, Coping Strategies, Defense Mechanism, Freud, Neofreud theories, Personality Testing.");
        txtmod7_topic.setText("Epilepsy, Consultation Liasion, Ect, rTMS, Soft Neurological Signs, Vascular Depression, Neurotransmitters and receptors, Limbic systems, Cerebral Dominance, Phantom Limb, Sterios Induced Psychosis, Depression in clinical setting, Headache.");
        txtmod8_topic.setText("Detailed mental status examination, Psychopathology discussion, Cases on Schizophrenia, Bipolace disorder, Elderly Depression, Substance use disorder, Explanation of Token Economy, Lithium toxicity, Cognitive Behaviour therapy, Kirby's method, Treatment Substance Schizophrenia.");
        txtmod9_topic.setText("Pathways and receptors of Dopamine, Glutamate, GABA; NMDA Hypo functioning hypothesis of Schizophrenia, Serotonergic pathways and receptors, Hypnosis, anti-manic, antidepressants, antipsychotics, Clozapine, Individual antipsychotic drugs including Newer 3rd Gen antipsychotics, Depression and mania, SSRIs in detail, SPARI, SNRIs, Agomelatine, alpha blockers, SARIs,TCAs, Vortioxetin, Mood stabilizers, Lithium, Valproate and other mood stabilizers including special considerations, ADHD including individual drugs, Addiction, impulsivity, compulsivity including individual drugs, anxiety.");

        lyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new CartFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "Cart");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

            }
        });

        txtSubsFullCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Subscribe Full Course ?");
                alertDialog.setMessage("Do you want to subscribe Full Course ?");
                alertDialog.setCancelable(false);
                final AlertDialog alertDialog1 = alertDialog.create();

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                                if (!GlobalConst.User_id.isEmpty()){
                                    addToCartService("1,2,3,4,5,6,7,8,9", GlobalConst.FullCourse);
                                   // subscribeModuleService("1,2,3,4,5,6,7,8,9", GlobalConst.FullCourse);
                                    //call web service
                                } else {

                                    Intent i = new Intent(activity, RegisterActivity.class);
                                    startActivity(i);
                                   // activity.finish();
                                    Toast.makeText(activity, "Kindly Register to Subscribe.", Toast.LENGTH_SHORT).show();
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

        txtSubsMod1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Subscribe Module 1 ?");
                alertDialog.setMessage("Do you want to subscribe Module 1 ?");
                alertDialog.setCancelable(false);
                final AlertDialog alertDialog1 = alertDialog.create();

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {

                            if (!GlobalConst.User_id.isEmpty()){
                                //call web service
                                addToCartService(GlobalConst.Module1ID, GlobalConst.AnyModule);
                                //subscribeModuleService(GlobalConst.Module1ID, GlobalConst.AnyModule);
                            } else {
                                Intent i = new Intent(activity, RegisterActivity.class);
                                startActivity(i);
                                // activity.finish();
                                Toast.makeText(activity, "Kindly Register to Subscribe.", Toast.LENGTH_SHORT).show();
                               // Toast.makeText(activity, "Please Login to Subscribe.", Toast.LENGTH_SHORT).show();
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
        txtSubsMod2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Subscribe Module 2 ?");
                alertDialog.setMessage("Do you want to subscribe Module 2 ?");
                alertDialog.setCancelable(false);
                final AlertDialog alertDialog1 = alertDialog.create();

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {

                            if (!GlobalConst.User_id.isEmpty()){
                                //call web service
                                addToCartService(GlobalConst.Module2ID, GlobalConst.AnyModule);
                                //subscribeModuleService(GlobalConst.Module2ID, GlobalConst.AnyModule);
                            } else {

                                Intent i = new Intent(activity, RegisterActivity.class);
                                startActivity(i);
                                // activity.finish();
                                Toast.makeText(activity, "Kindly Register to Subscribe.", Toast.LENGTH_SHORT).show();
                               // Toast.makeText(activity, "Please Login to Subscribe.", Toast.LENGTH_SHORT).show();
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

        txtSubsMod3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Subscribe Module 3 ?");
                alertDialog.setMessage("Do you want to subscribe Module 3 ?");
                alertDialog.setCancelable(false);
                final AlertDialog alertDialog1 = alertDialog.create();

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {

                            if (!GlobalConst.User_id.isEmpty()){
                                //call web service
                               // subscribeModuleService(GlobalConst.Module3ID, GlobalConst.AnyModule);
                                addToCartService(GlobalConst.Module3ID, GlobalConst.AnyModule);
                            } else {

                                Intent i = new Intent(activity, RegisterActivity.class);
                                startActivity(i);
                                // activity.finish();
                                Toast.makeText(activity, "Kindly Register to Subscribe.", Toast.LENGTH_SHORT).show();
                               // Toast.makeText(activity, "Please Login to Subscribe.", Toast.LENGTH_SHORT).show();
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
        txtSubsMod4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Subscribe Module 4 ?");
                alertDialog.setMessage("Do you want to subscribe Module 4 ?");
                alertDialog.setCancelable(false);
                final AlertDialog alertDialog1 = alertDialog.create();

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {

                            if (!GlobalConst.User_id.isEmpty()){
                                //call web service
                               // subscribeModuleService(GlobalConst.Module4ID, GlobalConst.AnyModule);
                                addToCartService(GlobalConst.Module4ID, GlobalConst.AnyModule);
                            } else {

                                Intent i = new Intent(activity, RegisterActivity.class);
                                startActivity(i);
                                // activity.finish();
                                Toast.makeText(activity, "Kindly Register to Subscribe.", Toast.LENGTH_SHORT).show();
                               // Toast.makeText(activity, "Please Login to Subscribe.", Toast.LENGTH_SHORT).show();
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

        txtSubsMod5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Subscribe Module 5 ?");
                alertDialog.setMessage("Do you want to subscribe Module 5 ?");
                alertDialog.setCancelable(false);
                final AlertDialog alertDialog1 = alertDialog.create();

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            if (!GlobalConst.User_id.isEmpty()){
                                //call web service
                               // subscribeModuleService(GlobalConst.Module5ID, GlobalConst.AnyModule);
                                addToCartService(GlobalConst.Module5ID, GlobalConst.AnyModule);
                            } else {

                                Intent i = new Intent(activity, RegisterActivity.class);
                                startActivity(i);
                                // activity.finish();
                                Toast.makeText(activity, "Kindly Register to Subscribe.", Toast.LENGTH_SHORT).show();
                               // Toast.makeText(activity, "Please Login to Subscribe.", Toast.LENGTH_SHORT).show();
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
        txtSubsMod6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Subscribe Module 6 ?");
                alertDialog.setMessage("Do you want to subscribe Module 6 ?");
                alertDialog.setCancelable(false);
                final AlertDialog alertDialog1 = alertDialog.create();

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            if (!GlobalConst.User_id.isEmpty()){
                                //call web service
                               // subscribeModuleService(GlobalConst.Module6ID, GlobalConst.AnyModule);
                                addToCartService(GlobalConst.Module6ID, GlobalConst.AnyModule);
                            } else {

                                Intent i = new Intent(activity, RegisterActivity.class);
                                startActivity(i);
                                // activity.finish();
                                Toast.makeText(activity, "Kindly Register to Subscribe.", Toast.LENGTH_SHORT).show();
                              //  Toast.makeText(activity, "Please Login to Subscribe.", Toast.LENGTH_SHORT).show();
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

        txtSubsMod7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Subscribe Module 7 ?");
                alertDialog.setMessage("Do you want to subscribe Module 7 ?");
                alertDialog.setCancelable(false);
                final AlertDialog alertDialog1 = alertDialog.create();

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            if (!GlobalConst.User_id.isEmpty()){
                                //call web service
                               // subscribeModuleService(GlobalConst.Module7ID, GlobalConst.AnyModule);
                                addToCartService(GlobalConst.Module7ID, GlobalConst.AnyModule);
                            } else {

                                Intent i = new Intent(activity, RegisterActivity.class);
                                startActivity(i);
                                // activity.finish();
                                Toast.makeText(activity, "Kindly Register to Subscribe.", Toast.LENGTH_SHORT).show();
                               // Toast.makeText(activity, "Please Login to Subscribe.", Toast.LENGTH_SHORT).show();
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
        txtSubsMod8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Subscribe Module 8 ?");
                alertDialog.setMessage("Do you want to subscribe Module 8 ?");
                alertDialog.setCancelable(false);
                final AlertDialog alertDialog1 = alertDialog.create();

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            if (!GlobalConst.User_id.isEmpty()){
                                //call web service
                               // subscribeModuleService(GlobalConst.Module8ID, GlobalConst.AnyModule);
                                addToCartService(GlobalConst.Module8ID, GlobalConst.AnyModule);
                            } else {

                                Intent i = new Intent(activity, RegisterActivity.class);
                                startActivity(i);
                                // activity.finish();
                                Toast.makeText(activity, "Kindly Register to Subscribe.", Toast.LENGTH_SHORT).show();
                               // Toast.makeText(activity, "Please Login to Subscribe.", Toast.LENGTH_SHORT).show();
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
        txtSubsMod9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Subscribe Module 9 ?");
                alertDialog.setMessage("Do you want to subscribe Module 9 ?");
                alertDialog.setCancelable(false);
                final AlertDialog alertDialog1 = alertDialog.create();

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            if (!GlobalConst.User_id.isEmpty()){
                                //call web service
                                //subscribeModuleService(GlobalConst.Module9ID, GlobalConst.AnyModule);
                                addToCartService(GlobalConst.Module9ID, GlobalConst.AnyModule);
                            } else {

                                Intent i = new Intent(activity, RegisterActivity.class);
                                startActivity(i);
                                // activity.finish();
                                Toast.makeText(activity, "Kindly Register to Subscribe.", Toast.LENGTH_SHORT).show();
                               // Toast.makeText(activity, "Please Login to Subscribe.", Toast.LENGTH_SHORT).show();
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

    public void subscribeModuleService(String ModuleNumber, String SubscriptionType){

        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();

            params.put("SC", GlobalConst.SC_SUBSCRIBE_MODULES);
            params.put("CourseID", GlobalConst.FoundationCourseID);
            params.put("ModuleID", ModuleNumber);
            params.put("SubscriptionType", SubscriptionType);
            params.put("UserID", GlobalConst.User_id);

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (GlobalConst.Result.equals("T")){
                                sendMegToUser();
                                //setSnackBar("You have successfully been subscribed.", "OK");
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
                                //sendMegToUser();
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
                        Toast.makeText(activity, "Sent successfully.", Toast.LENGTH_LONG).show();
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
     //   sbPostData.append("&number=" + "8882068510");
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
        GlobalConst.TOOLBAR_TITLE = getString(R.string.title_foundation);
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
