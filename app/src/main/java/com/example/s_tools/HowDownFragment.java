package com.example.s_tools;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.tools.PhotoFullScreen.PhotoFullPopupWindow;
import com.squareup.picasso.Picasso;

public class HowDownFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    Adapter adapter;

    public HowDownFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_how_down, container, false);
        recyclerView = view.findViewById(R.id.hdreccc);
        adapter = new Adapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }
    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{
        String[] imges = {"https://imagetot.com/images/2020/11/13/ed8af85aa99022d890915bad5a086df4.png",
        "https://imagetot.com/images/2020/11/13/eb60476663eecc5d6ddce410f1dfa841.png","https://imagetot.com/images/2020/11/13/de7c98e34899014c49246be483a69bf9.png"
        ,"https://imagetot.com/images/2020/11/13/1b7447228a2ef57239f5aa6104a93c56.png","https://imagetot.com/images/2020/11/13/eb60476663eecc5d6ddce410f1dfa841.png"
        ,"https://imagetot.com/images/2020/11/13/564ef8c3eb10da40a00af4e07c5fdfee.png","https://imagetot.com/images/2020/11/13/7e6875c6a5bdca08692e847e9ba5760d.png"
        ,"https://imagetot.com/images/2020/11/13/148c693887ea44f5d86297799886198e.png","https://imagetot.com/images/2020/11/13/948c429a91cfedb9f819ed90404f446e.png"};
        String[] titles={"This Tutorial only for Google Drive Downloads.","Click on Download Button.","If you are not logged in you will have to sign in with google account.because this file is going to copy in your google drive storage."
        ,"CLick allow button.","Now you are signed in. click download button","if Your google drive if full then this page is not going to show.s if you get error then goto google drive and empty your google drive from bin folder."
        ,"Now open google drive app. new folder Sharer.pw will automatically created .","Open this folder you will get your file init.","Now you can download your file. (Tip: After watching movie ,please delete from google drive and also remove the fle from bin folder. By doing this you will not get file storage full error in Sharer.pw Website.Thankyou)"};

        @RequiresApi(api=Build.VERSION_CODES.M)
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate=LayoutInflater.from(getActivity()).inflate(R.layout.custom_howtodown, parent, false);
            return new MyViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.title.setText(titles[position]);
            holder.num.setText(String.valueOf(position+1));
            Picasso.get().load(imges[position]).placeholder(R.drawable.ic_baseline_image_24).resize(300,500).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return imges.length;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView num,title;
            ImageView imageView;
            @RequiresApi(api=Build.VERSION_CODES.M)
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                num = itemView.findViewById(R.id.hpnum);
                title = itemView.findViewById(R.id.hptitle);
                imageView = itemView.findViewById(R.id.hoimg);
                imageView.setOnClickListener(view -> {
                    new PhotoFullPopupWindow(getActivity(), R.layout.popup_photo_full, imageView, imges[getAdapterPosition()], null);
                });
            }
        }
    }
}