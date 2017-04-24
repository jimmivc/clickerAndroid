package com.example.jimmivila.ClickersApp.Objects;

import android.content.Context;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Nearable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jimmivila on 4/24/17.
 */
public class NearableManager {
    private Listener listener;

    private BeaconManager beaconManager;
    private String scanId;

    private Map<NearableID, Boolean> nearablesMotionStatus = new HashMap<>();

    public NearableManager(Context context,final Map<NearableID,Clicker> nearables){
        beaconManager = new BeaconManager(context);
        beaconManager.setNearableListener(new BeaconManager.NearableListener() {
            @Override
            public void onNearablesDiscovered(List<Nearable> list) {
                for (Nearable nearable: list){
                    NearableID nearableID = new NearableID(nearable.identifier);
                    if (!nearables.keySet().contains(nearableID)) {
                        continue;
                    }

                    boolean previousStatus = nearablesMotionStatus.containsKey(nearableID) && nearablesMotionStatus.get(nearableID);

                    if (previousStatus != nearable.isMoving){
                        Clicker clicker = nearables.get(nearableID);
                        if (nearable.isMoving){
                            listener.onPickUp(clicker, nearable);
                        } else {
                            listener.onPutdown(clicker, nearable);
                        }
                        nearablesMotionStatus.put(nearableID,nearable.isMoving);
                    }
                }
            }
        });
    }

    public void serListener(Listener listener){
        this.listener = listener;
    }

    public interface Listener{
        void onPickUp(Clicker clicker, Nearable nearable);
        void onPutdown(Clicker clicker, Nearable nearable);
    }

    public void startUpdates(){
        beaconManager.connect(new BeaconManager.ServiceReadyCallback(){
            @Override
            public void onServiceReady() {
                scanId = beaconManager.startNearableDiscovery();
            }
        });
    }

    public void stopUpdates(){
        beaconManager.stopNearableDiscovery(scanId);
    }

    public void destroy(){
        beaconManager.disconnect();
    }
}
