package com.example.jimmivila.ClickersApp;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.estimote.sdk.EstimoteSDK;

/**
 * Created by jimmivila on 10/13/16.
 */
public class MyApplication extends Application{


    @Override
    public void onCreate() {
        super.onCreate();
        EstimoteSDK.initialize(this, "android-beacons-kei", "82d837e720d34be09372611d7cfe34f0");

//        beaconManager = new BeaconManager(getApplicationContext());
//
//        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener(){
//
//            @Override
//            public void onEnteredRegion(BeaconRegion region, List<Beacon> list) {
////                showNotification("OMG this is incredible my friend...", "Today was not a good day");
//            }
//
//            @Override
//            public void onExitedRegion(BeaconRegion region) {
////                showNotification("See yah broda!", "not bad nigro!");
//            }
//        });
//
//
//        beaconManager.connect(new BeaconManager.ServiceReadyCallback(){
//            @Override
//            public void onServiceReady() {
//                beaconManager.startMonitoring(new BeaconRegion(
//                        "chilling buscando beakokons",
////                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
////                        9274,
////                        54670));
//                        null,null,null
//                ));
//            }
//        });
    }

    public void showNotification(String title, String message){
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent},PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);
    }

}
