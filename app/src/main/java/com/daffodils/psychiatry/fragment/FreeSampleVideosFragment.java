package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.activity.MainActivity;
import com.daffodils.psychiatry.adapter.VideosAdapter;
import com.daffodils.psychiatry.helper.ApiConfig;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.helper.VolleyCallback;
import com.daffodils.psychiatry.model.VideosGetterSetter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class FreeSampleVideosFragment extends Fragment {

    View root;
    Context context;
    Activity activity;
    RecyclerView recyclerView;
    VideosAdapter videosAdapter;
    public List<String> m_videoPath = new ArrayList<>();
    public static List<VideosGetterSetter> videos_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.lyt_sample_videos, container, false);
        recyclerView = root.findViewById(R.id.recycler_videos);
        context = getContext();
        activity = getActivity();
        setHasOptionsMenu(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            Window window = getActivity().getWindow();
            WindowManager wm = getActivity().getWindowManager();
            wm.removeViewImmediate(window.getDecorView());
            wm.addView(window.getDecorView(), window.getAttributes());

        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new MainActivity.RecyclerTouchListener(getContext(), recyclerView, new MainActivity.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                String value = m_videoPath.get(position);
                Fragment fragment = new VideoPlayer();
                Bundle bundle = new Bundle();
                bundle.putString("VideoURL", "https://daffodilspsychiatry.com/"+ value);
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getAllFreeVideosList();

        return root;
    }

    public void getAllFreeVideosList(){

        videos_list.clear();
        if (AppController.isConnected(activity)) {

            Map<String, String> params = new HashMap<String, String>();
            params.put("SC", GlobalConst.SC_GET_SAMPLE_VIDEOS);

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
                                    GlobalConst.VIDEO_TYPE = "Sample Video";
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
