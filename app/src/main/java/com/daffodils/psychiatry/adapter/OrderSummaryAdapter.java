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
import static com.daffodils.psychiatry.fragment.FoundationCourseFragment.*;
import static com.daffodils.psychiatry.fragment.CartFragment.*;
import com.daffodils.psychiatry.helper.ApiConfig;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.CommonFunctions;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.helper.VolleyCallback;
import com.daffodils.psychiatry.model.OrderSummaryGetterSetter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.MyViewHolder> {
    Context context;
    List<OrderSummaryGetterSetter> order_list;
    CommonFunctions cf = new CommonFunctions();

    public OrderSummaryAdapter(Context context, List<OrderSummaryGetterSetter> order_list) {
        this.context = context;
        this.order_list = order_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_order_summary_data, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFromCartService("0", GlobalConst.CrashCourse);
                getCartDetailsService();
            }
        });


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
        if (order_list.get(i).getAmount().equals("")) {
            myViewHolder.amount.setText("");

        } else {
            myViewHolder.amount.setText(order_list.get(i).getAmount());

        }
    }

    @Override
    public int getItemCount() {
        return order_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView modulename, coursename, amount, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            modulename = itemView.findViewById(R.id.tvModName);
            coursename = itemView.findViewById(R.id.tvCourseName);
            amount = itemView.findViewById(R.id.tvPrice);
            delete = itemView.findViewById(R.id.tvDelete);
        }
    }


}

