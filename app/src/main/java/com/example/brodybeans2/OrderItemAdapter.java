package com.example.brodybeans2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemHolder> {

    private ArrayList<OrderItem> mItemList;


    public class OrderItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView orderTextView;
        public ImageButton deleteItem;

        public OrderItemHolder(View itemView) {
            super(itemView);
            orderTextView = itemView.findViewById(R.id.itemTextView);
            deleteItem = itemView.findViewById(R.id.deleteItem);
            deleteItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            delete(getAdapterPosition()); //calls the method above to delete
            Toast.makeText(v.getContext(), "Delete Button Clicked",Toast.LENGTH_SHORT).show();
        }

    }

    public OrderItemAdapter(ArrayList<OrderItem> itemList) {
        mItemList = itemList;
    }

    public void delete(int position) { //removes the row
        mItemList.remove(position);
        notifyItemRemoved(position);
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

