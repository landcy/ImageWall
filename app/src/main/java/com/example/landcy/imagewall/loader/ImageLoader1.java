package com.example.landcy.imagewall.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by landcy on 9/6/2017.
 */

public class ImageLoader1 {
//    private final static int CPU_COUNT = 1;
//    private final static int TAG_KEY_URI = 0x101;
//    private final static int DISK_CACHE_SIZE = 1024 * 1024 * 50;
//    private LruCache<String, Bitmap> mMemoryCache;
//    private DiskLruCache mDiskLruCache;
//    private ImageResizer mImageResizer;
//
//    private Context mContext;
//
//    private boolean mIsDiskLruCacheCreated = false;
//
//    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
//    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
//    private static final long KEEP_ALIVE = 10L;
//    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
//        private final AtomicInteger mCount = new AtomicInteger(1);
//
//        public Thread newThread(Runnable r) {
//            return new Thread(r, " ImageLoader1#" + mCount.getAndIncrement());
//        }
//    };
//    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), sThreadFactory);
//
//
//    public ImageLoader1(Context context) {
//        mContext = context;
//        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//        final int cacheSize = maxMemory / 8;
//        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
//            @Override
//            protected int sizeOf(String key, Bitmap value) {
//                return value.getRowBytes() * value.getHeight() / 1024;
//            }
//        };
//
//        File diskCacheDir = getCacheDiskDir(mContext, "Bitmap");
//        if (!diskCacheDir.exists()) {
//            diskCacheDir.mkdirs();
//        }
//
//        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
//            try {
//                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
//                mIsDiskLruCacheCreated = true;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private File getCacheDiskDir(Context context, String name) {
//        return null;
//    }
//
//    private int getUsableSpace(File dir) {
//        return 1;
//    }
//
//    public Bitmap loadBitmap(String url, int reqWidth, int reqHeight) {
//        Bitmap bitmap = loadBitmapFromMemCache(url, reqWidth, reqHeight);
//        if (bitmap != null) {
//            return bitmap;
//        }
//        try {
//            bitmap = loadBitmapFromDiskCache(url, reqWidth, reqHeight);
//            if (bitmap != null) {
//                return bitmap;
//            }
//            bitmap = loadBitmapFromHttp(url, reqWidth, reqHeight);
//        } catch (IOException e) {
//        }
//        if (bitmap == null && !mIsDiskLruCacheCreated) {
//            bitmap = downloadBitmapFromUrl(url);
//        }
//        return bitmap;
//    }
//
//    public void bindBitmap(final String uri, final ImageView imageView, final int reqWidth, final int reqHeight) {
//        imageView.setTag(TAG_KEY_URI, uri);
//        Bitmap bitmap = loadBitmapFromMemCache(uri, reqWidth, reqHeight);
//        if (bitmap != null) {
//            imageView.setImageBitmap(bitmap);
//            return;
//        }
//        Runnable loadBitmapTask = new Runnable() {
//            @Override
//            public void run() {
//                Bitmap bitmap = loadBitmap(uri, reqWidth, reqHeight);
//                if (bitmap != null) {
//                    LoaderResult result = new LoaderResult(imageView, uri, bitmap);
//                    mMainHandler.obtainMessage(MESSAGE POST_RESULT, result).sendToTarget();
//                }
//            }
//        };
//        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
//    }
//
//    public Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight) throws IOException {
//        if (Looper.myLooper() == Looper.getMainLooper()) {
//            throw new RuntimeException(" can not visit network from UI Thread.");
//        }
//        if (mDiskLruCache == null) {
//            return null;
//        }
//        String key = hashKeyFormUrl(url);
//
//        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
//        if (editor != null) {
//            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_SIZE);
//            if (downloadUrlToStream(url, outputStream)) {
//                editor.commit();
//            } else {
//                editor.abort();
//            }
//            mDiskLruCache.flush();
//        }
//
//        return loadBitmapFromDiskCache(url, reqWidth, reqHeight);
//    }
//
//
//    private Bitmap loadBitmapFromDiskCache(String url, int reqWidth, int reqHeight) throws IOException {
//        if (Looper.myLooper() == Looper.getMainLooper()) {
//            throw new RuntimeException(" can not visit network from UI Thread.");
//        }
//        if (mDiskLruCache == null) {
//            return null;
//        }
//        Bitmap bitmap = null;
//        String key = hashKeyFormUrl(url);
//        DiskLruCache.Snapshot snapShot = null;
//        snapShot = mDiskLruCache.get(key);
//        if (snapShot != null) {
//            FileInputStream fileInputStream = (FileInputStream) snapShot.getInputStream(DISK_CACHE_SIZE);
//            FileDescriptor fileDescriptor = null;
//
//            fileDescriptor = fileInputStream.getFD();
//            bitmap = mImageResizer.decodeSampledBitmapFromFileDescriptor(fileDescriptor, reqWidth, reqHeight);
//            if (bitmap != null) {
//                addBitmapToMemoryCache(key, bitmap);
//
//            }
//        }
//        return bitmap;
//
//    }
//
//    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
//        if (getBitmapFromMemCache(key) == null) {
//            mMemoryCache.put(key, bitmap);
//        }
//    }
//
//
//    private Bitmap getBitmapFromMemCache(String key) {
//        return mMemoryCache.get(key);
//    }
//
//    public Bitmap loadBitmapFromMemCache(String url, int reqWidth, int reqHeight) {
//        return null;
//    }
//
//    private String hashKeyFormUrl(String url) {
//        String cacheKey;
//        try {
//            final MessageDigest mDigest = MessageDigest.getInstance(" MD5");
//            mDigest.update(url.getBytes());
//            cacheKey = bytesToHexString(mDigest.digest());
//        } catch (NoSuchAlgorithmException e) {
//            cacheKey = String.valueOf(url.hashCode());
//        }
//        return cacheKey;
//    }
//
//    private String bytesToHexString(byte[] bytes) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < bytes.length; i++) {
//            String hex = Integer.toHexString(0xFF & bytes[i]);
//            if (hex.length() == 1) {
//                sb.append('0');
//            }
//            sb.append(hex);
//        }
//        return sb.toString();
//    }
//
//    private boolean downloadUrlToStream(String url, OutputStream outputStream) {
//        URL httpUrl = null;
//        HttpURLConnection httpURLConnection = null;
//        BufferedInputStream read = null;
//        BufferedOutputStream output = null;
//        try {
//            httpUrl = new URL(url);
//            httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
//            read = new BufferedInputStream(httpURLConnection.getInputStream());
//            byte[] b = new byte[1024];
//            output = new BufferedOutputStream(outputStream);
//            while (read.read(b) != -1) {
//                output.write(b);
//            }
//            return true;
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (read != null) {
//                    read.close();
//                }
//                if (output != null) {
//                    output.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (httpURLConnection != null) {
//                httpURLConnection.disconnect();
//            }
//        }
//        return false;
//    }
//
//    private Bitmap downloadBitmapFromUrl(String url) {
//        URL httpUrl = null;
//        HttpURLConnection httpURLConnection = null;
//        BufferedInputStream read = null;
//        BufferedOutputStream output = null;
//        try {
//            httpUrl = new URL(url);
//            httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
//            read = new BufferedInputStream(httpURLConnection.getInputStream());
//            byte[] b = new byte[1024];
//            output = new BufferedOutputStream(outputStream);
//            while (read.read(b) != -1) {
//                output.write(b);
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (read != null) {
//                    read.close();
//                }
//                if (output != null) {
//                    output.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (httpURLConnection != null) {
//                httpURLConnection.disconnect();
//            }
//        }
//    }

}
