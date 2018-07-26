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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_order);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        activeOrders = new ArrayList<>();
        adapter = new ActiveOrderAdapter(this, activeOrders);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);
        Common.getPushId = new getpushid();
        FirebaseDatabase.getInstance().getReference(Common.active_order).child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            Common.activeOrder = snap.getValue(ActiveOrder.class);
                            FirebaseDatabase.getInstance().getReference(Common.user_rider_tbl).child(Common.activeOrder.getCustomerId())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot2) {
                                            Common.user = dataSnapshot2.getValue(Users.class);

                                            DataActiveOrder dataActiveOrder = new DataActiveOrder();
                                            dataActiveOrder.setNama(Common.user.getName());
                                            dataActiveOrder.setBiaya(Common.activeOrder.getBiaya());
                                            dataActiveOrder.setLocation(Common.activeOrder.getLat() + "," + Common.activeOrder.getLng());
                                            dataActiveOrder.setTujuan(Common.activeOrder.getLatDestination() + "," + Common.activeOrder.getLngDestination());
                                            dataActiveOrder.setStatus(Common.activeOrder.getStatus());
                                            dataActiveOrder.setTipe(Common.activeOrder.getTipe());
                                            activeOrders.add(dataActiveOrder);
                                            Log.e("active Order", String.valueOf(activeOrders.size()));
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
