package com.example.deii.Adapter;

import android.content.Context;
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

/**
 * Created by deii on 12/8/2015.
 */
public class PptFragmentAdapter extends RecyclerView.Adapter<PptFragmentAdapter.TopicsHolder> {
    private ImageLoader loader;

    private ArrayList<ProductsModel> model;
    private Context context;


    public PptFragmentAdapter(Context context, ArrayList<ProductsModel> model) {
        this.model = model;
        this.context = context;
        loader = new ImageLoader(context);
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
    public PptFragmentAdapter.TopicsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_fragment_adapter, parent, false);
        TopicsHolder topicsHolder = new TopicsHolder(v);
        return topicsHolder;
    }

    @Override
    public void onBindViewHolder(PptFragmentAdapter.TopicsHolder holder, int position) {

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