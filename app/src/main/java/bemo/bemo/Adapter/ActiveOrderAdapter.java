package bemo.bemo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import bemo.bemo.Common.Common;
import bemo.bemo.CustomerCall;
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
    String alamat_tujuan;

    public ActiveOrderAdapter(Context context, List<DataActiveOrder> list) {
        this.context = context;
        this.list = list;
        Log.e("list",String.valueOf(list.size()));
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
        getDirection(activeOrderData.getLocation(), activeOrderData.getTujuan());
        holder.txtAlamat.setText(alamat_tujuan);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtStatus, txtJam, txtNamaRider, txtAlamat, txtBiaya;
        public ViewHolder(View itemView) {
            super(itemView);
            txtStatus = (TextView)itemView.findViewById(R.id.txtStatus);
            txtJam = (TextView)itemView.findViewById(R.id.txtJam);
            txtNamaRider = (TextView)itemView.findViewById(R.id.txtNamaRider);
            txtAlamat = (TextView)itemView.findViewById(R.id.txtAlamat);
            txtBiaya = (TextView)itemView.findViewById(R.id.txtBiaya);
            mService = Common.getGoogleAPI();
        }
    }


    private void getDirection(String location, String Destination) {

        String requestAPI = null;
        try {
            requestAPI = "https://maps.googleapis.com/maps/api/directions/json?" + "mode=driving&" +
                    "transit_routing_preference=less_driving&" + "origin=" +location+
                    "&" + "destination=" +Destination+ "&" + "key=AIzaSyA5-_YRFVMotBYJlNN5tgb5XkbVkSUGMgc";
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
                        Log.e("end address", alamat_tujuan);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
