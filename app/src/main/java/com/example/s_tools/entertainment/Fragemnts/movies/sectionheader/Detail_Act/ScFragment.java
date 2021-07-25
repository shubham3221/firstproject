package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.Detail_Act;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.tools.PhotoFullScreen.PhotoFullPopupWindow;
import com.squareup.picasso.Picasso;

public class ScFragment extends Fragment {
    RecyclerView recyclerView;
    View view;
    Myadapter myadapter;
    String[] list;
    HideToolbar hideToolbar;

    public ScFragment(String[] split) {
        this.list = split;
        // Required empty public constructor
    }
    public ScFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_my, container, false);
        recyclerView = view.findViewById(R.id.screcy);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myadapter = new Myadapter();
        recyclerView.setAdapter(myadapter);
        hideToolbar =(HideToolbar) getActivity();
        return view;
    }
    class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder>{

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view =LayoutInflater.from(getActivity()).inflate(R.layout.imagelayout,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            if (list[position]!=null){
                Picasso.get().load(list[position].trim()).resize(600,600).centerInside().into(holder.imageView);
            }
        }

        @Override
        public int getItemCount() {
            return list.length;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.mimage);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api=Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        new PhotoFullPopupWindow(getActivity(), R.layout.popup_photo_full, imageView, list[getAdapterPosition()].trim(), null);
                    }
                });
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideToolbar.hideToolbar();
    }

    @Override
    public void onDetach() {
        hideToolbar.showToolbar();
        super.onDetach();
    }

    public interface HideToolbar{
        void hideToolbar();
        void showToolbar();
    }
}