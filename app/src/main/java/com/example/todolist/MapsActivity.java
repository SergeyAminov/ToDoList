package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private SearchView searchView;
    private View mapView;
    private Button btnFind;

    public static final int LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        searchView = findViewById(R.id.search_bar);
        btnFind = findViewById(R.id.buttonFind);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_google);
        supportMapFragment.getMapAsync(MapsActivity.this);
        mapView = supportMapFragment.getView();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        Places.initialize(MapsActivity.this,"AIzaSyCuPb2sC7C575zCfzHoJLW9DJ3rk04jGYU");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("Your current location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,5 ));
        googleMap.addMarker(markerOptions);

       map = googleMap;
       map.setMyLocationEnabled(true);
       map.getUiSettings().setMyLocationButtonEnabled(true);


       if (mapView != null && mapView.findViewById(Integer.parseInt("1"))!=null)
       {
           View locButton = ((View)mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
           RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locButton.getLayoutParams();
           layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);
           layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
           layoutParams.setMargins(0,0,40,180);
       }
        LocationRequest locationRequest = LocationRequest.create();
       locationRequest.setInterval(10000);
       locationRequest.setFastestInterval(5000);
       locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient settingsClient = LocationServices.getSettingsClient(MapsActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MapsActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    getDeviceLocation();
            }
        });
        task.addOnFailureListener(MapsActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException)
                    {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        try {
                            resolvable.startResolutionForResult(MapsActivity.this,5);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                        }
                    }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==5)
        {
            getDeviceLocation();
        }
    }

    private void getDeviceLocation()
    {
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                       if (task.isSuccessful())
                       {
                           currentLocation = task.getResult();
                           if (currentLocation !=null)
                           {
                               map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(),
                                       currentLocation.getLongitude()),10));

                           }
                           else
                           {
                               final LocationRequest locationRequest = LocationRequest.create();
                               locationRequest.setInterval(10000);
                               locationRequest.setFastestInterval(5000);
                               locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                               locationCallback = new LocationCallback()
                               {
                                   @Override
                                   public void onLocationResult(LocationResult locationResult) {
                                       super.onLocationResult(locationResult);
                                       if (locationResult == null)
                                       {
                                           return;
                                       }
                                       currentLocation = locationResult.getLastLocation();
                                      map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),10));
                                      fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                   }
                               };
                               fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, null);


                           }
                       }
                    }
                });
    }

}
