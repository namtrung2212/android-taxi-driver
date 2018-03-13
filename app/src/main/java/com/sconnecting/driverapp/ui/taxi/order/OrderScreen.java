package com.sconnecting.driverapp.ui.taxi.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.sconnecting.driverapp.base.BaseActivity;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.ui.taxi.order.controlpanel.ControlPanelView;
import com.sconnecting.driverapp.ui.taxi.order.map.*;

import com.sconnecting.driverapp.ui.taxi.order.monitoring.MonitoringView;
import com.sconnecting.driverapp.ui.taxi.order.placesearcher.PlaceSearcher;
import com.sconnecting.driverapp.data.models.TravelOrder;
import com.sconnecting.driverapp.ui.taxi.order.review.ReviewView;

import org.parceler.Parcels;


/**
 * Created by TrungDao on 8/1/16.
 */


public class OrderScreen extends BaseActivity implements  ActivityCompat.OnRequestPermissionsResultCallback {

    public MapView mMapView;
    public MapLocation mMapLocation;
    public MapMarkerManager mMapMarkerManager;

    public PlaceSearcher mPlaceSearcher;

    public ControlPanelView mControlPanelView;
    public ReviewView mReviewView;
    public MonitoringView mMonitoringView;

    public boolean isMapReady =false;

    public interface OnMapReadyListener {
        void onReady();
    }

    public interface OnLocationReadyListener {
        void onReady();
    }

    public OnMapReadyListener mapReadyListener;
    public OnLocationReadyListener locationReadyListener;


    public TravelOrder CurrentOrder() {

        if( SCONNECTING.orderManager == null)
            return null;

        return SCONNECTING.orderManager.currentOrder;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(mMapView != null)
            mMapView.initMap();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SCONNECTING.orderScreen = this;

        initControls(new Completion() {
            @Override
            public void onCompleted() {

                SCONNECTING.locationHelper.getLocation();

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    public void initControls(final Completion listener) {

        mapReadyListener = new OnMapReadyListener() {
            @Override
            public void onReady() {

                isMapReady = true;
                reloadOrder();
            }
        };

        setContentView(R.layout.taxi_order);

        mPlaceSearcher = new PlaceSearcher(this);
        mMapLocation = new MapLocation(this);
        mMapMarkerManager = new MapMarkerManager(this);

        showToolbar(false);

        if(listener != null)
            listener.onCompleted();
    }

    @Override
    protected void onStart() {
        super.onStart();

        reloadOrder();

    }

    private void reloadOrder() {

        if(isMapReady) {
            TravelOrder order = Parcels.unwrap(getIntent().getParcelableExtra("CurrentOrder"));
            if (order == null) {
                Log.d("ORDER", "reloadOrder : null");
            } else {
                SCONNECTING.orderManager.reset(order, true, null);
            }
        }
    }


    public void invalidateUI(final Boolean isFirstTime, final Completion listener) {

        if(isFirstTime){

            mMapView.invalidate(isFirstTime,new Completion() {
                @Override
                public void onCompleted() {

                    mControlPanelView.invalidate(isFirstTime,new Completion() {
                        @Override
                        public void onCompleted() {

                            mReviewView.invalidate(isFirstTime, new Completion() {
                                @Override
                                public void onCompleted() {

                                    mMonitoringView.invalidate(isFirstTime,listener);
                                }
                            });
                        }
                    });
                }
            });

            return;

        }

        mMapView.invalidate(false,new Completion() {
            @Override
            public void onCompleted() {

                mControlPanelView.invalidate(false,new Completion() {
                    @Override
                    public void onCompleted() {

                        mMonitoringView.invalidate(false,listener);

                    }
                });
            }
        });

    }


}