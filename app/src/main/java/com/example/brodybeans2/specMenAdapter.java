package com.example.brodybeans2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.content.ContextCompat;
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
    private Context cxt;
    private Intent intent;


        public class specMenHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView specMenTextView;
        public ImageButton starBtn;
        public boolean isFavorite = false;
        public ImageView imgView;


        public specMenHolder(final View itemView) {
            super(itemView);
            specMenTextView = itemView.findViewById(R.id.specMenTextView);
            specMenTextView.setOnClickListener(this);
            starBtn = itemView.findViewById(R.id.starItem);

            starBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFavorite) {
                        // click the star button of a favorite item to remove it
                        starBtn.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),android.R.drawable.btn_star_big_off));
                        for (int i = 0; i < 4; i++) {
                            if (PreferenceManager.getDefaultSharedPreferences(cxt).getString("fav_" + i, "N/A").equals((String) specMenTextView.getText())) {
                                PreferenceManager.getDefaultSharedPreferences(cxt).edit().putString("fav_"+i, "N/A").apply();
                            }
                        }
                        isFavorite = false;
                    } else {
                        // click the star button of a non-favorite item to add it
                        boolean hasSpace = false;
                        for (int i = 0; i < 4; i++) {
                            if (PreferenceManager.getDefaultSharedPreferences(cxt).getString("fav_" + i, "N/A").equals("N/A")) {
                                PreferenceManager.getDefaultSharedPreferences(cxt).edit().putString("fav_"+i, (String) specMenTextView.getText()).apply();
                                hasSpace = true;
                                starBtn.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),android.R.drawable.btn_star_big_on));
                                isFavorite = true;
                                break;
                            }
                        }
                        if (!hasSpace) {
                            Toast.makeText(cxt,"Need to remove an old favorite item before adding a new one", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }


        @Override
        public void onClick(View v) {
            //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            //SharedPreferences.Editor editor = sharedPreferences.edit();
            //context = getApplicationContext();
            //delete(getAdapterPosition()); //calls the method above to delete
            //SharedPreferences app_preferences
            String menItem = (String) specMenTextView.getText();
            System.out.println("the item I want to add to cart is" + menItem);
            //editor.putString("item", menItem);
            PreferenceManager.getDefaultSharedPreferences(cxt).edit().putString("item", menItem ).apply();
            Toast.makeText(v.getContext(), menItem,Toast.LENGTH_SHORT).show();
            intent = new Intent(cxt, CartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            cxt.startActivity(intent);
        }

    }

    public specMenAdapter(ArrayList<String> specMenList, Context cxt) {
        specMenItemList = specMenList;
        this.cxt = cxt;
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
        // check if the item is a favorite
        for (int i = 0; i < 4; i++) {
            if (PreferenceManager.getDefaultSharedPreferences(cxt).getString("fav_" + i, "N/A").equals(currItem)) {
                holder.starBtn.setImageDrawable(ContextCompat.getDrawable(cxt,android.R.drawable.btn_star_big_on));
                holder.isFavorite = true;
            }
        }
    }

    @Override
    public int getItemCount() {
            return specMenItemList.size();
        }

}


