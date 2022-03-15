package com.daffodils.psychiatry.fragment;

import static com.daffodils.psychiatry.R.layout.lyt_cart;
import static com.daffodils.psychiatry.R.layout.progressbarlayout;
import static com.daffodils.psychiatry.R.layout.toolbar_layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.activity.MainActivity;
import com.daffodils.psychiatry.activity.PayMentGateWay;
import com.daffodils.psychiatry.adapter.OrderSummaryAdapter;
import com.daffodils.psychiatry.helper.ApiConfig;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.helper.VolleyCallback;
import com.daffodils.psychiatry.model.OrderSummaryGetterSetter;
import com.google.android.datatransport.runtime.dagger.Module;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartFragment extends Fragment {

    View root;
    public static Activity activity;
    public static Context context;
    public static ProgressBar progressBar;
    EditText edtPromoCode;
    Button btnApply;
    public static TextView txtAmtToBePaid, tvProceed, tvTotal, tvPromoDiscount, tvFinalAmt, tvAlert;
    public static RecyclerView recyclerView;
    public static List<OrderSummaryGetterSetter> m_orderSummary = new ArrayList<>();
    public static OrderSummaryAdapter orderSummaryAdapter;
    public static String m_SubTotalAmt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(lyt_cart, container, false);
        activity = getActivity();
        context = getContext();
        setHasOptionsMenu(true);

        edtPromoCode = root.findViewById(R.id.edtPromoCode);
        btnApply = root.findViewById(R.id.btnApply);
        progressBar = root.findViewById(R.id.progressBar);
        txtAmtToBePaid = root.findViewById(R.id.tvAmttobepaid);
        tvProceed = root.findViewById(R.id.tvProceed);
        tvTotal = root.findViewById(R.id.tvTotal);
        tvPromoDiscount = root.findViewById(R.id.tvPromoDiscount);
        tvFinalAmt = root.findViewById(R.id.tvFinalAmt);
        tvAlert = root.findViewById(R.id.tvAlert);
        recyclerView = root.findViewById(R.id.summaryRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getCartDetailsService();

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String promoCode = edtPromoCode.getText().toString().trim();

                if (promoCode.isEmpty()){
                    tvAlert.setVisibility(View.VISIBLE);
                    tvAlert.setText("Enter Promo Code");
                } else {
                    tvAlert.setVisibility(View.GONE);
                    btnApply.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    promoCodevalidateService();
                }
            }
        });

        tvProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(activity, PayMentGateWay.class);
                i.putExtra("RECHARGE_AMT", m_SubTotalAmt);
                startActivity(i);

                //Toast.makeText(context, "Payment Gateway Under Construction.", Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

    public void promoCodevalidateService(){
        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();

            params.put("SC", GlobalConst.SC_APPLY_COUPON);
            params.put("CouponCode", edtPromoCode.getText().toString());
            params.put("TotalAmount", m_SubTotalAmt);

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (GlobalConst.Result.equals("T")){
                                btnApply.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_green));
                                btnApply.setText("Applied");
                                tvFinalAmt.setText(GlobalConst.SETTING_CURRENCY_SYMBOL + GlobalConst.formater.format(Double.valueOf(GlobalConst.FINALAMOUNT)));
                                txtAmtToBePaid.setText(GlobalConst.SETTING_CURRENCY_SYMBOL + GlobalConst.formater.format(Double.valueOf(GlobalConst.FINALAMOUNT)));
                                tvPromoDiscount.setText(GlobalConst.SETTING_CURRENCY_SYMBOL + GlobalConst.formater.format(Double.valueOf(GlobalConst.DISCOUNT)));

                            } else {
                                btnApply.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                                btnApply.setText("Apply");
                                tvAlert.setVisibility(View.VISIBLE);
                                tvAlert.setText(GlobalConst.Description);
                            }

                            progressBar.setVisibility(View.GONE);
                            btnApply.setVisibility(View.VISIBLE);

                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim() , params, true);

        }
    }

    public static void getCartDetailsService(){

        m_orderSummary.clear();
        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();

            params.put("SC", GlobalConst.SC_GET_CART);
            params.put("UserID", GlobalConst.User_id);

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (GlobalConst.Result.equals("T")){
                                JSONObject jsonObject = new JSONObject(response);
                                m_SubTotalAmt = jsonObject.getString("TotalAmount");
                                tvTotal.setText(GlobalConst.SETTING_CURRENCY_SYMBOL + GlobalConst.formater.format(Double.valueOf(m_SubTotalAmt)));
                                tvFinalAmt.setText(GlobalConst.SETTING_CURRENCY_SYMBOL + GlobalConst.formater.format(Double.valueOf(m_SubTotalAmt)));
                                txtAmtToBePaid.setText(GlobalConst.SETTING_CURRENCY_SYMBOL + GlobalConst.formater.format(Double.valueOf(m_SubTotalAmt)));
                                tvPromoDiscount.setText("-");

                                JSONArray jsonArray = jsonObject.getJSONArray("dtModules");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject innerObj = jsonArray.getJSONObject(i);
                                    String ModuleID = innerObj.getString("ModuleID");
                                    String SubscriptionTypeID = innerObj.getString("SubscriptionType");
                                    String ModuleName = innerObj.getString("ModuleName");
                                    String CourseName = innerObj.getString("CourseName");
                                    String Amount = innerObj.getString("Amount");

                                    OrderSummaryGetterSetter orderSummaryGetterSetter = new OrderSummaryGetterSetter(ModuleName, CourseName, Amount, ModuleID, SubscriptionTypeID);
                                    m_orderSummary.add(orderSummaryGetterSetter);
                                    orderSummaryAdapter = new OrderSummaryAdapter(context, m_orderSummary);
                                    recyclerView.setAdapter(orderSummaryAdapter);
                                }

                                progressBar.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(activity, GlobalConst.Description, Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim() , params, true);

        }
    }

    public static void removeFromCartService(String ModuleNumber, String SubscriptionType){

        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();

            params.put("SC", GlobalConst.SC_ADD_TO_CART);
            params.put("ModuleID", ModuleNumber);
            params.put("SubscriptionType", SubscriptionType);
            params.put("UserID", GlobalConst.User_id);
            params.put("CartType", GlobalConst.CART_REMOVE);

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (GlobalConst.Result.equals("T")){
                                Toast.makeText(activity, "Item removed from cart successfully.", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(activity, "Description : " + GlobalConst.Description, Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim() , params, true);

        }
    }

}
