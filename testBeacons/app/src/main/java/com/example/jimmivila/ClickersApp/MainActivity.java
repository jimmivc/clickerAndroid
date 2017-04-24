package com.example.jimmivila.ClickersApp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.estimote.sdk.Nearable;
import com.estimote.sdk.SystemRequirementsChecker;
import com.example.jimmivila.ClickersApp.Objects.Clicker;
import com.example.jimmivila.ClickersApp.Objects.NearableID;
import com.example.jimmivila.ClickersApp.Objects.NearableManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

//    private BeaconManager beaconManager;
//    private BeaconRegion region;
//    private String scanId;
//    private int times = 0;
    private RequestQueue queue;
//    private List<Nearable> savedNearables;

    private NearableManager nearableManager;

    //Requests http logic
  String post = "http://clicker.tech/back-end/index.php/app/saveEvent";
//    String  post = "http://koko-test.com/click/back-end/index.php/app/saveEvent";

    JSONObject json = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.jimmivila.ClickersApp.R.layout.activity_main);

//        EstimoteSDK.initialize(this, "android-beacons-kei", "82d837e720d34be09372611d7cfe34f0");

//        savedNearables = new ArrayList<>();
        queue = Volley.newRequestQueue(this);

//        initialiceManager();


        Map<NearableID, Clicker> clickers = new HashMap<>();

        clickers.put(new NearableID("cc972e2764691817"),new Clicker("cc972e2764691817","idChannel","channelName","eventName","idRule","RuleName"));

        clickers.put(new NearableID("6fa0c418142f43e7"),new Clicker("cc972e2764691817","idChannel","channelName","eventName","idRule","RuleName"));
        clickers.put(new NearableID("e93082a7ac5c2657"),new Clicker("e93082a7ac5c2657","idChannel","channelName","eventName","idRule","RuleName"));
        clickers.put(new NearableID("3854759c4d1b1c78"),new Clicker("3854759c4d1b1c78","idChannel","channelName","eventName","idRule","RuleName"));
        clickers.put(new NearableID("4a9e5c63b35c3c82"),new Clicker("4a9e5c63b35c3c82","idChannel","channelName","eventName","idRule","RuleName"));
        clickers.put(new NearableID("7b8ff0bc3c7de5d5"),new Clicker("7b8ff0bc3c7de5d5","idChannel","channelName","eventName","idRule","RuleName"));
        clickers.put(new NearableID("76ffd830d0287482"),new Clicker("76ffd830d0287482","idChannel","channelName","eventName","idRule","RuleName"));
        clickers.put(new NearableID("958a6d3d67a7078f"),new Clicker("958a6d3d67a7078f","idChannel","channelName","eventName","idRule","RuleName"));






        nearableManager = new NearableManager(this, clickers);
        nearableManager.serListener(new NearableManager.Listener(){
            @Override
            public void onPickUp(Clicker clicker, Nearable nearable) {
                Toast.makeText(getApplicationContext(),"picked up", Toast.LENGTH_SHORT).show();

                try {
                    json.put("id_channel",clicker.getIdChannel());
                    json.put("channel_name",clicker.getChannelName());
                    json.put("event_name",clicker.getEventName());
                    json.put("id_rule",clicker.getIdRule());
                    json.put("rule_name",clicker.getRuleName());
                    json.put("id_object",clicker.getIdClicker());
                    Log.d("OMG",nearable.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(post,json,new Response .Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));

//                                addToSavedList(near);
                                /////Toast
                                Toast.makeText(getApplicationContext(),"agregando a la base de datos", Toast.LENGTH_SHORT).show();
                                /////
//                                new Timer().schedule(new TimerTask(){
//                                    @Override
//                                    public void run(){
//                                        removeFromList(near);
//                                        Log.d("Removed","from list");
//                                    }
//                                },30000);


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


                ///show beacon info
                ((TextView)findViewById(R.id.txtId)).setText("Id: "+nearable.identifier);
                ((TextView)findViewById(R.id.txtTemp)).setText("Temperatura: "+nearable.temperature);
                ((TextView)findViewById(R.id.txtColor)).setText("Color: "+nearable.color.toString());
                ((TextView)findViewById(R.id.txtBaterry)).setText("Nivel de bateria: "+nearable.batteryLevel+"");
                ((TextView)findViewById(R.id.txtTipo)).setText("Tipo: "+nearable.type.toString());

            }

            @Override
            public void onPutdown(Clicker clicker, Nearable nearable) {
                Toast.makeText(getApplicationContext(),"putted down", Toast.LENGTH_SHORT).show();

            }
        });
    }
//
//    private void initialiceManager() {
//        beaconManager = new BeaconManager(this);
//        setNearableListener();
//    }

//    private void setNearableListener() {
//        beaconManager.setNearableListener(new BeaconManager.NearableListener(){
//            @Override
//            public void onNearablesDiscovered(List<Nearable> nearables){
//
//                if(!nearables.isEmpty()){
//                    Log.d("NEARABLES", ++times + " " +getNearableById("cc972e2764691817",nearables));
//                    Log.d("Lista scaneados",savedNearables.toString());
////                    Nearable nearable = getNearableById("cc972e2764691817",nearables);
//                    for (final Nearable near: nearables) {
//
//                        Nearable nearable = getNearableById(near.identifier,savedNearables);
//
//                        if(nearable == null){
//                            if (near.isMoving){
//
//                                Log.d("Nice!!!","Not Bad!");
//                                try {
//                                    json.put("id_channel","null");
//                                    json.put("channel_name","null");
//                                    json.put("event_name","null");
//                                    json.put("id_rule","null");
//                                    json.put("rule_name","null");
//                                    json.put("id_object",near.identifier);
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                JsonObjectRequest jsonRequest = new JsonObjectRequest(post,json,new Response.Listener<JSONObject>() {
//                                    @Override
//                                    public void onResponse(JSONObject response) {
//                                        try {
//                                            VolleyLog.v("Response:%n %s", response.toString(4));
//
//                                            addToSavedList(near);
//
//                                            /////Toast
//                                            Toast.makeText(getApplicationContext(),"agregando nearable: "+near.type +" a la base de datos", Toast.LENGTH_LONG).show();
//                                            /////
//                                            initialiceManager();
//                                            new Timer().schedule(new TimerTask(){
//                                                @Override
//                                                public void run(){
//                                                    removeFromList(near);
//                                                    Log.d("Removed","from list");
//                                                }
//                                            },30000);
//
//
//                                        } catch (JSONException e) {
//
//                                        }
//                                    }
//                                }, new Response.ErrorListener(){
//                                    @Override
//                                    public void onErrorResponse(VolleyError error){
//                                        VolleyLog.e("faq","no se pudo hacer json");
//                                    }
//                                });
//
//                                queue.add(jsonRequest);
//
//                            }
//                        }
//                    }
//
//                }
////                TextView txtLugaresCercanos = (TextView) findViewById(R.id.txtLugaresCercanos);
////                txtLugaresCercanos.setText(nearables.toString());
//            }
//        });
//    }


//    private void removeFromList(Nearable near) {
//        savedNearables.remove(near);
//    }
//
//    private void addToSavedList(Nearable near) {
//        savedNearables.add(near);
//    }

//    private Nearable getNearableById(String id,List<Nearable> nearables) {
//        for (Nearable nearable:nearables) {
//            if(id.equals(nearable.identifier)){
//                return nearable;
//            }
//        }
//        return null;
//    }
//
    @Override
    protected void onResume() {
        super.onResume();

        if (!SystemRequirementsChecker.checkWithDefaultDialogs(this)){
            Log.e(TAG, "Can't scan for beacons, some pre-conditions were not met");
            Log.e(TAG, "Read more about what's required at: http://estimote.github.io/Android-SDK/JavaDocs/com/estimote/sdk/SystemRequirementsChecker.html");
            Log.e(TAG, "If this is fixable, you should see a popup on the app's screen right now, asking to enable what's necessary");
        } else {
            Log.d(TAG, "Starting ShowroomManager updates");
            nearableManager.startUpdates();
        }
//        SystemRequirementsChecker.checkWithDefaultDialogs(this);
//
//        beaconManager.connect(new BeaconManager.ServiceReadyCallback(){
//            @Override
//            public void onServiceReady() {
//                beaconManager.startNearableDiscovery();
//            }
//        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        nearableManager.stopUpdates();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        nearableManager.destroy();
    }

}
