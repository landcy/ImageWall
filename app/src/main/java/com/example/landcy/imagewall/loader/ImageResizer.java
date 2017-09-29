package com.example.landcy.imagewall.loader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

/**
 * Created by landcy on 9/6/2017.
 */

public final class ImageResizer {
    public ImageResizer(){

    }
    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fileDescriptor , int requiredWidth, int requiredHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = caculateSample(options,requiredWidth,requiredHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

    }

    public Bitmap decodeSampledBitmapFromResource(Resources resource, int resId, int requiredWidth, int requiredHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = caculateSample(options,requiredWidth,requiredHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resource, resId, options);
    }

    private int caculateSample(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int w = options.outWidth;
        final int h = options.outHeight;
        int inSampleSize = 1;
        if (w > reqWidth || h > reqHeight) {
            final int halfW = w / 2;
            final int halfH = h / 2;
            while (halfH / inSampleSize >= reqHeight && halfW / inSampleSize >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;

    }

}
