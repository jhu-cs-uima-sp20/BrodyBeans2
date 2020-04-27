package com.example.brodybeans2;


import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemHolder> {

    private ArrayList<OrderItem> mItemList;


    public class OrderItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView orderTextView;
        public TextView extraOrderInfo;
        public ImageButton deleteItem;

        public OrderItemHolder(View itemView) {
            super(itemView);
            orderTextView = itemView.findViewById(R.id.itemTextView);
            extraOrderInfo = itemView.findViewById(R.id.optionsTextView);
            deleteItem = itemView.findViewById(R.id.deleteItem);
            deleteItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            delete(getAdapterPosition(),v); //calls the method below to delete item from the cart
        }
    }

    public OrderItemAdapter(ArrayList<OrderItem> itemList) {
        mItemList = itemList;
    }

    public void delete(final int position, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext(), R.style.AlertDialogTheme); //!!!!!!!
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to remove this item from the cart?");
        builder.setPositiveButton("YES, REMOVE FROM CART", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mItemList.remove(position);
                notifyItemRemoved(position);
            }
        });
        builder.setNegativeButton("NO", null);
        AlertDialog removeItemAlert = builder.create();
        removeItemAlert.show();

        /*mItemList.remove(position);
        notifyItemRemoved(position); */
    }

    @Override
    public OrderItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        OrderItemHolder oih = new OrderItemHolder(v);
        return oih;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.OrderItemHolder holder, int position) {
        String extra;
        OrderItem currItem = mItemList.get(position);
        holder.orderTextView.setText(currItem.getItem());
        if (currItem.getSize() == null || currItem.getTemp() == null) {
            extra = " ";
        } else {
            extra = currItem.getSize() + currItem.getTemp();
        }
        holder.extraOrderInfo.setText(extra);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }


}

