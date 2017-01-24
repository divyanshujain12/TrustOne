package com.example.deii.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.deii.Models.ProductsModel;
import com.example.deii.Utils.ImageLoader;
import com.example.deii.trustone.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by deii on 12/7/2015.
 */


public class VideosFragmentAdapter extends RecyclerView.Adapter<VideosFragmentAdapter.TopicsHolder> {
    private ImageLoader loader;

    private ArrayList<ProductsModel> model;
    private Context context;
    private static MediaMetadataRetriever mediaMetadataRetriever = null;


    public VideosFragmentAdapter(Context context, ArrayList<ProductsModel> model) {
        this.model = model;
        this.context = context;
        loader = new ImageLoader(context);
        mediaMetadataRetriever = new MediaMetadataRetriever();
    }

    public static class TopicsHolder extends RecyclerView.ViewHolder {
        // CardView cv;
        ImageView thumbnail;
        TextView txtTitle, txtDescription;

        TopicsHolder(View itemView) {
            super(itemView);
            //  cv = (CardView) itemView.findViewById(R.id.cv);
            thumbnail = (ImageView) itemView.findViewById(R.id.imageView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);

        }
    }

    @Override
    public VideosFragmentAdapter.TopicsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_video_fragment, parent, false);
        TopicsHolder topicsHolder = new TopicsHolder(v);
        return topicsHolder;
    }

    @Override
    public void onBindViewHolder(VideosFragmentAdapter.TopicsHolder holder, int position) {

        /*try {
            holder.thumbnail.setImageBitmap(retriveVideoFrameFromVideo(model.get(position).getUrl()));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }*/
        loader.DisplayImage(model.get(position).getThumbnailUrl(), holder.thumbnail);
        holder.txtTitle.setText(model.get(position).getName());
        holder.txtDescription.setText(model.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public void addAllData(ArrayList<ProductsModel> model) {
        this.model.addAll(model);
        notifyDataSetChanged();
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;

        try {

            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
