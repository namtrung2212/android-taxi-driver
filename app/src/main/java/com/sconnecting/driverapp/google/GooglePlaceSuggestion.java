package com.sconnecting.driverapp.google;

/**
 * Created by TrungDao on 7/30/16.
 */


import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;


public class GooglePlaceSuggestion implements SearchSuggestion {

    public String mPlaceName;
    public String mPlaceDetail;
    public String mID;

    public GooglePlaceSuggestion(String suggestion,String id) {
        this.mPlaceName = suggestion.split(",")[0].trim();
        if(suggestion.indexOf(",") >= 0)
            this.mPlaceDetail = suggestion.substring(suggestion.indexOf(",") +1 ,suggestion.length()).trim();
        else
            this.mPlaceDetail = "";

         this.mID = id;
    }

    public GooglePlaceSuggestion(Parcel source) {
        String[] arr = new String[3];
        source.readStringArray(arr);

        this.mPlaceName = arr[0];
        this.mPlaceDetail = arr[1];
        this.mID = arr[2];
    }


    @Override
    public String getBody() {
        return mPlaceName;
    }

    public String getName() {
        return mPlaceName;
    }

    public String getDetail() {
        return mPlaceDetail;
    }

    public static final Creator<GooglePlaceSuggestion> CREATOR = new Creator<GooglePlaceSuggestion>() {
        @Override
        public GooglePlaceSuggestion createFromParcel(Parcel in) {
            return new GooglePlaceSuggestion(in);
        }

        @Override
        public GooglePlaceSuggestion[] newArray(int size) {
            return new GooglePlaceSuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{mPlaceName,mPlaceDetail,mID});
    }
}