package com.example.jimmivila.clicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.recognition.packets.Nearable;
import com.estimote.coresdk.service.BeaconManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private BeaconRegion region;
    private String scanId;
    private int times = 0;
    private RequestQueue queue;
    private List<Nearable> savedNearables;


    //Requests http logic
    String post = "http://clicker.tech/back-end/index.php/app/saveEvent";
//    String  post = "http://koko-test.com/click/back-end/index.php/app/saveEvent";

    JSONObject json = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.jimmivila.clicker.R.layout.activity_main);

        EstimoteSDK.initialize(this, "android-beacons-kei", "82d837e720d34be09372611d7cfe34f0");

        savedNearables = new ArrayList<>();
        queue = Volley.newRequestQueue(this);

        beaconManager = new BeaconManager(this);

//        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
//            @Override
//            public void onBeaconsDiscovered(BeaconRegion region, List<Beacon> list) {
//                Log.d("BEACONS",list.toString());
//                if(!list.isEmpty()){
//                    Beacon nearestBeacon = list.get(0);
////                    List<String> lugares = lugaresCercaDeBeacon(nearestBeacon);
//
////                    Log.d("Parada", "Lugares cercanos" + lugares);
//
////                    TextView txtLugaresCercanos = (TextView) findViewById(R.id.txtLugaresCercanos);
////                    txtLugaresCercanos.setText(lugares.toString());
//
//                    TextView txtuuid = (TextView) findViewById(R.id.txtuuid);
//                    TextView txtMajor = (TextView) findViewById(R.id.txtMajor);
//                    TextView txtMinor = (TextView) findViewById(R.id.txtMinor);
//                    TextView txtMac = (TextView) findViewById(R.id.txtMac);
//
//                    txtuuid.setText(nearestBeacon.getProximityUUID().toString());
//                    txtMajor.setText(""+nearestBeacon.getMajor());
//                    txtMinor.setText(""+nearestBeacon.getMinor());
//                    txtMac.setText(nearestBeacon.getMacAddress().toString());
//
//                }
//
//            }
//        });


        setNearableListener();

        region = new BeaconRegion("polish ambassador",null,null,null);

    }

    private void setNearableListener() {
        beaconManager.setNearableListener(new BeaconManager.NearableListener(){
            @Override
            public void onNearablesDiscovered(List<Nearable> nearables){

                if(!nearables.isEmpty()){
//                    Log.d("NEARABLES", ++times + " " +nearables.toString());
//                    Nearable nearable = getNearableById("cc972e2764691817",nearables);
                    for (final Nearable near: nearables) {

                        Nearable nearable = getNearableById(near.identifier,savedNearables);

                        if(nearable == null){
                            if (near.isMoving){
    //

                                Log.d("Nice!!!","Lo lograstes!");
                                try {
                                    json.put("id_channel","null");
                                    json.put("channel_name","null");
                                    json.put("event_name","null");
                                    json.put("id_rule","null");
                                    json.put("rule_name","null");
                                    json.put("id_object",near.identifier);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                JsonObjectRequest jsonRequest = new JsonObjectRequest(post,json,new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            VolleyLog.v("Response:%n %s", response.toString(4));

                                            addToSavedList(near);

                                            /////Toast
                                            Toast.makeText(getApplicationContext(),"agregando nearable: "+near.type +" a la base de datos", Toast.LENGTH_LONG).show();
                                            /////

                                            new Timer().schedule(new TimerTask(){
                                                @Override
                                                public void run(){
                                                    removeFromList(near);
                                                    Log.d("Removed","from list");
                                                }
                                            },30000);


                                        } catch (JSONException e) {

                                        }
                                    }
                                }, new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError error){
                                        VolleyLog.e("faq","no se pudo hacer json");
                                    }
                                });

                                queue.add(jsonRequest);


                            }
                        }
                    }

                }
//                TextView txtLugaresCercanos = (TextView) findViewById(R.id.txtLugaresCercanos);
//                txtLugaresCercanos.setText(nearables.toString());
            }
        });
    }

    private void removeFromList(Nearable near) {
        savedNearables.remove(near);
    }

    private void addToSavedList(Nearable near) {
        savedNearables.add(near);
    }

    private Nearable getNearableById(String id,List<Nearable> nearables) {
        for (Nearable nearable:nearables) {
            if(id.equals(nearable.identifier)){
                return nearable;
            }
        }
        return null;
    }
//
    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback(){
            @Override
            public void onServiceReady() {
                beaconManager.startNearableDiscovery();
            }
        });
    }

    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);
        super.onPause();
    }

}
