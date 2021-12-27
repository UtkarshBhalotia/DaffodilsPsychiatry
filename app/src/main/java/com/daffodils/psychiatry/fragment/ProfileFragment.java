package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.helper.GlobalConst;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ProfileFragment extends Fragment {

    View root;
    Activity activity;
    EditText edtName, edtEmail, edtMobile, edtAltMobile, edtAddress;
    Button btnUpdateProfile;
    TextView txtImageTitle, txtMobileNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.lyt_profile, container, false);
        activity = getActivity();
        setHasOptionsMenu(true);

        edtName = root.findViewById(R.id.edtname);
        edtEmail = root.findViewById(R.id.edtemail);
        edtMobile = root.findViewById(R.id.edtmobile);
        edtAltMobile = root.findViewById(R.id.edtaltmob);
        edtAddress = root.findViewById(R.id.edtaddress);
        btnUpdateProfile = root.findViewById(R.id.btnUpdateProfile);

        txtImageTitle = root.findViewById(R.id.image_title);
        txtMobileNo = root.findViewById(R.id.txtMobileNo);

        String a = String.valueOf(GlobalConst.Name.charAt(0));
        txtImageTitle.setText(a.toUpperCase());
        txtMobileNo.setText("Mob. " + GlobalConst.Mobile);

        edtName.setText(GlobalConst.Name);
        edtEmail.setText(GlobalConst.Username);
        edtMobile.setText(GlobalConst.Mobile);

        edtEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email, 0, 0, 0);
        edtMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone, 0, 0, 0);
        edtAltMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone, 0, 0, 0);
        edtAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_address, 0, 0, 0);
        edtName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_name, 0, 0, 0);


        if (GlobalConst.Address.equals("null")){
            edtAddress.setText("-");
        } else {
            edtAddress.setText(GlobalConst.Address);
        }


        return root;

    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalConst.TOOLBAR_TITLE = getString(R.string.title_profile);
        activity.invalidateOptionsMenu();
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
