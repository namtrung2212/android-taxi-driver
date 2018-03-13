package com.sconnecting.driverapp.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.google.firebase.messaging.RemoteMessage;
import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.AppRootActivity;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.base.RegionalHelper;
import com.sconnecting.driverapp.base.listener.GetBoolValueListener;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.data.controllers.TravelOrderController;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.ui.taxi.order.OrderScreen;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.app.Notification.VISIBILITY_PUBLIC;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.view.View.GONE;

/**
 * Created by TrungDao on 8/7/16.
 */

public class NotificationHelper {


    public TaxiSocket taxiSocket;
    public ChatSocket chatSocket;

    public NotificationHelper(){

        taxiSocket = new TaxiSocket();
        chatSocket = new ChatSocket();
    }

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";
    public static final String NOTIFICATION_DATA = "NOTIFICATION_DATA";
    public static final String NOTIFICATION_TYPE = "NOTIFICATION_TYPE";
    public static final String NOTIFICATION_ACTION = "NOTIFICATION_ACTION";


    //----------------------------------------------- NOTIFICATION CREATION ----------------------------------------------

    public static PendingIntent getIntent(int notifyId, HashMap<String,String> data, String notifyAction, Context context) {

        String notifyType = data.get("Type");

        Intent intent = new Intent(context, AppRootActivity.class);
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra(NOTIFICATION_ID, notifyId);
        intent.putExtra(NOTIFICATION_DATA, new HashMap<String,String>( data));
        intent.putExtra(NOTIFICATION_TYPE, (CharSequence) notifyType);
        intent.putExtra(NOTIFICATION_ACTION, (CharSequence) notifyAction);
        intent.setAction(String.valueOf(notifyAction));

        PendingIntent dismissIntent = PendingIntent.getActivity(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        return dismissIntent;

    }

    public static PendingIntent getOPENIntent(int notifyId, HashMap<String,String> data, Context context) {

        return getIntent(notifyId,data,NotificationActionType.OPEN,context);

    }

    public static void ShowNotification(RemoteMessage message, Context context) {


        Map<String,String> msgData = message.getData();
        HashMap<String, String> data = (msgData instanceof HashMap) ? (HashMap) msgData : new HashMap<String, String>(msgData);
        String notifyType = data.get("Type");

        String orderId = data.get("OrderID");
        if(orderId != null && orderId.isEmpty() == false) {

            final int notifyId = orderId.hashCode();

            PendingIntent piOpen = NotificationHelper.getOPENIntent(notifyId,data, context);

            if(notifyType!= null && notifyType.equals(NotificationType.UserChatToDriver)) {

                ShowChatNotificationByOrder(message, context, notifyId, piOpen);

            }else {

                ShowDefaultNotificationByOrder(message, context, notifyId, piOpen);

            }

        }else{

            final int notifyId = new Random().nextInt();
        }
        
    }

    private static void ShowDefaultNotificationByOrder(RemoteMessage message, Context context, int notifyId, PendingIntent piOpen) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notifyId);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.centerlogo)
                        .setDefaults( Notification.DEFAULT_LIGHTS)
                        .setPriority(NotificationCompat.PRIORITY_MAX) //must give priority to High, Max which will considered as heads-up notification
                        .setVisibility(VISIBILITY_PUBLIC)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000})
                        .setContentIntent(piOpen)
                        .setAutoCancel(true)
                        .setShowWhen(true)
                        .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notifysound))
                        .setOnlyAlertOnce(true);


        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notify_default);
        remoteViews.setImageViewResource(R.id.imgLogo,R.drawable.centerlogo);
        remoteViews.setImageViewResource(R.id.line1,R.drawable.line);
        remoteViews.setImageViewResource(R.id.line2,R.drawable.line);
        remoteViews.setTextColor(R.id.txtTitle, Color.DKGRAY);
        remoteViews.setTextColor(R.id.txtDesc, Color.DKGRAY);
        remoteViews.setTextColor(R.id.txtSentTime, Color.DKGRAY);
        remoteViews.setTextColor(R.id.txtBelowDesc, Color.DKGRAY);

        Notification notification = builder.build();
        notification.bigContentView = remoteViews;
        notification.contentView = remoteViews;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification.headsUpContentView = remoteViews;
        }

        Map<String,String> msgData = message.getData();
        HashMap<String, String> data = (msgData instanceof HashMap) ? (HashMap) msgData : new HashMap<String, String>(msgData);
        String orderId = data.get("OrderID");

        String strSentTime = new SimpleDateFormat("h:mm a").format(new Date(message.getSentTime()));
        remoteViews.setTextViewText(R.id.txtSentTime,strSentTime);

        String notifyType = data.get("Type");
        if(notifyType!= null) {


            remoteViews.setTextViewText(R.id.txtDesc,data.get("PickupPlace"));
            remoteViews.setTextViewText(R.id.txtBelowDesc,"ẤN ĐỂ XEM CHI TIẾT");

            String strAmount = data.get("Amount");
            if(strAmount != null) {

                Double amount = Double.valueOf(data.get("Amount"));
                String strAmountInVND = RegionalHelper.toCurrency(amount, "VND");
                remoteViews.setTextViewText(R.id.txtPrice, strAmountInVND);

            }else{

                remoteViews.setTextViewText(R.id.txtPrice, "");
            }

            if (notifyType.equals(NotificationType.UserRequestTaxi)) {

                remoteViews.setTextViewText(R.id.txtTitle,"KHÁCH ĐỀ NGHỊ ĐÓN");
                notificationManager.notify(notifyId,notification );
                AutoRemoveNotification(notifyId,1,context);


            }else if (notifyType.equals(NotificationType.UserCancelRequest)) {
                notificationManager.cancel(notifyId);

            }else if (notifyType.equals(NotificationType.UserAcceptBidding)) {

                remoteViews.setTextViewText(R.id.txtTitle,"KHÁCH ĐỒNG Ý ĐỀ NGHỊ");
                notificationManager.notify(notifyId,notification );
                AutoRemoveNotification(notifyId,60*3,context);

            }else if (notifyType.equals(NotificationType.UserCancelAcceptingBidding)) {

                remoteViews.setTextViewText(R.id.txtTitle,"KHÁCH HỦY ĐỀ NGHỊ");
                notificationManager.notify(notifyId,notification );
                AutoRemoveNotification(notifyId,60*3,context);

            }else if (notifyType.equals(NotificationType.UserVoidedBfPickup)) {

                remoteViews.setTextViewText(R.id.txtTitle,"KHÁCH HỦY ĐÓN");
                notificationManager.notify(notifyId,notification );
                AutoRemoveNotification(notifyId,60*3,context);

            }else if (notifyType.equals(NotificationType.UserVoidedAfPickup)) {

                remoteViews.setTextViewText(R.id.txtTitle,"KHÁCH HỦY CHUYẾN TRONG CƯỚC");
                notificationManager.notify(notifyId,notification );
                AutoRemoveNotification(notifyId,60*3,context);

            }else if (notifyType.equals(NotificationType.UserPaidByCard)) {

                remoteViews.setTextViewText(R.id.txtTitle,"KHÁCH ĐÃ THANH TOÁN BẰNG THẺ");
                notificationManager.notify(notifyId,notification );
                AutoRemoveNotification(notifyId,60*3,context);

            }

        }


    }

    private static void ShowChatNotificationByOrder(RemoteMessage message, Context context, int notifyId, PendingIntent piOpen) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notifyId);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.centerlogo)
                        .setDefaults( Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND)
                        .setPriority(NotificationCompat.PRIORITY_MAX) //must give priority to High, Max which will considered as heads-up notification
                        .setVisibility(VISIBILITY_PUBLIC)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000})
                        .setContentIntent(piOpen)
                        .setAutoCancel(true)
                        .setShowWhen(true)
                        .setOnlyAlertOnce(true);


        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notify_default);
        remoteViews.setImageViewResource(R.id.imgLogo,R.drawable.centerlogo);
        remoteViews.setImageViewResource(R.id.line1,R.drawable.line);
        remoteViews.setImageViewResource(R.id.line2,R.drawable.line);
        remoteViews.setTextColor(R.id.txtTitle, Color.DKGRAY);
        remoteViews.setTextColor(R.id.txtDesc, Color.DKGRAY);
        remoteViews.setTextColor(R.id.txtSentTime, Color.DKGRAY);
        remoteViews.setTextColor(R.id.txtBelowDesc, Color.DKGRAY);

        Notification notification = builder.build();
        notification.bigContentView = remoteViews;
        notification.contentView = remoteViews;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification.headsUpContentView = remoteViews;
        }

        Map<String,String> msgData = message.getData();
        HashMap<String, String> data = (msgData instanceof HashMap) ? (HashMap) msgData : new HashMap<String, String>(msgData);
        String orderId = data.get("OrderID");

        String strSentTime = new SimpleDateFormat("h:mm a").format(new Date(message.getSentTime()));
        remoteViews.setTextViewText(R.id.txtSentTime,strSentTime);

        remoteViews.setTextViewText(R.id.txtTitle,data.get("UserName"));
        remoteViews.setTextViewText(R.id.txtDesc,data.get("Message"));
        remoteViews.setTextViewText(R.id.txtBelowDesc,data.get("PickupPlace"));
        remoteViews.setViewVisibility(R.id.txtBelowDesc,GONE);
        remoteViews.setViewVisibility(R.id.txtPrice,GONE);

        notificationManager.notify(notifyId,notification );
        AutoRemoveNotification(notifyId,30,context);




    }


    private static void AutoRemoveNotification(final int notificationId, int minutes,final Context context) {

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(notificationId);

            }
        },1000*60*minutes);
    }

    //----------------------------------------------- OPEN ACTIVITIES ----------------------------------------------

    public static Boolean isNotification(Intent rootIntent){

        int notifyId = rootIntent.getIntExtra(NOTIFICATION_ID, -1);

        return notifyId != -1;

    }

    public static void TryToForwardFromNotification(final Intent rootIntent,final Activity rootContext, final GetBoolValueListener listener){

        if(isNotification(rootIntent) == false) {

            if(listener!=null)
                listener.onCompleted(true,false);

            return;
        }

        NotificationManager manager = (NotificationManager) AppDelegate.getContext().getSystemService(NOTIFICATION_SERVICE);
        int notifyId = rootIntent.getIntExtra(NOTIFICATION_ID, -1);
        manager.cancel(notifyId);

        String notifyType = rootIntent.getStringExtra(NOTIFICATION_TYPE);

        if(notifyType != null ){

            OpenOrderFromNotification(rootIntent, rootContext, new GetBoolValueListener() {
                @Override
                public void onCompleted(Boolean success, Boolean value) {

                    if(listener!=null)
                        listener.onCompleted(success,value);
                }
            });

        }else{

            if(listener!=null)
                listener.onCompleted(true,false);
        }


    }

    static void OpenOrderFromNotification(final Intent rootIntent, final Activity rootContext, final GetBoolValueListener listener){

        final HashMap<String, String> data = (HashMap<String, String>)rootIntent.getSerializableExtra(NOTIFICATION_DATA);

        String orderId = data.get("OrderID") ;


        new TravelOrderController().getById(true, orderId, new GetOneListener() {
            @Override
            public void onGetOne(Boolean success, BaseModel item) {

                if(item != null){

                    Intent intent = new Intent(rootContext, OrderScreen.class);
                    Parcelable wrappedCurrentOrder = Parcels.wrap(item);
                    intent.putExtra("CurrentOrder",wrappedCurrentOrder);

                    String notifyType = rootIntent.getStringExtra(NOTIFICATION_TYPE);
                    String notifyAction = rootIntent.getStringExtra(NOTIFICATION_ACTION);
                    intent.putExtra(NOTIFICATION_TYPE, (CharSequence) notifyType);
                    intent.putExtra(NOTIFICATION_ACTION, (CharSequence) notifyAction);
                    intent.putExtra(NOTIFICATION_DATA, data);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    rootContext.startActivity(intent);
                    rootContext.overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);
                }

                if(listener!=null)
                    listener.onCompleted(success,item != null);
            }
        });

    }
}
