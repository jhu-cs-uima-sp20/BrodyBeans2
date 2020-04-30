package com.example.brodybeans2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mOrdersDatabaseReference;
    private ChildEventListener mChildEventListener;
    private String email;
    private String userId;
    private Integer orderNum;
    private boolean orderInProg = false;

    private TextView orderNumberText;
    private TextView thanksText;
    private TextView orderPlacedText;
    private TextView orderNumberMessage;
    private TextView notifyMessage;
    private String mainKey;

    private final String CHANNEL_ID = "my channel id";

    private DataSnapshot datasnap;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        orderNumberText = (TextView) findViewById(R.id.order_num);
        thanksText = (TextView) findViewById(R.id.thanks_txt);
        orderPlacedText = (TextView) findViewById(R.id.order_placed_msg);
        orderNumberMessage = (TextView) findViewById(R.id.order_num_msg);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mOrdersDatabaseReference = mFirebaseDatabase.getReference().child("orders");

        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        orderNum = 0;
        orderInProg = false;
        mainKey = "";

        cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datasnap != null) {
                    if (!datasnap.child("progressStatus").getValue(Boolean.class) || !datasnap.child("paid").getValue(Boolean.class)) {
                        datasnap.getRef().removeValue();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Sorry. Your order cannot be deleted once it is in progress", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });


        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.child("email").getValue().equals(email)) {
                    datasnap = dataSnapshot;
                    orderNum = dataSnapshot.child("orderNumber").getValue(Integer.class);
                    orderNumberText.setText(orderNum.toString());
                    orderInProg = true;
                    if (dataSnapshot.child("progressStatus").equals("false")) {
                        cancel.setVisibility(View.VISIBLE);
                    }
                    else {
                        cancel.setVisibility(View.INVISIBLE);
                        orderPlacedText.setText("Your order is being prepared!");
                    }
                    ListView listView = findViewById(R.id.more_view);
                    ArrayList<OrderItem> items = new ArrayList<>();

                    GenericTypeIndicator<ArrayList<OrderItem>> map = new GenericTypeIndicator<ArrayList<OrderItem>>() {
                    };
                    items = dataSnapshot.child("order").getValue(map);

                    ArrayList<String> arrayList = new ArrayList<>();

                    for (OrderItem oi : items) {
                        arrayList.add(oi.getItem());
                    }


                    ViewGroup.LayoutParams params = listView.getLayoutParams();
                    if (arrayList != null) {
                        params.height = 135 * arrayList.size();
                    }
                    listView.setLayoutParams(params);
                    listView.requestLayout();
                    ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, arrayList);
                    listView.setAdapter(arrayAdapter);

                    orderNumberMessage.setVisibility(View.VISIBLE);
                    orderNumberText.setVisibility(View.VISIBLE);
                    findViewById(R.id.more_view).setVisibility(View.VISIBLE);
                }

                PreferenceManager.getDefaultSharedPreferences(OrdersActivity.this).edit().putString("status", Boolean.toString(orderInProg)).apply();

                if (orderInProg) {
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        getSupportActionBar().setHomeButtonEnabled(false);
                        //change visibility of notify message on screen
                        //notifyMessage.setVisibility(View.INVISIBLE);
                        orderPlacedText.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setHomeButtonEnabled(true);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //orderNum =  dataSnapshot.getValue(Integer.class);

                //change visibility of notify message on screen
                //notifyMessage.setVisibility(View.VISIBLE);
                //orderPlacedText.setVisibility(View.INVISIBLE);


                //notify
                //int i = Integer.parseInt(token.replaceAll("[\\D]", ""));
                if (dataSnapshot.child("email").getValue().equals(email)) {
                    datasnap = dataSnapshot;
                    String key = dataSnapshot.getKey();
                    orderPlacedText.setText("Your order is being prepared!");
                    if (dataSnapshot.child("paid").getValue(Boolean.class)) {
                        orderInProg = false;
                        PreferenceManager.getDefaultSharedPreferences(OrdersActivity.this).edit().putString("status", Boolean.toString(orderInProg)).apply();
                        //thanksText.setVisibility(View.INVISIBLE)
                        orderNumberMessage.setVisibility(View.INVISIBLE);
                        orderNumberText.setVisibility(View.INVISIBLE);
                        findViewById(R.id.more_view).setVisibility(View.INVISIBLE);
                        orderPlacedText.setText("Click the back button to place a new one!");
                        orderPlacedText.setTextColor(getResources().getColor(R.color.colorPrimary));
                        thanksText.setText("Your order has been paid. \n\n");
                        orderNum = 0;
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            getSupportActionBar().setHomeButtonEnabled(true);
                        }
                    } else if (dataSnapshot.child("progressStatus").equals("false")){
                        onChildAdded(dataSnapshot, "");
                        //mOrdersDatabaseReference.child(key).child("progressStatus").setValue("false");
                    }
                    if (dataSnapshot.child("progressStatus").getValue().equals("true") && !dataSnapshot.child("paid").getValue(Boolean.class)){
                        cancel.setVisibility(View.INVISIBLE);
                        String message = "Your order is being prepared!";
                        String title = "Brody Beans";
                        //mainKey = (String)dataSnapshot.getValue();
                        Uri defSoundsUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.beans_logo_background)
                                .setContentTitle(title)
                                .setContentText(message)
                                .setSound(defSoundsUri)
                                .setAutoCancel(true);

                        //Message m = Message.builder().putData().setToken().build();


                        Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("message", message);

                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(pendingIntent);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            //String channelId = "Your_channel_id";
                            int importance = NotificationManager.IMPORTANCE_DEFAULT;
                            NotificationChannel channel = new NotificationChannel(
                                    CHANNEL_ID,
                                    "Channel human readable title",
                                    importance);
                            channel.enableLights(true);
                            channel.enableVibration(true);
                            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);


                            notificationManager.createNotificationChannel(channel);
                            builder.setChannelId(CHANNEL_ID);

                        }

                        notificationManager.notify(0, builder.build());
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (email.equals(dataSnapshot.child("email").getValue(String.class))) {
                    datasnap = dataSnapshot;
                    orderInProg = false;
                    //thanksText.setVisibility(View.INVISIBLE)
                    orderNumberMessage.setVisibility(View.INVISIBLE);
                    orderNumberText.setVisibility(View.INVISIBLE);
                    orderPlacedText.setVisibility(View.VISIBLE);
                    //notifyMessage.setVisibility(View.INVISIBLE);
                    orderPlacedText.setText("Click the back button to place a new one!");
                    orderPlacedText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    thanksText.setText("Your order has been cancelled. \n\n");
                    orderNum = 0;
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setHomeButtonEnabled(true);
                    }
                    cancel.setVisibility(View.INVISIBLE);
                    findViewById(R.id.more_view).setVisibility(View.INVISIBLE);

                }
                PreferenceManager.getDefaultSharedPreferences(OrdersActivity.this).edit().putString("status", Boolean.toString(orderInProg)).apply();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mOrdersDatabaseReference.addChildEventListener(mChildEventListener); //what does this do

        if (orderNum == 0) {
            onEmptyOrderList();
        }

    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    }

     */

    public void onEmptyOrderList() {
        PreferenceManager.getDefaultSharedPreferences(OrdersActivity.this).edit().putString("status", Boolean.toString(orderInProg)).apply();
    }


    @Override
    public void onBackPressed() {
        if (!orderInProg) {
            super.onBackPressed();
        }
        else {
            //do nothing
            Toast toast = Toast.makeText(getApplicationContext(), "Cannot make new order while order is in progress", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
