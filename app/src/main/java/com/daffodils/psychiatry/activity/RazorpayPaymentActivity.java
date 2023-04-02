package com.daffodils.psychiatry.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RazorpayPaymentActivity extends Activity implements PaymentResultListener {

    Context context;
    final Activity activity = this;
    Handler mHandler = new Handler();
    private static final String TAG = RazorpayPaymentActivity.class.getSimpleName();
    static String getRechargeAmt;
    int amount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        Checkout.preload(getApplicationContext());

        Intent oIntent = getIntent();
        getRechargeAmt = oIntent.getExtras().getString("RECHARGE_AMT");
        // rounding off the amount.
        amount = Math.round(Float.parseFloat(getRechargeAmt) * 100);


        startPayment();

    }

    public void startPayment() {

        final Activity activity = this;

        final Checkout co = new Checkout();
       // co.setKeyID("rzp_live_h8me54AFmh1HvG");
        co.setKeyID("rzp_live_lB2JitECy52a60");

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Daffodils Psychiatry");
            options.put("description", "Payment Details");
            options.put("send_sms_hash",true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", amount);

            JSONObject preFill = new JSONObject();
            preFill.put("email", GlobalConst.Username);
            preFill.put("contact", GlobalConst.Mobile);

            options.put("prefill", preFill);
            co.open(activity, options);

        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            mHandler.post(new Runnable() {
                public void run() {
                    mHandler = null;
                    String webAddress = GlobalConst.URL;

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, webAddress, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (GlobalConst.Result.equals("T")){
                                sendMegToUser();
                            } else {
                                Toast.makeText(context, "Error : "+ GlobalConst.Description, Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RazorpayPaymentActivity.this, "Unable to connect to remote server", Toast.LENGTH_LONG).show();

                        }

                    }) {


                        @Override
                        protected Response parseNetworkResponse(NetworkResponse response) {
                            try {


                                String jsonString = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers));

                                GlobalConst.Result = response.headers.get("Result");

                                return Response.success(jsonString,
                                        HttpHeaderParser.parseCacheHeaders(response));
                            } catch (UnsupportedEncodingException e) {
                                return Response.error(new ParseError(e));


                            }

                        }

                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("SC", GlobalConst.SC_SAVE_RECHARGE_DETAILS);
                            params.put("UserID", GlobalConst.User_id);
                            params.put("Amount", getRechargeAmt);
                            params.put("TransactionID", razorpayPaymentID);
                            params.put("Remarks", "RazorPay Payment Gateway");
                            params.put("AddByUserID", GlobalConst.User_id);

                            return params;
                        }
                    };


                    RequestQueue requestQueue = Volley.newRequestQueue(RazorpayPaymentActivity.this);
                    stringRequest.setShouldCache(false);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);


                }
            });
           // Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    public void sendMegToUser(){

        String message = "Welcome to Daffodils Family !! Enjoy your subscription. For any queries or clarifications contact" +
                " at +91-9872551972 / +91-7528920011. Happy Reading, Daffodils Psychiatry !!";

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

    public void sendMsgToAdmin() {

        String message = "New Subscription details : Name : " + GlobalConst.Name + " , Mobile No " + GlobalConst.Mobile + " , Recharge Amt " + getRechargeAmt +
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

                        Toast.makeText(activity, "You have successfully paid the amount.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }
}
