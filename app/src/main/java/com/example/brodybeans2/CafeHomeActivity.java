package com.example.brodybeans2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CafeHomeActivity extends AppCompatActivity implements OrderAdapter.OnExpandListener {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;
    private ChildEventListener mChildEventListener;
    private OrderAdapter orderAdapter;
    private OrderItemAdapter orderItemAdapter;
    private ArrayList<Order> orderList;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_home);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("orders");

        orderList = new ArrayList<>();

        buildRecyclerView();

        signOut = findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("Children Num",String.valueOf(dataSnapshot.getChildrenCount()));
                Order order = new Order();
                ArrayList<OrderItem> items = new ArrayList<>();

                GenericTypeIndicator<ArrayList<OrderItem>> map = new GenericTypeIndicator<ArrayList<OrderItem>>(){};
                items = dataSnapshot.child("order").getValue(map);

                /*for (int i = 0; i < map.size(); i++) {
                    OrderItem o = new OrderItem((String) map.get(String.valueOf(i)));
                    items.add(o);
                }*/

                Integer num = dataSnapshot.child("orderNumber").getValue(Integer.class);
                String email = dataSnapshot.child("email").getValue(String.class);

                Log.d("User",email);

                order.setEmail(email);
                order.setOrderNumber(num);
                order.setOrder(items);

                orderList.add(order);

                orderAdapter.notifyDataSetChanged();
                Log.d("Array size", String.valueOf(orderList.size()));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        mOrdersDatabaseReference.addChildEventListener(mChildEventListener);
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.order_list);

        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        orderAdapter = new OrderAdapter(orderList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(orderAdapter);

    }

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_signout]
        finish();
    }

    @Override
    public void onExpandClick(int position, View view, boolean open) {
        mRecyclerView = view.findViewById(R.id.more_view);

        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        if (!open) {
            orderItemAdapter = new OrderItemAdapter(orderList.get(position).getOrder());
            Log.d("IS IT OPEN?", "NO");
        } else {
            orderItemAdapter = new OrderItemAdapter(new ArrayList<OrderItem>());
            Log.d("IS IT OPEN?", "YES");
        }

        Toast.makeText(CafeHomeActivity.this, "Go to cart", Toast.LENGTH_LONG).show();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(orderItemAdapter);

        orderItemAdapter.notifyDataSetChanged();
    }
}
