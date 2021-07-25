package com.example.s_tools.entertainment.Fragemnts.Imagess;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.s_tools.R;
import com.example.s_tools.entertainment.Fragemnts.Imagess.ActivityImage.ImagesActivity;


public class FragmentI_Images extends Fragment {
    View view;
    ImageView imageView;
    TextView textView;


    public FragmentI_Images() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_i__images, container, false);
        imageView = view.findViewById(R.id.ImageviewFragmentImage);
        textView = view.findViewById(R.id.imagesText);



        imageView.setOnClickListener(v -> startActivity(new Intent(getActivity(), ImagesActivity.class)));
        return view;
    }
}