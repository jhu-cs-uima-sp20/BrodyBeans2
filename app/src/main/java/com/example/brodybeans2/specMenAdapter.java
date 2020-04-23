package com.example.brodybeans2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class specMenAdapter extends RecyclerView.Adapter<specMenAdapter.specMenHolder> {

    // need to add items to the list
        private ArrayList<String> specMenItemList;


        public class specMenHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public TextView specMenTextView;
            private Context context;

            public specMenHolder(View itemView) {
                super(itemView);
                specMenTextView = itemView.findViewById(R.id.specMenTextView);
                //clickItem = itemView.findViewById(R.id.deleteItem);
                specMenTextView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //context = getApplicationContext();
                //delete(getAdapterPosition()); //calls the method above to delete

                //SharedPreferences app_preferences
                String menItem = (String) specMenTextView.getText();
                System.out.println("the item I want to add to cart is" + menItem);
                editor.putString("item", menItem);
                //PreferenceManager.getDefaultSharedPreferences(context).edit().putString("item", menItem ).apply();
                Toast.makeText(v.getContext(), menItem,Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(v.getContext(), CartActivity.class);
                //startActivity(intent);
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.spec_men_item, parent, false);
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


