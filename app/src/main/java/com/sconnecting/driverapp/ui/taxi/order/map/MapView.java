package com.sconnecting.driverapp.ui.taxi.order.map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.InternetHelper;
import com.sconnecting.driverapp.google.GoogleDirectionHelper;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.ui.taxi.order.OrderScreen;

import com.sconnecting.driverapp.location.LocationHelper;
import com.sconnecting.driverapp.data.models.TravelOrder;


import com.daimajia.androidanimations.library.*;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


/**
 * Created by TrungDao on 8/1/16.
 */

public class MapView extends Fragment{

    SupportMapFragment mapFragment;
    View view;

    Button btnMyLocation;
    Button btnDirection;

    public Boolean shouldToMoveToCurrentLocaton = true;

    OrderScreen parent;

    public TravelOrder CurrentOrder() {

        if( SCONNECTING.orderManager == null)
            return null;

        return SCONNECTING.orderManager.currentOrder;

    }

    public String currentEncodedPolyline;

    public GoogleMap gmsMapView;
    public Polyline pathPolyLine;
    public Marker mSourceMarker;
    public Marker mDestinyMarker;


    public MapView(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        parent = (OrderScreen) context;
        parent.mMapView = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.taxi_order_map, container, false);

        initControls();

        return view;
    }

    public void initControls() {


        if (! InternetHelper.checkConnectingToInternet()) {
            return;
        }


        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googlemap);
        mapFragment.getMapAsync(new OnMapReadyCallback(){

            @Override
            public void onMapReady(GoogleMap map) {

                gmsMapView = map;
                gmsMapView.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {

                        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},2212);

                            return;
                        }

                        initMap();
                    }
                });


            }

            }
        );


        btnMyLocation = (Button) view.findViewById(R.id.btnMyLocation);
        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                YoYo.with(Techniques.ZoomIn).duration(300).playOn(v);

                SCONNECTING.locationHelper.getLocation();

                parent.mMapMarkerManager.currentVehicle().showCar(false, new Completion() {
                    @Override
                    public void onCompleted() {

                        moveToCurrentLocation(null,false);
                    }
                });



            }
        });

        btnDirection = (Button) view.findViewById(R.id.btnDirection);
        btnDirection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                YoYo.with(Techniques.ZoomIn).duration(300).playOn(v);

                if(SCONNECTING.orderManager.currentOrder != null){

                    if(SCONNECTING.orderManager.currentOrder.OrderPickupLoc != null){

                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + SCONNECTING.orderManager.currentOrder.OrderPickupLoc.latitude + "," + SCONNECTING.orderManager.currentOrder.OrderPickupLoc.longitude);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
                        startActivity(mapIntent);

                    }else if(SCONNECTING.orderManager.currentOrder.OrderDropLoc != null){
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + SCONNECTING.orderManager.currentOrder.OrderDropLoc.latitude + "," + SCONNECTING.orderManager.currentOrder.OrderDropLoc.longitude);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
                        startActivity(mapIntent);
                    }
                }

            }
        });



    }

    @SuppressWarnings("MissingPermission")
    public void initMap(){

        gmsMapView.setMyLocationEnabled(false);
        gmsMapView.getUiSettings().setMyLocationButtonEnabled(false);
        gmsMapView.getUiSettings().setScrollGesturesEnabled(true);
        gmsMapView.getUiSettings().setZoomGesturesEnabled(true);
        gmsMapView.getUiSettings().setTiltGesturesEnabled(true);
        gmsMapView.getUiSettings().setRotateGesturesEnabled(true);
        gmsMapView.getUiSettings().setMapToolbarEnabled(false);
        gmsMapView.getUiSettings().setZoomControlsEnabled(false);


        gmsMapView.setMapType(1);
        gmsMapView.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                parent.mMapMarkerManager.currentVehicle().invalidate(false,null);
            }
        });

        Location location = SCONNECTING.locationHelper.getLocation();
        if(location != null){

            parent.mMapMarkerManager.currentVehicle().showCar(false, new Completion() {
                @Override
                public void onCompleted() {

                    moveToCurrentLocation((float)15.0,false);
                }
            });
        }
        parent.locationReadyListener = new OrderScreen.OnLocationReadyListener() {
            @Override
            public void onReady() {

                parent.mMapMarkerManager.currentVehicle().showCar(false, new Completion() {
                    @Override
                    public void onCompleted() {

                        moveToCurrentLocation((float)15.0,false);
                    }
                });


                if(gmsMapView != null)
                    gmsMapView.setMyLocationEnabled(false);
            }
        };

        Picasso.with(parent).load(R.drawable.sourcepin).resize(80, 80).centerCrop().into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if(gmsMapView != null){

                    mSourceMarker = gmsMapView.addMarker(new MarkerOptions()
                            .position(new LatLng(0,0))
                            .title("Điểm đi")
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                            .anchor((float) 0.5,1)
                    );

                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });


        Picasso.with(parent).load(R.drawable.destinypin).resize(80, 80).centerCrop().into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if(gmsMapView != null){
                    mDestinyMarker = gmsMapView.addMarker(new MarkerOptions()
                            .position(new LatLng(0,0))
                            .title("Điểm đến")
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                            .anchor((float) 0.5,1)
                    );
                    mDestinyMarker.setVisible(false);

                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

        if(parent.mapReadyListener != null)
            parent.mapReadyListener.onReady();
    }

    @SuppressWarnings("MissingPermission")
    public void invalidate(Boolean isFirstTime,  final Completion listener){

        if(gmsMapView != null)
            gmsMapView.setMyLocationEnabled(false);

        invalidatePath();


        if(this.parent.mMapMarkerManager.currentVehicle() != null) {
            if (this.CurrentOrder() != null && this.CurrentOrder().IsStopped()) {
                this.parent.mMapMarkerManager.currentVehicle().hideCar(false,null);
            }else{

                this.parent.mMapMarkerManager.currentVehicle().showCar(false,null);
            }
        }

        btnDirection.setVisibility((this.CurrentOrder() != null && (this.CurrentOrder().OrderPickupLoc != null || this.CurrentOrder().OrderDropLoc != null)) ? View.VISIBLE : View.GONE );

        if(listener != null)
            listener.onCompleted();
    }


    public void invalidatePath(){

        if(CurrentOrder() == null){

            if (pathPolyLine != null)
                pathPolyLine.remove();

            moveToCurrentLocation(null,false);
            invalidateRouteMarkers();

        }else{

            String strEncodedPolyline = null;

            if(CurrentOrder().OrderPolyline != null)
                strEncodedPolyline = CurrentOrder().OrderPolyline;

            if(CurrentOrder().ActPolyline != null)
                strEncodedPolyline = CurrentOrder().ActPolyline;


            final String encoded = strEncodedPolyline;
            if(encoded != null && (currentEncodedPolyline == null || currentEncodedPolyline.equals(encoded) == false)) {

                new GoogleDirectionHelper(this.getContext()).requestPolyline(gmsMapView, strEncodedPolyline, new PolylineOptions().width(3).color(Color.rgb(73, 139, 199)), new GoogleDirectionHelper.RequestPolylineResult() {
                    @Override
                    public void onCompleted(Polyline polyline) {

                        if (polyline != null) {
                            if (pathPolyLine != null)
                                pathPolyLine.remove();
                            pathPolyLine = polyline;
                            currentEncodedPolyline = encoded;
                        }

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (LatLng pos : pathPolyLine.getPoints()) {
                            builder.include(pos);
                        }

                        LatLngBounds bounds = builder.build();
                        gmsMapView.setPadding(100, 200, 100, 450);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                        gmsMapView.animateCamera(cameraUpdate);
                    }
                });
            }

            invalidateRouteMarkers();




        }
    }

    void invalidateRouteMarkers(){


        LatLng sourceLoc = null;
        LatLng destinyLoc = null;

        if(this.CurrentOrder() != null && this.CurrentOrder().OrderPickupLoc != null ){
            sourceLoc = this.CurrentOrder().OrderPickupLoc.getLatLng();
        }

        if(this.CurrentOrder() != null &&  this.CurrentOrder().ActPickupLoc != null){
            sourceLoc = this.CurrentOrder().ActPickupLoc.getLatLng();
        }

        if(this.CurrentOrder() != null &&  this.CurrentOrder().OrderDropLoc != null){
            destinyLoc = this.CurrentOrder().OrderDropLoc.getLatLng();
        }

        if(this.CurrentOrder() != null &&  this.CurrentOrder().ActDropLoc != null ){
            destinyLoc = this.CurrentOrder().ActDropLoc.getLatLng();
        }

        if(this.mSourceMarker == null){

            final LatLng sourceLoc2 = sourceLoc;

            Picasso.with(parent).load(R.drawable.sourcepin).resize(80, 80).centerCrop().into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if(gmsMapView != null){

                        if(mSourceMarker != null)
                            mSourceMarker.remove();

                        mSourceMarker = gmsMapView.addMarker(new MarkerOptions()
                                .position(new LatLng(0,0))
                                .title("Điểm đi")
                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                .anchor((float) 0.5,1)
                        );

                        mSourceMarker.setVisible( sourceLoc2 != null );
                        if( mSourceMarker.isVisible()){

                            mSourceMarker.setPosition(sourceLoc2);

                        }

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });

        }
        if(this.mSourceMarker != null){
            this.mSourceMarker.setVisible( sourceLoc != null );
            if( this.mSourceMarker.isVisible()){

                this.mSourceMarker.setPosition(sourceLoc);

            }
        }

        if(this.mDestinyMarker == null) {

            final LatLng destinyLoc2 = destinyLoc;

            Picasso.with(parent).load(R.drawable.destinypin).resize(80, 80).centerCrop().into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if(gmsMapView != null){

                        if(mDestinyMarker != null)
                            mDestinyMarker.remove();

                        mDestinyMarker = gmsMapView.addMarker(new MarkerOptions()
                                .position(new LatLng(0,0))
                                .title("Điểm đến")
                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                .anchor((float) 0.5,1)
                        );
                        mDestinyMarker.setVisible( destinyLoc2 != null );
                        if( mDestinyMarker.isVisible()){

                            mDestinyMarker.setPosition(destinyLoc2);

                        }

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });

        }
        if(this.mDestinyMarker != null) {
            this.mDestinyMarker.setVisible(destinyLoc != null);
            if (this.mDestinyMarker.isVisible()) {

                this.mDestinyMarker.setPosition(destinyLoc);

            }
        }
    }


    public void moveToLocation(LatLng loc, Float zoom) {

        if(loc == null)
            return;

        moveToLocation(loc,null,zoom,null);

    }

    public void moveToLocation(LatLng target,Boolean isAnimate,Float zoom,Double bearing ){

        CameraPosition.Builder builder =  new CameraPosition.Builder();
        builder = builder.target(target);

        if(bearing != null)
            builder = builder.bearing(bearing.floatValue());

       if( zoom == null && ( this.gmsMapView.getCameraPosition().zoom >14 || this.gmsMapView.getCameraPosition().zoom <=8) )
           zoom = (float) 14.0;

        if(zoom == null)
            zoom = this.gmsMapView.getCameraPosition().zoom;


        if(zoom != null)
            builder = builder.zoom(zoom);

        if(isAnimate != null && isAnimate == true){

            this.gmsMapView.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()), null);
            return;
        }

        Location source = LocationHelper.newLocation(target);
        Location destiny =  LocationHelper.newLocation(this.gmsMapView.getCameraPosition().target);
        Float distance = Math.abs(source.distanceTo(destiny));

        if(distance > 300 || (isAnimate != null && isAnimate == false)){

            this.gmsMapView.moveCamera(CameraUpdateFactory.newCameraPosition(builder.build()));

        }else{

            this.gmsMapView.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()), null);

        }
    }


    public void moveToCurrentLocation(Float zoom,Boolean isAnimate) {

        if(gmsMapView == null)
            return;

        Location location =SCONNECTING.locationHelper.getLocation();

        if(location != null) {
            LatLng target = new LatLng(location.getLatitude(), location.getLongitude());
            moveToLocation(target, isAnimate, zoom,null);

            //noinspection MissingPermission
            gmsMapView.setMyLocationEnabled(false);

            //parent.mMapLocation.updateDotLcation(SCONNECTING.locationHelper.getLocation());
        }
    }




    public void moveToCurrentCarLocation(){

        if(parent.mMapMarkerManager.currentVehicle() != null){
            parent.mMapMarkerManager.currentVehicle().moveToCarLocation();
        }
    }


}
