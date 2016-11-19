package com.example.deii.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.deii.Models.ProductsModel;
import com.example.deii.Utils.ImageLoader;
import com.example.deii.trustone.R;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by deii on 12/7/2015.
 */
public class AudioFragmentAdapter extends RecyclerView.Adapter<AudioFragmentAdapter.TopicsHolder> {
    private ImageLoader loader;

    private ArrayList<ProductsModel> model;
    private Context context;
    Integer[] colorArray = new Integer[4];
    int previousColor = -1;

    public AudioFragmentAdapter(Context context, ArrayList<ProductsModel> model) {
        this.model = model;
        this.context = context;
        loader = new ImageLoader(context);
        colorArray[0] = context.getResources().getColor(R.color.first);
        colorArray[1] = context.getResources().getColor(R.color.second);
        colorArray[2] = context.getResources().getColor(R.color.third);
        colorArray[3] = context.getResources().getColor(R.color.fourth);
    }

    public class TopicsHolder extends RecyclerView.ViewHolder {
        // CardView cv;
        ImageView thumbnail;
        TextView txtTitle, txtDescription, textView;
        ProgressBar progressBar2;
        LinearLayout footerView;
        RelativeLayout audioRL;


        TopicsHolder(View itemView) {
            super(itemView);

            //  cv = (CardView) itemView.findViewById(R.id.cv);
            audioRL = (RelativeLayout) itemView.findViewById(R.id.audioRL);
            audioRL.setBackgroundColor(colorArray[randInt(0, 3)]);
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

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_audio_fragment, parent, false);

        topicsHolder = new TopicsHolder(v);


        return topicsHolder;
    }

    @Override
    public void onBindViewHolder(AudioFragmentAdapter.TopicsHolder holder, int position) {

        String name = model.get(position).getUrl();

        //loader.DisplayImage(model.get(position).getThumbnailUrl(), holder.thumbnail);
        String[] data = name.split("/");
        name = data[data.length - 1];
        String[] namePos = name.split("\\.");
        name = namePos[0];
        name = name.replace("_", " ");
        holder.txtDescription.setText(model.get(position).getDescription());
        holder.txtTitle.setText(name);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public void addAllData(ArrayList<ProductsModel> model) {
        this.model.addAll(model);
        notifyDataSetChanged();
    }

    public int randInt(int min, int max) {
        // Usually this can be a field rather than a method variable
        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;
        if (previousColor == randomNum)
            randInt(0, 3);
        previousColor = randomNum;
        return randomNum;
    }
}
