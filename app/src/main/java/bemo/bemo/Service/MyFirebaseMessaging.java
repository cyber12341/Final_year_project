package bemo.bemo.Service;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import bemo.bemo.CustomerCall;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData() != null) {

            Map<String, String> data = remoteMessage.getData();
            String customer = data.get("customer");
            String customerId = data.get("customerId");
            String lat = data.get("lat");
            String lng = data.get("lng");
            String lat_des = data.get("lat_des");
            String lng_des = data.get("lng_des");
            String tipe = data.get("tipe");
            String biaya = data.get("biaya");

            Log.e("lat_Des", lat_des);

            Intent intent = new Intent(getApplicationContext(), CustomerCall.class);
            intent.putExtra("lat", Double.parseDouble(lat));
            intent.putExtra("lng", Double.parseDouble(lng));
            intent.putExtra("latDestination", Double.parseDouble(lat_des));
            intent.putExtra("lngDestination", Double.parseDouble(lng_des));
            intent.putExtra("customer", customer);
            intent.putExtra("customerId", customerId);
            intent.putExtra("tipe", tipe);
            intent.putExtra("biaya", biaya);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }
}
