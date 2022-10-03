package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.activity.MainActivity;
import com.daffodils.psychiatry.adapter.SliderAdapter;
import com.daffodils.psychiatry.helper.ApiConfig;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.helper.VolleyCallback;
import com.daffodils.psychiatry.ui.CircleImageView;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import static android.content.Context.INPUT_METHOD_SERVICE;

public class HomeFragment extends Fragment {

    Activity activity;
    NestedScrollView nestedScrollView;
    RelativeLayout progressBar;
    SwipeRefreshLayout swipeLayout;
    View root;
    int timerDelay = 0, timerWaiting = 0;
    public static Integer [] images = {R.drawable.daff_banner12,R.drawable.daff_banner5, R.drawable.daff_banner6, R.drawable.daff_banner7, R.drawable.daff_banner8, R.drawable.daff_banner9, R.drawable.daff_banner10, R.drawable.daff_banner11};

    private ViewPager mPager;
    private LinearLayout mMarkersLayout;
    private int size = 5;
    private Timer swipeTimer;
    private Handler handler;
    private Runnable Update;
    private int currentPage = 0;
    CircleImageView imgSampleVdo, imgSubsVdo, imgPricing, imgCourses, imgWeb, imgInsta, imgFb, imgWhatsapp, imgFaculty, imgBooks;
    private LinearLayout llSampleVdo, llSubsVdo, llPricing, llCourses, llWeb, llInsta, llFB, llWhatsapp, llFaculty, llBooks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.home_fragment, container, false);
        timerDelay = 3000;
        timerWaiting = 3000;

        activity = getActivity();
        setHasOptionsMenu(true);

        progressBar = root.findViewById(R.id.progressBar);
        swipeLayout = root.findViewById(R.id.swipeLayout);
        llPricing = root.findViewById(R.id.llPricing);
        llCourses = root.findViewById(R.id.llCourses);
        llSampleVdo = root.findViewById(R.id.llSampleVdo);
        llSubsVdo = root.findViewById(R.id.llSubsVdo);
        llFaculty = root.findViewById(R.id.llFaculty);
        llBooks = root.findViewById(R.id.llBooks);
        llWeb = root.findViewById(R.id.llWeb);
        llInsta = root.findViewById(R.id.llInsta);
        llFB = root.findViewById(R.id.llFB);
        llWhatsapp = root.findViewById(R.id.llWhatsapp);

        imgPricing = root.findViewById(R.id.imgPricing);
        imgCourses = root.findViewById(R.id.imgCourses);
        imgSampleVdo = root.findViewById(R.id.imgSampleVdo);
        imgSubsVdo = root.findViewById(R.id.imgSubscribeVdo);
        imgFaculty = root.findViewById(R.id.imgFaculty);
        imgBooks = root.findViewById(R.id.imgBooks);
        imgWeb = root.findViewById(R.id.imgWeb);
        imgInsta = root.findViewById(R.id.imgInsta);
        imgFb = root.findViewById(R.id.imgFB);
        imgWhatsapp = root.findViewById(R.id.imgWhatsapp);

        imgSampleVdo.setDefaultImageResId(R.drawable.sample_vdo);
        imgSubsVdo.setDefaultImageResId(R.drawable.subscribe_vdo);
        imgCourses.setDefaultImageResId(R.drawable.courses);
        imgPricing.setDefaultImageResId(R.drawable.pricing);
        imgFaculty.setDefaultImageResId(R.drawable.faculty);
        imgBooks.setDefaultImageResId(R.drawable.book);
        imgWeb.setDefaultImageResId(R.drawable.weblink);
        imgInsta.setDefaultImageResId(R.drawable.instagram1);
        imgFb.setDefaultImageResId(R.drawable.facebook);
        imgWhatsapp.setDefaultImageResId(R.drawable.whatsapp);

        nestedScrollView = root.findViewById(R.id.nestedScrollView);
        mMarkersLayout = root.findViewById(R.id.layout_markers);
        mPager = root.findViewById(R.id.pager);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                ApiConfig.addMarkers(position, images, mMarkersLayout, getContext());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipeTimer != null) {
                    swipeTimer.cancel();
                }
                if (AppController.isConnected(getActivity())) {
                    GetSlider();
                }
                swipeLayout.setRefreshing(false);
            }
        });

        llSampleVdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FreeSampleVideosFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "SampleVdos");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
            }
        });

        llSubsVdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // getSubscribedModuleID();

                if(GlobalConst.User_id.equals("")){
                    Toast.makeText(activity, "Please register yourself to view subscribed videos", Toast.LENGTH_LONG).show();
                } else {
                    Fragment fragment = new SubscribedVideosFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "SubsVdo");
                    fragment.setArguments(bundle);
                    MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
                }
            }
        });

        llCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new CoursesFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "Courses");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

            }
        });

        llPricing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new FeeStructureFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "FeeStructure");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

            }
        });

        llFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new FacultyFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "Faculty");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();


            }
        });

        llBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new ReferenceBooksFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "Books");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

            }
        });

        llWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://daffodilspsychiatry.com/"));
                startActivity(browserIntent);
            }
        });

        llFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.facebook.com/PsychMasterClasses"));
                startActivity(browserIntent);
            }
        });

        llInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/invites/contact/?i=1df9rp4qkcfxu&utm_content=m8e8yli"));
                startActivity(browserIntent);
            }
        });

        llWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Please enter your query here : ";
                String mobileNumber = "7528920011";
              //  boolean installed = AppController.appInstalledOrNot("com.whatsapp");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+91"+mobileNumber + "&text="+message));
                startActivity(intent);
            }
        });


        if (AppController.isConnected(getActivity())) {
            GetSlider();
        }

        return root;
    }

    private void GetSlider() {

        SliderAdapter viewPagerAdapter = new SliderAdapter(getContext());
        mPager.setAdapter(viewPagerAdapter);

        //  mPager.setAdapter(new SliderAdapter(sliderArrayList, getActivity(), R.layout.lyt_slider, "home"));
        ApiConfig.addMarkers(0, images, mMarkersLayout, getContext());
        handler = new Handler();
        Update = new Runnable() {
            public void run() {
                if (currentPage == size) {
                    currentPage = 0;
                }
                try {
                    mPager.setCurrentItem(currentPage++, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, timerDelay, timerWaiting);
    }



    @Override
    public void onResume() {
        super.onResume();
        GlobalConst.TOOLBAR_TITLE = getString(R.string.app_name);
        getActivity().invalidateOptionsMenu();
        hideKeyboard();
    }

    public void hideKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(root.getApplicationWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


