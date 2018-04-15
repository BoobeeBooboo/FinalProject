package drusp.shimi.finalproject;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import drusp.shimi.finalproject.model.MarkerLocation;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private GoogleMap mGoogleMap;

    private GoogleApiClient mGoogleApiClient;

    private Marker mCurrLocationMarker;
    private Marker marker;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private DatabaseReference databaseReference;

    private List<MarkerLocation> markerLocations = new ArrayList<>();
    private List<Marker> markerAdd = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFrag.getMapAsync(this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.btm_nav_maps);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("marker_location");

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    toScreen();
                } else {
                    String userId = auth.getCurrentUser().getUid();
                }
            }
        };

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuMain:
                        toMainActivity();
                        break;
//                    case R.id.menuMaps:
//                        break;
                    case R.id.menuPost:
                        showDialogPost();
                        break;
                    case R.id.menuUser:
                        toUserActivity();
                        break;
                    case R.id.menuLogOut:
                        showAlertDialogLogOut();
                        break;
                }
                return false;
            }
        });
        showAlertDialogMaps();
    }

    private void showAlertDialogMaps() {
        new AlertDialog.Builder(MapsActivity.this, R.style.MyDialogTheme)
                .setIcon(R.drawable.app_icon)
                .setTitle(R.string.maps_alert_title)
                .setMessage(R.string.maps_alert_maps)
                .setPositiveButton(R.string.maps_alert_done, null)
                .setPositiveButton(R.string.maps_alert_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showAlertDialogLogOut() {
        new AlertDialog.Builder(MapsActivity.this, R.style.MyDialogTheme)
                .setIcon(R.drawable.app_icon)
                .setTitle(R.string.maps_alert_title)
                .setMessage(R.string.maps_alert_msg)
                .setPositiveButton(R.string.maps_alert_done, null)
                .setNegativeButton(R.string.maps_alert_cancel, null)
                .setPositiveButton(R.string.maps_alert_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton(R.string.maps_alert_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void logout() {
        auth.signOut();
    }

    private void toScreen() {
        Intent toScreenIntent = new Intent(getApplicationContext(), ScreenActivity.class);
        toScreenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toScreenIntent);
    }

    private void toMainActivity() {
        Intent toMainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(toMainIntent);
    }

    private void toUserActivity() {
        Intent toUserIntent = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(toUserIntent);
    }

    private void toLostDogActivity() {
        Intent toLostDogInputIntent = new Intent(getApplicationContext(), LostDogInputActivity.class);
        startActivity(toLostDogInputIntent);
    }

    private void toFoundDogActivity() {
        Intent toFoundDogInputIntent = new Intent(getApplicationContext(), FoundDogInputActivity.class);
        startActivity(toFoundDogInputIntent);
    }

    private void toAdoptDogActivity() {
        Intent toAdoptDogInputIntent = new Intent(getApplicationContext(), AdoptDogInputActivity.class);
        startActivity(toAdoptDogInputIntent);
    }

    private void showDialogPost() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_dialog_status);
        dialog.findViewById(R.id.btn_dialog_status_lost)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toLostDogActivity();
                    }
                });
        dialog.findViewById(R.id.btn_dialog_status_found)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toFoundDogActivity();
                    }
                });
        dialog.findViewById(R.id.btn_dialog_status_adopt)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toAdoptDogActivity();
                    }
                });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        MapStyleOptions styleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.google_maps_style_retro);
        mGoogleMap.setMapStyle(styleOptions);
//        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        addMarkerToMap();
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final String postKey = marker.getTitle();
                final String status = marker.getSnippet();

                switch (status) {
                    case "หาย":
                        Intent toLostDogPosterIntent = new Intent(getApplicationContext(), LostDogPosterActivity.class);
                        toLostDogPosterIntent.putExtra("postKey", postKey);
                        startActivity(toLostDogPosterIntent);
                        break;
                    case "พบ":
                        Intent toFoundDogPosterIntent = new Intent(getApplicationContext(), FoundDogPosterActivity.class);
                        toFoundDogPosterIntent.putExtra("postKey", postKey);
                        startActivity(toFoundDogPosterIntent);
                        break;
                    case "location":
                        Toast.makeText(getApplicationContext(), "ที่อยู่ของคุณ", Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });
    }

    private void addMarkerToMap() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                MarkerLocation markerLocation = dataSnapshot.getValue(MarkerLocation.class);

                assert markerLocation != null;
                final Uri uri = Uri.parse(markerLocation.getDog_image_url());
                final String status = markerLocation.getStatus();

                if (status.equals("พบ")) {
                    LatLng location = new LatLng(markerLocation.getLat(), markerLocation.getLng());
                    MarkerOptions markerOptions = new MarkerOptions().position(location).title(markerLocation.getPoster_id()).snippet(markerLocation.getStatus())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.found_marker));
                    marker = mGoogleMap.addMarker(markerOptions);
                    marker.setVisible(true);
                    markerAdd.add(marker);
                    markerLocations.add(markerLocation);
                } else if (status.equals("หาย")) {
                    LatLng location = new LatLng(markerLocation.getLat(), markerLocation.getLng());
                    MarkerOptions markerOptions = new MarkerOptions().position(location).title(markerLocation.getPoster_id()).snippet(markerLocation.getStatus())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.lost_marker));
                    marker = mGoogleMap.addMarker(markerOptions);
                    marker.setVisible(true);
                    markerAdd.add(marker);
                    markerLocations.add(markerLocation);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addChildEventListener(childEventListener);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        Location mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("ที่อยู่ของคุณ");
        markerOptions.snippet("location");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
        //move map camera
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}