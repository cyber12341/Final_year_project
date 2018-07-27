package bemo.bemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bemo.bemo.Adapter.ActiveOrderAdapter;
import bemo.bemo.Common.Common;
import bemo.bemo.Model.ActiveOrder;
import bemo.bemo.Model.DataActiveOrder;
import bemo.bemo.Model.Users;
import bemo.bemo.Model.getpushid;

public class ActiveOrderActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter adapter;
    private List<DataActiveOrder> activeOrders;
    ArrayList<HashMap<String, String>> arraylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_order);
        arraylist = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        activeOrders = new ArrayList<>();
        mRecyclerView.setLayoutManager(mLayoutManager);
        Common.getPushId = new getpushid();
        FirebaseDatabase.getInstance().getReference(Common.active_order).child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            Common.activeOrder = snap.getValue(ActiveOrder.class);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("customerId", Common.activeOrder.getCustomerId());
                            map.put("biaya", Common.activeOrder.getBiaya());
                            map.put("status", Common.activeOrder.getStatus());
                            map.put("tipe", Common.activeOrder.getTipe());
                            map.put("lat", String.valueOf(Common.activeOrder.getLat()));
                            map.put("lng", String.valueOf(Common.activeOrder.getLng()));
                            map.put("latDestination", String.valueOf(Common.activeOrder.getLatDestination()));
                            map.put("lngDestination", String.valueOf(Common.activeOrder.getLngDestination()));
                            map.put("date", Common.activeOrder.getDate());
                            map.put("pushId", snap.getKey());
                            arraylist.add(map);
                        }
                        for (int i =0; i<arraylist.size(); i++)
                        {
                            final int finalI = i;
                            FirebaseDatabase.getInstance().getReference(Common.user_rider_tbl).child(arraylist.get(i).get("customerId"))
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot2) {
                                            Common.user = dataSnapshot2.getValue(Users.class);
                                            DataActiveOrder dataActiveOrder = new DataActiveOrder();
                                            dataActiveOrder.setNama(Common.user.getName());
                                            dataActiveOrder.setCustomerId(arraylist.get(finalI).get("customerId"));
                                            dataActiveOrder.setBiaya(arraylist.get(finalI).get("biaya"));
                                            dataActiveOrder.setLocation(arraylist.get(finalI).get("lat") + "," + arraylist.get(finalI).get("lng"));
                                            dataActiveOrder.setTujuan(arraylist.get(finalI).get("latDestination") + "," + arraylist.get(finalI).get("lngDestination"));
                                            dataActiveOrder.setStatus(arraylist.get(finalI).get("status"));
                                            dataActiveOrder.setTipe(arraylist.get(finalI).get("tipe"));
                                            dataActiveOrder.setDate(arraylist.get(finalI).get("date"));
                                            dataActiveOrder.setPushId(arraylist.get(finalI).get("pushId"));
                                            activeOrders.add(dataActiveOrder);
                                            adapter = new ActiveOrderAdapter(ActiveOrderActivity.this, activeOrders);
                                            mRecyclerView.setAdapter(adapter);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
