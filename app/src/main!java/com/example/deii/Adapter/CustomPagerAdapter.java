package com.example.deii.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.example.deii.Models.BannerModel;
import com.example.deii.Utils.ImageLoader;
import com.example.deii.trustone.MyApplication;
import com.example.deii.trustone.NavigationDrawerActivity;
import com.example.deii.trustone.R;

import java.util.ArrayList;

/**
 * Created by Lenovo on 05-11-2015.
 */
public class CustomPagerAdapter extends PagerAdapter implements View.OnClickListener {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private ArrayList<BannerModel> bannerModels;
    private ImageLoader loader;
    ImageView imageView;

    public CustomPagerAdapter(Context context, ArrayList<BannerModel> bannerModels) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.bannerModels = bannerModels;
        loader = new ImageLoader(mContext);
    }

    @Override
    public int getCount() {
        return bannerModels.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        //TextView title = (TextView) itemView.findViewById(R.id.title);
        //imageView.setImageResource(productsModels.get(position).getThumbnailUrl());
       // loader.DisplayImage(bannerModels.get(position).getImage(), imageView);

        MyApplication.getInstance(mContext).getImageLoader().get(bannerModels.get(position).getImage(), new com.android.volley.toolbox.ImageLoader.ImageListener() {
            @Override
            public void onResponse(com.android.volley.toolbox.ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                if (bitmap != null)
                    imageView.setImageBitmap(bitmap);
                else
                    imageView.setImageResource(R.drawable.img_empty);
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // title.setText(bannerModels.get(position).getTitle());
        container.addView(itemView);

        imageView.setId(position);
        imageView.setOnClickListener(this);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public void onClick(View view) {
        int pos = view.getId();
        String url = NavigationDrawerActivity.bannerList.get(pos).getUrl();

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        mContext.startActivity(i);

        /*if (NavigationDrawerActivity.bannerList.get(pos).getType().contentEquals("1")) {

            video_id = video_id.substring(video_id.indexOf("=")+1, video_id.length());
            Intent intent = new Intent(mContext, YouTubePlayerActivity.class);
            intent.putExtra(Constants.DATA, video_id);
            mContext.startActivity(intent);

        }
        else{
            Intent intent = new Intent(mContext, AndroidBuildingMusicPlayerActivity.class);
            intent.putExtra(Constants.DATA, video_id);
            mContext.startActivity(intent);

        }*/

    }
}

