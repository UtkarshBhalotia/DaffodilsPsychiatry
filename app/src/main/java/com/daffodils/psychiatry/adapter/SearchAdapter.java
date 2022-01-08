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

import static com.daffodils.psychiatry.activity.RegisterActivity.date1_array;
import static com.daffodils.psychiatry.activity.RegisterActivity.m_listModule;
import static com.daffodils.psychiatry.activity.RegisterActivity.value1_array;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{

    Context context;
    ArrayList<String> arrayList;


    public SearchAdapter(Context context, ArrayList<String> arrayList) {
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
                    date1_array.add(arrayList.get(position));
                    value1_array.add(m_listModule.get(position).getId());

                } else {
                    date1_array.remove(arrayList.get(position));
                    value1_array.remove(m_listModule.get(position).getId());
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
