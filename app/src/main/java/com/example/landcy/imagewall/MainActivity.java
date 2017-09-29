package com.example.landcy.imagewall;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.landcy.imagewall.loader.ImageLoader;
import com.example.landcy.imagewall.utils.ImageUrls;
import com.example.landcy.imagewall.utils.MyUtils;

import java.util.HashMap;

public class MainActivity extends Activity {
    private ListView mListView;
    private boolean mIsGridViewIdle = true;
    private ListWallAdapter mImageAdapter;
    private ImageLoader mImageLoader;

    private int mImageWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listview);
        mImageAdapter = new ListWallAdapter(this);
        mListView.setAdapter(mImageAdapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    mIsGridViewIdle = true;
                    mImageAdapter.notifyDataSetChanged();
                } else {
                    mIsGridViewIdle = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // ignored
            }
        });


        int screenWidth = MyUtils.getScreenMetrics(this).widthPixels;
        int space = (int) MyUtils.dp2px(this, 20f);
        mImageWidth = (screenWidth - space) / 3;
        mImageLoader = ImageLoader.build(this);


        HashMap<String,Integer> map = new HashMap<>();




    }

    public class ListWallAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private Drawable mDefaultBitmapDrawable;
        private String[] mUrList;

        private ListWallAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            mUrList = ImageUrls.imageThumbUrls;
            mDefaultBitmapDrawable = context.getResources().getDrawable(R.mipmap.ic_launcher);
        }


        @Override

        public int getCount() {
            return mUrList.length;

        }


        @Override

        public String getItem(int position) {
            return mUrList[position];
        }


        @Override

        public long getItemId(int position) {
            return position;
        }


        @Override

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.image_list_item, parent, false);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageView imageView = holder.imageView;
            final String tag = (String) imageView.getTag();
            final String uri = getItem(position);
            if (!uri.equals(tag)) {
                imageView.setImageDrawable(mDefaultBitmapDrawable);
            }
            if (mIsGridViewIdle) {
                imageView.setTag(uri);
                mImageLoader.bindBitmap(uri, imageView, mImageWidth, mImageWidth);
            }
            return convertView;
        }
    }

    private class ViewHolder {
        public ImageView imageView;
    }

}
