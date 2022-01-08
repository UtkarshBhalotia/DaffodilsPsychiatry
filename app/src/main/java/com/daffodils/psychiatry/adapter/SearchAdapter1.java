package com.daffodils.psychiatry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.daffodils.psychiatry.R;

import java.util.ArrayList;

import static com.daffodils.psychiatry.activity.RegisterActivity.date_array;
import static com.daffodils.psychiatry.activity.RegisterActivity.m_listCourse;
import static com.daffodils.psychiatry.activity.RegisterActivity.value_array;

public class SearchAdapter1 extends RecyclerView.Adapter<SearchAdapter1.MyViewHolder>{

    Context context;
    ArrayList<String> arrayList;


    public SearchAdapter1(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList=arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_adapter_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        String pickup_drop_location = arrayList.get(position);
        holder.airport_name_textView.setText(pickup_drop_location);

        holder.chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.chk.isChecked() == true){
                    date_array.add(arrayList.get(position));
                    value_array.add(m_listCourse.get(position).getId());

                } else {
                    date_array.remove(arrayList.get(position));
                    value_array.remove(m_listCourse.get(position).getId());
                }

            }
        });


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView airport_name_textView;
        CheckBox chk;

        public MyViewHolder(View itemView) {
            super(itemView);
            airport_name_textView = (TextView) itemView.findViewById(R.id.airport_name_textView);
            chk = (CheckBox) itemView.findViewById(R.id.chk);

        }
    }
}
