package com.example.brodybeans2;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    //Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;
    private DatabaseReference nOrdersDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ChildEventListener nChildEventListener;


    private Button placeOrderBtn;
    Button addItem;
    private Context context;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<OrderItem> itemList;
    private static boolean listInitialized;
    private ImageButton deleteItem;

    private static Integer orderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        context = getApplicationContext();


        createItemList();
        buildRecyclerView();
        insertItem();

        //initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("orders");
        nOrdersDatabaseReference = mFirebaseDatabase.getReference().child("numTracker");
        //Log.d("Num Tracker", mOrdersDatabaseReference.toString());

        placeOrderBtn = (Button) findViewById(R.id.place_order_btn);
        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                Order order = new Order(itemList, user.getEmail(), orderNumber);

                if (itemList.isEmpty()) {
                    Toast.makeText(CartActivity.this, "Can't place an empty order!",
                            Toast.LENGTH_SHORT).show();
                }

                if (!itemList.isEmpty()) {
                    dbIncreaseOrderNumber();



                mOrdersDatabaseReference.push().setValue(order);

                Intent sendIntent = new Intent(getBaseContext(), OrdersActivity.class);
                // Verify that the intent will resolve to an activity
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendIntent);
                }

                itemList.clear();
                PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
                buildRecyclerView();

                }

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



        deleteItem = (ImageButton) findViewById(R.id.deleteItem);
         /*
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position = mAdapter.getAdapterPosition();
                removeAt(0);
            }
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
            }
        });
        deleteItem = (ImageButton) findViewById(R.id.deleteItem);
        deleteItem.setOnClickListener {
            public void onClick(@NonNull RecyclerView.ViewHolder viewHolder) {
                int position = orderItemHolder.getAdapterPosition();
                OrderItem o = itemList.remove(position);
                mAdapter.notifyItemRemoved(position);

            }
        });

 */




            nChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                orderNumber =  dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                orderNumber =  dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        nOrdersDatabaseReference.addChildEventListener(nChildEventListener);

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
        //orderItemAdapter = new OrderItemAdapter(itemList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void insertItem() {
        String cat = PreferenceManager.getDefaultSharedPreferences(context).getString("category", null);
        if (cat != null) {
            itemList.add(new OrderItem(cat));
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void dbIncreaseOrderNumber() {
        orderNumber++;
        nOrdersDatabaseReference.child("num").setValue(orderNumber);
    }

    private void removeAt(int position) {
        itemList.remove(position);
        mAdapter.notifyItemRemoved(position);
        //notifyItemRangeChanged(position, mDataSet.size());
    }
}
