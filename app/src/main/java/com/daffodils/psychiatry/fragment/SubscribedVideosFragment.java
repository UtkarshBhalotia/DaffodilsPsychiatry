package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.activity.ExoPlayerActivity;
import com.daffodils.psychiatry.activity.MainActivity;
import com.daffodils.psychiatry.adapter.VideosAdapter;
import com.daffodils.psychiatry.helper.ApiConfig;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.helper.VolleyCallback;
import com.daffodils.psychiatry.model.VideosGetterSetter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SubscribedVideosFragment extends Fragment{

    View root;
    Context context;
    Activity activity;
    RecyclerView recyclerView;
    VideosAdapter videosAdapter;
    public static ProgressDialog mProgressDialog;
    public List<String> m_videoPath = new ArrayList<>();
    public static List<VideosGetterSetter> videos_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.lyt_sample_videos, container, false);
        recyclerView = root.findViewById(R.id.recycler_videos);
        context = getContext();
        activity = getActivity();
        setHasOptionsMenu(true);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new MainActivity.RecyclerTouchListener(getContext(), recyclerView, new MainActivity.ClickListener() {
            @Override
            public void onClick(View view, int position) {

               /* String value = m_videoPath.get(position);
                Intent i = new Intent(context, ExoPlayerActivity.class);
                i.putExtra("VideoURL", "https://daffodilspsychiatry.com/"+ value);
                startActivity(i);*/

             /*   Fragment fragment = new VideoPlayer();
                Bundle bundle = new Bundle();
                bundle.putString("VideoURL", "https://daffodilspsychiatry.com/"+ value);
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();*/

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getSubscribedModuleIDs();

        return root;
    }

    public void getSubscribedModuleIDs(){

        String webAddress = GlobalConst.URL;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, webAddress, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (GlobalConst.Result.equals("T")){
                    getAllSubscribedVideosList();
                } else {
                    Toast.makeText(context, "Error : "+ GlobalConst.Description, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Unable to connect to remote server", Toast.LENGTH_LONG).show();

            }

        }) {

            @Override
            protected Response parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));

                    GlobalConst.Result = response.headers.get("Result");
                    GlobalConst.Description = response.headers.get("Description");
                    GlobalConst.ModuleID = response.headers.get("Modules");

                    return Response.success(jsonString,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));

                }

            }

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("SC", GlobalConst.SC_GET_SUBSCRIBED_MODULES);
                params.put("UserID", GlobalConst.User_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }

  /*  public void getSubscribedModuleID(){
        if (AppController.isConnected(activity)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("SC", GlobalConst.SC_GET_SUBSCRIBED_MODULES);
            params.put("UserID", GlobalConst.User_id);

            GlobalConst.SERVICE_TYPE = GlobalConst.SC_GET_SUBSCRIBED_MODULES;

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {
                            if (GlobalConst.Result.equals("T")){
                                getAllSubscribedVideosList();
                            } else {
                                Toast.makeText(activity, "Error : " + GlobalConst.Description, Toast.LENGTH_LONG).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim() , params, true);

        }
    }*/

    public void getAllSubscribedVideosList(){

        videos_list.clear();
        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();
            params.put("SC", GlobalConst.SC_GET_COURSE_VIDEOS);
            params.put("ModuleID", GlobalConst.ModuleID);

            ApiConfig.RequestToVolley(new VolleyCallback() {
                @Override
                public void onSuccess(boolean result, String response) {
                    if (result) {
                        try {

                            if (GlobalConst.Result.equals("T")) {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject;

                                for(int i =0; i<jsonArray.length();i++){

                                    jsonObject = jsonArray.getJSONObject(i);
                                    String CourseName = jsonObject.getString("CourseName");
                                    String ModuleName = jsonObject.getString("ModuleName");
                                    String VideoName = jsonObject.getString("VideoName");
                                    String VideoPath= jsonObject.getString("VideoPath");

                                    VideosGetterSetter videosGetterSetter = new VideosGetterSetter(CourseName, ModuleName, VideoName, VideoPath);
                                    videos_list.add(videosGetterSetter);
                                    m_videoPath.add(VideoPath);
                                    GlobalConst.VIDEO_TYPE = "Subscribed Video";
                                    videosAdapter = new VideosAdapter(context, videos_list);
                                    recyclerView.setAdapter(videosAdapter);
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, activity, GlobalConst.URL.trim() , params, true);

        }
    }

    public static class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private String VideoName;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context, String VideoName) {
            this.context = context;
            this.VideoName = VideoName;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;

            String root = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + ".offlinemode"));
            File myDir = new File(root);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }

            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(myDir + File.separator + VideoName + ".mp4");
                //  output = new FileOutputStream("/sdcard/file_name.extension");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalConst.TOOLBAR_TITLE = getString(R.string.title_subs_vdo);
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
