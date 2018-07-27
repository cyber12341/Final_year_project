package bemo.bemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import bemo.bemo.Common.Common;
import bemo.bemo.DriverTracking;
import bemo.bemo.DriverTrackings;
import bemo.bemo.Model.DataActiveOrder;
import bemo.bemo.R;
import bemo.bemo.Remote.IGoogleAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActiveOrderAdapter extends RecyclerView.Adapter<ActiveOrderAdapter.ViewHolder> {

    private Context context;
    private List<DataActiveOrder> list;
    IGoogleAPI mService;
    String alamat_tujuan, customerId, pushId;

    public ActiveOrderAdapter(Context context, List<DataActiveOrder> list) {
        this.context = context;
        this.list = list;

        mService = Common.getGoogleAPI();
    }

    @Override
    public ActiveOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activeorderitem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ActiveOrderAdapter.ViewHolder holder, int position) {
        final DataActiveOrder activeOrderData = list.get(position);
        holder.txtNamaRider.setText(activeOrderData.getNama());
        String requestAPI = null;
        requestAPI = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                "transit_routing_preference=less_driving&" + "origin=" +activeOrderData.getLocation()+
                "&" + "destination=" +activeOrderData.getTujuan()+ "&" + "key=AIzaSyBGb4-BwEEOvA8Dg5ohu4BrM6f7uNay26Y";
        Log.e("request Api",requestAPI);
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

                    JSONObject time = legsObject.getJSONObject("duration");

                    String address = "dari : " + legsObject.getString("start_address") + "\n Ke : " + legsObject.getString("end_address");
                    alamat_tujuan = legsObject.getString("end_address");
                    holder.txtAlamat.setText(alamat_tujuan);
                    Log.e("end address", alamat_tujuan);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


        holder.txtBiaya.setText("Price of this Order : $"+activeOrderData.getBiaya());
        holder.txtJam.setText(activeOrderData.getDate());
        holder.txtStatus.setText(activeOrderData.getStatus());
        pushId = activeOrderData.getPushId();
        customerId = activeOrderData.getPushId();
//        riderLat = getIntent().getDoubleExtra("lat", -1.0);
//        riderLng = getIntent().getDoubleExtra("lng", -1.0);
//        latDestination = getIntent().getDoubleExtra("latDestination", -1.0);
//        lngDestination = getIntent().getDoubleExtra("lngDestination", -1.0);
//        customerId = getIntent().getStringExtra("customerId");
//        pushId = getIntent().getStringExtra("pushid");
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context.getApplicationContext(), DriverTrackings.class);
                i.putExtra("lat",Double.valueOf(activeOrderData.getLocation().substring(0,activeOrderData.getLocation().indexOf(",")-1)));
                i.putExtra("lng",Double.valueOf(activeOrderData.getLocation().substring(activeOrderData.getLocation().indexOf(",")+1)));
                i.putExtra("latDestination",Double.valueOf(activeOrderData.getTujuan().substring(0,activeOrderData.getTujuan().indexOf(",")-1)));
                i.putExtra("lngDestination",Double.valueOf(activeOrderData.getTujuan().substring(activeOrderData.getTujuan().indexOf(",")+1)));
                i.putExtra("customerId",activeOrderData.getCustomerId());
                i.putExtra("pushid", activeOrderData.getPushId());
                i.putExtra("status", activeOrderData.getStatus());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtStatus, txtJam, txtNamaRider, txtAlamat, txtBiaya;
        Button btnDetail;
        public ViewHolder(View itemView) {
            super(itemView);
            txtStatus = (TextView)itemView.findViewById(R.id.txtStatus);
            txtJam = (TextView)itemView.findViewById(R.id.txtJam);
            txtNamaRider = (TextView)itemView.findViewById(R.id.txtNamaRider);
            txtAlamat = (TextView)itemView.findViewById(R.id.txtAlamat);
            txtBiaya = (TextView)itemView.findViewById(R.id.txtBiaya);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }


    private void getDirection(String location, String Destination) {


    }


}
