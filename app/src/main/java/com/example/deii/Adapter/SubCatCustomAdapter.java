package com.example.deii.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.example.deii.Fragments.SubCategoryFragment;
import com.example.deii.Models.SubCategoryModel;
import com.example.deii.trustone.R;
import com.neopixl.pixlui.components.textview.TextView;
import com.rey.material.widget.RadioButton;

import java.util.ArrayList;

/**
 * Created by deii on 10/25/2015.
 */
public class SubCatCustomAdapter extends ArrayAdapter<SubCategoryModel> {

    /**
     * Global declaration of variables. As there scope lies in whole class.
     */
    private Context context;
    private ArrayList<SubCategoryModel> listOfValues;
    CompoundButton checkedView;
    public int selectedPosition = 0;
    Animation fade_in;

    /**
     * Constructor Class
     */
    public SubCatCustomAdapter(Context c, ArrayList<SubCategoryModel> values) {
        super(c, R.layout.custom_subcat_list, values);
        this.context = c;
        this.listOfValues = values;
        fade_in = AnimationUtils.loadAnimation(context, R.anim.fade_in_animation);

    }

    /**
     * Implement getView method for customizing row of list view.
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        // Creating a view of row.
        View rowView = inflater.inflate(R.layout.custom_subcat_list, parent, false);
        TextView radioButton = (TextView) rowView.findViewById(R.id.subCatRadio);
        //radioButton.setChecked(position == selectedPosition);
        radioButton.setTag(position);
        radioButton.setTypeface(Typeface.createFromAsset(((Activity) context).getAssets(), "fonts/Roboto-Medium.ttf"));
        radioButton.setText(listOfValues.get(position).getName());

       /* if (position == 0) {
            radioButton.setChecked(true);
            checkedView = radioButton;
            setValueInAboutTextView(listOfValues.get(position).getContent());

        }

        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    // if (checkedView != null)

                    selectedPosition = (Integer) compoundButton.getTag();
                    setValueInAboutTextView(listOfValues.get(selectedPosition).getContent());
                    checkedView.setChecked(false);
                    compoundButton.setChecked(true);
                    checkedView = compoundButton;
                }
            }
        });*/

        return rowView;
    }

    /*private void setValueInAboutTextView(String text) {
        SubCategoryFragment.txtAbout.setText(text);
        SubCategoryFragment.txtAbout.startAnimation(fade_in);
    }*/
}
