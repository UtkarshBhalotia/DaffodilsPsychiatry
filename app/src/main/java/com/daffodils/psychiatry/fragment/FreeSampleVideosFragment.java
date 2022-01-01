package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.daffodils.psychiatry.R;

public class FreeSampleVideosFragment extends Fragment {

    View root;
    Activity activity;
    RecyclerView recyclerView;
    String videoUrl = "https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.lyt_sample_videos, container, false);
        recyclerView = root.findViewById(R.id.recycler_videos);

        activity = getActivity();
        setHasOptionsMenu(true);

        VideoView videoView = root.findViewById(R.id.videoView);
        Uri uri = Uri.parse(videoUrl);
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();

        return root;
    }
}
