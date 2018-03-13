package com.sconnecting.driverapp.ui.taxi.search;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.listener.GetDoubleValueListener;
import com.sconnecting.driverapp.google.GoogleDirectionHelper;
import com.sconnecting.driverapp.base.AnimationHelper;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.base.DateTimeHelper;
import com.sconnecting.driverapp.base.RegionalHelper;
import com.sconnecting.driverapp.data.controllers.TravelOrderController;
import com.sconnecting.driverapp.data.models.TravelOrder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TrungDao on 10/14/16.
 */


public  class LateOrderSearchCell extends RecyclerView.ViewHolder implements OnMapReadyCallback {

    public View cellView;
    MapView mapFragment;
    public TextView lblPickupLocation;
    public TextView lblDropLocation;
    public TextView lblDateTime;
    public TextView lblTravelDistance;
    public TextView lblCurrentPrice;


    LatLng sourceLoc;
    LatLng destinyLoc;

    public GoogleMap gmsMapView;
    Polyline pathPolyLine;
    Marker mSourceMarker;
    Marker mDestinyMarker;


    TravelOrder travelOrder;
    public Double estPrice;

    public LateOrderSearchCell(View view) {
        super(view);

        cellView = view;

        lblPickupLocation = (TextView) view.findViewById(R.id.lblPickupLocation);
        lblDropLocation = (TextView) view.findViewById(R.id.lblDropLocation);
        lblDateTime = (TextView) view.findViewById(R.id.lblDateTime);
        lblTravelDistance = (TextView) view.findViewById(R.id.lblTravelDistance);
        lblCurrentPrice = (TextView) view.findViewById(R.id.lblCurrentPrice);


        mapFragment = (MapView) view.findViewById(R.id.googlemap);
        mapFragment.onCreate(null);
        mapFragment.onResume();
        mapFragment.getMapAsync(this);
        mapFragment.setClickable(false);
        mapFragment.setFocusable(false);
        mapFragment.setFocusableInTouchMode(false);
    }


    @Override
    public void onMapReady(GoogleMap map) {

        gmsMapView = map;
        gmsMapView.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                if (ActivityCompat.checkSelfPermission(cellView.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(cellView.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions((Activity) cellView.getContext(),
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},2212);

                }


                gmsMapView.setMyLocationEnabled(false);
                gmsMapView.getUiSettings().setMyLocationButtonEnabled(false);
                gmsMapView.getUiSettings().setScrollGesturesEnabled(false);
                gmsMapView.getUiSettings().setZoomGesturesEnabled(false);
                gmsMapView.getUiSettings().setTiltGesturesEnabled(false);
                gmsMapView.getUiSettings().setRotateGesturesEnabled(false);
                gmsMapView.getUiSettings().setAllGesturesEnabled(false);
                gmsMapView.getUiSettings().setMapToolbarEnabled(false);
                gmsMapView.getUiSettings().setZoomControlsEnabled(false);
                gmsMapView.setPadding(5,5,5,5);
                gmsMapView.setMapType(1);

                gmsMapView.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        AnimationHelper.animateButton(itemView, new Completion() {
                            @Override
                            public void onCompleted() {

                                mOnItemClickListener.onItemClick(travelOrder);
                            }
                        });
                    }
                });

                invalidateMap(travelOrder,null);
            }
        });

    }

    public interface OnItemClickListener {
        void onItemClick(TravelOrder item);
    }

    LateOrderSearchCell.OnItemClickListener mOnItemClickListener;
    public void bind(final TravelOrder order, final LateOrderSearchCell.OnItemClickListener listener){

        mOnItemClickListener = listener;
        travelOrder = order;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        mOnItemClickListener.onItemClick(travelOrder);
                    }
                });
            }
        });

    }


    public void updateWithModel(TravelOrder order){

        travelOrder = order;
        lblPickupLocation.setText(order.OrderPickupPlace != null ?  order.OrderPickupPlace : "");
        lblDropLocation.setText( order.OrderDropPlace != null ?  order.OrderDropPlace : "");
        invalidateDate(order);
        invalidateTravelPath(order);
        loadEstPrice(order,null);
    }

    public void invalidateMap(final TravelOrder order, final Completion listener){

        sourceLoc = null;
        destinyLoc = null;

        if( order.OrderPickupLoc != null ){
            sourceLoc = order.OrderPickupLoc.getLatLng();
        }

        if( order.OrderDropLoc != null){
            destinyLoc = order.OrderDropLoc.getLatLng();
        }

        this.addPathPolyLine(order);


        if(listener != null)
            listener.onCompleted();
    }


    public void invalidateTravelPath(final TravelOrder order ){

        if(order.OrderDistance > 0 && order.OrderDuration > 0){

            final String strDistance = String.format("%.1f Km", order.OrderDistance/1000 );
            int hours =  (int)(order.OrderDuration / 3600);
            int minutes =  (int)(Math.round((order.OrderDuration % 3600) / 60));
            final String strDuration = (hours > 0) ?  String.format("%d giờ %d phút", hours, minutes ) :  String.format("%d phút", minutes );

            lblTravelDistance.setText(strDistance + " - " + strDuration );


        }else{

            lblTravelDistance.setText("");
        }

    }


    public void invalidateDate(final TravelOrder order){

        Date date ;

        if(order.OrderPickupTime != null){

            date = order.OrderPickupTime;

        }else{

            date = order.createdAt;

        }

        if(date == null){
            lblDateTime.setText( "");
            return;
        }

        String strPickupTime = "";
        if(DateTimeHelper.isNow(date,5)){

            strPickupTime = "ngay bây giờ.";

        }else{


            String strDate =  new SimpleDateFormat("HH:mm").format(date);

            if(DateTimeHelper.isToday(date)){

                strPickupTime = strDate + " hôm nay";

            }else if(DateTimeHelper.isYesterday(date)){

                strPickupTime = strDate + " hôm qua";

            }else if(DateTimeHelper.isTomorrow(date)){

                strPickupTime = strDate + " ngày mai";

            }else{

                String strDate2 =  new SimpleDateFormat("dd/MM").format(date);
                strPickupTime = strDate + " ngày " + strDate2;
            }



        }

        lblDateTime.setText(strPickupTime);
    }

    public void addPathPolyLine(final TravelOrder order){

        if(sourceLoc == null || destinyLoc == null ) {
            if (pathPolyLine != null)
                pathPolyLine.remove();
            invalidateMarkers();
            return;
        }

        String strEncodedPolyline = null;

        if(order.OrderPolyline != null)
            strEncodedPolyline = order.OrderPolyline;

        if(strEncodedPolyline == null){

            invalidateMarkers();

            return;

        }

        new GoogleDirectionHelper(mapFragment.getContext()).requestPolyline(gmsMapView, strEncodedPolyline, new PolylineOptions().width(3).color(Color.rgb(73, 139, 199)), new GoogleDirectionHelper.RequestPolylineResult() {
            @Override
            public void onCompleted(Polyline polyline) {

                if (polyline != null) {
                    if (pathPolyLine != null)
                        pathPolyLine.remove();
                    pathPolyLine = polyline;
                }

                invalidateMarkers();
            }
        });

    }

    private void invalidateMarkers() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        if(pathPolyLine != null) {
            for (LatLng pos : pathPolyLine.getPoints()) {
                builder.include(pos);
            }
        }

        if(sourceLoc != null)
            builder.include(sourceLoc);

        if(destinyLoc != null)
            builder.include(destinyLoc);

        LatLngBounds bounds = builder.build();
        gmsMapView.setPadding(100, 80, 100, 100);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        gmsMapView.moveCamera(cameraUpdate);


        if(sourceLoc != null && mSourceMarker == null){

            Picasso.with(cellView.getContext()).load(R.drawable.sourcepin).resize(50, 50).centerCrop().into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if(gmsMapView != null){

                        mSourceMarker = gmsMapView.addMarker(new MarkerOptions()
                                .position(sourceLoc)
                                .title("Điểm đi")
                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                .anchor((float) 0.5,1)
                        );
                        mSourceMarker.setVisible(true);
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

        if(destinyLoc != null && mDestinyMarker == null) {

            Picasso.with(cellView.getContext()).load(R.drawable.destinypin).resize(50, 50).centerCrop().into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if(gmsMapView != null){
                        mDestinyMarker = gmsMapView.addMarker(new MarkerOptions()
                                .position(destinyLoc)
                                .title("Điểm đến")
                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                .anchor((float) 0.5,1)
                        );
                        mDestinyMarker.setVisible(true);

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
    }


    public void loadEstPrice(final TravelOrder order,final Completion listener){


            if(order != null){
                TravelOrderController.CalculateOrderPrice(SCONNECTING.driverManager.CurrentDriver.id,order.User, order.OrderDistance, order.Currency,order.OrderPickupLoc.getLatLng(), new GetDoubleValueListener() {
                    @Override
                    public void onCompleted(Boolean success,Double value) {

                        if(success)
                            estPrice = value;

                        lblCurrentPrice.setText((estPrice != null && estPrice > 0) ? RegionalHelper.toCurrencyOfCountry(estPrice,SCONNECTING.driverManager.CurrentDriver.Country) : "");

                    }
                });
            }


    }
}
