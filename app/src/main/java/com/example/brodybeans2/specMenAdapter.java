package com.example.brodybeans2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import java.util.ArrayList;

public class specMenAdapter extends RecyclerView.Adapter<specMenAdapter.specMenHolder> {

        private ArrayList<String> specMenItemList;


        public class specMenHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public TextView specMenTextView;


            public specMenHolder(View itemView) {
                super(itemView);
                specMenTextView = itemView.findViewById(R.id.specMenTextView);
            }
            @Override
            public void onClick(View v) {
                //delete(getAdapterPosition()); //calls the method above to delete
            Toast.makeText(v.getContext(), "item clicked",Toast.LENGTH_SHORT).show();

            }


        }

        public specMenAdapter(ArrayList<String> specMenList) {
            specMenItemList = specMenList;
        }

        public void delete(int position) { //removes the row
            specMenItemList.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public specMenAdapter.specMenHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
            specMenAdapter.specMenHolder oih = new specMenAdapter.specMenHolder(v);
            return oih;
        }

        @Override
        public void onBindViewHolder(@NonNull specMenAdapter.specMenHolder holder, int position) {
            String currItem = specMenItemList.get(position);
            holder.specMenTextView.setText(currItem);
        }

        @Override
        public int getItemCount() {
            return specMenItemList.size();
        }


    }


