package com.example.s_tools.Services_item.mytoken;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.s_tools.R;

public class TokenGenerate extends Fragment {
    View view;
    Button showBtn;

    public TokenGenerate() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_token_generate, container, false);
        init();
        return view;
    }

    private void init() {
        showBtn = view.findViewById(R.id.serBtn);

    }

}