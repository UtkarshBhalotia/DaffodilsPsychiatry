package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.activity.ExoPlayerActivity;
import com.daffodils.psychiatry.activity.MainActivity;
import com.daffodils.psychiatry.adapter.OfflineVideoAdapter;
import com.daffodils.psychiatry.adapter.VideosAdapter;
import com.daffodils.psychiatry.helper.ApiConfig;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.helper.VolleyCallback;
import com.daffodils.psychiatry.model.VideosGetterSetter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class OfflineVideoFragment extends Fragment {

    View root;
    Context context;
    Activity activity;
    RecyclerView recyclerView;
    OfflineVideoAdapter videosAdapter;
    public List<String> m_offlinevideoPath = new ArrayList<>();
    public static List<VideosGetterSetter> offline_videos_list = new ArrayList<>();

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
            public void onClick(View view, int position) throws IOException {

                String value = m_offlinevideoPath.get(position);
                Intent i = new Intent(context, ExoPlayerActivity.class);
                i.putExtra("VideoURL", value);
                startActivity(i);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getAllVideosDaysLeft();
//        getAllVideosListInOfflineMode();

        return root;
    }

    public void getAllVideosListInOfflineMode(){

        offline_videos_list.clear();
        m_offlinevideoPath.clear();

        String path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + ".offlinemode"));
        Log.d("Files", "Path: " + path);

        File directory = new File(path);
        File[] files = directory.listFiles();

        Log.d("Files", "Size: "+ files.length);

        for (int i = 0; i < files.length; i++)
        {
            VideosGetterSetter videosGetterSetter = new VideosGetterSetter("CourseName", "ModuleName", files[i].getName(), "VideoPath");
            offline_videos_list.add(videosGetterSetter);
            m_offlinevideoPath.add(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + File.separator + ".offlinemode" + File.separator + files[i].getName())));
            GlobalConst.VIDEO_TYPE = "Sample Video";
            videosAdapter = new OfflineVideoAdapter(context, offline_videos_list);
            recyclerView.setAdapter(videosAdapter);
            Log.d("Files", "FileName:" + files[i].getName());
        }
    }

    public void getAllVideosDaysLeft(){

        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();
            params.put("SC", GlobalConst.SC_GET_SUBSCRIBED_VIDEO_DETAILS);
            params.put("UserID", GlobalConst.User_id);

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
                                    String VideoName = jsonObject.getString("VideoName");
                                    String DaysLeft = jsonObject.getString("DaysLeft");
                                }

                                getAllVideosListInOfflineMode();

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
        GlobalConst.TOOLBAR_TITLE = getString(R.string.title_sample_vdo);
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
