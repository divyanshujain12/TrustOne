package com.example.deii.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deii.trustone.R;

import java.util.ArrayList;

/**
 * Created by deii on 10/25/2015.
 */
public class SubCatCustomAdapter extends ArrayAdapter<String> {
    /** Global declaration of variables. As there scope lies in whole class. */
    private Context context;
    private ArrayList<String> listOfValues;
    int selectedPosition = 0;
    /** Constructor Class */
    public SubCatCustomAdapter (Context c,ArrayList<String> values) {
        super(c, R.layout.custom_subcat_list,values);
        this.context = c;
        this.listOfValues = values;

    }

    /** Implement getView method for customizing row of list view. */
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        // Creating a view of row.
        View rowView = inflater.inflate(R.layout.custom_subcat_list, parent, false);
        RadioButton radioButton = (RadioButton) rowView.findViewById(R.id.subCatRadio);
        radioButton.setChecked(position == selectedPosition);
        radioButton.setTag(position);
        radioButton.setTypeface(Typeface.createFromAsset(((Activity)context).getAssets(),"fonts/Roboto-Medium.ttf"));
        radioButton.setText(listOfValues.get(position));
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = (Integer)v.getTag();
                notifyDataSetChanged();
            }
        });

        return rowView;
    }
}