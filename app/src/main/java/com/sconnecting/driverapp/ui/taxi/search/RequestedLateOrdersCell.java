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
import com.sconnecting.driverapp.data.models.DriverBidding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TrungDao on 10/14/16.
 */


public  class RequestedLateOrdersCell extends RecyclerView.ViewHolder implements OnMapReadyCallback {

    public View cellView;
    MapView mapFragment;
    public TextView lblPickupLocation;
    public TextView lblDropLocation;
    public TextView lblDateTime;
    public TextView lblStatus;
    public TextView lblCurrentPrice;


    LatLng sourceLoc;
    LatLng destinyLoc;

    public GoogleMap gmsMapView;
    Polyline pathPolyLine;
    Marker mSourceMarker;
    Marker mDestinyMarker;


    DriverBidding driverBidding;
    public Double estPrice;

    public RequestedLateOrdersCell(View view) {
        super(view);

        cellView = view;

        lblPickupLocation = (TextView) view.findViewById(R.id.lblPickupLocation);
        lblDropLocation = (TextView) view.findViewById(R.id.lblDropLocation);
        lblDateTime = (TextView) view.findViewById(R.id.lblDateTime);
        lblStatus = (TextView) view.findViewById(R.id.lblStatus);
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

                                mOnItemClickListener.onItemClick(driverBidding);
                            }
                        });
                    }
                });

                invalidateMap(driverBidding,null);
            }
        });

    }

    public interface OnItemClickListener {
        void onItemClick(DriverBidding item);
    }

    RequestedLateOrdersCell.OnItemClickListener mOnItemClickListener;
    public void bind(final DriverBidding bidding, final RequestedLateOrdersCell.OnItemClickListener listener){

        mOnItemClickListener = listener;
        driverBidding = bidding;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        mOnItemClickListener.onItemClick(driverBidding);
                    }
                });
            }
        });

    }


    public void updateWithModel(DriverBidding bidding){

        driverBidding = bidding;
        lblPickupLocation.setText(bidding.OrderPickupPlace != null ?  bidding.OrderPickupPlace : "");
        lblDropLocation.setText( bidding.OrderDropPlace != null ?  bidding.OrderDropPlace : "");
        invalidateDate(bidding);
        invalidateStatus(bidding);
        loadEstPrice(bidding,null);
    }

    public void invalidateStatus(final DriverBidding bidding ){

        String strStatus  = "";


        if(bidding.Status.equals("Open")) {

            strStatus = "Chưa phản hồi";

        }else if(bidding.Status.equals("Expired")) {

            strStatus = "Đã hết hạn";

        }else if(bidding.Status.equals("Rejected")) {

            strStatus = "Đã từ chối";

        }else if(bidding.Status.equals("Accepted")) {

            strStatus = "Chấp nhận";

        }

        lblStatus.setText( strStatus.toUpperCase());


    }

    public void invalidateMap(final DriverBidding bidding, final Completion listener){

        sourceLoc = null;
        destinyLoc = null;

        if( bidding.OrderPickupLoc != null ){
            sourceLoc = bidding.OrderPickupLoc.getLatLng();
        }

        if( bidding.OrderDropLoc != null){
            destinyLoc = bidding.OrderDropLoc.getLatLng();
        }

        this.addPathPolyLine(bidding);

        if(listener != null)
            listener.onCompleted();
    }


    public void invalidateDate(final DriverBidding bidding){

        Date date =null ;

        if(bidding.OrderPickupTime != null){

            date = bidding.OrderPickupTime;

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

    public void addPathPolyLine(final DriverBidding bidding){

        if(sourceLoc == null || destinyLoc == null ) {
            if (pathPolyLine != null)
                pathPolyLine.remove();
            invalidateMarkers();
            return;
        }

        String strEncodedPolyline = null;

        if(bidding.OrderPolyline != null)
            strEncodedPolyline = bidding.OrderPolyline;

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


    public void loadEstPrice(final DriverBidding bidding,final Completion listener){


            if(bidding != null){
                TravelOrderController.CalculateOrderPrice(SCONNECTING.driverManager.CurrentDriver.id,bidding.User, bidding.OrderDistance, bidding.Currency,bidding.OrderPickupLoc.getLatLng(), new GetDoubleValueListener() {
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
