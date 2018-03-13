package com.sconnecting.driverapp.google;

import android.content.Context;
import android.util.DisplayMetrics;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sconnecting.driverapp.base.listener.GetStringValueListener;
import com.sconnecting.driverapp.data.entity.BaseController;
import com.sconnecting.driverapp.data.storages.server.ServerStorage;
import com.sconnecting.driverapp.base.CryptoHelper;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * Created by TrungDao on 9/30/16.
 */

public class GoogleDirectionHelper {


    private Context mContext = null;


    public GoogleDirectionHelper(Context context) {
        mContext = context;
    }


    public interface RequestDirectionResult {
        public void onCompleted(Polyline polyline,  double distance,double duration);
    }


    public interface RequestPolylineResult{
        public void onCompleted(Polyline polyline);
    }

    public void requestPolyline(final GoogleMap gm,LatLng start, LatLng end,final PolylineOptions po, final RequestDirectionResult listener) {

        request(start, end, new GetStringValueListener() {
            @Override
            public void onCompleted(Boolean success, String value) {

                if(success == false || value == null){

                    if(listener != null)
                        listener.onCompleted(null,-1,-1);
                }

                try {

                    JSONObject reader = new JSONObject(value);
                    String encoded = reader.getString("polyline");
                    ArrayList<LatLng> direction = decodePoly(encoded);
                    Polyline polyline = gm.addPolyline(po.addAll(direction).width(dpToPx((int)po.getWidth())));

                    double distance = reader.getDouble("distance");
                    double duration = reader.getDouble("duration");

                    if(listener != null)
                        listener.onCompleted(polyline,distance,duration);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public void requestPolyline(final GoogleMap gm,String encodedPolyline,final PolylineOptions po, final RequestPolylineResult listener) {

            ArrayList<LatLng> direction = decodePoly(encodedPolyline);
            Polyline polyline = gm.addPolyline(po.addAll(direction).width(dpToPx((int)po.getWidth())));

            if(listener != null)
                listener.onCompleted(polyline);


    }


        public void request(LatLng start, LatLng end,final GetStringValueListener listener) {

        String url = ServerStorage.ServerURL + "/googlemap/getDirections?orgLat=" + start.latitude + "&orgLong=" + start.longitude +
                "&destLat=" + end.latitude + "&destLong=" + end.longitude;


        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",BaseController.Token());
        client.addHeader("Hash", CryptoHelper.generateHashKey(url));
        client.get(url, new TextHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                if(listener != null)
                    listener.onCompleted(true,response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {

                if(listener != null)
                    listener.onCompleted(false,null);
            }


        });
    }



    public Polyline addPolyline(GoogleMap gm, ArrayList<LatLng> direction, PolylineOptions po) {

        return gm.addPolyline(po.addAll(direction)
                .width(dpToPx((int)po.getWidth())));
    }

    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double)lat / 1E5, (double)lng / 1E5);
            poly.add(position);
        }
        return poly;
    }


    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
