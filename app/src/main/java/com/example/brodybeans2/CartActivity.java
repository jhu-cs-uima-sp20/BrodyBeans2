package com.example.brodybeans2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
    AlertDialog.Builder builder;

    private static Integer orderNumber;
    private String token;

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
        builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);

        placeOrderBtn = (Button) findViewById(R.id.place_order_btn);


        //****************************************
        //finds the token
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("idk", "getInstanceId failed", task.getException());
                    return;
                }

                //get instance ID token
                token = task.getResult().getToken();
            }
        });

        //****************************************


        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mFirebaseAuth.getCurrentUser();

                final Order order = new Order(itemList, user.getEmail(), orderNumber, token);

                if (itemList.isEmpty()) {
                    Toast.makeText(CartActivity.this, "Can't place an empty order!",
                            Toast.LENGTH_SHORT).show();
                }

                if (!itemList.isEmpty()) {
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure you want to place order now?");
                    builder.setPositiveButton("YES, Go Ahead", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
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
                    });
                    builder.setNegativeButton("NO", null);
                    AlertDialog placeOrderAlert = builder.create();
                    placeOrderAlert.show();
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
        //SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        //String cat = sharedPreferences.getString("item", "no item??");
        String cat = PreferenceManager.getDefaultSharedPreferences(context).getString("category", null);
        System.out.println("the category is registered on the menu is " + cat);
        String item = PreferenceManager.getDefaultSharedPreferences(context).getString("item", null);
        String temp = PreferenceManager.getDefaultSharedPreferences(context).getString("temp", null);
        String size = PreferenceManager.getDefaultSharedPreferences(context).getString("size", null);
        String custom = PreferenceManager.getDefaultSharedPreferences(context).getString("custom", null);

        if (cat != null) {
            if (cat == "Breakfast") {
                //System.out.println("registered that the category is breakfast");
                itemList.add(new OrderItem(item));

            } else {
                itemList.add(new OrderItem(cat, item, temp, size, custom));
            }
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
