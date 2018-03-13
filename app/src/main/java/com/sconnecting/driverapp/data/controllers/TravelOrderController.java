package com.sconnecting.driverapp.data.controllers;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sconnecting.driverapp.base.listener.GetDoubleValueListener;
import com.sconnecting.driverapp.base.listener.GetItemsListener;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.base.listener.GetStringValueListener;
import com.sconnecting.driverapp.data.entity.BaseController;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.data.storages.server.ServerStorage;
import com.sconnecting.driverapp.base.CryptoHelper;
import com.sconnecting.driverapp.data.models.*;

import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by TrungDao on 7/26/16.
 */




public class TravelOrderController extends BaseController<TravelOrder> {


    public TravelOrderController()
    {
        super(TravelOrder.class);
    }

    public static void UserSendRequestToDriver(String orderId,String driverId, final GetOneListener listener) {

        new BaseController<>(TravelOrder.class).getOne("UserSendRequestToDriver","orderId=" + orderId + "&driverId=" + driverId,listener);

    }

    public static void UserCancelRequestingToDriver(String orderId, final GetOneListener listener) {

        new BaseController<>(TravelOrder.class).getOne("UserCancelRequestingToDriver","orderId=" + orderId ,listener);

    }

    public static void UserAcceptBidding(String biddingId, final GetOneListener listener) {

        new BaseController<>(TravelOrder.class).getOne("UserAcceptBidding/" + biddingId,null,listener);

    }

    public static void UserCancelAcceptingBidding(String biddingId, final GetOneListener listener) {

        new BaseController<>(TravelOrder.class).getOne("UserCancelAcceptingBidding/" + biddingId,null,listener);

    }

    public static void SetRejectedDriverRequestToOpen(String orderId, final GetOneListener listener) {

        new BaseController<>(TravelOrder.class).getOne("SetRejectedDriverRequestToOpen" ,"orderId=" + orderId ,listener);

    }

    public static void DriverAcceptRequest(String orderId,String driverId, final GetOneListener listener) {

        new BaseController<>(TravelOrder.class).getOne("DriverAcceptRequest","orderId=" + orderId + "&driverId=" + driverId,listener);

    }

    public static void DriverRejectRequest(String orderId,String driverId, final GetOneListener listener) {

        new BaseController<>(TravelOrder.class).getOne("DriverRejectRequest","orderId=" + orderId + "&driverId=" + driverId,listener);

    }

    public static void DriverStartPicking(String orderId,String driverId, final GetOneListener listener) {

        new BaseController<>(TravelOrder.class).getOne("DriverStartPicking","orderId=" + orderId + "&driverId=" + driverId,listener);

    }

    public static void DriverStartTrip(String orderId,String driverId,LatLng coordinate, final GetOneListener listener) {

        String filter = "orderId=" + orderId + "&driverId=" + driverId
                + "&voidLong=" +  Location.convert(coordinate.longitude, Location.FORMAT_DEGREES).replace(",",".")
                + "&voidLat="  +  Location.convert(coordinate.latitude, Location.FORMAT_DEGREES).replace(",",".");

        new BaseController<>(TravelOrder.class).getOne("DriverStartTrip",filter,listener);

    }

    public static void UserVoidOrder(String orderId,LatLng coordinate, final GetOneListener listener) {

        String filter ="orderId=" + orderId
                + "&voidLong=" +  Location.convert(coordinate.longitude, Location.FORMAT_DEGREES).replace(",",".")
                + "&voidLat="  +  Location.convert(coordinate.latitude, Location.FORMAT_DEGREES).replace(",",".");

        new BaseController<>(TravelOrder.class).getOne("UserVoidOrder" ,filter ,listener);

    }

    public static void DriverVoidOrder(String orderId,LatLng coordinate, final GetOneListener listener) {

        String filter ="orderId=" + orderId
                + "&voidLong=" +  Location.convert(coordinate.longitude, Location.FORMAT_DEGREES).replace(",",".")
                + "&voidLat="  +  Location.convert(coordinate.latitude, Location.FORMAT_DEGREES).replace(",",".");

        new BaseController<>(TravelOrder.class).getOne("DriverVoidOrder" ,filter ,listener);

    }

    public static void DriverFinishTrip(String orderId,String driverId,LatLng coordinate, final GetOneListener listener) {

        String filter = "orderId=" + orderId + "&driverId=" + driverId
                + "&voidLong=" +  Location.convert(coordinate.longitude, Location.FORMAT_DEGREES).replace(",",".")
                + "&voidLat="  +  Location.convert(coordinate.latitude, Location.FORMAT_DEGREES).replace(",",".");

        new BaseController<>(TravelOrder.class).getOne("DriverFinishTrip",filter,listener);

    }

    public static void DriverReceivedCash(String orderId,String driverId, final GetOneListener listener) {

        String filter = "orderId=" + orderId + "&driverId=" + driverId;

        new BaseController<>(TravelOrder.class).getOne("DriverReceivedCash",filter,listener);

    }

    public static void UserPayByPersonalCard(String orderId,String cardId,String cardCurrency, final GetOneListener listener) {

        String filter = "orderId=" + orderId + "&cardId=" + cardId+ "&cardCurry=" + cardCurrency;

        new BaseController<>(TravelOrder.class).getOne("UserPayByPersonalCard",filter,listener);

    }

    public static void UserPayByBusinessCard(String orderId,String cardId,String cardCurrency, final GetOneListener listener) {

        String filter = "orderId=" + orderId + "&cardId=" + cardId+ "&cardCurry=" + cardCurrency;

        new BaseController<>(TravelOrder.class).getOne("UserPayByBusinessCard",filter,listener);

    }
    public static void RecalculateTripOrder(String orderId, final GetOneListener listener) {

        new BaseController<>(TravelOrder.class).getOne("RecalculateTripOrder","orderId=" + orderId.toString(),listener);

    }


    public static void HostCreateTripMate(String orderId, final GetOneListener listener) {

        new BaseController<>(TravelOrder.class).getOne("HostCreateTripMate","orderId=" + orderId.toString(),listener);

    }

    public static void GetNearestMateHostOrders(String orderId,  final GetStringValueListener listener){

        String filter = "orderId=" + orderId.toString();

        String url = ServerStorage.ServerURL + "/TravelOrder/GetNearestMateHostOrders?" + filter;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Token",Token());
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

    public static void UserPreviewMateHostOrder(String orderId,String hostId, final GetOneListener listener) {

        new BaseController<>(TravelOrder.class).getOne("UserPreviewMateHostOrder","orderId=" + orderId.toString() + "&" + "hostId=" + hostId.toString(),listener);

    }

    public static void GetRequestingMateOrder(String hostId, final GetItemsListener listener) {

        String filter = "MateHostOrder=" + hostId.toString() +"&MateStatus=Requested";

        new BaseController<>(TravelOrder.class).get("selectall",filter,listener);

    }

    public static void GetMateSubOrdersByHostId(String hostId, final GetItemsListener listener) {

        String filter = "hostId=" + hostId.toString();

        new BaseController<>(TravelOrder.class).get("GetMateSubOrdersByHostId",filter,listener);

    }

    public static void MemberRequestToJoinTripMate(String orderId,String hostId, final GetOneListener listener) {

        String filter =  "orderId=" + orderId.toString() + "&hostId=" + hostId.toString();

        new BaseController<>(TravelOrder.class).getOne("MemberRequestToJoinTripMate" ,filter,listener);

    }

    public static void HostAcceptMateMember(String orderId, final GetOneListener listener) {

        new BaseController<>(TravelOrder.class).getOne("HostAcceptMateMember/" + orderId.toString() ,null,listener);

    }

    public static void HostRejectMateMember(String orderId, final GetOneListener listener) {

        new BaseController<>(TravelOrder.class).getOne("HostRejectMateMember/" + orderId.toString() ,null,listener);

    }

    public static void MemberLeaveFromTripMate(String orderId, final GetOneListener listener) {

        new BaseController<>(TravelOrder.class).getOne("MemberLeaveFromTripMate/" + orderId.toString() ,null,listener);

    }

    public static void HostCloseTripMate(String orderId, final GetOneListener listener) {

        new BaseController<>(TravelOrder.class).getOne("HostCloseTripMate/" + orderId.toString() ,null,listener);

    }

    public static void HostVoidTripMate(String orderId, final GetOneListener listener) {

        new BaseController<>(TravelOrder.class).getOne("HostVoidTripMate/" + orderId.toString() ,null,listener);

    }

    public static void GetOpenBiddingsByOrder(String orderId , final GetItemsListener listener){

        new BaseController<>(DriverBidding.class).get("GetOpenBiddingsByOrder/" + orderId.toString(),null,listener );

    }

    public static void GetBiddingsByDriver(String driverId , final GetItemsListener listener){

        new BaseController<>(DriverBidding.class).get("GetBiddingsByDriver/" + driverId.toString(),null,listener );

    }

    public static void GetLastOpenningOrderByUser(String userId, final GetOneListener listener){


        new TravelOrderController().getOne("GetLastOpenningOrderByUser/" + userId.toString(),null,listener );


    }

    public static void GetAllOrderByUser(String userId, Integer page,Integer  pagesize, final GetItemsListener listener) {


        String filter  = "User=" + userId.toString();

        if(page != null){
            filter = filter + "&page=" + page.toString();
        }

        if(pagesize != null){
            filter = filter + "&pagesize=" + pagesize.toString();
        }

        new TravelOrderController().get("selectall", filter,listener);
    }

    public static void GetAllOrderByDriver(String driverId, Integer page,Integer  pagesize, final GetItemsListener listener) {


        String filter  = "Driver=" + driverId.toString();

        if(page != null){
            filter = filter + "&page=" + page.toString();
        }

        if(pagesize != null){
            filter = filter + "&pagesize=" + pagesize.toString();
        }

        new TravelOrderController().get("selectall", filter,listener);
    }

    public static void GetNotYetPaidOrderByUser(String userId, final GetItemsListener listener ){

        new TravelOrderController().get("GetNotYetPaidOrderByUser/" + userId.toString(),null,listener );

    }

    public static void GetNotYetPickupOrderByUser(String userId, final GetItemsListener listener ){

        new TravelOrderController().get("GetNotYetPickupOrderByUser/" + userId.toString(),null,listener );

    }

    public static void GetOnTheWayOrderByUser(String userId,  final GetItemsListener listener){

        new TravelOrderController().get("GetOnTheWayOrderByUser/" + userId.toString(),null,listener );

    }

    public static void GetOrderComments(String driverId ,Integer page,Integer pagesize,  final GetItemsListener listener ){

        String filter = null;

        if(page != null){
            filter =  "page=" + page.toString();
        }

        if(pagesize != null){

            if(filter == null){
                filter = "pagesize=" + pagesize.toString();
            }else{
                filter = filter + "&pagesize=" + pagesize.toString();
            }
        }


        new TravelOrderController().get("comment/" + driverId.toString(),filter,listener );

    }

    public static void GetLastOpenningOrderByDriver(String driverId, final GetOneListener listener){

        new TravelOrderController().getOne("GetLastOpenningOrderByDriver/" + driverId.toString(),null,listener );

    }

    public static void CalculateOrderPrice(String driverId,String userId,  Double distance , String currency,LatLng pickupLoc, final GetDoubleValueListener listener) {

        new BaseController<>(TravelComPrice.class).getDouble("CalculateTripPrice",
                "Driver=" + driverId.toString() +"&User=" + userId.toString()+"&PickupLat=" + pickupLoc.latitude+"&PickupLong=" + pickupLoc.longitude
                        +"&Currency=" + currency.toString() + "&Distance=" + distance.toString(), listener);

    }

    public static void GetNotYetPaidOrderByDriver(String driverId, final GetItemsListener listener ){

        new TravelOrderController().get("GetNotYetPaidOrderByDriver/" + driverId.toString(),null,listener );

    }

    public static void GetNotYetPickupOrderByDriver(String driverId, final GetItemsListener listener){

        new TravelOrderController().get("GetNotYetPickupOrderByDriver/" + driverId.toString(),null,listener );

    }

    public static void GetStoppedOrderByDriver(String driverId,Integer page,Integer pagesize, final GetItemsListener listener){

        String filter = "";

        if(page != null){
            filter = "page=" + page.toString();
        }

        if(pagesize != null){
            filter = filter + "&pagesize=" + pagesize.toString();
        }

        new TravelOrderController().get("GetStoppedOrderByDriver/" + driverId.toString(),filter,listener );

    }

    public static void GetNearestOpenOrders(LatLng coordinate,Integer page,Integer pagesize,  final GetItemsListener listener) {

        String filter = "longtitude=" + coordinate.longitude + "&latitude=" + coordinate.latitude;

        if (page != null) {
            filter = filter + "&page=" + page.toString();
        }

        if (pagesize != null) {
            filter = filter + "&pagesize=" + pagesize.toString();
            pagesize.toString();
        }

        new TravelOrderController().get("nearest", filter, listener);
    }

    public static void GetLastChattingMessage(String orderId, final GetOneListener listener) {

        String filter = "Order=" + orderId.toString() +"&sortField=createdAt&sort=-1&pagesize=1&page=1";

        new BaseController<>(TravelOrderChatting.class).get("selectall", filter, new GetItemsListener() {
            @Override
            public void onGetItems(Boolean success, List list) {
                if(list != null && list.size() > 0){
                    if(listener !=null)
                        listener.onGetOne(true, (BaseModel) list.get(0));
                }else {
                    if(listener !=null)
                        listener.onGetOne(true, null);

                }
            }
        });

    }

    public static void CountNotYetViewedMessageByUser(String orderId, final GetDoubleValueListener listener) {

        String filter = "Order=" + orderId.toString() + "&IsUser=0&IsViewed=0";
        new BaseController<>(TravelOrderChatting.class).getDouble("count", filter,listener);

    }

    public static void CountNotYetViewedMessageByDriver(String orderId, final GetDoubleValueListener listener) {

        String filter = "Order=" + orderId.toString() + "&IsUser=1&IsViewed=0";
        new BaseController<>(TravelOrderChatting.class).getDouble("count", filter,listener);

    }

    public static void SetAllMessageToViewedByUser(String orderId, final GetDoubleValueListener listener) {

        new BaseController<>(TravelOrderChatting.class).getDouble("SetAllMessageToViewedByUser/"+ orderId.toString(), null,listener);

    }

    public static void SetAllMessageToViewedByDriver(String orderId, final GetDoubleValueListener listener) {

        new BaseController<>(TravelOrderChatting.class).getDouble("SetAllMessageToViewedByDriver/"+ orderId.toString(), null,listener);

    }


    public static void GetOnTheWayOrderByDriver(String driverId,  final GetItemsListener listener){

        new TravelOrderController().get("GetOnTheWayOrderByDriver/" + driverId.toString(),null,listener );

    }


    public static void GetNearestLateOrders(LatLng coordinate,String vehicleType,String qualityService,  final GetItemsListener listener){

        String filter = "longtitude=" + coordinate.longitude + "&latitude=" + coordinate.latitude+
                "&vehicleType=" + vehicleType + "&qualityService=" + qualityService;

        new TravelOrderController().get("GetNearestLateOrders",filter,listener );

    }







}




