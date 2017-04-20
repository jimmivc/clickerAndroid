package com.example.jimmivila.clicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.recognition.packets.Nearable;
import com.estimote.coresdk.service.BeaconManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private BeaconRegion region;
    private String scanId;
    private int times = 0;
    private RequestQueue queue;


    //Requests http logic
//    String url = "http://clicker.tech/back-end/index.php/app/test";
    String post = "http://clicker.tech/back-end/index.php/app/saveEvent";
    String url = "http://clicker.tech/back-end/index.php/app/test";

    StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("RESPONSE", response.toString());
                }
            }, new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error){
            Log.d("RESPONSE","ERRORIFICO");
        }
    });

    JSONObject json = new JSONObject();


//    JsonObjectRequest jsonRequest = new JsonObjectRequest(post,);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.jimmivila.clicker.R.layout.activity_main);

        EstimoteSDK.initialize(this, "android-beacons-kei", "82d837e720d34be09372611d7cfe34f0");

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
                    Log.d("NEARABLES", ++times + " " +nearables.toString());
                    Nearable nearable = getNearableById("cc972e2764691817",nearables);

                    if(nearable != null){
                        if (nearable.isMoving){
                            queue.add(stringRequest);

                        }
                    }
                }
//                TextView txtLugaresCercanos = (TextView) findViewById(R.id.txtLugaresCercanos);
//                txtLugaresCercanos.setText(nearables.toString());
            }
        });
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
//    @Override
//    protected void onStart(){
//        super.onStart();
//        beaconManager.connect(new BeaconManager.ServiceReadyCallback(){
//            @Override
//            public void onServiceReady(){
//                beaconManager.startNearableDiscovery();
//            }
//        });
//    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback(){
            @Override
            public void onServiceReady() {
//                beaconManager.startRanging(region);
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
