package com.daffodils.psychiatry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.helper.CommonFunctions;
import com.daffodils.psychiatry.model.ValidityDetailsGetterSetter;

import java.util.List;

public class ValidityDetailsAdapter extends RecyclerView.Adapter<ValidityDetailsAdapter.MyViewHolder> {

    Context context;
    List<ValidityDetailsGetterSetter> order_list;
    CommonFunctions cf = new CommonFunctions();

    public ValidityDetailsAdapter(Context context, List<ValidityDetailsGetterSetter> order_list) {
        this.context = context;
        this.order_list = order_list;
    }

    @NonNull
    @Override
    public ValidityDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_validity_details, parent, false);
        return new ValidityDetailsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ValidityDetailsAdapter.MyViewHolder myViewHolder, int i) {


        if (order_list.get(i).getCourseName().equals("")) {
            myViewHolder.coursename.setText("");

        } else {
            myViewHolder.coursename.setText(order_list.get(i).getCourseName());
        }

        if (order_list.get(i).getModuleName().equals("")) {
            myViewHolder.modulename.setText("");

        } else {
            myViewHolder.modulename.setText(order_list.get(i).getModuleName());

        }
        if (order_list.get(i).getStartDate().equals("")) {
            myViewHolder.startDate.setText("");

        } else {
            myViewHolder.startDate.setText(order_list.get(i).getStartDate());

        }
        if (order_list.get(i).getEndDate().equals("")) {
            myViewHolder.endDate.setText("");

        } else {
            myViewHolder.endDate.setText(order_list.get(i).getEndDate());

        }
        if (order_list.get(i).getDaysLeft().equals("")) {
            myViewHolder.daysLeft.setText("");

        } else {
            myViewHolder.daysLeft.setText(order_list.get(i).getDaysLeft());

        }
    }

    @Override
    public int getItemCount() {
        return order_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView modulename, coursename, startDate, endDate, daysLeft;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            modulename = itemView.findViewById(R.id.txtModule);
            coursename = itemView.findViewById(R.id.txtCourse);
            startDate = itemView.findViewById(R.id.txtStart);
            endDate = itemView.findViewById(R.id.txtEnd);
            daysLeft = itemView.findViewById(R.id.txtDays);
        }
    }

}
