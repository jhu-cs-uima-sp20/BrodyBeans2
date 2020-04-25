package com.example.brodybeans2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
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

//        signOut = findViewById(R.id.sign_out);
//        signOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signOut();
//            }
//        });

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("Children Num",String.valueOf(dataSnapshot.getChildrenCount()));
                Order order = new Order();
                ArrayList<OrderItem> items = new ArrayList<>();

                GenericTypeIndicator<ArrayList<OrderItem>> map = new GenericTypeIndicator<ArrayList<OrderItem>>(){};
                items = dataSnapshot.child("order").getValue(map);

                Integer num = dataSnapshot.child("orderNumber").getValue(Integer.class);
                String email = dataSnapshot.child("email").getValue(String.class);

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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.order_list);

        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        orderAdapter = new OrderAdapter(orderList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(orderAdapter);

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
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
                                ds.getRef().removeValue();
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
    };

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(CafeHomeActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCheckboxClick(int position, View view, boolean clicked) {
        //do something
        Toast.makeText(view.getContext(), "Testing",Toast.LENGTH_LONG).show();
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cafe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
