package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.fragment.app.Fragment;
import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.helper.AppController;

public class VideoPlayer extends Fragment {

    View root;
    Context context;
    Activity activity;
    Bundle bundle;
    String videoUrl ="";
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.lyt_video_player, container, false);
        context = getContext();
        activity = getActivity();
        progressBar = new ProgressBar(context);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            videoUrl = bundle.getString("VideoURL");
        }

        setHasOptionsMenu(true);

        if (AppController.isConnected(activity)){
            progressBar.setVisibility(View.VISIBLE);
            VideoView videoView = root.findViewById(R.id.videoView);
            Uri uri = Uri.parse(videoUrl);
            videoView.setVideoURI(uri);
            MediaController mediaController = new MediaController(getContext());
            mediaController.setAnchorView(videoView);
            mediaController.setMediaPlayer(videoView);
            videoView.setMediaController(mediaController);
            videoView.start();
            progressBar.setVisibility(View.GONE);
        } else {
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
        }



        return root;
    }
}
