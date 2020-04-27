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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private final String CHANNEL_ID = "my channel id";


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



        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.child("email").getValue().equals(email)) {
                    orderNum = dataSnapshot.child("orderNumber").getValue(Integer.class);
                    orderNumberText.setText(orderNum.toString());
                    orderInProg = true;
                }

                PreferenceManager.getDefaultSharedPreferences(OrdersActivity.this).edit().putString("status", Boolean.toString(orderInProg)).apply();

                if (orderInProg) {
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        getSupportActionBar().setHomeButtonEnabled(false);
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

                //notify
                //int i = Integer.parseInt(token.replaceAll("[\\D]", ""));
                if (dataSnapshot.child("email").getValue().equals(email)) {
                    String message = "Your order is being prepared!";
                    String title = "Brody Beans";
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

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (email.equals(dataSnapshot.child("email").getValue(String.class))) {
                    orderInProg = false;
                    //thanksText.setVisibility(View.INVISIBLE)
                    orderNumberMessage.setVisibility(View.INVISIBLE);
                    orderNumberText.setVisibility(View.INVISIBLE);
                    orderPlacedText.setText("Click the back button to place a new one!");
                    orderPlacedText.setTextColor(getResources().getColor(R.color.colorPrimary));
                    thanksText.setText("Your order has been paid. \n\n");
                    orderNum = 0;
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setHomeButtonEnabled(true);
                    }
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
