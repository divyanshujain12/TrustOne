package com.example.deii.ProductFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deii.trustone.R;

/**
 * Created by deii on 12/6/2015.
 */
public class AudioFragment extends Fragment{

    public static AudioFragment newInstance(int pos, String name) {
        AudioFragment myFragment = new AudioFragment();

        Bundle args = new Bundle();
        args.putInt("topicID", pos);
        myFragment.setArguments(args);

        return myFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.product_fragment, container, false);

    }
}
