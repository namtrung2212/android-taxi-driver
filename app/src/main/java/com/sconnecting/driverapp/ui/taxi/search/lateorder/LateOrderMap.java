package com.sconnecting.driverapp.ui.taxi.search.lateorder;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.InternetHelper;
import com.sconnecting.driverapp.google.GoogleDirectionHelper;
import com.sconnecting.driverapp.location.LocationHelper;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.data.models.TravelOrder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by TrungDao on 10/17/16.
 */

public class LateOrderMap extends Fragment {

    SupportMapFragment mapFragment;
    View view;

    LateOrderScreen parent;

    public TravelOrder CurrentOrder() {

        return parent.currentOrder;

    }
    public String currentEncodedPolyline;

    public GoogleMap gmsMapView;
    public Polyline pathPolyLine;
    public Marker mSourceMarker;
    public Marker mDestinyMarker;


    public LateOrderMap(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        parent = (LateOrderScreen) context;
        parent.mMapView = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.taxi_late_order_map, container, false);

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

                                        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                            ActivityCompat.requestPermissions(getActivity(),
                                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},2212);

                                            return;
                                        }


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

                                            }
                                        });


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

                                }
        );


    }

    @SuppressWarnings("MissingPermission")
    public void invalidate(Boolean isFirstTime,  final Completion listener){

        gmsMapView.setMyLocationEnabled(false);

        invalidatePath();

        if(listener != null)
            listener.onCompleted();
    }


    public void invalidatePath(){

        if(CurrentOrder() == null){

            if (pathPolyLine != null)
                pathPolyLine.remove();

            gmsMapView.setPadding(5, 5, 5, 5);
            invalidateRouteMarkers();

        }else{

            String strEncodedPolyline = null;

            if(CurrentOrder().OrderPolyline != null)
                strEncodedPolyline = CurrentOrder().OrderPolyline;

            final String encoded = strEncodedPolyline;
            if(strEncodedPolyline != null && (currentEncodedPolyline == null || currentEncodedPolyline.equals(encoded) == false)) {

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
                        gmsMapView.setPadding(100, 100, 100, 450);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                        gmsMapView.moveCamera(cameraUpdate);
                    }
                });

            }else{

                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                if(CurrentOrder().OrderPickupLoc != null)
                    builder.include(CurrentOrder().OrderPickupLoc.getLatLng());

                if(CurrentOrder().OrderDropLoc != null)
                    builder.include(CurrentOrder().OrderDropLoc.getLatLng());

                LatLngBounds bounds = builder.build();
                gmsMapView.setPadding(100, 100, 100, 450);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0);
                gmsMapView.moveCamera(cameraUpdate);
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


        if(this.CurrentOrder() != null &&  this.CurrentOrder().OrderDropLoc != null){
            destinyLoc = this.CurrentOrder().OrderDropLoc.getLatLng();
        }


        if(this.mSourceMarker == null){

            final LatLng sourceLoc2 = sourceLoc;

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



    public void moveToLocation(LatLng target,Boolean isAnimate,Integer zoom ){

        if(isAnimate != null && isAnimate == true){

            if(zoom != null){
                this.gmsMapView.animateCamera(CameraUpdateFactory.newLatLngZoom(target,zoom));
            }else{
                this.gmsMapView.animateCamera(CameraUpdateFactory.newLatLng(target));
            }

            return;
        }

        Location source = LocationHelper.newLocation(target);
        Location destiny =  LocationHelper.newLocation(this.gmsMapView.getCameraPosition().target);
        Float distance = Math.abs(source.distanceTo(destiny));

        if(distance > 100 || (isAnimate != null && isAnimate == false)){

            if(zoom != null){
                this.gmsMapView.moveCamera(CameraUpdateFactory.newLatLngZoom(target,zoom));
            }else{
                this.gmsMapView.moveCamera(CameraUpdateFactory.newLatLng(target));
            }
        }else{

            if(zoom != null){
                this.gmsMapView.animateCamera(CameraUpdateFactory.newLatLngZoom(target,zoom));
            }else{
                this.gmsMapView.animateCamera(CameraUpdateFactory.newLatLng(target));
            }

        }
    }


}
