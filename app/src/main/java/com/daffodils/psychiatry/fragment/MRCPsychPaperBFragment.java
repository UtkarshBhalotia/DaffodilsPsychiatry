package com.daffodils.psychiatry.fragment;

import static android.content.Context.INPUT_METHOD_SERVICE;

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

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.activity.MainActivity;
import com.daffodils.psychiatry.activity.RegisterActivity;
import com.daffodils.psychiatry.helper.ApiConfig;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.helper.VolleyCallback;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class MRCPsychPaperBFragment extends Fragment {

    View root;
    Activity activity;
    TextView txtpapb_topic, txtDuration;
    Button btnSubscribe;
    RelativeLayout lytCart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.lyt_mrc_psych_paper_b, container, false);
        activity = getActivity();
        setHasOptionsMenu(true);

        lytCart = root.findViewById(R.id.lytCart);
        btnSubscribe = root.findViewById(R.id.btnSubscribe);

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
                alertDialog.setTitle("Add to Cart MRCPsych PaperB Course ?");
                alertDialog.setMessage("Do you want to Add MRCPsych PaperB Course to Cart ?");
                alertDialog.setCancelable(false);
                final AlertDialog alertDialog1 = alertDialog.create();

                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {

                            if (!GlobalConst.User_id.isEmpty()){
                                addToCartService("14", GlobalConst.MRCPsychStaticsPaperB);
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
        GlobalConst.TOOLBAR_TITLE = getString(R.string.title_mrc_psych_paperb);
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
