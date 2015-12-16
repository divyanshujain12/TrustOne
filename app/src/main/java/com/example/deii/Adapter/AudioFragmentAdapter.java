package com.example.deii.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.deii.Models.ProductsModel;
import com.example.deii.Utils.ImageLoader;
import com.example.deii.trustone.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;

/**
 * Created by deii on 12/7/2015.
 */
public class AudioFragmentAdapter extends RecyclerView.Adapter<AudioFragmentAdapter.TopicsHolder> {
    private ImageLoader loader;

    private ArrayList<ProductsModel> model;
    private Context context;

    public AudioFragmentAdapter(Context context, ArrayList<ProductsModel> model) {
        this.model = model;
        this.context = context;
        loader = new ImageLoader(context);
    }

    public static class TopicsHolder extends RecyclerView.ViewHolder {
        // CardView cv;
        ImageView thumbnail;
        TextView txtTitle, txtDescription, textView;
        ProgressBar progressBar2;
        LinearLayout footerView;

        TopicsHolder(View itemView) {
            super(itemView);

            //  cv = (CardView) itemView.findViewById(R.id.cv);
            thumbnail = (ImageView) itemView.findViewById(R.id.imageView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);

            progressBar2 = (ProgressBar) itemView.findViewById(R.id.progressBar2);
            textView = (TextView) itemView.findViewById(R.id.textView);
            footerView = (LinearLayout) itemView.findViewById(R.id.footerView);
        }
    }

    @Override
    public AudioFragmentAdapter.TopicsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        TopicsHolder topicsHolder = null;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_video_fragment, parent, false);
        topicsHolder = new TopicsHolder(v);


        return topicsHolder;
    }

    @Override
    public void onBindViewHolder(AudioFragmentAdapter.TopicsHolder holder, int position) {

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


}
