package com.example.familytrackerapp;

import static android.content.ContentValues.TAG;

import static com.example.familytrackerapp.Constants.constant.ACTION_STOP;
import static com.example.familytrackerapp.Constants.constant.lat;
import static com.example.familytrackerapp.Constants.constant.longi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.familytrackerapp.Constants.constant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MyService extends Service {
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if(locationResult!=null)
            {
                for(Location location :locationResult.getLocations())
                {
                    Coordinates coordinates=new Coordinates(location.getLatitude(),location.getLongitude());
                    Log.d(TAG, "Coordinates: "+coordinates);
                    Log.d(TAG, "Latitude: "+location.getLatitude());
                    Log.d(TAG, "Longitude: "+location.getLongitude());
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put(lat,String.valueOf(location.getLatitude()));
                    map.put(longi,String.valueOf(location.getLongitude()));
                    MainActivity.firebaseFirestore.collection("Locations").document(firebaseAuth.getUid().toString()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Coordinates added successfully ");
                         }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error while adding Coordinates", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error on FireStore: "+e);
                        }
                    });
                }
            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service has started");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        setttingsandpermissioncheck();
        Intent stop_intent = new Intent(this, MyBroadCastReceiver.class);
        stop_intent.setAction(ACTION_STOP);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent =
                PendingIntent.getBroadcast(this, 0, stop_intent,  PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationChannel channel = new NotificationChannel("Ram", "name", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("description");
        NotificationManager notificationManager1 = getSystemService(NotificationManager.class);
        notificationManager1.createNotificationChannel(channel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Ram")
                .setSmallIcon(R.drawable.ic_baseline_location_on_24)
                .setContentTitle("Family Tracker App")
                .setContentText("Your device is under tracking mode click STOP to stop tracking")
                .addAction(R.drawable.notification_icon,"STOP",pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(1, builder.build());
        startForeground(1, builder.build());


        return START_STICKY;
    }
    private void setttingsandpermissioncheck() {
        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build();
        SettingsClient settingsClient = LocationServices.getSettingsClient( this);
        Task<LocationSettingsResponse> locationSettingsResponseTask = settingsClient.checkLocationSettings(locationSettingsRequest);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getlocationupdates();
            }
        });
    }
    @SuppressLint("MissingPermission")
    private void getlocationupdates() {

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
    private void stoplocationupdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

    }
}
