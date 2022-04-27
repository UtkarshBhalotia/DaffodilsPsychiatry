package com.daffodils.psychiatry.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.activity.ExoPlayerActivity;
import com.daffodils.psychiatry.fragment.SubscribedVideosFragment;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.model.VideosGetterSetter;

import java.util.List;

import static com.daffodils.psychiatry.fragment.SubscribedVideosFragment.mProgressDialog;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.MyViewHolder> {
    Context context;
    List<VideosGetterSetter> videos_list;


    public VideosAdapter(Context context, List<VideosGetterSetter> videos_list) {
        this.context = context;
        this.videos_list = videos_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_sample_vdo_data, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        if (GlobalConst.VIDEO_TYPE.equals("Sample Video")){
            myViewHolder.module.setVisibility(View.GONE);
            myViewHolder.lytSaveToOffline.setVisibility(View.GONE);
            myViewHolder.lytWatchVideo.setVisibility(View.GONE);
        } else {
            myViewHolder.module.setVisibility(View.VISIBLE);
            myViewHolder.lytSaveToOffline.setVisibility(View.VISIBLE );
            myViewHolder.lytWatchVideo.setVisibility(View.VISIBLE);
        }

        if (videos_list.get(i).getCourseName().equals("")) {
            myViewHolder.course.setText("");

        } else {
            myViewHolder.course.setText(videos_list.get(i).getCourseName());
        }

        if (videos_list.get(i).getModuleName().equals("")) {
            myViewHolder.module.setText("");

        } else {
            myViewHolder.module.setText(videos_list.get(i).getModuleName());

        }
        if (videos_list.get(i).getVideoName().equals("")) {
            myViewHolder.video.setText("");

        } else {
            myViewHolder.video.setText(videos_list.get(i).getVideoName());

        }

        if (videos_list.get(i).getVideoPath().equals("")) {
            myViewHolder.path.setText("");

        } else {
            myViewHolder.path.setText("");
        }

        myViewHolder.lytSaveToOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setMessage("Downloading Video. Plz do not cancel");
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(true);

                final SubscribedVideosFragment.DownloadTask downloadTask = new SubscribedVideosFragment.DownloadTask(context, videos_list.get(i).getVideoName().trim());
             //   downloadTask.execute("https://daffodilspsychiatry.com/Videos/Sample/2_0_Lecture%2024.mp4");
                downloadTask.execute("https://daffodilspsychiatry.com/" + videos_list.get(i).getVideoPath());

                mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        downloadTask.cancel(true); //cancel the task
                    }
                });
            }
        });

        myViewHolder.lytWatchVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(context, ExoPlayerActivity.class);
                ii.putExtra("VideoURL", "https://daffodilspsychiatry.com/"+ videos_list.get(i).getVideoPath());
                context.startActivity(ii);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView course, module, video, path;
        LinearLayout lytSaveToOffline, lytWatchVideo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            course = itemView.findViewById(R.id.tvCourseName);
            module = itemView.findViewById(R.id.tvModName);
            video = itemView.findViewById(R.id.tvVideoName);
            path = itemView.findViewById(R.id.image_title);
            lytSaveToOffline = itemView.findViewById(R.id.lytsave);
            lytWatchVideo = itemView.findViewById(R.id.lytWatchVideo);

        }
    }
}


