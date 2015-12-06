package com.example.deii.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.deii.Models.TopicModel;
import com.example.deii.trustone.R;
import com.rey.material.widget.RadioButton;

import java.util.ArrayList;

/**
 * Created by deii on 12/5/2015.
 */
public class TopicFragmentAdapter extends RecyclerView.Adapter<TopicFragmentAdapter.TopicsHolder> {

    private ArrayList<TopicModel> model;
    CompoundButton checkedView;
   public int selectedPosition = 0;
    private Context context;

    public TopicFragmentAdapter(Context context, ArrayList<TopicModel> model) {
        this.model = model;
        this.context = context;
    }

    @Override
    public TopicsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_subcat_list, parent, false);
        TopicsHolder topicsHolder = new TopicsHolder(v);
        return topicsHolder;
    }

    @Override
    public void onBindViewHolder(TopicsHolder holder, int position) {

        holder.topicRadio.setText(model.get(position).getName());
        holder.topicRadio.setChecked(position == selectedPosition);
        holder.topicRadio.setTag(position);
        holder.topicRadio.setTypeface(Typeface.createFromAsset(((Activity) context).getAssets(), "fonts/Roboto-Medium.ttf"));
        holder.topicRadio.setText(model.get(position).getName());

        if (position == 0) {
            holder.topicRadio.setChecked(true);
            checkedView = holder.topicRadio;
        }

        holder.topicRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    // if (checkedView != null)
                    checkedView.setChecked(false);
                    compoundButton.setChecked(true);
                    checkedView = compoundButton;
                    selectedPosition = (Integer) compoundButton.getTag();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public static class TopicsHolder extends RecyclerView.ViewHolder {
        // CardView cv;
        RadioButton topicRadio;


        TopicsHolder(View itemView) {
            super(itemView);
            //  cv = (CardView) itemView.findViewById(R.id.cv);
            topicRadio = (RadioButton) itemView.findViewById(R.id.subCatRadio);

        }
    }
}
