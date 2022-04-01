package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.adapter.OrderSummaryAdapter;
import com.daffodils.psychiatry.adapter.ValidityDetailsAdapter;
import com.daffodils.psychiatry.helper.ApiConfig;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.helper.VolleyCallback;
import com.daffodils.psychiatry.model.OrderSummaryGetterSetter;
import com.daffodils.psychiatry.model.ValidityDetailsGetterSetter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.daffodils.psychiatry.R.layout.lyt_validity;

public class ValidityDetails extends Fragment {

    View root;
    Activity activity;
    Context context;
    RecyclerView recyclerView_details;
    public static List<ValidityDetailsGetterSetter> m_validityDetails = new ArrayList<>();
    ValidityDetailsAdapter validityDetailsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(lyt_validity, container, false);
        activity = getActivity();
        context = getContext();

        recyclerView_details = root.findViewById(R.id.recycler_details);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView_details.setLayoutManager(mLayoutManager);
        recyclerView_details.setItemAnimator(new DefaultItemAnimator());

        fetchValidityDetails();

        return root;
    }

    public void fetchValidityDetails(){

        m_validityDetails.clear();
        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();

            params.put("SC", GlobalConst.SC_GET_PROFILE_DETAILS);
            params.put("UserID", GlobalConst.User_id);

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (GlobalConst.Result.equals("T")){

                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject innerObj = jsonArray.getJSONObject(i);

                                    String CourseName = innerObj.getString("CourseName");
                                    String ModuleName = innerObj.getString("ModuleName");
                                    String StartDate = innerObj.getString("SubscriptionStartDate");
                                    String EndDate = innerObj.getString("SubscriptionEndDate");
                                    String DaysLeft = innerObj.getString("DaysLeft");

                                    ValidityDetailsGetterSetter validityDetailsGetterSetter = new ValidityDetailsGetterSetter(CourseName, ModuleName, StartDate, EndDate, DaysLeft);
                                    m_validityDetails.add(validityDetailsGetterSetter);
                                    validityDetailsAdapter = new ValidityDetailsAdapter(context, m_validityDetails);
                                    recyclerView_details.setAdapter(validityDetailsAdapter);
                                }


                            } else {
                                Toast.makeText(activity, GlobalConst.Description, Toast.LENGTH_LONG).show();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim() , params, true);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalConst.TOOLBAR_TITLE = getString(R.string.title_validity);
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
