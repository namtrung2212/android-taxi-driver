package com.sconnecting.driverapp.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.sconnecting.driverapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by TrungDao on 8/8/16.
 */

public class ImageHelper {


    public  static  void loadImage(Context context, int resourceId, int newWidth , int newHeight , ImageView target, final Callback callback){

        Picasso.with(context).load(resourceId).resize(newWidth,newHeight).centerCrop().into(target,callback);
    }

    public  static  void loadImage(Context context, String url, int imgPlaceHolder, int newWidth , int newHeight , ImageView target, final Callback callback){

        Picasso.with(context).load(url)

                .error(imgPlaceHolder).placeholder(imgPlaceHolder).resize(newWidth,newHeight).centerCrop().into(target,callback);
    }

    public  static  void loadCircleImage(Context context, String url, int imgPlaceHolder, int newWidth , int newHeight , ImageView target, final Callback callback){

        Picasso.with(context).load(url).error(imgPlaceHolder).placeholder(imgPlaceHolder).resize(newWidth,newHeight).centerCrop()
                .transform(new CircleTransform()).into(target,callback);
    }


    public static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size/2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }



}
