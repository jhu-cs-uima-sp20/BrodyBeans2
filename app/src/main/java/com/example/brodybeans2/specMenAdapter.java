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
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class specMenAdapter extends RecyclerView.Adapter<specMenAdapter.specMenHolder> {

    // need to add items to the list
    private ArrayList<String> specMenItemList;
    private ArrayList<Integer> images;
    private Context cxt;
    private Intent intent;


        public class specMenHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView specMenTextView;
        public ImageView imgView;


        public specMenHolder(final View itemView) {
            super(itemView);
            specMenTextView = itemView.findViewById(R.id.specMenTextView);
            imgView = itemView.findViewById(R.id.item_image_view);
            specMenTextView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            //SharedPreferences.Editor editor = sharedPreferences.edit();
            //context = getApplicationContext();
            //delete(getAdapterPosition()); //calls the method above to delete
            //SharedPreferences app_preferences
            String menItem = (String) specMenTextView.getText();
            //System.out.println("the item I want to add to cart is" + menItem);
            //editor.putString("item", menItem);
            String cat = PreferenceManager.getDefaultSharedPreferences(cxt).getString("category", null);
            //System.out.println("the category preference on the specific menu is " + cat);
            PreferenceManager.getDefaultSharedPreferences(cxt).edit().putString("item", menItem ).apply();
            //get rid of toast
            //Toast.makeText(v.getContext(), menItem,Toast.LENGTH_SHORT).show();

            if (cat == "Breakfast") {
                intent = new Intent(cxt, CartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                cxt.startActivity(intent);
            } else {
                intent = new Intent(cxt, Customization.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                cxt.startActivity(intent);
            }
        }

    }

    public specMenAdapter(ArrayList<String> specMenList, ArrayList<Integer> images, Context cxt) {
        specMenItemList = specMenList;
        this.images = images;
        this.cxt = cxt;
    }

    @Override
    public specMenAdapter.specMenHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.spec_men_item, parent, false);
        specMenAdapter.specMenHolder oih = new specMenAdapter.specMenHolder(v);
        return oih;
    }

       // View v = LayoutInflater.from(viewGroup.getContext()).inflate(isViewWithCatalog ? R.layout.product_row_layout_list : R.layout.product_row_layout_grid, null);


    @Override
    public void onBindViewHolder(@NonNull specMenAdapter.specMenHolder holder, int position) {
        String currItem = specMenItemList.get(position);
        holder.specMenTextView.setText(currItem);
        holder.imgView.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
            return specMenItemList.size();
        }

}


