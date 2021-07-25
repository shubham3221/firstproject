package com.example.s_tools.entertainment.Fragemnts.movies.sectionheader.Detail_Act;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s_tools.R;
import com.example.s_tools.Splash_after_reg;
import com.example.s_tools.Splash_login_reg.SplashScreen;
import com.example.s_tools.chatting.ChatKaro;
import com.example.s_tools.testing.DownloadActivity;
import com.example.s_tools.tools.Cvalues;
import com.example.s_tools.tools.ToastMy;
import com.example.s_tools.tools.retrofitcalls.MySharedPref;
import com.example.s_tools.user_request.UserRequestActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LFragment extends Fragment {
    ListView gdlist;
    View view;
    List<String> drive720, drive1080, others,watchList;
    ArrayList<Item> urls;
    CountryAdapter adapter;
    TextView postidbtn, directDownload, watchBtn,linknotworking;
    Button request;
    int postid;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    private String anonLink;


    public LFragment(){}

    public LFragment(int postid, List<String> drive720, List<String> drive1080, List<String> otherlinks,List<String> watchonlineList) {
        this.postid=postid;
        this.drive720=drive720;
        this.drive1080=drive1080;
        this.others=otherlinks;
        this.watchList = watchonlineList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_l, container, false);
        adsetup();
        gdlist=(ListView) view.findViewById(R.id.gdlv);
        request=view.findViewById(R.id.reqbtn);
        postidbtn=view.findViewById(R.id.reqbtn2);
        directDownload=view.findViewById(R.id.downloadbtn);
        watchBtn=view.findViewById(R.id.watchonlinebtn);
        linknotworking=view.findViewById(R.id.linknotworking);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        bottom_sheet=view.findViewById(R.id.bottom_sheet);
        sheetBehavior=BottomSheetBehavior.from(bottom_sheet);
        bottom_sheet.setVisibility(View.GONE);

        bottom_sheet.setOnClickListener(view -> {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        linknotworking.setOnClickListener(v -> {
            bottom_sheet.setVisibility(View.VISIBLE);
        });

        new MyasynTask().execute();

        return view;
    }

    class MyasynTask extends AsyncTask<Void, Void, Void> {

        public static final String ID="id: ";
        public static final String DRIVE_LINKS_720="Google Drive Links 720p";
        public static final String DRIVE_LINKS_1080="Google Drive Links 1080p";
        public static final String OTHERS="Available Download Links";

        @Override
        protected Void doInBackground(Void... voids) {
            urls=new ArrayList<>();
            if (drive720 != null) {
                // Header
                urls.add(new SectionItem(DRIVE_LINKS_720));
                if (drive720.get(0).contains(",")) {
                    String[] split=drive720.get(0).split(",");
                    for (String link : split) {
                        urls.add(new EntryItem(link.trim()));
                    }

                } else {
                    urls.add(new EntryItem(drive720.get(0).trim()));
                }
            }
            if (drive1080 != null) {
                // Header
                urls.add(new SectionItem(DRIVE_LINKS_1080));
                if (drive1080.get(0).contains(",")) {
                    String[] split=drive1080.get(0).split(",");
                    for (String link : split) {
                        urls.add(new EntryItem(link.trim()));
                    }

                } else {
                    urls.add(new EntryItem(drive1080.get(0).trim()));
                }
            }

            //other
            if (others != null) {
                if (others.get(0).contains(",")) {
                    String[] split=others.get(0).split(",");
                    urls.add(new SectionItem(OTHERS + " (" + split.length + ")"));
                    for (String link : split) {
                        if (link.contains("anonfiles")) {
                            anonLink=link;
                        }
                        urls.add(new EntryItem(link.trim()));
                    }
                } else {
                    if (others.get(0).contains("anonfiles")) {
                        anonLink=others.get(0);
                    }
                    urls.add(new SectionItem(OTHERS));
                    urls.add(new EntryItem(others.get(0).trim()));
                }
            }
            adapter=new CountryAdapter(getActivity(), urls);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            gdlist.setAdapter(adapter);
            if (watchList!=null){
                bottom_sheet_download();
            }else if (anonLink != null) {
                directDownload.setVisibility(View.VISIBLE);
                watchBtn.setVisibility(View.VISIBLE);
                directDownload.setOnClickListener(v -> {
                    fetch_direct_download_link(anonLink);
                });
                watchBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), UserRequestActivity.class);
                    intent.putExtra("directdownloadlink",anonLink);
                    startActivity(intent);
                });
            }
            request.setOnClickListener(view -> {
                if (MySharedPref.isSharedPrefnull(getActivity())) {
                    startActivity(new Intent(getActivity(), SplashScreen.class));
                } else {
                    startActivity(new Intent(getActivity(), ChatKaro.class));
                }
            });
            postidbtn.setText(ID + postid);
            request.setText("Request New Links");
        }
    }

    private void bottom_sheet_download() {
        directDownload.setVisibility(View.VISIBLE);
        watchBtn.setVisibility(View.VISIBLE);

        View sheet = view.findViewById(R.id.recyclerBottomsheet);
        sheet.setVisibility(View.VISIBLE);
        BottomSheetBehavior sheetBehavior2;
        sheetBehavior2=BottomSheetBehavior.from(sheet);

        sheet.setOnClickListener(view -> {
            if (sheetBehavior2.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        directDownload.setOnClickListener(v -> {
            if (sheetBehavior2.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        watchBtn.setOnClickListener(v -> {
            if (sheetBehavior2.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.watchdownrecycler);
        String[] split=watchList.get(0).split(",");
        List<String> list=new ArrayList<>();

        for (int i=1; i<split.length; i++){
            list.add(split[i]);
        }

        BottomsheetAdapter bottomsheetAdapter = new BottomsheetAdapter(getActivity(),list,split[0]);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(bottomsheetAdapter);
    }

    private void fetch_direct_download_link(String mdirectDownloadLink) {
        KProgressHUD show=KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Fetching Download Link").setCancellable(false).setDimAmount(0.5f).show();
        new Thread(() -> {
            Document document=null;
            try {
                Log.e("//", "fetch_direct_download_link: "+mdirectDownloadLink );
                document=Jsoup.connect(mdirectDownloadLink).userAgent(Cvalues.USER_AGENT).referrer("http://www.google.co.in").ignoreContentType(true).get();
                Log.e("//", "fetch_direct_download_link:2 "+mdirectDownloadLink );
                String href=document.getElementsByClass("btn btn-primary btn-block").attr("href");
                getActivity().runOnUiThread(() -> {
                    show.dismiss();
                    if (href !=null){
//                    String replace=href.replace(" ", "%");
                        Intent intent = new Intent(getActivity(), DownloadActivity.class);
                        intent.putExtra(DownloadActivity.INTENTDOWNLINK,href);
                        startActivity(intent);
                    }
                    else {
                        ToastMy.errorToast(getActivity(), Cvalues.ERROR,ToastMy.LENGTH_SHORT);
                    }
                });
            } catch (IOException e) {
                getActivity().runOnUiThread(() -> {
                    show.dismiss();
                    ToastMy.errorToast(getActivity(),"Sorry! Direct link is expired!",ToastMy.LENGTH_SHORT);
                    directDownload.setVisibility(View.GONE);
                    watchBtn.setVisibility(View.GONE);
                });
                e.printStackTrace();
            }


        }).start();
    }

    public interface Item {
        boolean isSection();

        String getTitle();
    }

    public class SectionItem implements Item {
        private final String title;

        public SectionItem(String title) {
            this.title=title;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public boolean isSection() {
            return true;
        }
    }

    public class EntryItem implements Item {
        public final String title;

        public EntryItem(String title) {
            this.title=title;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public boolean isSection() {
            return false;
        }
    }


    public class CountryAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Item> item;

        public CountryAdapter() {
            super();
        }

        public CountryAdapter(Context context, ArrayList<Item> item) {
            this.context=context;
            this.item=item;
            //this.originalItem = item;
        }

        @Override
        public int getCount() {
            return item.size();
        }

        @Override
        public Object getItem(int position) {
            return item.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (item.get(position).isSection()) {
                // if section header
                convertView=inflater.inflate(R.layout.header_gd, parent, false);
                TextView tvSectionTitle=(TextView) convertView.findViewById(R.id.textSeparator);
                tvSectionTitle.setText(((SectionItem) item.get(position)).getTitle());

            } else {
                // if item
                convertView=inflater.inflate(R.layout.listview_gd, parent, false);
                TextView tvItemTitle=(TextView) convertView.findViewById(R.id.gdtxt);
                tvItemTitle.setText(((EntryItem) item.get(position)).getTitle());
                tvItemTitle.setOnClickListener(view -> {
//                    android.content.ClipboardManager clipboard=(android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
//                    android.content.ClipData clip=android.content.ClipData.newPlainText(item.get(position).getTitle(), item.get(position).getTitle());
//                    clipboard.setPrimaryClip(clip);
//                    ToastMy.successToast(getActivity(), Cvalues.LINK_COPY, ToastMy.LENGTH_SHORT);
                    if (URLUtil.isValidUrl(item.get(position).getTitle())){
                        Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(item.get(position).getTitle()));
                        startActivity(browserIntent);

                    }
                });
            }

            return convertView;
        }
    }

    private void adsetup() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            AdView adView=new AdView(getActivity());

            adView.setAdSize(AdSize.BANNER);

            adView.setAdUnitId(Cvalues.banr);
            adView=view.findViewById(R.id.adView);
            adView.setVisibility(View.VISIBLE);
            AdRequest adRequest=new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        },150);
    }
}