package com.daffodils.psychiatry.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.model.VideosGetterSetter;

import java.util.List;

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
    }

    @Override
    public int getItemCount() {
        return videos_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView course, module, video, path;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            course = itemView.findViewById(R.id.tvCourseName);
            module = itemView.findViewById(R.id.tvModName);
            video = itemView.findViewById(R.id.tvVideoName);
            path = itemView.findViewById(R.id.image_title);

        }
    }
}


