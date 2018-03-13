package com.sconnecting.driverapp.google;

import android.annotation.TargetApi;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.SCONNECTING;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.sconnecting.driverapp.base.DownloadHelper;


public class GooglePlaceSearcher {

    public static String placeKey = "AIzaSyBArZgCF0ZcAyHsIqVXbnVg-LbT-ySi6L0";

    public Integer distance = 5000;

    public GooglePlaceSearcher(){

    }

    public interface SearchPlaceListener {

        public void onCompleted(List<GooglePlaceSuggestion> newSuggestions);
    }
    public void search(String strQuery,SearchPlaceListener listener){

        SearchPlacesTask searchPlacesTask = new SearchPlacesTask(listener);
        searchPlacesTask.execute(strQuery);


    }
    private class SearchPlacesTask extends AsyncTask<String, Void, String>{

        private SearchPlaceListener mSearchPlaceListener;

        public SearchPlacesTask(SearchPlaceListener listener){
            mSearchPlaceListener = listener;
        }
        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=" + placeKey;

            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=address";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key+"&language=vi&location="
                    + Double.toString(SCONNECTING.locationHelper.location.getLatitude())+ ","
                    + Double.toString(SCONNECTING.locationHelper.location.getLongitude()) + "" +
                    "&radius=" + distance;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{
                // Fetching the data from we service
                data = DownloadHelper.downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            SearchPlacesParserTask parserTask = new SearchPlacesParserTask(mSearchPlaceListener);

            parserTask.execute(result);
        }
    }
    private class SearchPlacesParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        private SearchPlaceListener mSearchPlaceListener;
        JSONObject jObject;

        public SearchPlacesParserTask(SearchPlaceListener listener){
            mSearchPlaceListener = listener;
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            GooglePlaceJSONParser placeJsonParser = new GooglePlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            List<GooglePlaceSuggestion> newSuggestions = new ArrayList<GooglePlaceSuggestion>();

            for (HashMap<String, String> map : result) {

                GooglePlaceSuggestion suggestion = new GooglePlaceSuggestion(map.get("description"), map.get("place_id"));
                newSuggestions.add(suggestion);

            }


            if(mSearchPlaceListener != null )
                mSearchPlaceListener.onCompleted(newSuggestions);

        }
    }

    public interface GetPlaceListener {

        public void onCompleted(LocationInfo locAddress);
    }
    public void getAddress(LatLng location, GetPlaceListener listener){

        GetPlaceTask getPlaceTask = new GetPlaceTask(listener);
        getPlaceTask.execute(location);

    }
    private class GetPlaceTask extends AsyncTask<LatLng, Void, LocationInfo> {

        GetPlaceListener mGetPlaceListener;

        public  GetPlaceTask(GetPlaceListener listener){
            mGetPlaceListener = listener;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected LocationInfo doInBackground(LatLng... jsonData) {

            LocationInfo result = new LocationInfo();
            Geocoder geocoder = new Geocoder(AppDelegate.getApplication().getApplicationContext(), Locale.forLanguageTag("vi-VN"));

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(jsonData[0].latitude,jsonData[0].longitude,1);

            } catch (IOException ioException) {

            } catch (IllegalArgumentException illegalArgumentException) {

            }


            if (addresses != null && addresses.size()  != 0) {

                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();

                for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }

                String strAddress = TextUtils.join(System.getProperty("line.separator"),addressFragments);
                strAddress = strAddress.replace(", " + address.getCountryName(),"");
                result.Address = strAddress;
                result.CountryCode = address.getCountryCode();
                result.CountryName = address.getCountryName();
                result.Locality = address.getLocality();
                result.PostalCode = address.getPostalCode();
                result.Province = address.getAdminArea();
                return result;
            }

            return null;
        }

        @Override
        protected void onPostExecute(LocationInfo locAddress) {


            if(mGetPlaceListener != null )
                mGetPlaceListener.onCompleted(locAddress);

        }
    }
}
