package com.nibiru.lbspuskesmas.ListPuskesmas.MapsActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.nibiru.lbspuskesmas.ListPuskesmas.MapsActivity.JsonDataMaps.Distance;
import com.nibiru.lbspuskesmas.ListPuskesmas.MapsActivity.JsonDataMaps.Duration;
import com.nibiru.lbspuskesmas.ListPuskesmas.MapsActivity.JsonDataMaps.LegsItem;
import com.nibiru.lbspuskesmas.ListPuskesmas.MapsActivity.JsonDataMaps.Responsenya;
import com.nibiru.lbspuskesmas.R;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback {
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static LatLng lokasi_saya = new LatLng(-6.2087726,106.8455953);
    public static LatLng lokasi_puskesmas = new LatLng(-6.17567,107.00027);
    private GoogleMap mMap;
    Marker myMarker;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    public static String nama_tempat,aimage;
    private boolean route_found = false;
    LinearLayout ln_nama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ln_nama = findViewById(R.id.ln_nama);
        ln_nama.setVisibility(View.GONE);
        LocationDialogSetting(MapsActivity.this);
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFrag != null) {
            mapFrag.getMapAsync(this);
        }
    }


    private void LocationDialogSetting(final Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("REQUEST_LOCATION", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("REQUEST_LOCATION", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("REQUEST_LOCATION", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("REQUEST_LOCATION", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private void LoadMarker(int resid, final LatLng marker_dest, final String title_dest){
        Glide.with(MapsActivity.this).asBitmap()
                .load(resid)
                .apply(new RequestOptions().bitmapTransform(new CropCircleTransformation())
                        .dontAnimate())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        final float scale = MapsActivity.this.getResources().getDisplayMetrics().density;
                        int pixels = (int) (50 * scale + 0.5f);
                        resource = Bitmap.createScaledBitmap(resource, pixels, pixels, true);
                        mMap.addMarker(new MarkerOptions().flat(true)
                                .icon(BitmapDescriptorFactory.fromBitmap(resource))
                                .position(marker_dest).title(title_dest));
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};
        Permissions.check(MapsActivity.this, permissions, null, null, new PermissionHandler() {
            @SuppressLint("MissingPermission")
            @Override
            public void onGranted() {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        mLastLocation = location;
                        if (myMarker != null) {
                            myMarker.remove();
                        }
                        lokasi_saya = new LatLng(location.getLatitude(), location.getLongitude());
                        if(!route_found) {
                            DirectionMap(lokasi_saya, lokasi_puskesmas);
                        }
                        Log.d("LATLONG_LOKASI", String.valueOf(lokasi_puskesmas));
                        Log.d("LATLONG_LOKASI", String.valueOf(lokasi_saya));
                    }
                });
            }
            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                Toast.makeText(context,"Lokasi anda tidak diketahui, nyalakan GPS",Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private void DirectionMap(final LatLng awal, final LatLng tujuan) {
        final ProgressDialog dialog = new ProgressDialog(MapsActivity.this);
        dialog.setTitle("Memuat maps...");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        dialog.show();
        AndroidNetworking.get("https://maps.googleapis.com/maps/api/directions/json")
                .addQueryParameter("key", "AIzaSyDzxjZ5FMsYJ875u-zZ1cVEbaUY1AmTMcU")
                .addQueryParameter("origin", awal.latitude+","+awal.longitude)
                .addQueryParameter("destination", tujuan.latitude+","+tujuan.longitude)
                .setPriority(Priority.HIGH)
                .build().getAsOkHttpResponseAndObject(Responsenya.class, new OkHttpResponseAndParsedRequestListener<Responsenya>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Response okHttpResponse, Responsenya response) {
                dialog.hide();
                if (okHttpResponse.isSuccessful()) {
                    if (response.getStatus().matches("OK")) {
                        LegsItem dataLegs = response.getRoutes().get(0).getLegs().get(0);
                        String polylinePoint = response.getRoutes().get(0).getOverviewPolyline().getPoints();
                        List<LatLng> decodePath = PolyUtil.decode(polylinePoint);
                        LoadMarker(R.drawable.ic_maps_me_24dp,lokasi_saya, "Saya");
                        LoadMarker(R.drawable.ic_maps_other_24dp,lokasi_puskesmas, nama_tempat);

                        //Membuat Garis Route
                        mMap.addPolyline(new PolylineOptions().addAll(decodePath)
                                .width(15f).color(Color.argb(255, 56, 167, 252)))
                                .setGeodesic(true);

                        LatLngBounds.Builder latLongBuilder = new LatLngBounds.Builder();
                        latLongBuilder.include(awal);
                        latLongBuilder.include(tujuan);

                        // Bounds Coordinata (Untuk Agar Route 1 dan 2 Berada ditengah layar
                        LatLngBounds bounds = latLongBuilder.build();
                        int width = getResources().getDisplayMetrics().widthPixels;
                        int height = getResources().getDisplayMetrics().heightPixels;
                        int paddingMap = (int) (width * 0.2);

                        //Update posisi kamera
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, paddingMap);
                        mMap.moveCamera(cu);

                        Distance dataDistance = dataLegs.getDistance();
                        Duration dataDuration = dataLegs.getDuration();
                        TextView txt_nama = findViewById(R.id.txt_nama);
                        TextView txt_alamat = findViewById(R.id.txt_alamat);
                        TextView txt_jarak = findViewById(R.id.txt_jarak);
                        TextView txt_durasi = findViewById(R.id.txt_waktu);
                        ImageView img_puskesmas = findViewById(R.id.img_puskesmas);


                        img_puskesmas.setImageResource(
                                MapsActivity.this.getResources()
                                        .getIdentifier(aimage,"drawable",MapsActivity.this.getPackageName()));
                        txt_nama.setText(nama_tempat);
                        txt_alamat.setText(dataLegs.getEndAddress());
                        txt_durasi.setText(dataDuration.getText());
                        txt_jarak.setText(dataDistance.getText());
                        route_found = true;
                        ln_nama.setVisibility(View.VISIBLE);
                        findViewById(R.id.btn_navigasi).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                    final AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(MapsActivity.this, R.style.myDialog))
                                            .setCancelable(false)
                                            .setTitle("Konfirmasi")
                                            .setMessage("Menuju lokasi menggunakan Maps Navigasi ?")
                                            .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setPositiveButton("Mulai", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lokasi_puskesmas.latitude + "," + lokasi_puskesmas.longitude);
                                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                                    mapIntent.setPackage("com.google.android.apps.maps");
                                                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                                        startActivity(mapIntent);
                                                    }
                                                    dialog.dismiss();
                                                }
                                            });
                                    dialog.show();
                            }
                        });


                    }else {
                        dialog.hide();
                        Toast.makeText(MapsActivity.this,"Titik koordinat tidak ditemukan",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onError(ANError anError) {
                dialog.hide();
                Log.d("MAPS_ERROR",anError.getErrorDetail());
            }
        });
    }
}
