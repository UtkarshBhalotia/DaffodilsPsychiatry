package com.daffodils.psychiatry.fragment;

import static com.daffodils.psychiatry.R.layout.lyt_cart;
import static com.daffodils.psychiatry.R.layout.toolbar_layout;

import android.app.Activity;
import android.content.Context;
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
    Activity activity;
    Context context;
    ProgressBar progressBar;
    EditText edtPromoCode;
    Button btnApply;
    TextView txtAmtToBePaid, tvProceed, tvTotal, tvPromoDiscount, tvFinalAmt, tvAlert;
    RecyclerView recyclerView;
    public static List<OrderSummaryGetterSetter> m_orderSummary = new ArrayList<>();
    OrderSummaryAdapter orderSummaryAdapter;

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

        return root;
    }

    public void promoCodevalidateService(){
        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();

            params.put("SC", GlobalConst.SC_APPLY_COUPON);
            params.put("CouponCode", edtPromoCode.getText().toString());
            params.put("TotalAmount", "10000");

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (GlobalConst.Result.equals("T")){
                                btnApply.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_green));
                                btnApply.setText("Applied");
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
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim() , params, true);

        }
    }

    public void getCartDetailsService(){
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
                                String TotalAmt = jsonObject.getString("TotalAmount");
                                String dtModules = jsonObject.getString("dtModules");

                                JSONArray jsonArray = jsonObject.getJSONArray(dtModules);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject innerObj = jsonArray.getJSONObject(i);
                                    String ModuleName = innerObj.getString("ModuleName");
                                    String CourseName = innerObj.getString("CourseName");
                                    String Amount = innerObj.getString("Amount");

                                    OrderSummaryGetterSetter orderSummaryGetterSetter = new OrderSummaryGetterSetter(ModuleName, CourseName, Amount);
                                    m_orderSummary.add(orderSummaryGetterSetter);
                                    orderSummaryAdapter = new OrderSummaryAdapter(context, m_orderSummary);
                                    recyclerView.setAdapter(orderSummaryAdapter);
                                }

                            } else {
                                Toast.makeText(context, GlobalConst.Description, Toast.LENGTH_LONG).show();
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
