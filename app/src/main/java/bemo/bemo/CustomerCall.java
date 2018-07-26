package bemo.bemo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bemo.bemo.Common.Common;
import bemo.bemo.Model.ActiveOrder;
import bemo.bemo.Model.DataMessage;
import bemo.bemo.Model.FCMResponse;
import bemo.bemo.Model.Token;
import bemo.bemo.Remote.IFCMService;
import bemo.bemo.Remote.IGoogleAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerCall extends AppCompatActivity {

    TextView txtTime, txtAddress, txtDistance;
    CardView btnAccept, btnDecline;
    MediaPlayer mediaPlayer;
    IGoogleAPI mService;
    IFCMService mFCMService;
    String customerId, customerIds, tipe,biaya;
    double lat, lng;
    double latDestination, lngDestination;
    FirebaseDatabase database;
    DatabaseReference activeOrderRef;
    String pushId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_call);
        database = FirebaseDatabase.getInstance();
        activeOrderRef = database.getReference(Common.active_order);

        mService = Common.getGoogleAPI();
        mFCMService = Common.getFCMService();

        txtTime = (TextView) findViewById(R.id.txtTime);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtDistance = (TextView) findViewById(R.id.txtDistance);

        btnAccept = (CardView) findViewById(R.id.btn_accept);
        btnDecline = (CardView) findViewById(R.id.btn_decline);

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(customerId)) {
                    cancelBooking(customerId);
                }
            }
        });


        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        if (getIntent() != null) {
            lat = getIntent().getDoubleExtra("lat", -1.0);
            lng = getIntent().getDoubleExtra("lng", -1.0);
            latDestination = getIntent().getDoubleExtra("latDestination", -1.0);
            lngDestination = getIntent().getDoubleExtra("lngDestination", -1.0);
            customerId = getIntent().getStringExtra("customer");
            customerIds = getIntent().getStringExtra("customerId");
            tipe = getIntent().getStringExtra("tipe");
            biaya = getIntent().getStringExtra("biaya");
            getDirection(lat, lng);
        }

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushId = activeOrderRef.push().getKey();
                ActiveOrder activeOrder = new ActiveOrder();
                activeOrder.setCustomerId(customerIds);
                activeOrder.setLat(lat);
                activeOrder.setLng(lng);
                activeOrder.setLatDestination(latDestination);
                activeOrder.setLngDestination(lngDestination);
                activeOrder.setLngDestination(lngDestination);
                activeOrder.setTipe(tipe);
                activeOrder.setBiaya(biaya);
                activeOrder.setStatus("Pick Up the Passenger");
                Log.e("biaya", biaya);
                activeOrderRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(pushId)
                        .setValue(activeOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.e("success", "Success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.e("success", "Failed");
                    }
                });
                Log.e("pushid", pushId);
                Intent driverTracking = new Intent(CustomerCall.this, DriverTracking.class);
                acceptBooking(customerId);
                //sending value
                driverTracking.putExtra("lat", lat);
                driverTracking.putExtra("lng", lng);
                driverTracking.putExtra("latDestination", latDestination);
                driverTracking.putExtra("lngDestination", lngDestination);
                driverTracking.putExtra("customerId", customerId);
                driverTracking.putExtra("pushid", pushId);
                startActivity(driverTracking);
                finish();
            }
        });
    }

    private void acceptBooking(String customerId) {

        Token token = new Token(customerId);
//        Notification notification = new Notification("Notice!", "Driver has Cancelled your request");
//        Sender sender = new Sender(token.getToken(), notification);

        Map<String, String> content = new HashMap<>();
        content.put("title", "Accept");
        content.put("message", "You Found the Driver");
        content.put("driverId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        content.put("latDestination", String.valueOf(latDestination));
        content.put("lngDestination", String.valueOf(lngDestination));
        DataMessage dataMessage = new DataMessage(token.getToken(), content);

        mFCMService.sendMessage(dataMessage).enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                if (response.body().success == 1) {
                    Toast.makeText(CustomerCall.this, "Accepted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {

            }
        });

    }

    private void cancelBooking(String customerId) {
        Token token = new Token(customerId);
//        Notification notification = new Notification("Notice!", "Driver has Cancelled your request");
//        Sender sender = new Sender(token.getToken(), notification);

        Map<String, String> content = new HashMap<>();
        content.put("title", "Cancel");
        content.put("message", "Driver has Cancelled your request");
        DataMessage dataMessage = new DataMessage(token.getToken(), content);

        mFCMService.sendMessage(dataMessage).enqueue(new Callback<FCMResponse>() {
            @Override
            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                if (response.body().success == 1) {
                    Toast.makeText(CustomerCall.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<FCMResponse> call, Throwable t) {

            }
        });
    }

    private void getDirection(double lat, double lng) {


        String requestAPI = null;
        try {
            requestAPI = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                    "transit_routing_preference=less_driving&" + "origin=" + lat + "," + lng +
                    "&" + "destination=" + latDestination + "," + lngDestination + "&" + "key=" + getResources().getString(R.string.google_direction_API);
            mService.getPath(requestAPI).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());

                        JSONArray routes = jsonObject.getJSONArray("routes");

                        JSONObject object = routes.getJSONObject(0);

                        JSONArray legs = object.getJSONArray("legs");

                        JSONObject legsObject = legs.getJSONObject(0);

                        JSONObject distance = legsObject.getJSONObject("distance");
                        txtDistance.setText(distance.getString("text"));

                        JSONObject time = legsObject.getJSONObject("duration");
                        txtTime.setText(time.getString("text"));

                        String address = "dari : " + legsObject.getString("start_address") + "\n Ke : " + legsObject.getString("end_address");
                        txtAddress.setText(address);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(CustomerCall.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStop() {
        mediaPlayer.release();
        super.onStop();
    }

    @Override
    protected void onPause() {
        mediaPlayer.release();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
