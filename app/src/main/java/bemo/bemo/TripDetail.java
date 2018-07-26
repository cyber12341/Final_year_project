package bemo.bemo;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

import bemo.bemo.Common.Common;

public class TripDetail extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private TextView txtDate, txtFee, txtBaseFare, txtTime, txtDistance, txtEstimatedPayout, txtFrom, txtTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //InitView
        txtBaseFare = (TextView) findViewById(R.id.txtBaseFare);
        txtFee = (TextView) findViewById(R.id.txtFee);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtEstimatedPayout = (TextView) findViewById(R.id.txtEstimatedPayout);
        txtFrom = (TextView) findViewById(R.id.txtFrom);
        txtTo = (TextView) findViewById(R.id.txtTo);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        settingInformation();

    }

    private void settingInformation() {

        if (getIntent()!=null)
        {
            Calendar calendar = Calendar.getInstance();
            String date = String.format("%s, %d/%d", converToDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)),calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH));

            txtDate.setText(date);
            txtFee.setText(String.format("$ %.2f",getIntent().getDoubleExtra("total",0.0)));
            txtEstimatedPayout.setText(String.format("$ %.2f",getIntent().getDoubleExtra("total",0.0)));
            txtBaseFare.setText(String.format("$ %.2f", Common.baseFare));
            txtTime.setText(String.format("%s min", getIntent().getStringExtra("time")));
            txtDistance.setText(String.format("%s km", getIntent().getStringExtra("distance")));
            txtFrom.setText(getIntent().getStringExtra("start_address"));
            txtFrom.setText(getIntent().getStringExtra("end_address"));

            //Add Marker
            String[] location_end = getIntent().getStringExtra("location_end").split(",");
            LatLng dropOff = new LatLng(Double.parseDouble(location_end[0]),Double.parseDouble(location_end[1]));

            mMap.addMarker(new MarkerOptions().position(dropOff).title("Drop Off Here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dropOff,12.0f));

        }

    }

    private Object converToDayOfWeek(int day) {
        switch (day)
        {
            case Calendar.SUNDAY:
                return "Minggu";
            case Calendar.MONDAY:
                return "Senin";
            case Calendar.TUESDAY:
                return "Selasa";
            case Calendar.WEDNESDAY:
                return "Rabu";
            case Calendar.THURSDAY:
                return "Kamis";
            case Calendar.FRIDAY:
                return "Jumat";
            case Calendar.SATURDAY:
                return "Sabtu";
                default:
                    return "UNK";
        }
    }
}
