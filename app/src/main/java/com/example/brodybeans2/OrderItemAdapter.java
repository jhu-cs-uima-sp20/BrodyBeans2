package com.example.brodybeans2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemHolder> {

    private ArrayList<OrderItem> mItemList;

    public static class OrderItemHolder extends RecyclerView.ViewHolder {

        public TextView orderTextView;

        public OrderItemHolder(View itemView) {
            super(itemView);
            //TODO edit this
            orderTextView = itemView.findViewById(R.id.itemTextView);
        }
    }

    public OrderItemAdapter(ArrayList<OrderItem> itemList) {
        mItemList = itemList;
    }

    @Override
    public OrderItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        OrderItemHolder oih = new OrderItemHolder(v);
        return oih;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.OrderItemHolder holder, int position) {
        OrderItem currItem = mItemList.get(position);
        holder.orderTextView.setText(currItem.getCategory());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}

