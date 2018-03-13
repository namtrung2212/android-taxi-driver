package com.sconnecting.driverapp.ui.taxi.order.placesearcher;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.arlib.floatingsearchview.util.view.SearchInputView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.ui.taxi.order.OrderScreen;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import com.sconnecting.driverapp.google.GooglePlaceSearcher;
import com.sconnecting.driverapp.google.GooglePlaceSuggestion;

public class PlaceSearcher{


    public FloatingSearchView mSearchView;
    public GooglePlaceSearcher mPlaceSearcher;

    public GoogleApiClient mGoogleApiClient;


    public OrderScreen parent;


    public PlaceSearcher(OrderScreen screen) {

        parent = screen;


        initControls();
    }

    private void initControls() {

        initPlaceSearcher();

        mSearchView = (FloatingSearchView) parent.findViewById(R.id.floating_search_view);

        SearchInputView mSearchInput = (SearchInputView) mSearchView.findViewById(com.arlib.floatingsearchview.R.id.search_bar_text);
        mSearchInput.setTextSize(12);

        View mSearchInputParent = (View) mSearchView.findViewById(com.arlib.floatingsearchview.R.id.search_input_parent);
        mSearchInputParent.setTranslationX(-Util.dpToPx(25));

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    mSearchView.showProgress();
                    mPlaceSearcher.search(newQuery,new GooglePlaceSearcher.SearchPlaceListener(){

                        @Override
                        public void onCompleted(List<GooglePlaceSuggestion> newSuggestions) {
                            mSearchView.swapSuggestions(newSuggestions);
                            mSearchView.hideProgress();
                        }

                    });

                }
            }

        });


        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                GooglePlaceSuggestion suggestion = (GooglePlaceSuggestion) searchSuggestion;

                if(mGoogleApiClient == null){

                    mGoogleApiClient = new GoogleApiClient
                            .Builder(parent)
                            .addApi(Places.GEO_DATA_API)
                            .addApi(Places.PLACE_DETECTION_API)
                            .enableAutoManage(parent, new GoogleApiClient.OnConnectionFailedListener(){

                                @Override
                                public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                                }
                            })
                            .build();
                }

                Places.GeoDataApi.getPlaceById(mGoogleApiClient, suggestion.mID)
                        .setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                    final Place myPlace = places.get(0);
                                    parent.mMapView.moveToLocation(myPlace.getLatLng(),(float)14);
                                }
                                places.release();
                            }
                        });

            }

            @Override
            public void onSearchAction(String query) {

            }
        });

        mSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {

                parent.showLeftMenu(true);
                mSearchView.setLeftMenuOpen(false);
                mSearchView.closeMenu(true);

            }

            @Override
            public void onMenuClosed() {

            }
        });

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView,final ImageView leftIcon,
                                         final TextView textView, SearchSuggestion item, int itemPosition) {

                final GooglePlaceSuggestion suggestion = (GooglePlaceSuggestion) item;




                Picasso.with(parent).load(R.drawable.location).resize(38, 50).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        String textColor = "#000000";
                        String textLight = "#787878";

                        leftIcon.setPadding(10,6,5,6);
                        leftIcon.getLayoutParams().width = 40;
                        leftIcon.setImageBitmap(bitmap);
                        leftIcon.setAlpha(.36f);
                        textView.setTextColor(Color.parseColor(textColor));
                        if(suggestion.getDetail().isEmpty()) {
                            textView.setText(Html.fromHtml("<h2>" + suggestion.getName() + "</h2>"));
                            textView.setPadding(0,15,0,0);
                        }
                        else {
                            textView.setText(Html.fromHtml("<h2>" + suggestion.getName() + "</h2><br><p>" + suggestion.getDetail() + "</p>"));
                            textView.setLineSpacing(-13, 1);
                            textView.setPadding(0,0,0,0);
                        }
                        textView.setGravity(Gravity.CENTER_VERTICAL);
                        textView.getLayoutParams().height = 70;
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });

            }

        });
    }

    private void initPlaceSearcher() {
        mPlaceSearcher = new GooglePlaceSearcher();
    }

}
