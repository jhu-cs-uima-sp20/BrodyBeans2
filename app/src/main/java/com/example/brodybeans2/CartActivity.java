package com.example.brodybeans2;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    //Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;
    private ChildEventListener mChildEventListener;


    private Button placeOrderBtn;
    Button addItem;
    private Context context;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<OrderItem> itemList;
    private static boolean listInitialized;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        context = getApplicationContext();


        createItemList();
        buildRecyclerView();
        insertItem();

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        placeOrderBtn = (Button) findViewById(R.id.place_order_btn);
        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderItem orderItem = new OrderItem("orderNumber");
            }
        });
        //code added to bring user back to menu category page when add another item btn clicked
        addItem = (Button) findViewById(R.id.new_order_btn);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MenuActivity.class);
                startActivity(intent);
            }
        });

        //initialize Firebase components
        mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("orders");


        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mOrdersDatabaseReference.addChildEventListener(mChildEventListener);
    }

    //REMOVED by Justin - initialization moved to buildRecyclerView()
    public void createItemList() {
        //itemList = new ArrayList<>();
        //itemList.add(new OrderItem("blended beverage"));
        //itemList.add(new OrderItem("item2"));
        //itemList.add(new OrderItem("item3"));

    }

    public void buildRecyclerView() {
        mRecyclerView =findViewById(R.id.cartList);
        if (!listInitialized) {
            itemList = new ArrayList<>();
            listInitialized = true;
        }
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new OrderItemAdapter(itemList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void insertItem() {
        String cat = PreferenceManager.getDefaultSharedPreferences(context).getString("category", "defaultStringIfNothingFound");
        itemList.add(new OrderItem(cat));
        mAdapter.notifyItemInserted(itemList.size() - 1);
    }

}
