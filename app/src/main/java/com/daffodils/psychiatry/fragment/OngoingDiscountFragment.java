package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.helper.GlobalConst;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.daffodils.psychiatry.R.layout.lyt_ongoing_discount;

public class OngoingDiscountFragment extends Fragment {

    TextView txtFFC, txtCombo;
    View root;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(lyt_ongoing_discount, container, false);
        activity = getActivity();
        setHasOptionsMenu(true);

        txtFFC = root.findViewById(R.id.txtFFC);
        txtCombo = root.findViewById(R.id.txtCombo);
        txtFFC.setPaintFlags(txtFFC.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtCombo.setPaintFlags(txtCombo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalConst.TOOLBAR_TITLE = getString(R.string.title_discount);
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
