package com.example.brodybeans2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PaidActivity extends AppCompatActivity implements OrderAdapter.OnExpandListener, NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;
    private ChildEventListener mChildEventListener;
    private OrderAdapter orderAdapter;
    private OrderItemAdapter orderItemAdapter;
    private ArrayList<Order> orderList;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("orders");

        orderList = new ArrayList<>();

        buildRecyclerView();

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.child("paid").getValue(Boolean.class)) {
                    Log.d("Children Num", String.valueOf(dataSnapshot.getChildrenCount()));
                    Order order = new Order();
                    ArrayList<OrderItem> items = new ArrayList<>();

                    GenericTypeIndicator<ArrayList<OrderItem>> map = new GenericTypeIndicator<ArrayList<OrderItem>>() {
                    };
                    items = dataSnapshot.child("order").getValue(map);

                    Integer num = dataSnapshot.child("orderNumber").getValue(Integer.class);
                    String email = dataSnapshot.child("email").getValue(String.class);

                    order.setEmail(email);
                    order.setOrderNumber(num);
                    order.setOrder(items);
                    order.setPaid(true);

                    orderList.add(order);

                    orderAdapter.notifyDataSetChanged();
                    Log.d("Array size", String.valueOf(orderList.size()));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.child("paid").getValue(Boolean.class)) {
                    Log.d("Children Num", String.valueOf(dataSnapshot.getChildrenCount()));
                    Order order = new Order();
                    ArrayList<OrderItem> items = new ArrayList<>();

                    GenericTypeIndicator<ArrayList<OrderItem>> map = new GenericTypeIndicator<ArrayList<OrderItem>>() {
                    };
                    items = dataSnapshot.child("order").getValue(map);

                    Integer num = dataSnapshot.child("orderNumber").getValue(Integer.class);
                    String email = dataSnapshot.child("email").getValue(String.class);

                    order.setEmail(email);
                    order.setOrderNumber(num);
                    order.setOrder(items);
                    order.setPaid(true);

                    orderList.add(order);

                    orderAdapter.notifyDataSetChanged();
                    Log.d("Array size", String.valueOf(orderList.size()));
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        mOrdersDatabaseReference.addChildEventListener(mChildEventListener);

        //ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        //itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.order_list);

        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        orderAdapter = new OrderAdapter(orderList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(orderAdapter);

    }
    /**Swipe to un pay order feature not implemented**/
    /**ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }


        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    Order o = orderList.remove(position);
                    orderAdapter.notifyItemRemoved(position);
                    mOrdersDatabaseReference.orderByChild("email").equalTo(o.getEmail()).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                ds.getRef().child("paid").setValue(false);
                                ds.getRef().child("progressStatus").setValue("false");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    break;
                case ItemTouchHelper.RIGHT:
                    break;
            }
        }
    };**/

    @Override
    public void onExpandClick(int position, View view, boolean open) {
        //mRecyclerView = view.findViewById(R.id.more_view);
        ListView listView = view.findViewById(R.id.more_view);
        ArrayList<String> arrayList = new ArrayList<>();

        for (OrderItem oi : orderList.get(position).getOrder()) {
            arrayList.add(oi.getCategory());
        }


        //mRecyclerView.setHasFixedSize(true);
        //mLayoutManager = new LinearLayoutManager(this);

        if (!open) {
            //orderItemAdapter = new OrderItemAdapter(orderList.get(position).getOrder());
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = 135 * arrayList.size();
            listView.setLayoutParams(params);
            listView.requestLayout();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, arrayList);
            listView.setAdapter(arrayAdapter);
            Log.d("IS IT OPEN?", "NO");
        } else {
            //orderItemAdapter = new OrderItemAdapter(new ArrayList<OrderItem>());
            arrayList.clear();
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            listView.setLayoutParams(params);
            listView.requestLayout();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, arrayList);
            listView.setAdapter(arrayAdapter);
            Log.d("IS IT OPEN?", "YES");
        }

        //mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.setAdapter(orderItemAdapter);

        //orderItemAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                Intent intent = new Intent(this,CafeHomeActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_paid:
                //Intent intent = new Intent(this,PaidActivity.class);
                //startActivity(intent);
                break;
            case R.id.action_settings:
                signOut();
                break;
        }
        return true;
    }

    @Override
    public void onCheckboxClick(int position, View view, boolean clicked) {
        //do something
        //Toast.makeText(view.getContext(), "Testing",Toast.LENGTH_LONG).show();
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(PaidActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
