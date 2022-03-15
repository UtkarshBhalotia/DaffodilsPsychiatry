package com.daffodils.psychiatry.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import android.os.Handler;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.fragment.PaymentDetailsFragment;
import com.daffodils.psychiatry.helper.ApiConfig;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.helper.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Math.abs;

public class PayMentGateWay extends Activity {

    private ArrayList<String> post_val = new ArrayList<String>();
    private String post_Data = "";
    WebView webView;
    final Activity activity = this;
    private String tag = "Daffodils Psychiatry";
    private String hash, hashSequence;
    ProgressDialog progressDialog;
    Context context;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    String merchant_key = "LyQlVt";
    String salt = "uA5M5VoI7dlfQ1TghcXGBvBWBHqyPu3f";

    String action1 = "";
    String base_url="https://secure.payu.in";//
    int error = 0;
    String hashString = "";
    Map<String, String> params;
    String txnid = "";

    String SUCCESS_URL = "https://www.payumoney.com/mobileapp/payumoney/success.php"; // failed
    String FAILED_URL = "https://www.payumoney.com/mobileapp/payumoney/failure.php";

    Handler mHandler = new Handler();
    static String getFirstName, getNumber, getEmailAddress, getRechargeAmt;

    ProgressDialog pDialog;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(activity);
        context = getApplicationContext();

        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        webView = new WebView(this);
        setContentView(webView);

        Intent oIntent = getIntent();

        getRechargeAmt = oIntent.getExtras().getString("RECHARGE_AMT");

        params = new HashMap<String, String>();
        params.put("key", merchant_key);

        params.put("amount", "1");
        params.put("firstname", GlobalConst.Name);
        params.put("email", GlobalConst.Username);
        params.put("phone", GlobalConst.Mobile);
        params.put("productinfo", "Recharge");
        params.put("surl", SUCCESS_URL);
        params.put("furl", FAILED_URL);
        params.put("service_provider", "payu_paisa");
        params.put("lastname", "");
        params.put("address1", "");
        params.put("address2", "");
        params.put("city", "");
        params.put("state", "");
        params.put("country", "");
        params.put("zipcode", "");
        params.put("udf1", "");
        params.put("udf2", "");
        params.put("udf3", "");
        params.put("udf4", "");
        params.put("udf5", "");
        params.put("pg", "");

        if (empty(params.get("txnid"))) {
            Random rand = new Random();
            String rndm = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
            txnid = hashCal("SHA-256", rndm).substring(0, 20);
            params.put("txnid", txnid);
        } else
            txnid = params.get("txnid");
        //String udf2 = txnid;
        String txn = "abcd";
        hash = "";
        String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";
        if (empty(params.get("hash")) && params.size() > 0) {
            if (empty(params.get("key"))
                    || empty(params.get("txnid"))
                    || empty(params.get("amount"))
                    || empty(params.get("firstname"))
                    || empty(params.get("email"))
                    || empty(params.get("phone"))
                    || empty(params.get("productinfo"))
                    || empty(params.get("surl"))
                    || empty(params.get("furl"))
                    || empty(params.get("service_provider"))

            ) {
                error = 1;
            } else {
                String[] hashVarSeq = hashSequence.split("\\|");

                for (String part : hashVarSeq) {
                    hashString = (empty(params.get(part))) ? hashString.concat("") : hashString.concat(params.get(part));
                    hashString = hashString.concat("|");
                }
                hashString = hashString.concat(salt);


                hash = hashCal("SHA-512", hashString);
                action1 = base_url.concat("/_payment");
            }
        } else if (!empty(params.get("hash"))) {
            hash = params.get("hash");
            action1 = base_url.concat("/_payment");
        }

        webView.setWebViewClient(new MyWebViewClient() {

            public void onPageFinished(WebView view, final String url) {
                progressDialog.dismiss();
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //make sure dialog is showing
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }


			/*@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "SslError! " +  error, Toast.LENGTH_SHORT).show();
				 handler.proceed();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, "Page Started! " + url, Toast.LENGTH_SHORT).show();
				if(url.startsWith(SUCCESS_URL)){
					Toast

					.makeText(activity, "Payment Successful! " + url, Toast.LENGTH_SHORT).show();
					 Intent intent = new Intent(PayMentGateWay.this, MainActivity.class);
					    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
					    startActivity(intent);
					    finish();
					    return false;
				}else if(url.startsWith(FAILED_URL)){
					Toast.makeText(activity, "Payment Failed! " + url, Toast.LENGTH_SHORT).show();
				    return false;
				}else if(url.startsWith("http")){
					return true;
				}
				//return super.shouldOverrideUrlLoading(view, url);
				return false;
			}*/


        });


        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);

        //webView.addJavascriptInterface(new PayUJavaScriptInterface(getApplicationContext()), "PayUMoney");
        webView.addJavascriptInterface(new PayUJavaScriptInterface(), "PayUMoney");
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("key", merchant_key);
        mapParams.put("hash", PayMentGateWay.this.hash);
        mapParams.put("txnid", (empty(PayMentGateWay.this.params.get("txnid"))) ? "" : PayMentGateWay.this.params.get("txnid"));
        Log.d(tag, "txnid: " + PayMentGateWay.this.params.get("txnid"));
        mapParams.put("service_provider", "payu_paisa");

        mapParams.put("amount", (empty(PayMentGateWay.this.params.get("amount"))) ? "" : PayMentGateWay.this.params.get("amount"));
        mapParams.put("firstname", (empty(PayMentGateWay.this.params.get("firstname"))) ? "" : PayMentGateWay.this.params.get("firstname"));
        mapParams.put("email", (empty(PayMentGateWay.this.params.get("email"))) ? "" : PayMentGateWay.this.params.get("email"));
        mapParams.put("phone", (empty(PayMentGateWay.this.params.get("phone"))) ? "" : PayMentGateWay.this.params.get("phone"));

        mapParams.put("productinfo", (empty(PayMentGateWay.this.params.get("productinfo"))) ? "" : PayMentGateWay.this.params.get("productinfo"));
        mapParams.put("surl", (empty(PayMentGateWay.this.params.get("surl"))) ? "" : PayMentGateWay.this.params.get("surl"));
        mapParams.put("furl", (empty(PayMentGateWay.this.params.get("furl"))) ? "" : PayMentGateWay.this.params.get("furl"));
        mapParams.put("lastname", (empty(PayMentGateWay.this.params.get("lastname"))) ? "" : PayMentGateWay.this.params.get("lastname"));

        mapParams.put("address1", (empty(PayMentGateWay.this.params.get("address1"))) ? "" : PayMentGateWay.this.params.get("address1"));
        mapParams.put("address2", (empty(PayMentGateWay.this.params.get("address2"))) ? "" : PayMentGateWay.this.params.get("address2"));
        mapParams.put("city", (empty(PayMentGateWay.this.params.get("city"))) ? "" : PayMentGateWay.this.params.get("city"));
        mapParams.put("state", (empty(PayMentGateWay.this.params.get("state"))) ? "" : PayMentGateWay.this.params.get("state"));

        mapParams.put("country", (empty(PayMentGateWay.this.params.get("country"))) ? "" : PayMentGateWay.this.params.get("country"));
        mapParams.put("zipcode", (empty(PayMentGateWay.this.params.get("zipcode"))) ? "" : PayMentGateWay.this.params.get("zipcode"));
        mapParams.put("udf1", (empty(PayMentGateWay.this.params.get("udf1"))) ? "" : PayMentGateWay.this.params.get("udf1"));
        mapParams.put("udf2", (empty(PayMentGateWay.this.params.get("udf2"))) ? "" : PayMentGateWay.this.params.get("udf2"));

        mapParams.put("udf3", (empty(PayMentGateWay.this.params.get("udf3"))) ? "" : PayMentGateWay.this.params.get("udf3"));
        mapParams.put("udf4", (empty(PayMentGateWay.this.params.get("udf4"))) ? "" : PayMentGateWay.this.params.get("udf4"));
        mapParams.put("udf5", (empty(PayMentGateWay.this.params.get("udf5"))) ? "" : PayMentGateWay.this.params.get("udf5"));
        mapParams.put("pg", (empty(PayMentGateWay.this.params.get("pg"))) ? "" : PayMentGateWay.this.params.get("pg"));
        webview_ClientPost(webView, action1, mapParams.entrySet());

    }

	/*public class PayUJavaScriptInterface {

		@JavascriptInterface
        public void success(long id, final String paymentId) {
            mHandler.post(new Runnable() {
                public void run() {
                    mHandler = null;

                    new PostRechargeData().execute();

            		Toast.makeText(getApplicationContext(),"Successfully payment\n redirect from PayUJavaScriptInterface" ,Toast.LENGTH_LONG).show();

                }
            });
        }
	}*/


    private final class PayUJavaScriptInterface {

        PayUJavaScriptInterface() {
        }

        @JavascriptInterface
        public void success(long id, final String paymentId) {

            Toast.makeText(getApplicationContext(), "Success payment" + paymentId, Toast.LENGTH_LONG).show();

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
                            Toast.makeText(PayMentGateWay.this, "Unable to connect to remote server", Toast.LENGTH_LONG).show();

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
                            params.put("Amount", "1");
                            params.put("TransactionID", txnid);
                            params.put("Remarks", "PayuMoney Payment Gateway");
                            params.put("AddByUserID", GlobalConst.User_id);

                            return params;
                        }
                    };


                    RequestQueue requestQueue = Volley.newRequestQueue(PayMentGateWay.this);
                    stringRequest.setShouldCache(false);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);


                }
            });
        }

        @JavascriptInterface
        public void failure(final String id, String error) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Cancel payment", Toast.LENGTH_LONG).show();
                }
            });
        }

        @JavascriptInterface
        public void failure() {
            failure("");
        }

        @JavascriptInterface
        public void failure(final String params) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Failed payment", Toast.LENGTH_LONG).show();
                }
            });
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


    public void webview_ClientPost(WebView webView, String url, Collection<Map.Entry<String, String>> postData) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>", url, "post"));
        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format("<input name='%s' type='hidden' value='%s' />", item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");
        Log.d(tag, "webview_ClientPost called");

        //setup and load the progress bar
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading. Please wait...");
        webView.loadData(sb.toString(), "text/html", "utf-8");
    }


    public void success(long id, final String paymentId) {

        mHandler.post(new Runnable() {
            public void run() {
                mHandler = null;

                Toast.makeText(getApplicationContext(), "Successfully payment\n redirect from Success Function", Toast.LENGTH_LONG).show();

            }
        });
    }


    public boolean empty(String s) {
        if (s == null || s.trim().equals(""))
            return true;
        else
            return false;
    }

    public String hashCal(String type, String str) {
        byte[] hashseq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();


            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) hexString.append("0");
                hexString.append(hex);
            }

        } catch (NoSuchAlgorithmException nsae) {
        }

        return hexString.toString();


    }



    //String SUCCESS_URL = "https://pay.in/sccussful" ; // failed
    //String FAILED_URL = "https://pay.in/failed" ;
    //override the override loading method for the webview client
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

        	/*if(url.contains("response.php") || url.equalsIgnoreCase(SUCCESS_URL)){

        		new PostRechargeData().execute();

        		Toast.makeText(getApplicationContext(),"Successfully payment\n redirect from webview" ,Toast.LENGTH_LONG).show();

                return false;
        	}else  */
            if (url.startsWith("http")) {
                //Toast.makeText(getApplicationContext(),url ,Toast.LENGTH_LONG).show();
                progressDialog.show();
                view.loadUrl(url);
                System.out.println("myresult " + url);
                //return true;
            } else {
                return false;
            }

            return true;
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
        //Write your code here
        // Toast.makeText(getApplicationContext(), "Back press disabled!", Toast.LENGTH_SHORT).show();
    }

/******************************************* send record to back end ******************************************/
    /*class PostRechargeData extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(PayMentGateWay.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }
        protected String doInBackground(String... args)
        {
            String strStatus = null;
            ProfileSessionManager ProSessionManager = new ProfileSessionManager(PayMentGateWay.this);

            String getUserid   = ProSessionManager.getSpeculatorId();
            String getSpeculationId  = "0";
            String rechargeAmt = getRechargeAmt;
            String postAction = "1";
            //http://speculometer.com/webService/stockApp/speculationMoneyreports.php?
            //access_token=ISOFTINCstockAppCheckDevelop&speculator=1&speculation=&amount=1000&action=1
            ServiceHandler sh = new ServiceHandler();
            String upLoadServerUri = ServiceList.payment_money_url+"speculator="+getUserid+"&speculation="+getSpeculationId+"&amount="+rechargeAmt+"&action="+postAction;

            try{
                String jsonStr = sh.makeServiceCall(upLoadServerUri, ServiceHandler.POST);
                JSONObject jsonObj  = new JSONObject(jsonStr);

                JSONObject jobjDoc = jsonObj.optJSONObject("document");
                JSONObject jobjRes = jobjDoc.optJSONObject("response");

                strStatus   = jobjRes.getString("status");
                //strMessage  = jobjRes.getString("message");
                String strUserId = jobjRes.getString("user_id");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return strStatus;
        }

        protected void onPostExecute(final String strStatus)
        {

            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    pDialog.dismiss();
                    if(strStatus != null && strStatus.equalsIgnoreCase("0")){
                        Toast.makeText(getApplicationContext(),"Your recharge amount not added in wallet." ,Toast.LENGTH_LONG).show();
                    }else if(strStatus != null && strStatus.equalsIgnoreCase("1")){

                        Toast.makeText(getApplicationContext(),"Your recharge amount added in wallet." ,Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(activity, MainActivity.class);
                    startActivity(intent);
                }
            });

        }
    }*/


/******************************************* closed send record to back end ************************************/
    public void insertIntoDatabase(){
        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();

            params.put("SC", GlobalConst.SC_SAVE_RECHARGE_DETAILS);
            params.put("UserID", GlobalConst.User_id);
            params.put("AddByUserID", GlobalConst.User_id);
            params.put("TransactionID", txnid);
            params.put("Amount", "1");
            params.put("Remarks", "PayUmoney Payment Gateway ");

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (GlobalConst.Result.equals("T")){
                                sendMegToUser();
                            } else {
                                Toast.makeText(context, "Error : "+ GlobalConst.Description, Toast.LENGTH_LONG).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim() , params, true);

        }
    }

    private void showAccountLedger() {

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
                Toast.makeText(PayMentGateWay.this, "Unable to connect to remote server", Toast.LENGTH_LONG).show();

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
                params.put("Amount", "1");
                params.put("TransactionID", txnid);
                params.put("Remarks", "PayuMoney Payment Gateway");
                params.put("AddByUserID", GlobalConst.User_id);

                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(PayMentGateWay.this);
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }



}