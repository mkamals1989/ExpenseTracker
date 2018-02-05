package com.example.kamal.test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KAMAL on 2/5/2018.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private ArrayList<ExpenseModel> arrData;
    private Context mContext;
    private CallBack listener;

    public DataAdapter(Context mContext, ArrayList<ExpenseModel> arrData, CallBack listener) {
        this.arrData = arrData;
        this.mContext = mContext;
        this.listener = listener;
    }

    public void refreshData(ArrayList<ExpenseModel> arrData) {
        this.arrData = arrData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ExpenseModel expenseModel = arrData.get(position);
        holder.titleTv.setText(expenseModel.getTitle());
        holder.dateTv.setText(expenseModel.getDate());
        holder.amountTv.setText(expenseModel.getAmount());
        holder.categoryTv.setText(expenseModel.getCategory());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrData != null ? arrData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv, dateTv, amountTv, categoryTv;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.title);
            dateTv = itemView.findViewById(R.id.date);
            amountTv = itemView.findViewById(R.id.amount);
            categoryTv = itemView.findViewById(R.id.category);
        }
    }

    public interface CallBack {
        void onClick(int pos);
    }
}
