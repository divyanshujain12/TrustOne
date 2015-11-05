package com.example.deii.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.deii.Models.ProductsModel;
import com.example.deii.Utils.Constants;
import com.example.deii.Utils.ImageLoader;
import com.example.deii.trustone.NavigationDrawerActivity;
import com.example.deii.trustone.R;
import com.example.deii.trustone.YouTubePlayerActivity;

import java.util.ArrayList;

/**
 * Created by Lenovo on 05-11-2015.
 */
public class CustomPagerAdapter extends PagerAdapter implements View.OnClickListener {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private ArrayList<ProductsModel> productsModels;
    private ImageLoader loader;

    public CustomPagerAdapter(Context context, ArrayList<ProductsModel> productsModels) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.productsModels = productsModels;
        loader = new ImageLoader(mContext);
    }

    @Override
    public int getCount() {
        return productsModels.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        //imageView.setImageResource(productsModels.get(position).getThumbnailUrl());
        loader.DisplayImage(productsModels.get(position).getThumbnailUrl(), imageView);

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
        if (NavigationDrawerActivity.productsModel.get(pos).getType().contentEquals("1")) {

            String video_id = NavigationDrawerActivity.productsModel.get(pos).getUrl();
            video_id = video_id.substring(video_id.indexOf("=")+1, video_id.length() - 1);
            Intent intent = new Intent(mContext, YouTubePlayerActivity.class);
            intent.putExtra(Constants.DATA, video_id);
            mContext.startActivity(intent);

        }

    }
}
